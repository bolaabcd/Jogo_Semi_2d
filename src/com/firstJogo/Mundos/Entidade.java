package com.firstJogo.Mundos;

import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.TipodeCriatura;

public class Entidade {
	
	public static Entidade player;
	
	private byte direc;//2 bits pra direção de olhar, 4 bits pra direção de movimento!
	private TipodeCriatura tipo_visual;
	private char veloc=5;
	private double direcModifier=1;
	
	protected float velocModifier;//60% pra corrida maratonada, 100% na corrida rápida, se ficar em linha reta 2 segundos muda pra 120%, 10% pra agachado.

	
	public boolean IsPlayer;
	
	public Entidade(TipodeCriatura tipo) {
		direc=0;
		tipo_visual=tipo;
	}
	
	public String getOlhar() {
		if((direc&0x03)==0)return "up";
		else if((direc&0x03)==1)return "down";
		else if((direc&0x03)==2)return "left";
		else return "right";
	}
	public void setOlhar(String olhar) {
		//UP=xxxxxx00,DOWN=xxxxxx01,LEFT=xxxxxx10,RIGHT=xxxxxx11
		if(olhar.equals("up"))direc=(byte) (direc&0xFC);
		else if(olhar.equals("down"))direc=(byte) ((direc&0xFC)|0x01);
		else if(olhar.equals("left"))direc=(byte) ((direc&0xFC)|0x02);
		else if(olhar.equals("right"))direc=(byte)(direc|0x03);
		else {
			System.out.println("DIREÇÃO DE OLHAR INVÁLIDA!");
			System.exit(1);
		}
		
	}
	public void setMover(String mover) {
		//UP=xx 10 1x xx,DOWN=xx 10 0x xx,LEFT=xx 01 x0 xx,RIGHT=xx 01 x1 xx
		//UP RIGHT=xx 11 11 xx, UP LEFT=xx 11 10 xx, DOWN RIGHT= xx 11 01 xx, DOWN LEFT= xx 11 00 xx
		//NADA=xx 00 xx xx (2 bits falando qual tá ligado e 2 falando qual tipo seria se tivesse ligado)
		boolean X=false;
		boolean Y=false;
		if(IsPlayer) {
			short[] mov=getMoverxy();
			if(mov[0]!=0)X=true;
			if(mov[1]!=0)Y=true;
		}
		if(mover.equals("up")) {
			direc=(byte) (direc|0x28);//Ativando Y e colocando como up
			if(X)direcModifier=GlobalVariables.raizde2inv;
		}
		else if(mover.equals("down")) {
			direc=(byte) ((direc|0x20)&0xF7);//Ativando Y e colocando como down
			if(X)direcModifier=GlobalVariables.raizde2inv;
		}
		else if(mover.equals("left")) {
			direc=(byte) ((direc|0x10)&0xFB);//Ativando X e colocando como left
			if(Y)direcModifier=GlobalVariables.raizde2inv;
		}
		else if(mover.equals("right")) {
			direc=(byte) (direc|0x14);//Ativando X e colocando como right
			if(Y)direcModifier=GlobalVariables.raizde2inv;
		}
		
//		else if(mover.equals("ur"))direc=(byte) ((direc&0xE3)|0x10);
//		else if(mover.equals("ul"))direc=(byte) ((direc&0xE3)|0x14);
//		else if(mover.equals("dr"))direc=(byte) ((direc&0xE3)|0x18);
//		else if(mover.equals("dl"))direc=(byte) ((direc&0xE3)|0x1C);
		
		else {
			System.out.println("DIREÇÃO DE MOVIMENTO INVÁLIDA!");
			System.exit(1);
		}
	}
//	public void addMover(String mover) {
//		//UP=xx x0 00 xx,DOWN=xx x0 01 xx,LEFT=xx x0 10 xx,RIGHT=xx x0 11 xx
//		//UP RIGHT=xx x1 00 xx, UP LEFT=xx x1 01 xx, DOWN RIGHT= xx x1 10 xx, DOWN LEFT= xx x1 11 xx
//		short[] mov=getMoverxy();
//		if(getVeloc()!=0) {
//			//Se não tá nem esquerda nem direita, só setar a direção dada.
//			if(mover.equals("up")&&mov[0]==0)direc=(byte) (direc&0xE3);//up
//			else if(mover.equals("down")&&mov[0]==0)direc=(byte) ((direc&0xE3)|0x04);//down
//			else if(mover.equals("left")&&mov[1]==0)direc=(byte) ((direc&0xE3)|0x08);//left
//			else if(mover.equals("right")&&mov[1]==0)direc=(byte) ((direc&0xE3)|0x0C);//right
//			
//			//Se já está esquerda ou direita, adaptar pras diagonais.
//			else if(mover.equals("up")&&mov[0]==1)direc=(byte) ((direc&0xE3)|0x10);//ur
//			else if(mover.equals("up")&&mov[0]==-1)direc=(byte) ((direc&0xE3)|0x14);//ul
//			else if(mover.equals("down")&&mov[0]==1)direc=(byte) ((direc&0xE3)|0x18);//dr
//			else if(mover.equals("down")&&mov[0]==-1)direc=(byte) ((direc&0xE3)|0x1C);//dl
//		
//			//Se já está cima ou baixo, adaptar pras diagonais.
//			else if(mover.equals("left")&&mov[1]==1)direc=(byte) ((direc&0xE3)|0x14);//ul
//			else if(mover.equals("left")&&mov[1]==-1)direc=(byte) ((direc&0xE3)|0x1C);//dl
//			else if(mover.equals("right")&&mov[1]==1)direc=(byte) ((direc&0xE3)|0x10);//ur
//			else if(mover.equals("right")&&mov[1]==-1)direc=(byte) ((direc&0xE3)|0x18);//dr
//			else {
//				System.out.println("DIREÇÃO DE MOVIMENTO INVÁLIDA!");
//				System.exit(1);
//			}
//		} else {
//			if(mover.equals("up"))direc=(byte) (direc&0xE3);//up
//			else if(mover.equals("down"))direc=(byte) ((direc&0xE3)|0x04);//down
//			else if(mover.equals("left"))direc=(byte) ((direc&0xE3)|0x08);//left
//			else if(mover.equals("right"))direc=(byte) ((direc&0xE3)|0x0C);//right
//		}
//		
//		
//	}
	public void remMover(String mover) {
		//UP=xx 10 1x xx,DOWN=xx 10 0x xx,LEFT=xx 01 x0 xx,RIGHT=xx 01 x1 xx
		//UP RIGHT=xx 11 11 xx, UP LEFT=xx 11 10 xx, DOWN RIGHT= xx 11 01 xx, DOWN LEFT= xx 11 00 xx
		//NADA=xx 00 xx xx (2 bits falando qual tá ligado e 2 falando qual tipo seria se tivesse ligado)
		
		short[] mov=getMoverxy();
		if((mover.equals("up")&&mov[1]!=-1)||(mover.equals("down")&&mov[1]!=1)) {//se não tiver indo na direção oposta em Y
			direc=(byte) (direc&0xDF);//Desativando Y 
			if(IsPlayer)direcModifier=1f;//Alterando para movimento linear se for player.
		}
		else if((mover.equals("left")&&mov[0]!=1)||(mover.equals("right")&&mov[0]!=-1)) {//se não tiver na direção oposta em X
			direc=(byte) (direc&0xEF);//Desativando X 
			if(IsPlayer)direcModifier=1f;//Alterando para movimento linear se for player.
		}
		
		else if((mover.equals("up")&&mov[1]==-1)||(mover.equals("down")&&mov[1]==1)||(mover.equals("left")&&mov[0]==1)||(mover.equals("right")&&mov[0]==-1));//Ignorando se tiver numa direção oposta.
		
		
//		short[] mov=getMoverxy();
//		if(mover.equals("up")&&mov[0]==1)direc=(byte) ((direc&0xE3)|0x0C);//right, de ur
//		else if(mover.equals("up")&&mov[0]==-1)direc=(byte) ((direc&0xE3)|0x08);//left, de ul
//		else if(mover.equals("down")&&mov[0]==1)direc=(byte) ((direc&0xE3)|0x0C);//right, de dr
//		else if(mover.equals("down")&&mov[0]==-1)direc=(byte) ((direc&0xE3)|0x08);//left, de dl
//		
//		else if(mover.equals("up")&&mov[1]==1&&mov[0]==0)setVeloc((char) 0);//Parando de up
//		else if(mover.equals("down")&&mov[1]==-1&&mov[0]==0)setVeloc((char) 0);//Parando de down
//		
//		else if(mover.equals("left")&&mov[1]==1)direc=(byte) (direc&0xE3);//up, de ul
//		else if(mover.equals("left")&&mov[1]==-1)direc=(byte) ((direc&0xE3)|0x04);//down, de dl
//		else if(mover.equals("right")&&mov[1]==1)direc=(byte) (direc&0xE3);//up, de ur
//		else if(mover.equals("right")&&mov[1]==-1)direc=(byte) ((direc&0xE3)|0x04);//down, de dr
//		
//		else if(mover.equals("right")&&mov[0]==1&&mov[1]==0)setVeloc((char) 0);//Parando de right
//		else if(mover.equals("left")&&mov[0]==-1&&mov[1]==0)setVeloc((char) 0);//Parando de left
//		
//		else if(mover.equals("up")&&mov[1]==-1);//ignorando se soltou up estando down
//		else if(mover.equals("down")&&mov[1]==1);//ignorando se soltou down estando up
//		else if(mover.equals("right")&&mov[0]==-1);//ignorando se soltou right estando left
//		else if(mover.equals("left")&&mov[0]==1);//ignorando se soltou left estando right
		
		
		
		
		else {
			System.out.println("DIREÇÃO DE MOVIMENTO INVÁLIDA!");
			System.exit(1);
		}
	}
//	public String getMover() {
//		if(((direc>>>2)&0x07)==0)return "up";
//		else if(((direc>>>2)&0x07)==1)return "down";
//		else if(((direc>>>2)&0x07)==2)return "left";
//		else if(((direc>>>2)&0x07)==3)return "right";
//		
//		else if(((direc>>>2)&0x07)==4)return "ur";
//		else if(((direc>>>2)&0x07)==5)return "ul";
//		else if(((direc>>>2)&0x07)==6)return "dr";
//		else return "dl";
//		
//	}
	
