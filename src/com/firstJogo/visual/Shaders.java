package com.firstJogo.visual;


import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Shaders {
	private int programa;
	private int vertex_shader;//Processa os vertices
	private int fragment_shader;//Processa os pixels mesmo aparentemente (cores)
	
	public Shaders(String arquivosemestencao) {
		programa=GL20.glCreateProgram();
		
		//Arquivo termina com .vs (ou .fs)
		try {
		vertex_shader=GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vertex_shader, new String(Files.readAllBytes(Paths.get(arquivosemestencao+".vs"))));
		GL20.glCompileShader(vertex_shader);
		if(GL20.glGetShaderi(vertex_shader, GL20.GL_COMPILE_STATUS)!=1) {
			System.err.println(GL20.glGetShaderInfoLog(vertex_shader));
			System.exit(1);
		}
		
		fragment_shader=GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fragment_shader, new String(Files.readAllBytes(Paths.get(arquivosemestencao+".fs"))));
		GL20.glCompileShader(fragment_shader);
		if(GL20.glGetShaderi(fragment_shader, GL20.GL_COMPILE_STATUS)==0) {
			System.err.println(GL20.glGetShaderInfoLog(fragment_shader));
			System.exit(1);
		}
		}catch(IOException ioe) {
			System.out.println("Não foi possível acessar os Shaders!");
			ioe.printStackTrace();
			System.exit(0);
		}
		
		GL20.glAttachShader(programa, vertex_shader);
		GL20.glAttachShader(programa, fragment_shader);
		
		GL30.glGetAttribLocation(programa, "verticesar");
		
		GL30.glGetAttribLocation(programa, "texturalura");
		
		GL20.glLinkProgram(programa);
		
		if(GL20.glGetProgrami(programa, GL20.GL_LINK_STATUS)!=1) {
			System.err.println(GL20.glGetProgramInfoLog(programa));
			System.exit(1);
		}
		
		GL20.glValidateProgram(programa);
		
		if(GL20.glGetProgrami(programa, GL20.GL_VALIDATE_STATUS)!=1) {
			System.err.println(GL20.glGetProgramInfoLog(programa));
			System.exit(1);
		}
	}
	public void bindar() {
		GL20.glUseProgram(programa);
	}
	public void setUniforme(String qual, int valor){
		int local=GL20.glGetUniformLocation(programa, qual);
		if(local!=-1) GL20.glUniform1i(local, valor);
		else {
			System.out.println("ERRO INESPERADO! 1");
			System.err.println(GL20.glGetProgramInfoLog(programa));
			System.exit(1);
		}
	}
	public void setUniforme(String qual, float valor){
		int local=GL20.glGetUniformLocation(programa, qual);
		if(local!=-1) GL20.glUniform1f(local, valor);
		else {
			System.out.println("ERRO INESPERADO! 2");
			System.err.println(GL20.glGetProgramInfoLog(programa));
			System.exit(1);
		}
	}
	public void setUniforme(String qual, Matrix4f valor){
		int local=GL20.glGetUniformLocation(programa, qual);
		FloatBuffer buf=BufferUtils.createFloatBuffer(4*4);
		valor.get(buf);
		if(local!=-1) GL20.glUniformMatrix4fv(local, false, buf);
		//Setar esse transpose de false pra true mudou muitas coisas
		else {
			System.out.println("ERRO INESPERADO! 3");
			System.err.println(GL20.glGetProgramInfoLog(programa));
			System.exit(1);
		}
	}
}
