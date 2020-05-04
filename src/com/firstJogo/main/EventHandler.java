package com.firstJogo.main;

import java.util.HashMap;

import com.firstJogo.utils.FuncaoHandler;

public class EventHandler<K, V> {
	private final HashMap<K, FuncaoHandler<V>> funcoes=new HashMap<K, FuncaoHandler<V>>();
	
	public void addEvento(K chave, FuncaoHandler<V> funcao) {
		funcoes.put(chave, funcao);
	}
	public void remEvento(K chave) {
		funcoes.remove(chave);
	}
	public FuncaoHandler<V> getEvento(K chave) {
		return funcoes.get(chave);
	}
//	public void throwEvento(K chave, V argumento) {
//		if(funcoes.get(chave)!=null)
//			funcoes.get(chave).run(argumento);
//	}
	public void throwEvento(K chave) {
//		System.out.println(funcoes.size());
//		if(funcoes.get(chave)!=null)
			funcoes.get(chave).run();
	}
	
	public int getsize() {
		return funcoes.size();
	}
}
