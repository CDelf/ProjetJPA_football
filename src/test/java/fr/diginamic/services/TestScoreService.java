package fr.diginamic.services;

import fr.diginamic.dao.ScoreDaoImpl;
import fr.diginamic.model.Match;
import fr.diginamic.model.Score;
import fr.diginamic.utils.ErreurCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Classe de test pour tester ScoreService avec Junit & Mockito
 */
public class TestScoreService {

    private ScoreDaoImpl scoreDaoMock;
    private ErreurCollector collectorMock;
    private ScoreService scoreService;

    private Match match;
    private Score score;

    @BeforeEach
    public void setUp() {
        scoreDaoMock = mock(ScoreDaoImpl.class);
        collectorMock = mock(ErreurCollector.class);
        scoreService = new ScoreService(null, collectorMock);
        scoreService.setScoreDao(scoreDaoMock);

        match = new Match();
        match.setId(1);
        score = new Score(match, 1, 2);
    }

    @Test
    public void enregistrerSiNouveau_NouveauScore() {
        //GIVEN
        when(scoreDaoMock.findByMatchAndScores(match.getId(), 1,2))
                .thenReturn(Collections.emptyList());
        //WHEN
        scoreService.enregistrerScoreSiNouveau(match, 1, 2, "ligne test", "fichier test");
        //THEN
        verify(scoreDaoMock).insert(argThat(s ->
                s.getMatch().getId() == match.getId() &&
                s.getScoreHote() == score.getScoreHote() &&
                score.getScoreInvite() == score.getScoreInvite()
                ));
        // Aucun log d'erreur attendu
        verify(collectorMock, never()).log(any(), any(), any(), any());
    }

    @Test
    public void enregistrerSiNouveau_doublon_logError() {
        //GIVEN
        when(scoreDaoMock.findByMatchAndScores(match.getId(), 1,2))
                .thenReturn(List.of(score, new Score()));
        //WHEN
        scoreService.enregistrerScoreSiNouveau(match, 1, 2, "ligne test", "fichier test");
        //THEN
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("plusieurs scores"),
                eq("Score")
        );
        // Aucun insert
        verify(scoreDaoMock, never()).insert(any());
    }
    @Test
    public void testEnregistrerSiNouveau_ScoreInvalide_LogErreur() {
        // WHEN – nom null invalide
        scoreService.enregistrerScoreSiNouveau(
                match, -1, 2, "ligne test", "fichier test"
        );

        // THEN – log erreur attendu
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("scores invalides"),
                eq("Score")
        );
        verify(scoreDaoMock, never()).insert(any());
    }

    @Test
    public void testEnregistrerSiNouveau_MatchInvalide_LogErreur() {
        // WHEN – nom null invalide
        scoreService.enregistrerScoreSiNouveau(
                null, 1, 2, "ligne test", "fichier test"
        );

        // THEN – log erreur attendu
        verify(collectorMock).log(
                eq("fichier test"),
                eq("ligne test"),
                contains("match null"),
                eq("Score")
        );
        verify(scoreDaoMock, never()).insert(any());
    }
}
