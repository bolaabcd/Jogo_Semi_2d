package com.firstJogo.Mundos;

import java.util.HashMap;

import org.joml.Vector2f;

import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.TipodeBloco;

public class Bloco {
	
	public static Vector2f toCentroBloco(long x,long y) {
		return new Vector2f(x*GlobalVariables.intperbloco+GlobalVariables.intperbloco/2,y*GlobalVariables.intperbloco+GlobalVariables.intperbloco/2);
	}
	
	private char idTipo;
	protected HashMap<String,String> dadosAdicionais;
	
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
