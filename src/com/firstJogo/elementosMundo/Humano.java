package com.firstJogo.elementosMundo;

import com.firstJogo.elementosJogo.Player;
import com.firstJogo.elementosJogo.TempoEvento;
import com.firstJogo.elementosJogo.TexturaAnimador;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.padroes.EventosPadrao;
import com.firstJogo.padroes.GlobalVariables;
import com.firstJogo.padroes.PlayerRegras;
import com.firstJogo.visual.Textura;

public class Humano extends Entidade implements Impulsionavel{
	private modos movModo;
	private final long milisImpulso;
	private float sprintModifier;
	private TexturaAnimador animado;
	
	private final static long mediandando=450000000;
	private final static Textura[][] udlrTextura=new Textura[][] {
		new Textura[] {
				new Textura(GlobalVariables.imagem_path+"HumanoUp1"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoUp2"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoUp3"+GlobalVariables.imagem_formato),
		},
		new Textura[] {
				new Textura(GlobalVariables.imagem_path+"HumanoDown1"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoDown2"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoDown3"+GlobalVariables.imagem_formato),
		},
		new Textura[] {
				new Textura(GlobalVariables.imagem_path+"HumanoLeft1"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoLeft2"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoLeft3"+GlobalVariables.imagem_formato),
		},
		new Textura[] {
				new Textura(GlobalVariables.imagem_path+"HumanoRight1"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoRight2"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoRight3"+GlobalVariables.imagem_formato),

		}
	};
	
	private final static long[][] aacsTempo=new long[][] {//ANDANDO
				new long[] {
						0,
						mediandando*3,
						mediandando*3
				},
				new long[] {
						0,
						mediandando,
						mediandando
				},
				new long[] {
						0,
						mediandando*3/5,
						mediandando*3/5
				},
				new long[] {
						0,
						(long) (mediandando/2.4f),
						(long) (mediandando/2.4f)
				}
	};
	//Agachado, andando, correndo, sprint
	
	private final TexturaAnimador[][] animadosaacsDirec=new TexturaAnimador[][] {
		new TexturaAnimador[] {//AGACHADO
				new TexturaAnimador(aacsTempo[0],udlrTextura[0],this.getTextura()),//UP
				new TexturaAnimador(aacsTempo[0],udlrTextura[1],this.getTextura()),//DOWN
				new TexturaAnimador(aacsTempo[0],udlrTextura[2],this.getTextura()),//LEFT
				new TexturaAnimador(aacsTempo[0],udlrTextura[3],this.getTextura())//RIGHT
		},
		new TexturaAnimador[] {//ANDANDO
				new TexturaAnimador(aacsTempo[1],udlrTextura[0],this.getTextura()),//UP
				new TexturaAnimador(aacsTempo[1],udlrTextura[1],this.getTextura()),//DOWN
				new TexturaAnimador(aacsTempo[1],udlrTextura[2],this.getTextura()),//LEFT
				new TexturaAnimador(aacsTempo[1],udlrTextura[3],this.getTextura())//RIGHT
		},
		new TexturaAnimador[] {//CORRENDO
				new TexturaAnimador(aacsTempo[2],udlrTextura[0],this.getTextura()),//UP
				new TexturaAnimador(aacsTempo[2],udlrTextura[1],this.getTextura()),//DOWN
				new TexturaAnimador(aacsTempo[2],udlrTextura[2],this.getTextura()),//LEFT
				new TexturaAnimador(aacsTempo[2],udlrTextura[3],this.getTextura())//RIGHT
		},
		new TexturaAnimador[] {//SPRINT
				new TexturaAnimador(aacsTempo[3],udlrTextura[0],this.getTextura()),//UP
				new TexturaAnimador(aacsTempo[3],udlrTextura[1],this.getTextura()),//DOWN
				new TexturaAnimador(aacsTempo[3],udlrTextura[2],this.getTextura()),//LEFT
				new TexturaAnimador(aacsTempo[3],udlrTextura[3],this.getTextura())//RIGHT
		}
	};
		
	public enum modos{
		CORRENDO,
		ANDANDO,
		AGACHADO,
		SPRINT
	}

	public Humano() {
		super(new Textura(GlobalVariables.imagem_path+"HumanoUp1"+GlobalVariables.imagem_formato),Textura.modeloPadrao);
		
		milisImpulso=2000;//2000
		movModo=modos.ANDANDO;
		sprintModifier=2.4f;//TODO:Seria 1.2 (quando não tiver com a fome completa!)
		GeradorEventos.addTempoEvento(EventosPadrao.impulsoEventoChave(this), EventosPadrao.impulsoEvento(milisImpulso, this));
	}
	
	public void modo_agachar() {
		setMovModo(Humano.modos.AGACHADO);
	}
	
	public void modo_correr() {
		setMovModo(Humano.modos.CORRENDO);
	}
	
	public void modo_andar() {
		setMovModo(Humano.modos.ANDANDO);
	}

	
	public modos getMovModo() {
		return movModo;
	}
	
