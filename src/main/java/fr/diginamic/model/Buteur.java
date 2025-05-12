package fr.diginamic.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe représentant l'entité buteur :
 * joueur d'une équipe ayant marqué au moins un but au cours d'un match
 */
@Entity
@Table(name="buteur")
public class Buteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nom;

    /**
     * Relation ManyToOne pour représenter son appartenance à l'équipe
     */
    @ManyToOne
    @JoinColumn(name="equipe_id")
    private Equipe equipe;

    /**
     * Relation OneToMany avec But
     * représentant les buts marqués par le buteur
     */
    @OneToMany(mappedBy = "buteur")
    private Set<But> buts;

    /**
     * Initialisation d'instance
     */
    {
        buts = new HashSet<>();
    }

    /**
     * Constructeur vide pour JPA
     */
    public Buteur() {
    }

    /**
     * Constructeur permettant de créer un buteur
     * @param nom nom du buteur
     * @param equipe équipe à laquelle appartient le buteur
     */
    public Buteur(String nom, Equipe equipe) {
        this.nom = nom;
        this.equipe = equipe;
    }

    /**
     * Optimise l'affichage des attributs de l'instance
     *
     * @return les attributs de l'instance.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Buteur{");
        sb.append("id=").append(id);
        sb.append(", nom='").append(nom).append('\'');
        sb.append(", equipe=").append(equipe);
        sb.append(", buts=").append(buts);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
     * @return id du buteur
     */
    public int getId() {
        return id;
    }

    /**
     * Setter
     * @param id id, utilisés pour les tests
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter
     * @return nom du buteur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter
     * @param nom nom du buteur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter
     * @return equipe à laquelle appartient le buteur
     */
    public Equipe getEquipe() {
        return equipe;
    }

    /**
     * Setter
     * @param equipe equipe à laquelle appartient le buteur
     */
    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
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
}
