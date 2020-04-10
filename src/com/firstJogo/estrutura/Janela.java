package com.firstJogo.estrutura;

import java.awt.Frame;
import java.awt.Toolkit;
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

import com.firstJogo.padrao.CallbacksExternas;
import com.firstJogo.regras.ExternalCallback;
import com.firstJogo.utils.GlobalVariables;

//Classe da Janela do Jogo.
public class Janela {
	private static Object principalready = new Object();//Objeto-chave pra avisar quando a Janela está pronta.
	private static Janela principal;//Objeto da Janela do Jogo
	private static int screen_width;//Comprimento da Tela TODO: inutilizado
	private static int screen_height;//Altura da Tela TODO: inutilizado
	
	private long id;//Identificador da janela no GLFW
	private int width = 480, height = 480;//Tamanho padrão da ViewPort e da Janela
	private long fullscr = 0;//Se a janela está ou não em tela cheia
	private ArrayList<ExternalCallback> callbacks = new ArrayList<ExternalCallback>();//Callbacks brutas de botões
	private int FPS = 60;//Quantidade padrão de FPS TODO: inutilizado

	//Obtendo comprimento atual da ViewPort da Janela
	public int getWidth() {
		return width;
	}

	//Obtendo altura atual da ViewPort da Janela
	public int getHeight() {
		return height;
	}
	//Obter Frames Por Segundo padrão da Janela
	public int getFPS() {
		return FPS;
	}
	//Obtendo se a Janela está ou não em tela cheia.
	public boolean getFullscr() {
		if (fullscr == 0)
			return false;
		return true;
	}
	//Obter identificador do GLFW
	public long getId() {
		return id;
	}
	//Saber se a Janela deveria fechar
	public boolean ShouldClose() {
		return GLFW.glfwWindowShouldClose(id);
	}
	//Construtor de Janela
	public Janela(String titulo, boolean Hide) {
		if (Hide)//Criar janela escondida pra só mostrar quando ela tiver pronta.
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		//Criar Janela e associar ao um ID
		id = GLFW.glfwCreateWindow(width, height, titulo, 0, 0);
		//Setar tamanho da tela
		Janela.getScreen_width();
		Janela.getScreen_height();
		//Aviso de erro ao criar Janela
		if (id == 0)
			throw new IllegalStateException("Failed to create the GLFW window");
		//Adicionando Callbacks Externas padrão
		callbacks.add(new CallbacksExternas());
		//Setando callbacks da Janela a partir da lista
		setWindowCallbacks();
		//Setando contexto GLFW pra Janela
		GLFW.glfwMakeContextCurrent(id);
	}
	//Esconder Janela
	public void Hide() {
		GLFW.glfwHideWindow(id);
	}
	//Mostrar Janela
	public void show() {
		GLFW.glfwShowWindow(id);
	}
	//Destruir Janela
	public void Destroy() {
		GLFW.glfwDestroyWindow(id);
	}
	//Setar posição da Janela na tela
	public void setWindowPos(float x, float y) {
		if (!getFullscr())
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
	//Coloca o contexto GLFW da Janela, e cria capabilidades do GL
	public void contextualize() {
		GLFW.glfwMakeContextCurrent(id);
		GL.createCapabilities();
	}
	//Troca os buffers de preparação da tela pelos da tela, efetivamente fazendo o frame aparecer.
	public void apresente() {
		GLFW.glfwSwapBuffers(id);
	}
	//Setar FPS padrão da Janela
	public void setFPS(int fPS) {
		FPS = fPS;
	}
	//Ativa as callbacks da Janela
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
		
		//Ativa a callback de maximização da Janela
		GLFW.glfwSetWindowMaximizeCallback(id, (window, maximizado) -> {
			resize();
		});
		
		//Ativa a callback de mudança de tamanho da Janela
		GLFW.glfwSetWindowSizeCallback(id, (window, wi, hei) -> {
			resize();
		});
	}
	//Liberar Callbacks da janela
	public void finalizarCallbacks() {
		Callbacks.glfwFreeCallbacks(id);
	}
	//Ajusta a Janela ao novo tamaho, alterando o "ViewPort".
	private void resize() {//A idéia aqui é manter o jogo acontecendo quadradamente no meio da Janela sempre.
		IntBuffer wi = BufferUtils.createIntBuffer(1);
		IntBuffer hei = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(id, wi, hei);//Obtém o novo tamanho desejado da Janela
		width = wi.get();
		height = hei.get();//Salva o novo tamanho desejado no objeto Janela
		if (width >= height) {//Se o novo comprimento for maior, posiciona a viewPort na metade do comprimento total.
//Metade do comprimento da Janela completa - metade do tamanho da ViewPort (posiciona a quina inferior esquerda da ViewPort).
			GL11.glViewport(width / 2 - height / 2, 0, height, height);
			width = height;//Salva o comprimento da viewport como se fosse o da Janela.
			//(A viewPort sempre será quadrada nesse joguinho.)
		} else {//Se a nova altura for maior, coloca a viewPort na metade da altura total.
//Metade da altura da Janela completa - metade do tamanho da ViewPort (posiciona a quina inferior esquerda da ViewPort).
			GL11.glViewport(0, height / 2 - width / 2, width, width);
			height = width;//Salva o comprimento da viewport como se fosse o da Janela
			//(A viewPort sempre será quadrada nesse joguinho.)
		}
	}
	
	
	//Iniciar GLFW
	public static void Iniciar() {
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
	}
	//Puxar ecentos recebidos pela Janela
	public static void PollEvents() {
		GLFW.glfwPollEvents();
	}
	//Ativar Vsync pra não despertiçar frame sem o monitor dar conta de carregar
	public static void Vsync(boolean deve_ativar) {
		if (deve_ativar)
			GLFW.glfwSwapInterval(1);
		else
			GLFW.glfwSwapInterval(0);
	}
	//Terminalizar GLFW
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
	//Setar Objeto de Janela principal
	public static void setPrincipal(Janela j) {
		Janela.principal = j;
		synchronized (principalready) {
			principalready.notify();
		}
	}
	//Obter Janela Principal, aguardando se ela ainda não existir
	public static Janela getPrincipal() {
		while (Janela.principal == null)//Enquanto não existir Janela principal
			synchronized (principalready) {
				try {
					principalready.wait();//Esperar avisarem que a Janela principal foi criada
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		return Janela.principal;
	}
	//Obter comprimento da Tela
	public static int getScreen_width() {
		screen_width = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).width()
				- (int) Toolkit.getDefaultToolkit().getScreenInsets((new Frame()).getGraphicsConfiguration()).left;
		return screen_width;
	}
	//Obter altura da Janela
	public static int getScreen_height() {
		screen_height = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).height()
				- (int) Toolkit.getDefaultToolkit().getScreenInsets((new Frame()).getGraphicsConfiguration()).top;
		return screen_height;
	}

}
