package com.firstJogo.main;

import com.firstJogo.utils.GlobalVariables;

public class Main {
	public static void main(String[] args) {
		//Se tiver o argumento debug, seta para modo debug.
		if(args!=null)if(args.length>0)if(args[0].equals("debug"))GlobalVariables.debugue=true;
		
		//Cria as 3 principais Threads do Jogo
		Thread prep=new Thread(new Prepare());
		Thread rend=new Thread(new Renderer());
		Thread ev=new Thread(new GeradorEventos());
		
		//Seta as principais Threads para comparações posteriores
		GeradorEventos.main=ev;
		Renderer.main=rend;
		
		//Ativa a Thread Preparatória, responsável por ler as configurações e preparar as variáveis.
		//É executada antes de todas na Thread Inicial.
		prep.start();
		try {
			prep.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//Inicia as Threads de Renderização e de Eventos, respectivamente.
		rend.start();
		ev.start();
	}
	
}
