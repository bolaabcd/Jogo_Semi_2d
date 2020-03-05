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

public class ArquivosGerais {
	public static String lertudo(String arquivo) {
		try {
			return new String(Files.readAllBytes(Paths.get(arquivo)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static HashMap<String,String[]> getArgs(File arquivo, Set<String> chaves) throws IOException{
		HashMap<String,String[]> ans=new HashMap<String,String[]>();
		BufferedReader br=new BufferedReader(new FileReader(arquivo));
		String linha;
		String chavatual="NO_KEYS";
		ArrayList<String> argsatual=new ArrayList<String>();
		while((linha=br.readLine())!=null) {
			if(chaves.contains(linha)) {
				ans.put(chavatual, argsatual.toArray(new String[0]));
				argsatual=new ArrayList<String>();
				chavatual=linha;
				continue;
			}
			argsatual.add(linha);
		}
		ans.put(chavatual, argsatual.toArray(new String[0]));//COLOCANDO ULTIMO ARGUMENTO!
		br.close();
		return ans;
	}
	public static void setArgs(File arquivo,HashMap<String,String[]> args) throws IOException {
		BufferedWriter bw=new BufferedWriter(new FileWriter(arquivo));
		Set<String> keys=args.keySet();
		for(String key:keys) {
			bw.append(key+"\n");
			for(String val:args.get(key))bw.append(val+"\n");
		}
		bw.close();
	}
	public static void addArgs(File arquivo,HashMap<String,String[]> args) throws IOException {
		BufferedWriter bw=new BufferedWriter(new FileWriter(arquivo,true));
		Set<String> keys=args.keySet();
		for(String key:keys) {
			bw.append(key+"\n");
			for(String val:args.get(key))bw.append(val+"\n");
		}
		bw.close();
	}
}
