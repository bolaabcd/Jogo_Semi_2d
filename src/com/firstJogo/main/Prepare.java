package com.firstJogo.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.firstJogo.Mundos.Humano;
import com.firstJogo.estrutura.Camera;
import com.firstJogo.estrutura.Janela;
import com.firstJogo.estrutura.KeyHandler;
import com.firstJogo.utils.ArquivosGerais;
import com.firstJogo.utils.GlobalVariables;

//Thread responsável por preparar as variáveis iniciais do programa e ler as configurações.
public class Prepare implements Runnable{
	//Conjunto de argumentos presentes nas configurações.
	private static final Set<String> configs=new HashSet<String>(Arrays.asList(
			"Pasta de imagens:",
			"Formato das imagens:",//Apenas para avisar que SOMENTE png é aceito.
			"Pasta-mundi:",
			"Cor de fundo (RGBA):",
			"Mundos:",//TODO: futuro próximo
			"Pasta de plugins:",
			"Plugins:",//TODO: futuro pouco mais distante
			"FPS Maximo:"
			));
	
	
	//OUTRA THREAD: Prepara o objeto da câmera principal (do player):
	public static void prepararCamera() {
		Camera camera=new Camera(Janela.getPrincipal().getWidth(),Janela.getPrincipal().getHeight());
		Camera.setMain(camera);
	}
	//OUTRA THREAD: Define a entidade Humano que será o player:
	public static void prepararPlayer() {
		Humano player=new Humano();
		player.setPlayer(true);
		player.setMundopos(new float[] {0,0});
	}
	//OUTRA THREAD: Define a Janela do jogo:
	public static void prepararJanela() {
		Janela.setGeneralCallbacks();
		Janela.Iniciar();
		Janela.setPrincipal(new Janela("MicroCraft!",true));
		Janela.getPrincipal().contextualize();
		Janela.getPrincipal().setWindowPos(0.5f, 0.5f);
		Janela.Vsync(true);
	}
	
	
	
