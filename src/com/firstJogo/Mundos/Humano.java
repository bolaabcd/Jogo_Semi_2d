package com.firstJogo.Mundos;

import com.firstJogo.regras.DirecoesPadrao;
import com.firstJogo.regras.PlayerRegras;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.Textura;
import com.firstJogo.visual.TexturaAnimador;
//import com.firstJogo.visual.TipodeCriatura;

public class Humano extends Entidade{
	private modos movModo;
	private int milisImpulso;
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
	
	private final static long[][] aacsTempo=new long[][] {
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
	
	
	private TempoMarker impulso;//Pode alterar os milisImpulso, então pode alterar o TempoMarker!
	
	public enum modos{
		CORRENDO,
		ANDANDO,
		AGACHADO,
		SPRINT
	}
	public Humano() {
		super(new Textura(GlobalVariables.imagem_path+"HumanoUp1"+GlobalVariables.imagem_formato));
		this.veloc=5;
		milisImpulso=2000;
		movModo=modos.ANDANDO;
		sprintModifier=2.4f;//TODO:Seria 1.2 (quando não tiver com a fome completa!)
		impulso=new TempoMarker(milisImpulso*1000000,(pessoa)->{
			((Humano) pessoa).setMovModo(modos.SPRINT);
		},this);
		
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
	public float getVelocModified() {
		return super.getVelocModified()*getModVelocModifier();
	}
	@Override
	public void iniciarMovimento() {
		super.iniciarMovimento();
		updateAnimacao();
	}
	@Override
	public void pararMovimento() {
		super.pararMovimento();
		impulso.desativar();
		animado.desativar();

		if(this.isPlayer())PlayerRegras.resetMovModo(this);;
	}
	@Override
	public void setAngulo(double angulo) {
		if(this.getAngulo()!=angulo) {
			impulso.resetTemporegistrado();
			if(this.isPlayer()&&this.movModo==modos.SPRINT)PlayerRegras.resetMovModo(this);
		}
		super.setAngulo(angulo);
	}
	@Override
	public void setOlhar(DirecoesPadrao olhar) {
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
		
		if(modo==modos.CORRENDO)impulso.ativar();
		else {
			impulso.desativar();
			}
		movModo=modo;
		
		if(!isParado&&!mesmomodo) 
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

}
