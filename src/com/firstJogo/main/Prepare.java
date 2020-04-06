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
import com.firstJogo.utils.ArquivosGerais;
import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.Camera;

public class Prepare implements Runnable{
	private static final Set<String> configs=new HashSet<String>(Arrays.asList(
			"Pasta de imagens:",
			"Formato das imagens:",
			"Pasta-mundi:",
			"Cor de fundo (RGBA):",
			"Mundos:",
			"Pasta de plugins:",
			"Plugins:"
			));
	@Override
	public void run() {
		try {
			
			System.out.println("Analisando configurações!");
			
			File config=new File("./config.txt");
			if(!config.exists())config_padrao(config);
			valid_config(config);
			load_config(config);
			config=null;
			
			
			File shaders=new File("./shaders/");
			if(!shaders.exists())shaders.mkdir();
			
			shaders=new File("./shaders/sombra.vs");
			if(!shaders.exists())vshader(shaders);
			
			shaders=new File("./shaders/sombra.fs");
			if(!shaders.exists())fshader(shaders);
			shaders=null;
			
			File defaultimage=new File(GlobalVariables.imagem_path);
			if(!defaultimage.exists())defaultimage.mkdir();
			defaultimage=new File(GlobalVariables.imagem_path+"Grama"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"Grama");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoUp1"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoUp1");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoUp2"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoUp2");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoUp3"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoUp3");
			
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoDown1"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoDown1");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoDown2"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoDown2");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoDown3"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoDown3");
			
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoLeft1"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoLeft1");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoLeft2"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoLeft2");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoLeft3"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoLeft3");
			
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoRight1"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoRight1");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoRight2"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoRight2");
			defaultimage=new File(GlobalVariables.imagem_path+"HumanoRight3"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage,"HumanoRight3");
			defaultimage=null;
			
			
			
			File blocktypes=new File(GlobalVariables.mundos_pasta+"descricao_dos_blocos"+".txt");
			if(!blocktypes.exists())tipospadrao(blocktypes);
			blocktypes=null;
		} catch (IOException e) {
			System.out.println("ERRO COM OS ARQUIVOS!");
			e.printStackTrace();
		} catch(NumberFormatException e) {
			e.printStackTrace();
			erroconfig("Erro ao ler valor de cor no arquivo de configurações!");
		}
		
		//TUDO ISSO SERÁ PREPARADO NA THREAD DE RENDERIZAÇÃO!
