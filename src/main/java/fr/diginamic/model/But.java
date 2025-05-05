package fr.diginamic.model;

import jakarta.persistence.*;

import java.util.StringJoiner;

/**
 * Classe représentant l'entité but dans le cadre d'un match de football
 */
@Entity
@Table(name="but")
public class But {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int minute;

    @Column(name="contre_son_camp")
    private boolean contreSonCamp;

    private boolean penalty;

    /**
     * Relation ManyToOne avec le match durant lequel le but a été marqué
     */
    @ManyToOne
    @JoinColumn(name="match_id")
    private Match match;
    /**
     * Relation ManyToOne avec le buteur ayant marqué le but
     */
    @ManyToOne
    @JoinColumn(name="buteur_id")
    private Buteur buteur;

    /**
     * Constructeur vide pour JPA
     */
    public But() {
    }

    /**
     * Constructeur pour créer un but (par défaut contreSonCamp et Penalty sont false si non indiqués)
     * @param minute minute du match à laquelle le but a été marqué
     * @param match match durant lequel le but a été marqué
     * @param buteur buteur ayant marqué le but
     */
    public But(int minute, Match match, Buteur buteur) {
        this.minute = minute;
        this.match = match;
        this.buteur = buteur;
    }

    /**
     * Constructeur complet pour créer un but
     * @param minute minute du match à laquelle le but a été marqué
     * @param match match durant lequel le but a été marqué
     * @param buteur buteur ayant marqué le but
     */
    public But(int minute, boolean contreSonCamp, boolean penalty, Match match, Buteur buteur) {
        this.minute = minute;
        this.contreSonCamp = contreSonCamp;
        this.penalty = penalty;
        this.match = match;
        this.buteur = buteur;
    }

    /**
     * Optimise l'affichage des attributs de l'instance
     *
     * @return les attributs de l'instance.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("But{");
        sb.append("id=").append(id);
        sb.append(", minute=").append(minute);
        sb.append(", contreSonCamp=").append(contreSonCamp);
        sb.append(", penalty=").append(penalty);
        sb.append(", match=").append(match);
        sb.append(", buteur=").append(buteur);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
     * @return id du but
     */
    public int getId() {
        return id;
    }

    /**
     * Getter
     * @return minute du match à laquelle le but a été marqué
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Setter
     * @param minute minute du match à laquelle le but a été marqué
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * Getter
     * @return contreSonCamp true si le buteur a marqué contre son équipe
     */
    public boolean isContreSonCamp() {
        return contreSonCamp;
    }

    /**
     * Setter
     *
     * @param contreSonCamp contreSonCamp true si le buteur a marqué contre son équipe
     */
    public void setContreSonCamp(boolean contreSonCamp) {
        this.contreSonCamp = contreSonCamp;
    }

    /**
     * Getter
     * @return penalty : true si le but a été marqué lors d'un penalty
     */
    public boolean isPenalty() {
        return penalty;
    }

    /**
     * Setter
     *
     * @param penalty : true si le but a été marqué lors d'un penalty
     */
    public void setPenalty(boolean penalty) {
        this.penalty = penalty;
    }

    /**
     * Getter
     * @return match durant lequel le but a été marqué
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Setter
     *
     * @param match match durant lequel le but a été marqué
     */
    public void setMatch(Match match) {
        this.match = match;
    }

    /**
     * Getter
     * @return buteur ayant marqué le but
     */
    public Buteur getButeur() {
        return buteur;
    }

    /**
     * Setter
     * @param buteur buteur ayant marqué le but
     */
    public void setButeur(Buteur buteur) {
        this.buteur = buteur;
    }
}
