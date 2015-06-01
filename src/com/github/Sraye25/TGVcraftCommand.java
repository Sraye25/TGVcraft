package com.github.Sraye25;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
					if(arg.length != 6) p.sendMessage("Utilisation : /tgvcraft cinter <id_inter> <gare_nord> <gare_sud> <gare_est> <gare_ouest> / gare vide = 0");
					else execCinter(p,args);
				break;
				case "sinter":
					if(arg.length != 2) p.sendMessage("Utilisation : /tgvcraft sinter <id_inter> / gare vide = 0");
					else execSinter(p,args);
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
		try {
			state.executeUpdate("INSERT INTO Inter VALUES ('"+args.get(1)+"','"+avoirIdGare(args.get(2))+"','"+args.get(3)+"','"+args.get(4)+"')");
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
	
	@SuppressWarnings("unused")
	private String avoirNomGare(int id)
	{
		String ret="";
		ResultSet result;
		try{
			result = state.executeQuery("SELECT nom FROM Gare WHERE id_gare = '"+ id +"'");
			ret = result.getObject(1).toString();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return ret;
	}
	
	private int avoirIdGare(String nom)
	{
		String ret="";
		ResultSet result;
		try{
			result = state.executeQuery("SELECT id_gare FROM Gare WHERE nom = '"+ nom +"'");
			ret = result.getObject(1).toString();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return Integer.parseInt(ret);
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
}
