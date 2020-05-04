package com.firstJogo.estrutura;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.Funcao;
import com.firstJogo.utils.TempoMarker;
import com.firstJogo.visual.Textura;

public class TexturaAnimador {
	public TempoMarker[] marcadores;//Marca o tempo DEPOIS de a textura ser colocada!
	private Textura textura_referencial;
	private int[] texturas_alternativas;
	private int texatual;
	
//	private static final Funcao<Object> funcao=((objeto)->{
	private static final Funcao<TexturaAnimador> funcao=((animador)->{
//		System.out.println(animador.texatual);
		animador.marcadores[animador.texatual].desativar();
		animador.texatual+=1;
 		if(animador.texatual==animador.texturas_alternativas.length)animador.texatual=0;
		animador.textura_referencial.setId(animador.texturas_alternativas[animador.texatual]);
		animador.marcadores[animador.texatual].ativar();
		
	});
	
	public TexturaAnimador(long[] intervalos,Textura[] texturas_alternativas, Textura textura_referencial) {
		if(intervalos.length!=texturas_alternativas.length)throw new IllegalStateException("É preciso ter o mesmo número de intervalos de tempo e de texturas!");
		this.textura_referencial=textura_referencial;
		this.texatual=0;
		marcadores=new TempoMarker[intervalos.length];
		this.texturas_alternativas=new int[texturas_alternativas.length];
		
		for(int i=0;i<intervalos.length;i++) {
			this.texturas_alternativas[i]=texturas_alternativas[i].getId();
//			marcadores[i]=new TempoMarker(intervalos[i],funcao,this);
			marcadores[i]=new TempoMarker(intervalos[i]);
//			GeradorEventos.tempoHandler.put(marcadores[i], new EventHandler<>());
		}
//		if(GeradorEventos.texturaEventHandler.getEvento(marcadores[0])==null)
//			GeradorEventos.texturaEventHandler.addEvento(marcadores[0], new FuncaoHandler<TexturaAnimador>(funcao,this));
		for(TempoMarker marcador: marcadores)
			GeradorEventos.texturaEventHandler.addEvento(marcador, new FuncaoHandler<TexturaAnimador>(funcao,this));
	}
	public void ativar() {
//		System.out.println(marcadores[0]);
		marcadores[0].ativar();
	}
	public void desativar() {
		for(TempoMarker marc:marcadores) 
			marc.desativar();
		
		this.textura_referencial.setId(this.texturas_alternativas[0]);
	}
	public boolean isAtivado() {
		for(TempoMarker marc:marcadores)
			if(GeradorEventos.tempopassado.contains(marc))return true;
		return false;
		
	}

}
