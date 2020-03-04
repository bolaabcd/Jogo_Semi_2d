package com.firstJogo.Mundos;

import org.joml.Matrix4f;

import com.firstJogo.visual.Camera;
import com.firstJogo.visual.Janela;
import com.firstJogo.visual.Shaders;

public class World {
	private byte[][] azulejos;
	private int width;
	private int height;
	private Matrix4f mundo;
	
	public World() {
		height=32;
		width=32;
		azulejos=new byte[32][32];
		
		mundo=new Matrix4f().setTranslation(0,0,0);//BOTTON-RIGHT do mundo no centro da tela é o 0,0,0!
		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2+15, -Janela.getPrincipal().getHeight()/2+15, 0);
		//mundo.scale(16)//Faz um AZULEJO 32x32 (!!) -> O modelo tem a origem no centro, e ele escala pros dois lados!
		mundo.scale(Janela.getPrincipal().getHeight()/32)
		;
	}
	public void renderizar(AzRenderer rend,Shaders shad,Camera cam) {
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++){
				rend.Renderizar(azulejos[j][i], j, i, shad, mundo, cam);
			}
	}
}
