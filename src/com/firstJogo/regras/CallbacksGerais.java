package com.firstJogo.regras;

import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.Handlers.KeyCallbackHandler;
import com.firstJogo.Mundos.Entidade;
import com.firstJogo.Mundos.Humano;
import com.firstJogo.Mundos.MundoCarregado;
import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.DirecoesPadrao;
import com.firstJogo.estrutura.ExternalCallback;
import com.firstJogo.estrutura.NotFoundException;
import com.firstJogo.estrutura.TempoEvento;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;

public class CallbacksGerais implements ExternalCallback {
	private boolean todomarker;

	// ATIVADO NA THREAD RENDERIZADORA
	public static void prepararBotoes() {
		
		HashMap<Integer, FuncaoHandler<Boolean>> botaoremovido = new HashMap<Integer, FuncaoHandler<Boolean>>();
		HashMap<Integer, FuncaoHandler<Boolean>> botaopressionado = new HashMap<Integer, FuncaoHandler<Boolean>>();
		botaopressionado.put(GLFW.GLFW_KEY_W, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(MundoCarregado.mainPlayer, DirecoesPadrao.CIMA);
			MundoCarregado.mainPlayer.iniciarMovimento();
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_A, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(MundoCarregado.mainPlayer, DirecoesPadrao.ESQUERDA);
			MundoCarregado.mainPlayer.iniciarMovimento();
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_S, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(MundoCarregado.mainPlayer, DirecoesPadrao.BAIXO);
			MundoCarregado.mainPlayer.iniciarMovimento();

		},null));
		botaopressionado.put(GLFW.GLFW_KEY_D, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(MundoCarregado.mainPlayer, DirecoesPadrao.DIREITA);
			MundoCarregado.mainPlayer.iniciarMovimento();

		},null));

