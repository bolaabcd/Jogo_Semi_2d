package com.firstJogo.utils;

import com.firstJogo.main.GeradorEventos;

public class TempoMarker {//Guarda a função e o objeto-argumento da função pra quando o tempo passar.
//	private final FuncaoHandler fhand;
	
	private long tempolimite;
	private long temporegistrado;
	
	public TempoMarker(long tempolimite) {//Nulo indica o prório objeto
		this.tempolimite=tempolimite;
//		fhand=new FuncaoHandler(funcao,endereco_argumento,this);
	}
	
	public void ativar() {
		if(GeradorEventos.isOn(this))return;
		this.resetTemporegistrado();
		GeradorEventos.addMarker(this);
//		GeradorEventos.puroTempoHandler.addEvento(this);
//		GeradorEventos.entidadeTempoHandler.remEvento(this);
	}
	
	public void desativar() {
		if(!GeradorEventos.isOn(this))return;
		while(GeradorEventos.isOn(this))
			GeradorEventos.remMarker(this);
//			GeradorEventos.puroTempoHandler.remEvento(this);
//			GeradorEventos.entidadeTempoHandler.remEvento(this);
	}
	
	public long getTemporegistrado() {
		return temporegistrado;
	}
	public void resetTemporegistrado() {
		temporegistrado=System.nanoTime();
	}
	public boolean checkTempo() {
		if(passouTempolimite()) {
//			fhand.run();
//			this.resetTemporegistrado();
			return true;
		}
		return false;
	}
	public void setTempolimite(long tempolimite) {
		this.tempolimite = tempolimite;
	}
	
	
	private boolean passouTempolimite() {
		if(System.nanoTime()-temporegistrado>tempolimite)return true;
		return false;
	}
}
