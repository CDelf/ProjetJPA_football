package fr.diginamic.services;

import fr.diginamic.dao.EquipeDaoImpl;
import fr.diginamic.model.Equipe;
import fr.diginamic.utils.ErreurCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour tester EquipeService avec Junit & Mockito
 */
public class TestEquipeService {

    private EquipeDaoImpl equipeDaoMock;
    private ErreurCollector collectorMock;
    private EquipeService equipeService;

    private Equipe brazil;

    @BeforeEach
    public void setUp() {
        equipeDaoMock = mock(EquipeDaoImpl.class);
        collectorMock = mock(ErreurCollector.class);
        equipeService = new EquipeService(null, collectorMock);
        equipeService.setEquipeDao(equipeDaoMock);

        brazil = new Equipe("Brazil");
        brazil.setId(10);
    }

    @Test
    public void testGetByNom_existant() {
        // GIVEN
        when(equipeDaoMock.findByName("Brazil")).thenReturn(Collections.singletonList(brazil));
        // WHEN
        Equipe result = equipeService.getByNom("Brazil");
        // THEN
        assertNotNull(result);
        assertEquals("Brazil", result.getNom());
        verify(equipeDaoMock).findByName("Brazil");
    }

    @Test
    public void testGetByNom_inexistant() {
        // GIVEN
        when(equipeDaoMock.findByName("PSG")).thenReturn(Collections.emptyList());
        // WHEN
        Equipe result = equipeService.getByNom("PSG");
        // THEN
        assertNull(result);
        verify(equipeDaoMock).findByName("PSG");
    }

    @Test
    public void testEnregistrerEquipeSiNouvelle_Doublon_LogErreur() {
        // GIVEN – simulons un doublon : 2 équipes trouvées
        when(equipeDaoMock.findByName("Brazil"))
                .thenReturn(List.of(brazil, new Equipe()));

        // WHEN – appel de la méthode à tester
        equipeService.enregistrerEquipeSiNouvelle("Brazil", "ligne test", "fichier test");

        // THEN – vérifie que le log a bien été appelé
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("plusieurs équipes"),
                eq("Equipe")
        );
        // Et s'assure qu'aucune insertion n'est faite
        verify(equipeDaoMock, never()).insert(any());
    }

    @Test
    public void testEnregistrerSiNouvelle_NouvelleEquipe() {
        // GIVEN – aucune équipe trouvée
        when(equipeDaoMock.findByName("Japan")).thenReturn(Collections.emptyList());

        // WHEN
        equipeService.enregistrerEquipeSiNouvelle("Japan", "ligne test", "fichier test");

        // THEN – vérifie qu’une insertion est bien faite
        verify(equipeDaoMock).insert(argThat(e -> e.getNom().equals("Japan")));

        // Et aucun log d'erreur
        verify(collectorMock, never()).log(any(), any(), any(), any());
    }

    @Test
    public void testNomInvalide_LogErreur() {
        // WHEN
        equipeService.enregistrerEquipeSiNouvelle(" ", "ligne test", "fichier test");

        // THEN
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("vide ou invalide"),
                eq("Equipe")
        );

        verify(equipeDaoMock, never()).insert(any());
    }

}
