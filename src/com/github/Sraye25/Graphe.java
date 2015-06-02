package com.github.Sraye25;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Graphe
{
	public ArrayList<Sommet> liste;
	public Statement state;
	
	public Graphe(Statement state,String depart)
	{
		this.state = state;
		liste = new ArrayList<Sommet>();
		try {
			ResultSet result = state.executeQuery("SELECT nom FROM Gare");
			while(result.next())
			{
				if(!depart.equals(result.getString("nom"))) liste.add(new Sommet(result.getString("nom"),Integer.MAX_VALUE));
				else liste.add(new Sommet(result.getString("nom"),0));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet result = state.executeQuery("SELECT id_inter FROM Inter GROUP BY id_inter");
			while(result.next()) liste.add(new Sommet(result.getString("id_inter"),Integer.MAX_VALUE));
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> dijkstra(String debut, String arrivee)
	{
		while(!tt_sommets_marquer())
		{
			Sommet a = sommetChoisit();
			a.valider();
			for(Sommet b : voisinNNMarquer(a))
			{
				if(b.label>a.label+distance(a,b))
				{
					b.label=a.label+distance(a,b);
					b.precedent = a;
				}
			}
		}
		ArrayList<String> res = new ArrayList<String>();
		Sommet n = avoirSommet(arrivee);
		while(n != avoirSommet(debut))
		{
			res.add(0,n.nom);
			n = n.precedent;
		}
		res.add(0,avoirSommet(debut).nom);
		return res;
	}
	
	public int dijkstraDistance(String arrivee)
	{
		while(!tt_sommets_marquer())
		{
			Sommet a = sommetChoisit();
			a.valider();
			for(Sommet b : voisinNNMarquer(a)) if(b.label>a.label+distance(a,b)) b.label=a.label+distance(a,b);
		}
		return avoirSommet(arrivee).label;
	}
	
	public void afficheListe1()
	{
		System.out.println("Affiche liste :");
		int i=0;
		for(Sommet temp : liste)
		{
			String aff;
			if(temp.precedent == null) aff="null";
			else aff=temp.precedent.nom;
			System.out.println(i+" | "+temp.nom+" | "+temp.label+" | "+aff);
			i++;
		}
	}
	
	public void afficheListe(ArrayList<String> list)
	{
		System.out.println("Affiche liste :");
		int i=0;
		for(String temp : list)
		{
			System.out.println(i+" | "+temp);
			i++;
		}
	}
	
	public Sommet sommetChoisit()
	{
		int labmin = 0;
		boolean prem = true;
		Sommet temp = null;
		for(Sommet a : liste)
		{		
			if(!a.val && a.label!=Integer.MAX_VALUE)
			{
				if(prem)
				{
					labmin = a.label;
					prem=false;
				}
				if(a.label <= labmin) temp = a;
			}
		}
		return temp;
	}
	
	public void valider(String nom)
	{
		boolean res=false;
		int i=0;
		while(!res && i < liste.size())
		{
			if(nom.equals(liste.get(i).nom))
			{
				res=true;
				liste.get(i).val = true;
			}
			i++;
		}
	}
	
	public ArrayList<Sommet> voisinNNMarquer(Sommet x)
	{
		ArrayList<Sommet> res = new ArrayList<Sommet>();
		if(x.estGare())
		{
			try {
				ResultSet result = state.executeQuery("SELECT id_inter FROM Gare, Inter WHERE Gare.nom='"+x.nom+"' AND Gare.id_gare=Inter.id_gare");
				while(result.next())
				{
					Sommet temp = avoirSommet(result.getString("id_inter"));
					if(!temp.val) res.add(temp);
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				ResultSet result = state.executeQuery("SELECT nom FROM Gare, Inter WHERE Gare.id_gare=Inter.id_gare AND id_inter='"+Integer.parseInt(x.nom)+"'");
				while(result.next())
				{
					Sommet temp = avoirSommet(result.getString("nom"));
					if(!temp.val) res.add(temp);
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}	
		return res;
	}
	
	public int indexSommet(String nom)
	{
		int i=0;
		boolean trouver = false;
		while(i<liste.size() && !trouver)
		{
			if(nom.equals(liste.get(i).nom)) trouver=true;
			i++;
		}
		i--;
		return i;
	}
	
	public Sommet avoirSommet(String nom)
	{
		return liste.get(indexSommet(nom));
	}
	
	public boolean tt_sommets_marquer()
	{
		boolean res = true;
		int i=0;
		
		while(res && i < liste.size())
		{
			if(!(boolean)liste.get(i).val) res=false;
			i++;
		}
		return res;
	}
	
	public int distance(Sommet a, Sommet b)
	{
		int dist = 0;
		if(!a.estGare()) /*Si a est une inter*/
		{
			try {
				ResultSet result = state.executeQuery("SELECT distance FROM Inter, Gare WHERE Gare.nom ='"+b.nom+"' AND Gare.id_gare=Inter.id_gare AND id_inter='"+Integer.parseInt(a.nom)+"'");
				while(result.next()) dist = result.getInt("distance");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				ResultSet result = state.executeQuery("SELECT distance FROM Inter, Gare WHERE Gare.nom ='"+a.nom+"' AND Gare.id_gare=Inter.id_gare AND id_inter='"+Integer.parseInt(b.nom)+"'");
				while(result.next()) dist = result.getInt("distance");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return dist;
	}
}



