package com.firstJogo.padroes;

import org.joml.Vector2f;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.Util.ElementoDuplo;
import com.firstJogo.Util.TempoMarker;
import com.firstJogo.elementosJogo.Player;
import com.firstJogo.elementosJogo.TempoEvento;
import com.firstJogo.elementosMundo.Entidade;
import com.firstJogo.elementosMundo.Impulsionavel;

public class EventosPadrao {

	private static final String Impulso = "Impulso";
	
	public static final ElementoDuplo<Impulsionavel,String> impulsoEventoChave(Impulsionavel imp){
		return new ElementoDuplo<Impulsionavel,String>(imp,EventosPadrao.Impulso,false);
	}
	
	public static final TempoEvento<Impulsionavel> impulsoEvento(long miliSegundosDelay,Impulsionavel imp){
		TempoMarker impulso=new TempoMarker(miliSegundosDelay*1000000);
		return new TempoEvento<Impulsionavel>(impulso, new FuncaoHandler<Impulsionavel>((Impulsionavel impuls)->{
			impuls.impulsionar();
		},imp));
	}
	
	
	
	private static final String SeguirPlayer = "SeguirPlayer";

	public static final ElementoDuplo<Entidade, String> seguirPlayerEventoChave(Entidade ent) {
		return new ElementoDuplo<Entidade, String>(ent, EventosPadrao.SeguirPlayer, false);
	}
	
	public static final TempoEvento<?> seguirPlayerEvento(long miliSegundosDelay, Entidade ent) {
		final class MarkerEntidade {
			final TempoMarker marc;
			final Entidade ent;

			MarkerEntidade(TempoMarker marc, Entidade ent) {
				this.marc = marc;
				this.ent = ent;
			}
		}
		TempoMarker mover = new TempoMarker(miliSegundosDelay * 1000000L);
		MarkerEntidade tempoEntidade = new MarkerEntidade(mover, ent);
		return new TempoEvento<MarkerEntidade>(mover, new FuncaoHandler<MarkerEntidade>((MarkerEntidade marEnt) -> {

			TempoMarker marcador = marEnt.marc;
			Entidade entidade = marEnt.ent;

			Vector2f playerpos = Player.mainPlayer.ent.getMundopos();
			Vector2f gentipos = entidade.getMundopos();
			entidade.setAnguloMovimento(Math.atan2(playerpos.y - gentipos.y, playerpos.x - gentipos.x));

			if (!entidade.isParado()) {
				float movx = (float) ((entidade.getForcedVelocModified().x
						+ entidade.getMovDirecModifiers()[0] * entidade.getVelocModified())
						* GlobalVariables.intperbloco * (double) (System.nanoTime() - marcador.getTemporegistrado())
						/ 1000000000);
				float movy = (float) ((entidade.getForcedVelocModified().y
						+ entidade.getMovDirecModifiers()[1] * entidade.getVelocModified())
						* GlobalVariables.intperbloco * (double) (System.nanoTime() - marcador.getTemporegistrado())
						/ 1000000000);
				if(Math.floor(movx*10)>1)
				System.out.println(movx);

				entidade.setMundoPos(new Vector2f(entidade.getMundopos().x + movx, entidade.getMundopos().y));

				entidade.setMundoPos(new Vector2f(entidade.getMundopos().x, entidade.getMundopos().y + movy));
			}
			marcador.resetar();
		}, tempoEntidade));
	}

	private static final String RemoverEntidade = "RemoverEntidade";

	public static final ElementoDuplo<Entidade, String> removerEntidadeEventoChave(Entidade ent) {
		return new ElementoDuplo<Entidade, String>(ent, EventosPadrao.RemoverEntidade, false);
	}
	
	public static final TempoEvento<Entidade> removerEntidadeEvento(long segundosDelay, Entidade ent) {
		return new TempoEvento<Entidade>(new TempoMarker(segundosDelay * 1000000000L),
				new FuncaoHandler<Entidade>((Entidade entidade) -> {
					entidade.kill();
				}, ent));
	}

}
