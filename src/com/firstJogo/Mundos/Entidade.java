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

		float movx=(float) (ent.getDirecModifiers()[0]*ent.getVelocModified()*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000);
		float movy=(float) (ent.getDirecModifiers()[1]*ent.getVelocModified()*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000);

		ent.setMundopos(new float[] { 
				ent.getMundopos()[0] + movx, 
				ent.getMundopos()[1] + movy 
				});
		
	},new Object[] {2,null,this});
	
	private boolean isPlayer;
	private Textura visual;
	private Modelo modelo;
	private DirecoesPadrao olharDir=DirecoesPadrao.CIMA;
	private double angulo=1200;
	private float[] mundopos;//Em ints de bloco! (32/bloco no padrão)
	private int[] hitboxPos;
	
	protected Set<Float> velocModifiers=new HashSet<Float>();
	protected boolean isParado=true;
	protected char veloc=0;
	
	
	public Entidade(Textura visu) {
		visual=visu;
		modelo=visu.genModelo();
		hitboxPos=modelo.getVertices();
	}
	
	public boolean pararMovimento() {//True se foi, False se não.
		if(isParado)return false;
		isParado=true;
		if(!this.isPlayer())mover.desativar();
		return true;
	}
	public boolean iniciarMovimento() {
		if(!isParado)return false;
		isParado=false;
		if(!this.isPlayer())mover.ativar();
		return true;
	}
	
	public DirecoesPadrao getOlhar() {
		return olharDir;
	}
	public void setOlhar(DirecoesPadrao olhar) {
		olharDir=olhar;
		
	}
	
	public boolean setAngulo(double angulo) {
		if(Math.abs(this.angulo-Math.round(angulo*180/Math.PI)*Math.PI/180)*180/Math.PI<=1)return false;
		
//		if(!this.isPlayer)System.out.println(Math.round((this.angulo-angulo)*180/Math.PI));
		
		if(angulo<Math.PI/4&&angulo>-Math.PI/4)this.setOlhar(DirecoesPadrao.DIREITA);
		else if(angulo>3*Math.PI/4||angulo<-3*Math.PI/4)this.setOlhar(DirecoesPadrao.ESQUERDA);
		else if(angulo>=Math.PI/4&&angulo<=3*Math.PI/4)this.setOlhar(DirecoesPadrao.CIMA);
		else if(angulo<=-Math.PI/4&&angulo>=-3*Math.PI/4)this.setOlhar(DirecoesPadrao.BAIXO);
		else System.out.println("ERRADO");
		this.angulo=Math.round(angulo*180/Math.PI)*Math.PI/180;
		return true;
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

	public float[] getMundopos() {
		return mundopos;
	}

	public void setMundopos(float[] mundopos) {
		this.mundopos = mundopos;
		if(GlobalVariables.debugue&&!this.isPlayer)System.out.println("MundoPlayerPos x: "+this.getMundopos()[0]);
		if(GlobalVariables.debugue&&!this.isPlayer)System.out.println("MundoPlayerPos y: "+this.getMundopos()[1]);
	}

	public int[] getHitboxPos() {
		return hitboxPos;
	}
	

	

}
