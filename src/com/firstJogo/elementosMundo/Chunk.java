package com.firstJogo.elementosMundo;

public class Chunk {
	private final Bloco[][] blocos=new Bloco[16][16];

	public Chunk(Bloco[][] blocos) {
		for(short i=0;i<16;i++)
			for(short j=0;j<16;j++)
				this.blocos[i][j]=blocos[i][j];
	}

	public Bloco[][] getBlocos() {
		return blocos;
	}
	
}
