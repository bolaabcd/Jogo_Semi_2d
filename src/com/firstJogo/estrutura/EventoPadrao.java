package com.firstJogo.estrutura;

import org.joml.Vector2f;

import com.firstJogo.Handlers.FuncaoHandler;
import com.firstJogo.Mundos.Entidade;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoMarker;

public class EventoPadrao {
	public static final TempoEvento<?> moverEntidadeEvento(long miliSegundosDelay, Entidade ent){
		final class MarkerEntidade{
			final TempoMarker marc;
			final Entidade ent;
			MarkerEntidade(TempoMarker marc,Entidade ent){
				this.marc=marc;
				this.ent=ent;
			}
		}
		TempoMarker mover=new TempoMarker(miliSegundosDelay*1000000L);
		MarkerEntidade tempoEntidade=new MarkerEntidade(mover,ent);
		return new TempoEvento<MarkerEntidade>(mover, new FuncaoHandler<MarkerEntidade>((MarkerEntidade marEnt) -> {
			
		TempoMarker marcador = marEnt.marc;
		Entidade entidade = marEnt.ent;
		
		Vector2f playerpos = Player.mainPlayer.ent.getMundopos();
		Vector2f gentipos = entidade.getMundopos();
		entidade.setAnguloMovimento(Math.atan2(playerpos.y - gentipos.y, playerpos.x - gentipos.x));
		
		if(!entidade.isParado()) {
			float movx = (float) ((entidade.getForcedVelocModified().x
					+ entidade.getMovDirecModifiers()[0] * entidade.getVelocModified()) * GlobalVariables.intperbloco
					* (double) (System.nanoTime() - marcador.getTemporegistrado()) / 1000000000);
			float movy = (float) ((entidade.getForcedVelocModified().y
					+ entidade.getMovDirecModifiers()[1] * entidade.getVelocModified()) * GlobalVariables.intperbloco
					* (double) (System.nanoTime() - marcador.getTemporegistrado()) / 1000000000);

			entidade.setMundopos(new Vector2f(entidade.getMundopos().x + movx, entidade.getMundopos().y));

			entidade.setMundopos(new Vector2f(entidade.getMundopos().x, entidade.getMundopos().y + movy));
		}
		marcador.resetar();
	}, tempoEntidade));
	}
	public static final TempoEvento<Entidade> removerEntidadeEvento(long segundosDelay, Entidade ent){
		return new TempoEvento<Entidade>(new TempoMarker(segundosDelay*1000000000L), new FuncaoHandler<Entidade>((Entidade entidade) -> {
			entidade.kill();
		}, ent));
	}

}
