package com.firstJogo.regras;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.Mundos.Humano;
import com.firstJogo.Mundos.MundoCarregado;
import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.KeyHandler;
import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.WorldRenderer;

public class CallbacksGerais implements ExternalCallback {
	private boolean todomarker;

	//ATIVADO NA THREAD RENDERIZADORA
	public static void prepararBotoes() {
		HashMap<Integer,Funcao<Boolean>> botaoremovido=new HashMap<Integer,Funcao<Boolean>>();
		HashMap<Integer,Funcao<Boolean>> botaopressionado=new HashMap<Integer,Funcao<Boolean>>();
		botaopressionado.put(GLFW.GLFW_KEY_W, (isSintetico)->{
			//Ativando a movimentação do player para a direção adequada e iniciando o movimento
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.CIMA);
			Entidade.getPlayer().iniciarMovimento();
		});
		botaopressionado.put(GLFW.GLFW_KEY_A, (isSintetico)->{
			//Ativando a movimentação do player para a direção adequada e iniciando o movimento
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.ESQUERDA);
			Entidade.getPlayer().iniciarMovimento();
		});
		botaopressionado.put(GLFW.GLFW_KEY_S, (isSintetico)->{
			//Ativando a movimentação do player para a direção adequada e iniciando o movimento
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.BAIXO);
			Entidade.getPlayer().iniciarMovimento();

		});
		botaopressionado.put(GLFW.GLFW_KEY_D, (isSintetico)->{
			//Ativando a movimentação do player para a direção adequada e iniciando o movimento
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.DIREITA);
			Entidade.getPlayer().iniciarMovimento();

		});
		
		botaopressionado.put(GLFW.GLFW_KEY_LEFT_CONTROL, (isSintetico)->{
			try {
				//Ativar modo correr se for humano
				Humano player=(Humano) Entidade.getPlayer();
				player.modo_correr();
			}catch(ClassCastException c) {
				
			}
		});
		botaopressionado.put(GLFW.GLFW_KEY_LEFT_SHIFT, (isSintetico)->{
			try {
				//Ativar modo agachar se for humano
				Humano player=(Humano) Entidade.getPlayer();
				player.modo_agachar();
			}catch(ClassCastException c) {
				
			}
		});
		
		//TODO: aprimorar o spawnar humano
		botaopressionado.put(GLFW.GLFW_KEY_P, (nada)->{
			Humano testado=new Humano();
			testado.setMundopos(new float[] {0,0});
			MundoCarregado.atual.getEntidades().add(testado);
			testado.setAngulo(Math.atan2(Entidade.getPlayer().getMundopos()[1]-testado.getMundopos()[1], Entidade.getPlayer().getMundopos()[0]-testado.getMundopos()[0]));
			testado.iniciarMovimento();
			testado.modo_correr();
			new TempoMarker(1000000,(perseguidor)-> {
				Humano genti = (Humano) perseguidor;
				float[] playerpos=Entidade.getPlayer().getMundopos();
				float[] gentipos=genti.getMundopos();
				genti.setAngulo(Math.atan2(playerpos[1]-gentipos[1], playerpos[0]-gentipos[0]));
//				genti.iniciarMovimento();
//				genti.modo_correr();
			},testado).ativar();
		});
		
		//Alteradores de visão da câmera do player
		botaopressionado.put(GLFW.GLFW_KEY_PAGE_DOWN, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth()-GlobalVariables.intperbloco, Camera.getMain().getHeight()-GlobalVariables.intperbloco);
		});
		botaopressionado.put(GLFW.GLFW_KEY_PAGE_UP, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth()+GlobalVariables.intperbloco, Camera.getMain().getHeight()+GlobalVariables.intperbloco);
		});
		botaopressionado.put(GLFW.GLFW_KEY_UP, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth(), Camera.getMain().getHeight()+GlobalVariables.intperbloco);
		});
		botaopressionado.put(GLFW.GLFW_KEY_DOWN, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth(), Camera.getMain().getHeight()-GlobalVariables.intperbloco);
		});
		botaopressionado.put(GLFW.GLFW_KEY_LEFT, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth()-GlobalVariables.intperbloco, Camera.getMain().getHeight());
		});
		botaopressionado.put(GLFW.GLFW_KEY_RIGHT, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth()+GlobalVariables.intperbloco, Camera.getMain().getHeight());
		});
		botaopressionado.put(GLFW.GLFW_KEY_R, (isSintetico)->{
			Camera.getMain().setSize(480, 480);
//			GlobalVariables.contador=1;
		});
		botaopressionado.put(GLFW.GLFW_KEY_J, (isSintetico)->{
			Camera.getMain().setPos(Camera.getMain().getPos().add(1,0,0));
		});
		botaopressionado.put(GLFW.GLFW_KEY_K, (isSintetico)->{
			Camera.getMain().setPos(Camera.getMain().getPos().add(-1,0,0));
		});
		
		
		
		
		
		
		botaoremovido.put(GLFW.GLFW_KEY_LEFT_CONTROL, (nada)->{
			try {
				Humano player=(Humano) Entidade.getPlayer();
				PlayerRegras.resetMovModo(player);
			}catch(ClassCastException c) {

			}
		});
		botaoremovido.put(GLFW.GLFW_KEY_LEFT_SHIFT, (nada)->{
			try {
				Humano player=(Humano) Entidade.getPlayer();
				PlayerRegras.resetMovModo(player);
			}catch(ClassCastException c) {

			}
		});
		botaoremovido.put(GLFW.GLFW_KEY_W,(isSintetico)->{
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.CIMA);
		});
		botaoremovido.put(GLFW.GLFW_KEY_A,(isSintetico)->{
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.ESQUERDA);
		});
		botaoremovido.put(GLFW.GLFW_KEY_S,(isSintetico)->{
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.BAIXO);
		});
		botaoremovido.put(GLFW.GLFW_KEY_D,(isSintetico)->{
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.DIREITA);
		});
		
		KeyHandler.addBotaoCallbacks(botaoremovido, botaopressionado);
		
	}
	public static void prepararTempos() {
				new TempoMarker(1000000, (Marcador)->{//Move o Player, e cada milisegundo vai executar!
					Object[] obs=(Object[]) Marcador;
					//obs[0] é o número de elementos no Objeto[], sem contar o próprio obs[0]
					//obs[1] é o TempoMarker.
					
					TempoMarker marcador=(TempoMarker) obs[1];
					
					
					Camera.getMain().setPos(Camera.getMain().getPos().add(
							(float)(-Entidade.getPlayer().getDirecModifiers()[0]*Entidade.getPlayer().getVelocModified()*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000),
							(float)(-Entidade.getPlayer().getDirecModifiers()[1]*Entidade.getPlayer().getVelocModified()*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000),

							0));
					
					Entidade.getPlayer().setMundopos(new float[] {
							-Camera.getMain().getPos().x,
							-Camera.getMain().getPos().y
							});
//					System.out.println("X: "+Entidade.getPlayer().getMundopos()[0]);
//					System.out.println("Y: "+Entidade.getPlayer().getMundopos()[1]);
//					System.out.println("Xp: "+(long)Math.round((Entidade.getPlayer().getMundopos()[0]/GlobalVariables.intperbloco)));//Bloco Coords
//					System.out.println("Yp: "+(long)Math.round((Entidade.getPlayer().getMundopos()[1]/GlobalVariables.intperbloco)));
//					System.out.println("X: "+(float)Math.round((Entidade.getPlayer().getMundopos()[0]/GlobalVariables.intperbloco)/16));//Chunk Coords
//					System.out.println("Y: "+(float)Math.round((Entidade.getPlayer().getMundopos()[1]/GlobalVariables.intperbloco)/16));
					
				},new Object[] {2,null}).ativar();
				
				
				new TempoMarker(1000000000, (nada)-> {
					System.out.println("TPS: "+GlobalVariables.TicksPorSegundo);
					GlobalVariables.TicksPorSegundo=0;
				},null).ativar();;
				
				
	}
	@Override
	public void KeyCallback(long window, int key, int scancode, int action, int mods) {
			if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
				GLFW.glfwSetWindowShouldClose(window, true);
			else if(action==GLFW.GLFW_PRESS) {
				KeyHandler.botaoPressionado(key,false);
			}else if(action==GLFW.GLFW_RELEASE) {
				KeyHandler.botaoRemovido(key,false);
			}
	}

	@Override
	public void CursorPosCallback() {
		
		
	}


}
