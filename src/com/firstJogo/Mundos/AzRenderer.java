package com.firstJogo.Mundos;

import java.util.HashMap;

import org.joml.Matrix4f;

import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.TipodeAzulejo;
import com.firstJogo.visual.Camera;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Shaders;
import com.firstJogo.visual.Textura;

public class AzRenderer {
//public static AzRenderer main;
private HashMap<String,Textura> texturas;
private static final Modelo quadrado=new Modelo(
		new float[] {
//                -1,-1,0,//índice 0
//                 1, 1,0,//índice 1
//                 1,-1,0,//índice 2
//                -1, 1,0,//índice 3
//				-0.5f,-0.5f,0,//índice 0
//				 0.5f, 0.5f,0,//índice 1
//                 0.5f,-0.5f,0,//índice 2
//                -0.5f, 0.5f,0,//índice 3
				0,0,0,//índice 0
                1,1,0,//índice 1
                1,0,0,//índice 2
                0, 1,0,//índice 3
                 
		},
		new int[] {//Pra só declarar os pontos uma vez!
				0,1,2,
				0,3,1
		}
		).addtex(
				new float[] {//Parece q a origem aqui é o topo da esquerda...
//						0,1,
//						1,0,
//						1,1,
//						0,0,//Dá pra fazer MUITA maluquice com esses números aqui...
						0,1,
						1,0,
						1,1,
						0,0,//Dá pra fazer MUITA maluquice com esses números aqui...
				}
//				new float[] {//Se a origem fosse embaixo na direita seria isso daqui...
//						0,0,
//						1,1,
//						1,0,
//						0,1,//Dá pra fazer MUITA maluquice com esses números aqui...
//				}
				);
	
public AzRenderer() {
//	main=this;
	texturas=new HashMap<String,Textura>();
	for(TipodeAzulejo az:TipodeAzulejo.azulejos) {
//		System.out.println(GlobalVariables.imagem_path+az.getTextura()+GlobalVariables.imagem_formato);
		if(az!=null)	
			if(!texturas.containsKey(az.getTextura()))
				texturas.put(az.getTextura(), new Textura(GlobalVariables.imagem_path+az.getTextura()+GlobalVariables.imagem_formato));
	}
}

public void Renderizar(char id,char x,char y, Shaders shad, Matrix4f mundo,Camera cam) {
	shad.bindar();
	if(texturas.containsKey(TipodeAzulejo.azulejos[id].getTextura())) 
		texturas.get(TipodeAzulejo.azulejos[id].getTextura()).bind(0);//Bindou ao sampler número 0 ¯\_(ツ)_/¯
	//Se trocar para 1 vira o humano !!?????!!
	//Matrix4f pos=new Matrix4f().translate(new Vector3f(x*2,y*2,0)); TODO ORIGINAL
	Matrix4f pos=new Matrix4f().translate(2*x,2*y,0);//O modelo tem a origem no centro, e ele escala pros dois lados!
	//ERA PRA SER 2x e 2y!!!
//	System.out.println(pos);
	if(GlobalVariables.debugue==true)System.out.println("MATRIZ DE POSIÇÃO:");
	if(GlobalVariables.debugue==true)System.out.println(pos);
	if(GlobalVariables.debugue==true)System.out.println("MATRIZ DE CÂMERA:");
	if(GlobalVariables.debugue==true)System.out.println(cam.getProjec());
	if(GlobalVariables.debugue==true)System.out.println("MATRIZ DE MUNDO:");
	if(GlobalVariables.debugue==true)System.out.println(mundo);//Ortho
	Matrix4f mat=new Matrix4f();
	cam.getProjec().mul(mundo, mat);
	mat.mul(pos);
	
	shad.setUniforme("localizacao_da_textura_tambem_chamada_de_sampler", 0);//Setamos o sampler para 0, onde está a nossa textura!
	shad.setUniforme("projecao", mat);

	
	quadrado.renderizar();
}

}
