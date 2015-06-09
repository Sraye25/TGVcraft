package com.github.Sraye25;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class TGVcraftCommand implements CommandExecutor
{
	private Plugin plugin;
	private Statement state;
	
	public TGVcraftCommand(Plugin plugin, Statement state)
	{
		this.plugin = plugin;
		this.state = state;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			switch(label)
		    {
		    	case "tgvcraft":
		    		commandeTGVcraft(p,args);
		    	break;
		    }
		}
	    return true;
	}
	
	public void commandeTGVcraft(Player p, String[] arg)
	{
		List<String> args = Arrays.asList(arg);
		
		if(arg.length == 0)
		{
			p.sendMessage("Il manque les arguments, veuillez voir la documentation du plugin");
		}
		else
		{
			switch(args.get(0))
			{
				case "aller":
					if(arg.length > 2 && arg.length != 0) p.sendMessage("Utilisation : /tgvcraft aller <nom>");
					else execaller(p,arg);
				break;
				case "gare":
					if(arg.length > 2 && arg.length != 0) p.sendMessage("Utilisation : /tgvcraft gare <nom>");
					else execgare(p,arg);
				break;
				case "cgare":
					if(arg.length != 2) p.sendMessage("Utilisation : /tgvcraft cgare <nom>");
					else execCgare(p,args);
				break;
				case "mgare":
					if(arg.length != 2) p.sendMessage("Utilisation : /tgvcraft mgare <nom>");
					else execMgare(p,args);
				break;
				case "sgare":
					if(arg.length != 2) p.sendMessage("Utilisation : /tgvcraft sgare <nom>");
					else execSgare(p,args);
				break;
				
				case "inter":
					if(arg.length != 1) p.sendMessage("Utilisation : /tgvcraft inter");
					else execinter(p,arg);
				break;
				case "cinter":
					if(arg.length != 6) p.sendMessage("Utilisation : /tgvcraft cinter <id_inter> <nom_gare> <cote_inter> <cote_gare> <distance>");
					else execCinter(p,args);
				break;
				case "sinter":
					if(arg.length != 2) p.sendMessage("Utilisation : /tgvcraft sinter <id_inter>");
					else execSinter(p,args);
				break;
				case "distance":
					if(arg.length != 3) p.sendMessage("Utilisation : /tgvcraft distance <depart> <arrivee>");
					else execDistance(p,args);
				break;
				case "itineraire":
					if(arg.length != 3) p.sendMessage("Utilisation : /tgvcraft itineraire <depart> <arrivee>");
					else execItineraire(p,args);
				break;
				case "itineraireBasNiveau":
					if(arg.length != 3) p.sendMessage("Utilisation : /tgvcraft itineraireBasNiveau <depart> <arrivee>");
					else execItineraireBasNiveau(p,args);
				break;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void execaller(Player p, String[] args)
	{
		if(p.isInsideVehicle() && p.getVehicle().getType() == EntityType.MINECART) /*Si le joueur est dans un vehicule est que c'est un minecart*/
		{
			Minecart minecart = (Minecart)p.getVehicle();
			Location loc = minecart.getLocation();
		    loc.setY(loc.getY()-1); /*Avoir block sous le minecart*/
		    Block b = loc.getBlock();
		    
			int id_bloc_sous_minecart = b.getTypeId();
		    
		    if(id_bloc_sous_minecart == plugin.getConfig().getInt("bloc_gare")) /*Si on passe sur un bloc d'obsi*/
		    {
		    	String nomGare = avoirNomGareDepart(loc);
		    	if(!nomGare.equals("")) /* Si la gare est reconnue*/
		    	{
		    		String chemin = cheminDeAaB(nomGare,args[1]);
		    		minecart.setMetadata("direction",new FixedMetadataValue(plugin,chemin));
		    		p.sendMessage("Le minecart est initialisé, veillez appuyer sur le bouton pour partir");
		    	}
		    	else p.sendMessage("Cette gare est inconnue");
		    }
		    else p.sendMessage("Veillez à ce que vous êtes sur la zone de départ d'une gare ");
		}
		else p.sendMessage("Veillez etre dans un minecart pour utiliser cette commande");
	}
	
	public void execgare(Player p, String[] args)
	{
		if(args.length == 1)
		{
			try {
				ResultSet result = state.executeQuery("SELECT * FROM Gare");
				ResultSetMetaData res = result.getMetaData();
				afficheJoueurTout(p,result,res);
			}catch (SQLException e){
				e.printStackTrace();
				p.sendMessage("Impossible d'afficher toutes les gares // Veuillez vous reporter au log");
			}
		}
		else
		{
			try {
				ResultSet result = state.executeQuery("SELECT * FROM Gare WHERE nom = '"+args[1]+"'");
				ResultSetMetaData res = result.getMetaData();
				afficheJoueurTout(p,result,res);
			}catch (SQLException e){
				e.printStackTrace();
				p.sendMessage("Impossible d'afficher la gare " + args[1] + " // Veuillez vous reporter au log");
			}
		}
	}
	
	public void execCgare(Player p, List<String> args)
	{
		Location loc = p.getEyeLocation();
		int x = tronc(loc.getX());
		int y = tronc(loc.getY());
		int z = tronc(loc.getZ());
		
		try {
			state.executeUpdate("INSERT INTO Gare VALUES (NULL,'"+args.get(1)+"','"+x+"','"+y+"','"+z+"')");
			p.sendMessage("Creation de la gare "+args.get(1));
		} catch (SQLException e) {
			e.printStackTrace();
			p.sendMessage("Impossible de créer la gare "+args.get(1)+" // Veuillez vous reporter au log");
		}
	}
	
	public void execMgare(Player p, List<String> args)
	{
		Location loc = p.getEyeLocation();
		int x = tronc(loc.getX());
		int y = tronc(loc.getY());
		int z = tronc(loc.getZ());
		
		try {
			state.executeUpdate("UPDATE Gare SET x='"+x+"', y='"+y+"', z='"+z+"' WHERE nom='"+args.get(1)+"'");
			p.sendMessage("Modification de la gare "+args.get(1));
		}catch (SQLException e){
			e.printStackTrace();
			p.sendMessage("Impossible de modifier la gare "+args.get(1)+" // Veuillez vous reporter au log");
		}
	}
	
	public void execSgare(Player p, List<String> args)
	{
		try {
			state.executeUpdate("DELETE FROM Gare WHERE nom='"+args.get(1)+"'");
			p.sendMessage("Suppression de la gare "+args.get(1));
		}catch (SQLException e){
			e.printStackTrace();
			p.sendMessage("Impossible de supprimer la gare "+args.get(1)+" // Veuillez vous reporter au log");
		}
	}
	
	public void execinter(Player p, String[] args)
	{
		try {
			ResultSet result = state.executeQuery("SELECT * FROM Inter");
			ResultSetMetaData res = result.getMetaData();
			afficheJoueurTout(p,result,res);
		}catch (SQLException e){
			e.printStackTrace();
			p.sendMessage("Impossible d'afficher toutes les intersections // Veuillez vous reporter au log");
		}
	}
	
	public void execCinter(Player p, List<String> args)
	{
		int id_gare = avoirIdGare(args.get(2));
		
		try {
			state.executeUpdate("INSERT INTO Inter VALUES ('"+args.get(1)+"','"+id_gare+"','"+args.get(3)+"','"+args.get(4)+"','"+args.get(5)+"')");
			p.sendMessage("Creation de l'intersection "+args.get(1));
		} catch (SQLException e) {
			e.printStackTrace();
			p.sendMessage("Impossible de créer l'intersection "+args.get(1)+" // Veuillez vous reporter au log");
		}
	}
	
	public void execSinter(Player p, List<String> args)
	{
		try {
			state.executeUpdate("DELETE FROM Inter WHERE id_inter='"+args.get(1)+"'");
			p.sendMessage("Suppression de l'intersection "+args.get(1));
		}catch (SQLException e){
			e.printStackTrace();
			p.sendMessage("Impossible de supprimer la intersection "+args.get(1)+" // Veuillez vous reporter au log");
		}
	}
	
	public void execDistance(Player p, List<String> args)
	{
		if(gareExist(args.get(1)) && gareExist(args.get(2)))
		{
			Graphe graphe = new Graphe(state,args.get(1));
			Graphe graphe1 = new Graphe(state,args.get(1));
			p.sendMessage("Distance "+args.get(1)+" -> "+args.get(2)+" : " + graphe.dijkstraDistance(args.get(2)));
			graphe1.dijkstra(args.get(1),args.get(2));
		}
		else
		{
			if(!gareExist(args.get(1))) p.sendMessage("La gare "+args.get(1)+" n'existe pas");
			else p.sendMessage("La gare "+args.get(2)+" n'existe pas");
		}
		
	}
	
	public void execItineraireBasNiveau(Player p, List<String> args)
	{
		if(gareExist(args.get(1)) && gareExist(args.get(2)))
		{
			Graphe graphe = new Graphe(state,args.get(1));
			ArrayList<Sommet> chemin = graphe.dijkstraSommet(args.get(1),args.get(2));
			String sec = creerSequenceChemin(state,chemin);
			String res = stringChemin(sec);
			p.sendMessage("Direction a prendre de "+args.get(1)+" à "+args.get(2)+" : " + res);
		}
		else
		{
			if(!gareExist(args.get(1))) p.sendMessage("La gare "+args.get(1)+" n'existe pas");
			else p.sendMessage("La gare "+args.get(2)+" n'existe pas");
		}
	}
	
	public void execItineraire(Player p, List<String> args)
	{
		if(gareExist(args.get(1)) && gareExist(args.get(2)))
		{
			Graphe graphe = new Graphe(state,args.get(1));
			ArrayList<String> chemin = graphe.dijkstra(args.get(1),args.get(2));
			String res = stringChemin(chemin);
			p.sendMessage("Chemin de "+args.get(1)+" à "+args.get(2)+" : " + res);
		}
		else
		{
			if(!gareExist(args.get(1))) p.sendMessage("La gare "+args.get(1)+" n'existe pas");
			else p.sendMessage("La gare "+args.get(2)+" n'existe pas");
		}
		
	}
	
	/*
	 * Renvoie les instruction minecart entre les gares a et b ( renvoie "" si l'une des gare n'existe pas )
	 */
	public String cheminDeAaB(String a, String b)
	{
		String res = "";
		if(gareExist(a) && gareExist(b))
		{
			Graphe graphe = new Graphe(state,a);
			ArrayList<Sommet> chemin = graphe.dijkstraSommet(a,b);
			String sec = creerSequenceChemin(state,chemin);
			res = stringChemin(sec);
		}
		return res;
	}
	
	/*
	 * Renvoie le nom d'un gare grace à ses coord ( si gare inconnu , alors renvoie "" )
	 */
	public String avoirNomGareDepart(Location loc)
	{
		String res ="";
		ResultSet result;
		try{
			System.out.println(" | x : "+loc.getX()+" | x : "+loc.getY()+" | x : "+loc.getZ());
			result = state.executeQuery("SELECT nom FROM Gare WHERE x='"+loc.getX()+"' AND y='"+(loc.getY()+1)+"' AND z='"+loc.getZ()+"'");
			while(result.next()) res = result.getString("nom");
		}catch (SQLException e){
			e.printStackTrace();
		}
		return res;
	}
	
	/*
	 * Met en forme l'affichage du chemin à l'ecran
	 */
	public String stringChemin(ArrayList<String> chem)
	{
		String res ="";
		boolean prem = true;
		for(String s : chem)
		{
			if(prem)
			{
				res = s;
				prem = false;
			}
			else res = res + " -> " + s;
		}
		return res;
	}
	
	/*
	 * Met en forme l'affichage du chemin à l'ecran
	 */
	public String stringChemin(String chem)
	{
		String res ="";
		int i = 0;
		while(i < chem.length())
		{
			if(i==0) res = "" + chem.charAt(0);
			else res = res + " -> " + chem.charAt(i);
			i++;
		}
		return res;
	}
	
	/*
	 * Dit si la gare existe
	 */
	public boolean gareExist(String nom)
	{
		boolean ret = false;
		ResultSet result;
		try{
			result = state.executeQuery("SELECT COUNT(*) FROM Gare WHERE nom = '"+ nom +"'");
			result.next();
			int res = result.getInt("COUNT(*)");
			if(res==0) ret=false;
			else ret=true;
		}catch (SQLException e){
			e.printStackTrace();
		}
		return ret;
	}
	
	/*
	 * Avoir le nom de la gare grace à son id
	 */
	@SuppressWarnings("unused")
	private String avoirNomGare(int id)
	{
		String ret="";
		ResultSet result;
		try{
			result = state.executeQuery("SELECT nom FROM Gare WHERE id_gare = '"+ id +"'");
			result.next();
			ret = result.getString("nom");
		}catch (SQLException e){
			e.printStackTrace();
		}
		return ret;
	}
	
	/*
	 * Avoir l'id de la gare grace à son nom
	 */
	private int avoirIdGare(String nom)
	{
		int ret = 0;
		ResultSet result;
		try{
			result = state.executeQuery("SELECT id_gare FROM Gare WHERE nom = '"+ nom +"'");
			result.next();
			ret = result.getInt("id_gare");
		}catch (SQLException e){
			e.printStackTrace();
		}
		/*System.out.println("nom : "+nom+" | id_gare : "+ret);*/
		return ret;
	}
	
	/*
	 * Affiche une liste de resultat issues d'un requete SQL au joueur en jeu
	 */
	private void afficheJoueurTout(Player p, ResultSet result, ResultSetMetaData res) throws SQLException
	{
		String ligne="";
		for(int i = 1; i <= res.getColumnCount(); i++) ligne = ligne + "  " + res.getColumnName(i).toUpperCase() + "  |";
			p.sendMessage(ligne);
		while(result.next())
		{
			ligne = "";
			for(int i = 1; i <= res.getColumnCount(); i++)
			{
				if(result.getObject(i) == null) ligne = ligne +"  " + " null " + "  |";
				else ligne = ligne +"  " + result.getObject(i).toString() + "  |";
			}
			p.sendMessage(ligne);
		}
	}
	
	/*
	 * Creer un string avec les instruction pour aller de a à b en minecart
	 */
	public String creerSequenceChemin(Statement state, ArrayList<Sommet> liste)
	{
		String res="";
		int i = 0;
		ArrayList<String> chemin = creerCheminInter(state,liste);
		while(i < chemin.size()-1)
		{
			res = res + traduire(chemin.get(i),chemin.get(i+1));
			i+=2;
		}
		return res;
	}
	
	/*
	 * Traduit des directions en instruction pour le minecart ( droite, gauche et milieu )
	 */
	public String traduire(String nb1,String nb2)
	{
		String res="";
		if(nb1.equals("dep") && !nb2.equals("arr")) /* Si on est au depart*/
		{
			if(nb2.equals("g")) res = "g";
			else res = "mdm";
		}
		else if(nb2.equals("arr") && !nb1.equals("dep"))
		{
			if(nb1.equals("d")) res = "g";
			else res = "mdm";
		}
		else if(nb1.equals("dep") && nb2.equals("arr"))
		{
			res = "mddm";
		}
		else
		{
			if(!(etreDirectionGeo(nb2) || etreDirectionGeo(nb2))) res = "mm"; /*Si on traverse une gare*/
			else if(etreDirectionGeo(nb1) && etreDirectionGeo(nb2))/*Si on est a une inter*/
			{
				if(nb1.equals("e") && nb2.equals("o") || nb1.equals("o") && nb2.equals("e") || nb1.equals("n") && nb2.equals("s") || nb1.equals("s") && nb2.equals("n")) res = "mm";
				else if(nb1.equals("n") && nb2.equals("e") || nb1.equals("s") && nb2.equals("o") || nb1.equals("e") && nb2.equals("s") || nb1.equals("o") && nb2.equals("n")) res = "g";
				else res = "mdm";
			}
		}
		return res;
	}

	/*
	 * Vrai si on a à faire à une direction ( nord, sud, est et ouest )
	 */
	public boolean etreDirectionGeo(String dir)
	{
		boolean res= false;
		if(dir.equals("n") || dir.equals("s") || dir.equals("o") || dir.equals("e")) res = true;
		return res;
	}
	
	/*
	 * Créer une liste avec toutes les directions d'entres et de sortie d'inter et de gares 
	 * pour aller d'un point a à b
	 */
	public ArrayList<String> creerCheminInter(Statement state, ArrayList<Sommet> liste)
	{
		int i = 0;
		ArrayList<String> res = new ArrayList<String>();
		res.add("dep");
		while(i < liste.size()-1)
		{
			ArrayList<String> temp = avoirCote(state,liste.get(i),liste.get(i+1));
			String cote_gare = temp.get(0);
			String cote_inter = temp.get(1);
			
			if(liste.get(i).estGare())
			{
				res.add(cote_gare);
				res.add(cote_inter);
			}
			else
			{
				res.add(cote_inter);
				res.add(cote_gare);
			}
			i++;
		}
		res.add("arr");
		return res;
	}
	
	/*
	 * Renvoie les directions d'entrées et de sortie entre 2 sommets adjacents
	 * 1er elem renvoyé : cote_gare
	 * 2e elem renvoyé : cote_inter
	 */
	public ArrayList<String> avoirCote(Statement state, Sommet nb1, Sommet nb2)
	{
		String cote_gare = "";
		String cote_inter = "";
		
		if(nb1.estGare())
		{
			try {
				ResultSet result = state.executeQuery("SELECT cote_inter, cote_gare FROM Inter, Gare WHERE nom ='"+nb1.nom+"' AND Gare.id_gare=Inter.id_gare AND Inter.id_inter='"+nb2.nom+"'");
				while(result.next())
				{
					cote_gare = result.getString("cote_gare");
					cote_inter = result.getString("cote_inter");
				}
				if(cote_gare=="n") cote_gare="s";
				else if(cote_gare=="s") cote_gare="n";
				else if(cote_gare=="e") cote_gare="o";
				else if(cote_gare=="o") cote_gare="e";
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		else
		{
			try {
				ResultSet result = state.executeQuery("SELECT cote_inter, cote_gare FROM Inter, Gare WHERE nom ='"+nb2.nom+"' AND Gare.id_gare=Inter.id_gare AND Inter.id_inter='"+nb1.nom+"'");
				while(result.next())
				{
					cote_gare = result.getString("cote_gare");
					cote_inter = result.getString("cote_inter");
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		ArrayList<String> res = new ArrayList<String>();
		res.add(cote_gare);
		res.add(cote_inter);
		return res;
	}

	/*
	 * Faire la troncature d'un nb
	 */
	private int tronc(double y)
	{
		return (int)y;
	}
	

}
