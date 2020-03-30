package com.firstJogo.utils;

import java.util.HashMap;

public class TempoMarker {
	public static HashMap<String,TempoMarker> temporizadores=new HashMap<String,TempoMarker>();
	
	private long temporegistrado;

	public TempoMarker(String chave) {
		temporizadores.put(chave, this);
	}
	
	public long getTemporegistrado() {
		return temporegistrado;
	}
	public void resetTemporegistrado() {
		temporegistrado=System.nanoTime();
	}
}
