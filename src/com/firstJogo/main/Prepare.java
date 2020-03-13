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

import com.firstJogo.utils.ArquivosGerais;
import com.firstJogo.utils.GlobalVariables;

public class Prepare implements Runnable{
	private static final Set<String> configs=new HashSet<String>(Arrays.asList(
			"Pasta de imagens:",
			"Formato das imagens:",
			"Pasta-mundi:",
			"Cor de fundo (RGBA):",
			"Mundos:"
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
			defaultimage=new File(GlobalVariables.imagem_path+"Default"+GlobalVariables.imagem_formato);
			if(!defaultimage.exists())defimagem(defaultimage);
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
		
		

		
	}
	private void config_padrao(File config)throws IOException{
		config.createNewFile();
		 HashMap<String,String[]> args=new  HashMap<String,String[]>();
		 args.put("Pasta de imagens:", new String[]{"./imgs/"});
		 args.put("Formato das imagens:",new String[] {".png"});
		 args.put("Pasta-mundi:", new String[] {"./mundos/"});
		 args.put("Cor de fundo (RGBA):", new String[] {"0","0","0","0"});
		 args.put("Mundos:",new String[] {"Default"});
		ArquivosGerais.setArgs(config, args);
		if(!new File("./imgs/").exists())new File("./imgs/").mkdir();
		if(!new File("./mundos/").exists())new File("./mundos/").mkdir();
		if(!new File("./mundos/Default.world").exists())new File("./mundos/Default.world").createNewFile();
	}
//	private void config_padrao(File config) throws IOException {
//		config.createNewFile();
//		BufferedWriter escritor=new BufferedWriter(new FileWriter(config));
//		escritor.append("./imgs/\n");
//		escritor.append(".png\n");
//		escritor.append("./mundos/\n");
//		escritor.append("0\n");
//		escritor.append("0\n");
//		escritor.append("0\n");
//		escritor.append("0\n");
//		escritor.append("M\n");
//		escritor.append("Default");
//		escritor.close();
//		if(!new File("./imgs/").exists())new File("./imgs/").mkdir();
//		if(!new File("./mundos/").exists())new File("./mundos/").mkdir();
//		if(!new File("./mundos/Default.world").exists())new File("./mundos/Default.world").createNewFile();
//	}
	private void load_config(File config) throws IOException {
		HashMap<String, String[]> args=ArquivosGerais.getArgs(config, configs);
		GlobalVariables.imagem_path=args.get("Pasta de imagens:")[0];
		GlobalVariables.imagem_formato=args.get("Formato das imagens:")[0];
		GlobalVariables.mundos_pasta=args.get("Pasta-mundi:")[0];
		String[] cores=args.get("Cor de fundo (RGBA):");
		GlobalVariables.ClearColor=new float[] {Integer.valueOf(cores[0])/255,Integer.valueOf(cores[1])/255,Integer.valueOf(cores[2])/255,Integer.valueOf(cores[3])/255};
		GlobalVariables.mundos=args.get("Mundos:");
	}
//	private void load_config(File config) throws IOException {
//		BufferedReader br=new BufferedReader(new FileReader(config));
//		GlobalVariables.imagem_path=br.readLine();
//		GlobalVariables.imagem_formato=br.readLine();
//		GlobalVariables.mundos_pasta=br.readLine();
//		int r=Integer.valueOf(br.readLine());
//		int g=Integer.valueOf(br.readLine());
//		int b=Integer.valueOf(br.readLine());
//		int a=Integer.valueOf(br.readLine());
//		GlobalVariables.ClearColor=new float[] {r/255,g/255,b/255,a/255};
//		br.close();
//	}
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
		
		argatual=args.get("Cor de fundo (RGBA):");
		if(argatual==null)erroconfig("Argumento 'Cor de fundo (RGBA):' indisponível!");
		if(argatual.length!=4)erroconfig("RGBA são 4 valores entre 0 e 255! (erro no argumento 'Cor de fundo (RGBA)')");
		for(String arg:argatual)
			if(Integer.valueOf(arg)<0)erroconfig("Valor de cor RGBA inválido! (erro no argumento 'Cor de fundo (RGBA)')");
		
		argatual=args.get("Mundos:");
		if(argatual==null)erroconfig("Argumento 'Mundos:' indisponível!");
		if(argatual.length<1)erroconfig("É preciso ter pelo menos um arquivo de mundo! (erro no argumento 'Mundos')");
		for(String arg:argatual)
			if(!(new File(mundopath+arg+".world")).exists());
		
		
	}
//	private String[] valid_config(File config) throws IOException,NumberFormatException {
//		BufferedReader br=new BufferedReader(new FileReader(config));
//		int quantas=0;
//		String mundopath="";
//		String linha;
//		boolean hasmundos=false;
//		ArrayList<String> mundos=new ArrayList<String>();
//		while((linha=br.readLine())!=null) {
//			if(linha.equals("\n"))continue;
//			quantas++;
//			if(quantas==1&&!(new File(linha)).exists()) erroconfig("O local de imagens registrado na linha 1 do arquivo das configurações não existe!");
//			else if(quantas==2&&!linha.equals(".png")) erroconfig("Apenas o formato .png é suportado no momento! Linha 2 do arquivo de configurações!");
//			else if(quantas==3&&!(new File(linha)).exists())erroconfig("O local de mundos registrado na linha 3 do arquivo das configurações não existe!");
//			else if(quantas==4&&Integer.valueOf(linha)<0)erroconfig("Valor de cor R em RGBA inválido na linha 4!");
//			else if(quantas==5&&Integer.valueOf(linha)<0)erroconfig("Valor de cor G em RGBA inválido na linha 5!");
//			else if(quantas==6&&Integer.valueOf(linha)<0)erroconfig("Valor de cor B em RGBA inválido na linha 6!");
//			else if(quantas==7&&Integer.valueOf(linha)<0)erroconfig("Valor de cor A em RGBA inválido na linha 7!");
//			
//			else if(quantas>GlobalVariables.minimo_linhas_config-2&&linha.equals("M"))hasmundos=true;
//			else if(hasmundos&&!(new File(mundopath+linha+".world")).exists())erroconfig("O arquivo de mundo registrado na linha "+quantas+" do arquivo de configurações não existe!");
//			else if (hasmundos)mundos.add(linha);
//			if(quantas==3)mundopath=linha;
//		}
//		br.close();
//		
//		
//		if(quantas<GlobalVariables.minimo_linhas_config)erroconfig("O arquivo de configurações tem menos linhas que o mínimo necessário!");
//		return mundos.toArray(new String[0]);
//	}
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
				"	gl_Position=projecao*vec4(verticesar,1);\n" + 
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
	private void defimagem(File arq) throws IOException {
		File def=new File("com/firstJogo/main/Default.png");
		arq.createNewFile();
		//FileReader read=new FileReader(def);
		InputStream read=this.getClass().getClassLoader().getResourceAsStream("com/firstJogo/main/Default.png");
//		System.out.println("CAMINHO ABSOLUTO:");
//		System.out.println(def.getAbsolutePath());
//		System.out.println("CAMINHO RELATIVO:");
//		System.out.println(def.getPath());
//		System.out.println("URL CLASS:");
//		System.out.println(this.getClass().getResource(def.getPath()));
//		System.out.println("URL CLASS_LOADER:");
//		System.out.println(this.getClass().getClassLoader().getResource("com/firstJogo/main/Default.png"));
//		System.out.println("INPUTSTREAM CLASS_LOADER:");
//		System.out.println(read);
		//InputStream read=this.getClass().getResourceAsStream("/"+def.getPath());
		//FileOutputStream wri=new FileOutputStream(arq);
		OutputStream wri=new FileOutputStream(arq);
		//read.transferTo(wri);
		IOUtils.copy(read, wri);
		read.close();
		wri.close();
	}
	
	private void tipospadrao(File arquivo) throws IOException {
		BufferedWriter bw=new BufferedWriter(new FileWriter(arquivo));
		bw.append("Mais um:\n");
		bw.append((short)0+"\n");
		bw.append("Default\n");
		bw.append("Mais um:\n");
		bw.append((short)1+"\n");
		bw.append("Grama\n");
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


