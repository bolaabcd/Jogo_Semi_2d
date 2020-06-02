package com.firstJogo.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.joml.Vector2f;

import com.firstJogo.Handlers.KeyEventHandler;
import com.firstJogo.Mundos.Humano;
import com.firstJogo.Mundos.MundoCarregado;
import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.utils.ConsumerCheckedException;
import com.firstJogo.utils.GlobalVariables;

//Thread responsável por preparar as variáveis iniciais do programa e ler as configurações.
public class Prepare implements Runnable {
	
	public static void prepararCamera() {
		Camera camera = new Camera(Janela.getPrincipal().getWidth(), Janela.getPrincipal().getHeight());
		Camera.setMain(camera);
	}

	public static void prepararPlayer() {
		Humano player = new Humano(new Vector2f(0, 0),true);
//		player.setPlayer(true);
	}

	public static void prepararJanela() {
		Janela.setGeneralCallbacks();
		Janela.Iniciar();
		Janela.setPrincipal(new Janela("MicroCraft!", true));
		Janela.getPrincipal().contextualize();
		Janela.getPrincipal().setWindowPos(0.5f, 0.5f);
		Janela.Vsync(GlobalVariables.vSync);
	}

	public static void prepararMundo() {
		MundoCarregado mundo = new MundoCarregado();
		mundo.setAtual();
	}

	@Override
	public void run() {
		try {
			System.out.println("Analisando configurações!");

			// Lendo configurações:
			File arq = new File("./config.properties");
			runIfNotExists(arq,this::config_padrao);
			valid_config(arq);// Verifica se as configurações são válidas
			load_config(arq);// Carrega as configurações

			
			// Perparando Shaders:
			arq = new File("./shaders/");
			runIfNotExists(arq, File::mkdir);
			runIfNotExists(new File("./shaders/sombra.vs"), this::vshader);
			runIfNotExists(new File("./shaders/sombra.fs"), this::fshader);

			
			// Preparando imagens iniciais:
			arq = new File(GlobalVariables.imagem_pastas[0]);
			runIfNotExists(arq, File::mkdir);
			ArrayList<String> listaImagens = new ArrayList<String>();
			for (String str : new String[] { "Up", "Down", "Left", "Right" })
				for (int i = 1; i < 3; i++)
					listaImagens.add("Humano" + str + i);
			listaImagens.add("Grama");
			listaImagens.add("Pedra");
			
			for (String str : listaImagens)
				runIfNotExists(new File(GlobalVariables.imagem_pastas[0] + str +".png"),  //+ GlobalVariables.imagem_formato),
						(File arqi) -> defimagem(arqi, str));

			
			// Cria o arquivo padrão para os tipos de bloco. TODO
			arq = new File(GlobalVariables.mundos_pastas[0] + "descricao_dos_blocos" + ".txt");
			runIfNotExists(arq, this::tipospadrao);

			KeyEventHandler.inicializar();

		} catch (IOException e) {
			System.out.println("ERRO COM OS ARQUIVOS!");
			throw new UncheckedIOException(e);
		} 
	}
	
	private void runIfNotExists (File arq,ConsumerCheckedException<File> c) throws IOException {
		if(!arq.exists())
			c.accept(arq);
	}

	private void config_padrao(File config) throws IOException {
		config.createNewFile();
		Properties prop = new Properties();
		prop.put("Pastas de imagem:", "./imgs/");
		prop.put("Pastas de mundo:", "./mundos/");
		prop.put("Cor de fundo (RGBA):", "0,0,0,0");
		prop.put("Pastas de plugins:", "./plugins/");// TODO: Pasta de plugins padrão
		prop.put("Plugins:", "");// TODO: Lista de plugins
		prop.put("FPS Maximo:", "60");
		prop.put("VSync ativado:", "true");

		FileOutputStream out = new FileOutputStream(config);

		prop.store(out, "Configurações gerais do jogo!");

		out.close();
		
		runIfNotExists(new File("./imgs/"), File::mkdir);
		runIfNotExists(new File("./mundos/"), File::mkdir);
		runIfNotExists(new File("./plugins/"), File::mkdir);
	}

	private String[] getArgs(Properties prop, String chave) {
		try {
			String s=(String) prop.get(chave);
			return s==null?null:s.split(",");
		} catch (ClassCastException c) {
			throw new RuntimeException("Erro inesperado!");
		}
	}

	private String[] validacaoInicial(String chave, int quantiaMin, int quantiaMax, Properties prop) {
		String[] args = getArgs(prop, chave);
		if (args == null)
			erroconfig(new IllegalStateException("Argumento " + chave + " indisponível!"));
		if (args.length > quantiaMax || args.length < quantiaMin)
			erroconfig(new IllegalStateException("Devem haver entre " + quantiaMin + " e " + quantiaMax
					+ " elementos sob o argumento \"" + chave + "\"."));
		return args;
	}

	private void validateArquivos(String chave, int quantiaMin, int quantiaMax, Properties prop) {
		String[] args = validacaoInicial(chave, quantiaMin, quantiaMax, prop);
		for (String arg : args)
			if (!new File(arg).exists())
				erroconfig(new IllegalArgumentException("A pasta \"" + arg + "\" registrado no argumento " + chave
						+ " do arquivo das configurações não existe!"));
	}

