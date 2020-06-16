package com.firstJogo.elementosMundo;

import java.util.function.Consumer;

public class TipodeBloco {
	private char id;
	private String textura;
	
	private final Consumer<Object[]> funcaoColisiva;
	private final int tangibilidadePadrao;//Quanto mais tangível, mais ele causa colisão. 0 é o chão.
	//Entidades tem fantasmabilidade. Só podem se mover por blocos com tang. menor que sua fanstamabilidade.
	//10 é a fantasmabilidade padrão. Quem tentar se mover por algo mais tangível ou menos fantasmável, não
	//consegue. Portanto, um fantasma pode paralizar um humano só de ficar encima dele.
	
	private static char nextid=0;
	
	
	public static TipodeBloco[] azulejos=new TipodeBloco[256*256];//Todos os azulejos existentes!
	
	public static final TipodeBloco grama=new TipodeBloco("Grama");
	public static final TipodeBloco pedra=new TipodeBloco("Pedra", 20);//Padrede de pedra
	public TipodeBloco(String textura) {
		this(textura,0,null);
	}
	public TipodeBloco(String textura,int tangibilidade) {
		this(textura,tangibilidade,null);
	}
	public TipodeBloco(String textura,int tangibilidade,Consumer<Object[]> funcaoColisiva) {
		this.id=nextid;
		nextid+=1;
		this.textura=textura;
		this.tangibilidadePadrao=tangibilidade;
		if(azulejos[id]!=null)throw new IllegalStateException("Azulejo de id "+id+" já salvo!");
		azulejos[id]=this;
		this.funcaoColisiva=funcaoColisiva;
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

	public int getTangibilidade() {
		return tangibilidadePadrao;
	}

//	public void setTangibilidade(int tangibilidadePadrao) {
//		this.tangibilidadePadrao = tangibilidadePadrao;
//	}
	public Consumer<Object[]> getFuncaoColisiva() {
		return funcaoColisiva;
	}
}
