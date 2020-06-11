package com.firstJogo.visual;

import java.util.function.Consumer;

import org.joml.Vector2f;

import com.firstJogo.Mundos.Entidade;
import com.firstJogo.estrutura.ForcedVelocModifier;
import com.firstJogo.utils.GlobalVariables;

public class TipodeBloco {
	private char id;
	private String textura;
	
	private final Consumer<Object[]> funcaoColisiva;
	private short tangibilidadePadrao=0;//Quanto mais tangível, mais ele causa colisão. 0 é o chão.
	//Entidades tem fantasmabilidade. Só podem se mover por blocos com tang. menor que sua fanstamabilidade.
	//10 é a fantasmabilidade padrão. Quem tentar se mover por algo mais tangível ou menos fantasmável, não
	//consegue. Portanto, um fantasma pode paralizar um humano só de ficar encima dele.
	
	private static char nextid=0;
	
	//Entidade e posição central do bloco!
	private final Consumer<Object[]> colisaoPadrao=(entidadecentro)-> {
		Object[] ob=(Object[]) entidadecentro;
		Entidade ent=(Entidade) ob[0];
		Vector2f cent=(Vector2f)ob[1];
		Vector2f result=new Vector2f();
		Vector2f.add(cent.mul(-1f),ent.getMundopos(),result);//Ajustar velocidade aqui!
//		System.out.println(result);
		float newx=0f;
		float newy=0f;
		if(result.x>=GlobalVariables.intperbloco)
			newx = -(float) (ent.getMovDirecModifiers()[0] * ent.getVelocModified());
		else if (result.x <= -GlobalVariables.intperbloco)
			newx = -(float) (ent.getMovDirecModifiers()[0] * ent.getVelocModified());
		if (result.y >= GlobalVariables.intperbloco)
			newy = -(float) (ent.getMovDirecModifiers()[1] * ent.getVelocModified());
		else if (result.y <= -GlobalVariables.intperbloco)
			newy = -(float) (ent.getMovDirecModifiers()[1] * ent.getVelocModified());
		ent.addForcedVelocModifier(ForcedVelocModifier.COLISAO, new Vector2f(newx, newy));
	};
	
	public static TipodeBloco[] azulejos=new TipodeBloco[256*256];//Todos os azulejos existentes!
	
	public static final TipodeBloco grama=new TipodeBloco("Grama");
	public static final TipodeBloco pedra=new TipodeBloco("Pedra",(short) 20);//Padrede de pedra
	public TipodeBloco(String textura) {
		this.id=nextid;
		nextid+=1;
		this.textura=textura;
		if(azulejos[id]!=null)throw new IllegalStateException("Azulejo de id "+id+" já salvo!");
		azulejos[id]=this;
		this.funcaoColisiva=colisaoPadrao;
	}
	public TipodeBloco(String textura,short tangibilidade) {
		this.id=nextid;
		nextid+=1;
		this.textura=textura;
		this.tangibilidadePadrao=tangibilidade;
		if(azulejos[id]!=null)throw new IllegalStateException("Azulejo de id "+id+" já salvo!");
		azulejos[id]=this;
		this.funcaoColisiva=colisaoPadrao;
	}
	public TipodeBloco(String textura,short tangibilidade,Consumer<Object[]> funcaoColisiva) {
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

	public short getTangibilidade() {
		return tangibilidadePadrao;
	}

	public void setTangibilidade(short tangibilidadePadrao) {
		this.tangibilidadePadrao = tangibilidadePadrao;
	}
	public Consumer<Object[]> getFuncaoColisiva() {
		return funcaoColisiva;
	}
}
