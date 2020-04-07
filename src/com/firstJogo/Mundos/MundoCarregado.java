package com.firstJogo.Mundos;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.regras.DirecoesPadrao;
import com.firstJogo.utils.GlobalVariables;

import java.util.ArrayList;

public class MundoCarregado {
	public static MundoCarregado atual;
	public static ArrayList<MundoCarregado> mundos;
	
	private ArrayList<Entidade> entidades=new ArrayList<Entidade>();
	
	private Bloco[][][][] blocos=new Bloco[3][3][16][16];//3x3 chunks de 16x16
	
	public MundoCarregado() {
		mundos.add(this);
		long[] playerPos= Entidade.getPlayer().getMundopos();
		long chunkLoaderX=Math.round((playerPos[0]/GlobalVariables.intperbloco)/16);
		long chunkLoaderY=Math.round((playerPos[1]/GlobalVariables.intperbloco)/16);
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				blocos[x][y]=this.loadChunkBlocos(new long[] {chunkLoaderX+x-1,chunkLoaderY+y-1});
		
	}
	
	public void setAtual() {
		atual=this;
	}
	
	public void updateChunks(DirecoesPadrao lado) {
		long[] playerPos= Entidade.getPlayer().getMundopos();
		long chunkLoaderX=Math.round((playerPos[0]/GlobalVariables.intperbloco)/16);
		long chunkLoaderY=Math.round((playerPos[1]/GlobalVariables.intperbloco)/16);
		ArrayList<int[]> quais=new ArrayList<int[]>();
		ArrayList<long[]> coordenadas=new ArrayList<long[]>();
		if(lado==DirecoesPadrao.BAIXO) {
			quais.add(new int[] {-1,-1});
			quais.add(new int[] { 0,-1});
			quais.add(new int[] { 1,-1});
			coordenadas.add(new long[] {chunkLoaderX-1,chunkLoaderY-1});
			coordenadas.add(new long[] {chunkLoaderX  ,chunkLoaderY-1});
			coordenadas.add(new long[] {chunkLoaderX+1,chunkLoaderY-1});
		}
		else if(lado==DirecoesPadrao.CIMA){
			quais.add(new int[] {-1,1});
			quais.add(new int[] { 0,1});
			quais.add(new int[] { 1,1});
			coordenadas.add(new long[] {chunkLoaderX-1,chunkLoaderY+1});
			coordenadas.add(new long[] {chunkLoaderX  ,chunkLoaderY+1});
			coordenadas.add(new long[] {chunkLoaderX+1,chunkLoaderY+1});
		}
		else if(lado==DirecoesPadrao.ESQUERDA){
			quais.add(new int[] {-1,-1});
			quais.add(new int[] {-1, 0});
			quais.add(new int[] {-1, 1});
			coordenadas.add(new long[] {chunkLoaderX-1,chunkLoaderY-1});
			coordenadas.add(new long[] {chunkLoaderX-1,chunkLoaderY  });
			coordenadas.add(new long[] {chunkLoaderX-1,chunkLoaderY+1});
		}
		else if(lado==DirecoesPadrao.DIREITA){
			quais.add(new int[] {1,-1});
			quais.add(new int[] {1, 0});
			quais.add(new int[] {1, 1});
			coordenadas.add(new long[] {chunkLoaderX+1,chunkLoaderY-1});
			coordenadas.add(new long[] {chunkLoaderX+1,chunkLoaderY  });
			coordenadas.add(new long[] {chunkLoaderX+1,chunkLoaderY+1});
		}
		
		
		loadChunks(quais,coordenadas);
	}
	
	private void loadChunks(ArrayList<int[]> quais,ArrayList<long[]> coordenadas) {
		if(quais.size()!=coordenadas.size())
			throw new IllegalStateException("Quantidade de chunks a carregar não é igual a coordenadas de chunks a carregar!");
		for(int i=0;i<quais.size();i++) {
			int[] xy=quais.get(i);
			if(xy.length!=2)throw new IllegalStateException("Coordenadas inválidas!");
			blocos[xy[0]][xy[1]]=loadChunkBlocos(coordenadas.get(i));
		}
	}
	private Bloco[][] loadChunkBlocos(long[] coordenadas) {
		if(coordenadas.length!=2)throw new IllegalStateException("Coordenadas inválidas!");
		//Código para carregar da memória os chunks
		Bloco[][] ans=new Bloco[16][16];
		for(short ia=0;ia<ans.length;ia++)
			for(short i=0;i<ans[ia].length;i++)
				ans[ia][i]=new Bloco((char)0);
		return ans;
	}
}
