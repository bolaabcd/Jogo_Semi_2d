package com.firstJogo.Mundos;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2f;

import com.firstJogo.estrutura.DirecoesPadrao;
import com.firstJogo.estrutura.ElementoDuplo;
import com.firstJogo.estrutura.EventoPadrao;
import com.firstJogo.estrutura.ForcedVelocModifier;
import com.firstJogo.estrutura.TempoEvento;
import com.firstJogo.estrutura.VelocModifier;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Textura;
import com.firstJogo.visual.TipodeBloco;

//TODO: aprimorar sistema de quem é o player, e remover TODAS as dependências do player possíveis.
//TODO: alterar ENUMs pra tornar possível adicionar novas chaves.
//TODO menor: aprimorar conversão pra ChunkCoords e BlocoCoords...
//TODO FUTURO: tornar independente a direção do olhar e a direção de movimento, com penalidade!
//TODO BEM FUTURO: colocar sistema de iluminação com o olhar no futuro!

public abstract class Entidade {
	public enum EventosEntidades{
		MOVER,
		REMOVER,
		TESTE
	}
	
	private final float[][] hitboxPos;//Começa no baixo esquerda, sentido horário. 00 é a superior esquerda!
	private final Textura visual;
	private final Modelo modelo;

	private Double anguloMovimento=null;
	private double olharDir=Math.PI/2;
	private Vector2f mundoPos;//A coordenada 0,0 é a quina inferior direita do bloco 0,0 (centro do chunk 0,0).
	private MundoCarregado mundo;
	private boolean isPlayer;
	
	private HashMap<VelocModifier,Float> velocModifiers=new HashMap<VelocModifier,Float>();
	private HashMap<ForcedVelocModifier,Vector2f> forcedVelocModifiers=new HashMap<ForcedVelocModifier,Vector2f>();
	
	private int fantasmabilidade=getFantasmabilidadePadrao();
	
	protected abstract int getVelocPadrao();
	protected abstract int getFantasmabilidadePadrao();
	
	protected Entidade(Textura visu,Modelo model) {
		this.visual=visu;
		this.modelo=model;
		hitboxPos=modelo.getVertices();
		velocModifiers.put(VelocModifier.PARADO, 0f);
	}
	
	public void spawnar(Vector2f mundoPos,MundoCarregado mundo,boolean isPlayer) {
		this.mundoPos=mundoPos;
		this.mundo=mundo;
		this.isPlayer=isPlayer;
		if (!isPlayer) {
			TempoEvento<?> eventoEntidade=EventoPadrao.moverEntidadeEvento(1, this);
			eventoEntidade.resetar();
			GeradorEventos.addTempoEvento(new ElementoDuplo<Entidade, EventosEntidades>(this, EventosEntidades.MOVER,false),
					eventoEntidade);
			eventoEntidade=EventoPadrao.removerEntidadeEvento(30, this);
			eventoEntidade.resetar();
			GeradorEventos.addTempoEvento(new ElementoDuplo<Entidade, EventosEntidades>(this, EventosEntidades.REMOVER,false),
					eventoEntidade);
		}
	}

	public void kill() {
		mundo.getEntidades().remove(this);
		GeradorEventos.remEvento(new ElementoDuplo<Entidade, EventosEntidades>(this, EventosEntidades.MOVER, false));
		GeradorEventos.remEvento(new ElementoDuplo<Entidade, EventosEntidades>(this, EventosEntidades.REMOVER, false));
	}
	