	@Override
	public void run() {
		try {		
			System.out.println("Analisando configurações!");
			
			
			
			//Lendo configurações:
			File config=new File("./config.txt");
			if(!config.exists())config_padrao(config);//Se não tiver configurações, ele cria o padrão.
			valid_config(config);//Verifica se as configurações são válidas
			load_config(config);//Carrega as configurações
			config=null;//Tira a referência das configurações da memória.
			
			
			
			//Perparando Shaders:
			File shaders=new File("./shaders/");
			if(!shaders.exists())shaders.mkdir();//Cria pasta shaders se ela não existir.
			shaders=new File("./shaders/sombra.vs");
			if(!shaders.exists())vshader(shaders);//Cria VertexShader se ele já não existir.
			shaders=new File("./shaders/sombra.fs");
			if(!shaders.exists())fshader(shaders);//Cria FragmentShader se ele já não existir.
			shaders=null;//Tira a referência ao arquivo de shaders da memória.
			
			
			
			//Preparando imagens iniciais:
			File defaultimage=new File(GlobalVariables.imagem_path);
			if(!defaultimage.exists())defaultimage.mkdir();//Cria pasta de imagens se já não existir.
			defaultimage=new File(GlobalVariables.imagem_path+"Grama"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"Grama");//Transfere a imagem da grama para a pasta externa.
			//Transferindo os frames das animações dos humanos andando para cima:
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoUp1"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoUp1");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoUp2"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoUp2");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoUp3"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoUp3");
			//Transferindo os frames das animações dos humanos andando para baixo:
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoDown1"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoDown1");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoDown2"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoDown2");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoDown3"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoDown3");
			//Transferindo os frames das animações dos humanos andando para esquerda:
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoLeft1"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoLeft1");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoLeft2"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoLeft2");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoLeft3"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoLeft3");
			//Transferindo os frames das animações dos humanos andando para direita:
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoRight1"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoRight1");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoRight2"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoRight2");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoRight3"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoRight3");
			defaultimage=null;//Tira a referência ao arquivo de imagens da memória
			
			
			
			//Cria o arquivo padrão para os tipos de bloco.
			File blocktypes=new File(GlobalVariables.mundos_pasta+"descricao_dos_blocos"+".txt");
			if(!blocktypes.exists())tipospadrao(blocktypes);
			blocktypes=null;
			
			KeyHandler.inicializar();
			
		} catch (IOException e) {//Erro ao manipular os arquivos
			System.out.println("ERRO COM OS ARQUIVOS!");
			e.printStackTrace();
			System.exit(0);
		} catch(NumberFormatException e) {//Erro ao ler valor numérico nas configurações
			erroconfig(new NumberFormatException("Erro ao ler valor numérico no arquivo de configurações!"));
		}
		
	}
	
	
	
	//Criando configurações padrão:
	private void config_padrao(File config)throws IOException{
		config.createNewFile();//Criando arquivo
		HashMap<String,String[]> args=new  HashMap<String,String[]>();//Criando HashMap de nome-argumentos
		args.put("Pasta de imagens:", new String[]{"./imgs/"});//Pasta de imagens padrão
		args.put("Formato das imagens:",new String[] {".png"});//Formato de imagens padrão (INUTILIZADO)
		args.put("Pasta-mundi:", new String[] {"./mundos/"});//Pasta de mundos padrão
		args.put("Cor de fundo (RGBA):", new String[] {"0","0","0","0"});//Cor de fundo padrão
		args.put("Mundos:",new String[] {"Default"});//Lista de mundos padrão
		args.put("Pasta de plugins:", new String[] {"./plugins/"});//Pasta de plugins padrão (INUTILIZADO)
		args.put("Plugins:", new String[] {"Default"});//Lista de plugins padrão (INUTILIZADO)
		args.put("FPS Maximo:", new String[] {"120"});//FPS Máximo Padrão
		
		ArquivosGerais.setArgs(config, args);//Escrevendo argumentos-padrão no arquivo de configurações.
		
		if(!new File("./imgs/").exists())new File("./imgs/").mkdir();//Criando pasta padrão de imagens
		if(!new File("./mundos/").exists())new File("./mundos/").mkdir();//Criando pasta padrão de mundos
		if(!new File("./plugins/").exists())new File("./plugins/").mkdir();//Criando pasta padrão de plugins
		if(!new File("./mundos/Default.world").exists())new File("./mundos/Default.world").createNewFile();//Criando mundo padrão (vazio)
		if(!new File("./plugins/Default.jar").exists())new File("./plugins/Default.jar").createNewFile();//Criando plugin padrão (INUTILIZADO)
	}
	
	
	
	//Validando configurações atuais:
	private void valid_config(File config)throws IOException,NumberFormatException {
		//Obtendo argumentos do arquivo de configurações:
		HashMap<String, String[]> args=ArquivosGerais.getArgs(config, configs);
		
		//Verificando pasta de imagens
		String[] argatual=args.get("Pasta de imagens:");
		if(argatual==null)erroconfig(new IllegalStateException("Argumento 'Pasta de imagens:' indisponível!"));
		if(argatual.length!=1)erroconfig(new IllegalArgumentException("Só é possível ter uma pasta de imagens! (erro no arugumento 'Pasta de imagens')"));
		if(!new File(argatual[0]).exists())
			erroconfig(new IllegalArgumentException("O local de imagens registrado no argumento 'Pasta de imagens' do arquivo das configurações não existe!"));
		
		//Verificando formato das imagens
		argatual=args.get("Formato das imagens:");
		if(argatual==null)erroconfig(new IllegalStateException("Argumento 'Formato das imagens:' indisponível!"));
		if(argatual.length!=1)erroconfig(new IllegalArgumentException("Apenas o formato .png é suportado no momento! Argumento 'Formato das imagens' do arquivo de configurações!"));
		if(!argatual[0].equals(".png"))erroconfig(new IllegalArgumentException("Apenas o formato .png é suportado no momento! Argumento 'Formato das imagens' do arquivo de configurações!"));
		
		//Verificando pasta de mundos
		argatual=args.get("Pasta-mundi:");
		if(argatual==null)erroconfig(new IllegalStateException("Argumento 'Pasta-mundi:' indisponível!"));
		if(argatual.length!=1)erroconfig(new IllegalArgumentException("Só é possível ter uma pasta de mundos! (erro no arugumento 'Pasta-mundi')"));
		if(!new File(argatual[0]).exists())
			erroconfig(new IllegalArgumentException("O local de mundos registrado no argumento 'Pasta-mundi' do arquivo das configurações não existe!"));
		String mundopath=argatual[0];
		
		//verificando pasta de plugins
		argatual=args.get("Pasta de plugins:");
		if(argatual==null)erroconfig(new IllegalStateException("Argumento 'Pasta de plugins' indisponível!"));
		if(argatual.length!=1)erroconfig(new IllegalArgumentException("Só é possível ter uma pasta de plugins! (erro no arugumento 'Pasta de plugins')"));
		if(!new File(argatual[0]).exists())
			erroconfig(new IllegalArgumentException("O local de plugins registrado no argumento 'Pasta de plugins' do arquivo das configurações não existe!"));
		String plugpath=argatual[0];
		
		//Verificando cor de fundo
		argatual=args.get("Cor de fundo (RGBA):");
		if(argatual==null)erroconfig(new IllegalStateException("Argumento 'Cor de fundo (RGBA):' indisponível!"));
		if(argatual.length!=4)erroconfig(new IllegalArgumentException("RGBA são 4 valores entre 0 e 255! (erro no argumento 'Cor de fundo (RGBA)')"));
		for(String arg:argatual)
			if(Integer.valueOf(arg)<0||Integer.valueOf(arg)>255)erroconfig(new IllegalArgumentException("Valor de cor RGBA inválido! (erro no argumento 'Cor de fundo (RGBA)')"));
		
		//Verificando FPS Máximo:
		argatual=args.get("FPS Maximo:");
		if(argatual==null)erroconfig(new IllegalStateException("Argumento 'FPS Maximo:' indisponível!"));
		if(argatual.length!=1)erroconfig(new IllegalArgumentException("FPS máximo é apenas 1 valor numérico! (erro no argumento 'FPS Maximo')"));
		if(Integer.valueOf(argatual[0])<0)erroconfig(new IllegalArgumentException("Valor de FPS inválido! (erro no argumento 'FPS Maximo')"));
		if(Integer.valueOf(argatual[0])>60)System.out.println("AVISO: Valores de FPS são limitados pela capacidade do seu monitor. Mesmo que coloque 5 000 FPS, se seu monitor não for capaz de apresentar tamanha quantidade de Frames Por Segundo, o jogo não atingirá esse valor de FPS.");
		
		//Verificando existência dos Mundos
		argatual=args.get("Mundos:");
		if(argatual==null)erroconfig(new IllegalStateException("Argumento 'Mundos:' indisponível!"));
		if(argatual.length<1)erroconfig(new IllegalArgumentException("É preciso ter pelo menos um arquivo de mundo! (erro no argumento 'Mundos')"));
		for(String arg:argatual)
			if(!(new File(mundopath+arg+".world")).exists())erroconfig(new IllegalArgumentException("Um mundo registrado no argumento 'Mundos' do arquivo de configurações não existe!"));
		
		//Verificando existência dos plugins
		argatual=args.get("Plugins:");
		if(argatual==null)erroconfig(new IllegalStateException("Argumento 'Plugins:' indisponível!"));
		if(argatual.length<1)erroconfig(new IllegalArgumentException("É preciso ter pelo menos um arquivo de plugin! (erro no argumento 'Plugins')"));
		for(String arg:argatual)
			if(!(new File(plugpath+arg+".jar")).exists())erroconfig(new IllegalArgumentException("Um plugin registrado no argumento 'Plugins' do arquivo de configurações não existe!"));
		
	}
	
	
	
	//Carregando configurações
	private void load_config(File config) throws IOException {
		HashMap<String, String[]> args=ArquivosGerais.getArgs(config, configs);//Obtendo argumentos do arquivo
		GlobalVariables.imagem_path=args.get("Pasta de imagens:")[0];//Setando pasta de imagens
		GlobalVariables.imagem_formato=args.get("Formato das imagens:")[0];//Setando formato das imagens
		GlobalVariables.mundos_pasta=args.get("Pasta-mundi:")[0];//Setando pasta-mundi
		GlobalVariables.plugins_pasta=args.get("Pasta de plugins:")[0];//Setando pasta de plugins
		String[] cores=args.get("Cor de fundo (RGBA):");
		GlobalVariables.ClearColor=new float[] {//Setando cor de fundo
				Integer.valueOf(cores[0])/255,//RED
				Integer.valueOf(cores[1])/255,//GREEN
				Integer.valueOf(cores[2])/255,//BLUE
				Integer.valueOf(cores[3])/255//ALPHA
				};
		Janela.setFPS(Integer.valueOf(args.get("FPS Maximo:")[0]));
		GlobalVariables.mundos=args.get("Mundos:");//Setando mundos
		GlobalVariables.plugins=args.get("Plugins:");//Setando plugins
	}


	
	//Enviar aviso de erro de configuração e fechar programa.
	private void erroconfig(Exception e) {
		e.printStackTrace();
		System.out.println("Conserte esse erro ou delete o config.txt para restaurar o padrão!");
		System.exit(0);
	}
	
	
	
	//Criar arquivo padrão de Vertex Shader
	private void vshader(File arq) throws IOException {
		BufferedWriter bw=new BufferedWriter(new FileWriter(arq));
		bw.write("#version 130\n" + 
				"in vec3 verticesar; \n" + 
				"in vec2 texturalura;\n" +  
				"out vec2 coordenadas_da_difamada_e_famosa_textura;\n" + 
				"uniform mat4 projecao;\n" + 
				"void main(){\n" + 
				"	coordenadas_da_difamada_e_famosa_textura=texturalura;\n" + 
				"	gl_Position=projecao*vec4(verticesar,0.5);\n" + 
				"}");
		bw.close();
	}
	
	
	
	//Criar arquivo padrão de Fragment Shader
	private void fshader(File arq) throws IOException {
		BufferedWriter bw=new BufferedWriter(new FileWriter(arq));
		bw.write("#version 130\n" + 
				"uniform sampler2D localizacao_da_textura_tambem_chamada_de_sampler;\n" + 
				"in vec2 coordenadas_da_difamada_e_famosa_textura;\n" + 
				"void main(){\n" + 
				"	gl_FragColor=texture2D(localizacao_da_textura_tambem_chamada_de_sampler,coordenadas_da_difamada_e_famosa_textura);\n" + 
				"}");
		bw.close();
	}
	
	
	
	//Copiar imagem de dentro do JAR para a pasta externa
	private void defimagem(File arq,String nome) throws IOException {
		arq.createNewFile();
		InputStream read=this.getClass().getClassLoader().getResourceAsStream("com/firstJogo/imagens/"+nome+GlobalVariables.imagem_formato);
		OutputStream wri=new FileOutputStream(arq);
		IOUtils.copy(read, wri);//Copia de um InputStram para um OutputStream.
		read.close();
		wri.close();
	}
	
	
	
	//Define os tipos padrão de bloco
	private void tipospadrao(File arquivo) throws IOException {
		BufferedWriter bw=new BufferedWriter(new FileWriter(arquivo));
		bw.append("Mais um:\n");
		bw.append((short)0+"\n");
		bw.append("Grama\n");
		bw.close();
	}
	
}


