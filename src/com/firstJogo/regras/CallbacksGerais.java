package com.firstJogo.regras;

import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.Handlers.KeyEventHandler;
import com.firstJogo.Mundos.Entidade;
import com.firstJogo.Mundos.Humano;
import com.firstJogo.Mundos.MundoCarregado;
import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.DirecoesPadrao;
import com.firstJogo.estrutura.ExternalCallback;
import com.firstJogo.estrutura.NotFoundException;
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
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.CIMA);
			Entidade.getPlayer().iniciarMovimento();
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_A, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.ESQUERDA);
			Entidade.getPlayer().iniciarMovimento();
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_S, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.BAIXO);
			Entidade.getPlayer().iniciarMovimento();

		},null));
		botaopressionado.put(GLFW.GLFW_KEY_D, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.newMoveDirection(Entidade.getPlayer(), DirecoesPadrao.DIREITA);
			Entidade.getPlayer().iniciarMovimento();

		},null));

		botaopressionado.put(GLFW.GLFW_KEY_LEFT_CONTROL, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) Entidade.getPlayer();
				player.modo_correr();
			} catch (ClassCastException c) {

			}
		},null));
		botaopressionado.put(GLFW.GLFW_KEY_LEFT_SHIFT, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) Entidade.getPlayer();
				player.modo_agachar();
			} catch (ClassCastException c) {

			}
		},null));

		// TODO: aprimorar o spawnar humano
		botaopressionado.put(GLFW.GLFW_KEY_P, new FuncaoHandler<Boolean>((isSintetico) -> {
			Humano testado = new Humano(new Vector2f(0, 0),false);
			MundoCarregado.atual.getEntidades().add(testado);
			testado.setAnguloMovimento(Math.atan2(Entidade.getPlayer().getMundopos().y - testado.getMundopos().y,
					Entidade.getPlayer().getMundopos().x - testado.getMundopos().x));
			testado.iniciarMovimento();
			testado.modo_andar();

			TempoMarker marc=new TempoMarker(1000000);
			GeradorEventos.addTempoEvento(marc, new FuncaoHandler<Entidade>((Entidade perseguidor) -> {
				Entidade genti=perseguidor;
				Vector2f playerpos = Entidade.getPlayer().getMundopos();
				Vector2f gentipos = genti.getMundopos();
				genti.setAnguloMovimento(Math.atan2(playerpos.y - gentipos.y, playerpos.x - gentipos.x));
				genti.iniciarMovimento();
				marc.resetar();
			},testado));
//			GeradorEventos.addTempoEvento(Entidade.class, new FuncaoHandler<Entidade>((perseguidor) -> {
//				Entidade genti=perseguidor;
//				Vector2f playerpos = Entidade.getPlayer().getMundopos();
//				Vector2f gentipos = genti.getMundopos();
//				genti.setAngulo(Math.atan2(playerpos.y - gentipos.y, playerpos.x - gentipos.x));
//				genti.iniciarMovimento();
//			},testado), marc);
			marc.resetar();
			
			
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
				Humano player = (Humano) Entidade.getPlayer();
				PlayerRegras.resetMovModo(player);
			} catch (ClassCastException c) {

			}
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_LEFT_SHIFT, new FuncaoHandler<Boolean>((isSintetico) -> {
			try {
				Humano player = (Humano) Entidade.getPlayer();
				PlayerRegras.resetMovModo(player);
			} catch (ClassCastException c) {

			}
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_W, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.CIMA);
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_A, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.ESQUERDA);
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_S, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.BAIXO);
		},null));
		botaoremovido.put(GLFW.GLFW_KEY_D, new FuncaoHandler<Boolean>((isSintetico) -> {
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.DIREITA);
		},null));

		KeyEventHandler.addBotaoCallbacks(botaoremovido, botaopressionado);

	}

	public static void prepararTempos() {
		TempoMarker marctemp=new TempoMarker(1000000);
		GeradorEventos.addTempoEvento(marctemp, new FuncaoHandler<>((Marcador) -> {// Move o Player, e cada milisegundo vai executar!

			TempoMarker marcador = Marcador;
			if(!Entidade.getPlayer().isParado()) {
			float newx = Camera.getMain().getPos().x + (float) ((-Entidade.getPlayer().getForcedVelocModified().x
					- Entidade.getPlayer().getMovDirecModifiers()[0] * Entidade.getPlayer().getVelocModified())
					* GlobalVariables.intperbloco * (double) (System.nanoTime() - marcador.getTemporegistrado())
					/ 1000000000);
			float newy = Camera.getMain().getPos().y + (float) ((-Entidade.getPlayer().getForcedVelocModified().y
					- Entidade.getPlayer().getMovDirecModifiers()[1] * Entidade.getPlayer().getVelocModified())
					* GlobalVariables.intperbloco * (double) (System.nanoTime() - marcador.getTemporegistrado())
					/ 1000000000);

			if (Entidade.getPlayer().setMundopos(new Vector2f(-newx, Entidade.getPlayer().getMundopos().y))			)
				Camera.getMain().setPos(new Vector3f(newx, Camera.getMain().getPos().y, 0));
			
			if (Entidade.getPlayer().setMundopos(new Vector2f(Entidade.getPlayer().getMundopos().x, -newy)))

				Camera.getMain().setPos(new Vector3f(Camera.getMain().getPos().x, newy, 0));


//					System.out.println("X: "+Entidade.getPlayer().getMundopos().x);
//					System.out.println("Y: "+Entidade.getPlayer().getMundopos().y);
//					System.out.println("Xb: "+(long)Math.floor((Entidade.getPlayer().getMundopos().x/GlobalVariables.intperbloco)));//Bloco Coords
//					System.out.println("Yb: "+(long)Math.floor((Entidade.getPlayer().getMundopos().x/GlobalVariables.intperbloco)));
//					System.out.println("Xc: "+Math.floor((Entidade.getPlayer().getMundopos()[0]/GlobalVariables.intperbloco)/16+0.5f));//Chunk Coords
//					System.out.println("Yc: "+Math.floor((Entidade.getPlayer().getMundopos()[1]/GlobalVariables.intperbloco)/16+0.5f));

			}
			
			marcador.resetar();
		},marctemp));
		marctemp.resetar();

		
		TempoMarker marc=new TempoMarker(1000000000);
		GeradorEventos.addTempoEvento(marc, new FuncaoHandler<TempoMarker>((Marcador) -> {
			System.out.println("TPS: " + GlobalVariables.TicksPorSegundo);
			GlobalVariables.TicksPorSegundo = 0;
			marc.resetar();
		}, marc));
		marc.resetar();
		

	}

	@Override
	public void KeyCallback(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
			GLFW.glfwSetWindowShouldClose(window, true);
		else if (action == GLFW.GLFW_PRESS) {
			try {
				KeyEventHandler.botaoPressionado(key, false);
			} catch (NotFoundException e) {
				//TODO: Joga toda chave que chega, se tiver alguém querendo usar ele pega.
			}
		} else if (action == GLFW.GLFW_RELEASE) {
			try {
				KeyEventHandler.botaoRemovido(key, false);
			} catch (NotFoundException e) {
				//TODO: Joga toda chave que chega, se tiver alguém querendo usar ele pega.
			}
		}
	}

	@Override
	public void CursorPosCallback() {

	}

}
