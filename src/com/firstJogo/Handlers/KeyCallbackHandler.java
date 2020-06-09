package com.firstJogo.Handlers;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.lwjgl.glfw.GLFW;

import com.firstJogo.estrutura.NotFoundException;

//Atua como filtro para lidar com botões opostos pressionados ao mesmo tempo da seguinte forma:
//1) Se um botão é solto e seu oposto está pressionado, considera como se o oposto tivesse sido apertado 
//2) Se um botão é pressionado quando seu oposto já está pressionado, considera como se o oposto tivesse sido solto 

//Motivo para uso desse método:
//Se dois botões opostos estão pressionados, a prioridade de funcionamento será do último botão pressionado.
//Se há um botão está pressionado e seu oposto não, ele DEVE cumprir seu papel.
//Este jogo utiliza eventos de "pressionar" e "soltar" botões para alterações de estado 
//Este jogo utiliza apenas mudanças de estado para alterar qualquer coisa no mundo.
public class KeyCallbackHandler {
	private static final CallbackHandler<Integer,Boolean> botaoRemovidoHandler=new CallbackHandler<Integer,Boolean>();
	private static final CallbackHandler<Integer,Boolean> botaoPressionadoHandler=new CallbackHandler<Integer,Boolean>();

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
	public static void addBotaoCallbacks(HashMap<Integer,FuncaoHandler<Boolean>> botaoremovidonovo,HashMap<Integer,FuncaoHandler<Boolean>> botaopressionadonovo) {
		for(Integer i:botaoremovidonovo.keySet())
			botaoRemovidoHandler.addCallback(i, botaoremovidonovo.get(i));
		for(Integer i:botaopressionadonovo.keySet())
			botaoPressionadoHandler.addCallback(i, botaopressionadonovo.get(i));
	}
	
	//Evento de remoção de botão
	public static void botaoRemovido(int botao, boolean isSintetico) throws NotFoundException {
		if(!keys.contains(botao))throw new NotFoundException("Botão não encontrado!");;
		
		keys.remove(botao);
		botaoRemovidoHandler.runCallback(botao, isSintetico);
		
		if(!isSintetico)
			if(opostos.get(botao)!=null)
				if(keys.contains(opostos.get(botao))) 
					botaoPressionadoHandler.runCallback(opostos.get(botao), true);
					
		
	}
	//Evento de pressionamento de botão
	public static void botaoPressionado(int botao,boolean isSintetico) throws NotFoundException {
		if(keys.contains(botao))return;
		
		if(!isSintetico)
			if(opostos.get(botao)!=null)
				if(keys.contains(opostos.get(botao))) 
					botaoRemovidoHandler.runCallback(opostos.get(botao), true);
					
				
//					botaoRemovidoHandler.getEvento(opostos.get(botao)).run(true);
		
		keys.add(botao);
		botaoPressionadoHandler.runCallback(botao, isSintetico);
//		if(botaoPressionadoHandler.getEvento(botao)!=null)
//			botaoPressionadoHandler.getEvento(botao).run(isSintetico);

	}
	public static void ativarEvento(boolean isSintetico,int chave) throws NotFoundException {
		botaoPressionadoHandler.runCallback(chave, isSintetico);
	}
	public static boolean containsKey(int chave) {
		return keys.contains(chave);
	}
	
//	public static void ativarMantidos() {
//		for(int k:keys)
//			try {
//				GeradorEventos.botaomantidoHandler.throwEvento(k);
//			} catch (NotFoundException e) {
//				//Joga pra ver se tem algum evento de apertar esse botão.
//			}
//	}
}
