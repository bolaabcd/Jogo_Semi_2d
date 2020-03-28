package com.firstJogo.Mundos;

import com.firstJogo.visual.TipodeCriatura;

public class Entidade {
	public static Entidade player;
	private byte direc;//2 bits pra direção de olhar, 3 bits pra direção de movimento!
	private TipodeCriatura tipo_visual;
	private char veloc;
	
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
		//UP=xx x0 00 xx,DOWN=xx x0 01 xx,LEFT=xx x0 10 xx,RIGHT=xx x0 11 xx
		//UP RIGHT=xx x1 00 xx, UP LEFT=xx x1 01 xx, DOWN RIGHT= xx x1 10 xx, DOWN LEFT= xx x1 11 xx
		if(mover.equals("up"))direc=(byte) (direc&0xE3);
		else if(mover.equals("down"))direc=(byte) ((direc&0xE3)|0x04);
		else if(mover.equals("left"))direc=(byte) ((direc&0xE3)|0x08);
		else if(mover.equals("right"))direc=(byte) ((direc&0xE3)|0x0C);
		
		else if(mover.equals("ur"))direc=(byte) ((direc&0xE3)|0x10);
		else if(mover.equals("ul"))direc=(byte) ((direc&0xE3)|0x14);
		else if(mover.equals("dr"))direc=(byte) ((direc&0xE3)|0x18);
		else if(mover.equals("dl"))direc=(byte) ((direc&0xE3)|0x1C);
		
		else {
			System.out.println("DIREÇÃO DE MOVIMENTO INVÁLIDA!");
			System.exit(1);
		}
	}
	public String getMover() {
		if(((direc>>>2)&0x07)==0)return "up";
		else if(((direc>>>2)&0x07)==1)return "down";
		else if(((direc>>>2)&0x07)==2)return "left";
		else if(((direc>>>2)&0x07)==3)return "right";
		
		else if(((direc>>>2)&0x07)==4)return "ur";
		else if(((direc>>>2)&0x07)==5)return "ul";
		else if(((direc>>>2)&0x07)==6)return "dr";
		else return "dl";
		
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

	

}
