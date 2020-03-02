package com.firstJogo.visual;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private Vector3f pos;
	private Matrix4f projec;
	
	public Camera(int width, int height) {
		pos=new Vector3f(0,0,0);
		projec=new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
		//Setando origem pro centro...
		
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
	
}
