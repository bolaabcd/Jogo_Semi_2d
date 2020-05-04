package com.firstJogo.main;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.estrutura.KeyHandler;
import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.TexturaAnimador;

public class GeradorEventos implements Runnable{
	public static Thread main;//Objeto da Thread de eventos
	//Callbacks dos botões mantidos,pressionados ou removidos, de acordo com o ID de cada botão (Integer).
	public static HashMap<Integer,Funcao<?>> botaomantido=new HashMap<Integer,Funcao<?>>();
	
	//CopyOnWrite porque as modificações podem vir de outras Threads.
	public static CopyOnWriteArrayList<TempoMarker> tempopassado=new  CopyOnWriteArrayList<TempoMarker>();//Cbacks de tempo
//	public static EventHandler<TempoMarker,TempoMarker> autotemperHandler=new EventHandler<TempoMarker,TempoMarker>();
//	public static HashMap<TempoMarker,EventHandler> tempoHandler=new HashMap<TempoMarker,EventHandler>();
	public static EventHandler<TempoMarker,TexturaAnimador> texturaEventHandler=new EventHandler<TempoMarker,TexturaAnimador>();
	public static EventHandler<TempoMarker,Entidade> entidadeTempoHandler=new EventHandler<TempoMarker,Entidade>();
	public static EventHandler<TempoMarker,TempoMarker> puroTempoHandler=new EventHandler<TempoMarker,TempoMarker>();
	
	//Objeto da Janela do Jogo
	private Janela principal;
	
	@Override
	public void run() {
		principal=Janela.getPrincipal();
		
//		long init=System.nanoTime();
		
		System.out.println("Iniciando Loop de eventos");
		while(!principal.ShouldClose()) {//Enquanto não tiver mandado fechar a Janela, executa os eventos
			
			//Ativando eventos de botão mantido: 1000000
			KeyHandler.ativarMantidos();

			//Ativando eventos de tempo:
			for(TempoMarker marker:tempopassado) 
				if(marker.checkTempo()) {
//					System.out.println(System.nanoTime()-init);
					texturaEventHandler.throwEvento(marker);
					entidadeTempoHandler.throwEvento(marker);
					puroTempoHandler.throwEvento(marker);
					marker.resetTemporegistrado();
				}
//			System.out.println(texturaEventHandler.getsize());
			//Confere se passou o tempo mínimo e,  se sim, executa a função adequada com o argumento adequado.
			
			GlobalVariables.TicksPorSegundo+=1;//Marca quantos Loops se passaram por segundo
		}
	}
	
}
