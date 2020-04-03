package com.firstJogo.visual;

public class TipodeAzulejo {
	private char id;
	private String textura;
	public static TipodeAzulejo[] azulejos=new TipodeAzulejo[256*256];//Todos os azulejos existentes!
	private static char nextid=0;
	
	public static final TipodeAzulejo grama=new TipodeAzulejo("Grama");
	
//	public TipodeAzulejo(char id,String textura) {
//		this.id=id;
//		this.textura=textura;
//		if(azulejos[id]!=null)throw new IllegalStateException("Azulejo de id "+id+" já salvo!");
//		azulejos[id]=this;
//	}
	public TipodeAzulejo(String textura) {
		this.id=nextid;
		nextid+=1;
		this.textura=textura;
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
}
