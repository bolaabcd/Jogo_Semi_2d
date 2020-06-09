package com.firstJogo.main;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.firstJogo.estrutura.Evento;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.estrutura.TempoEvento;
import com.firstJogo.utils.GlobalVariables;

public class GeradorEventos implements Runnable {
	public static Thread main;

	//Fazendo ficar Concurrent
	private static final Set<Evento<?>> eventos=Collections.newSetFromMap(new ConcurrentHashMap<Evento<?>,Boolean>());
//	private static final Set<WeakReference<? extends Evento<?>>> eventos=Collections.newSetFromMap(new ConcurrentHashMap<WeakReference<? extends Evento<?>>,Boolean>());
	
	public static void addTempoEvento(TempoEvento<?> tEv) {
		eventos.add(tEv);
	}
	
	public static void remEvento(Evento<?> ev) {
//		System.out.println(eventos.size());
		eventos.remove(ev);
//		System.out.println(eventos.size());
	}

	// Objeto da Janela do Jogo
	private Janela principal;
	
	@Override
	public void run() {
		principal = Janela.getPrincipal();

		System.out.println("Iniciando Loop de eventos");
		
		while (!principal.ShouldClose()) {
			for(Evento<?> e:eventos)
					if(e.podeExecutar())
						e.executar();
			
//			if(GlobalVariables.TicksPorSegundo==2)
//			System.out.println(eventos.size());
			GlobalVariables.TicksPorSegundo += 1;
		}
	}

}
