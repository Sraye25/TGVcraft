package com.github.Sraye25;

import java.sql.ResultSet;
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
		System.out.println("Hopla !!!");
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
		
		switch(args.get(0))
		{
			case "cgare":
				if(arg.length != 1) p.sendMessage("Utilisation : /tgvcraft cgare <nom>");
				else execCgare(p,args);
			break;
			
			case "mgare":
				if(arg.length != 4) p.sendMessage("Utilisation : /tgvcraft mgare <inter_gauche> <dist_gauche> <inter_droite> <dist_droite>");
				else execMgare(p,args);
			break;
			case "cinter":
				if(arg.length != 4) p.sendMessage("Utilisation : /tgvcraft cinter <gare_nord> <gare_sud> <gare_est> <gare_ouest> / gare vide = 0");
				else execCinter(p,args);
			break;
			case "minter":
				if(arg.length != 5) p.sendMessage("Utilisation : /tgvcraft minter <id_inter> <gare_nord> <gare_sud> <gare_est> <gare_ouest> / gare vide = 0");
				else execMinter(p,args);
			break;
		}
	}
	
	public void execCgare(Player p, List<String> args)
	{
		Location loc = p.getEyeLocation();
		int x = tronc(loc.getX());
		int y = tronc(loc.getY());
		int z = tronc(loc.getZ());
		
		try {
			ResultSet result = state.executeQuery("INSERT INTO Gare VALUES (NULL,'"+args.get(1)+"',NULL,NULL,NULL,NULL,'"+x+"','"+y+"','"+z+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Impossible de créer la gare "+args.get(1));
		}
	}
	
	public void execMgare(Player p, List<String> args)
	{
		try {
			ResultSet result = state.executeQuery("UPDATE Gare SET inter_gauche='"+args.get(2)+"',dist_gauche='"+args.get(3)+"',inter_droite='"+args.get(4)+"',dist_droite='"+args.get(5)+"' WHERE nom='"+args.get(1)+"'");
		}catch (SQLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Impossible de modifier la gare "+args.get(1));
		}
	}
	
	public void execCinter(Player p, List<String> args)
	{
		try {
			ResultSet result = state.executeQuery("INSERT INTO Inter VALUES ('"+args.get(1)+"','"+args.get(2)+"','"+args.get(3)+"','"+args.get(4)+"','"+args.get(5)+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Impossible de créer une intersection ");
		}
	}
	
	public void execMinter(Player p, List<String> args)
	{
		try {
			ResultSet result = state.executeQuery("UPDATE Inter SET n='"+args.get(2)+"',s='"+args.get(3)+"',e='"+args.get(4)+"',o='"+args.get(5)+"' WHERE id='"+args.get(1)+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Impossible de modifier une intersection ");
		}
	}

	private int tronc(double y)
	{
		return (int)y;
	}
}
