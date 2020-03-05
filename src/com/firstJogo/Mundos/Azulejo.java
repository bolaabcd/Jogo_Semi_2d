package com.firstJogo.Mundos;

public class Azulejo {
	private char id;
	private String textura;
	public static Azulejo[] azulejos=new Azulejo[256*256];//Todos os azulejos existentes!
	
	public static final Azulejo grama=new Azulejo((char)0,"Default");
	
	public Azulejo(char id,String textura) {
		this.id=id;
		this.textura=textura;
		if(azulejos[id]!=null)throw new IllegalStateException("Azulejo de id "+id+" já salvo!");
		azulejos[id]=this;
	}

	public String getTextura() {
		return textura;
	}

	public char getId() {
		return id;
	}
}
