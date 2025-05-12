package fr.diginamic.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe représentant l'entité Equipe :
 * il s'agit d'une équipe internationale de football
 */
@Entity
@Table(name="equipe")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;

    /**
     * Relation OneToMany avec le match
     * représentant tous les matchs joués par l'équipe :
     * d'une part les matchs en tant qu'hôtes
     * d'autre part les matchs en tant qu'invités
     */
    @OneToMany(mappedBy = "equipeHote")
    private Set<Match> matchsHote;

    @OneToMany(mappedBy = "equipeInvitee")
    private Set<Match> matchsInvitee;

    /**
     * Relation OneToMany avec le buteur
     * représentant tous les joueurs de l'équipe ayant marqué au moins un but
     */
    @OneToMany(mappedBy = "equipe")
    private Set<Buteur> buteurs;

    /**
     * Relation OneToMany avec l'entité TirsButs
     * représentant l'équipe qui commence la séance de tirs au but
     */
    @OneToMany(mappedBy = "equipeCommence")
    private Set<TirsButs> tirsButsCommences;

    /**
     * Relation OneToMany avec l'entité TirsButs
     * représentant l'équipe qui remporte la séance de tirs au but
     */
    @OneToMany(mappedBy = "vainqueur")
    private Set<TirsButs> tirsButsRemportes;

    /**
     * Initialisation d'instance
     */
    {
        matchsHote = new HashSet<>();
        matchsInvitee = new HashSet<>();
        buteurs = new HashSet<>();
        tirsButsCommences = new HashSet<>();
        tirsButsRemportes = new HashSet<>();
    }

    /**
     * Constructeur vide pour JPA
     */
    public Equipe() {
    }

    /**
     * Constructeur permettant de créer une équipe avec son nom
     * @param nom nom de l'équipe
     */
    public Equipe(String nom) {
        this.nom = nom;
    }

    /**
     * Optimise l'affichage d'une instance de Equipe
     * @return les attributs de l'instance.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Equipe{");
        sb.append("id=").append(id);
        sb.append(", nom='").append(nom).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
     * @return id de l'équipe
     */
    public int getId() {
        return id;
    }

    /**
     * Setter
     * @param id id, utile pour les tests
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter
     * @return nom de l'équipe
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter
     * @param nom nom de l'équipe
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter
     * @return matchsHote : matchs joués en tant qu'hôte
     */
    public Set<Match> getMatchsHote() {
        return matchsHote;
    }

    /**
     * Setter
     *
     * @param matchsHote matchsHote : matchs joués en tant qu'hôte
     */
    public void setMatchsHote(Set<Match> matchsHote) {
        this.matchsHote = matchsHote;
    }

    /**
     * Getter
     *
     * @return matchsInvitee : matchs joués en tant qu'invitée
     */
    public Set<Match> getMatchsInvitee() {
        return matchsInvitee;
    }

    /**
     * Setter
     * @param matchsInvitee matchsInvitee: matchs joués en tant qu'invitée
     */
    public void setMatchsInvitee(Set<Match> matchsInvitee) {
        this.matchsInvitee = matchsInvitee;
    }

    /**
     * Getter
     * @return buteurs de l'équipe
     */
    public Set<Buteur> getButeurs() {
        return buteurs;
    }

    /**
     * Setter
     * @param buteurs buteurs de l'équipe
     */
    public void setButeurs(Set<Buteur> buteurs) {
        this.buteurs = buteurs;
    }

    /**
     * Getter
     * @return tirsButsCommences
     */
    public Set<TirsButs> getTirsButsCommences() {
        return tirsButsCommences;
    }

    /**
     * Setter
     * @param tirsButsCommences tirsButsCommences
     */
    public void setTirsButsCommences(Set<TirsButs> tirsButsCommences) {
        this.tirsButsCommences = tirsButsCommences;
    }

    /**
     * Getter
     * @return tirsButsRemportes
     */
    public Set<TirsButs> getTirsButsRemportes() {
        return tirsButsRemportes;
    }

    /**
     * Setter
     * @param tirsButsRemportes tirsButsRemportes
     */
    public void setTirsButsRemportes(Set<TirsButs> tirsButsRemportes) {
        this.tirsButsRemportes = tirsButsRemportes;
    }
}
