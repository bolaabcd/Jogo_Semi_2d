package com.firstJogo.main;

import java.util.concurrent.ConcurrentHashMap;

import com.firstJogo.estrutura.Evento;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.estrutura.TempoEvento;
import com.firstJogo.utils.GlobalVariables;

public class GeradorEventos implements Runnable {
	public static Thread main;

	private static final ConcurrentHashMap<Object,Evento<?>> eventos=new ConcurrentHashMap<Object,Evento<?>>();
	//trocar pra lista de eventos...
	public static void addTempoEvento(Object chave,TempoEvento<?> tEv) {
		eventos.put(chave,tEv);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Evento<?>> T getEvento(Object chave) {
		try {
			return (T) eventos.get(chave);
		} catch (ClassCastException ce) {
			throw new IllegalArgumentException(ce);
		}
	}
	
	public static void remEvento(Evento<?> ev) {
		eventos.remove(ev);
	}
	public static void remEvento(Object chave) {
		eventos.remove(chave);
	}

	// Objeto da Janela do Jogo
	private Janela principal;
	
	@Override
	public void run() {
		principal = Janela.getPrincipal();

		System.out.println("Iniciando Loop de eventos");
		
		while (!principal.ShouldClose()) {
			for (Object chave : eventos.keySet())
				if (eventos.get(chave) != null)
					if (eventos.get(chave).podeExecutar())
						eventos.get(chave).executar();
//			if(GlobalVariables.TicksPorSegundo==2)
//				System.out.println(eventos.size());
			GlobalVariables.TicksPorSegundo += 1;
		}
	}

}
