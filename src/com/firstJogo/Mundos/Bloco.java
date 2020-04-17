package com.firstJogo.Mundos;

import java.util.HashMap;

import com.firstJogo.visual.TipodeBloco;

public class Bloco {
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
