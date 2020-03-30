package com.firstJogo.padrao;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.regras.DirecoesPadrao;

public class PlayerRegras {
	private static DirecoesPadrao[] getMoveDirection(Entidade e) {
		DirecoesPadrao[] res = new DirecoesPadrao[2];
		double[] mods = e.getDirecModifiers();
//		System.out.println(e.getAngulo()/Math.PI);
//		System.out.println(mods[0]);
//		System.out.println(mods[1]);
//		System.out.println(e.getAngulo());
		if (mods[0] > 0)
			res[0] = DirecoesPadrao.DIREITA;
		else if (mods[0] < 0)
			res[0] = DirecoesPadrao.ESQUERDA;

		if (mods[1] > 0)
			res[1] = DirecoesPadrao.CIMA;
		else if (mods[1] < 0)
			res[1] = DirecoesPadrao.BAIXO;
		
		return res;
	}

	public static void setMoveDirection(Entidade e, DirecoesPadrao dir) {
		DirecoesPadrao[] dirs = getMoveDirection(e);

		DirecoesPadrao horizontal = dirs[0];
		DirecoesPadrao vertical = dirs[1];
		dirs = null;
		
//		System.out.println(horizontal);
//		System.out.println(vertical);

		if (dir == DirecoesPadrao.BAIXO) {
			if (horizontal == DirecoesPadrao.ESQUERDA)
				e.setAngulo(-3 * Math.PI / 4);
			else if (horizontal == DirecoesPadrao.DIREITA)
				e.setAngulo(-Math.PI / 4);
			else
				e.setAngulo(-Math.PI / 2);
		} else if (dir == DirecoesPadrao.CIMA) {
			if (horizontal == DirecoesPadrao.ESQUERDA)
				e.setAngulo(3 * Math.PI / 4);
			else if (horizontal == DirecoesPadrao.DIREITA)
				e.setAngulo(Math.PI / 4);
			else
				e.setAngulo(Math.PI / 2);
		} else if (dir == DirecoesPadrao.ESQUERDA) {
			if (vertical == DirecoesPadrao.CIMA)
				e.setAngulo(3 * Math.PI / 4);
			else if (vertical == DirecoesPadrao.BAIXO)
				e.setAngulo(-3 * Math.PI / 4);
			else
				e.setAngulo(Math.PI);
		} else if (dir == DirecoesPadrao.DIREITA) {
			if (vertical == DirecoesPadrao.CIMA)
				e.setAngulo(Math.PI / 4);
			else if (vertical == DirecoesPadrao.BAIXO)
				e.setAngulo(-Math.PI / 4);
			else
				e.setAngulo(0);
		}
	}

	public static void remMoveDirection(Entidade e, DirecoesPadrao dir) {
		DirecoesPadrao[] dirs = getMoveDirection(e);

		DirecoesPadrao horizontal = dirs[0];
		DirecoesPadrao vertical = dirs[1];
		
//		System.out.println(horizontal);
//		System.out.println(vertical);
		dirs = null;

		if (dir == DirecoesPadrao.BAIXO) {
			if(vertical==DirecoesPadrao.CIMA)return;//Ignorando se estiver na direção oposta
			else if(vertical==DirecoesPadrao.BAIXO&&horizontal==null)e.pararMovimento();//Se era apenas baixo, parar.
			else if (horizontal == DirecoesPadrao.ESQUERDA)
				e.setAngulo(Math.PI);//Só esquerda
			else if (horizontal == DirecoesPadrao.DIREITA)
				e.setAngulo(0);//Só direita
			else {
				System.out.println("ERRO NA ALTERAÇÃO DA DIREÇÃO DE MOVIMENTO DO PLAYER!");
				System.exit(1);
			}
			
		} else if (dir == DirecoesPadrao.CIMA) {
			if(vertical==DirecoesPadrao.BAIXO)return;//Ignorando se estiver na direção oposta
			else if(vertical==DirecoesPadrao.CIMA&&horizontal==null)e.pararMovimento();//Se era apenas cima, parar.
			else if (horizontal == DirecoesPadrao.ESQUERDA)
				e.setAngulo(Math.PI);//Só esquerda
			else if (horizontal == DirecoesPadrao.DIREITA)
				e.setAngulo(0);//Só direita
			else {
				System.out.println("ERRO NA ALTERAÇÃO DA DIREÇÃO DE MOVIMENTO DO PLAYER!");
				System.exit(1);
			}
		} else if (dir == DirecoesPadrao.ESQUERDA) {
			if(horizontal==DirecoesPadrao.DIREITA)return;//Ignorando se estiver na direção oposta
			else if(horizontal==DirecoesPadrao.ESQUERDA&&vertical==null)e.pararMovimento();//Se era apenas esquerda, parar.
			else if (vertical == DirecoesPadrao.CIMA)
				e.setAngulo(Math.PI/2);//Só cima
			else if (vertical == DirecoesPadrao.BAIXO)
				e.setAngulo(-Math.PI/2);//Só baixo
			else {
				System.out.println("ERRO NA ALTERAÇÃO DA DIREÇÃO DE MOVIMENTO DO PLAYER!");
				System.exit(1);
			}
		} else if (dir == DirecoesPadrao.DIREITA) {
			if(horizontal==DirecoesPadrao.ESQUERDA)return;//Ignorando se estiver na direção oposta
			else if(horizontal==DirecoesPadrao.DIREITA&&vertical==null)e.pararMovimento();//Se era apenas direita, parar.
			else if (vertical == DirecoesPadrao.CIMA)
				e.setAngulo(Math.PI/2);//Só cima
			else if (vertical == DirecoesPadrao.BAIXO)
				e.setAngulo(-Math.PI/2);//Só baixo
			else {
				System.out.println("ERRO NA ALTERAÇÃO DA DIREÇÃO DE MOVIMENTO DO PLAYER!");
				System.exit(1);
			}
		}
	}

}
