package com.firstJogo.visual;

public class TipodeBloco {
	private char id;
	private String textura;
	
	private short tangibilidadePadrao=0;//Quanto mais tangível, mais ele causa colisão. 0 é o chão.
	//Entidades tem fantasmabilidade. Só podem se mover por blocos com tang. menor que sua fanstamabilidade.
	//10 é a fantasmabilidade padrão. Quem tentar se mover por algo mais tangível ou menos fantasmável, não
	//consegue. Portanto, um fantasma pode paralizar um humano só de ficar encima dele.
	
	public static TipodeBloco[] azulejos=new TipodeBloco[256*256];//Todos os azulejos existentes!
	private static char nextid=0;
	
	public static final TipodeBloco grama=new TipodeBloco("Grama");
	public static final TipodeBloco ppedra=new TipodeBloco("PPedra",(short) 20);//Padrede de pedra
	
	public TipodeBloco(String textura) {
		this.id=nextid;
		nextid+=1;
		this.textura=textura;
		if(azulejos[id]!=null)throw new IllegalStateException("Azulejo de id "+id+" já salvo!");
		azulejos[id]=this;
	}
	public TipodeBloco(String textura,short tangibilidade) {
		this.id=nextid;
		nextid+=1;
		this.textura=textura;
		this.tangibilidadePadrao=tangibilidade;
		if(azulejos[id]!=null)throw new IllegalStateException("Azulejo de id "+id+" já salvo!");
		azulejos[id]=this;
	}

	public String getTextura() {
		return textura;
	}
	public void setTextura(String textura) {
		this.textura=textura;
	}

	public char getId() {
		return id;
	}

	public short getTangibilidade() {
		return tangibilidadePadrao;
	}

	public void setTangibilidade(short tangibilidadePadrao) {
		this.tangibilidadePadrao = tangibilidadePadrao;
	}
}
