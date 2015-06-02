package com.github.Sraye25;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TGVcraftCommand implements CommandExecutor
{
	@SuppressWarnings("unused")
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
			p.sendMessage("Il manque les arguments");
		}
		else
		{
			switch(args.get(0))
			{
				case "gare":
					if(arg.length > 2 && arg.length != 0) p.sendMessage("Utilisation : /tgvcraft gare [nom]");
					else execgare(p,arg);
				break;
				case "cgare":
					if(arg.length != 2) p.sendMessage("Utilisation : /tgvcraft cgare <nom>");
					else execCgare(p,args);
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
			}
		}
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
		p.sendMessage("Distance depart -> arrivee : " + dijkstraDistance(state,args.get(1),args.get(2)));
	}
	
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

	private int tronc(double y)
	{
		return (int)y;
	}
	
	/* Calcule distance */
	
	public ArrayList<String> dijkstra(Statement state, String depart, String arrivee)
	{
		Graphe graphe = new Graphe(state,depart);
		while(graphe.tt_sommets_marquer())
		{
			Sommet a = graphe.sommetChoisit();
			a.valider();
			for(Sommet b : graphe.voisinNNMarquer(a))
			{
				b.label = min(b.label,a.label+graphe.distance(a,b));
			}
		}
		System.out.println("Distance depart -> arrivee : "+graphe.avoirSommet(arrivee).label);
		return null;
	}
	
	public int dijkstraDistance(Statement state, String depart, String arrivee)
	{
		Graphe graphe = new Graphe(state,depart);
		while(graphe.tt_sommets_marquer())
		{
			Sommet a = graphe.sommetChoisit();
			a.valider();
			for(Sommet b : graphe.voisinNNMarquer(a))
			{
				b.label = min(b.label,a.label+graphe.distance(a,b));
			}
		}
		return graphe.avoirSommet(arrivee).label;
	}
	
	public int min(int a, int b)
	{
		int res = 0;
		if(a<b && b==-1 && a!=-1) res=a;
		if(b<a && a==-1 && b!=-1) res=b;
		return res;
	}
}
