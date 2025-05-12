package fr.diginamic.services;

import fr.diginamic.dao.TirsButsDaoImpl;
import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import fr.diginamic.model.TirsButs;
import fr.diginamic.utils.ErreurCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Classe de test pour tester TirsButsService avec Junit & Mockito
 */
public class TestTirsButsService {

    private TirsButsDaoImpl tirsButsDaoMock;
    private ErreurCollector collectorMock;
    private TirsButsService tirsButsService;

    private Match match;
    private Equipe equipe;
    private TirsButs tirsButs;

    @BeforeEach
    public void setUp() {
        tirsButsDaoMock = mock(TirsButsDaoImpl.class);
        collectorMock = mock(ErreurCollector.class);
        tirsButsService = new TirsButsService(null, collectorMock);
        tirsButsService.setTirsDao(tirsButsDaoMock);

        match = new Match();
        match.setId(1);
        equipe = new Equipe();
        equipe.setId(10);
        tirsButs = new TirsButs(match, equipe, equipe);
    }

    @Test
    public void enregistrerSiNouveau_NouveauTirsButs() {
        //GIVEN
        when(tirsButsDaoMock.findByMatchAndVainqueur(1, 10))
                .thenReturn(Collections.emptyList());
        //WHEN
        tirsButsService.enregistrerTirsButsSiNouveau(match, equipe, equipe, "ligne test", "fichier test");
        //THEN
        verify(tirsButsDaoMock).insert(argThat(tb ->
                tb.getMatch().getId() == 1 &&
                tb.getEquipeCommence().getId() == 10 &&
                tb.getVainqueur().getId() == 10
                ));
        verify(collectorMock, never()).log(any(), any(), any(), any());
    }

    @Test
    public void enregistrerSiNouveau_doublon_logError() {
        //GIVEN
        when(tirsButsDaoMock.findByMatchAndVainqueur(1, 10))
                .thenReturn(List.of(tirsButs, new TirsButs()));
        //WHEN
        tirsButsService.enregistrerTirsButsSiNouveau(match, equipe, equipe, "ligne test", "fichier test");
        // THEN
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("plusieurs tirs aux buts"),
                eq("TirsButs")
        );
        verify(tirsButsDaoMock, never()).insert(any());
    }

    @Test
    public void testVainqueurInvalide_LogErreur() {
        //WHEN (equipeCommence peut être null mais vainqueur doit être indiqué)
        tirsButsService.enregistrerTirsButsSiNouveau(match, null, null, "ligne test", "fichier test");
        //THEN
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("vainqueur null"),
                eq("TirsButs")
        );
        verify(tirsButsDaoMock, never()).insert(any());
    }

    @Test
    public void testMatchInvalide_LogErreur() {
        //WHEN
        tirsButsService.enregistrerTirsButsSiNouveau(null, equipe, equipe, "ligne test", "fichier test");
        //THEN
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("match ou vainqueur null"),
                eq("TirsButs")
        );
        verify(tirsButsDaoMock, never()).insert(any());
    }
}
