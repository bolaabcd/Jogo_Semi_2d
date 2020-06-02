package com.firstJogo.utils;

import java.io.IOException;

@FunctionalInterface
public interface ConsumerCheckedException<T>{
	public void accept(T t) throws IOException;
}