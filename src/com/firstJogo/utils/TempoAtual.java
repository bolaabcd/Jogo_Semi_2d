package com.firstJogo.utils;

public class TempoAtual {
public static long getsec() {
	return System.nanoTime()/(long)1000000000;
}
}
