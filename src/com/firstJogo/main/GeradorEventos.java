package com.firstJogo.main;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.firstJogo.Handlers.EventHandler;
import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.Handlers.KeyEventHandler;
import com.firstJogo.Mundos.Entidade;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.estrutura.TexturaAnimador;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;

public class GeradorEventos implements Runnable{
	public static Thread main;//Objeto da Thread de eventos
	//Callbacks dos botões mantidos,pressionados ou removidos, de acordo com o ID de cada botão (Integer).
//	public static HashMap<Integer,Funcao<?>> botaomantido=new HashMap<Integer,Funcao<?>>();
	public static EventHandler<Integer,Integer> botaomantidoHandler=new EventHandler<Integer,Integer>();
	
	//CopyOnWrite porque as modificações podem vir de outras Threads.
	private static CopyOnWriteArrayList<TempoMarker> tempopassado=new  CopyOnWriteArrayList<TempoMarker>();//Cbacks de tempo
//	public static EventHandler<TempoMarker,TempoMarker> autotemperHandler=new EventHandler<TempoMarker,TempoMarker>();
//	public static HashMap<TempoMarker,EventHandler> tempoHandler=new HashMap<TempoMarker,EventHandler>();
	public static EventHandler<TempoMarker,TexturaAnimador> texturaEventHandler=new EventHandler<TempoMarker,TexturaAnimador>();
	public static EventHandler<TempoMarker,Entidade> entidadeTempoHandler=new EventHandler<TempoMarker,Entidade>();
	public static EventHandler<TempoMarker,TempoMarker> puroTempoHandler=new EventHandler<TempoMarker,TempoMarker>();
	
//	public static ArrayList<EventHandler<TempoMarker,Object>> eventHandlers= new ArrayList<EventHandler<TempoMarker,Object>>(); 
	public static ArrayList<EventHandler<TempoMarker,?>> eventHandlers= new ArrayList<EventHandler<TempoMarker,?>>(); 
//	public static EventHandler<TempoMarker,?> tempoHandlers=new EventHandler<TempoMarker,?>();
	
	
	public static <T> void addTempoEvento(EventHandler<TempoMarker, T> handler, FuncaoHandler<T> func,
			TempoMarker marcador) {
		GeradorEventos.tempopassado.add(marcador);
		handler.addEvento(marcador, func);
	}

	public static boolean isOn(TempoMarker marc) {
		return tempopassado.contains(marc);
	}

	public static void addMarker(TempoMarker marc) {
		tempopassado.add(marc);
	}

	public static void remMarker(TempoMarker marc) {
		tempopassado.remove(marc);
//		for(EventHandler<TempoMarker,?> ev:eventHandlers)
//			ev.remEvento(marc);
	}
	public static void forcedRemMarker(TempoMarker marc) {
		tempopassado.remove(marc);
		for(EventHandler<TempoMarker,?> ev:eventHandlers)
			ev.remEvento(marc);
	}
	
	//Objeto da Janela do Jogo
	private Janela principal;
	
	@Override
	public void run() {
		principal=Janela.getPrincipal();
		
		eventHandlers.add(entidadeTempoHandler);
		eventHandlers.add(texturaEventHandler);
		eventHandlers.add(puroTempoHandler);
		
//		long init=System.nanoTime();
		
		System.out.println("Iniciando Loop de eventos");
		while(!principal.ShouldClose()) {//Enquanto não tiver mandado fechar a Janela, executa os eventos
			
			//Ativando eventos de botão mantido: 1000000
			KeyEventHandler.ativarMantidos();

			//Ativando eventos de tempo:
			for(TempoMarker marker:tempopassado) 
				if(marker.checkTempo()) {
//					System.out.println(System.nanoTime()-init);
					for(EventHandler<TempoMarker,?> ev:eventHandlers)
						ev.throwEvento(marker);
//					texturaEventHandler.throwEvento(marker);
//					entidadeTempoHandler.throwEvento(marker);
//					puroTempoHandler.throwEvento(marker);
					marker.resetTemporegistrado();
				}
//			System.out.println(entidadeTempoHandler.getsize());
			//Confere se passou o tempo mínimo e,  se sim, executa a função adequada com o argumento adequado.
			
			GlobalVariables.TicksPorSegundo+=1;//Marca quantos Loops se passaram por segundo
		}
	}
	
	
}
