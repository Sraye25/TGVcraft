package com.github.Sraye25;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class TGVcraft extends JavaPlugin
{
	FileConfiguration config = getConfig();
	static Connection connection;
	static Statement state;
    
	/*
	 * Creer un fichier de config
	 */
	public void loadConfiguration()
	{
		config.addDefault("vitesse_moy",0.4);
        config.addDefault("vitesse_max",4.0); /* n*vitessenormale */
        config.addDefault("lent_si_vide",false); /*ralentie ou non si il y a rien dans le minecart*/
        config.addDefault("bloc_debut_tgv",41); /*id bloc debut zone*/
        config.addDefault("bloc_fin_tgv",22); /*id bloc fin zone*/
        config.addDefault("bloc_change_dir",42); /*id bloc de changement de direction*/
        config.addDefault("bloc_gare",49); /*id d'identification de départ gare*/
        config.addDefault("dir_active",true); /*active changement de direction*/
        config.addDefault("url_db","jdbc:mysql://localhost:3306/TGVcraft");
        config.addDefault("user","TGVcraft");
        config.addDefault("password","1234");
        config.options().copyDefaults(true);
		saveConfig();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
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
        System.out.println("[TGVcraft] Connection à MySql effectuée");
        
        try{
        	state = connection.createStatement();
        }catch(SQLException e){
            e.printStackTrace();
        }
		
		this.getServer().getPluginManager().registerEvents(new Listeners(this), this);
		this.getCommand("tgvcraft").setExecutor(new TGVcraftCommand(this,state));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	public void onDisable()
	{
        try{
           if(connection!=null && connection.isClosed())
           {
                connection.close();
                System.out.println("[TGVcraft] Fermeture de la connection à MySql effectuée");
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
	
}
