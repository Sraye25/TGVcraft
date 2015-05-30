package com.github.Sraye25;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.Plugin;

public class Listeners implements Listener
{
	private Plugin plugin;
	
	/* Notez le constructeur car sinon nous ne pourrions pas obtenir la config ! 
	 * Nous ne pouvons obtenir la config uniquement avec un objet Plugin !
	 */
	public Listeners(Plugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onVehicleCreate(VehicleCreateEvent event)
	{
	  Vehicle vehicle=event.getVehicle();
	  if (vehicle instanceof Minecart)
	  {
		  Minecart minecart=(Minecart)vehicle;
		  minecart.setSlowWhenEmpty(plugin.getConfig().getBoolean("lent_si_vide"));
		  minecart.setMaxSpeed(minecart.getMaxSpeed()*plugin.getConfig().getInt("vitesse_max"));
	  }
	}
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent event)
	{
		Vehicle vehicule = event.getVehicle();
		if(vehicule.getType() == EntityType.MINECART)/*est un minecart ?*/
		{
			Minecart minecart = (Minecart)vehicule;
			Location loc = minecart.getLocation();
		    loc.setY(loc.getY()-1); /*Avoir block sous le minecart*/
		    
		    Block b = loc.getBlock();
		    @SuppressWarnings("deprecation")
			int id_bloc_sous_minecart = b.getTypeId();
		    
		    System.out.println("Bloc : " + id_bloc_sous_minecart + " / vitesse_max : " + minecart.getMaxSpeed() + " / vitesse : " + minecart.getVelocity().length());
		}
	}
}
