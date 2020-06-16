package com.firstJogo.elementosJogo;

import org.joml.Matrix4f;
import org.joml.Vector3f;

//Câmera virtual que pode ser movida pelo mundo.
public class Camera{
	
	private Vector3f pos;
	private Matrix4f projec;
	private int width,height;//Tamanho e comprimento do que a câmera apresenta
	//(o quanto do mundo ela apresenta, EM INTS!)
	
	
	public Camera(int width, int height) {
		this.width=width;
		this.height=height;
		pos=new Vector3f(0,0,0);
		projec=new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
		//Setando origem da câmera pro centro da tela...
	}

	public void setPos(Vector3f pos) {
		this.pos=pos;
	}

	public Vector3f getPos() {
		return this.pos;
	}
	//Obtendo projeção da câmera, já com a posição imbutida, para uso de renderização.
	public Matrix4f getProjec() {
		Matrix4f mat=new Matrix4f();
		projec.mul(new Matrix4f().setTranslation(pos),mat);
		return mat;
	}
	//Obtendo projeção "crua" da câmera, sem considerar a posição da câmera no mundo, só na tela
	public Matrix4f getRawProjec() {
		return projec;
	}
	public void setSize(int width, int height) {
		this.width=width;
		this.height=height;
		projec=new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Camera getCopia() {
		Camera res=new Camera(width,height);
		float x=pos.x,y=pos.y,z=pos.z;
		res.setPos(new Vector3f(x,y,z));
		
		
		return res;
	}
}
