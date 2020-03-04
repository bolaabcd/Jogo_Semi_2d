package com.firstJogo.visual;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private static Camera main;
	private Vector3f pos;
	private Matrix4f projec;
	private int width,height;
	
	
	
	public Camera(int width, int height) {
		this.width=width;
		this.height=height;
		pos=new Vector3f(0,0,0);
		projec=new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
		//Setando origem pro centro...
		Camera.setMain(this);
	}
	public void setPos(Vector3f pos) {
		this.pos=pos;
	}
	public Vector3f getPos() {
		return this.pos;
	}
	public Matrix4f getProjec() {
		return new Matrix4f().mul(projec.mul(new Matrix4f().setTranslation(pos)));
		//Multiplica pela posição antes de retornar!!!
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
	public static Camera getMain() {
		return main;
	}
	public static void setMain(Camera main) {
		Camera.main = main;
	}
}
