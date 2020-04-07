package com.firstJogo.Mundos;

import com.firstJogo.Mundos.Entidade;

import java.util.ArrayList;

public class Mundo {
	public static ArrayList<Mundo> mundos;
	
	private Bloco[][] blocos=new Bloco[16*3][16*3];
	private ArrayList<Entidade> entidades=new ArrayList<Entidade>();
	
	public Mundo() {
		mundos.add(this);
		
	}
	private void loadChunks(int[][] quais,long[] coordenadas) {
		if(quais.length!=coordenadas.length)
			throw new IllegalStateException("Quantidade de chunks a carregar não é igual a coordenadas de chunks a carregar!");
//		for(int[] ia:quais)
//			for(int i:ia)
//				blocos[][]
	}
	private Bloco[] loadChunkBlocks(long coordenada) {
		//Código para carregar da memória os chunks
		Bloco[] ans=new Bloco[256];
		for(short i=0;i<256;i++)
			ans[i]=new Bloco((char)0);
		return ans;
	}
}
