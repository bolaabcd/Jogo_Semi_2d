package com.firstJogo.Util;

public class TempoMarker {
	
	private long tempolimite;
	private long temporegistrado;
	
	public TempoMarker(long tempolimite) {
		this.tempolimite=tempolimite;
		this.temporegistrado=Long.MAX_VALUE;
	}
	public void ignoreMarker() {
		this.temporegistrado=Long.MAX_VALUE;
	}
	
	public long getTemporegistrado() {
		return temporegistrado;
	}
	public void resetar() {
		temporegistrado=System.nanoTime();
	}
	public boolean isIgnored() {
		return temporegistrado==Long.MAX_VALUE;
	}
	public void setTempolimite(long tempolimite) {
		this.tempolimite = tempolimite;
	}
	
	
	public boolean passouTempolimite() {
		if(System.nanoTime()-temporegistrado>tempolimite)return true;
		return false;
	}
}
