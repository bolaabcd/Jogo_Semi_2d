package com.firstJogo.Handlers;

import java.util.HashMap;

import com.firstJogo.elementosJogo.NotFoundException;

public class CallbackHandler<K, V> {
	private final HashMap<K, FuncaoHandler<V>> funcoes=new HashMap<K, FuncaoHandler<V>>();
	
	public void addCallback(K chave, FuncaoHandler<V> funcao) {
		funcoes.put(chave, funcao);
	}
	public void remCallback(K chave) {
		funcoes.remove(chave);
	}
	public void runCallback(K chave,V valor) throws NotFoundException {
		if(funcoes.get(chave)==null) 
			throw new NotFoundException("Evento não encontrado!");
		funcoes.get(chave).run(valor);
	}

	public void throwCallback(K chave) throws NotFoundException {
		if(funcoes.get(chave)==null) 
			throw new NotFoundException("Evento não encontrado!");
		funcoes.get(chave).run();
	}
	
	public int getsize() {
		return funcoes.size();
	}
}
