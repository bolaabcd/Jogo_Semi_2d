package com.firstJogo.regras;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.Mundos.Humano;
import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.KeyHandler;
import com.firstJogo.main.GeradorEventos;
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
			testado.setMundopos(new long[] {0,0});
			WorldRenderer.main.getCriaturas().add(testado);
			new TempoMarker(1000000,(perseguidor)-> {
				Humano genti = (Humano) perseguidor;
				long[] playerpos=Entidade.getPlayer().getMundopos();
				long[] gentipos=genti.getMundopos();
				genti.setAngulo(Math.atan2(playerpos[1]-gentipos[1], playerpos[0]-gentipos[0]));
				genti.iniciarMovimento();
				genti.modo_agachar();
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
			Camera.getMain().setSize(GlobalVariables.intperbloco*16, GlobalVariables.intperbloco*16);
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
				new TempoMarker(1000000, (MarcadorFloats)->{//Move o Player, e cada milisegundo vai executar!
					Object[] obs=(Object[]) MarcadorFloats;
					//obs[0] é o número de elementos no Objeto[], sem contar o próprio obs[0]
					//obs[1] é o TempoMarker.
					//obs[2] é o float[] de X e Y da posição anterior do mundo.
					
					TempoMarker marcador=(TempoMarker) obs[1];
					
					if(obs[2]==null)throw new IllegalStateException("Argumento inválido, deveria ser um array de floats.");
					if(((float[])obs[2]).length==0)obs[2]=new float[] {Camera.getMain().getPos().x,Camera.getMain().getPos().y};
					Camera.getMain().setPos(Camera.getMain().getPos().add(
							(float)(-Entidade.getPlayer().getDirecModifiers()[0]*Entidade.getPlayer().getVelocModified()*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000),
							(float)(-Entidade.getPlayer().getDirecModifiers()[1]*Entidade.getPlayer().getVelocModified()*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000),

							0));
					float[] pospos=new float[] {Camera.getMain().getPos().x,Camera.getMain().getPos().y};
					if(
							(pospos[0]-((float[])obs[2])[0]>1||pospos[0]-((float[])obs[2])[0]<-1)
							||
							(pospos[1]-((float[])obs[2])[1]>1||pospos[1]-((float[])obs[2])[1]<-1)
							) {
						long[] playerprepos=Entidade.getPlayer().getMundopos();
						Entidade.getPlayer().setMundopos(new long[] {
								playerprepos[0]-(long)Math.round(pospos[0]-((float[])obs[2])[0]),
								playerprepos[1]-(long)Math.round(pospos[1]-((float[])obs[2])[1])
						});
						obs[2]=new float[0];
					}
				},new Object[] {2,null,new float[0]}).ativar();
				
				
				new TempoMarker(1000000000, (nada)-> {
					System.out.println("TPS: "+GlobalVariables.TicksPorSegundo);
					GlobalVariables.TicksPorSegundo=0;
					System.out.println(GeradorEventos.tempopassado.size());
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
