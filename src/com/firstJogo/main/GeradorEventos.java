package com.firstJogo.main;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.firstJogo.estrutura.Janela;
import com.firstJogo.estrutura.KeyHandler;
import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;

public class GeradorEventos implements Runnable{
	public static Thread main;//Objeto da Thread de eventos
	//Callbacks dos botões mantidos,pressionados ou removidos, de acordo com o ID de cada botão (Integer).
	public static HashMap<Integer,Funcao<?>> botaomantido=new HashMap<Integer,Funcao<?>>();
	public static HashMap<Integer,Funcao<Boolean>> botaoremovido=new HashMap<Integer,Funcao<Boolean>>();
	public static HashMap<Integer,Funcao<Boolean>> botaopressionado=new HashMap<Integer,Funcao<Boolean>>();
	
	//CopyOnWrite porque as modificações podem vir de outras Threads.
	public static CopyOnWriteArrayList<TempoMarker> tempopassado=new  CopyOnWriteArrayList<TempoMarker>();//Cbacks de tempo
	
	//Objeto da Janela do Jogo
	private Janela principal;
	
	@Override
	public void run() {
		principal=Janela.getPrincipal();
		
		System.out.println("Iniciando Loop de eventos");
		while(!principal.ShouldClose()) {//Enquanto não tiver mandado fechar a Janela, executa os eventos
			//Ativando eventos de botão mantido:
			KeyHandler.ativarMantidos();

			//Ativando eventos de tempo:
			for(TempoMarker marker:tempopassado) 
				marker.checkTempo();//Confere se passou o tempo mínimo e,  se sim, xecuta a função adequada com o argumento adequado.
			
			GlobalVariables.TicksPorSegundo+=1;//Marca quantos Loops se passaram por segundo
		}
	}
	
}
