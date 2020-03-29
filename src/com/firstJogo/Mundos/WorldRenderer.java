package com.firstJogo.Mundos;

import java.util.ArrayList;

import org.joml.Matrix4f;

import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.Camera;
import com.firstJogo.visual.Janela;
import com.firstJogo.visual.Shaders;

public class WorldRenderer {
	private char[][] azulejos;//Azulejos efetivos no mundo!
	private ArrayList<Entidade> criaturas;
	private short width;
	private short height;
	private Matrix4f mundo;
	
	private short escala;
	
	public WorldRenderer() {
		height=18;//32*3 -> NÃO DÁ PRA RENDERIZAR ISSO TUDO! ESSE MUNDO DEVE SER O RENDERIZADO APENAS!
		width=18;
		escala=15;//15
		azulejos=new char[18][18];
		
		criaturas=new ArrayList<Entidade>();
		criaturas.add(Entidade.player);
		
		mundo=new Matrix4f().setTranslation(0,0,0);//BOTTON-RIGHT do mundo no centro da tela é o 0,0,0!
//		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2+15, -Janela.getPrincipal().getHeight()/2+15, 0);
		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2-escala, -Janela.getPrincipal().getHeight()/2-escala, 0);
		//mundo.scale(16)//Faz um AZULEJO 32x32 (!!) -> O modelo tem a origem no centro, e ele escala pros dois lados!
		mundo.scale(escala)
		;
		
//		mundo.scale(16)
		;
//		mundo.setOrtho2D(-width/2, width/2, -height/2, height/2);
	}
//	public World(short height, short width) {
//		azulejos=new int[width][height];
//		
//		mundo=new Matrix4f().setTranslation(0,0,0);//BOTTON-RIGHT do mundo no centro da tela é o 0,0,0!
//		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2+15, -Janela.getPrincipal().getHeight()/2+15, 0);
//		mundo.scale(Janela.getPrincipal().getHeight()/32)
//		;
//	}
	public void renderizar(CrRenderer crend,AzRenderer rend,Shaders shad,Camera cam) {
		for(char i=0;i<height;i++)
			for(char j=0;j<width;j++){
				if(GlobalVariables.debugue==true)System.out.println("RENDERIZANDO QUADRADO EM X= "+j+", Y= "+i);
				rend.Renderizar(azulejos[j][i], j, i, shad, mundo, cam);
				GlobalVariables.debugue=false;
			}
		crend.Renderizar(Entidade.player.getTipo_visual().getId(), 8.5f, 8.5f, (char)0, shad, mundo,cam);//Esse 8.5f é a posição no mundo que aparece.
	}
//	public void setblocos(char[][] blocos) {
//		
//		this.azulejos=blocos;
//	}
	public void setbloco(short x, short y, char tipo) {
		this.azulejos[x][y]=tipo;
	}
}
