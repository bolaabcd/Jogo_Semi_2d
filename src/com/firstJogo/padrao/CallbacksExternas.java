package com.firstJogo.padrao;

import org.lwjgl.glfw.GLFW;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.Mundos.Humano;
import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.KeyHandler;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.regras.DirecoesPadrao;
import com.firstJogo.regras.ExternalCallback;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.WorldRenderer;

public class CallbacksExternas implements ExternalCallback {
	//ATIVADO NA THREAD RENDERIZADORA
	public static void prepararBotoes() {
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_W, (isSintetico)->{
			//Ativando a movimentação do player para a direção adequada e iniciando o movimento
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.CIMA);
			Entidade.getPlayer().iniciarMovimento(1f);//TODO trocar esse 1f (futuramente) pelos mofificadores cabíveis de velocidade
			
			//Se o evento não for sintético
//			if(!((Boolean) isSintetico).booleanValue())
//				//Se tiver o botão oposto pressionado
//				if(KeyHandler.containsKey(GLFW.GLFW_KEY_S))
//					//Enviar Evento sintético de desativação do botão oposto
//					if(GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_S)!=null)GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_S).run(true);

		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_A, (isSintetico)->{
			//Ativando a movimentação do player para a direção adequada e iniciando o movimento
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.ESQUERDA);
			Entidade.getPlayer().iniciarMovimento(1f);
			
			//Se o evento não for sintético
//			if (!((Boolean) isSintetico).booleanValue())
//				//Se tiver o botão oposto pressionado
//				if (KeyHandler.containsKey(GLFW.GLFW_KEY_D))
//					//Enviar Evento sintético de desativação do botão oposto
//					if (GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_D)!=null)GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_D).run(true);

		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_S, (isSintetico)->{
			//Ativando a movimentação do player para a direção adequada e iniciando o movimento
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.BAIXO);
			Entidade.getPlayer().iniciarMovimento(1f);

			//Se o evento não for sintético
//			if (!((Boolean) isSintetico).booleanValue())
//				//Se tiver o botão oposto pressionado
//				if (KeyHandler.containsKey(GLFW.GLFW_KEY_W))
//					//Enviar Evento sintético de desativação do botão oposto
//					if (GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_W)!=null)GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_W).run(true);

		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_D, (isSintetico)->{
			//Ativando a movimentação do player para a direção adequada e iniciando o movimento
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.DIREITA);
			Entidade.getPlayer().iniciarMovimento(1f);

			//Se o evento não for sintético
//			if (!((Boolean) isSintetico).booleanValue())
//				//Se tiver o botão oposto pressionado
//				if (KeyHandler.containsKey(GLFW.GLFW_KEY_A))
//					//Enviar Evento sintético de desativação do botão oposto
//					if (GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_A)!=null)GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_A).run(true);

		});
		
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_LEFT_CONTROL, (isSintetico)->{
			try {
				//Ativar modo correr se for humano
				Humano player=(Humano) Entidade.getPlayer();
				player.modo_correr();
			}catch(ClassCastException c) {
				
			}
			
			//Se não for sintético, desativar control
//			if (!((Boolean) isSintetico).booleanValue())
//				if (KeyHandler.containsKey(GLFW.GLFW_KEY_LEFT_SHIFT))
//					if (GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_LEFT_SHIFT)!=null) {
//						KeyHandler.remove(GLFW.GLFW_KEY_LEFT_SHIFT,true);
//						GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_LEFT_SHIFT).run(true);
//						}
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_LEFT_SHIFT, (isSintetico)->{
			try {
				//Ativar modo agachar se for humano
				Humano player=(Humano) Entidade.getPlayer();
				player.modo_agachar();
			}catch(ClassCastException c) {
				
			}
			
			//Se não for sintético, desativar shift
//			if (!((Boolean) isSintetico).booleanValue())
//				if (KeyHandler.containsKey(GLFW.GLFW_KEY_LEFT_CONTROL))
//					if (GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_LEFT_CONTROL)!=null) {
//						KeyHandler.remove(GLFW.GLFW_KEY_LEFT_CONTROL,true);
//						GeradorEventos.botaoremovido.get(GLFW.GLFW_KEY_LEFT_CONTROL).run(true);
//						}
		});
		
		//TODO: aprimorar o spawnar humano
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_P, (nada)->{
			Humano testado=new Humano();
			testado.setMundopos(new long[] {0,0});
			WorldRenderer.main.getCriaturas().add(testado);
			new TempoMarker(1000000,(perseguidor)-> {
				Humano genti = (Humano) perseguidor;
				long[] playerpos=Entidade.getPlayer().getMundopos();
				long[] gentipos=genti.getMundopos();
				genti.setAngulo(Math.atan2(playerpos[1]-gentipos[1], playerpos[0]-gentipos[0]));
				genti.iniciarMovimento(1);
				genti.modo_agachar();
			},testado).ativar();
		});
		
		//Alteradores de visão da câmera do player
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_PAGE_DOWN, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth()-GlobalVariables.intperbloco, Camera.getMain().getHeight()-GlobalVariables.intperbloco);
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_PAGE_UP, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth()+GlobalVariables.intperbloco, Camera.getMain().getHeight()+GlobalVariables.intperbloco);
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_UP, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth(), Camera.getMain().getHeight()+GlobalVariables.intperbloco);
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_DOWN, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth(), Camera.getMain().getHeight()-GlobalVariables.intperbloco);
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_LEFT, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth()-GlobalVariables.intperbloco, Camera.getMain().getHeight());
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_RIGHT, (isSintetico)->{
			Camera.getMain().setSize(Camera.getMain().getWidth()+GlobalVariables.intperbloco, Camera.getMain().getHeight());
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_R, (isSintetico)->{
			Camera.getMain().setSize(GlobalVariables.intperbloco*16, GlobalVariables.intperbloco*16);
		});
		
		
		
		
		
		
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_LEFT_CONTROL, (nada)->{
			try {
				Humano player=(Humano) Entidade.getPlayer();
				PlayerRegras.resetMovModo(player);
			}catch(ClassCastException c) {
				c.printStackTrace();
				System.exit(1);
			}
		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_LEFT_SHIFT, (nada)->{
			try {
				Humano player=(Humano) Entidade.getPlayer();
//				if(player.getMovModo()==Humano.modos.AGACHADO)
//				player.setMovModo(Humano.modos.ANDANDO);
				PlayerRegras.resetMovModo(player);
//				if(KeyHandler.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL))player.setMovModo(Humano.modos.CORRENDO);

			}catch(ClassCastException c) {
				c.printStackTrace();
				System.exit(1);
			}
		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_W,(isSintetico)->{
//			Entidade.player.remMover("up");

			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.CIMA);
//			if(!((Boolean) isSintetico).booleanValue())
//			if(KeyHandler.containsKey(GLFW.GLFW_KEY_S))
//				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_S)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_S).run(true);
			//Se a pessoa apertar direções contrárias e soltar uma, essa linha lida com isso.
		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_A,(isSintetico)->{
//			Entidade.player.remMover("left");
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.ESQUERDA);
//			if(!((Boolean) isSintetico).booleanValue())
//			if(KeyHandler.containsKey(GLFW.GLFW_KEY_D))
//				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_D)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_D).run(true);

		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_S,(isSintetico)->{
//			Entidade.player.remMover("down");
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.BAIXO);
//			if(!((Boolean) isSintetico).booleanValue())
//			if(KeyHandler.containsKey(GLFW.GLFW_KEY_W))
//				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_W)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_W).run(true);

		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_D,(isSintetico)->{
//			Entidade.player.remMover("right");
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.DIREITA);
//			if(!((Boolean) isSintetico).booleanValue())
//			if(KeyHandler.containsKey(GLFW.GLFW_KEY_A))
//				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_A)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_A).run(true);

		});
		
		
		
		
		
		
		
		
		
		//TODO: CRIAR CLASSE PARA OS EVENTOS INTERNOS:
		new CameraMarker(1000000, (camera_Marker)->{//Move o Player, e cada milisegundo vai executar!
			CameraMarker marcador=(CameraMarker) camera_Marker;
			if(marcador.mundoPosAnterior==null)marcador.mundoPosAnterior=new float[] {Camera.getMain().getPos().x,Camera.getMain().getPos().y};
			Camera.getMain().setPos(Camera.getMain().getPos().add(
					(float)(-Entidade.getPlayer().getDirecModifiers()[0]*Entidade.getPlayer().getVelocModifier()*((int)Entidade.getPlayer().getVeloc())*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000),
					(float)(-Entidade.getPlayer().getDirecModifiers()[1]*Entidade.getPlayer().getVelocModifier()*((int)Entidade.getPlayer().getVeloc())*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000),

					0));
			float[] pospos=new float[] {Camera.getMain().getPos().x,Camera.getMain().getPos().y};
			if(
					(pospos[0]-marcador.mundoPosAnterior[0]>1||pospos[0]-marcador.mundoPosAnterior[0]<-1)
					||
					(pospos[1]-marcador.mundoPosAnterior[1]>1||pospos[1]-marcador.mundoPosAnterior[1]<-1)
					) {
				long[] playerprepos=Entidade.getPlayer().getMundopos();
				Entidade.getPlayer().setMundopos(new long[] {
//						playerprepos[0]+(long)Math.round((pospos[0]-prepos[0])*GlobalVariables.intperbloco),
//						playerprepos[1]+(long)Math.round((pospos[1]-prepos[1])*GlobalVariables.intperbloco)
						playerprepos[0]-(long)Math.round(pospos[0]-marcador.mundoPosAnterior[0]),
						playerprepos[1]-(long)Math.round(pospos[1]-marcador.mundoPosAnterior[1])
				});
//				System.out.println(Entidade.getPlayer().getMundopos()[0]);
				marcador.mundoPosAnterior=null;
			}
//			((TempoMarker)camera_Marker).resetTemporegistrado();
		},null).ativar();;
//		Camera_Marker.ativar();
		
		
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
				KeyHandler.add(key,false);
//				if(GeradorEventos.botaopressionado.get(key)!=null)GeradorEventos.botaopressionado.get(key).run(false);
			}else if(action==GLFW.GLFW_RELEASE) {
				KeyHandler.remove(key,false);
//				if(GeradorEventos.botaoremovido.get(key)!=null)GeradorEventos.botaoremovido.get(key).run(false);
			}
	}

	@Override
	public void CursorPosCallback() {
		
		
	}

//	@Override
//	public void ifon(int botao) {
//		if(botao==GLFW.GLFW_KEY_W) {
//			Camera.getMain().setPos(Camera.getMain().getPos().add(0,-2,0));
//		}
//		if(botao==GLFW.GLFW_KEY_S) {
//			Camera.getMain().setPos(Camera.getMain().getPos().add(0,2,0));
//		}
//		if(botao==GLFW.GLFW_KEY_D) {
//			Camera.getMain().setPos(Camera.getMain().getPos().add(-2,0,0));
//		}
//		if(botao==GLFW.GLFW_KEY_A) {
//			Camera.getMain().setPos(Camera.getMain().getPos().add(2,0,0));
//		}
//	}

}
