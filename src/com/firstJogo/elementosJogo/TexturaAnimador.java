package com.firstJogo.elementosJogo;

import java.util.function.Consumer;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.Util.TempoMarker;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.visual.Textura;

public class TexturaAnimador {
	public TempoMarker[] marcadores;// Marca o tempo DEPOIS de a textura ser colocada!
	private Textura textura_referencial;
	private int[] texturas_alternativas;
	private int texatual;

	private static final Consumer<TexturaAnimador> funcao = ((animador) -> {
		animador.marcadores[animador.texatual].ignoreMarker();
		animador.texatual += 1;
		if (animador.texatual == animador.texturas_alternativas.length)
			animador.texatual = 0;
		animador.textura_referencial.setId(animador.texturas_alternativas[animador.texatual]);
		animador.marcadores[animador.texatual].resetar();
	});

	public TexturaAnimador(long[] intervalos, Textura[] texturas_alternativas, Textura textura_referencial) {
		if (intervalos.length != texturas_alternativas.length)
			throw new IllegalStateException("É preciso ter o mesmo número de intervalos de tempo e de texturas!");
		this.textura_referencial = textura_referencial;
		this.texatual = 0;
		marcadores = new TempoMarker[intervalos.length];
		this.texturas_alternativas = new int[texturas_alternativas.length];

		for (int i = 0; i < intervalos.length; i++) {
			this.texturas_alternativas[i] = texturas_alternativas[i].getId();
			marcadores[i] = new TempoMarker(intervalos[i]);
		}
		for (int i = 0; i < marcadores.length; i++) {
			TempoEvento<TexturaAnimador> ev=new TempoEvento<TexturaAnimador>(marcadores[i], new FuncaoHandler<TexturaAnimador>(funcao, this));
			GeradorEventos.addTempoEvento(marcadores[i],ev);
		}
	}

	public void kill() {
		for(TempoMarker chave:marcadores) {
			GeradorEventos.remEvento(chave);
		}
	}
	
	public void ativar() {
		marcadores[0].resetar();
	}

	public void desativar() {
		for (TempoMarker marc : marcadores)
			marc.ignoreMarker();

		this.textura_referencial.setId(this.texturas_alternativas[0]);
	}

	public boolean isAtivado() {
		for (TempoMarker marc : marcadores)
			if (!marc.isIgnored())
				return true;
		return false;

	}

}
