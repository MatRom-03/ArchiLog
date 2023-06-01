# Projet Architecture Logicielle

### Présentation

Ce projet a pour but de créer 3 logiciels client qui communiquent avec l'application serveur.
- Le premier est un serveur qui gère les reservations de documents.
- Le deuxième est un client qui gère les emprunts de documents.
- Le troisième est un client qui gère les retours de documents.

L'application serveur communique indépendamment avec les clients et la base de données.
La base de données est une base de données sqLite.

C'est un projet purement éducatif fait par des élèves de l'IUT de paris rives-de-seine.

### Lancement du projet

#### Lancement de l'application serveur :
- Pour lancer l'application serveur, il faut lancer le fichier `Application.java` dans le dossier `src/appliServer`.

#### Lancement des clients :
- Pour lancer le client de reservation, il faut lancer le fichier `Bttp.java` dans le dossier `src/applisClient` avec les 2 arguments suivant :
  - l'adresse IP du serveur (ex: 127.0.0.1 ou localhost)
  - le port du service (1000 -> réservations; 1001 -> emprunts; 1002 -> retours)
