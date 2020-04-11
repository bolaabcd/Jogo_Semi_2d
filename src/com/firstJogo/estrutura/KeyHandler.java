package com.firstJogo.estrutura;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.lwjgl.glfw.GLFW;

import com.firstJogo.main.GeradorEventos;
import com.firstJogo.utils.Funcao;

//Atua como filtro para lidar com botões opostos pressionados ao mesmo tempo da seguinte forma:
//1) Se um botão é solto e seu oposto está pressionado, considera como se o oposto tivesse sido apertado 
//2) Se um botão é pressionado quando seu oposto já está pressionado, considera como se o oposto tivesse sido solto 

//Motivo para uso desse método:
//Se dois botões opostos estão pressionados, a prioridade de funcionamento será do último botão pressionado.
//Se há um botão está pressionado e seu oposto não, ele DEVE cumprir seu papel.
//Este jogo utiliza eventos de "pressionar" e "soltar" botões para alterações de estado 
//Este jogo utiliza apenas mudanças de estado para alterar qualquer coisa no mundo.
public class KeyHandler {
	private static final HashMap<Integer,Funcao<Boolean>> botaoremovido=new HashMap<Integer,Funcao<Boolean>>();
	private static final HashMap<Integer,Funcao<Boolean>> botaopressionado=new HashMap<Integer,Funcao<Boolean>>();
	
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
	public static void addBotaoCallbacks(HashMap<Integer,Funcao<Boolean>> botaoremovidonovo,HashMap<Integer,Funcao<Boolean>> botaopressionadonovo) {
		botaoremovido.putAll(botaoremovidonovo);
		botaopressionado.putAll(botaopressionadonovo);
	}
	
	//Evento EXTERNO de remoção de botão
	public static void botaoRemovido(int botao, boolean isSintetico) {
		if(!keys.contains(botao))return;
		
		keys.remove(botao);
		if(botaoremovido.get(botao)!=null)
			botaoremovido.get(botao).run(isSintetico);
		
		if(!isSintetico)
			if(opostos.get(botao)!=null)
				if(keys.contains(opostos.get(botao)))
					botaopressionado.get(opostos.get(botao)).run(true);
		
		
	}
	//Evento EXTERNO de pressionamento de botão
	public static void botaoPressionado(int botao,boolean isSintetico) {
		if(keys.contains(botao))return;
		
		if(!isSintetico)
			if(opostos.get(botao)!=null)
				if(keys.contains(opostos.get(botao)))
					botaoremovido.get(opostos.get(botao)).run(true);
		
		keys.add(botao);
		if(botaopressionado.get(botao)!=null)
			botaopressionado.get(botao).run(isSintetico);

	}
	public static void ativarEvento(boolean isSintetico,int chave) {
		if(botaopressionado.get(chave)!=null)
			botaopressionado.get(chave).run(isSintetico);
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
