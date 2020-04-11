package com.firstJogo.Mundos;

import java.util.HashSet;
import java.util.Set;

import com.firstJogo.regras.DirecoesPadrao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Textura;

public class Entidade {
	private static Entidade player;
	
	
	private final TempoMarker mover=new TempoMarker(1000000,(objetotal)->{
		Object[] ob=(Object[]) objetotal;
		TempoMarker marcador=(TempoMarker) ob[1];
		Entidade ent=(Entidade)ob[2];
		if((Long)ob[3]==0)
			ob[3]=(Long)marcador.getTemporegistrado();//Tentei castar ob[3] para Long, mas ele não aceitou
		long movx=(long) Math.round(ent.getDirecModifiers()[0]*ent.getVelocModified()*GlobalVariables.intperbloco*(double)(System.nanoTime()-(Long)ob[3])/1000000000);
		long movy=(long) Math.round(ent.getDirecModifiers()[1]*ent.getVelocModified()*GlobalVariables.intperbloco*(double)(System.nanoTime()-(Long)ob[3])/1000000000);
		
		if (Math.abs(movx) > 1 || Math.abs(movy) > 1) {
			this.setMundopos(new long[] {
					ent.getMundopos()[0] + movx, 
					ent.getMundopos()[1] + movy 
			});
			ob[3] = 0L;
			
		}
	},new Object[] {3,null,this,new Long(0)});
	
	private boolean isPlayer;
	private Textura visual;
	private Modelo modelo;
	private DirecoesPadrao olharDir=DirecoesPadrao.CIMA;
	private double angulo=1200;
	private long[] mundopos;//Em ints de bloco! (32/bloco no padrão)
	private int[] hitboxPos;
	
	protected Set<Float> velocModifiers=new HashSet<Float>();
	protected boolean isParado=true;
	protected char veloc=0;
	
	
	public Entidade(Textura visu) {
		visual=visu;
		modelo=visu.genModelo();
		hitboxPos=modelo.getVertices();
	}
	
	public void pararMovimento() {
		if(isParado)return;
		isParado=true;
		if(!this.isPlayer())mover.desativar();
	}
	public void iniciarMovimento() {
		if(!isParado)return;
		isParado=false;
		if(!this.isPlayer())mover.ativar();
	}
	
	public DirecoesPadrao getOlhar() {
		return olharDir;
	}
	public void setOlhar(DirecoesPadrao olhar) {
		olharDir=olhar;
		
	}
	
	public void setAngulo(double angulo) {
		if(this.angulo==angulo)return;
		if(angulo<Math.PI/4&&angulo>-Math.PI/4)this.setOlhar(DirecoesPadrao.DIREITA);
		else if(angulo>3*Math.PI/4||angulo<-3*Math.PI/4)this.setOlhar(DirecoesPadrao.ESQUERDA);
		else if(angulo>=Math.PI/4&&angulo<=3*Math.PI/4)this.setOlhar(DirecoesPadrao.CIMA);
		else if(angulo<=-Math.PI/4&&angulo>=-3*Math.PI/4)this.setOlhar(DirecoesPadrao.BAIXO);
		else System.out.println("ERRADO");
		this.angulo=angulo;
	}
	
	public double getAngulo() {
		return angulo;
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
		if(isParado) return 0;
		float velocModFinal=1f;
		for(float modif:velocModifiers)
			velocModFinal*=modif;
		return velocModFinal*veloc;
	}
	public boolean addVelocModifier(float valor) {
		return velocModifiers.add(valor);
	}
	public boolean remVelocModifier(float valor) {
		return velocModifiers.remove(valor);
	}

	public double[] getDirecModifiers() {
		if(this.isParado)return new double[] {0,0};
		return new double[] {(double)Math.round(Math.cos(angulo)*100000d)/100000d,(double)Math.round(Math.sin(angulo)*100000d)/100000d};
	}

	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public boolean isPlayer() {
		return isPlayer;
	}

	public void setPlayer(boolean isPlayer) {
		if(isPlayer=true) {
			Entidade.player=this;
			this.isPlayer = isPlayer;
			}
		else {
			if(this.isPlayer)Entidade.player=null;
			this.isPlayer=false;
		}
		
	}

	public static Entidade getPlayer() {
		return player;
	}

	public long[] getMundopos() {
		return mundopos;
	}

	public void setMundopos(long[] mundopos) {
		this.mundopos = mundopos;
		if(!this.isPlayer)System.out.println("MundoPlayerPos x: "+this.getMundopos()[0]);
		if(!this.isPlayer)System.out.println("MundoPlayerPos y: "+this.getMundopos()[1]);
	}

	public int[] getHitboxPos() {
		return hitboxPos;
	}
	

	

}
