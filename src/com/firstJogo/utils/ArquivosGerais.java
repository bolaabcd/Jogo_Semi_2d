package com.firstJogo.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

//Classe para maniúlação geral de arquivos
public class ArquivosGerais {
	//Ler o arquivo inteiro na forma de string
	public static String lertudo(String arquivo) {
		try {
			return new String(Files.readAllBytes(Paths.get(arquivo)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//Obter argumentos do arquivo a partir do método usado aqui (dividir o arquivo em argumentos e seus identificadores)
	public static HashMap<String,String[]> getArgs(File arquivo, Set<String> chaves) throws IOException{
		HashMap<String,String[]> ans=new HashMap<String,String[]>();
		BufferedReader br=new BufferedReader(new FileReader(arquivo));//Leitor do arquivo
		String linha;
		String chavatual="NO_KEYS";//Chave padrão do início do arquivo, de quando ainda não tem nenhum identificador.
		ArrayList<String> argsatual=new ArrayList<String>();
		while((linha=br.readLine())!=null) {//Ler linha por linha do arquivo
			if(chaves.contains(linha)) {//Se a linha é uma nova chave identificadora de uma nova aba de argumentos
				ans.put(chavatual, argsatual.toArray(new String[0]));//Adicionar a chave anterior e seus argumentos ao resultado.
				argsatual=new ArrayList<String>();//Reseta os argumentos atuais
				chavatual=linha;//Reseta a chave atual
				continue;
			}
			argsatual.add(linha);//Adiciona os argumentos da chave atual
		}
		ans.put(chavatual, argsatual.toArray(new String[0]));//Coloca o último argumento (depois de ter terminado de ler todas as linhas, sai do while loop)
		br.close();//Fecha o Leitor
		return ans;
	}
	//Escrever um arquivo a partir de argumentos e seus identificadores.
	public static void setArgs(File arquivo,HashMap<String,String[]> args) throws IOException {
		BufferedWriter bw=new BufferedWriter(new FileWriter(arquivo));//Abre no modo OverWrite
		Set<String> keys=args.keySet();
		for(String key:keys) {//Pra cada chave identificadora
			bw.append(key+"\n");//Adiciona a chave em uma nova linha
			for(String val:args.get(key))//Pra cada argumento que está sob a chave identificadora
				bw.append(val+"\n");//Adiciona o argumento numa nova linha.
		}
		bw.close();//Fecha o escritor de arquivo.
	}
	//Escrever novos argumentos em um arquivo
	public static void addArgs(File arquivo,HashMap<String,String[]> args) throws IOException {
		BufferedWriter bw=new BufferedWriter(new FileWriter(arquivo,true));//Abre no modo Append
		Set<String> keys=args.keySet();
		for(String key:keys) {//Pra cada chave identificadora
			bw.append(key+"\n");//Adiciona a chave em uma nova linha
			for(String val:args.get(key))//Pra cada argumento que está sob a chave identificadora
				bw.append(val+"\n");//Adiciona o argumento numa nova linha.
		}
		bw.close();//Fecha o escritor de arquivo.
	}
}
