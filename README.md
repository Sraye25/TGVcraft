# TGVcraft

/!\ Ce mod est en version alpha, si vous trouver des bugs, merci de me prévenir /!\

Ce mod a pour but de modifier le comportement des minecarts sur un serveur Spigot, voici ces specificités : 
  * Augmenter la vitesse des Minecarts sur certains tronçons. 
  * Créer un réseau de minecart dans lequel les minecarts peuvent se diriger seul en leur indiquant la gare de départ et celle d'arrivée.

##Installation : 
  1. Télécharger les fichiers TGVcraft.jar et creer_table_tgvcraft.sql. 
  2. Mettre le fichier TGVcraft.jar dans le dossier plugins de Spigot. 
  3. Intaller MySQL et créer un utilisateur ( n'oubliez pas de modifier si nécessaire le fichier de configuration ). 
  4. Exécuter le fichier creer_table_tgvcraft.sql qui créera les tables nécessaire dans la base de données. 
  5. Enjoy !!!

##Configuration : 
  * **vitesse_moy** : par défaut "0.4", définit la vitesse moyenne d'un minecart. 
  * **vitesse_max** : par défaut "4.0", définit la vitesse maximale d'un minecart. 
  * **lent_si_vide** : par défaut "false", si l'argument est false, alors un minecart vide avancera aussi vide qu'un minecart plein ou occupé. 
  * **bloc_debut_tgv** : par défaut "41" ( bloc d'or ), définit quel bloc est le début d'un zone à grande vitesse. 
  * **bloc_fin_tgv** : par défaut "22" ( bloc de lapis lazuli ), définit quel bloc est la fin d'un zone à grande vitesse. 
  * **bloc_change_dir** : par défaut "42" ( bloc de fer ), définit quel bloc est un changement de direction pour un minecart.
  * **bloc_gare** : par défaut "49" ( bloc d'obsidienne ), définit quel bloc est la zone de départ d'une gare. 
  * **dir_active** : par défaut "true", si l'argument est false, alors le module de changement de direction du plugin est désactivé. 
  * **url_db** : par défaut "jdbc:mysql://localhost:3306/TGVcraft", définit l'url de la base de données MySQL. 
  * **user** : par défaut "TGVcraft", définit le nom d'utilisateur de la base de données MySQL. 
  * **password** : par défaut "1234", définit le mot de passe de l'utilisateur de la base de données MySQL. 

##Commandes : 
  * **/tgvcraft gare "nom_gare"** : Voir toutes les gares ( ainsi que leur localisation ), si on précise un nom, on a que les informations de cette gare. 
  * **/tgvcraft cgare [nom_gare]** : Créer une gare en précisant son nom. Il faut absolument que le joueur qui tape cette commande soit sur la zone de départ de la gare a créé ( sur le cube d'obsidienne par defaut ). 
  * **/tgvcraft mgare [nom_gare]** : Modifier la localisation d'une gare. Il faut absolument que le joueur qui tape cette commande soit sur la zone de départ de la gare a modifié ( sur le cube d'obsidienne par defaut ). 
  * **/tgvcraft sgare [nom_gare]** : Supprimer une gare. 
  * **/tgvcraft inter "id_inter"** : Voir toutes les intersections. Si on precise un nom, on a que les informations de cette intersection. 
  * **/tgvcraft cinter [id_inter] [nom_gare] [cote_inter] [cote_gare] [distance]** : Créer une intersection, on precise son id, le nom de la gare a lié, le coté de l'intersection consernées ( n pour nord, s pour sud, e pour est et o pour ouest ), le coté de la gare consernées ( g pour gauche, d pour droite ) et la distance en cube entre la gare et l'intersection. 
  * **/tgvcraft sinter [id_inter]** : Supprimer une intersection. 
  * **/tgvcraft distance [depart] [destination]** : Avoir la distance approximative entre 2 gares. 
  * **/tgvcraft itineraire [depart] [destination]** : Avoir l'itinéraire entre la gare de départ et celle de destination. 
  * **/tgvcraft itineraireBasNiveau [depart] [destination]** : Avoir l'itinéaire entre la gare de départ et celle de destination en "instructions minecart" ( instructions qui servent à diriger le minecart). 
  * **/tgvcraft aller [destination]** : Initialiser le minecart ( dans une gare ) afin qu'il connaisse le chemin jusqu'à une gare de destination choisit. 

##Permissions : 
  * **tgvcraft.aller** : Pouvoir utiliser /tgvcraft aller [destination] 
  * **tgvcraft.gare.voir** : Pouvoir utiliser /tgvcraft gare "nom_gare" 
  * **tgvcraft.gare.creer** : Pouvoir utiliser /tgvcraft cgare [nom_gare] 
  * **tgvcraft.gare.modifier** : Pouvoir utiliser /tgvcraft mgare [nom_gare] 
  * **tgvcraft.gare.supprimer** : Pouvoir utiliser /tgvcraft sgare [nom_gare] 
  * **tgvcraft.inter.voir** : Pouvoir utiliser /tgvcraft inter "id_inter" 
  * **tgvcraft.inter.creer** : Pouvoir utiliser /tgvcraft cinter [id_inter] [nom_gare] [cote_inter] [cote_gare] [distance] 
  * **tgvcraft.inter.supprimer** : Pouvoir utiliser /tgvcraft sinter [id_inter] 
  * **tgvcraft.distance** : Pouvoir utiliser /tgvcraft distance [depart] [destination] 
  * **tgvcraft.itineraire** : Pouvoir utiliser /tgvcraft itineraire [depart] [destination] 
  * **tgvcraft.itineraire_bas_niveau** : Pouvoir utiliser /tgvcraft itineraireBasNiveau [depart] [destination] 