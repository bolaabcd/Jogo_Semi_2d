package com.firstJogo.Mundos;

import com.firstJogo.padrao.PlayerRegras;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.TipodeCriatura;

public class Humano extends Entidade{
	private modos movModo;
	private int milisImpulso;
	private float sprintModifier;
	
	private TempoMarker impulso;//Pode alterar os milisImpulso, então pode alterar o TempoMarker!
	
	public enum modos{
		CORRENDO,
		ANDANDO,
		AGACHADO,
		SPRINT
	}
	public Humano(TipodeCriatura tipo) {
		super(tipo);
//		this.velocModifier=0.6f;
		this.veloc=5;
		milisImpulso=2000;
		movModo=modos.ANDANDO;
		sprintModifier=1.4f;//Seria 1.2 (quando tiver a animação!)
		impulso=new TempoMarker(milisImpulso*1000000,(pessoa)->{
			((Humano) pessoa).setMovModo(modos.SPRINT);
		},this);
	}
	
	public void modo_agachar() {
		setMovModo(Humano.modos.AGACHADO);
	}
	
	public void modo_correr() {
		setMovModo(Humano.modos.CORRENDO);
	}
	
	public void modo_andar() {
		setMovModo(Humano.modos.ANDANDO);
	}
	
//	public void resetMovModo() {
//		if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL))setMovModo(modos.CORRENDO);//Prioridade é correr, afinal ele tava sprintando.
//		else if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT)) setMovModo(modos.AGACHADO);
//		else setMovModo(Humano.modos.ANDANDO);
//	}
	
	public modos getMovModo() {
		return movModo;
	}
	@Override
	public float getVelocModifier() {
		return velocModifier*getModVelocModifier();
	}
	@Override
	public void pararMovimento() {
		super.pararMovimento();
		impulso.desativar();
		if(IsPlayer)PlayerRegras.resetMovModo(this);;
	}
	@Override
	public void setAngulo(double angulo) {
		if(this.getAngulo()!=angulo) {
			impulso.resetTemporegistrado();
			if(IsPlayer)PlayerRegras.resetMovModo(this);;
		}
		super.setAngulo(angulo);
	}
	
	private float getModVelocModifier() {
		switch(movModo) {
		case CORRENDO:
			return 1f;
		case AGACHADO:
			return 0.2f;
		case SPRINT:
			return sprintModifier;
		case ANDANDO:
			return 0.6f;
		default:
			System.out.println("Acontecimento impossível! Fechando programa pra evitar erros maiores..");
			System.exit(0);
			return 0f;
		
		}
	}
	private void setMovModo(modos modo) {
		if(movModo!=modo&&modo==modos.CORRENDO)impulso.ativar();
		movModo=modo;
//		if(modo.equals(modos.CORRENDO))this.velocModifier=1f;
//		else if(modo.equals(modos.ANDANDO))this.velocModifier=0.6f;
//		else if(modo.equals(modos.AGACHADO))this.velocModifier=0.2f;
//		else if(modo.equals(modos.SPRINT))this.velocModifier=1.2f;
	}

}
