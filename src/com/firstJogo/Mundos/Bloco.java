package com.firstJogo.Mundos;

import com.firstJogo.visual.TipodeBloco;

public class Bloco {
	private char idTipo;
	private short tangibilidade;
	
	public Bloco(char idTipo) {
		this.idTipo=idTipo;
		tangibilidade=TipodeBloco.azulejos[idTipo].getTangibilidade();
	}
	public Bloco(char idTipo,short tangibilidade) {
		this.idTipo=idTipo;
		this.tangibilidade=tangibilidade;
	}
	
	public TipodeBloco getTipo() {
		return TipodeBloco.azulejos[idTipo];
	}
	public short getTangibilidade() {
		return tangibilidade;
	}
}
