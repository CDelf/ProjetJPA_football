package fr.diginamic.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entité représentant une erreur rencontrée lors de l'import de données.
 */
@Entity
@Table(name = "erreur_import")
public class ErreurImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fichier;

    @Column(columnDefinition = "TEXT")
    private String ligne;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "date_erreur")
    private LocalDateTime dateErreur;

    private String traitement;

    /**
     * Constructeur vide pour JPA
     */
    public ErreurImport() {
        this.dateErreur = LocalDateTime.now();
    }

    /**
     * Constructeur complet
     * @param fichier fichier à l'origine de l'erreur
     * @param ligne ligne concernée par l'erreur
     * @param message message d'erreur
     * @param traitement traitement à l'origine de l'erreur
     */
    public ErreurImport(String fichier, String ligne, String message, String traitement) {
        this.fichier = fichier;
        this.ligne = ligne;
        this.message = message;
        this.traitement = traitement;
        this.dateErreur = LocalDateTime.now();
    }

    /**
     * Optimise l'affichage des attributs de l'instance
     * @return les attributs de l'instance.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErreurImport{");
        sb.append("id=").append(id);
        sb.append(", fichier='").append(fichier).append('\'');
        sb.append(", ligne='").append(ligne).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", dateErreur=").append(dateErreur);
        sb.append(", traitement='").append(traitement).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter
     * @return fichier à l'origine de l'erreur
     */
    public String getFichier() {
        return fichier;
    }

    /**
     * Getter
     * @return ligne concernée par l'erreur
     */
    public String getLigne() {
        return ligne;
    }

    /**
     * Getter
     * @return message d'erreur
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter
     * @return dateErreur date à laquelle l'erreur s'est déclenchée
     */
    public LocalDateTime getDateErreur() {
        return dateErreur;
    }

    /**
     * Getter
     * @return traitement (ou entité) lié à l'erreur
     */
    public String getTraitement() {
        return traitement;
    }
}