	private void validateNumerico(String chave, int min, int max, int quantiaMin, int quantiaMax, Properties prop) {
		String[] args = validacaoInicial(chave, quantiaMax, quantiaMax, prop);
		String mensagem = "Os elementos do argumento \"" + chave + "\" devem possuir valores numéricos entre " + min + " e "
				+ max + ".";
		for (String arg : args) {
			int valor = 0;
			try {
				valor = Integer.valueOf(arg);
			} catch (NumberFormatException n) {
				erroconfig(new IllegalStateException(mensagem));
			}
			if (valor < min || valor > max)
				erroconfig(new IllegalStateException(mensagem));
		}
	}
	
	private void validateBooleano(String chave,int quantiaMin,int quantiaMax,Properties prop) {
		String[] args=validacaoInicial(chave, quantiaMin, quantiaMax, prop);
		if(!(args[0].equals("true")||args[0].equals("false")))
			erroconfig(new IllegalStateException("Os elementos do argumento \""+chave+"\" devem ser \"true\" ou \"false\"."));
	}

	// Validando configurações atuais:
	private void valid_config(File config) throws IOException {
		Properties prop = new Properties();
		FileInputStream fin = new FileInputStream(config);
		prop.load(fin);
		fin.close();

		for (String chave : new String[] { "Pastas de imagem:", "Pastas de mundo:", "Pastas de plugins:" })
			validateArquivos(chave, 1, Integer.MAX_VALUE, prop);
		validateNumerico("Cor de fundo (RGBA):", 0, 255, 4, 4, prop);
		validateNumerico("FPS Maximo:", 0, Integer.MAX_VALUE, 1, 1, prop);
		validateBooleano("VSync ativado:", 1, 1, prop);
	}

	// Carregando configurações
	private void load_config(File config) throws IOException {
		FileInputStream fin = new FileInputStream(config);
		Properties prop = new Properties();
		prop.load(fin);
		GlobalVariables.vSync=prop.get("VSync ativado:").equals("true");
		GlobalVariables.imagem_pastas = getArgs(prop, "Pastas de imagem:");// Setando pasta de imagens
		GlobalVariables.mundos_pastas = getArgs(prop, "Pastas de mundo:");// Setando pasta-mundi
		GlobalVariables.plugins_pastas = getArgs(prop, "Pastas de plugins:");// Setando pasta de plugins
		String[] cores = getArgs(prop, "Cor de fundo (RGBA):");
		GlobalVariables.ClearColor = new float[] { // Setando cor de fundo
				Integer.valueOf(cores[0]) / 255, // RED
				Integer.valueOf(cores[1]) / 255, // GREEN
				Integer.valueOf(cores[2]) / 255, // BLUE
				Integer.valueOf(cores[3]) / 255// ALPHA
		};
		Janela.setFPS(Integer.valueOf(prop.getProperty("FPS Maximo:")));
	}

	// Enviar aviso de erro de configuração e fechar programa.
	private void erroconfig(Exception e) {
		e.printStackTrace();
		System.out.println("Conserte esse erro ou delete o config.properties para restaurar o padrão!");
		System.exit(0);
	}

	// Criar arquivo padrão de Vertex Shader
	private void vshader(File arq) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(arq));
		bw.write("#version 130\n" + "in vec3 verticesar; \n" + "in vec2 texturalura;\n"
				+ "out vec2 coordenadas_da_difamada_e_famosa_textura;\n" + "uniform mat4 projecao;\n" + "void main(){\n"
				+ "	coordenadas_da_difamada_e_famosa_textura=texturalura;\n"
				+ "	gl_Position=projecao*vec4(verticesar,0.5);\n" + "}");
		bw.close();
	}

	// Criar arquivo padrão de Fragment Shader
	private void fshader(File arq) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(arq));
		bw.write("#version 130\n" + "uniform sampler2D localizacao_da_textura_tambem_chamada_de_sampler;\n"
				+ "in vec2 coordenadas_da_difamada_e_famosa_textura;\n" + "void main(){\n"
				+ "	gl_FragColor=texture2D(localizacao_da_textura_tambem_chamada_de_sampler,coordenadas_da_difamada_e_famosa_textura);\n"
				+ "}");
		bw.close();
	}

	// Copiar imagem de dentro do JAR para a pasta externa
	private void defimagem(File arq, String nome) throws IOException  {
		arq.createNewFile();
		InputStream read = this.getClass().getClassLoader()
				.getResourceAsStream("com/firstJogo/imagens/" + nome +".png");//+ GlobalVariables.imagem_formato);
		OutputStream wri = new FileOutputStream(arq);
		IOUtils.copy(read, wri);// Copia de um InputStram para um OutputStream.
		read.close();
		wri.close();

	}

	// Define os tipos padrão de bloco
	private void tipospadrao(File arquivo) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo));
		bw.append("Mais um:\n");
		bw.append((short) 0 + "\n");
		bw.append("Grama\n");
		bw.close();
	}

}
