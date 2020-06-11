package com.firstJogo.Mundos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Vector2f;

import com.firstJogo.estrutura.DirecoesPadrao;
import com.firstJogo.estrutura.Player;
import com.firstJogo.utils.GlobalVariables;

public class MundoCarregado {
	public static MundoCarregado atual;
	public static ArrayList<MundoCarregado> mundos=new ArrayList<MundoCarregado>();
	
	private final Set<Entidade> entidades=Collections.newSetFromMap(new ConcurrentHashMap<Entidade,Boolean>());
	private long[] chunkloader;//X e Y do chunk central!
	
	private char[][][][] blocos=new char[3][3][16][16];//3x3 chunks de 16x16
	
	public MundoCarregado() {
		mundos.add(this);
		long chunkLoaderX=0;
		long chunkLoaderY=0L;
		
		chunkloader=new long[] {chunkLoaderX, chunkLoaderY};
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				blocos[x][y]=this.loadChunkBlocos(new float[] {chunkLoaderX+x-1,chunkLoaderY+y-1});
		
	}
	
	public char[] getBlocos(long[][] coordenadas){//O y do array são as coordenadas!
		char[] ans=new char[coordenadas.length];
		int i=0;
		for(long[] coords:coordenadas) {
			ans[i]=getbloco(coords[0],coords[1]);
			i++;
		}
		return ans;
	}
	
	public void setAtual() {
		atual=this;
	}
	
	public void updateChunks(DirecoesPadrao lado) {
		Vector2f playerPos= Player.mainPlayer.ent.getMundopos();
		float chunkLoaderX=(playerPos.x/GlobalVariables.intperbloco)/16;
		float chunkLoaderY=(playerPos.y/GlobalVariables.intperbloco)/16;
		ArrayList<int[]> quais=new ArrayList<int[]>();
		ArrayList<float[]> coordenadas=new ArrayList<float[]>();
		if(lado==DirecoesPadrao.BAIXO) {
			quais.add(new int[] {-1,-1});
			quais.add(new int[] { 0,-1});
			quais.add(new int[] { 1,-1});
			coordenadas.add(new float[] {chunkLoaderX-1,chunkLoaderY-1});
			coordenadas.add(new float[] {chunkLoaderX  ,chunkLoaderY-1});
			coordenadas.add(new float[] {chunkLoaderX+1,chunkLoaderY-1});
		}
		else if(lado==DirecoesPadrao.CIMA){
			quais.add(new int[] {-1,1});
			quais.add(new int[] { 0,1});
			quais.add(new int[] { 1,1});
			coordenadas.add(new float[] {chunkLoaderX-1,chunkLoaderY+1});
			coordenadas.add(new float[] {chunkLoaderX  ,chunkLoaderY+1});
			coordenadas.add(new float[] {chunkLoaderX+1,chunkLoaderY+1});
		}
		else if(lado==DirecoesPadrao.ESQUERDA){
			quais.add(new int[] {-1,-1});
			quais.add(new int[] {-1, 0});
			quais.add(new int[] {-1, 1});
			coordenadas.add(new float[] {chunkLoaderX-1,chunkLoaderY-1});
			coordenadas.add(new float[] {chunkLoaderX-1,chunkLoaderY  });
			coordenadas.add(new float[] {chunkLoaderX-1,chunkLoaderY+1});
		}
		else if(lado==DirecoesPadrao.DIREITA){
			quais.add(new int[] {1,-1});
			quais.add(new int[] {1, 0});
			quais.add(new int[] {1, 1});
			coordenadas.add(new float[] {chunkLoaderX+1,chunkLoaderY-1});
			coordenadas.add(new float[] {chunkLoaderX+1,chunkLoaderY  });
			coordenadas.add(new float[] {chunkLoaderX+1,chunkLoaderY+1});
		}
		
		
		loadChunks(quais,coordenadas);
	}
	
	public char getbloco(long x,long y) {
//		int coordx=(int) (x-chunkloader[0]*16)+8;
//		int coordy=(int) (y-chunkloader[0]*16)+8;
//		if(x==0&&y==0)return 1;
		if(Math.abs(Math.floor((float)x/16+0.5f))%2==1&&Math.abs(Math.floor((float)y/16+0.5f))%2==1)return 1;
		return 0;
	}
	
	private void loadChunks(ArrayList<int[]> quais,ArrayList<float[]> coordenadas) {
		if(quais.size()!=coordenadas.size())
			throw new IllegalStateException("Quantidade de chunks a carregar não é igual a coordenadas de chunks a carregar!");
		for(int i=0;i<quais.size();i++) {
			int[] xy=quais.get(i);
			if(xy.length!=2)throw new IllegalStateException("Coordenadas inválidas!");
			blocos[xy[0]][xy[1]]=loadChunkBlocos(coordenadas.get(i));
		}
	}
	private char[][] loadChunkBlocos(float[] coordenadas) {
		if(coordenadas.length!=2)throw new IllegalStateException("Coordenadas inválidas!");
		//TODO: Código para carregar da memória os chunks
		char[][] ans=new char[16][16];
		for(short ia=0;ia<ans.length;ia++)
			for(short i=0;i<ans[ia].length;i++)
				ans[ia][i]=(char)0;
		return ans;
	}

	public Set<Entidade> getEntidades() {
		return entidades;
	}
}
