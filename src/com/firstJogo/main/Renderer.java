package com.firstJogo.main;

import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoAtual;
import com.firstJogo.visual.Camera;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Shaders;
import com.firstJogo.visual.Textura;

public class Renderer implements Runnable{
	private long window;
	public Renderer(long window) {
		this.window=window;
	}
	@Override
	public void run() {
		//Centralizando janela
		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); 
			IntBuffer pHeight = stack.mallocInt(1); 
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			GLFW.glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} 
		//aparentemnte faz um contexto de gráficos...
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);

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
		
		
		Camera camera=new Camera(480,480);
		//TAMANHO DA JANELA!!!
		
		//System.out.println(camera.getProjec());
		
		primeira.bind(0);//Usamos o sampler número 0!
		shad.bindar();
		GLFW.glfwShowWindow(window);
		long begtime=TempoAtual.getsec();
		int amt=0;
		while(!GLFW.glfwWindowShouldClose(window) ) {
			//double beg=(double)System.nanoTime();
			
			
			if(begtime==TempoAtual.getsec()) {
				
				try {
					Thread.sleep(1000/GlobalVariables.FPS);
				} catch (InterruptedException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
				
				if(amt==GlobalVariables.FPS)continue;
			}else {
				begtime=TempoAtual.getsec();
				System.out.println("FPS: "+Integer.toString(amt));
				amt=0;
				continue;
			}
			//System.out.println(amt);
			
			Matrix4f mat=new Matrix4f()
					.scale(GlobalVariables.tam)
					.setTranslation(GlobalVariables.tam, GlobalVariables.tam, 0);
			// Poll for window events.
			GLFW.glfwPollEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			
			shad.setUniforme("localizacao_da_textura_tambem_chamada_de_sampler", 0);//Setamos o sampler para 0, onde está a nossa textura!
			shad.setUniforme("projecao", camera.getProjec().mul(mat));//camera.getProjec().mul(finala)
			mod.renderizar();
			
			
			//TROCA O DO BACK-END COM O DO FRONT-END, pq o do back end tava sendo desenhado ainda!
			GLFW.glfwSwapBuffers(window); // swap the color buffers
			
			amt++;
			//System.out.println(((double)System.nanoTime()-beg)/(double)1000000000);
		}
		// Free the window callbacks and destroy the window
		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

}
