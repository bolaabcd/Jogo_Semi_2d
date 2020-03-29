package com.firstJogo.visual;

public class TipodeCriatura {
	private char id;
	private String textura;
	public static TipodeCriatura[] criaturas=new TipodeCriatura[256*256];
	private static char nextid=0;
	public static final TipodeCriatura Humano=new TipodeCriatura("Humano");
//public TipodeCriatura(char id,String textura) {
//	this.id=id;
//	this.textura=textura;
//	if(criaturas[id]!=null)throw new IllegalStateException("Criatura de id "+id+" já salvo!");
//	criaturas[id]=this;
//}
	public TipodeCriatura(String textura) {
		this.id=nextid;
		nextid+=1;
		this.textura=textura;
		if(criaturas[id]!=null)throw new IllegalStateException("Criatura de id "+id+" já salvo!");
		criaturas[id]=this;
	}
public String getTextura() {
	return textura;
}

public char getId() {
	return id;
}
}
