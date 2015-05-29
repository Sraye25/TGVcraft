package com.github.Sraye25;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Listeners implements Listener
{
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		// Get the player's location.
	    Location loc = event.getPlayer().getLocation();
	    // Sets loc to five above where it used to be. Note that this doesn't change the player's position.
	    loc.setZ(loc.getZ() - 1);
	    // Gets the block at the new location.
	    Block b = loc.getBlock();
	    
	    @SuppressWarnings("deprecation")
		int id_mat = b.getTypeId();
	    
	    System.out.println("Bloc : " + id_mat);
	}
}