//		botaopressionado.put(GLFW.GLFW_KEY_Y, new FuncaoHandler<Boolean>((isSintetico)->{
//			
//		},));
		
		botaopressionado.put(GLFW.GLFW_KEY_LEFT_CONTROL, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) MundoCarregado.mainPlayer;
				player.modo_correr();
			} catch (ClassCastException c) {

			}
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_LEFT_SHIFT, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) MundoCarregado.mainPlayer;
				player.modo_agachar();
			} catch (ClassCastException c) {

			}
		},null));

		// TODO: aprimorar o spawnar humano
		botaopressionado.put(GLFW.GLFW_KEY_P, new FuncaoHandler<Boolean>((isSintetico) -> {
			Humano testado = new Humano();
			testado.spawnar(new Vector2f(0, 0), MundoCarregado.atual, false);
			MundoCarregado.atual.getEntidades().add(testado);
			testado.setAnguloMovimento(Math.atan2(MundoCarregado.mainPlayer.getMundopos().y - testado.getMundopos().y,
					MundoCarregado.mainPlayer.getMundopos().x - testado.getMundopos().x));
			testado.iniciarMovimento();
			testado.modo_andar();

//			TempoMarker marc=new TempoMarker(1000000);
//			GeradorEventos.addTempoEvento(new TempoEvento<Entidade>(marc, new FuncaoHandler<Entidade>((Entidade perseguidor) -> {
//				Entidade genti=perseguidor;
//				Vector2f playerpos = MundoCarregado.mainPlayer.getMundopos();
//				Vector2f gentipos = genti.getMundopos();
//				genti.setAnguloMovimento(Math.atan2(playerpos.y - gentipos.y, playerpos.x - gentipos.x));
//				genti.iniciarMovimento();
//				marc.resetar();
//			},testado)));
			
			
//			GeradorEventos.addTempoEvento(Entidade.class, new FuncaoHandler<Entidade>((perseguidor) -> {
//				Entidade genti=perseguidor;
//				Vector2f playerpos = MundoCarregado.mainPlayer.getMundopos();
//				Vector2f gentipos = genti.getMundopos();
//				genti.setAngulo(Math.atan2(playerpos.y - gentipos.y, playerpos.x - gentipos.x));
//				genti.iniciarMovimento();
//			},testado), marc);
			
			
//			marc.resetar();
			
			
		},null));

		// Alteradores de visão da câmera do player
		botaopressionado.put(GLFW.GLFW_KEY_PAGE_DOWN, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera.getMain().setSize(Camera.getMain().getWidth() - GlobalVariables.intperbloco,
					Camera.getMain().getHeight() - GlobalVariables.intperbloco);
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_PAGE_UP, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera.getMain().setSize(Camera.getMain().getWidth() + GlobalVariables.intperbloco,
					Camera.getMain().getHeight() + GlobalVariables.intperbloco);
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_UP, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera.getMain().setSize(Camera.getMain().getWidth(),
					Camera.getMain().getHeight() + GlobalVariables.intperbloco);
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_DOWN, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera.getMain().setSize(Camera.getMain().getWidth(),
					Camera.getMain().getHeight() - GlobalVariables.intperbloco);
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_LEFT, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera.getMain().setSize(Camera.getMain().getWidth() - GlobalVariables.intperbloco,
					Camera.getMain().getHeight());
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_RIGHT, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera.getMain().setSize(Camera.getMain().getWidth() + GlobalVariables.intperbloco,
					Camera.getMain().getHeight());
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_R, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera.getMain().setSize(480, 480);
//			GlobalVariables.contador=1;
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_J, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera.getMain().setPos(Camera.getMain().getPos().add(1, 0, 0));
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_K, new FuncaoHandler<Boolean>((isSintetico) -> {
			Camera.getMain().setPos(Camera.getMain().getPos().add(-1, 0, 0));
		},null));

		botaoremovido.put(GLFW.GLFW_KEY_LEFT_CONTROL, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) MundoCarregado.mainPlayer;
				PlayerRegras.resetMovModo(player);
			} catch (ClassCastException c) {

			}
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_LEFT_SHIFT, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) MundoCarregado.mainPlayer;
				PlayerRegras.resetMovModo(player);
			} catch (ClassCastException c) {

			}
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_W, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(MundoCarregado.mainPlayer, DirecoesPadrao.CIMA);
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_A, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(MundoCarregado.mainPlayer, DirecoesPadrao.ESQUERDA);
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_S, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(MundoCarregado.mainPlayer, DirecoesPadrao.BAIXO);
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_D, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(MundoCarregado.mainPlayer, DirecoesPadrao.DIREITA);
		},null));

		KeyCallbackHandler.addBotaoCallbacks(botaoremovido, botaopressionado);

	}

	public static void prepararTempos() {
		
		
		TempoMarker marctemp=new TempoMarker(1000000L);
		GeradorEventos.addTempoEvento("Mover Player",new TempoEvento<TempoMarker>(marctemp, new FuncaoHandler<TempoMarker>((Marcador) -> {// Move o Player, e cada milisegundo vai executar!
			TempoMarker marcador = Marcador;
			if(!MundoCarregado.mainPlayer.isParado()) {
			float newx = Camera.getMain().getPos().x + (float) ((-MundoCarregado.mainPlayer.getForcedVelocModified().x
					- MundoCarregado.mainPlayer.getMovDirecModifiers()[0] * MundoCarregado.mainPlayer.getVelocModified())
					* GlobalVariables.intperbloco * (double) (System.nanoTime() - marcador.getTemporegistrado())
					/ 1000000000);
			float newy = Camera.getMain().getPos().y + (float) ((-MundoCarregado.mainPlayer.getForcedVelocModified().y
					- MundoCarregado.mainPlayer.getMovDirecModifiers()[1] * MundoCarregado.mainPlayer.getVelocModified())
					* GlobalVariables.intperbloco * (double) (System.nanoTime() - marcador.getTemporegistrado())
					/ 1000000000);

			if (MundoCarregado.mainPlayer.setMundopos(new Vector2f(-newx, MundoCarregado.mainPlayer.getMundopos().y))			)
				Camera.getMain().setPos(new Vector3f(newx, Camera.getMain().getPos().y, 0));
			
			if (MundoCarregado.mainPlayer.setMundopos(new Vector2f(MundoCarregado.mainPlayer.getMundopos().x, -newy)))

				Camera.getMain().setPos(new Vector3f(Camera.getMain().getPos().x, newy, 0));


//					System.out.println("X: "+MundoCarregado.mainPlayer.getMundopos().x);
//					System.out.println("Y: "+MundoCarregado.mainPlayer.getMundopos().y);
//					System.out.println("Xb: "+(long)Math.floor((MundoCarregado.mainPlayer.getMundopos().x/GlobalVariables.intperbloco)));//Bloco Coords
//					System.out.println("Yb: "+(long)Math.floor((MundoCarregado.mainPlayer.getMundopos().x/GlobalVariables.intperbloco)));
//					System.out.println("Xc: "+Math.floor((MundoCarregado.mainPlayer.getMundopos()[0]/GlobalVariables.intperbloco)/16+0.5f));//Chunk Coords
//					System.out.println("Yc: "+Math.floor((MundoCarregado.mainPlayer.getMundopos()[1]/GlobalVariables.intperbloco)/16+0.5f));

			}
			
			marcador.resetar();
		},marctemp)));
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
				//TODO: Joga toda chave que chega, se tiver alguém querendo usar ele pega.
			}
		} else if (action == GLFW.GLFW_RELEASE) {
			try {
				KeyCallbackHandler.botaoRemovido(key, false);
			} catch (NotFoundException e) {
				//TODO: Joga toda chave que chega, se tiver alguém querendo usar ele pega.
			}
		}
	}

	@Override
	public void CursorPosCallback() {

	}

}
