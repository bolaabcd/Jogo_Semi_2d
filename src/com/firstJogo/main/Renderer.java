package com.firstJogo.main;

import org.lwjgl.opengl.GL11;

import com.firstJogo.estrutura.Janela;
import com.firstJogo.estrutura.Player;
import com.firstJogo.regras.CallbacksGerais;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.AzRenderer;
import com.firstJogo.visual.Shaders;
import com.firstJogo.visual.WorldRenderer;

public class Renderer implements Runnable{
	public static Thread main;
	private Janela principal;
	
	@Override
	public void run() {
		//PRECISA ser feito aqui porque o WINDOWS não deixa mexer com Janelas criadas em outras Threads).
		Prepare.prepararJanela();
		Prepare.prepararMundo();
		Prepare.prepararPlayer();
		CallbacksGerais.prepararBotoes();
		CallbacksGerais.prepararTempos();
		
		principal=Janela.getPrincipal();
		principal.contextualize();
		
		System.out.println("Iniciando Loop visual!");
		loop();
	}
	
	private void loop() {
		GL11.glClearColor(GlobalVariables.ClearColor[0], GlobalVariables.ClearColor[1], GlobalVariables.ClearColor[2], GlobalVariables.ClearColor[3]);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		
		Shaders shad=new Shaders("./shaders/sombra");
		AzRenderer renderizator=new AzRenderer();
		WorldRenderer mundo=new WorldRenderer();
		
		
		principal.show();
		
		//Variáveis para checar e manter FPS.
		long begtime=System.nanoTime() / (long) 1000000000;
		int amt=0;
		long begnano=System.nanoTime();
				
		while(!principal.ShouldClose() ) {
//			if (amt == 1)
//				try {
//					KeyCallbackHandler.ativarEvento(true, GLFW.GLFW_KEY_P);
//				} catch (NotFoundException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			
			if (begtime == System.nanoTime() / (long) 1000000000) {
				try {
					long temp = System.nanoTime();

					// Se tiver passado menos milisegundos (desde a última checagem) que o previsto pelos FPS
					if ((1000 / Janela.getFPS() - (temp - begnano) / (long) 1000000) >= 0)
						// Dormir a Thread pelo número de milisegundos adequados entre-loops (1/FPS segundos)
						Thread.sleep(1000 / Janela.getFPS() - (temp - begnano) / (long) 1000000);
					begnano = System.nanoTime();// Atualizar tempo da checagem anterior
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (amt == Janela.getFPS())
					continue;//Se já tiver batido o limite desejado de FPS, continuar
				
			}else {//Se tiver passado um segundo inteiro
				begtime=System.nanoTime() / (long) 1000000000;//Atualizar para o segundo atual
				System.out.println("FPS: "+Integer.toString(amt));//Imprime a quantidade de frames do segundo
				amt=0;//reseta a quantidade de frames
				//Renderiza um Frame extra, senão dá diferente.
			}

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			Janela.PollEvents();
			//Se não der o Poll Events aqui buga no Windows...

			mundo.renderizar(renderizator, shad, Player.mainPlayer.camera);
			principal.apresente();
			amt++;//FPS
		}
		
		principal.Destroy();
		
		Janela.terminate();
	}
	
}
