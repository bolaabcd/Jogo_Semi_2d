package com.firstJogo.main;

import java.util.concurrent.ConcurrentHashMap;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;

public class GeradorEventos implements Runnable {
	public static Thread main;

	// CopyOnWrite porque as modificações podem vir de outras Threads.
	private static final ConcurrentHashMap<TempoMarker, FuncaoHandler> tempoHandlers = new ConcurrentHashMap<TempoMarker, FuncaoHandler>();

	public static void addTempoEvento(TempoMarker marcador, FuncaoHandler<?> func) {
		tempoHandlers.put(marcador, func);
	}

	public static boolean isOn(TempoMarker marc) {
		return tempoHandlers.containsKey(marc);
	}

	public static void forcedRemMarker(TempoMarker marc) {
		tempoHandlers.remove(marc);
	}

	// Objeto da Janela do Jogo
	private Janela principal;

	@Override
	public void run() {
		principal = Janela.getPrincipal();

		System.out.println("Iniciando Loop de eventos");
		while (!principal.ShouldClose()) {

			// Ativando eventos de tempo:
			for (TempoMarker marker : tempoHandlers.keySet())
				if (marker.passouTempolimite()) 
					tempoHandlers.get(marker).run();;

			GlobalVariables.TicksPorSegundo += 1;
		}
	}

}
