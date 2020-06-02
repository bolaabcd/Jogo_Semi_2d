package com.firstJogo.Mundos;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2f;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.estrutura.DirecoesPadrao;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Textura;
import com.firstJogo.visual.TipodeBloco;

public class Entidade {
	private static final long segundosRemover=30;
	private static Entidade player;
	
	protected final TempoMarker mover=new TempoMarker(1000000);
	protected final TempoMarker remover=new TempoMarker(segundosRemover*1000000000L);
//			new TempoMarker(1000000,(objetotal)->{
//				Object[] ob=(Object[]) objetotal;
//				TempoMarker marcador=(TempoMarker) ob[1];
//				Entidade ent=(Entidade)ob[2];
//				
//				float movx=(float) ((ent.getForcedVelocModified().x+ent.getDirecModifiers()[0]*ent.getVelocModified())*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000);
//				float movy=(float) ((ent.getForcedVelocModified().y+ent.getDirecModifiers()[1]*ent.getVelocModified())*GlobalVariables.intperbloco*(double)(System.nanoTime()-marcador.getTemporegistrado())/1000000000);
//
//				ent.setMundopos(new Vector2f (
//						ent.getMundopos().x + movx, 
//						ent.getMundopos().y 
//						));
//				
//				ent.setMundopos(new Vector2f (
//						ent.getMundopos().x, 
//						ent.getMundopos().y  +  movy 
//						));
////				System.out.println("X: "+ent.getBlocoCoords()[0]);
////				System.out.println("Y: "+ent.getBlocoCoords()[1]);
//
//				
//			},new Object[] {2,null,this});
	private boolean isPlayer;
	private double angulo=1200;
	private Vector2f mundopos;//A coordenada 0,0 é a quina inferior direita do bloco 0,0 (centro do chunk 0,0).
	private float[][] hitboxPos;//Começa no baixo esquerda, sentido horário. 00 é a superior esquerda!
	private Textura visual;
	private Modelo modelo;
	private DirecoesPadrao olharDir=DirecoesPadrao.CIMA;
	private HashMap<String,Float> velocModifiers=new HashMap<String,Float>();
	private HashMap<String,Vector2f> forcedVelocModifiers=new HashMap<String,Vector2f>();
//	private Vector2f colidido=new Vector2f(0,0);
	
	protected short fantasmabilidade=10;
	protected boolean isParado=true;
	protected char veloc=0;
	
