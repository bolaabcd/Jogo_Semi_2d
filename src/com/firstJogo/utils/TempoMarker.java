package com.firstJogo.utils;

import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.Funcao;

public class TempoMarker {//Guarda a função e o objeto-argumento da função pra quando o tempo passar.
	private final FuncaoHandler fhand;
	
	private long tempolimite;
	private long temporegistrado;
	
	public TempoMarker(long tempolimite,Funcao<Object> funcao,Object endereco_argumento) {//Nulo indica o prório objeto
		this.tempolimite=tempolimite;
		fhand=new FuncaoHandler(funcao,endereco_argumento,this);
	}
	
	public void ativar() {
		if(GeradorEventos.tempopassado.contains(this))return;
		this.resetTemporegistrado();
		GeradorEventos.tempopassado.add(this);
	}
	
	public void desativar() {
		if(!GeradorEventos.tempopassado.contains(this))return;
		while(GeradorEventos.tempopassado.contains(this))
			GeradorEventos.tempopassado.remove(this);
	}
	
	public long getTemporegistrado() {
		return temporegistrado;
	}
	public void resetTemporegistrado() {
		temporegistrado=System.nanoTime();
	}
	public void checkTempo() {
		if(passouTempolimite()) {
			fhand.run();
			this.resetTemporegistrado();
		}
	}
	public void setTempolimite(long tempolimite) {
		this.tempolimite = tempolimite;
	}
	
	
	private boolean passouTempolimite() {
		if(System.nanoTime()-temporegistrado>tempolimite)return true;
		return false;
	}
}
