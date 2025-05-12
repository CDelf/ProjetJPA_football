package fr.diginamic.services;

import fr.diginamic.dao.ButDaoImpl;
import fr.diginamic.model.But;
import fr.diginamic.model.Buteur;
import fr.diginamic.model.Match;
import fr.diginamic.utils.ErreurCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Classe de test pour tester ButService avec Junit & Mockito
 */
public class TestButService {

    private ButDaoImpl butDaoMock;
    private ErreurCollector collectorMock;
    private ButService butService;

    private Match match;
    private Buteur buteur;

    @BeforeEach
    public void setUp() {
        butDaoMock = mock(ButDaoImpl.class);
        collectorMock = mock(ErreurCollector.class);
        butService = new ButService(null, collectorMock);
        butService.setButDao(butDaoMock);

        match = new Match();
        match.setId(1);
        buteur = new Buteur();
        buteur.setId(10);
    }

    @Test
    public void testEnregistrerButSiNouveau_nouveauBut(){
        // GIVEN
        when(butDaoMock.findByMatchButeurAndMinute(1, 10, 45)).thenReturn(Collections.emptyList());
        // WHEN
        butService.enregistrerButSiNouveau(45, false, false, match, buteur, "ligne test", "fichier test");
        // THEN
        verify(butDaoMock).insert(argThat(b -> b.getMinute() == 45 && !b.isContreSonCamp()));
        verify(collectorMock, never()).log(any(), any(), any(), any());
    }

    @Test
    public void testEnregistrerButSiNouveau_doublon_logErreur() {
        // GIVEN
        But but = new But();
        but.setMinute(45);
        when(butDaoMock.findByMatchButeurAndMinute(1, 10, 45)).thenReturn(List.of(but, new But()));

        // WHEN
        butService.enregistrerButSiNouveau(45, false, false, match, buteur, "ligne test", "fichier test");

        // THEN
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("plusieurs buts"),
                eq("But")
        );
        verify(butDaoMock, never()).insert(any());
    }

    @Test
    public void testMinutageInvalide_LogErreur() {
        // WHEN – minute négative
        butService.enregistrerButSiNouveau(-45, false, false, match, buteur, "ligne test", "fichier test");

        // THEN
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("minutage négatif"),
                eq("But")
        );
        verify(butDaoMock, never()).insert(any());
    }

    @Test
    public void testMatchInvalide_LogErreur() {
        // WHEN – minute négative
        butService.enregistrerButSiNouveau(45, false, false, null, buteur, "ligne test", "fichier test");

        // THEN
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("match ou buteur null"),
                eq("But")
        );
        verify(butDaoMock, never()).insert(any());
    }

    @Test
    public void testButeurInvalide_LogErreur() {
        // WHEN – minute négative
        butService.enregistrerButSiNouveau(45, false, false, match, null, "ligne test", "fichier test");

        // THEN
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("match ou buteur null"),
                eq("But")
        );
        verify(butDaoMock, never()).insert(any());
    }
}
