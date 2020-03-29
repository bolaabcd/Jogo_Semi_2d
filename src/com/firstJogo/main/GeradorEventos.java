package com.firstJogo.main;

import java.util.HashMap;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.Camera;
import com.firstJogo.visual.Janela;

public class GeradorEventos implements Runnable{
	public static HashMap<Integer,Funcao> botaomantido=new HashMap<Integer,Funcao>();
	public static HashMap<Integer,Funcao> botaoremovido=new HashMap<Integer,Funcao>();
	public static HashMap<Integer,Funcao> botaopressionado=new HashMap<Integer,Funcao>();
	private final short intperbloco=32;
	@Override
	public void run() {
		//CÓDIGO PARA PEGAR EVENTOS ADICIONAIS!
		
		System.out.println("Iniciando Loop de eventos");
		long nananterior=System.nanoTime();
		while(!Janela.getPrincipal().ShouldClose()) {
			for(int k:GlobalVariables.Keys)
				if(botaomantido.containsKey(k))botaomantido.get(k).run();
			//Ativando eventos de botão mantido!
			
			if(System.nanoTime()-nananterior>(long)1000000) {//Move o player a cada ms
				Camera.getMain().setPos(Camera.getMain().getPos().add(
						(float)(Entidade.player.getVelocModifier()*-Entidade.player.getMoverxy()[0]*((int)Entidade.player.getVeloc())*intperbloco*(double)(System.nanoTime()-nananterior)/1000000000),
						(float)(Entidade.player.getVelocModifier()*-Entidade.player.getMoverxy()[1]*((int)Entidade.player.getVeloc())*intperbloco*(double)(System.nanoTime()-nananterior)/1000000000),
						0));
				nananterior=System.nanoTime();
			}
			
			
		}
	}
}
