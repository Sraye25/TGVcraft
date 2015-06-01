package com.github.Sraye25;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
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
			  minecart.setMetadata("direction", new FixedMetadataValue(plugin,"dmgd"));
			  minecart.setMetadata("x_prec", new FixedMetadataValue(plugin,tronc(minecart.getLocation().getX())));
			  minecart.setMetadata("y_prec", new FixedMetadataValue(plugin,tronc(minecart.getLocation().getY())));
			  minecart.setMetadata("z_prec", new FixedMetadataValue(plugin,tronc(minecart.getLocation().getZ())));
		  }
		  /*System.out.println("Création d'un minecart :");
		  System.out.println("direction :" + avoirDirection(minecart));*/
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
		    	if(id_bloc_sous_minecart == plugin.getConfig().getInt("bloc_change_dir") && blocDifferent(minecart.getLocation(),avoirInt(minecart,"x_prec"),avoirInt(minecart,"y_prec"),avoirInt(minecart,"z_prec")))
			    {
			    	String dir = avoirDirection(minecart); /*direction*/
			    	
			    	loc.setY(loc.getY()+1);
			    	Block a_modif = loc.getBlock();
			    	if(a_modif.getType() == Material.RAILS && dir.length()!=0)
			    	{
			    		byte face = modifDirectionRails(a_modif,dir,minecart);
				    	a_modif.setData(face);
				    	dir = enlevePremLettre(dir);
			    		minecart.setMetadata("direction",new FixedMetadataValue(plugin,dir));
			    		/*if(dir.length() > 0) System.out.println("dir :" + avoirDirection(minecart).charAt(0));
			    		else System.out.println("dir : vide");*/
			    	}
			    	
			    }
			}
		    minecart.setMetadata("x_prec", new FixedMetadataValue(plugin,tronc(minecart.getLocation().getX())));
			minecart.setMetadata("y_prec", new FixedMetadataValue(plugin,tronc(minecart.getLocation().getY())));
			minecart.setMetadata("z_prec", new FixedMetadataValue(plugin,tronc(minecart.getLocation().getZ())));
		}
	}
	
	
	
	public String enlevePremLettre(String mot)
	{
		int taille = mot.length();
		String res;
		
		if(taille <= 1) res="";
		else res = mot.substring(1);
		
		return res;
	}
	
	public int tronc(double nb)
	{
		return (int)nb;
	}
	
	public double absolue(double nb)
	{
		if(nb < 0.0) { nb=-nb; }
		return nb;
	}
	
	public boolean blocDifferent(Location loc, int x, int y, int z)
	{
		boolean res=false;
		if(tronc(loc.getX()) != x || tronc(loc.getY()) != y || tronc(loc.getZ()) != z ) { res = true; }
		return res;
	}
	
	public int avoirInt(Minecart minecart, String meta)
	{
		List<MetadataValue> liste_dir = minecart.getMetadata(meta);
		int i=0;
    	while(liste_dir.get(i).getOwningPlugin() != plugin) /* tq direction n'est pas du plugin */
    	{
    		i++;
    	}
    	return liste_dir.get(i).asInt();
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
	
	public char avoirDirectionMinecart(Minecart minecart)
	{
		char dir=0;
		Vector velocity = minecart.getVelocity();
		if(absolue(velocity.getX()) > absolue(velocity.getZ()))
		{
			if(velocity.getX() > 0.0) dir = 'e';
			else dir = 'o';
		}
		else
		{
			if(velocity.getZ() > 0.0) dir = 's';
			else dir = 'n';
		}
		/*System.out.println("minecart dir : " + dir);*/
		return dir;
	}
	
	public byte modifDirectionRails(Block a_modif, String dir, Minecart minecart)
	{
		byte face = 0;
		char direction = avoirDirectionMinecart(minecart);
		
		if((direction == 'n' || direction == 's') && dir.charAt(0)=='m') { face = 0; }
		else if((direction == 'e' || direction =='o') && dir.charAt(0)=='m') { face = 1; }
		else if( direction == 'o' && dir.charAt(0)=='g' || direction == 'n' && dir.charAt(0)=='d') { face = 6; }
		else if( direction == 'e' && dir.charAt(0)=='d' || direction == 'n' && dir.charAt(0)=='g') { face = 7; }
		else if( direction == 'e' && dir.charAt(0)=='g' || direction == 's' && dir.charAt(0)=='d') { face = 8; }
		else if( direction == 'o' && dir.charAt(0)=='d' || direction == 's' && dir.charAt(0)=='g') { face = 9; }
		
		return face;
	}
}
