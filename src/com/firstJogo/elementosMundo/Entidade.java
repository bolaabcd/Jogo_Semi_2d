package com.firstJogo.elementosMundo;

import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.firstJogo.elementosJogo.CollisionDetector;
import com.firstJogo.elementosJogo.TempoEvento;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.padroes.DirecoesPadrao;
import com.firstJogo.padroes.EventosPadrao;
import com.firstJogo.padroes.GlobalVariables;
import com.firstJogo.padroes.VelocModifiersPadrao;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Textura;

public abstract class Entidade {
	
	private final Vector2f[] hitboxPos;//Começa no baixo esquerda, sentido horário. 00 é a superior esquerda!
	private final Textura visual;
	private final Modelo modelo;
	private final HashMap<String,Float> velocModifiers=new HashMap<String,Float>();
	private final HashMap<String,Vector2f> forcedVelocModifiers=new HashMap<String,Vector2f>();

	private Double anguloMovimento=null;
	private double olharDir=Math.PI/2;
	private Vector2f mundoPos;//A coordenada 0,0 é a quina inferior direita do bloco 0,0 (centro do chunk 0,0).
	private MundoCarregado mundo;
	
	
	private int fantasmabilidade=getFantasmabilidadePadrao();
	
	protected abstract int getVelocPadrao();
	protected abstract int getFantasmabilidadePadrao();
	
	protected Entidade(Textura visu,Modelo model) {
		this.visual=visu;
		this.modelo=model;
		hitboxPos=modelo.getVertices();
		velocModifiers.put(VelocModifiersPadrao.ParadoModifier, 0f);
	}
	
	public void spawnar(Vector2f mundoPos,MundoCarregado mundo,boolean isPlayer) {
		this.mundoPos=mundoPos;
		this.mundo=mundo;
		if (!isPlayer) {
			TempoEvento<?> seguirPlayer=EventosPadrao.seguirPlayerEvento(1, this);
			seguirPlayer.resetar();
			GeradorEventos.addTempoEvento(EventosPadrao.seguirPlayerEventoChave(this),
					seguirPlayer);
			TempoEvento<?> removerEntidade=EventosPadrao.removerEntidadeEvento(30, this);
			removerEntidade.resetar();
			GeradorEventos.addTempoEvento(EventosPadrao.removerEntidadeEventoChave(this),
					removerEntidade);
		}
	}

	public void kill() {
		mundo.getEntidades().remove(this);
		GeradorEventos.remEvento(EventosPadrao.seguirPlayerEventoChave(this));
		GeradorEventos.remEvento(EventosPadrao.removerEntidadeEventoChave(this));
	}
	
	public boolean isParado() {
		return velocModifiers.get(VelocModifiersPadrao.ParadoModifier)==0;
	}
	
	
	
	
	private boolean checkBlocoColisao(Vector2f newMundopos) {
		boolean res=false;
		for(Vector2i bloco:CollisionDetector.getColidBlocos(newMundopos, mundo, hitboxPos)) {
			TipodeBloco tipo=TipodeBloco.azulejos[MundoCarregado.atual.getbloco(bloco.x, bloco.y)];
			if(tipo.getTangibilidade()>this.fantasmabilidade) 
				res=true;
			if(tipo.getFuncaoColisiva()!=null)
				tipo.getFuncaoColisiva().accept(new Object[] {this,Bloco.toCentroBloco(bloco.x, bloco.y)});
		}
		return res;
	}
	private boolean checkEntidadeColisao(Vector2f newMundopos) {
		
		return false;
	}
	private boolean checkColisao(Vector2f newMundopos) {
		return checkEntidadeColisao(newMundopos)||checkBlocoColisao(newMundopos);
	}
	
	public boolean addForcedVelocModifier(String chave, Vector2f valor) {
		if(forcedVelocModifiers.put(chave, valor)==null)return false;
		return true;
	}
	
	public boolean remForcedVelocModifier(String chave) {
		if(forcedVelocModifiers.remove(chave)==null)return false;
		return true;
	}
	
	
	
	
	public boolean pararMovimento() {//True se foi, False se não.
		if (isParado())
			return false;
		velocModifiers.put(VelocModifiersPadrao.ParadoModifier, 0f);
		return true;
	}
	
	public boolean iniciarMovimento() {
		if (!isParado())
			return false;
		velocModifiers.put(VelocModifiersPadrao.ParadoModifier, 1f);
		return true;
	}
	
	public DirecoesPadrao getOlhar() {
		if(olharDir>-Math.PI/4&&olharDir<Math.PI/4)return DirecoesPadrao.DIREITA;
		else if(olharDir>=Math.PI/4&&olharDir<=3*Math.PI/4)return DirecoesPadrao.CIMA;
		else if(olharDir>3*Math.PI/4||olharDir<-3*Math.PI/4)return DirecoesPadrao.ESQUERDA;
		else if(olharDir>=-3*Math.PI/4&&olharDir<=-Math.PI/4)return DirecoesPadrao.BAIXO;
		throw new IllegalStateException("Erro inesperado!");
	}
	
	public void setOlhar(double olhar) {
		olharDir=olhar;
	}
	
	public long[] getChunkCoords() {
		return new long[] {
				(long)Math.floor((this.getMundopos().x/GlobalVariables.intperbloco)/16+0.5f),
				(long)Math.floor((this.getMundopos().y/GlobalVariables.intperbloco)/16+0.5f)
		};
	}

	public boolean setAnguloMovimento(double angulo) {
		if(this.anguloMovimento!=null)
			if(Math.abs(this.anguloMovimento-Math.round(angulo*180/Math.PI)*Math.PI/180)*180/Math.PI<=1)return false;
		this.anguloMovimento=Math.round(angulo*180/Math.PI)*Math.PI/180;
		
		setOlhar(anguloMovimento);
		
		return true;
	}
	
	public Textura getTextura() {
		return visual;
	}

	public float getVelocModified() {
		if (isParado())
			return 0;
		float velocModFinal = 1f;
		for (String modifchave : velocModifiers.keySet())
			velocModFinal *= velocModifiers.get(modifchave);
		return velocModFinal * getVelocPadrao();
	}
	
	public boolean addVelocModifier(String chave,float valor) {
		if(velocModifiers.put(chave,valor)==null)
			return false;
		return true;
	}
	
	public boolean remVelocModifier(String chave) {
		if(velocModifiers.remove(chave)==null)
			return false;
		return true;
	}

	public Double[] getMovDirecModifiers() {
		if(this.isParado())return null;
		return new Double[] {(double)Math.round(Math.cos(anguloMovimento)*100000d)/100000d,(double)Math.round(Math.sin(anguloMovimento)*100000d)/100000d};
	}

	public Modelo getModelo() {
		return modelo;
	}
	
	
	public Vector2f getMundopos() {
		return mundoPos;
	}

	public boolean setMundoPos(Vector2f mundopos) {
		if (mundopos.x == this.mundoPos.x && mundopos.y == this.mundoPos.y)
			return false;
		if (checkColisao(mundopos)) {
			return false;
//			forcedVelocModifiers.remove(VelocModifiersPadrao.ColisaoForcedModifier);
		} 
		this.mundoPos = mundopos;
		return true;
	}
	public void forcedSetMundoPos(Vector2f mundopos) {
		this.mundoPos = mundopos;;
	}
	
	public Vector2f getForcedVelocModified() {
		Vector2f forcedVelocModFinal=new Vector2f(0,0);
		for(String chave:forcedVelocModifiers.keySet())
			forcedVelocModFinal.add(forcedVelocModifiers.get(chave));
		return forcedVelocModFinal;
	}

}