	protected Entidade(Textura visu,Vector2f mundopos,boolean isPlayer) {
		visual=visu;
		modelo=visu.genModelo();
		hitboxPos=modelo.getVertices();
//		this.isPlayer=isPlayer;
		setPlayer(isPlayer);
//		if(mundopos.length!=2)throw new IllegalArgumentException("Quantidade inválida de coordenadas!");
		this.mundopos=mundopos;
//		if(!isPlayer)GeradorEventos.entidadeTempoHandler.addEvento(mover, new FuncaoHandler<Entidade>((entidade)->{
		if (!isPlayer) {
			GeradorEventos.addTempoEvento(Entidade.class, new FuncaoHandler<Entidade>((entidade) -> {

//			Object[] ob=(Object[]) objetotal;
				TempoMarker marcador = entidade.mover;
//				System.out.println(marcador);
				Entidade ent = entidade;

				float movx = (float) ((ent.getForcedVelocModified().x
						+ ent.getDirecModifiers()[0] * ent.getVelocModified()) * GlobalVariables.intperbloco
						* (double) (System.nanoTime() - marcador.getTemporegistrado()) / 1000000000);
				float movy = (float) ((ent.getForcedVelocModified().y
						+ ent.getDirecModifiers()[1] * ent.getVelocModified()) * GlobalVariables.intperbloco
						* (double) (System.nanoTime() - marcador.getTemporegistrado()) / 1000000000);

				ent.setMundopos(new Vector2f(ent.getMundopos().x + movx, ent.getMundopos().y));

				ent.setMundopos(new Vector2f(ent.getMundopos().x, ent.getMundopos().y + movy));
//			System.out.println("X: "+ent.getBlocoCoords()[0]);
//			System.out.println("Y: "+ent.getBlocoCoords()[1]);

			}, this), mover);
//			GeradorEventos.entidadeTempoHandler.addEvento(remover, new FuncaoHandler<Entidade>((entidade)->{
			GeradorEventos.addTempoEvento(Entidade.class, new FuncaoHandler<Entidade>((entidade) -> {
				GeradorEventos.forcedRemMarker(entidade.remover);
				GeradorEventos.forcedRemMarker(entidade.mover);

			}, this), remover);
//			mover.ativar();
		}
//		remover.ativar();
	}
	private boolean checkColisao(Vector2f mundopos) {
//		System.out.println(getColidCoords(mundopos).size());
		boolean res=false;
//		int i=1;
		for(long[] bloco:getColidCoords(mundopos)) { //TODO: Colocar no mundo em que a entidade está carregada! (aprimorar spawn)
			TipodeBloco tipo=TipodeBloco.azulejos[MundoCarregado.atual.getbloco(bloco[0], bloco[1])];
//			System.out.println("X"+i+": "+bloco[0]);
//			System.out.println("Y"+i+": "+bloco[1]);
//			i++;
			
//			System.out.println(tipo.getTangibilidade()>this.fantasmabilidade);
			if(tipo.getTangibilidade()>this.fantasmabilidade) {
				tipo.getFuncaoColisiva().accept(new Object[] {this,Bloco.toCentroBloco(bloco[0], bloco[1])});
				res=true;
			}
		}
//		System.out.println("Xb: "+this.getBlocoCoords()[0]);
//		System.out.println("Yb: "+this.getBlocoCoords()[1]);
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
//		float deslocx=GlobalVariables.intperbloco*(hitboxPos[2][0]-hitboxPos[0][0])/2;
//		float deslocy=GlobalVariables.intperbloco*(hitboxPos[2][1]-hitboxPos[0][1])/2;
//		float xi=(mundopos.x-deslocx);
//		float yi=(mundopos.y-deslocy);
//		float xf=(mundopos.x+deslocx);
//		float yf=(mundopos.y+deslocy);
//		int xmax=(int) Math.floor(Math.abs(2*deslocx)+1);//Arredondando pra cima quantos blocos a entidade ocupa.
//		int ymax=(int) Math.floor(Math.abs(2*deslocy)+1);
		ArrayList<long[]> coords=new ArrayList<long[]>();
		for(int ix=-1-xmax/2;ix<xmax/2+1;ix++)
			for(int iy=-1-ymax/2;iy<ymax/2+1;iy++)
				if(
						colide(xi, yi, xf, yf, getBlocoCoords()[0]+ix, getBlocoCoords()[1]+iy,getBlocoCoords()[0]+ix+1, getBlocoCoords()[1]+iy+1)
//						colide(xi, yi, xf, yf, getBlocoCoords()[0]*GlobalVariables.intperbloco+ix*GlobalVariables.intperbloco, getBlocoCoords()[1]*GlobalVariables.intperbloco+iy*GlobalVariables.intperbloco,getBlocoCoords()[0]*GlobalVariables.intperbloco+ix*GlobalVariables.intperbloco+30, getBlocoCoords()[1]*GlobalVariables.intperbloco+iy*GlobalVariables.intperbloco+30)
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
		for(String modifchave:velocModifiers.keySet())
			velocModFinal*=velocModifiers.get(modifchave);
//		for(String modiforchave:forcedVelocModifiers.keySet())
//			velocModFinal*=forcedVelocModifiers.get(modiforchave);
		return velocModFinal*veloc;
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

	private void setPlayer(boolean isPlayer) {
		if(isPlayer) {
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

	public Vector2f getMundopos() {
		return mundopos;
	}

	public boolean setMundopos(Vector2f mundopos) {
		if(mundopos.x==this.mundopos.x&&mundopos.y==this.mundopos.y) return false;
		if(!checkColisao(mundopos)) {
//			System.out.println("Que");
//			if(forcedVelocModifiers.remove("colisao")!=null)return false;//Se removeu algo retorna falso.
			forcedVelocModifiers.remove("colisao");
//			colidido=new Vector2f(0f,0f);
			
		}else {
			return false;
		}
		this.mundopos = mundopos;
		if(GlobalVariables.debugue&&!this.isPlayer)System.out.println("MundoPlayerPos x: "+this.getMundopos().x);
		if(GlobalVariables.debugue&&!this.isPlayer)System.out.println("MundoPlayerPos y: "+this.getMundopos().y);
		return true;
	}

	public float[][] getHitboxPos() {
		return hitboxPos;
	}
	public Vector2f getForcedVelocModified() {
		Vector2f forcedVelocModFinal=new Vector2f(0,0);
		for(String chave:forcedVelocModifiers.keySet())
			forcedVelocModFinal.add(forcedVelocModifiers.get(chave));
		return forcedVelocModFinal;
	}
	public boolean addForcedVelocModifier(String chave, Vector2f valor) {
//		System.out.println("OI");
		if(forcedVelocModifiers.put(chave, valor)==null)return false;
		return true;
	}
	public boolean remForcedVelocModifier(String chave) {
		if(forcedVelocModifiers.remove(chave)==null)return false;
		return true;
	}
//	public Vector2f getColidido() {
//		return colidido;
//	}
//	public void setColidido(Vector2f colidido) {
//		this.colidido = colidido;
//	}

	

}
