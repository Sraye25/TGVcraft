package com.github.Sraye25;

public class Sommet
{
	public String nom;
	public boolean val;
	public int label;
	public Sommet precedent;
	
	public Sommet(String nom, int label)
	{
		this.nom=nom;
		this.val=false;
		this.label=label;
		this.precedent=null;
	}
	
	public void valider()
	{
		this.val = true;
	}
	
	public boolean estGare()
	{
		return !isInteger(nom);
	}
	
	public static boolean isInteger(String s)
	{
	    return isInteger(s,10);
	}

	public static boolean isInteger(String s, int radix)
	{
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
}
