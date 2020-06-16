package com.firstJogo.padroes;

import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.Handlers.KeyCallbackHandler;
import com.firstJogo.Util.TempoMarker;
import com.firstJogo.elementosJogo.Camera;
import com.firstJogo.elementosJogo.ExternalCallback;
import com.firstJogo.elementosJogo.NotFoundException;
import com.firstJogo.elementosJogo.Player;
import com.firstJogo.elementosJogo.TempoEvento;
import com.firstJogo.elementosMundo.Humano;
import com.firstJogo.elementosMundo.MundoCarregado;
import com.firstJogo.main.GeradorEventos;

public class CallbacksGerais implements ExternalCallback {

	// ATIVADO NA THREAD RENDERIZADORA
	public static void prepararBotoes() {
		
		HashMap<Integer, FuncaoHandler<Boolean>> botaoremovido = new HashMap<Integer, FuncaoHandler<Boolean>>();
		HashMap<Integer, FuncaoHandler<Boolean>> botaopressionado = new HashMap<Integer, FuncaoHandler<Boolean>>();
		botaopressionado.put(GLFW.GLFW_KEY_W, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(Player.mainPlayer.ent, DirecoesPadrao.CIMA);
			Player.mainPlayer.ent.iniciarMovimento();
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_A, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(Player.mainPlayer.ent, DirecoesPadrao.ESQUERDA);
			Player.mainPlayer.ent.iniciarMovimento();
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_S, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(Player.mainPlayer.ent, DirecoesPadrao.BAIXO);
			Player.mainPlayer.ent.iniciarMovimento();

		},null));
		botaopressionado.put(GLFW.GLFW_KEY_D, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(Player.mainPlayer.ent, DirecoesPadrao.DIREITA);
			Player.mainPlayer.ent.iniciarMovimento();

		},null));

		
		botaopressionado.put(GLFW.GLFW_KEY_LEFT_CONTROL, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) Player.mainPlayer.ent;
				player.modo_correr();
			} catch (ClassCastException c) {

			}
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_LEFT_SHIFT, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) Player.mainPlayer.ent;
				player.modo_agachar();
			} catch (ClassCastException c) {

			}
		},null));

		botaopressionado.put(GLFW.GLFW_KEY_P, new FuncaoHandler<Boolean>((isSintetico) -> {
			Humano testado = new Humano();
			testado.spawnar(new Vector2f(0, 0), MundoCarregado.atual, false);
			MundoCarregado.atual.getEntidades().add(testado);
			testado.setAnguloMovimento(Math.atan2(Player.mainPlayer.ent.getMundopos().y - testado.getMundopos().y,
					Player.mainPlayer.ent.getMundopos().x - testado.getMundopos().x));
			testado.iniciarMovimento();
			testado.modo_andar();			
			
		},null));

		// Alteradores de visão da câmera do player
		botaopressionado.put(GLFW.GLFW_KEY_PAGE_DOWN, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera mainCamera=Player.mainPlayer.camera;
			mainCamera.setSize(mainCamera.getWidth() - GlobalVariables.intperbloco,
					mainCamera.getHeight() - GlobalVariables.intperbloco);
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_PAGE_UP, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera mainCamera=Player.mainPlayer.camera;
			mainCamera.setSize(mainCamera.getWidth() + GlobalVariables.intperbloco,
					mainCamera.getHeight() + GlobalVariables.intperbloco);
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_UP, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera mainCamera=Player.mainPlayer.camera;
			mainCamera.setSize(mainCamera.getWidth(),
					mainCamera.getHeight() + GlobalVariables.intperbloco);
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_DOWN, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera mainCamera=Player.mainPlayer.camera;
			mainCamera.setSize(mainCamera.getWidth(),
					mainCamera.getHeight() - GlobalVariables.intperbloco);
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_LEFT, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera mainCamera=Player.mainPlayer.camera;
			mainCamera.setSize(mainCamera.getWidth() - GlobalVariables.intperbloco,
					mainCamera.getHeight());
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_RIGHT, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera mainCamera=Player.mainPlayer.camera;
			mainCamera.setSize(mainCamera.getWidth() + GlobalVariables.intperbloco,
					mainCamera.getHeight());
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_R, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera mainCamera=Player.mainPlayer.camera;
			mainCamera.setSize(480, 480);
//			GlobalVariables.contador=1;
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_J, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera mainCamera=Player.mainPlayer.camera;
			mainCamera.setPos(mainCamera.getPos().add(1, 0, 0));
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_K, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera mainCamera=Player.mainPlayer.camera;
			mainCamera.setPos(mainCamera.getPos().add(-1, 0, 0));
		},null));

		botaoremovido.put(GLFW.GLFW_KEY_LEFT_CONTROL, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) Player.mainPlayer.ent;
				PlayerRegras.resetMovModo(player);
			} catch (ClassCastException c) {

			}
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_LEFT_SHIFT, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) Player.mainPlayer.ent;
				PlayerRegras.resetMovModo(player);
			} catch (ClassCastException c) {

			}
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_W, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(Player.mainPlayer.ent, DirecoesPadrao.CIMA);
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_A, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(Player.mainPlayer.ent, DirecoesPadrao.ESQUERDA);
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_S, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(Player.mainPlayer.ent, DirecoesPadrao.BAIXO);
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_D, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(Player.mainPlayer.ent, DirecoesPadrao.DIREITA);
		},null));
		
		botaoremovido.put(GLFW.GLFW_KEY_O, new FuncaoHandler<Boolean>((isSintetico)->{//O erro é algo nesse estilo
			Player.mainPlayer.ent.forcedSetMundoPos(new Vector2f(Player.mainPlayer.ent.getMundopos().x+30f,Player.mainPlayer.ent.getMundopos().y+30f));
			Player.mainPlayer.camera.setPos(new Vector3f(-Player.mainPlayer.ent.getMundopos().x,-Player.mainPlayer.ent.getMundopos().y,0));
		},null));

		KeyCallbackHandler.addBotaoCallbacks(botaoremovido, botaopressionado);

	}

	public static void prepararTempos() {
		
		
		TempoMarker marctemp = new TempoMarker(1000000L);
		GeradorEventos.addTempoEvento("Mover Player",
				new TempoEvento<TempoMarker>(marctemp, new FuncaoHandler<TempoMarker>((Marcador) -> {// Move o Player, e
																										// cada
																										// milisegundo
																										// vai executar!
					TempoMarker marcador = Marcador;
//				synchronized (Player.mainPlayer.ent) {
					Camera mainCamera = Player.mainPlayer.camera;
					Double[] movDirecModifiers=Player.mainPlayer.ent.getMovDirecModifiers();
					
					if (movDirecModifiers!=null) {
						float newx = mainCamera.getPos().x + (float) ((-Player.mainPlayer.ent.getForcedVelocModified().x
								- movDirecModifiers[0]
										* Player.mainPlayer.ent.getVelocModified())
								* GlobalVariables.intperbloco
								* (double) (System.nanoTime() - marcador.getTemporegistrado()) / 1000000000);
						float newy = mainCamera.getPos().y + (float) ((-Player.mainPlayer.ent.getForcedVelocModified().y
								- movDirecModifiers[1]
										* Player.mainPlayer.ent.getVelocModified())
								* GlobalVariables.intperbloco
								* (double) (System.nanoTime() - marcador.getTemporegistrado()) / 1000000000);

						if (Player.mainPlayer.ent
								.setMundoPos(new Vector2f(-newx, Player.mainPlayer.ent.getMundopos().y)))
							mainCamera.setPos(new Vector3f(newx, mainCamera.getPos().y, 0));

						if (Player.mainPlayer.ent
								.setMundoPos(new Vector2f(Player.mainPlayer.ent.getMundopos().x, -newy)))

							mainCamera.setPos(new Vector3f(mainCamera.getPos().x, newy, 0));

//					System.out.println("X: "+Player.mainPlayer.ent.getMundopos().x);
//					System.out.println("Y: "+Player.mainPlayer.ent.getMundopos().y);
//					System.out.println("Xb: "+(long)Math.floor((Player.mainPlayer.ent.getMundopos().x/GlobalVariables.intperbloco)));//Bloco Coords
//					System.out.println("Yb: "+(long)Math.floor((Player.mainPlayer.ent.getMundopos().x/GlobalVariables.intperbloco)));
//					System.out.println("Xc: "+Math.floor((Player.mainPlayer.ent.getMundopos()[0]/GlobalVariables.intperbloco)/16+0.5f));//Chunk Coords
//					System.out.println("Yc: "+Math.floor((Player.mainPlayer.ent.getMundopos()[1]/GlobalVariables.intperbloco)/16+0.5f));
					}
//				}

					marcador.resetar();
				}, marctemp)));
		marctemp.resetar();
		
		TempoMarker marc=new TempoMarker(1000000000);
		GeradorEventos.addTempoEvento("Imprimir TPS",new TempoEvento<TempoMarker>(marc, new FuncaoHandler<TempoMarker>((Marcador) -> {
			System.out.println("TPS: " + GlobalVariables.TicksPorSegundo);
			GlobalVariables.TicksPorSegundo = 0;
			marc.resetar();
		}, marc)));
		marc.resetar();
		

	}

	@Override
	public void KeyCallback(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
			GLFW.glfwSetWindowShouldClose(window, true);
		else if (action == GLFW.GLFW_PRESS) {
			try {
				KeyCallbackHandler.botaoPressionado(key, false);
			} catch (NotFoundException e) {

			}
		} else if (action == GLFW.GLFW_RELEASE) {
			try {
				KeyCallbackHandler.botaoRemovido(key, false);
			} catch (NotFoundException e) {

			}
		}
	}

	@Override
	public void CursorPosCallback() {

	}

}
