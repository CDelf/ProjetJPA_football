package fr.diginamic.services;

import fr.diginamic.dao.ButeurDaoImpl;
import fr.diginamic.model.Buteur;
import fr.diginamic.model.Equipe;
import fr.diginamic.utils.ErreurCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour tester ButeurService avec Junit & Mockito
 */
public class TestButeurService {

    private ButeurDaoImpl buteurDaoMock;
    private ErreurCollector collectorMock;
    private ButeurService buteurService;

    private Equipe france;
    private Buteur mbappe;

    @BeforeEach
    public void setUp() {
        buteurDaoMock = mock(ButeurDaoImpl.class);
        collectorMock = mock(ErreurCollector.class);
        buteurService = new ButeurService(null, collectorMock);
        buteurService.setButeurDao(buteurDaoMock);

        france = new Equipe("France");
        france.setId(1);
        mbappe = new Buteur("Mbappé", france);
    }

    @Test
    public void testGetByNomAndEquipe_existant() {
        //GIVEN
        when(buteurDaoMock.findByNomAndEquipe(mbappe.getNom(), france.getId()))
                .thenReturn(List.of(mbappe));
        //WHEN
        Buteur result = buteurService.getByNomAndEquipe("Mbappé", france);
        //THEN
        assertNotNull(result);
        assertEquals("Mbappé", result.getNom());
        assertEquals(1, mbappe.getEquipe().getId());
        verify(buteurDaoMock).findByNomAndEquipe("Mbappé", france.getId());
    }

    @Test
    public void testGetByNomAndEquipe_inexistant() {
        //GIVEN
        when(buteurDaoMock.findByNomAndEquipe(mbappe.getNom(), france.getId()))
                .thenReturn(Collections.emptyList());
        //WHEN
        Buteur result = buteurService.getByNomAndEquipe("Mbappé", france);
        //THEN
        assertNull(result);
        verify(buteurDaoMock).findByNomAndEquipe(mbappe.getNom(), france.getId());
    }

    @Test
    public void enregistrerSiNouveau_nouveauButeur() {
        //GIVEN
        when(buteurDaoMock.findByNomAndEquipe(mbappe.getNom(), france.getId()))
                .thenReturn(Collections.emptyList());
        //WHEN
        buteurService.enregistrerButeurSiNouveau(mbappe.getNom(), france, "ligne test", "fichier test");
        //THEN
        verify(buteurDaoMock).insert(argThat(b ->
                b.getNom().equals(mbappe.getNom()) &&
                        b.getEquipe().getNom().equals(france.getNom())));
        // Aucun log d'erreur attendu
        verify(collectorMock, never()).log(any(), any(), any(), any());
    }

    @Test
    public void enregistrerSiNouveau_doublon_LogErreur() {
        //GIVEN
        when(buteurDaoMock.findByNomAndEquipe(mbappe.getNom(), france.getId()))
                .thenReturn(List.of(mbappe, new Buteur()));
        //WHEN
        buteurService.enregistrerButeurSiNouveau(mbappe.getNom(), france, "ligne test", "fichier test");
        // THEN – erreur loguée
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("plusieurs buteurs"),
                eq("Buteur")
        );
        // Aucun insert
        verify(buteurDaoMock, never()).insert(any());
    }

    @Test
    public void testNomButeurInvalide_LogErreur() {
        // WHEN – nom null invalide
        buteurService.enregistrerButeurSiNouveau(
                " ", france, "ligne test", "fichier test"
        );

        // THEN – log erreur attendu
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("nom vide/invalide"),
                eq("Buteur")
        );

        verify(buteurDaoMock, never()).insert(any());
    }

    @Test
    public void testEquipeInvalide_LogErreur() {
        // WHEN – nom null invalide
        buteurService.enregistrerButeurSiNouveau(
                mbappe.getNom(), null, "ligne test", "fichier test"
        );

        // THEN – log erreur attendu
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("équipe null"),
                eq("Buteur")
        );

        verify(buteurDaoMock, never()).insert(any());
    }
}
