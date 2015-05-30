package com.github.Sraye25;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TGVcraft extends JavaPlugin
{
	FileConfiguration config = getConfig();
    
	public void loadConfiguration()
	{
		config.addDefault("vitesse_moy",0.4);
        config.addDefault("vitesse_max",1.0); /* n*vitessenormale */
        config.addDefault("lent_si_vide",true); /*ralentie ou non si il y a rien dans le minecart*/
        config.options().copyDefaults(true);
		saveConfig();
	}
        
	@Override
	public void onEnable()
	{
		loadConfiguration();
		this.getServer().getPluginManager().registerEvents(new Listeners(this), this);
		System.out.println("Chargement de TGVcraft ...");
	}
	
}