	public void pararAnimacoes() {
		for(TexturaAnimador[] an:animadosaacsDirec)
			for(TexturaAnimador a:an)
				a.desativar();
	}
	@Override
	public void kill() {
		super.kill();
		GeradorEventos.remEvento(EventosPadrao.impulsoEventoChave(this));
		for(TexturaAnimador[] tAnArr:animadosaacsDirec)
			for(TexturaAnimador tAn:tAnArr)
			tAn.kill();
	}
	@Override
	public float getVelocModified() {
		return super.getVelocModified()*getModVelocModifier();
	}
	@Override
	public boolean iniciarMovimento() {
		if(super.iniciarMovimento()) {
			updateAnimacao();
			return true;
		}
		return false;
	}
	@Override
	public boolean pararMovimento() {
		if(super.pararMovimento()) {
			((TempoEvento<?>)GeradorEventos.getEvento(EventosPadrao.impulsoEventoChave(this))).parar();
			animado.desativar();

			if(this.equals(Player.mainPlayer.ent))PlayerRegras.resetMovModo(this);
			
			return true;
		}
		return false;
	}
	@Override
	public boolean setAnguloMovimento(double angulo) {
		if(super.setAnguloMovimento(angulo)) {
			if (this.movModo == modos.CORRENDO)
				((TempoEvento<?>) GeradorEventos.getEvento(EventosPadrao.impulsoEventoChave(this))).resetar();
			if(this.movModo==modos.SPRINT) {
				if(this.equals(Player.mainPlayer.ent))
					PlayerRegras.resetMovModo(this);
				else
					this.modo_correr();
			}
			return true;
		}
		return false;
	}
	@Override
	public void setOlhar(double olhar) {
		super.setOlhar(olhar);
		updateAnimacao();
	}
	
	
	private float getModVelocModifier() {
		switch(movModo) {
		case CORRENDO:
			return 1f;
		case AGACHADO:
			return 0.2f;
		case SPRINT:
			return sprintModifier;
		case ANDANDO:
			return 0.6f;
		default:
			System.out.println("Acontecimento impossível! Fechando programa pra evitar erros maiores..");
			System.exit(0);
			return 0f;
		
		}
	}
	private void setMovModo(modos modo) {
		boolean mesmomodo=movModo==modo;
		if (modo == modos.CORRENDO)
			((TempoEvento<?>)GeradorEventos.getEvento(EventosPadrao.impulsoEventoChave(this))).resetar();
		else if (modo != modos.SPRINT) {
			((TempoEvento<?>)GeradorEventos.getEvento(EventosPadrao.impulsoEventoChave(this))).parar();
		}
		movModo = modo;
		
		if(!isParado()&&!mesmomodo) 
			updateAnimacao();
	}
	private void updateAnimacao() {
		if(animado!=null)if(animado.isAtivado())pararAnimacoes();
		switch(movModo) {
		case AGACHADO:
			switch(this.getOlhar()) {
			case CIMA:
				animado=animadosaacsDirec[0][0];
				animado.ativar();
				break;
			case BAIXO:
				animado=animadosaacsDirec[0][1];
				animado.ativar();
				break;
			case ESQUERDA:
				animado=animadosaacsDirec[0][2];
				animado.ativar();
				break;
			case DIREITA:
				animado=animadosaacsDirec[0][3];
				animado.ativar();
			}
			break;
		case ANDANDO:
			switch(this.getOlhar()) {
			case CIMA:
				animado=animadosaacsDirec[1][0];
				animado.ativar();
				break;
			case BAIXO:
				animado=animadosaacsDirec[1][1];
				animado.ativar();
				break;
			case ESQUERDA:
				animado=animadosaacsDirec[1][2];
				animado.ativar();
				break;
			case DIREITA:
				animado=animadosaacsDirec[1][3];
				animado.ativar();
				break;
			}
			break;
		case CORRENDO:
			switch(this.getOlhar()) {
			case CIMA:
				animado=animadosaacsDirec[2][0];
				animado.ativar();
				break;
			case BAIXO:
				animado=animadosaacsDirec[2][1];
				animado.ativar();
				break;
			case ESQUERDA:
				animado=animadosaacsDirec[2][2];
				animado.ativar();
				break;
			case DIREITA:
				animado=animadosaacsDirec[2][3];
				animado.ativar();
				break;
			}
			break;
		case SPRINT:
			switch(this.getOlhar()) {
			case CIMA:
				animado=animadosaacsDirec[3][0];
				animado.ativar();
				break;
			case BAIXO:
				animado=animadosaacsDirec[3][1];
				animado.ativar();
				break;
			case ESQUERDA:
				animado=animadosaacsDirec[3][2];
				animado.ativar();
				break;
			case DIREITA:
				animado=animadosaacsDirec[3][3];
				animado.ativar();
			}
		}
	}
	@Override
	protected int getVelocPadrao() {
		return 5;
	}
	@Override
	protected int getFantasmabilidadePadrao() {
		return 10;
	}

	@Override
	public void impulsionar() {
		setMovModo(modos.SPRINT);
		
	}


}
