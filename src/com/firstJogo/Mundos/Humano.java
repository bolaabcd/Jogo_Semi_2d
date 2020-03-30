package com.firstJogo.Mundos;

import com.firstJogo.visual.TipodeCriatura;

public class Humano extends Entidade{
	private modos movModo;
	public enum modos{
		CORRENDO,
		ANDANDO,
		AGACHADO,
		SPRINT
	}
	public Humano(TipodeCriatura tipo) {
		super(tipo);
//		this.velocModifier=0.6f;
		movModo=modos.ANDANDO;
		this.veloc=5;
	}
	public void setMovModo(modos modo) {
		movModo=modo;
//		if(modo.equals(modos.CORRENDO))this.velocModifier=1f;
//		else if(modo.equals(modos.ANDANDO))this.velocModifier=0.6f;
//		else if(modo.equals(modos.AGACHADO))this.velocModifier=0.2f;
//		else if(modo.equals(modos.SPRINT))this.velocModifier=1.2f;
	}
	public modos getMovModo() {
		return movModo;
	}
	@Override
	public float getVelocModifier() {
		return velocModifier*getModVelocModifier();
	}
	private float getModVelocModifier() {
		switch(movModo) {
		case CORRENDO:
			return 1f;
		case AGACHADO:
			return 0.2f;
		case SPRINT:
			return 1.2f;
		case ANDANDO:
			return 0.6f;
		default:
			System.out.println("Acontecimento impossível! Fechando programa pra evitar erros maiores..");
			System.exit(0);
			return 0f;
		
		}
	}

}