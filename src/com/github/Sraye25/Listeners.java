package com.github.Sraye25;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class Listeners implements Listener
{
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent event)
	{
	    Location loc = event.getVehicle().getLocation();
	    Block b = loc.getBlock();
	    @SuppressWarnings("deprecation")
		int id_mat = b.getTypeId();
	    System.out.println("Bloc : " + id_mat);
	}
}
