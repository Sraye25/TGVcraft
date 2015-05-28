package com.github.Sraye25;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TGVcraft extends JavaPlugin
{
	FileConfiguration config = getConfig();
    
	public void loadConfiguration()
	{
        config.addDefault("vitesse_max",8); /*Vitesse en blocs par seconde*/
        config.options().copyDefaults(true);
		saveConfig();
	}
        
	@Override
	public void onEnable()
	{
		loadConfiguration();
		System.out.println("Chargement de TGVcraft ...");
		System.out.println("Vitesse max :" + config.getInt("vitesse_max"));
	}
}
