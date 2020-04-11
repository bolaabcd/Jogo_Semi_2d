package com.firstJogo.utils;

import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.Funcao;

public class TempoMarker {//Guarda a função e o objeto-argumento da função pra quando o tempo passar.
	private final Object argumento;
	private final Funcao<Object> funcao;
	
	private long tempolimite;
	private long temporegistrado;
	
	public boolean desativar=false;;
	
	public TempoMarker(long tempolimite,Funcao<Object> funcao,Object endereco_argumento) {//Nulo indica o prório objeto
		this.tempolimite=tempolimite;
		this.funcao=funcao;
		if(endereco_argumento==null)this.argumento=this;
		else if(endereco_argumento instanceof Object[]) {
			Object[] e=(Object[])endereco_argumento;
			
			for(int i= 1;i<(int)e[0];i++)
				if(e[i]==null)
					e[i]=this;//Acho que ia dar errado enhanced for pq ia alterar o endereço do elemento sob o scope do for apenas!
			
			this.argumento=endereco_argumento;
		}
		else this.argumento=endereco_argumento;
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
		if(desativar)return;
		if(passouTempolimite()) {
			funcao.run(argumento);
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
