package com.firstJogo.utils;

import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.Funcao;

public class TempoMarker {//Guarda a função e o objeto-argumento da função pra quando o tempo passar.
	private final Object argumento;
	private final Funcao<Object> funcao;
	
	private long tempolimite;
	private long temporegistrado;
	
	private boolean isAtivo=false;;
	
	public TempoMarker(long tempolimite,Funcao<Object> funcao,Object endereco_argumento) {//Nulo indica o prório objeto
//		temporizadores.put(chave, this);
		this.tempolimite=tempolimite;
//		this.resetTemporegistrado();
		this.funcao=funcao;
		if(endereco_argumento==null)this.argumento=this;
		else if(endereco_argumento instanceof Object[]) {
//			for(Object obj:(Object[])endereco_argumento)
//				if(obj==null)
//					obj=this;//Acho que ia dar errado pq ia alterar o endereço do obj apenas!
			Object[] e=(Object[])endereco_argumento;
			
			for(int i= 1;i<(int)e[0];i++)
				if(e[i]==null)
					e[i]=this;//Acho que ia dar errado pq ia alterar o endereço do obj apenas!
			
			this.argumento=endereco_argumento;
		}
		else this.argumento=endereco_argumento;
	}
	
	public void ativar() {
		if(isAtivo)return;
		this.resetTemporegistrado();
		GeradorEventos.tempopassado.add(this);
		isAtivo=true;
	}
	
	public void desativar() {
		if(!isAtivo)return;
//		if(GlobalVariables.contador==1) System.out.println(argumento);
		GeradorEventos.tempopassado.remove(this);
		isAtivo=false;
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

	public boolean isAtivo() {
		return isAtivo;
	}
}
