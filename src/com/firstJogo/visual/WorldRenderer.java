package com.firstJogo.visual;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.Mundos.MundoCarregado;
import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.utils.GlobalVariables;

public class WorldRenderer {
	public static WorldRenderer main;
//	private char[][] azulejos;//Azulejos efetivos no mundo!
//	private ArrayList<Entidade> criaturas;
	private short width;
	private short height;
	private Matrix4f mundo;
	
	private short escala;
	
	public WorldRenderer() {
		main=this;
		
		height=18;//32*3 -> NÃO DÁ PRA RENDERIZAR ISSO TUDO! ESSE MUNDO DEVE SER O RENDERIZADO APENAS!
		width=18;
		escala=15;//15
//		azulejos=new char[18][18];
		
//		criaturas=new ArrayList<Entidade>();
		MundoCarregado.atual.getEntidades().add(Entidade.getPlayer());
		
		mundo=new Matrix4f().setTranslation(0,0,0);//BOTTON-RIGHT do mundo no centro da tela é o 0,0,0!
		mundo.setTranslation(-Janela.getPrincipal().getWidth()/2-2*escala, -Janela.getPrincipal().getHeight()/2-2*escala, 0);
		mundo.scale(escala);

	}
	
//	public ArrayList<Entidade> getCriaturas() {
//		return criaturas;
//	}

	public void renderizar(AzRenderer rend,Shaders shad,Camera cam) {
		cam=cam.getCopia();//Para não alterar nada enquanto renderiza!
		
		long xplayer=Entidade.getPlayer().getBlocoCoords()[0];
		long yplayer=Entidade.getPlayer().getBlocoCoords()[1];
		long[][] acarregar=new long[19*19][2];//X e Y pra cada elemento da lista
		short temp=0;
		for(short ypos=0;ypos<19;ypos++) 
			for(short xpos=0;xpos<19;xpos++) {
				acarregar[temp][0]=xplayer-9+xpos;
				acarregar[temp][1]=yplayer-9+ypos;
				temp++;
			}
		char[] blocos=MundoCarregado.atual.getBlocos(acarregar);
		for(short posx=0;posx<19;posx++) {
			for(short posy=0;posy<19;posy++)
			rend.Renderizar(blocos[posx*19+posy], acarregar[posx*19+posy][0]+9, acarregar[posx*19+posy][1]+9, shad, mundo, cam);
		}
		
		
		
				
//		for(char i=0;i<height;i++)
//			for(char j=0;j<width;j++){
//				rend.Renderizar((char)0, j, i, shad, mundo, cam);
//			}
		for(Entidade cr:MundoCarregado.atual.getEntidades())
			RenderizarEntidade(cr, shad, mundo,cam);//Esse 8.5f é a posição no mundo que aparece.
	}
//	public void setbloco(short x, short y, char tipo) {
//		this.azulejos[x][y]=tipo;
//	}
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
		float[] mundopos=ent.getMundopos();
		return new float[] {
				8.5f+(mundopos[0])/GlobalVariables.intperbloco,
				8.5f+(mundopos[1])/GlobalVariables.intperbloco
		};

	}
}
