package com.github.Sraye25;

import java.sql.Connection;
import java.sql.DriverManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TGVcraft extends JavaPlugin
{
	FileConfiguration config = getConfig();
    
	public void loadConfiguration()
	{
		config.addDefault("vitesse_moy",0.4);
        config.addDefault("vitesse_max",4.0); /* n*vitessenormale */
        config.addDefault("lent_si_vide",false); /*ralentie ou non si il y a rien dans le minecart*/
        config.addDefault("bloc_debut_tgv",41); /*id bloc debut zone*/
        config.addDefault("bloc_fin_tgv",22); /*id bloc fin zone*/
        config.addDefault("bloc_change_dir",42); /*id bloc de changement de direction*/
        config.addDefault("dir_active",true); /*active changement de direction*/
        config.addDefault("url_db","jdbc:postgresql://localhost:5432/TGVcraft");
        config.addDefault("user","TGVcraft");
        config.addDefault("password","xuty23");
        config.options().copyDefaults(true);
		saveConfig();
	}
    
	public Connection connectionDB()
	{
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Driver POSTGRESQL OK");
			String url = getConfig().getString("url_db");
			String user = getConfig().getString("user");
			String passwd = getConfig().getString("password");
			
			conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connection à postgresql effectuée !!!");
		} catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	@Override
	public void onEnable()
	{
		loadConfiguration();
		this.getServer().getPluginManager().registerEvents(new Listeners(this,connectionDB()), this);
	}
	
}