//		prepararJanela();
//
//		CallbacksExternas.prepararBotoes();//EXTERNALIZAR!
//		
//		prepararPlayer();
//		
//		prepararCamera();
		
	}
	public static void prepararCamera() {
		Camera camera=new Camera(Janela.getPrincipal().getWidth(),Janela.getPrincipal().getHeight());
		Camera.setMain(camera);
	}
	public static void prepararPlayer() {
//		Humano player=new Humano(TipodeCriatura.criaturas[0]);
		Humano player=new Humano();
		player.setPlayer(true);
		player.setMundopos(new long[] {0,0});
	}
	
	public static void prepararJanela() {
		Janela.setGeneralCallbacks();
		Janela.Iniciar();
		Janela.setPrincipal(new Janela("MicroCraft!",true));
		Janela.getPrincipal().contextualize();
		Janela.getPrincipal().setWindowPos(0.5f, 0.5f);
		Janela.Vsync(true);
	}
	private void config_padrao(File config)throws IOException{
		config.createNewFile();
		 HashMap<String,String[]> args=new  HashMap<String,String[]>();
		 args.put("Pasta de imagens:", new String[]{"./imgs/"});
		 args.put("Formato das imagens:",new String[] {".png"});
		 args.put("Pasta-mundi:", new String[] {"./mundos/"});
		 args.put("Cor de fundo (RGBA):", new String[] {"0","0","0","0"});
		 args.put("Mundos:",new String[] {"Default"});
		 args.put("Pasta de plugins:", new String[] {"./plugins/"});
		 args.put("Plugins:", new String[] {"Default"});
			 
		ArquivosGerais.setArgs(config, args);
		if(!new File("./imgs/").exists())new File("./imgs/").mkdir();
		if(!new File("./mundos/").exists())new File("./mundos/").mkdir();
		if(!new File("./plugins/").exists())new File("./plugins/").mkdir();
		if(!new File("./mundos/Default.world").exists())new File("./mundos/Default.world").createNewFile();
		if(!new File("./plugins/Default.jar").exists())new File("./plugins/Default.jar").createNewFile();
	}
	
	private void load_config(File config) throws IOException {
		HashMap<String, String[]> args=ArquivosGerais.getArgs(config, configs);
		GlobalVariables.imagem_path=args.get("Pasta de imagens:")[0];
		GlobalVariables.imagem_formato=args.get("Formato das imagens:")[0];
		GlobalVariables.mundos_pasta=args.get("Pasta-mundi:")[0];
		GlobalVariables.plugins_pasta=args.get("Pasta de plugins:")[0];
		String[] cores=args.get("Cor de fundo (RGBA):");
		GlobalVariables.ClearColor=new float[] {Integer.valueOf(cores[0])/255,Integer.valueOf(cores[1])/255,Integer.valueOf(cores[2])/255,Integer.valueOf(cores[3])/255};
		GlobalVariables.mundos=args.get("Mundos:");
		GlobalVariables.plugins=args.get("Plugins:");
	}

	private void valid_config(File config)throws IOException,NumberFormatException {
		HashMap<String, String[]> args=ArquivosGerais.getArgs(config, configs);
		
		String[] argatual=args.get("Pasta de imagens:");
		if(argatual==null)erroconfig("Argumento 'Pasta de imagens:' indisponível!");
		if(argatual.length!=1)erroconfig("Só é possível ter uma pasta de imagens! (erro no arugumento 'Pasta de imagens')");
		if(!new File(argatual[0]).exists())
			erroconfig("O local de imagens registrado no argumento 'Pasta de imagens' do arquivo das configurações não existe!");
		
		argatual=args.get("Formato das imagens:");
		if(argatual==null)erroconfig("Argumento 'Formato das imagens:' indisponível!");
		if(argatual.length!=1)erroconfig("Apenas o formato .png é suportado no momento! Argumento 'Formato das imagens' do arquivo de configurações!");
		if(!argatual[0].equals(".png"))erroconfig("Apenas o formato .png é suportado no momento! Argumento 'Formato das imagens' do arquivo de configurações!");
		
		argatual=args.get("Pasta-mundi:");
		if(argatual==null)erroconfig("Argumento 'Pasta-mundi:' indisponível!");
		if(argatual.length!=1)erroconfig("Só é possível ter uma pasta de mundos! (erro no arugumento 'Pasta-mundi')");
		if(!new File(argatual[0]).exists())
			erroconfig("O local de mundos registrado no argumento 'Pasta-mundi' do arquivo das configurações não existe!");
		String mundopath=argatual[0];
		
		argatual=args.get("Pasta de plugins:");
		if(argatual==null)erroconfig("Argumento 'Pasta de plugins' indisponível!");
		if(argatual.length!=1)erroconfig("Só é possível ter uma pasta de plugins! (erro no arugumento 'Pasta de plugins')");
		if(!new File(argatual[0]).exists())
			erroconfig("O local de plugins registrado no argumento 'Pasta de plugins' do arquivo das configurações não existe!");
		String plugpath=argatual[0];
			
		
		argatual=args.get("Cor de fundo (RGBA):");
		if(argatual==null)erroconfig("Argumento 'Cor de fundo (RGBA):' indisponível!");
		if(argatual.length!=4)erroconfig("RGBA são 4 valores entre 0 e 255! (erro no argumento 'Cor de fundo (RGBA)')");
		for(String arg:argatual)
			if(Integer.valueOf(arg)<0)erroconfig("Valor de cor RGBA inválido! (erro no argumento 'Cor de fundo (RGBA)')");
		
		argatual=args.get("Mundos:");
		if(argatual==null)erroconfig("Argumento 'Mundos:' indisponível!");
		if(argatual.length<1)erroconfig("É preciso ter pelo menos um arquivo de mundo! (erro no argumento 'Mundos')");
		for(String arg:argatual)
			if(!(new File(mundopath+arg+".world")).exists())erroconfig("Um mundo registrado no argumento 'Mundos' do arquivo de configurações não existe!");
		
		argatual=args.get("Plugins:");
		if(argatual==null)erroconfig("Argumento 'Plugins:' indisponível!");
		if(argatual.length<1)erroconfig("É preciso ter pelo menos um arquivo de plugin! (erro no argumento 'Plugins')");
		for(String arg:argatual)
			if(!(new File(plugpath+arg+".jar")).exists())erroconfig("Um plugin registrado no argumento 'Plugins' do arquivo de configurações não existe!");
		
		
	}

	private void erroconfig(String mess) {
		System.out.println(mess);
		System.out.println("Conserte esse erro ou delete o config.txt para restaurar o padrão!");
		System.exit(1);
	}
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
	private void defimagem(File arq,String nome) throws IOException {
		arq.createNewFile();
		InputStream read=this.getClass().getClassLoader().getResourceAsStream("com/firstJogo/imagens/"+nome+GlobalVariables.imagem_formato);
		OutputStream wri=new FileOutputStream(arq);
		IOUtils.copy(read, wri);
		read.close();
		wri.close();
	}
	
	private void tipospadrao(File arquivo) throws IOException {
		BufferedWriter bw=new BufferedWriter(new FileWriter(arquivo));
		bw.append("Mais um:\n");
		bw.append((short)0+"\n");
		bw.append("Grama\n");
//		bw.append("Mais um:\n");
//		bw.append((short)1+"\n");
//		bw.append("Grama\n");
		bw.close();
	}
	
}
/*
 * ----------Arquivo de configuração:
 * 1)Pasta de imagens
 * 2)Formato das imagens
 * 3)Pasta-mundi
 * 4)ClearColor RED
 * 5)ClearColor GREEN
 * 6)ClearColor BLUE
 * 7)ClearColor ALPHA
 * 
 * A partir da linha de letra M ele começa a listar os caminhos dos mundos!
 * nome-do-mundo
 * 
 * 
 */


