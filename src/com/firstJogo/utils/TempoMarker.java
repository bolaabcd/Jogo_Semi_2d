package com.firstJogo.utils;

import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.Funcao;

public class TempoMarker {//Guarda a função e o objeto-argumento da função pra quando o tempo passar.
	private final Object argumento;
	private final Funcao<Object> funcao;
	
	private long tempolimite;
	private long temporegistrado;
	
	public TempoMarker(long tempolimite,Funcao<Object> funcao,Object endereco_argumento) {//Nulo indica o prório objeto
//		temporizadores.put(chave, this);
		this.tempolimite=tempolimite;
//		this.resetTemporegistrado();
		this.funcao=funcao;
		if(endereco_argumento==null)this.argumento=this;
		else this.argumento=endereco_argumento;
	}
	
	public void ativar() {
		this.resetTemporegistrado();
		GeradorEventos.tempopassado.add(this);
	}
	
	public void desativar() {
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
			funcao.run(argumento);
			this.resetTemporegistrado();
		}
	}
	public void setTempolimite(long tempolimite) {
		this.tempolimite = tempolimite;
	}
	
	
	private boolean passouTempolimite() {
//		System.out.println(temporegistrado);
		if(System.nanoTime()-temporegistrado>tempolimite)return true;
		return false;
	}
}
