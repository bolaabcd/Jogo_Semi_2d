package com.firstJogo.main;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoAtual;
import com.firstJogo.visual.Camera;
import com.firstJogo.visual.Janela;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Shaders;
import com.firstJogo.visual.Textura;

public class Renderer implements Runnable{
	private Janela window;
	
	@Override
	public void run() {
		Janela.setGeneralCallbacks();
		Janela.Iniciar();
		window=new Janela("MicroCraft!",true);
		Janela.setPrincipal(window);
		window.contextualize();
		//Centralizando janela
		//try ( MemoryStack stack = MemoryStack.stackPush() ) {
		//	IntBuffer pWidth = stack.mallocInt(1); 
		//	IntBuffer pHeight = stack.mallocInt(1); 
		//	GLFW.glfwGetWindowSize(window.getId(), pWidth, pHeight);
		//	GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        //
		//	GLFW.glfwSetWindowPos(
		//		window.getId(),
		//		(vidmode.width() - pWidth.get(0)) / 2,
		//		(vidmode.height() - pHeight.get(0)) / 2
		//	);
		//} 
		//aparentemnte faz um contexto de gráficos...
		
		//window.setSize(480, 480);
		//window.setFullscr(true);
		//window.setSize(720, 480);
		
		window.setWindowPos(0.5f, 0.5f);
		
		//GLFW.glfwMakeContextCurrent(window.getId());
		//GLFW.glfwSwapInterval(1);//Vsync
		Janela.Vsync(true);
		
		System.out.println("Iniciando Loop visual!");
		loop();
	}
	
	private void loop() {
		//CRIA UM CONTEXTO aparentemnete e já coloca na janela!
		GL.createCapabilities();
		GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		
		Textura primeira=new Textura("./imgs/Grama.png");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		
		float[] vertices=new float[] {
		                  -1,-1,0,//índice 0
		                   1, 1,0,//índice 1
		                   1,-1,0,//índice 2
		                   
//		                   -1,-1,0,
    	                   -1, 1,0,//índice 3
//	    	                1, 1,0
		                   
		};
		
		float[] texturas=new float[] {//Parece q a origem aqui é o topo da esquerda...
				0,1,//Mas isso da origem não pode ser...
				1,0,
				1,1,
				
//				0,0,
				0,0,//Dá pra fazer MUITA maluquice com esses números aqui...
//				1,1
		};
		
		int[] indices=new int[] {//Pra só declarar os pontos uma vez!
				0,1,2,
				0,3,1
		};
		
		Modelo mod=new Modelo(vertices, indices);
		mod.addtex(texturas);
		
		Shaders shad=new Shaders("./shaders/sombra");
		
		/*
		Matrix4f mat=new Matrix4f()
				.ortho2D(-240, 240, -240, 240)
				.scale(240);
		//Aparentemente seta pra janela inteira...
		*/
		
		
		Camera camera=new Camera(window.getWidth(),window.getHeight());
		//TAMANHO DA JANELA!!!
		
		//System.out.println(camera.getProjec());
		Matrix4f mat=new Matrix4f()
		.scale(GlobalVariables.tam*window.getWidth()/2)
		.setTranslation(GlobalVariables.tam*window.getWidth()/2, GlobalVariables.tam*window.getWidth()/2, 0)
		;
		
		//primeira.bind(0);//Usamos o sampler número 0!
		//shad.bindar();
		window.show();
		long begtime=TempoAtual.getsec();
		int amt=0;
		//long realbegtime=TempoAtual.getsec();
		
		long begnano=System.nanoTime();
		
		while(!window.ShouldClose() ) {
			//double beg=(double)System.nanoTime();
			primeira.bind(0);//Usamos o sampler número 0!
			shad.bindar();
			mat=new Matrix4f()
					.scale(GlobalVariables.tam*window.getWidth()/2)
					//.setTranslation(GlobalVariables.tam, GlobalVariables.tam, 0)
					;
			
			
			if(begtime==TempoAtual.getsec()) {
				
				try {
					if((1000/Janela.getFPS()-(System.nanoTime()-begnano)/(long)1000000)>=0)
					Thread.sleep(1000/Janela.getFPS()-(System.nanoTime()-begnano)/(long)1000000);
					begnano=System.nanoTime();
				} catch (InterruptedException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
				
				if(amt==Janela.getFPS())continue;
			}else {
				//window.setSize(window.getHeight()+10, window.getWidth()+10);
				//if((realbegtime-TempoAtual.getsec())%2==0) {
					//window.setSize(window.getWidth()+10, window.getHeight()+10);
					//Camera.getMain().setSize(window.getWidth(), window.getHeight());
					//window.setFullscr(!window.getFullscr());
				//}
				begtime=TempoAtual.getsec();
				System.out.println("FPS: "+Integer.toString(amt));
				amt=0;
				continue;
			}
			//System.out.println(amt);
			
			/*Matrix4f mat=new Matrix4f()
					.scale(GlobalVariables.tam)
					.setTranslation(GlobalVariables.tam, GlobalVariables.tam, 0)
					;*/
			// Poll for window events.
			Janela.PollEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			shad.setUniforme("localizacao_da_textura_tambem_chamada_de_sampler", 0);//Setamos o sampler para 0, onde está a nossa textura!
			shad.setUniforme("projecao", camera.getProjec().mul(mat));//camera.getProjec().mul(finala)
			mod.renderizar();
			
			//TROCA O DO BACK-END COM O DO FRONT-END, pq o do back end tava sendo desenhado ainda!
			//GLFW.glfwSwapBuffers(window.getId()); // swap the color buffers
			window.apresente();
			//System.out.println(GlobalVariables.tam);
			
			amt++;
			//System.out.println(((double)System.nanoTime()-beg)/(double)1000000000);
		}
		
		//window.Hide();
		// Free the window callbacks and destroy the window
		window.Destroy();

		Janela.terminate();
		
		//GLFW.glfwSetErrorCallback(null).free();
	}
	
}
