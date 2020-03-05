package com.firstJogo.visual;

import java.awt.Frame;
import java.awt.Toolkit;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import com.firstJogo.utils.GlobalVariables;

public class Janela {
private static Janela principal;
private long id;
private int width=480,height=480;
private long fullscr=0;
private static int screen_width;
private static int screen_height;
private static int FPS=60;
public int getWidth() {
	return width;
}
public int getHeight() {
	return height;
}

//private void recreate() {
//	long idtemp=GLFW.glfwCreateWindow(width, height, titulo,fullscr, id);
//	Destroy();
//	id=idtemp;
//	if ( id == 0 )
//		throw new IllegalStateException("Failed to create the GLFW window");
//	GLFW.glfwMakeContextCurrent(id);
//	setWindowCallbacks();
//
////	GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
//}
//public void setSize(int w,int h) {
//	width=w;
//	height=h;
//	this.recreate();
//	this.setWindowPos(posx, posy);
//	show();
//	
//}
public boolean getFullscr() {
	if(fullscr==0)return false;
	return true;
}
//public void setFullscr(boolean b) {
//	if(b) {
//		
//	}else {
//		width=480;
//		height=480;
//		fullscr=0;
//		//GLFW.glfwSetWindowSize(id, screen_width, height);
//		this.recreate();
//		this.setWindowPos(posx, posy);
//		this.show();
//	}
//	
//}
public void Hide() {
	GLFW.glfwHideWindow(id);
}
public Janela(String titulo,boolean Hide) {
//	this.titulo=titulo;
	if(Hide)GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		
	id=GLFW.glfwCreateWindow(width, height, titulo, 0, 0);
	Janela.getScreen_width();
	Janela.getScreen_height();
	if ( id == 0 )
		throw new IllegalStateException("Failed to create the GLFW window");
	setWindowCallbacks();
	GLFW.glfwMakeContextCurrent(id);
}

public void show() {
	GLFW.glfwShowWindow(id);
}
public long getId() {
	return id;
}
public boolean ShouldClose() {
	return GLFW.glfwWindowShouldClose(id);
}
public void Destroy() {
	GLFW.glfwDestroyWindow(id);
}
public void setWindowPos(float x,float y) {
	if(!getFullscr())
	try ( MemoryStack stack = MemoryStack.stackPush() ) {
//		posx=x;
//		posy=y;
		IntBuffer pWidth = stack.mallocInt(1); 
		IntBuffer pHeight = stack.mallocInt(1); 
		GLFW.glfwGetWindowSize(id, pWidth, pHeight);
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

		GLFW.glfwSetWindowPos(
				id,
			(int)((vidmode.width() - pWidth.get(0)) *x),
			(int)((vidmode.height() - pHeight.get(0)) *y)
		);
	} 
	else System.out.println("Não dá pra setar o tamanho quando a Janela está fullscreen!");
}
public void contextualize() {
	GLFW.glfwMakeContextCurrent(id);
}
public void apresente() {
	GLFW.glfwSwapBuffers(id);
}
public void setWindowCallbacks() {
	GLFW.glfwSetKeyCallback(id, (window, key, scancode, action, mods) -> {
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
		if(key==GLFW.GLFW_KEY_W&&action!=GLFW.GLFW_RELEASE) {
			Camera.getMain().setPos(Camera.getMain().getPos().add(0,-2,0));
		}
		if(key==GLFW.GLFW_KEY_S&&action!=GLFW.GLFW_RELEASE) {
			Camera.getMain().setPos(Camera.getMain().getPos().add(0,2,0));
		}
		if(key==GLFW.GLFW_KEY_D&&action!=GLFW.GLFW_RELEASE) {
			Camera.getMain().setPos(Camera.getMain().getPos().add(-2,0,0));
		}
		if(key==GLFW.GLFW_KEY_A&&action!=GLFW.GLFW_RELEASE) {
			Camera.getMain().setPos(Camera.getMain().getPos().add(2,0,0));
		}
	});
	GLFW.glfwSetWindowMaximizeCallback(id, (window,maximizado) -> {
			resize();
	});
	GLFW.glfwSetWindowSizeCallback(id, (window,wi,hei)->{
		resize();
	});
}

private void resize() {
	IntBuffer wi=BufferUtils.createIntBuffer(1);
	IntBuffer hei=BufferUtils.createIntBuffer(1);
	GLFW.glfwGetWindowSize(id, wi, hei);
	int actwidth=width;
	int actheight=height;
	width=wi.get();
	height=hei.get();
	if(width>=height) {
		GL11.glViewport(width/2-actwidth/2, 0, height, height);
		width=height;
	}
	else {
		GL11.glViewport(0, height/2-actheight/2, width, width);
		height=width;
	}
}

public void finalizarCallbacks() {
	Callbacks.glfwFreeCallbacks(id);
}

public static void Iniciar() {
	if ( !GLFW.glfwInit() )
		throw new IllegalStateException("Unable to initialize GLFW");
}
public static void PollEvents() {
	GLFW.glfwPollEvents();
}
public static void Vsync(boolean deve_ativar) {
	if(deve_ativar)GLFW.glfwSwapInterval(1);
	else GLFW.glfwSwapInterval(0);
}
public static void terminate() {
	GLFW.glfwSetErrorCallback(null).free();
	GLFW.glfwTerminate();
}
public static void setGeneralCallbacks() {
	GLFW.glfwSetErrorCallback(new GLFWErrorCallback() {

		@Override
		public void invoke(int error_code, long descr_pointer) {
			throw new IllegalStateException(GLFWErrorCallback.getDescription(descr_pointer));
			
		}
		
	});
}
public static void setPrincipal(Janela j) {
	Janela.principal=j;
}
public static Janela getPrincipal() {
	return Janela.principal;
}
public static int getScreen_width() {
	screen_width=GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).width()-(int)Toolkit.getDefaultToolkit().getScreenInsets((new Frame()).getGraphicsConfiguration()).left;
	return screen_width;
}
public static int getScreen_height() {
	screen_height=GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).height()-(int)Toolkit.getDefaultToolkit().getScreenInsets((new Frame()).getGraphicsConfiguration()).top;
	return screen_height;
}
public static int getFPS() {
	return FPS;
}
public static void setFPS(int fPS) {
	FPS = fPS;
}
}
