package com.firstJogo.visual;

import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.TempoMarker;

public class TexturaAnimador {
	private TempoMarker[] marcadores;//Marca o tempo DEPOIS de a textura ser colocada!
	private Textura textura_referencial;
	private int[] texturas_alternativas;
	private int texatual;
	
	private final Funcao<Object> funcao=((objeto)->{
		
		TexturaAnimador t=(TexturaAnimador) objeto;
		
//		t.textura_referencial=t.texturas_alternativas[t.texatual];
		t.textura_referencial.setId(t.texturas_alternativas[t.texatual]);
		t.marcadores[t.texatual].desativar();
//		System.out.println("Referencial: "+t.textura_referencial);
		System.out.println("Atual: "+t.texturas_alternativas[t.texatual]);
		t.texatual+=1;
		if(t.texatual==t.texturas_alternativas.length)t.texatual=0;
		t.marcadores[t.texatual].ativar();
		
	});
	
	public TexturaAnimador(long[] intervalos,Textura[] texturas_alternativas, Textura textura_referencial) {
		if(intervalos.length!=texturas_alternativas.length)throw new IllegalStateException("É preciso ter o mesmo número de intervalos de tempo e de texturas!");
		this.textura_referencial=textura_referencial;
		this.texatual=0;
		marcadores=new TempoMarker[intervalos.length];
		this.texturas_alternativas=new int[texturas_alternativas.length];
		for(int i=0;i<intervalos.length;i++) {
			this.texturas_alternativas[i]=texturas_alternativas[i].getId();
			marcadores[i]=new TempoMarker(intervalos[i],funcao,this);
		}
	}
	public void ativar() {
		marcadores[0].ativar();
	}
	public void desativar() {
		for(TempoMarker marc:marcadores)
			marc.desativar();
	}

}
