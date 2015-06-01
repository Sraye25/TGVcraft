package com.github.Sraye25;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;


public class TGVcraft extends JavaPlugin
{
	FileConfiguration config = getConfig();
	static Connection connection;
	static Statement state;
    
	public void loadConfiguration()
	{
		config.addDefault("vitesse_moy",0.4);
        config.addDefault("vitesse_max",4.0); /* n*vitessenormale */
        config.addDefault("lent_si_vide",false); /*ralentie ou non si il y a rien dans le minecart*/
        config.addDefault("bloc_debut_tgv",41); /*id bloc debut zone*/
        config.addDefault("bloc_fin_tgv",22); /*id bloc fin zone*/
        config.addDefault("bloc_change_dir",42); /*id bloc de changement de direction*/
        config.addDefault("dir_active",true); /*active changement de direction*/
        config.addDefault("url_db","jdbc:mysql://localhost:3306/TGVcraft");
        config.addDefault("user","TGVcraft");
        config.addDefault("password","xuty23");
        config.options().copyDefaults(true);
		saveConfig();
	}
	
	public void onEnable()
	{
		loadConfiguration();
		
		/* base de données*/
		
		try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
		
        try{
            connection = DriverManager.getConnection( getConfig().getString("url_db"), getConfig().getString("user"), getConfig().getString("password"));
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("Connection à MySql effectuée");
        
        try{
        	state = connection.createStatement();
        }catch(SQLException e){
            e.printStackTrace();
        }
		
		this.getServer().getPluginManager().registerEvents(new Listeners(this), this);
	}
	
	public void onDisable()
	{
        try{
           if(connection!=null && connection.isClosed())
           {
                connection.close();
           }
        }catch(Exception e){
                e.printStackTrace();
        }
        
        try{
            if(state!=null && state.isClosed())
            {
            	state.close();
            }
         }catch(Exception e){
                 e.printStackTrace();
         }
    }
	
	@EventHandler
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
				if(arg.length != 1)
				{
					p.sendMessage("Utilisation : /tgvcraft cgare <nom>");
				}
				else
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
			break;
			
			case "mgare":
				if(arg.length != 4)
				{
					p.sendMessage("Utilisation : /tgvcraft mgare <inter_gauche> <dist_gauche> <inter_droite> <dist_droite>");
				}
				else
				{
					try {
						ResultSet result = state.executeQuery("UPDATE Gare SET inter_gauche='"+args.get(2)+"',dist_gauche='"+args.get(3)+"',inter_droite='"+args.get(4)+"',dist_droite='"+args.get(5)+"' WHERE nom='"+args.get(1)+"'");
					}catch (SQLException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Impossible de modifier la gare "+args.get(1));
					}
				}
			break;
			case "cinter":
				if(arg.length != 4)
				{
					p.sendMessage("Utilisation : /tgvcraft cinter <gare_nord> <gare_sud> <gare_est> <gare_ouest> / gare vide = 0");
				}
				else
				{
					try {
						ResultSet result = state.executeQuery("INSERT INTO Inter VALUES ('"+args.get(1)+"','"+args.get(2)+"','"+args.get(3)+"','"+args.get(4)+"','"+args.get(5)+"')");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Impossible de créer une intersection ");
					}
				}
			break;
			case "minter":
				if(arg.length != 5)
				{
					p.sendMessage("Utilisation : /tgvcraft minter <id_inter> <gare_nord> <gare_sud> <gare_est> <gare_ouest> / gare vide = 0");
				}
				else
				{
					try {
						ResultSet result = state.executeQuery("UPDATE Inter SET n='"+args.get(2)+"',s='"+args.get(3)+"',e='"+args.get(4)+"',o='"+args.get(5)+"' WHERE id='"+args.get(1)+"'");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Impossible de modifier une intersection ");
					}
				}
			break;
		}
	}

	private int tronc(double y)
	{
		return (int)y;
	}
	
}
