package com.firstJogo.main;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.firstJogo.Mundos.AzRenderer;
import com.firstJogo.Mundos.World;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.utils.TempoAtual;
import com.firstJogo.visual.Camera;
import com.firstJogo.visual.Janela;
import com.firstJogo.visual.Shaders;

public class Renderer implements Runnable{
	private Janela window;
	
	@Override
	public void run() {
		

		Janela.setGeneralCallbacks();
		Janela.Iniciar();
		window=new Janela("MicroCraft!",true);
		Janela.setPrincipal(window);
		//window.setFullscr(true);
		//window.setSize(1366, 768);
		//window.setSize(720, 720);
		window.contextualize();
		
		window.setWindowPos(0.5f, 0.5f);
		
		Janela.Vsync(true);
		
		System.out.println("Iniciando Loop visual!");
		loop();
	}
	
	private void loop() {
		GL.createCapabilities();
		GL11.glClearColor(GlobalVariables.ClearColor[0], GlobalVariables.ClearColor[1], GlobalVariables.ClearColor[2], GlobalVariables.ClearColor[3]);
		
//		Textura primeira=new Textura("./imgs/Grama.png");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		
//		float[] vertices=new float[] {
//		                  -1,-1,0,//índice 0
//		                   1, 1,0,//índice 1
//		                   1,-1,0,//índice 2
//    	                   -1, 1,0,//índice 3
//		                   
//		};
//		
//		float[] texturas=new float[] {//Parece q a origem aqui é o topo da esquerda...
//				0,1,//Mas isso da origem não pode ser...
//				1,0,
//				1,1,
//				0,0,//Dá pra fazer MUITA maluquice com esses números aqui...
//		};
//		
//		int[] indices=new int[] {//Pra só declarar os pontos uma vez!
//				0,1,2,
//				0,3,1
//		};
//		
//		Modelo mod=new Modelo(vertices, indices);
//		mod.addtex(texturas);
//		
		Shaders shad=new Shaders("./shaders/sombra");
		
//		int quantosblocos=9;
//		int experiencia=window.getHeight()/(quantosblocos*2);
//		Matrix4f escala=new Matrix4f()
//				//.translate(0, 0, 0)
//				//.scale(window.getWidth()/(2*quantosblocos))
//				.scale(16)
//				
//				;
//		int intperpixel=experiencia/8;
//		int pixelsperbloco=16;
		AzRenderer renderizator=new AzRenderer();
		World mundo=new World();
//		System.out.println(intperpixel*pixelsperbloco);
//		
//		
		Camera camera=new Camera(window.getWidth(),window.getHeight());
//		Camera camera=new Camera(intperpixel*quantosblocos*pixelsperbloco,intperpixel*quantosblocos*pixelsperbloco);
	//	camera.setPos(new Vector3f(-4f*intperpixel*pixelsperbloco,0*intperpixel*pixelsperbloco,0));

//		camera.setPos(new Vector3f((quantosblocos/2-0.5f)*intperpixel*pixelsperbloco,(quantosblocos/2-1.5f)*intperpixel*pixelsperbloco,0));
//
//		Matrix4f mat=new Matrix4f()
//		.scale(GlobalVariables.tam*window.getWidth()/2)
//		.setTranslation(GlobalVariables.tam*window.getWidth()/2, GlobalVariables.tam*window.getWidth()/2, 0)
//		;
		
		window.show();
		long begtime=TempoAtual.getsec();
		int amt=0;
		
		long begnano=System.nanoTime();
		
		while(!window.ShouldClose() ) {
//			primeira.bind(0);//Usamos o sampler número 0!
//			shad.bindar();
//			mat=new Matrix4f()
//					.scale(GlobalVariables.tam*window.getWidth()/2)
//					;
			
			if(begtime==TempoAtual.getsec()) {
				
				try {
					if((1000/Janela.getFPS()-(System.nanoTime()-begnano)/(long)1000000)>=0)
					Thread.sleep(1000/Janela.getFPS()-(System.nanoTime()-begnano)/(long)1000000);
					begnano=System.nanoTime();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(amt==Janela.getFPS())continue;
			}else {
//				camera.setPos(camera.getPos().add(0.01f, 0.01f, 0));

				begtime=TempoAtual.getsec();
				System.out.println("FPS: "+Integer.toString(amt));
				amt=0;
				continue;
			}
			Janela.PollEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Limpa o framebuffer
//			shad.setUniforme("localizacao_da_textura_tambem_chamada_de_sampler", 0);//Setamos o sampler para 0, onde está a nossa textura!
//			shad.setUniforme("projecao", camera.getProjec().mul(mat));//camera.getProjec().mul(finala)
//			mod.renderizar();
			
			mundo.renderizar(renderizator, shad, camera);
//			for(int i=0;i<16;i++)
//				for(int j=0;j<16;j++)
//					renderizator.Renderizar((byte)0, j, i, shad, escala, camera);
			
			//TROCA O DO BACK-END COM O DO FRONT-END, pq o do back end tava sendo desenhado ainda!
			window.apresente();
			
			amt++;
		}
		
		// Destruir janela 
		window.Destroy();

		Janela.terminate();
	}
	
}
