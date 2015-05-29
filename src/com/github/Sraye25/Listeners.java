package com.github.Sraye25;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Listeners extends JavaPlugin implements Listener
{
	FileConfiguration config = getConfig();
	
	@EventHandler
	public void onVehicleCreate(VehicleCreateEvent event)
	{
	  Vehicle vehicle=event.getVehicle();
	  if (vehicle instanceof Minecart)
	  {
		  Minecart minecart=(Minecart)vehicle;
		  minecart.setSlowWhenEmpty(config.getBoolean("lent_si_vide"));
		  minecart.setMaxSpeed(minecart.getMaxSpeed()*config.getInt("vitesse_max"));
	  }
	}
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent event)
	{
		Vehicle vehicule = event.getVehicle();
		if(vehicule instanceof Minecart)/*est un minecart ?*/
		{
			Minecart minecart = (Minecart)vehicule;
			Location loc = minecart.getLocation();
		    loc.setY(loc.getY()-1); /*Avoir block sous le minecart*/
		    
		    Block b = loc.getBlock();
		    @SuppressWarnings("deprecation")
			int id_bloc_sous_minecart = b.getTypeId();
		    
		    System.out.println("Bloc : " + id_bloc_sous_minecart + " / vitesse_max : " + minecart.getMaxSpeed());
		}
	}
}
