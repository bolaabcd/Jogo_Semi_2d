package com.firstJogo.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.Janela;

public class GeradorEventos implements Runnable{
	public static HashMap<Integer,Funcao> botaomantido=new HashMap<Integer,Funcao>();
	public static HashMap<Integer,Funcao> botaoremovido=new HashMap<Integer,Funcao>();
	public static HashMap<Integer,Funcao> botaopressionado=new HashMap<Integer,Funcao>();
	public static ArrayList<Funcao> milisegundoPassado=new ArrayList<Funcao>();
	@Override
	public void run() {
		//CÓDIGO PARA PEGAR EVENTOS ADICIONAIS!
		
		System.out.println("Iniciando Loop de eventos");
		long nananterior=System.nanoTime();
		while(!Janela.getPrincipal().ShouldClose()) {
			for(int k:GlobalVariables.Keys)
				if(botaomantido.containsKey(k))botaomantido.get(k).run();
			//Ativando eventos de botão mantido!
			
			if(System.nanoTime()-nananterior>(long)1000000) {//Executa a cada ms.
				for(Funcao milifunc:milisegundoPassado)
					milifunc.run();
				nananterior=System.nanoTime();
			}
			
			
		}
	}
}
