package com.firstJogo.main;

import java.util.HashMap;

import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.Janela;

public class GeradorEventos implements Runnable{
	public static HashMap<Integer,Funcao> botaomantido=new HashMap<Integer,Funcao>();
	public static HashMap<Integer,Funcao> botaoremovido=new HashMap<Integer,Funcao>();
	public static HashMap<Integer,Funcao> botaopressionado=new HashMap<Integer,Funcao>();
	public static HashMap<String,TempoMarker> tempopassado=new HashMap<String,TempoMarker>();
	@Override
	public void run() {
		//CÓDIGO PARA PEGAR EVENTOS ADICIONAIS!
		
		System.out.println("Iniciando Loop de eventos");
		while(!Janela.getPrincipal().ShouldClose()) {
			for(int k:GlobalVariables.Keys)
				if(botaomantido.containsKey(k))botaomantido.get(k).run();
			//Ativando eventos de botão mantido!
			
			for(String chave:tempopassado.keySet()) {
				tempopassado.get(chave).checkTempo();}

			
			
		}
	}
}
