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
		if(GeradorEventos.tempopassado.contains(this))return;
//		System.out.println("OI");
		
//		GeradorEventos.chaveUsando=true;
//		synchronized (GeradorEventos.chaveIterando) {
//			if(!Thread.currentThread().equals(GeradorEventos.main)&&GeradorEventos.chaveIterando)
//				try {
//					GeradorEventos.chaveIterando.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//					System.exit(1);
//				}
//		}
		this.resetTemporegistrado();
		GeradorEventos.tempopassado.add(this);
//		GeradorEventos.mapa.put(this, new Object());
		
//		synchronized (GeradorEventos.chaveUsando) {
//			GeradorEventos.chaveUsando=false;
//			GeradorEventos.chaveUsando.notify();
//		}
//		isAtivo=true;
	}
	
	public void desativar() {
		if(!GeradorEventos.tempopassado.contains(this))return;
//		System.out.println("Desativando");
//		boolean contem=GeradorEventos.tempopassado.contains(this);
//		if(!GeradorEventos.tempopassado.remove(this))
//			System.out.println(contem);
//		GeradorEventos.tempopassado.remove(this);//BUGA QUANDO EXECUTA JUNTO COM O ITERATOR!
//		if (!GeradorEventos.main.equals(Thread.currentThread()))
//			synchronized (GeradorEventos.chaveObjeto) {
//				try {
//					GeradorEventos.chaveObjeto.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//					System.exit(1);
//				}
//			}
		
//		if (GeradorEventos.main.equals(Thread.currentThread()))
		
//		GeradorEventos.tempopassado.remove(this);
//		new LoopCond(()-> {
////			System.out.println(this);
//			return GeradorEventos.tempopassado.contains(this);
//			
//		},(nada)->{
//			System.out.println(
		while(GeradorEventos.tempopassado.contains(this))
			GeradorEventos.tempopassado.remove(this);
//			GeradorEventos.mapa.remove(this);
//			System.out.println(GeradorEventos.tempopassado.size());
//		}
//		).run();
//		else
//			GeradorEventos.todelete.add(this);
		
//		this.desativar=true;
		
//		isAtivo=false;
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
//		System.out.println(temporegistrado);
		if(System.nanoTime()-temporegistrado>tempolimite)return true;
		return false;
	}

//	public boolean isAtivo() {
//		return isAtivo;
//	}
}
