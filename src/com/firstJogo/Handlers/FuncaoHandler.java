package com.firstJogo.Handlers;

import java.util.function.Consumer;

public class FuncaoHandler<V> {
	private final Consumer<V> funcao;
	private V argumento;

	public FuncaoHandler(Consumer<V> func,V arg) {
		this.funcao=func;
		this.argumento=arg;
	}
	public void setArgumento(V argumento) {
		this.argumento=argumento;
	}
	public void run() {
		funcao.accept(argumento);
	}
	public void run(V argumento) {
		funcao.accept(argumento);
	}
}
