package com.firstJogo.utils;

import com.firstJogo.utils.Funcao;

public class TempoMarker {
//	public static HashMap<String,TempoMarker> temporizadores=new HashMap<String,TempoMarker>();
	
	
	private long tempolimite;
	private long temporegistrado;
	
	public TempoMarker(long tempolimite) {
//		temporizadores.put(chave, this);
		this.tempolimite=tempolimite;
		this.resetTemporegistrado();
	}
	
	public long getTemporegistrado() {
		return temporegistrado;
	}
	public void resetTemporegistrado() {
		temporegistrado=System.nanoTime();
	}
	public void checkTempo(Funcao<TempoMarker> funcao) {
		if(passouTempolimite())funcao.run(this);
	}
	public void setTempolimite(long tempolimite) {
		this.tempolimite = tempolimite;
	}
	
	
	private boolean passouTempolimite() {
		if(System.nanoTime()-temporegistrado>tempolimite)return true;
		return false;
	}
}
