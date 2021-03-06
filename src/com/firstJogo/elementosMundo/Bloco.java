package com.firstJogo.elementosMundo;

import org.joml.Vector2f;

import com.firstJogo.padroes.GlobalVariables;

public class Bloco {
	
	public static Vector2f toCentroBloco(long x,long y) {
		return new Vector2f(x*GlobalVariables.intperbloco+GlobalVariables.intperbloco/2,y*GlobalVariables.intperbloco+GlobalVariables.intperbloco/2);
	}
	
	private char idTipo;
	
	public Bloco(char idTipo) {
		this.idTipo=idTipo;
	}
	public Bloco(char idTipo,short tangibilidade) {
		this.idTipo=idTipo;
	}
	
	public TipodeBloco getTipo() {
		return TipodeBloco.azulejos[idTipo];
	}

}
