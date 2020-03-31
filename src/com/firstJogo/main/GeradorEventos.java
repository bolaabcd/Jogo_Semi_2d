package com.firstJogo.main;

import java.util.HashMap;

import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;

public class GeradorEventos implements Runnable{
	public static HashMap<Integer,Funcao<?>> botaomantido=new HashMap<Integer,Funcao<?>>();//Eventos externos!
	public static HashMap<Integer,Funcao<?>> botaoremovido=new HashMap<Integer,Funcao<?>>();
	public static HashMap<Integer,Funcao<?>> botaopressionado=new HashMap<Integer,Funcao<?>>();
	//Gatilho, o que fazer
	
	public static HashMap<TempoMarker,Funcao<TempoMarker>> tempopassado=new HashMap<TempoMarker,Funcao<TempoMarker>>();//EVENTO INTERNO!!!
	
	@Override
	public void run() {
		//CÓDIGO PARA PEGAR EVENTOS ADICIONAIS!
		
		System.out.println("Iniciando Loop de eventos");
		while(!Janela.getPrincipal().ShouldClose()) {
			Janela.PollEvents();//Recolhendo eventos de botao!
			
			for(int k:GlobalVariables.Keys)
				if(botaomantido.containsKey(k))botaomantido.get(k).run(null);
			//Ativando eventos de botão mantido!
			
			for(TempoMarker marker:tempopassado.keySet()) 
				marker.checkTempo(tempopassado.get(marker));
			//Ativando eventos de tempo passado!

			
			GlobalVariables.TicksPorSegundo+=1;
		}
	}
	
//	public void lancarEvento(Evento e) {
//		
//	}
}
