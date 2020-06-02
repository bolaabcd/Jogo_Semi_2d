package com.firstJogo.estrutura;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import com.firstJogo.regras.CallbacksGerais;
import com.firstJogo.utils.GlobalVariables;

//Classe da Janela do Jogo.
public class Janela {
	private static Object principalready = new Object();//Objeto-chave pra avisar quando a Janela está pronta.
	private static Janela principal;
	
	private long id;//Identificador da janela no GLFW
	private int width = 480, height = 480;
	private long fullscr = 0;
	private ArrayList<ExternalCallback> callbacks = new ArrayList<ExternalCallback>();//Callbacks brutas
	private static int FPS;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isFullscr() {
		if (fullscr == 0)
			return false;
		return true;
	}

	public long getId() {
		return id;
	}

	public boolean ShouldClose() {
		return GLFW.glfwWindowShouldClose(id);
	}

	public Janela(String titulo, boolean Hide) {
		if (Hide)//Criar janela escondida pra só mostrar quando ela tiver pronta.
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);

		id = GLFW.glfwCreateWindow(width, height, titulo, 0, 0);
		
		if (id == 0)
			throw new IllegalStateException("Failed to create the GLFW window");

		callbacks.add(new CallbacksGerais());

		setWindowCallbacks();

		GLFW.glfwMakeContextCurrent(id);
	}

	public void Hide() {
		GLFW.glfwHideWindow(id);
	}

	public void show() {
		GLFW.glfwShowWindow(id);
	}

	public void Destroy() {
		GLFW.glfwDestroyWindow(id);
	}

	public void setWindowPos(float x, float y) {
		if (!isFullscr())
			try (MemoryStack stack = MemoryStack.stackPush()) {//Aparentemente faz poder mecher na memória
				IntBuffer pWidth = stack.mallocInt(1);
				IntBuffer pHeight = stack.mallocInt(1);
				GLFW.glfwGetWindowSize(id, pWidth, pHeight);//Salva o tamanho da Janela nos Buffers
				GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());//Obtém o VidMode

				GLFW.glfwSetWindowPos(//Seta posição da Janela (de acordo com o tamanho)
						id, 
						(int) ((vidmode.width() - pWidth.get(0)) * x),
						(int) ((vidmode.height() - pHeight.get(0)) * y)
						);
			}
		else
			System.out.println("Não dá pra setar a posição quando a Janela está tela cheia!");
	}

	public void contextualize() {
		GLFW.glfwMakeContextCurrent(id);
		GL.createCapabilities();
	}

	public void apresente() {
		GLFW.glfwSwapBuffers(id);
	}

	public void setWindowCallbacks() {
		GLFW.glfwSetKeyCallback(id, (window, key, scancode, action, mods) -> {
			//Não faz se estiver no modo debug, a janela não estiver em foco e a ação foi de soltar botão
			//(Evita que as consequências dos eventos de botão soltado sejam ativadas no modo Debug).
			if (!(GlobalVariables.debugue && GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_FOCUSED) == GLFW.GLFW_FALSE
					&& action == GLFW.GLFW_RELEASE))
				//Executa todas as callbacks da lista.
				for (ExternalCallback cback : callbacks) {
					cback.KeyCallback(window, key, scancode, action, mods);
				}
		});
		
		GLFW.glfwSetWindowMaximizeCallback(id, (window, maximizado) -> {
			resize();
		});
		
		GLFW.glfwSetWindowSizeCallback(id, (window, wi, hei) -> {
			resize();
		});
	}

	public void finalizarCallbacks() {
		Callbacks.glfwFreeCallbacks(id);
	}

	private void resize() {//A idéia aqui é manter o jogo acontecendo quadradamente no meio da Janela sempre.
		IntBuffer wi = BufferUtils.createIntBuffer(1);
		IntBuffer hei = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(id, wi, hei);
		width = wi.get();
		height = hei.get();//Salva o novo tamanho desejado no objeto Janela
		if (width >= height) {//Se o novo comprimento for maior, posiciona a viewPort na metade do comprimento total.
			GL11.glViewport(width / 2 - height / 2, 0, height, height);
//			width = height;
		} else {//Se a nova altura for maior, coloca a viewPort na metade da altura total.
			GL11.glViewport(0, height / 2 - width / 2, width, width);
//			height = width;
		}
	}
	
	public static void Iniciar() {
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
	}

	public static void PollEvents() {
		GLFW.glfwPollEvents();
	}

	public static void Vsync(boolean deve_ativar) {
		if (deve_ativar)
			GLFW.glfwSwapInterval(1);
		else
			GLFW.glfwSwapInterval(0);

	}

	public static void terminate() {
		GLFW.glfwSetErrorCallback(null).free();
		GLFW.glfwTerminate();
	}
	//Setar Callbacks Genéricas (Independentes do Objeto da Janela)
	public static void setGeneralCallbacks() {
		GLFW.glfwSetErrorCallback(new GLFWErrorCallback() {//Callback de erro

			@Override
			public void invoke(int error_code, long descr_pointer) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(descr_pointer));

			}

		});
	}

	public static void setPrincipal(Janela j) {
		Janela.principal = j;
		synchronized (principalready) {
			principalready.notify();
		}
	}

	public static void setFPS(int fPS) {
		FPS = fPS;
	}

	public static int getFPS() {
		return FPS;
	}
	//Obter Janela Principal, aguardando se ela ainda não existir
	public static Janela getPrincipal() {
		while (Janela.principal == null)
			synchronized (principalready) {
				try {
					principalready.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		return Janela.principal;
	}

}