	public boolean isParado() {
		return velocModifiers.get(VelocModifier.PARADO)==0;
	}
	
	
	
	
	private boolean checkColisao(Vector2f mundopos) {
		boolean res=false;
		for(long[] bloco:getColidCoords(mundopos)) {
			TipodeBloco tipo=TipodeBloco.azulejos[MundoCarregado.atual.getbloco(bloco[0], bloco[1])];
			if(tipo.getTangibilidade()>this.fantasmabilidade) {
				tipo.getFuncaoColisiva().accept(new Object[] {this,Bloco.toCentroBloco(bloco[0], bloco[1])});
				res=true;
			}
		}
		return res;
	}
	private ArrayList<long[]> getColidCoords(Vector2f mundopos) {//Obter os blocos que ele pode estar colidindo
		float deslocx=GlobalVariables.intperbloco*(hitboxPos[2][0]-hitboxPos[0][0])/2;
		float deslocy=GlobalVariables.intperbloco*(hitboxPos[2][1]-hitboxPos[0][1])/2;
		float xi=(mundopos.x-deslocx)/GlobalVariables.intperbloco;
		float yi=(mundopos.y-deslocy)/GlobalVariables.intperbloco;
		float xf=(mundopos.x+deslocx)/GlobalVariables.intperbloco;
		float yf=(mundopos.y+deslocy)/GlobalVariables.intperbloco;
		int xmax=(int) Math.floor(Math.abs(2*deslocx/GlobalVariables.intperbloco)+1);//Arredondando pra cima quantos blocos a entidade ocupa.
		int ymax=(int) Math.floor(Math.abs(2*deslocy/GlobalVariables.intperbloco)+1);
		ArrayList<long[]> coords=new ArrayList<long[]>();
		for(int ix=-1-xmax/2;ix<xmax/2+1;ix++)
			for(int iy=-1-ymax/2;iy<ymax/2+1;iy++)
				if(
						colide(xi, yi, xf, yf, getBlocoCoords()[0]+ix, getBlocoCoords()[1]+iy,getBlocoCoords()[0]+ix+1, getBlocoCoords()[1]+iy+1)
						)
					coords.add(new long[] {
							getBlocoCoords()[0]+ix,
							getBlocoCoords()[1]+iy
				});
		return coords;
	}
	private boolean colide(float xi,float yi,float xf,float yf,float xi2,float yi2,float xf2,float yf2) {
		if(
				dentrode(xi,yi,xf,yf,xi2,yi2)||dentrode(xi,yi,xf,yf,xf2,yf2)
				||
				dentrode(xi,yi,xf,yf,xi2,yf2)||dentrode(xi,yi,xf,yf,xf2,yi2)
				||
				dentrode(xi2,yi2,xf2,yf2,xi,yi)||dentrode(xi2,yi2,xf2,yf2,xf,yf)
				||
				dentrode(xi2,yi2,xf2,yf2,xf,yi)||dentrode(xi2,yi2,xf2,yf2,xi,yf)
				)return true;
		return false;
	}
	private boolean dentrode(float xi,float yi,float xf,float yf,float x, float y) {
		if(x>xi&&x<xf&&y>yi&&y<yf)return true;
		return false;
	}
	public boolean addForcedVelocModifier(ForcedVelocModifier chave, Vector2f valor) {
		if(forcedVelocModifiers.put(chave, valor)==null)return false;
		return true;
	}
	public boolean remForcedVelocModifier(ForcedVelocModifier chave) {
		if(forcedVelocModifiers.remove(chave)==null)return false;
		return true;
	}
	
	
	
	
	public boolean pararMovimento() {//True se foi, False se não.
		if (isParado())
			return false;
		velocModifiers.put(VelocModifier.PARADO, 0f);
		return true;
	}
	
	public boolean iniciarMovimento() {
		if (!isParado())
			return false;
		velocModifiers.put(VelocModifier.PARADO, 1f);
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
	
	public long[] getBlocoCoords() {
		return new long[] {
				(long)Math.floor(this.getMundopos().x/GlobalVariables.intperbloco),
				(long)Math.floor(this.getMundopos().y/GlobalVariables.intperbloco),
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
		for (VelocModifier modifchave : velocModifiers.keySet())
			velocModFinal *= velocModifiers.get(modifchave);
		return velocModFinal * getVelocPadrao();
	}
	
	public boolean addVelocModifier(VelocModifier chave,float valor) {
		if(velocModifiers.put(chave,valor)==null)
			return false;
		return true;
	}
	
	public boolean remVelocModifier(VelocModifier chave) {
		if(velocModifiers.remove(chave)==null)
			return false;
		return true;
	}

	public Double[] getMovDirecModifiers() {
		if(this.isParado())return new Double[] {null,null};
		return new Double[] {(double)Math.round(Math.cos(anguloMovimento)*100000d)/100000d,(double)Math.round(Math.sin(anguloMovimento)*100000d)/100000d};
	}

	public Modelo getModelo() {
		return modelo;
	}
	
	

	
	public boolean isPlayer() {
		return isPlayer;
	}
	
	public Vector2f getMundopos() {
		return mundoPos;
	}

	public boolean setMundopos(Vector2f mundopos) {
		if (mundopos.x == this.mundoPos.x && mundopos.y == this.mundoPos.y)
			return false;
		if (!checkColisao(mundopos)) {
			forcedVelocModifiers.remove(ForcedVelocModifier.COLISAO);
		} else {
			return false;
		}
		this.mundoPos = mundopos;
		return true;
	}
	
	public Vector2f getForcedVelocModified() {
		Vector2f forcedVelocModFinal=new Vector2f(0,0);
		for(ForcedVelocModifier chave:forcedVelocModifiers.keySet())
			forcedVelocModFinal.add(forcedVelocModifiers.get(chave));
		return forcedVelocModFinal;
	}
	

	

}
