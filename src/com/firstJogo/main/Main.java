package com.firstJogo.main;


public class Main {
	//private static Janela window;
	public static void main(String[] args) {
		initvisual();
		initbackground();
	}
	private static void initvisual() {
		//Janela.setGeneralCallbacks();
		//Janela.Iniciar();
		//window=new Janela("MicroCraft!",true);
		
		
		//window.setSize(700, 700);
		//window.setFullscr(true);
		//window.Criar();
		//Inicializando Janela
		//GLFWErrorCallback.createPrint(System.err).set();
		//GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		//GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
		//GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
		
		//window=GLFW.glfwCreateWindow(854, 480, "MicroCraft!", 0, 0);
		//window=GLFW.glfwCreateWindow(480, 480, "MicroCraft!", 0, 0);
		//if ( window == 0 )
		//	throw new RuntimeException("Failed to create the GLFW window");
		
		//Ativando Thread visual
		
		Renderer r=new Renderer();
		
		Thread t=new Thread(r);
		t.start();
	}
	private static void initbackground() {
		
		/*GLFW.glfwSetKeyCallback(window.getId(), (window, key, scancode, action, mods) -> {
			if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
				GLFW.glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			else if(key==GLFW.GLFW_KEY_ENTER) {
				if(GlobalVariables.tam==1)return;
				GlobalVariables.tam=((float)Math.round((GlobalVariables.tam+0.01f)*100))/100;
				}
			else if(key==GLFW.GLFW_KEY_BACKSPACE) {
				if(GlobalVariables.tam==0)return;
				GlobalVariables.tam=((float)Math.round((GlobalVariables.tam-0.01f)*100))/100;
				}
		});*/
		
		/*GlobalVariables.gkc=(window, key, scancode, action, mods) -> {
			if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
				GLFW.glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			else if(key==GLFW.GLFW_KEY_ENTER) {
				if(GlobalVariables.tam==1)return;
				GlobalVariables.tam=((float)Math.round((GlobalVariables.tam+10)*100))/100;
				}
			else if(key==GLFW.GLFW_KEY_BACKSPACE) {
				if(GlobalVariables.tam==0)return;
				GlobalVariables.tam=((float)Math.round((GlobalVariables.tam-10)*100))/100;
				}
		};
		GLFW.glfwSetKeyCallback(window.getId(), GlobalVariables.gkc);*/

	}
}
