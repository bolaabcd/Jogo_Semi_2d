package com.firstJogo.visual;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.firstJogo.elementosJogo.Camera;
import com.firstJogo.elementosJogo.Janela;
import com.firstJogo.elementosJogo.Player;
import com.firstJogo.elementosMundo.Entidade;
import com.firstJogo.elementosMundo.MundoCarregado;
import com.firstJogo.padroes.GlobalVariables;

public class WorldRenderer {
	public static WorldRenderer main;
//	private char[][] azulejos;//Azulejos efetivos no mundo!
//	private ArrayList<Entidade> criaturas;
//	private short width;
//	private short height;
	private Matrix4f mundo;
	
	private short escala;
	
	public WorldRenderer() {
		main=this;
		
//		height=18;//32*3 -> NÃO DÁ PRA RENDERIZAR ISSO TUDO! ESSE MUNDO DEVE SER O RENDERIZADO APENAS!
//		width=18;
		escala=15;//15
//		azulejos=new char[18][18];
		
//		criaturas=new ArrayList<Entidade>();
		MundoCarregado.atual.getEntidades().add(Player.mainPlayer.ent);
		
		mundo=new Matrix4f().setTranslation(0,0,0);//BOTTON-RIGHT do mundo no centro da tela é o 0,0,0!
		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2-2*escala, -Janela.getPrincipal().getHeight()/2-2*escala, 0);
		mundo.scale(escala);

	}

	public void renderizar(AzRenderer rend,Shaders shad,Camera cam) {
		cam=cam.getCopia();//Para não alterar nada enquanto renderiza!

		long xplayer=MundoCarregado.getBlocoCoords(Player.mainPlayer.ent.getMundopos()).x;
		long yplayer=MundoCarregado.getBlocoCoords(Player.mainPlayer.ent.getMundopos()).y;
		long[][] acarregar=new long[18*18][2];//X e Y pra cada elemento da lista
		int temp=0;
		for(int ypos=0;ypos<18;ypos++) 
			for(int xpos=0;xpos<18;xpos++) {
				acarregar[temp][0]=xplayer-9+xpos;
				acarregar[temp][1]=yplayer-9+ypos;
				temp++;
			}
		char[] blocos=MundoCarregado.atual.getBlocos(acarregar);
		for(int posx=0;posx<18;posx++) {
			for(int posy=0;posy<18;posy++)
			rend.Renderizar(blocos[posx*18+posy], acarregar[posx*18+posy][0]+9, acarregar[posx*18+posy][1]+9, shad, mundo, cam);
		}
		
		

		for(Entidade cr:MundoCarregado.atual.getEntidades())
			RenderizarEntidade(cr, shad, mundo,cam);//Esse 8.5f é a posição no mundo que aparece.
	}

	private void RenderizarEntidade(Entidade ent, Shaders shad, Matrix4f mundo, Camera cam) {
		shad.bindar();
		ent.getTextura().bind(1);//Bindou ao sampler número 1 ¯\_(ツ)_/¯
		Matrix4f mat=new Matrix4f();
		if(ent.equals(Player.mainPlayer.ent)) {
			Matrix4f pos=new Matrix4f().translate(2*8.5f,2*8.5f,0);//O modelo tem a origem no centro, e ele escala pros dois lados!
			cam.getRawProjec().mul(mundo, mat);
			mat.mul(pos);
		}else {
			float[] rendpos=getRendPos(ent);
//			if(!GlobalVariables.debugue&&(rendpos[0]>19||rendpos[0]<-1||rendpos[1]>19||rendpos[1]<-1))return;
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
		Vector2f mundopos=ent.getMundopos();
		return new float[] {
				8.5f+(mundopos.x)/GlobalVariables.intperbloco,
				8.5f+(mundopos.y)/GlobalVariables.intperbloco
		};

	}
}
