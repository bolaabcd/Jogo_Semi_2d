package com.firstJogo.Mundos;

import com.firstJogo.visual.TipodeBloco;

public class Bloco {
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
