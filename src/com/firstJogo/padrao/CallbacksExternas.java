package com.firstJogo.padrao;

import org.lwjgl.glfw.GLFW;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.Mundos.Humano;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.regras.ExternalCallback;
import com.firstJogo.utils.GlobalVariables;

public class CallbacksExternas implements ExternalCallback {
	public static void prepararBotoes() {
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_W, ()->{
			Entidade.player.setMover("up");
			try {
				Humano player=(Humano) Entidade.player;
				if(!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.ANDANDO);
			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_A, ()->{
			Entidade.player.setMover("left");
			try {
				Humano player=(Humano) Entidade.player;
				if(!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.ANDANDO);
			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_S, ()->{
			Entidade.player.setMover("down");
			try {
				Humano player=(Humano) Entidade.player;
				if(!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.ANDANDO);
			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_D, ()->{
			Entidade.player.setMover("right");
			try {
				Humano player=(Humano) Entidade.player;
				if(!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL)&&!GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.ANDANDO);
			}catch(ClassCastException c) {
				
			}
		});
		
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_LEFT_CONTROL, ()->{
			try {
				Humano player=(Humano) Entidade.player;
				player.setMovModo(Humano.modos.CORRENDO);
			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaopressionado.put(GLFW.GLFW_KEY_LEFT_SHIFT, ()->{
			try {
				Humano player=(Humano) Entidade.player;
				player.setMovModo(Humano.modos.AGACHADO);
			}catch(ClassCastException c) {
				
			}
		});
		
		
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_LEFT_CONTROL, ()->{
			try {
				Humano player=(Humano) Entidade.player;
				if(player.getMovModo()==Humano.modos.CORRENDO)
				player.setMovModo(Humano.modos.ANDANDO);
				
				if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT))player.setMovModo(Humano.modos.AGACHADO);
			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_LEFT_SHIFT, ()->{
			try {
				Humano player=(Humano) Entidade.player;
				if(player.getMovModo()==Humano.modos.AGACHADO)
				player.setMovModo(Humano.modos.ANDANDO);
				
				if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL))player.setMovModo(Humano.modos.CORRENDO);

			}catch(ClassCastException c) {
				
			}
		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_W,()->{
			Entidade.player.remMover("up");
			
			if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_S))
				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_S)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_S).run();
			//Se a pessoa apertar direções contrárias e soltar uma, essa linha lida com isso.
		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_A,()->{
			Entidade.player.remMover("left");
			if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_D))
				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_D)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_D).run();

		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_S,()->{
			Entidade.player.remMover("down");
			
			if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_W))
				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_W)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_W).run();

		});
		GeradorEventos.botaoremovido.put(GLFW.GLFW_KEY_D,()->{
			Entidade.player.remMover("right");
			if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_A))
				if(GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_A)!=null)GeradorEventos.botaopressionado.get(GLFW.GLFW_KEY_A).run();

		});
		
	}
	@Override
	public void KeyCallback(long window, int key, int scancode, int action, int mods) {
			if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
				GLFW.glfwSetWindowShouldClose(window, true);
			else if(action==GLFW.GLFW_PRESS) {
				GlobalVariables.Keys.add(key);
				if(GeradorEventos.botaopressionado.get(key)!=null)GeradorEventos.botaopressionado.get(key).run();
			}else if(action==GLFW.GLFW_RELEASE) {
				GlobalVariables.Keys.remove(key);
				if(GeradorEventos.botaoremovido.get(key)!=null)GeradorEventos.botaoremovido.get(key).run();
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
