package com.firstJogo.Mundos;

import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.firstJogo.estrutura.CollisionDetector;
import com.firstJogo.estrutura.DirecoesPadrao;
import com.firstJogo.estrutura.EventoPadrao;
import com.firstJogo.estrutura.TempoEvento;
import com.firstJogo.estrutura.VelocModifiersPadrao;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Textura;
import com.firstJogo.visual.TipodeBloco;
//TODO: Aprimorar colisão.
//TODO: Aprimorar criação de texturas (nas subclasses) e subclassificar os eventos.
//TODO menor: Organizar Packages.
//TODO menor: aprimorar conversão pra ChunkCoords e BlocoCoords...
//TODO FUTURO: tornar independente a direção do olhar e a direção de movimento, com penalidade!
//TODO BEM FUTURO: colocar sistema de iluminação com o olhar no futuro!

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
			TempoEvento<?> seguirPlayer=EventoPadrao.seguirPlayerEvento(1, this);
			seguirPlayer.resetar();
			GeradorEventos.addTempoEvento(EventoPadrao.seguirPlayerEventoChave(this),
					seguirPlayer);
			TempoEvento<?> removerEntidade=EventoPadrao.removerEntidadeEvento(30, this);
			removerEntidade.resetar();
			GeradorEventos.addTempoEvento(EventoPadrao.removerEntidadeEventoChave(this),
					removerEntidade);
		}
	}

	public void kill() {
		mundo.getEntidades().remove(this);
		GeradorEventos.remEvento(EventoPadrao.seguirPlayerEventoChave(this));
		GeradorEventos.remEvento(EventoPadrao.removerEntidadeEventoChave(this));
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
			else 
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
//	private ArrayList<long[]> getColidCoords(Vector2f mundopos) {//Obter os blocos que ele pode estar colidindo
//		float deslocx=GlobalVariables.intperbloco*(hitboxPos[2][0]-hitboxPos[0][0])/2;
//		float deslocy=GlobalVariables.intperbloco*(hitboxPos[2][1]-hitboxPos[0][1])/2;
//		float xi=(mundopos.x-deslocx)/GlobalVariables.intperbloco;
//		float yi=(mundopos.y-deslocy)/GlobalVariables.intperbloco;
//		float xf=(mundopos.x+deslocx)/GlobalVariables.intperbloco;
//		float yf=(mundopos.y+deslocy)/GlobalVariables.intperbloco;
//		int xmax=(int) Math.floor(Math.abs(2*deslocx/GlobalVariables.intperbloco)+1);//Arredondando pra cima quantos blocos a entidade ocupa.
//		int ymax=(int) Math.floor(Math.abs(2*deslocy/GlobalVariables.intperbloco)+1);
//		ArrayList<long[]> coords=new ArrayList<long[]>();
//		for(int ix=-1-xmax/2;ix<xmax/2+1;ix++)
//			for(int iy=-1-ymax/2;iy<ymax/2+1;iy++)
//				if(
//						colide(xi, yi, xf, yf, getBlocoCoords()[0]+ix, getBlocoCoords()[1]+iy,getBlocoCoords()[0]+ix+1, getBlocoCoords()[1]+iy+1)
//						)
//					coords.add(new long[] {
//							getBlocoCoords()[0]+ix,
//							getBlocoCoords()[1]+iy
//				});
//		return coords;
//	}
//	private boolean colide(float xi,float yi,float xf,float yf,float xi2,float yi2,float xf2,float yf2) {
//		if(
//				dentrode(xi,yi,xf,yf,xi2,yi2)||dentrode(xi,yi,xf,yf,xf2,yf2)
//				||
//				dentrode(xi,yi,xf,yf,xi2,yf2)||dentrode(xi,yi,xf,yf,xf2,yi2)
//				||
//				dentrode(xi2,yi2,xf2,yf2,xi,yi)||dentrode(xi2,yi2,xf2,yf2,xf,yf)
//				||
//				dentrode(xi2,yi2,xf2,yf2,xf,yi)||dentrode(xi2,yi2,xf2,yf2,xi,yf)
//				)return true;
//		return false;
//	}
//	private boolean dentrode(float xi,float yi,float xf,float yf,float x, float y) {
//		if(x>xi&&x<xf&&y>yi&&y<yf)return true;
//		return false;
//	}
	
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

	public boolean setMundopos(Vector2f mundopos) {
		if (mundopos.x == this.mundoPos.x && mundopos.y == this.mundoPos.y)
			return false;
		if (!checkColisao(mundopos)) {
			forcedVelocModifiers.remove(VelocModifiersPadrao.ColisaoForcedModifier);
		} else {
			return false;
		}
		this.mundoPos = mundopos;
		return true;
	}
	
	public Vector2f getForcedVelocModified() {
		Vector2f forcedVelocModFinal=new Vector2f(0,0);
		for(String chave:forcedVelocModifiers.keySet())
			forcedVelocModFinal.add(forcedVelocModifiers.get(chave));
		return forcedVelocModFinal;
	}

}
