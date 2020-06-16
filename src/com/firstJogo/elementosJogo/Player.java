package com.firstJogo.elementosJogo;

import java.util.HashSet;

import com.firstJogo.elementosMundo.Entidade;

public class Player {
	public static Player mainPlayer;
	public static final HashSet<Player> players=new HashSet<Player>();
	
	public static boolean containsEnt(Entidade ent) {
		return players.stream().anyMatch(p->ent.equals(ent));
	}
	
	public final Entidade ent;
	public final String nomeID;
	public final Camera camera;
	
	public String nomeDisplay;

	public Player(Entidade ent, String nomeID, Camera camera) {
		this.ent = ent;
		this.nomeID = nomeID;
		this.camera = camera;
		this.nomeDisplay = nomeID;
	}
	
	
}
