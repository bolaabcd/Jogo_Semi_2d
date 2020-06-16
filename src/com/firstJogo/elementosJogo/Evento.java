package com.firstJogo.elementosJogo;

import com.firstJogo.Handlers.FuncaoHandler;

public abstract class Evento<T> {
	private final FuncaoHandler<T> callback;
	public abstract boolean podeExecutar();
	
	public Evento(FuncaoHandler<T> callback) {
		this.callback=callback;
	}
	
	public void executar() {
		callback.run();
	}
}
