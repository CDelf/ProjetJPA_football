package fr.diginamic.model;

import jakarta.persistence.*;

@Entity
@Table(name="tirs_buts")
public class TirsButs {

    @Id
    private int matchId;

    /**
     * Relation oneToOne avec match
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "match_id")
    private Match match;

    /**
     * Relation ManyToOne avec Equipe
     * pour définir quelle équipe commence le tir aux buts
     */
    @ManyToOne
    @JoinColumn(name = "equipe_commence_id", nullable = true)
    private Equipe equipeCommence;

    /**
     * Relation ManyToOneavec Equipe
     * pour définir quelle équipe gagne au tir aux buts
     */
    @ManyToOne
    @JoinColumn(name="vainqueur_id")
    private Equipe vainqueur;

    /**
     * Constructeur vide pour JPA
     */
    public TirsButs() {
    }

    /**
     * Constructeur permettant de créer une séance de tirs aux buts
     * @param match durant lequel se passe le tir aux buts
     * @param equipeCommence équipe qui tire la première lors de la séance
     * @param vainqueur équipe qui remporte le tir aux buts
     */
    public TirsButs(Match match, Equipe equipeCommence, Equipe vainqueur) {
        this.match = match;
        this.equipeCommence = equipeCommence;
        this.vainqueur = vainqueur;
    }

    /**
     * Optimise l'affichage des attributs de l'instance
     *
     * @return les attributs de l'instance.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TirsButs{");
        sb.append("matchId=").append(matchId);
        sb.append(", match=").append(match);
        sb.append(", equipeCommence=").append(equipeCommence);
        sb.append(", vainqueur=").append(vainqueur);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
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
     * @return equipeCommence
     */
    public Equipe getEquipeCommence() {
        return equipeCommence;
    }

    /**
     * Setter
     * @param equipeCommence equipeCommence
     */
    public void setEquipeCommence(Equipe equipeCommence) {
        this.equipeCommence = equipeCommence;
    }

    /**
     * Getter
     * @return vainqueur
     */
    public Equipe getVainqueur() {
        return vainqueur;
    }

    /**
     * Setter
     * @param vainqueur vainqueur
     */
    public void setVainqueur(Equipe vainqueur) {
        this.vainqueur = vainqueur;
    }
}
