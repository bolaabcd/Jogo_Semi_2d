package com.firstJogo.Mundos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.firstJogo.regras.DirecoesPadrao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Textura;
import com.firstJogo.visual.TipodeBloco;
import com.firstJogo.visual.WorldRenderer;

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
//		System.out.println("X: "+ent.getBlocoCoords()[0]);
//		System.out.println("Y: "+ent.getBlocoCoords()[1]);

		
	},new Object[] {2,null,this});
	
	private boolean isPlayer;
	private double angulo=1200;
	private float[] mundopos;//A coordenada 0,0 é a quina inferior direita do bloco 0,0 (centro do chunk 0,0).
	private float[][] hitboxPos;//Começa no baixo esquerda, sentido horário. 00 é a superior esquerda!
	private Textura visual;
	private Modelo modelo;
	private DirecoesPadrao olharDir=DirecoesPadrao.CIMA;
	
	protected Set<Float> velocModifiers=new HashSet<Float>();
	protected short fantasmabilidade=10;
	protected boolean isParado=true;
	protected char veloc=0;
	
	
	public Entidade(Textura visu,float[] mundopos) {
		visual=visu;
		modelo=visu.genModelo();
		hitboxPos=modelo.getVertices();
		if(mundopos.length!=2)throw new IllegalArgumentException("Quantidade inválida de coordenadas!");
		this.mundopos=mundopos;
	}
	private boolean checkColisao(float[] mundopos) {
//		System.out.println(getColidCoords(mundopos).size());
		for(long[] bloco:getColidCoords(mundopos))  //TODO: Colocar no mundo em que a entidade está carregada! (aprimorar spawn)
			if(TipodeBloco.azulejos[MundoCarregado.atual.getbloco(bloco[0], bloco[1])].getTangibilidade()>this.fantasmabilidade)return true;
//		System.out.println();
//		for(long[] bloco:getColidCoords(mundopos)) {
//			System.out.println("X: "+bloco[0]);
//			System.out.println("Y: "+bloco[1]);
//		}
		return false;
	}
	private ArrayList<long[]> getColidCoords(float[] mundopos) {//Obter os blocos que ele pode estar colidindo
		float deslocx=GlobalVariables.intperbloco*(hitboxPos[2][0]-hitboxPos[0][0])/2;
		float deslocy=GlobalVariables.intperbloco*(hitboxPos[2][1]-hitboxPos[0][1])/2;
		float xi=(mundopos[0]-deslocx)/GlobalVariables.intperbloco;
		float yi=(mundopos[1]-deslocy)/GlobalVariables.intperbloco;
		float xf=(mundopos[0]+deslocx)/GlobalVariables.intperbloco;
		float yf=(mundopos[1]+deslocy)/GlobalVariables.intperbloco;
		int xmax=(int) Math.floor(Math.abs(2*deslocx/30)+1);//Arredondando pra cima quantos blocos a entidade ocupa.
		int ymax=(int) Math.floor(Math.abs(2*deslocy/30)+1);
		ArrayList<long[]> coords=new ArrayList<long[]>();
		for(int ix=-1-xmax/2;ix<xmax/2+1;ix++)
			for(int iy=-1-ymax/2;iy<ymax/2+1;iy++)
				if(
						colide(xi, yi, xf, yf, getBlocoCoords()[0]+ix, getBlocoCoords()[1]+iy,getBlocoCoords()[0]+ix+1, getBlocoCoords()[1]+iy+1)
//						||
//						colide(xi, yi, xf, yf, getBlocoCoords()[0]+ix, getBlocoCoords()[1]+iy,getBlocoCoords()[0]+ix+1, getBlocoCoords()[1]+iy+1)
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
	public long[] getChunkCoords() {//Chunk de -8 a 7.
		return new long[] {
				(long)Math.floor((Entidade.getPlayer().getMundopos()[0]/GlobalVariables.intperbloco)/16+0.5f),
				(long)Math.floor((Entidade.getPlayer().getMundopos()[1]/GlobalVariables.intperbloco)/16+0.5f)
		};
	}
	public long[] getBlocoCoords() {//Bloco do centro da entidade.
		return new long[] {
				(long)Math.floor(this.getMundopos()[0]/GlobalVariables.intperbloco),
				(long)Math.floor(this.getMundopos()[1]/GlobalVariables.intperbloco),
		};
	}
	public boolean setAngulo(double angulo) {
		if(Math.abs(this.angulo-Math.round(angulo*180/Math.PI)*Math.PI/180)*180/Math.PI<=1)return false;
				
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

	public boolean setMundopos(float[] mundopos) {
		if(mundopos[0]==this.mundopos[0]&&mundopos[1]==this.mundopos[1]) return false;
		if(checkColisao(mundopos)) {
			pararMovimento();
			return false;
		}
		this.mundopos = mundopos;
		if(GlobalVariables.debugue&&!this.isPlayer)System.out.println("MundoPlayerPos x: "+this.getMundopos()[0]);
		if(GlobalVariables.debugue&&!this.isPlayer)System.out.println("MundoPlayerPos y: "+this.getMundopos()[1]);
		return true;
	}

	public float[][] getHitboxPos() {
		return hitboxPos;
	}

	

}
