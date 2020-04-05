package com.firstJogo.padrao;

import org.lwjgl.glfw.GLFW;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.Mundos.Humano;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.regras.DirecoesPadrao;
import com.firstJogo.regras.ExternalCallback;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.Camera;

public class CallbacksExternas implements ExternalCallback {
	public static void prepararBotoes() {
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_W, (nada)->{
//			Entidade.player.setMover("up");
			PlayerRegras.setMoveDirection(Entidade.getPlayer(), DirecoesPadrao.CIMA);
			Entidade.getPlayer().iniciarMovimento(1f);//TODO trocar esse 1f (futuramente) pelos mofificadores cabíveis de velocidade
			try {
				Humano player=(Humano) Entidade.getPlayer();
				PlayerRegras.resetMovModo(player);
//				if(!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.ANDANDO);
			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_A, (nada)->{
//			Entidade.player.setMover("left");
			PlayerRegras.setMoveDirection(Entidade.getPlayer(), DirecoesPadrao.ESQUERDA);
			Entidade.getPlayer().iniciarMovimento(1f);
			try {
				Humano player=(Humano) Entidade.getPlayer();
				PlayerRegras.resetMovModo(player);
//				if(!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.ANDANDO);
			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_S, (nada)->{
//			Entidade.player.setMover("down");
			PlayerRegras.setMoveDirection(Entidade.getPlayer(), DirecoesPadrao.BAIXO);
			Entidade.getPlayer().iniciarMovimento(1f);
			try {
				Humano player=(Humano) Entidade.getPlayer();
				PlayerRegras.resetMovModo(player);
//				if(!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.ANDANDO);
			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_D, (nada)->{
//			Entidade.player.setMover("right");
			PlayerRegras.setMoveDirection(Entidade.getPlayer(), DirecoesPadrao.DIREITA);
			Entidade.getPlayer().iniciarMovimento(1f);
			try {
				Humano player=(Humano) Entidade.getPlayer();
				PlayerRegras.resetMovModo(player);
//				if(!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.ANDANDO);
			}catch(ClassCastException c) {
				
			}
		});
		
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_LEFT_CONTROL, (nada)->{
			try {
				Humano player=(Humano) Entidade.getPlayer();
				player.modo_correr();
			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_LEFT_SHIFT, (nada)->{
			try {
				Humano player=(Humano) Entidade.getPlayer();
				player.modo_agachar();
			}catch(ClassCastException c) {
				
			}
		});
		
		
		
		
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_LEFT_CONTROL, (nada)->{
			try {
				Humano player=(Humano) Entidade.getPlayer();
//				if(player.getMovModo()==Humano.modos.CORRENDO)
//				player.setMovModo(Humano.modos.ANDANDO);
				PlayerRegras.resetMovModo(player);
//				if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.AGACHADO);
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
//				if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL))player.setMovModo(Humano.modos.CORRENDO);

			}catch(ClassCastException c) {
				c.printStackTrace();
				System.exit(1);
			}
		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_W,(nada)->{
//			Entidade.player.remMover("up");

			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.CIMA);
			if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_S))
				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_S)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_S).run(null);
			//Se a pessoa apertar direções contrárias e soltar uma, essa linha lida com isso.
		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_A,(nada)->{
//			Entidade.player.remMover("left");
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.ESQUERDA);
			if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_D))
				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_D)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_D).run(null);

		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_S,(nada)->{
//			Entidade.player.remMover("down");
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.BAIXO);
			if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_W))
				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_W)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_W).run(null);

		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_D,(nada)->{
//			Entidade.player.remMover("right");
			PlayerRegras.remMoveDirection(Entidade.getPlayer(), DirecoesPadrao.DIREITA);
			if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_A))
				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_A)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_A).run(null);

		});
		
		
		
		
		
		
		
		
		
		//TODO: CRIAR CLASSE PARA OS EVENTOS INTERNOS:
		new TempoMarker(1000000, (camera_Marker)->{//Move o Player, e cada milisegundo vai executar!
			Camera.getMain().setPos(Camera.getMain().getPos().add(
					(float)(-Entidade.getPlayer().getDirecModifiers()[0]*Entidade.getPlayer().getVelocModifier()*((int)Entidade.getPlayer().getVeloc())*GlobalVariables.intperbloco*(double)(System.nanoTime()-((TempoMarker)camera_Marker).getTemporegistrado())/1000000000),
					(float)(-Entidade.getPlayer().getDirecModifiers()[1]*Entidade.getPlayer().getVelocModifier()*((int)Entidade.getPlayer().getVeloc())*GlobalVariables.intperbloco*(double)(System.nanoTime()-((TempoMarker)camera_Marker).getTemporegistrado())/1000000000),

					0));
//			((TempoMarker)camera_Marker).resetTemporegistrado();
		},null).ativar();;
//		Camera_Marker.ativar();
		
		
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
				GlobalVariables.Keys.add(key);
				if(GeradorEventos.botaopressionado.get(key)!=null)GeradorEventos.botaopressionado.get(key).run(null);
			}else if(action==GLFW.GLFW_RELEASE) {
				GlobalVariables.Keys.remove(key);
				if(GeradorEventos.botaoremovido.get(key)!=null)GeradorEventos.botaoremovido.get(key).run(null);
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