	public short[] getMoverxy() {//1 pra positivo -1 pra negativo no mapa, 0 pra nada
		//UP=xx 10 1x xx,DOWN=xx 10 0x xx,LEFT=xx 01 x0 xx,RIGHT=xx 01 x1 xx
		//UP RIGHT=xx 11 11 xx, UP LEFT=xx 11 10 xx, DOWN RIGHT= xx 11 01 xx, DOWN LEFT= xx 11 00 xx
		//NADA=xx 00 xx xx (2 bits falando qual tá ligado e 2 falando qual tipo seria se tivesse ligado)
		short[] saida=new short[2];
		if((direc&0x10)==0)saida[0]=0;//Se X estiver desligado
		else if((direc&0x04)==0)saida[0]=-1;//Se tiver pra esquerda
		else saida[0]=1;//Se tá ligado e não tá pra esquerda, só pode estar pra direita.
		
		if((direc&0x20)==0)saida[1]=0;//Se Y estiver desligado
		else if((direc&0x08)==0)saida[1]=-1;//Se tiver pra baixo
		else saida[1]=1;//Se tá ligado e não tá pra baixo, só pode estar pra cima.
		
		return saida;
	}
	public TipodeCriatura getTipo_visual() {
		return tipo_visual;
	}
	public void setTipo_visual(TipodeCriatura tipo_visual) {
		this.tipo_visual = tipo_visual;
	}

	public char getVeloc() {
		return veloc;
	}

	public void setVeloc(char veloc) {
		this.veloc=veloc;
	}

	public float getVelocModifier() {
		return velocModifier;
	}

	public double getDirecModifier() {
		return direcModifier;
	}
	
	

	

}
