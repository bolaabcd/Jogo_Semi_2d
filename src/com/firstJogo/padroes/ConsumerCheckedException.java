package com.firstJogo.padroes;

import java.io.IOException;

@FunctionalInterface
public interface ConsumerCheckedException<T>{
	public void accept(T t) throws IOException;
}