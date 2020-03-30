package com.firstJogo.utils;

public class TempoMarker {
//	public static HashMap<String,TempoMarker> temporizadores=new HashMap<String,TempoMarker>();
	
	private Funcao funcao;
	private long tempolimite;
	private long temporegistrado;
	
	
	
	public TempoMarker(String chave,Funcao funcao) {
//		temporizadores.put(chave, this);
		this.funcao=funcao;
	}
	
	public long getTemporegistrado() {
		return temporegistrado;
	}
	public void resetTemporegistrado() {
		temporegistrado=System.nanoTime();
	}
	public void checkTempo() {
		if(passouTempolimite())funcao.run();
	}
	public void setTempolimite(long tempolimite) {
		this.tempolimite = tempolimite;
	}
	
	
	private boolean passouTempolimite() {
		if(System.nanoTime()-temporegistrado>tempolimite)return true;
		return false;
	}
}
