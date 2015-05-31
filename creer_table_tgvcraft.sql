CREATE TABLE Gare (
	id_gare INTEGER NOT NULL PRIMARY KEY ,
	nom VARCHAR(50) NOT NULL UNIQUE,
	inter_gauche INTEGER,
	inter_droite INTEGER,
	dist_gauche INTEGER,
	dist_droite INTEGER,
	x INTEGER NOT NULL,
	z INTEGER NOT NULL);

CREATE TABLE Inter (
	id_inter INTEGER NOT NULL PRIMARY KEY ,
	gare_n INTEGER,
	gare_s INTEGER,
	gare_e INTEGER,
	gare_o INTEGER);
