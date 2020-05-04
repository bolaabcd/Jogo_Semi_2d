package com.firstJogo.Mundos;

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
//	public void setBloco(Bloco b,short x, short y) {
//		blocos[x][y]=b;
//	}
	
}
