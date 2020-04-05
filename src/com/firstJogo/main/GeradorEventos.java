package com.firstJogo.main;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;

public class GeradorEventos implements Runnable{
	public static Thread main;
	public static HashMap<Integer,Funcao<?>> botaomantido=new HashMap<Integer,Funcao<?>>();//Eventos externos!
	public static HashMap<Integer,Funcao<?>> botaoremovido=new HashMap<Integer,Funcao<?>>();
	public static HashMap<Integer,Funcao<?>> botaopressionado=new HashMap<Integer,Funcao<?>>();
	//Gatilho, o que fazer
	
	//Temq ser copyonwrite pq ele pode ser deletado enquanto estiver iterando!
	public static CopyOnWriteArrayList<TempoMarker> tempopassado=new  CopyOnWriteArrayList<TempoMarker>();//EVENTO INTERNO!!!
	public static Object chaveTempo=new Object();
	
	private Janela principal;
	@Override
	public void run() {
		principal=Janela.getPrincipal();
		
		System.out.println("Iniciando Loop de eventos");
		while(!principal.ShouldClose()) {
//			Janela.PollEvents();//Recolhendo eventos de botao!
			//Se nao tiver aqui buga...
			
			for(int k:GlobalVariables.Keys)
				if(botaomantido.containsKey(k))botaomantido.get(k).run(null);
			//Ativando eventos de botão mantido!
			
			for(TempoMarker marker:tempopassado) 
				marker.checkTempo();
			synchronized (chaveTempo) {
				chaveTempo.notify();
			}

//			for(Iterator<TempoMarker> iter=tempopassado.iterator();iter.hasNext();) 
//				iter.next().checkTempo();
//			tempopassado.forEach(o->o.checkTempo());
			//Ativando eventos de tempo passado!

			
			GlobalVariables.TicksPorSegundo+=1;
		}
	}
	
}
