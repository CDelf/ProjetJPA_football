package fr.diginamic.services;

import fr.diginamic.dao.MatchDaoImpl;
import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import fr.diginamic.utils.ErreurCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour tester MatchService avec Junit & Mockito
 */
public class TestMatchService {

    private MatchDaoImpl matchDaoMock;
    private ErreurCollector collectorMock;
    private MatchService matchService;

    private Equipe france;
    private Equipe argentine;
    private Match match;

    @BeforeEach
    public void setUp() {
        matchDaoMock = mock(MatchDaoImpl.class);
        collectorMock = mock(ErreurCollector.class);
        matchService = new MatchService(null, collectorMock);
        matchService.setMatchDao(matchDaoMock);

        france = new Equipe("France");
        france.setId(1);
        argentine = new Equipe("Argentina");
        argentine.setId(2);
        match = new Match(LocalDate.of(2022, 12, 18),"Doha", "Qatar", false, "World Cup", france, argentine);
        match.setId(100);
    }

    @Test
    public void testGetByDateAndEquipes_MatchExistant() {
        // GIVEN
        when(matchDaoMock.findByDateAndEquipes(match.getDate(), france.getId(), argentine.getId()))
                .thenReturn(List.of(match));
        // WHEN
        Match result = matchService.getByDateAndEquipes(match.getDate(), france, argentine);
        // THEN
        assertNotNull(result);
        assertEquals(match.getId(), result.getId());
        verify(matchDaoMock).findByDateAndEquipes(match.getDate(), france.getId(), argentine.getId());
    }

    @Test
    public void testGetByDateAndEquipes_AucunMatch() {
        // GIVEN
        when(matchDaoMock.findByDateAndEquipes(match.getDate(), france.getId(), argentine.getId()))
                .thenReturn(Collections.emptyList());
        // WHEN
        Match result = matchService.getByDateAndEquipes(match.getDate(), france, argentine);
        // THEN
        assertNull(result);
        verify(matchDaoMock).findByDateAndEquipes(match.getDate(), france.getId(), argentine.getId());
    }

    @Test
    public void testEnregistrerSiNouveau_NouveauMatch() {
        // GIVEN – aucun match existant
        when(matchDaoMock.findByDateAndEquipes(match.getDate(), france.getId(), argentine.getId()))
                .thenReturn(Collections.emptyList());

        // WHEN
        matchService.enregistrerMatchSiNouveau(
                match.getDate(), match.getVille(), match.getPays(),
                match.isLieuNeutre(), match.getTournoi(),
                france, argentine, "ligne test", "fichier test"
        );

        // THEN – vérifie que l’insertion est faite
        verify(matchDaoMock).insert(argThat(m ->
                m.getDate().equals(match.getDate()) &&
                        m.getEquipeHote().equals(france) &&
                        m.getEquipeInvitee().equals(argentine)
        ));

        // Aucun log d'erreur attendu
        verify(collectorMock, never()).log(any(), any(), any(), any());
    }

    @Test
    public void testEnregistrerSiNouveau_Doublon_LogErreur() {
        // GIVEN – simule un doublon en retournant une liste de 2 matchs
        when(matchDaoMock.findByDateAndEquipes(
                match.getDate(), france.getId(), argentine.getId()))
                .thenReturn(List.of(match, new Match()));

        // WHEN
        matchService.enregistrerMatchSiNouveau(
                match.getDate(), match.getVille(), match.getPays(),
                match.isLieuNeutre(), match.getTournoi(),
                france, argentine, "ligne test", "fichier test"
        );

        // THEN – erreur loguée
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("plusieurs matchs"),
                eq("Match")
        );

        // Aucun insert
        verify(matchDaoMock, never()).insert(any());
    }

    @Test
    public void testDateInvalide_LogErreur() {
        // WHEN – date nulle, ce qui est invalide
        matchService.enregistrerMatchSiNouveau(
                null, match.getVille(), match.getPays(),
                match.isLieuNeutre(), match.getTournoi(),
                france, argentine, "ligne test", "fichier test"
        );

        // THEN – log erreur attendu
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("date invalide"),
                eq("Match")
        );
        verify(matchDaoMock, never()).insert(any());
    }
    @Test
    public void testEquipeInvalide_LogErreur() {
        // WHEN – date nulle, ce qui est invalide
        matchService.enregistrerMatchSiNouveau(
                match.getDate(), match.getVille(), match.getPays(),
                match.isLieuNeutre(), match.getTournoi(),
                null, argentine, "ligne test", "fichier test"
        );

        // THEN – log erreur attendu
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("équipe hôte/invitée absente"),
                eq("Match")
        );
        verify(matchDaoMock, never()).insert(any());
    }

    @Test
    public void testVilleEtPaysInvalide_LogErreur() {
        // WHEN – date nulle, ce qui est invalide
        matchService.enregistrerMatchSiNouveau(
                match.getDate(), "", "",
                match.isLieuNeutre(), match.getTournoi(),
                null, argentine, "ligne test", "fichier test"
        );

        // THEN – log erreur attendu
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("Erreur dans les paramètres"),
                eq("Match")
        );
        verify(matchDaoMock, never()).insert(any());
    }
}
