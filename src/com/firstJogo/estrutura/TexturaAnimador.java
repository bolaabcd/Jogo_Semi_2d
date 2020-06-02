package com.firstJogo.estrutura;

import java.util.function.Consumer;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.Textura;

public class TexturaAnimador {
	public TempoMarker[] marcadores;//Marca o tempo DEPOIS de a textura ser colocada!
	private Textura textura_referencial;
	private int[] texturas_alternativas;
	private int texatual;
	
	private static final Consumer<TexturaAnimador> funcao=((animador)->{
		animador.marcadores[animador.texatual].desativar();
		animador.texatual+=1;
 		if(animador.texatual==animador.texturas_alternativas.length)animador.texatual=0;
		animador.textura_referencial.setId(animador.texturas_alternativas[animador.texatual]);
		animador.marcadores[animador.texatual].ativar();
	});
	
	public TexturaAnimador(long[] intervalos,Textura[] texturas_alternativas, Textura textura_referencial) {
		if(intervalos.length!=texturas_alternativas.length)throw new IllegalStateException("É preciso ter o mesmo número de intervalos de tempo e de texturas!");
		this.textura_referencial=textura_referencial;
		this.texatual=0;
		marcadores=new TempoMarker[intervalos.length];
		this.texturas_alternativas=new int[texturas_alternativas.length];
		
		for(int i=0;i<intervalos.length;i++) {
			this.texturas_alternativas[i]=texturas_alternativas[i].getId();
			marcadores[i]=new TempoMarker(intervalos[i]);
		}
		for(TempoMarker marcador: marcadores)
			GeradorEventos.addTempoEvento(TexturaAnimador.class, new FuncaoHandler<TexturaAnimador>(funcao,this),marcador);
		desativar();
	}
	public void ativar() {
		marcadores[0].ativar();
	}
	public void desativar() {
		for(TempoMarker marc:marcadores) 
			marc.desativar();
		
		this.textura_referencial.setId(this.texturas_alternativas[0]);
	}
	public boolean isAtivado() {
		for(TempoMarker marc:marcadores)
			if(GeradorEventos.isOn(marc))return true;
		return false;
		
	}

}
