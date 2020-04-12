package com.firstJogo.main;

import org.lwjgl.opengl.GL11;

import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.regras.CallbacksGerais;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.AzRenderer;
import com.firstJogo.visual.Shaders;
import com.firstJogo.visual.WorldRenderer;

public class Renderer implements Runnable{
	public static Thread main;//Objeto-Trhead de renderização
	private Janela principal;//Referência ao objeto da janela do jogo
	
	@Override
	public void run() {
		
		Prepare.prepararJanela();//Prepara a Janela (PRECISA ser feito aqui porque o WINDOWS não deixa mexer com Janelas criadas em outras Threads).
		Prepare.prepararPlayer();//Prepara a entidade do player
		Prepare.prepararCamera();//Prepara a câmera principal do player, já avisando que ela está pronta.
		Prepare.prepararMundo();
		CallbacksGerais.prepararBotoes();//Prepara as callbacks de botões
		CallbacksGerais.prepararTempos();//Prepara as callbacks de tempo
		
		principal=Janela.getPrincipal();
		principal.contextualize();//Contextualiza a Janela nessa Thread
		
		System.out.println("Iniciando Loop visual!");
		loop();
	}
	
	private void loop() {
		//Seta a cor de fundo padrão
		GL11.glClearColor(GlobalVariables.ClearColor[0], GlobalVariables.ClearColor[1], GlobalVariables.ClearColor[2], GlobalVariables.ClearColor[3]);
		//Ativa texturas 2d
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		
		//Cria os Vertex e Fragment Shaders principais.
		Shaders shad=new Shaders("./shaders/sombra");
		//Cria o renderizador de azulejos principal
		AzRenderer renderizator=new AzRenderer();
		//Cria o renderizador de mundos principal
		WorldRenderer mundo=new WorldRenderer();
		
		
		principal.show();//Apresenta a Janela principal
		
		
		long begtime=System.nanoTime() / (long) 1000000000;//Seta o tempo do segundo atual
		int amt=0;//Seta a quantidade de Frames Renderizados para 0
		long begnano=System.nanoTime();//Seta o Tempo da checagem anterior de FPS
		

		
		while(!principal.ShouldClose() ) {//Continua até a janela receber a ordem de ser fechada.
			
			if (begtime == System.nanoTime() / (long) 1000000000) {// Se estiver no mesmo segundo de antes
				try {
					long temp = System.nanoTime();// Carrega o tempo atual

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
				begtime=System.nanoTime() / (long) 1000000000;//Resetar tempo do segundo inicial
				System.out.println("FPS: "+Integer.toString(amt));//Imprime a quantidade de frames do segundo
				amt=0;//reseta a quantidade de frames
				//Vai renderizar mais um frame.
			}
			//Limpa o Buffer de frame (a tela não-renderizada)
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			Janela.PollEvents();//Recolhendo eventos de botao!
			//Se não colocar isso aqui buga no Windows...
			
			//Renderiza mundo no Buffer.
			mundo.renderizar(renderizator, shad, Camera.getMain());
			//Troca os pixels do Buffer com os pixels da tela, porque o buffer tava sendo preparado ainda.
			principal.apresente();
			//Soma mais um frame aos frames renderizados nesse segundo.
			amt++;
		}
		
		// Destruir janela quando o loop acabar
		principal.Destroy();
		
		//Finalizar tudo.
		Janela.terminate();
	}
	
}
