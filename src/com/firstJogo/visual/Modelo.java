package com.firstJogo.visual;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class Modelo {
	private int contagem;
	private int id_vertices;
	private int id_texturas;
	private int id_indices;
	private Vector2f[] vertices;
	
	public Modelo(float[] vertices,int[] indices) {
		Vector2f ponto1=new Vector2f (vertices[0],vertices[1]);
		Vector2f ponto2=new Vector2f (vertices[3],vertices[4]);
		Vector2f ponto3=new Vector2f (vertices[6],vertices[7]);
		Vector2f ponto4=new Vector2f (vertices[9],vertices[10]);
		this.vertices=new Vector2f[]{//Esse é o padrão, pra simplificar.
			ponto1,//00
			ponto4,//01
			ponto2,//11
			ponto3,//10
		};
		contagem=indices.length;
		
		id_vertices=GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id_vertices);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buf(vertices), GL15.GL_STATIC_DRAW);
/*
 * O último parâmetro é chamado de "usage", e pode ser tanto estático quanto variável.
 * Estático geralmente é usado quando não vai ficar alterando, e dinâmico quando vai
 * mudar o valor enquanto o negócio roda. Aparentemente dá pra setar se vai modificar
 * diretamente na variável ou se vai modificar a partir do GL ???? Tendi mt bem n.
 * Pergunta: ver oq acontece se mudar esse DRAW pra READ ou COPY.//Nada mudou
 */
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);//Unbinding
		
		id_indices=GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id_indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buf(indices),GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	public void renderizar() {
		GL20.glEnable(GL20.GL_BLEND);
		GL20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		GL20.glEnableVertexAttribArray(0);//Pq o shader foi bindado ao 0!!!
		GL20.glEnableVertexAttribArray(1);//Aparentemente os valores da textura foram bindados aqui!
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id_vertices);
		GL20.glVertexAttribPointer(0, 3, GL20.GL_FLOAT, false, 0, 0);
		//Local 0, tamanho 3(XYZ), tipo float, normalizar false, passo/stride 0,
		//pointer 0
		
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id_texturas);
		GL20.glVertexAttribPointer(1, 2, GL20.GL_FLOAT, false, 0, 0);
		//Aparentemnete esse camarada é o apontador de atributos usado pra textura...
		
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id_indices);
		GL15.glDrawElements(GL15.GL_TRIANGLES, contagem,GL15.GL_UNSIGNED_INT,0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL20.glDisable(GL20.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);//DESATIVANDO ARRAY DE TEXTURAS!!!
	}
	public Modelo addtex(float[] pontos) {
		id_texturas=GL15.glGenBuffers();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id_texturas);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf(pontos), GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return this;
		
	}
	public Vector2f[] getVertices() {//Começa no baixo esquerda, sentido horário.
		return vertices;
	}
	private FloatBuffer buf(float[] valores) {
		FloatBuffer fb= BufferUtils.createFloatBuffer(valores.length);
		fb.put(valores);
		fb.flip();
		return fb;
	}
	private IntBuffer buf(int[] valores) {
		IntBuffer ib= BufferUtils.createIntBuffer(valores.length);
		ib.put(valores);
		ib.flip();
		return ib;
	}
}
