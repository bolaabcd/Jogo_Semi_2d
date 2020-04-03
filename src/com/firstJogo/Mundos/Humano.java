package com.firstJogo.Mundos;

import com.firstJogo.padrao.PlayerRegras;
import com.firstJogo.regras.DirecoesPadrao;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.Textura;
import com.firstJogo.visual.TexturaAnimador;
//import com.firstJogo.visual.TipodeCriatura;

public class Humano extends Entidade{
	private modos movModo;
	private int milisImpulso;
	private float sprintModifier;
	
	private final long mediandando=450000000;
	private final Textura[][] udlrTextura=new Textura[][] {
		new Textura[] {
				new Textura(GlobalVariables.imagem_path+"HumanoUp3"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoUp2"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoUp1"+GlobalVariables.imagem_formato),
		},
		new Textura[] {
				new Textura(GlobalVariables.imagem_path+"HumanoDown3"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoDown2"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoDown1"+GlobalVariables.imagem_formato),
		},
		new Textura[] {
				new Textura(GlobalVariables.imagem_path+"HumanoLeft3"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoLeft2"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoLeft1"+GlobalVariables.imagem_formato),
		},
		new Textura[] {
				new Textura(GlobalVariables.imagem_path+"HumanoRight3"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoRight2"+GlobalVariables.imagem_formato),
				new Textura(GlobalVariables.imagem_path+"HumanoRight1"+GlobalVariables.imagem_formato),

		}
	};
	
	private final long[][] aacsTempo=new long[][] {
				new long[] {
						0,
						mediandando*7/5,
						mediandando*7/5
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
						mediandando/2,
						mediandando/2
				}
	};
	//Agachado, andando, correndo, sprint
	
	private TexturaAnimador animado;
//	private final TexturaAnimador movendo=new TexturaAnimador(
//			new long[] {aacsTempo[1],aacsTempo[1],aacsTempo[1]},
//			new Textura[] {
//					udlrTextura[0][0],
//					udlrTextura[0][1],
//					udlrTextura[0][2]
//					},
//			this.getTextura()
//			);
	
	private TempoMarker impulso;//Pode alterar os milisImpulso, então pode alterar o TempoMarker!
	
	public enum modos{
		CORRENDO,
		ANDANDO,
		AGACHADO,
		SPRINT
	}
	public Humano() {
		super(new Textura(GlobalVariables.imagem_path+"HumanoUp1"+GlobalVariables.imagem_formato));
//		this.velocModifier=0.6f;
		this.veloc=5;
		milisImpulso=2000;
		movModo=modos.ANDANDO;
		sprintModifier=2.4f;//Seria 1.2 (quando tiver a animação!)
		impulso=new TempoMarker(milisImpulso*1000000,(pessoa)->{
			((Humano) pessoa).setMovModo(modos.SPRINT);
		},this);
//		animado=new TexturaAnimador(aacsTempo[3],udlrTextura[0],this.getTextura());
//		animado.ativar();
		
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
	
//	public void resetMovModo() {
//		if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_CONTROL))setMovModo(modos.CORRENDO);//Prioridade é correr, afinal ele tava sprintando.
//		else if(GlobalVariables.Keys.contains(GLFW.GLFW_KEY_LEFT_SHIFT)) setMovModo(modos.AGACHADO);
//		else setMovModo(Humano.modos.ANDANDO);
//	}
	
	public modos getMovModo() {
		return movModo;
	}
	@Override
	public float getVelocModifier() {
		return velocModifier*getModVelocModifier();
	}
	@Override
	public void pararMovimento() {
		super.pararMovimento();
		impulso.desativar();
		
//		andando.desativar();
		if(this.isPlayer())PlayerRegras.resetMovModo(this);;
	}
	@Override
	public void setAngulo(double angulo) {
		if(this.getAngulo()!=angulo) {
			impulso.resetTemporegistrado();
			if(this.isPlayer())PlayerRegras.resetMovModo(this);;
		}
		super.setAngulo(angulo);
	}
	@Override
	public void setOlhar(DirecoesPadrao olhar) {
		super.setOlhar(olhar);
		updateAnimacao();
//		if(olhar!=this.getOlhar()||this.getVelocModifier()==0)updateAnimacao();
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
		if(movModo!=modo&&modo==modos.CORRENDO)impulso.ativar();
		else if(movModo!=modo) {
			impulso.desativar();
			}
		movModo=modo;
//		if(modo.equals(modos.CORRENDO))this.velocModifier=1f;
//		else if(modo.equals(modos.ANDANDO))this.velocModifier=0.6f;
//		else if(modo.equals(modos.AGACHADO))this.velocModifier=0.2f;
//		else if(modo.equals(modos.SPRINT))this.velocModifier=1.2f;
	}
	private void updateAnimacao() {
		if(animado!=null)animado.desativar();
//		System.out.println(movModo);
//		System.out.println(getOlhar());
		switch(movModo) {
		case AGACHADO:
			switch(this.getOlhar()) {
			case CIMA:
				animado=new TexturaAnimador(aacsTempo[0],udlrTextura[0],this.getTextura());
				animado.ativar();
				break;
			case BAIXO:
				animado=new TexturaAnimador(aacsTempo[0],udlrTextura[1],this.getTextura());
				animado.ativar();
				break;
			case ESQUERDA:
				animado=new TexturaAnimador(aacsTempo[0],udlrTextura[2],this.getTextura());
				animado.ativar();
				break;
			case DIREITA:
				animado=new TexturaAnimador(aacsTempo[0],udlrTextura[3],this.getTextura());
				animado.ativar();
			}
			break;
		case ANDANDO:
			switch(this.getOlhar()) {
			case CIMA:
				animado=new TexturaAnimador(aacsTempo[1],udlrTextura[0],this.getTextura());
//				System.out.println(udlrTextura[0][0].getId());
				animado.ativar();
				break;
			case BAIXO:
				animado=new TexturaAnimador(aacsTempo[1],udlrTextura[1],this.getTextura());
				animado.ativar();
				break;
			case ESQUERDA:
				animado=new TexturaAnimador(aacsTempo[1],udlrTextura[2],this.getTextura());
				animado.ativar();
				break;
			case DIREITA:
				animado=new TexturaAnimador(aacsTempo[1],udlrTextura[3],this.getTextura());
				animado.ativar();
				break;
			}
			break;
		case CORRENDO:
			switch(this.getOlhar()) {
			case CIMA:
				animado=new TexturaAnimador(aacsTempo[2],udlrTextura[0],this.getTextura());
				animado.ativar();
				break;
			case BAIXO:
				animado=new TexturaAnimador(aacsTempo[2],udlrTextura[1],this.getTextura());
				animado.ativar();
				break;
			case ESQUERDA:
				animado=new TexturaAnimador(aacsTempo[2],udlrTextura[2],this.getTextura());
				animado.ativar();
				break;
			case DIREITA:
				animado=new TexturaAnimador(aacsTempo[2],udlrTextura[3],this.getTextura());
				animado.ativar();
				break;
			}
			break;
		case SPRINT:
			switch(this.getOlhar()) {
			case CIMA:
				animado=new TexturaAnimador(aacsTempo[3],udlrTextura[0],this.getTextura());
				animado.ativar();
				break;
			case BAIXO:
				animado=new TexturaAnimador(aacsTempo[3],udlrTextura[1],this.getTextura());
				animado.ativar();
				break;
			case ESQUERDA:
				animado=new TexturaAnimador(aacsTempo[3],udlrTextura[2],this.getTextura());
				animado.ativar();
				break;
			case DIREITA:
				animado=new TexturaAnimador(aacsTempo[3],udlrTextura[3],this.getTextura());
				animado.ativar();
			}
		}
	}

}
