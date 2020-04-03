package com.firstJogo.utils;

public class Generalidades {
	public static double arredondar(double val, int casas_decimais) {
		return (double) Math.round(val*Math.pow(val, casas_decimais))/Math.pow(val, casas_decimais);
	}
	public static float arredondar(float val, int casas_decimais) {
		return (float) (Math.round(val*Math.pow(val, casas_decimais))/Math.pow(val, casas_decimais));
	}
}
