package com.firstJogo.estrutura;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.lwjgl.glfw.GLFW;

import com.firstJogo.main.GeradorEventos;

public class KeyHandler {
	private static CopyOnWriteArraySet<Integer> keys = new CopyOnWriteArraySet<Integer>();//Chaves atualmente pressionadas

	private static HashMap<Integer,Integer> opostos=new HashMap<Integer,Integer>();
	
	public static void inicializar() {
		opostos.put(GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_S);
		opostos.put(GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_W);
		opostos.put(GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_D);
		opostos.put(GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_A);
		opostos.put(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_LEFT_SUPER);
		opostos.put(GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_LEFT_CONTROL);
	}
	
	public static void remove(int chave, boolean isSintetico) {
		if(!keys.contains(chave))return;
		
		keys.remove(chave);
		if(GeradorEventos.botaoremovido.get(chave)!=null)
			GeradorEventos.botaoremovido.get(chave).run(isSintetico);
		
		if(!isSintetico)
			if(opostos.get(chave)!=null)
				if(keys.contains(opostos.get(chave)))
					GeradorEventos.botaopressionado.get(opostos.get(chave)).run(true);
		
		
	}
	
	public static void add(int chave,boolean isSintetico) {
		if(keys.contains(chave))return;
		
		if(!isSintetico)
			if(opostos.get(chave)!=null)
				if(keys.contains(opostos.get(chave)))
					GeradorEventos.botaoremovido.get(opostos.get(chave)).run(true);
		
		keys.add(chave);
		if(GeradorEventos.botaopressionado.get(chave)!=null)
			GeradorEventos.botaopressionado.get(chave).run(isSintetico);

	}
	public static void ativarEvento(boolean isSintetico,int chave) {
		if(GeradorEventos.botaopressionado.get(chave)!=null)
			GeradorEventos.botaopressionado.get(chave).run(isSintetico);
	}
	public static boolean containsKey(int chave) {
		return keys.contains(chave);
	}
	
	public static void ativarMantidos() {
		for(int k:keys)
			if(GeradorEventos.botaomantido.containsKey(k))
				GeradorEventos.botaomantido.get(k).run(null);
	}
}
