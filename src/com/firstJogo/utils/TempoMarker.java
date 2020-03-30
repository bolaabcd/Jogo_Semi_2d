package com.firstJogo.utils;

import com.firstJogo.regras.FuncaoTemporal;

public class TempoMarker {
//	public static HashMap<String,TempoMarker> temporizadores=new HashMap<String,TempoMarker>();
	
	private FuncaoTemporal funcao;
	private long tempolimite;
	private long temporegistrado;
	
	public TempoMarker(long tempolimite,FuncaoTemporal funcao) {
//		temporizadores.put(chave, this);
		this.funcao=funcao;
		this.tempolimite=tempolimite;
	}
	
	public long getTemporegistrado() {
		return temporegistrado;
	}
	public void resetTemporegistrado() {
		temporegistrado=System.nanoTime();
	}
	public void checkTempo() {
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
