package com.firstJogo.Mundos;

public class Azulejo {
	private byte id;
	private String textura;
	public static Azulejo[] azulejos=new Azulejo[32*32];
	
	public static final Azulejo grama=new Azulejo((byte)0,"Grama");
	
	public Azulejo(byte id,String textura) {
		this.id=id;
		this.textura=textura;
		if(azulejos[id]!=null)throw new IllegalStateException("Azulejo de id "+id+" já salvo!");
		azulejos[id]=this;
	}

	public String getTextura() {
		return textura;
	}

	public byte getId() {
		return id;
	}
}
