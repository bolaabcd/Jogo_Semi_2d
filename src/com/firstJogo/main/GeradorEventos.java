package com.firstJogo.main;

import java.util.HashMap;

import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.Janela;

public class GeradorEventos implements Runnable{
	public static HashMap<Integer,Funcao> eventosbotao=new HashMap<Integer,Funcao>();
	@Override
	public void run() {
		//CÓDIGO PARA PEGAR EVENTOS ADICIONAIS!
//		Janela.getPrincipal();
		
		System.out.println("Iniciando Loop de eventos");
		while(!Janela.getPrincipal().ShouldClose()) {
			for(int k:GlobalVariables.Keys)
				if(eventosbotao.containsKey(k))eventosbotao.get(k).run();
			
		}
	}
}
