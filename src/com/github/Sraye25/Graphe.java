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
				if(result.getString("nom") != depart) liste.add(new Sommet(result.getString("nom"),-1));
				else liste.add(new Sommet(result.getString("nom"),0));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet result = state.executeQuery("SELECT id_inter FROM Inter GROUP BY id_inter");
			while(result.next()) liste.add(new Sommet(result.getString("id_inter"),-1));
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Allocation de liste : "+liste.size());
		afficheListe();
	}
	
	public void afficheListe()
	{
		System.out.println("Affiche liste :");
		int i=0;
		for(Sommet temp : liste)
		{
			System.out.println(i+" | "+temp.nom+" | "+temp.val+" | "+temp.label);
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
			if(!a.val && a.label!=-1)
			{
				if(prem) labmin = a.label;
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
			if(nom == liste.get(i).nom)
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
		for(Sommet s : liste)
		{
			if(nom == s.nom) trouver = true;
			if(!trouver) i++;
		}
		System.out.println("lel :"+i);
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
				ResultSet result = state.executeQuery("SELECT distance FROM Inter, Gare WHERE nom ='"+b.nom+"' AND id_inter='"+Integer.parseInt(a.nom)+"'");
				while(result.next()) dist = result.getInt("distance");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				ResultSet result = state.executeQuery("SELECT distance FROM Inter, Gare WHERE nom ='"+a.nom+"' AND id_inter='"+Integer.parseInt(b.nom)+"'");
				while(result.next()) dist = result.getInt("distance");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return dist;
	}
}



