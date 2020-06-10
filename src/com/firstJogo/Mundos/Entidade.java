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

public class Entidade {
	public enum EventosEntidades{
		MOVER,
		REMOVER
	}
	private static Entidade player;//TODO REMOVER
			
	private Double anguloMovimento=null;
	private Vector2f mundoPos;//A coordenada 0,0 é a quina inferior direita do bloco 0,0 (centro do chunk 0,0).
	private MundoCarregado mundo;
	private float[][] hitboxPos;//Começa no baixo esquerda, sentido horário. 00 é a superior esquerda!
	private Textura visual;
	private Modelo modelo;
	private DirecoesPadrao olharDir=DirecoesPadrao.CIMA;
	private HashMap<VelocModifier,Float> velocModifiers=new HashMap<VelocModifier,Float>();
	private HashMap<ForcedVelocModifier,Vector2f> forcedVelocModifiers=new HashMap<ForcedVelocModifier,Vector2f>();
	
	protected short fantasmabilidade=10;
	protected char veloc=0;
	protected Entidade(Textura visu,Vector2f mundoPos,boolean isPlayer,MundoCarregado mundo,Modelo model) {
		this.visual=visu;
		this.mundoPos=mundoPos;
		this.mundo=mundo;
		this.modelo=model;
		hitboxPos=modelo.getVertices();
		velocModifiers.put(VelocModifier.PARADO, 0f);
		setPlayer(isPlayer);
		if (!isPlayer) {
			GeradorEventos.addTempoEvento(new ElementoDuplo<Entidade, EventosEntidades>(this, EventosEntidades.MOVER,false),
					EventoPadrao.moverEntidadeEvento(1, this));
			GeradorEventos.addTempoEvento(new ElementoDuplo<Entidade, EventosEntidades>(this, EventosEntidades.REMOVER,false),
					EventoPadrao.removerEntidadeEvento(3, this));
		}
	}
	protected Entidade(Textura visu,Vector2f mundoPos,boolean isPlayer) {
		this(visu,mundoPos,isPlayer,Entidade.player.mundo,Textura.modeloPadrao);
	}
	public void setMundo(MundoCarregado mundo) {
		this.mundo=mundo;
	}
	public void kill() {
		mundo.getEntidades().remove(this);
		GeradorEventos.remEvento(new ElementoDuplo<Entidade, EventosEntidades>(this, EventosEntidades.MOVER, false));
		GeradorEventos.remEvento(new ElementoDuplo<Entidade, EventosEntidades>(this, EventosEntidades.REMOVER, false));
	}
	public boolean isParado() {
		return velocModifiers.get(VelocModifier.PARADO)==0;
	}
	public MundoCarregado getMundo() {
		return mundo;
	}
	private boolean checkColisao(Vector2f mundopos) {
		boolean res=false;
		for(long[] bloco:getColidCoords(mundopos)) { //TODO: Colocar no mundo em que a entidade está carregada! (aprimorar spawn)
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
		if (!this.isPlayer()) {
			GeradorEventos.<TempoEvento<?>>getEvento(new ElementoDuplo<>(this, EventosEntidades.MOVER, false))
					.resetar();
			GeradorEventos.<TempoEvento<?>>getEvento(new ElementoDuplo<>(this, EventosEntidades.REMOVER, false))
			.resetar();	
		}
		return true;
	}
	
	public DirecoesPadrao getOlhar() {
		return olharDir;
	}
	public void setOlhar(DirecoesPadrao olhar) {
		olharDir=olhar;
		
	}
	public long[] getChunkCoords() {//Chunk de -8 a 7.
		return new long[] {
				(long)Math.floor((Entidade.getPlayer().getMundopos().x/GlobalVariables.intperbloco)/16+0.5f),
				(long)Math.floor((Entidade.getPlayer().getMundopos().y/GlobalVariables.intperbloco)/16+0.5f)
		};
	}
	public long[] getBlocoCoords() {//Bloco do centro da entidade.
		return new long[] {
				(long)Math.floor(this.getMundopos().x/GlobalVariables.intperbloco),
				(long)Math.floor(this.getMundopos().y/GlobalVariables.intperbloco),
		};
	}
	public boolean setAnguloMovimento(double angulo) {
		if(this.anguloMovimento!=null)
			if(Math.abs(this.anguloMovimento-Math.round(angulo*180/Math.PI)*Math.PI/180)*180/Math.PI<=1)return false;
				
		if(angulo<Math.PI/4&&angulo>-Math.PI/4)this.setOlhar(DirecoesPadrao.DIREITA);
		else if(angulo>3*Math.PI/4||angulo<-3*Math.PI/4)this.setOlhar(DirecoesPadrao.ESQUERDA);
		else if(angulo>=Math.PI/4&&angulo<=3*Math.PI/4)this.setOlhar(DirecoesPadrao.CIMA);
		else if(angulo<=-Math.PI/4&&angulo>=-3*Math.PI/4)this.setOlhar(DirecoesPadrao.BAIXO);
		else System.out.println("ERRADO");
		this.anguloMovimento=Math.round(angulo*180/Math.PI)*Math.PI/180;
		return true;
	}
	
	public double getAnguloMovimento() {
		return anguloMovimento;
	}
	public Textura getTextura() {
		return visual;
	}
	public void setTextura(Textura visual) {
		this.visual = visual;
	}

	public char getVeloc() {
		return veloc;
	}

	public void setVeloc(char veloc) {
		this.veloc=veloc;
	}

	public float getVelocModified() {
		if(isParado()) return 0;
		float velocModFinal=1f;
		for(VelocModifier modifchave:velocModifiers.keySet())
			velocModFinal*=velocModifiers.get(modifchave);
		return velocModFinal*veloc;
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

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public boolean isPlayer() {
		return Entidade.player==this?Entidade.player.equals(this):false;
	}

	private void setPlayer(boolean isPlayer) {
		if(isPlayer) {
			Entidade.player=this;
			}
		else {
			if(isPlayer())Entidade.player=null;
		}
		
	}

	public static Entidade getPlayer() {
		return player;
	}

	public Vector2f getMundopos() {
		return mundoPos;
	}

	public boolean setMundopos(Vector2f mundopos) {
		if(mundopos.x==this.mundoPos.x&&mundopos.y==this.mundoPos.y) return false;
		if(!checkColisao(mundopos)) {
			forcedVelocModifiers.remove(ForcedVelocModifier.COLISAO);
			
		}else {
			return false;
		}
		this.mundoPos = mundopos;
		if(GlobalVariables.debugue&&!this.isPlayer())System.out.println("MundoPlayerPos x: "+this.getMundopos().x);
		if(GlobalVariables.debugue&&!this.isPlayer())System.out.println("MundoPlayerPos y: "+this.getMundopos().y);
		return true;
	}

	public float[][] getHitboxPos() {
		return hitboxPos;
	}
	public Vector2f getForcedVelocModified() {
		Vector2f forcedVelocModFinal=new Vector2f(0,0);
		for(ForcedVelocModifier chave:forcedVelocModifiers.keySet())
			forcedVelocModFinal.add(forcedVelocModifiers.get(chave));
		return forcedVelocModFinal;
	}
	public boolean addForcedVelocModifier(ForcedVelocModifier chave, Vector2f valor) {
		if(forcedVelocModifiers.put(chave, valor)==null)return false;
		return true;
	}
	public boolean remForcedVelocModifier(ForcedVelocModifier chave) {
		if(forcedVelocModifiers.remove(chave)==null)return false;
		return true;
	}

	

}
