package com.firstJogo.main;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.firstJogo.Handlers.EventHandler;
import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.Mundos.Entidade;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.estrutura.NotFoundException;
import com.firstJogo.estrutura.TexturaAnimador;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;

public class GeradorEventos implements Runnable{
	public static Thread main;
	
	//CopyOnWrite porque as modificações podem vir de outras Threads.
	private static final ConcurrentHashMap<TempoMarker, FuncaoHandler<Object>> tempoHandlers=new ConcurrentHashMap<>();
	
	
//	private static CopyOnWriteArrayList<TempoMarker> tempopassado=new  CopyOnWriteArrayList<TempoMarker>();//Cbacks de tempo
//	private static EventHandler<TempoMarker,TexturaAnimador> texturaEventHandler=new EventHandler<TempoMarker,TexturaAnimador>();
//	private static EventHandler<TempoMarker, Entidade> entidadeTempoHandler=new EventHandler<TempoMarker,Entidade>();
//	private static EventHandler<TempoMarker,TempoMarker> puroTempoHandler=new EventHandler<TempoMarker,TempoMarker>();
	
//	private static ArrayList<EventHandler<TempoMarker,?>> eventHandlers= new ArrayList<EventHandler<TempoMarker,?>>(); 
	
	

//	@SuppressWarnings("unchecked")
//	public static <T> void addTempoEvento(Class<T> tipo,FuncaoHandler<T> func,TempoMarker marcador) {		
//		if(tipo==TempoMarker.class)
//			puroTempoHandler.addEvento(marcador, (FuncaoHandler<TempoMarker>) func);
//		else if(tipo==Entidade.class)
//			entidadeTempoHandler.addEvento(marcador, (FuncaoHandler<Entidade>) func);
//		else if(tipo==TexturaAnimador.class)
//			texturaEventHandler.addEvento(marcador, (FuncaoHandler<TexturaAnimador>) func);
//	}
	public static void addTempoEvento(TempoMarker marcador,FuncaoHandler<Object> func) {
		tempoHandlers.put(marcador, func);
	}
	

	public static boolean isOn(TempoMarker marc) {
		return tempoHandlers.containsKey(marc);
	}

	public static void addMarker(TempoMarker marc) {
		tempopassado.add(marc);
	}

	public static void remMarker(TempoMarker marc) {
		tempopassado.remove(marc);
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
		
		
		System.out.println("Iniciando Loop de eventos");
		while(!principal.ShouldClose()) {
			

			//Ativando eventos de tempo:
			for(TempoMarker marker:tempopassado) 
				if(marker.checkTempo()) {
					for(EventHandler<TempoMarker,?> ev:eventHandlers) {
						try {
							ev.throwEvento(marker);
						} catch (NotFoundException e) {
							//Como são vários Handlers de tempo, ele temq jogar pra todos né.
							continue;
						}
						break;//Se um handler já ativou, pode ignorar o resto.
					}
					marker.resetTemporegistrado();
				}
			
			GlobalVariables.TicksPorSegundo+=1;//Marca quantos Loops se passaram por segundo
		}
	}
	
	
}
