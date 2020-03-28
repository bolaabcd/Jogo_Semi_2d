package com.firstJogo.Mundos;

import java.util.HashMap;

import org.joml.Matrix4f;

import com.firstJogo.utils.GlobalVariables;
import com.firstJogo.visual.Camera;
import com.firstJogo.visual.Modelo;
import com.firstJogo.visual.Shaders;
import com.firstJogo.visual.Textura;
import com.firstJogo.visual.TipodeCriatura;

public class CrRenderer {
	private class objeto_a_renderizar {
		private Textura tex;
		private Modelo mod;
		private objeto_a_renderizar(Textura tex, Modelo mod) {
			this.tex=tex;
			this.mod=mod;
		}
	}
	private HashMap<String,objeto_a_renderizar> visual_das_criaturas;
//	private HashMap<String,Modelo> modelos;
	public CrRenderer() {
		visual_das_criaturas=new HashMap<String,objeto_a_renderizar>();
//		modelos=new HashMap<String,Modelo>();
		for(TipodeCriatura cr:TipodeCriatura.criaturas) {
			if(cr!=null) {
				if(!visual_das_criaturas.containsKey(cr.getTextura())) {
					Textura tex=new Textura(GlobalVariables.imagem_path+cr.getTextura()+GlobalVariables.imagem_formato);
					visual_das_criaturas.put(cr.getTextura(), new objeto_a_renderizar(tex,tex.genModelo()));
//					texturas.put(cr.getTextura(), new Textura(GlobalVariables.imagem_path+cr.getTextura()+GlobalVariables.imagem_formato));
				}
//					if(!modelos.containsKey(cr.getTextura()))
//					modelos.put(cr.getTextura()), cr.getTextura());
			}
		}
		
	}
	public void Renderizar(char id,float x,float y,char z, Shaders shad, Matrix4f mundo, Camera cam) {
		shad.bindar();
		objeto_a_renderizar ob=visual_das_criaturas.get(TipodeCriatura.criaturas[id].getTextura());
		if(visual_das_criaturas.containsKey(TipodeCriatura.criaturas[id].getTextura())) 
			ob.tex.bind(0);//Bindou ao sampler número 0 ¯\_(ツ)_/¯
		Matrix4f pos=new Matrix4f().translate(2*x,2*y,z);//O modelo tem a origem no centro, e ele escala pros dois lados!
		Matrix4f mat=new Matrix4f();
		cam.getRawProjec().mul(mundo, mat);
		mat.mul(pos);
				
		shad.setUniforme("localizacao_da_textura_tambem_chamada_de_sampler", 0);//Setamos o sampler para 0, onde está a nossa textura!
		shad.setUniforme("projecao", mat);
		
		ob.mod.renderizar();
	}
}
