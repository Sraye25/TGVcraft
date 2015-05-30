package com.github.Sraye25;

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
        config.options().copyDefaults(true);
		saveConfig();
	}
        
	@Override
	public void onEnable()
	{
		loadConfiguration();
		this.getServer().getPluginManager().registerEvents(new Listeners(this), this);
	}
	
}
