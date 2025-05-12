Ce projet Java utilise JPA (Hibernate) pour importer, gérer et interroger des données issues de compétitions de football international, à partir de fichiers CSV. Il intègre la persistance des entités principales telles que les équipes, matchs, buteurs, buts et tirs aux buts.

** Structure **
- Entities : Equipe, Match, Buteur, But, TirsButs, Score
- DAO : Requêtes JPQL/Hibernate
- Services : Logique métier (vérification doublons, insertion conditionnelle)
- Utils : Outils de parsing CSV, validation, journalisation d'erreurs
- Imports : Chargement de fichiers results.csv, goalscorers.csv, shootouts.csv
- Tests : Couverture unitaire via JUnit 5 et Mockito

** Technologies **
Java 21
JPA / Hibernate 6
MySQL
Ehcache (niveau 2 de cache via JCache API)
JUnit 5 + Mockito

** Exécution **
Créer la base football dans MySQL (voir script MPD dans le fichier de conception à la source du projet)
Configurer persistence.xml (utilisateur, mot de passe)
Lancer Main pour importer les données
Exécuter les tests avec mvn test

** Tests **
Tests unitaires avec mocks (EquipeServiceTest, MatchServiceTest, etc.) = aucune dépendance directe à une base réelle
