package com.firstJogo.visual;

import java.util.ArrayList;

import org.joml.Matrix4f;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.utils.GlobalVariables;

public class WorldRenderer {
	public static WorldRenderer main;
	private char[][] azulejos;//Azulejos efetivos no mundo!
	private ArrayList<Entidade> criaturas;
	private short width;
	private short height;
	private Matrix4f mundo;
	
	private short escala;
	
	public WorldRenderer() {
		main=this;
		
		height=18;//32*3 -> NÃO DÁ PRA RENDERIZAR ISSO TUDO! ESSE MUNDO DEVE SER O RENDERIZADO APENAS!
		width=18;
		escala=15;//15
		azulejos=new char[18][18];
		
		criaturas=new ArrayList<Entidade>();
		criaturas.add(Entidade.getPlayer());
		
		mundo=new Matrix4f().setTranslation(0,0,0);//BOTTON-RIGHT do mundo no centro da tela é o 0,0,0!
//		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2+15, -Janela.getPrincipal().getHeight()/2+15, 0);
//		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2-escala, -Janela.getPrincipal().getHeight()/2-escala, 0);
		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2-2*escala, -Janela.getPrincipal().getHeight()/2-2*escala, 0);

		//mundo.scale(16)//Faz um AZULEJO 32x32 (!!) -> O modelo tem a origem no centro, e ele escala pros dois lados!
		mundo.scale(escala)
		;
		
//		mundo.scale(16)
		;
//		mundo.setOrtho2D(-width/2, width/2, -height/2, height/2);
	}
	
	public ArrayList<Entidade> getCriaturas() {
		return criaturas;
	}
//	public World(short height, short width) {
//		azulejos=new int[width][height];
//		
//		mundo=new Matrix4f().setTranslation(0,0,0);//BOTTON-RIGHT do mundo no centro da tela é o 0,0,0!
//		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2+15, -Janela.getPrincipal().getHeight()/2+15, 0);
//		mundo.scale(Janela.getPrincipal().getHeight()/32)
//		;
//	}
	public void renderizar(AzRenderer rend,Shaders shad,Camera cam) {
		for(char i=0;i<height;i++)
			for(char j=0;j<width;j++){
//				if(GlobalVariables.debugue==true)System.out.println("RENDERIZANDO QUADRADO EM X= "+j+", Y= "+i);
				rend.Renderizar(azulejos[j][i], j, i, shad, mundo, cam);
//				GlobalVariables.debugue=false;
			}
		for(Entidade cr:criaturas)
			RenderizarEntidade(cr, shad, mundo,cam);//Esse 8.5f é a posição no mundo que aparece.

//		crend.Renderizar(cr.getTextura(), 8.5f, 8.5f, (char)0, shad, mundo,cam);//Esse 8.5f é a posição no mundo que aparece.
	}
//	public void setblocos(char[][] blocos) {
//		
//		this.azulejos=blocos;
//	}
	public void setbloco(short x, short y, char tipo) {
		this.azulejos[x][y]=tipo;
	}
	private void RenderizarEntidade(Entidade ent, Shaders shad, Matrix4f mundo, Camera cam) {
		shad.bindar();
		ent.getTextura().bind(1);//Bindou ao sampler número 1 ¯\_(ツ)_/¯
		Matrix4f mat=new Matrix4f();
		if(ent.isPlayer()) {
			Matrix4f pos=new Matrix4f().translate(2*8.5f,2*8.5f,0);//O modelo tem a origem no centro, e ele escala pros dois lados!
			cam.getRawProjec().mul(mundo, mat);
			mat.mul(pos);
		}else {
			float[] rendpos=getRendPos(ent);
			if(rendpos[0]>19||rendpos[0]<-1||rendpos[1]>19||rendpos[1]<-1)return;
			//Nem renderiza se tiver fora do mapa!
			Matrix4f pos=new Matrix4f().translate(2*rendpos[0],2*rendpos[1],0);//O modelo tem a origem no centro, e ele escala pros dois lados!
			cam.getProjec().mul(mundo, mat);
			mat.mul(pos);
		}
					
		shad.setUniforme("localizacao_da_textura_tambem_chamada_de_sampler", 1);//Setamos o sampler para 0, onde está a nossa textura!
		shad.setUniforme("projecao", mat);
			
		ent.getModelo().renderizar();

		
		
	}
	
	private float[] getRendPos(Entidade ent) {
		long[] mundopos=ent.getMundopos();
//		long[] mundopos=new long[] {0,0};
		return new float[] {
				8.5f+(float)(mundopos[0])/GlobalVariables.intperbloco,
				8.5f+(float)(mundopos[1])/GlobalVariables.intperbloco
		};

	}
}
