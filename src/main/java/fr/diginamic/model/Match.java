package fr.diginamic.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe représentant l'entité Match :
 * il s'agit d'un match de football disputé entre deux équipes internationales
 */
@Entity
@Table(name="match_foot")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private String pays;

    @Column(name="lieu_neutre")
    private boolean lieuNeutre;

    private String tournoi;

    /**
     * Relation ManyToOne avec 2 équipes :
     * une équipe hôte qui accueille le match
     * une équipe invitée qui rejoint le match
     */
    @ManyToOne
    @JoinColumn(name="equipe_hote_id")
    private Equipe equipeHote;

    @ManyToOne
    @JoinColumn(name="equipe_invitee_id")
    private Equipe equipeInvitee;

    /**
     * Relation OneToMany avec But
     * pour représenter les buts marqués au cours d'un match
     */
    @OneToMany(mappedBy = "match")
    private Set<But> buts;

    /**
     * Relation OneToOne avec l'entité score
     */
    @OneToOne(mappedBy = "match")
    private Score score;

    /**
     * Relation OneToOne avec l'entité tirsButs
     */
    @OneToOne(mappedBy = "match")
    private TirsButs tirsButs;

    /**
     * Constructeur vide pour JPA
     */
    public Match() {
        buts = new HashSet<>();
    }

    /**
     * Constructeur complet pour créer une instance de Match
     * @param date date du match
     * @param ville ville où se joue le match
     * @param pays pays où se joue le match
     * @param lieuNeutre vrai si le lieu est neutre
     * @param tournoi nom du tournoi
     * @param equipeHote équipe hôte
     * @param equipeInvitee équipe invitée
     */
    public Match(LocalDate date, String ville, String pays, boolean lieuNeutre, String tournoi, Equipe equipeHote, Equipe equipeInvitee) {
        this.date = date;
        this.ville = ville;
        this.pays = pays;
        this.lieuNeutre = lieuNeutre;
        this.tournoi = tournoi;
        this.equipeHote = equipeHote;
        this.equipeInvitee = equipeInvitee;
    }

    /**
     * Optimise l'affichage des attributs de l'instance
     * @return les attributs de l'instance.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Match{");
        sb.append("id=").append(id);
        sb.append(", date=").append(date);
        sb.append(", ville='").append(ville).append('\'');
        sb.append(", pays='").append(pays).append('\'');
        sb.append(", lieuNeutre=").append(lieuNeutre);
        sb.append(", tournoi='").append(tournoi).append('\'');
        sb.append(", equipeHote=").append(equipeHote);
        sb.append(", equipeInvitee=").append(equipeInvitee);
        sb.append(", buts=").append(buts);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
     * @return id du match
     */
    public int getId() {
        return id;
    }

    /**
     * Getter
     * @return date du match
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Setter
     * @param date date du match
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Getter
     * @return ville où se déroule le match
     */
    public String getVille() {
        return ville;
    }

    /**
     * Setter
     * @param ville ville où se déroule le match
     */
    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * Getter
     *
     * @return pays où se déroule le match
     */
    public String getPays() {
        return pays;
    }

    /**
     * Setter
     *
     * @param pays pays où se déroule le match
     */
    public void setPays(String pays) {
        this.pays = pays;
    }

    /**
     * Getter
     * indique si le lieu est neutre, donc sans rapport avec les équipes
     * @return lieu_neutre true ou false
     */
    public boolean isLieuNeutre() {
        return lieuNeutre;
    }

    /**
     * Setter
     * permet d'indiquer si le lieu est neutre, donc sans rapport avec les équipes
     * @param lieuNeutre lieuNeutre true ou false
     */
    public void setIsLieuNeutre(boolean lieuNeutre) {
        this.lieuNeutre = lieuNeutre;
    }

    /**
     * Getter
     * @return tournoi : nom du tournoi dans le cadre duquel se déroule le match
     */
    public String getTournoi() {
        return tournoi;
    }

    /**
     * Setter
     *
     * @param tournoi tournoi : nom du tournoi dans le cadre duquel se déroule le match
     */
    public void setTournoi(String tournoi) {
        this.tournoi = tournoi;
    }

    /**
     * Getter
     * @return equipeHote équipe accueillant le match
     */
    public Equipe getEquipeHote() {
        return equipeHote;
    }

    /**
     * Setter
     * @param equipeHote équipe hôte accueillant le match
     */
    public void setEquipeHote(Equipe equipeHote) {
        this.equipeHote = equipeHote;
    }

    /**
     * Getter
     * @return equipeInvitee équipe rejoignant le match
     */
    public Equipe getEquipeInvitee() {
        return equipeInvitee;
    }

    /**
     * Setter
     * @param equipeInvitee équipe invitée rejoignant le match
     */
    public void setEquipeInvitee(Equipe equipeInvitee) {
        this.equipeInvitee = equipeInvitee;
    }

    /**
     * Getter
     * @return buts
     */
    public Set<But> getButs() {
        return buts;
    }

    /**
     * Setter
     * @param buts buts
     */
    public void setButs(Set<But> buts) {
        this.buts = buts;
    }

    /**
     * Getter
     * @return score
     */
    public Score getScore() {
        return score;
    }

    /**
     * Setter
     * @param score score
     */
    public void setScore(Score score) {
        this.score = score;
    }

    /**
     * Getter
     * @return tirsButs
     */
    public TirsButs getTirsButs() {
        return tirsButs;
    }

    /**
     * Setter
     * @param tirsButs tirsButs
     */
    public void setTirsButs(TirsButs tirsButs) {
        this.tirsButs = tirsButs;
    }
}
