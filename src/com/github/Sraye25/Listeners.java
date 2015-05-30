package com.github.Sraye25;

import java.util.List;
import java.lang.Math;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.material.Rails;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

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
	  if (vehicle.getType() == EntityType.MINECART)
	  {
		  Minecart minecart=(Minecart)vehicle;
		  
		  /* --- Partie Grande Vitesse --- */
		  minecart.setSlowWhenEmpty(plugin.getConfig().getBoolean("lent_si_vide"));
		  minecart.setMaxSpeed(plugin.getConfig().getDouble("vitesse_moy"));
		  
		  /* --- Partie direction --- */
		  if(plugin.getConfig().getBoolean("dir_active"))
		  {
			  minecart.setMetadata("direction", new FixedMetadataValue(plugin,"dgd"));
			  minecart.setMetadata("iteration", new FixedMetadataValue(plugin,0));
		  }
	  }
	}
	
	@SuppressWarnings("deprecation")
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
		    
			int id_bloc_sous_minecart = b.getTypeId();
		    
		    if(id_bloc_sous_minecart == plugin.getConfig().getInt("bloc_debut_tgv") && minecart.getMaxSpeed() == plugin.getConfig().getDouble("vitesse_moy")) /*Si on passe sur un bloc d'or*/
		    {
		    	minecart.setMaxSpeed(plugin.getConfig().getDouble("vitesse_moy")*plugin.getConfig().getDouble("vitesse_max"));
		    }
		    else if(id_bloc_sous_minecart == plugin.getConfig().getInt("bloc_fin_tgv") && minecart.getMaxSpeed() == plugin.getConfig().getDouble("vitesse_moy")*plugin.getConfig().getDouble("vitesse_max"))
		    {
		    	minecart.setMaxSpeed(plugin.getConfig().getDouble("vitesse_moy"));
		    }
		    
		    /* Partie direction */
		    if(plugin.getConfig().getBoolean("dir_active"))
			{
		    	if(id_bloc_sous_minecart == plugin.getConfig().getInt("bloc_change_dir"))
			    {
			    	String dir = avoirDirection(minecart); /*direction*/
			    	int ite = avoirIteration(minecart); /*iteration*/
			    	
			    	loc.setY(loc.getY()+1);
			    	Block a_modif = loc.getBlock();
			    	if(a_modif.getType() == Material.RAILS)
			    	{
			    		byte face = modifDirectionRails(a_modif,dir,ite,minecart);
			    		a_modif.setData(face);
			    		minecart.setMetadata("iteration",new FixedMetadataValue(plugin,ite+1));
			    	}
			    	
			    }
			}
		}
	}
	
	public double absolue(double nb)
	{
		if(nb < 0.0) { nb=-nb; }
		return nb;
	}
	
	public String avoirDirection(Minecart minecart)
	{
		List<MetadataValue> liste_dir = minecart.getMetadata("direction");
		int i=0;
    	while(liste_dir.get(i).getOwningPlugin() != plugin) /* tq direction n'est pas du plugin */
    	{
    		i++;
    	}
    	return liste_dir.get(i).asString();
	}
	
	public int avoirIteration(Minecart minecart)
	{
		List<MetadataValue> liste_dir = minecart.getMetadata("iteration");
		int i=0;
    	while(liste_dir.get(i).getOwningPlugin() != plugin) /* tq direction n'est pas du plugin */
    	{
    		i++;
    	}
    	return liste_dir.get(i).asInt();
	}
	
	public char avoirDirectionMinecart(Minecart minecart)
	{
		char dir=0;
		Vector velocity = minecart.getVelocity();
		if(absolue(velocity.getX()) > absolue(velocity.getZ()))
		{
			if(velocity.getX() > 0.0) { dir = 's';}
			else { dir = 'n'; }
		}
		else
		{
			if(velocity.getZ() > 0.0) { dir = 'o'; }
			else {dir = 'e'; }
		}
		return dir;
	}
	
	public byte modifDirectionRails(Block a_modif, String dir, int ite, Minecart minecart)
	{
		byte face = 0;
		char direction = avoirDirectionMinecart(minecart);
		
		if((direction == 'n' || direction == 's') && dir.charAt(ite)=='m') { face = 0; }
		else if((direction == 'e' && dir.charAt(ite)=='m') && dir.charAt(ite)=='m') { face = 1; }
		else if( direction == 'e' && dir.charAt(ite)=='g' || direction == 's' && dir.charAt(ite)=='d') { face = 6; }
		else if( direction == 'o' && dir.charAt(ite)=='d' || direction == 's' && dir.charAt(ite)=='g') { face = 7; }
		else if( direction == 'o' && dir.charAt(ite)=='g' || direction == 'n' && dir.charAt(ite)=='d') { face = 8; }
		else if( direction == 'e' && dir.charAt(ite)=='d' || direction == 'n' && dir.charAt(ite)=='g') { face = 9; }
		
		return face;
	}
}
