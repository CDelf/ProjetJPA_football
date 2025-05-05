package fr.diginamic.model;

import jakarta.persistence.*;

/**
 * Entité représentant le score d'un match de football,
 * d'une part celui de l'équipe hôte, d'autre part celui de l'équipe invitée
 */
@Entity
@Table(name= "score")
public class Score {

    @Id
    private int matchId;
    /**
     * Relation oneToOne avec match
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(name="score_hote")
    private int scoreHote;

    @Column(name="score_invite")
    private int scoreInvite;

    /**
     * Constructeur vide pour JPA
     */
    public Score() {
    }

    /**
     * Constructeur pour créer un score
     */
    public Score(Match match, int scoreHote, int scoreInvite) {
        this.match = match;
        this.scoreHote = scoreHote;
        this.scoreInvite = scoreInvite;
    }

    /**
     * Optimise l'affichage des attributs de l'instance
     *
     * @return les attributs de l'instance.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Score{");
        sb.append("matchId=").append(matchId);
        sb.append(", match=").append(match);
        sb.append(", scoreHote=").append(scoreHote);
        sb.append(", scoreInvite=").append(scoreInvite);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
     *
     * @return matchId
     */
    public int getMatchId() {
        return matchId;
    }

    /**
     * Getter
     * @return match
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Setter
     * @param match match
     */
    public void setMatch(Match match) {
        this.match = match;
    }

    /**
     * Getter
     * @return scoreHote
     */
    public int getScoreHote() {
        return scoreHote;
    }

    /**
     * Setter
     * @param scoreHote scoreHote
     */
    public void setScoreHote(int scoreHote) {
        this.scoreHote = scoreHote;
    }

    /**
     * Getter
     * @return scoreInvite
     */
    public int getScoreInvite() {
        return scoreInvite;
    }

    /**
     * Setter
     * @param scoreInvite scoreInvite
     */
    public void setScoreInvite(int scoreInvite) {
        this.scoreInvite = scoreInvite;
    }
}
