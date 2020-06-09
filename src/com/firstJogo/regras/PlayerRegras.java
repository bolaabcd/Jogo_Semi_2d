package com.firstJogo.regras;

import org.lwjgl.glfw.GLFW;

import com.firstJogo.Handlers.KeyEventHandler;
import com.firstJogo.Mundos.Entidade;
import com.firstJogo.Mundos.Humano;
import com.firstJogo.estrutura.DirecoesPadrao;

//TODO: Transformar em interface para flexibilizar para players de classes não humanas!
public class PlayerRegras {
	private boolean todomarker;

	private static DirecoesPadrao[] getMoveDirection(Entidade e) {
		DirecoesPadrao[] res = new DirecoesPadrao[2];
		Double[] mods = e.getMovDirecModifiers();
		if(mods[0]==null)res[0]=null;
		else if (mods[0] > 0)
			res[0] = DirecoesPadrao.DIREITA;
		else if (mods[0] < 0)
			res[0] = DirecoesPadrao.ESQUERDA;
//		else
//			res[0]=null;
		if(mods[1]==null)res[1]=null;
		else if (mods[1] > 0)
			res[1] = DirecoesPadrao.CIMA;
		else if (mods[1] < 0)
			res[1] = DirecoesPadrao.BAIXO;
//		else
//			res[1]=null;
		
		return res;
	}
	public static void resetMovModo(Humano e) {
		Humano.modos modoAnterior=e.getMovModo();
		if(modoAnterior==Humano.modos.SPRINT||modoAnterior==Humano.modos.CORRENDO)
			if(KeyEventHandler.containsKey(GLFW.GLFW_KEY_LEFT_CONTROL))e.modo_correr();
			else if(KeyEventHandler.containsKey(GLFW.GLFW_KEY_LEFT_SHIFT)) e.modo_agachar();
			else e.modo_andar();
		else
			if(KeyEventHandler.containsKey(GLFW.GLFW_KEY_LEFT_SHIFT)) e.modo_agachar();
			else if(KeyEventHandler.containsKey(GLFW.GLFW_KEY_LEFT_CONTROL))e.modo_correr();
			else e.modo_andar();
	}
	public static void newMoveDirection(Entidade e, DirecoesPadrao dir) {
		DirecoesPadrao[] dirs = getMoveDirection(e);

		DirecoesPadrao horizontal = dirs[0];
		DirecoesPadrao vertical = dirs[1];
		dirs = null;

		if (dir == DirecoesPadrao.BAIXO) {
			if (horizontal == DirecoesPadrao.ESQUERDA)
				e.setAnguloMovimento(-3 * Math.PI / 4);
			else if (horizontal == DirecoesPadrao.DIREITA)
				e.setAnguloMovimento(-Math.PI / 4);
			else
				e.setAnguloMovimento(-Math.PI / 2);
		} else if (dir == DirecoesPadrao.CIMA) {
			if (horizontal == DirecoesPadrao.ESQUERDA)
				e.setAnguloMovimento(3 * Math.PI / 4);
			else if (horizontal == DirecoesPadrao.DIREITA)
				e.setAnguloMovimento(Math.PI / 4);
			else {
				e.setAnguloMovimento(Math.PI / 2);
				}
		} else if (dir == DirecoesPadrao.ESQUERDA) {
			if (vertical == DirecoesPadrao.CIMA)
				e.setAnguloMovimento(3 * Math.PI / 4);
			else if (vertical == DirecoesPadrao.BAIXO)
				e.setAnguloMovimento(-3 * Math.PI / 4);
			else
				e.setAnguloMovimento(Math.PI);
		} else if (dir == DirecoesPadrao.DIREITA) {
			if (vertical == DirecoesPadrao.CIMA)
				e.setAnguloMovimento(Math.PI / 4);
			else if (vertical == DirecoesPadrao.BAIXO)
				e.setAnguloMovimento(-Math.PI / 4);
			else
				e.setAnguloMovimento(0);
		}
	}

	public static void remMoveDirection(Entidade e, DirecoesPadrao dir) {
		DirecoesPadrao[] dirs = getMoveDirection(e);

		DirecoesPadrao horizontal = dirs[0];
		DirecoesPadrao vertical = dirs[1];
		
		dirs = null;

		if (dir == DirecoesPadrao.BAIXO) {
			if(vertical==DirecoesPadrao.CIMA)return;//Ignorando se estiver na direção oposta
			else if(vertical==DirecoesPadrao.BAIXO&&horizontal==null)e.pararMovimento();//Se era apenas baixo, parar.
			else if (horizontal == DirecoesPadrao.ESQUERDA)
				e.setAnguloMovimento(Math.PI);//Só esquerda
			else if (horizontal == DirecoesPadrao.DIREITA)
				e.setAnguloMovimento(0);//Só direita
//			else 
//				throw new IllegalStateException("ERRO NA ALTERAÇÃO DA DIREÇÃO DE MOVIMENTO DO PLAYER!");
			
			
		} else if (dir == DirecoesPadrao.CIMA) {
			if(vertical==DirecoesPadrao.BAIXO)return;//Ignorando se estiver na direção oposta
			else if(vertical==DirecoesPadrao.CIMA&&horizontal==null)e.pararMovimento();//Se era apenas cima, parar.
			else if (horizontal == DirecoesPadrao.ESQUERDA)
				e.setAnguloMovimento(Math.PI);//Só esquerda
			else if (horizontal == DirecoesPadrao.DIREITA)
				e.setAnguloMovimento(0);//Só direita
//			else {
//				throw new IllegalStateException("ERRO NA ALTERAÇÃO DA DIREÇÃO DE MOVIMENTO DO PLAYER!");
//			}
		} else if (dir == DirecoesPadrao.ESQUERDA) {
			if(horizontal==DirecoesPadrao.DIREITA)return;//Ignorando se estiver na direção oposta
			else if(horizontal==DirecoesPadrao.ESQUERDA&&vertical==null)e.pararMovimento();//Se era apenas esquerda, parar.
			else if (vertical == DirecoesPadrao.CIMA)
				e.setAnguloMovimento(Math.PI/2);//Só cima
			else if (vertical == DirecoesPadrao.BAIXO)
				e.setAnguloMovimento(-Math.PI/2);//Só baixo
//			else {
//				throw new IllegalStateException("ERRO NA ALTERAÇÃO DA DIREÇÃO DE MOVIMENTO DO PLAYER!");
//			}
		} else if (dir == DirecoesPadrao.DIREITA) {
			if(horizontal==DirecoesPadrao.ESQUERDA)return;//Ignorando se estiver na direção oposta
			else if(horizontal==DirecoesPadrao.DIREITA&&vertical==null)e.pararMovimento();//Se era apenas direita, parar.
			else if (vertical == DirecoesPadrao.CIMA)
				e.setAnguloMovimento(Math.PI/2);//Só cima
			else if (vertical == DirecoesPadrao.BAIXO)
				e.setAnguloMovimento(-Math.PI/2);//Só baixo
//			else {
//				throw new IllegalStateException("ERRO NA ALTERAÇÃO DA DIREÇÃO DE MOVIMENTO DO PLAYER!");
//			}
		}
	}

}
