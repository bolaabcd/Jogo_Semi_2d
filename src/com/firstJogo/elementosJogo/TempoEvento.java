package com.firstJogo.elementosJogo;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.Util.TempoMarker;

public class TempoEvento<T> extends Evento<T>{
	private final TempoMarker marc;
	public TempoEvento(TempoMarker marcador,FuncaoHandler<T> callback) {
		super(callback);
		this.marc = marcador;
	}
	@Override
	public boolean podeExecutar() {
		return marc.passouTempolimite();
	}
	public void resetar() {
		marc.resetar();
	}
	public void parar() {
		marc.ignoreMarker();
	}
}
