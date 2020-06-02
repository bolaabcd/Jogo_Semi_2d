package com.firstJogo.Handlers;

import java.util.HashMap;

import com.firstJogo.estrutura.NotFoundException;

public class EventHandler<K, V> {
	private final HashMap<K, FuncaoHandler<V>> funcoes=new HashMap<K, FuncaoHandler<V>>();
	
	public void addEvento(K chave, FuncaoHandler<V> funcao) {
		funcoes.put(chave, funcao);
	}
	public void remEvento(K chave) {
		funcoes.remove(chave);
	}
	public void runEvento(K chave,V valor) throws NotFoundException {
		if(funcoes.get(chave)==null) 
			throw new NotFoundException("Evento não encontrado!");
		funcoes.get(chave).run(valor);
	}

	public void throwEvento(K chave) throws NotFoundException {
		if(funcoes.get(chave)==null) 
			throw new NotFoundException("Evento não encontrado!");
		funcoes.get(chave).run();
	}
	
	public int getsize() {
		return funcoes.size();
	}
}
