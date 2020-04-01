//package com.firstJogo.Mundos;
//
//
//import org.joml.Matrix4f;
//
//import com.firstJogo.visual.Camera;
//import com.firstJogo.visual.Shaders;
////import com.firstJogo.visual.TipodeCriatura;
//
//public class CrRenderer {
//
//	public CrRenderer() {
//
//	}
////	public void Renderizar(String tipo,float x,float y,char z, Shaders shad, Matrix4f mundo, Camera cam) {
////		shad.bindar();
//////		objeto_a_renderizar ob=visual_das_criaturas.get(TipodeCriatura.criaturas[id].getTextura());
//////		objeto_a_renderizar ob=new objeto_a_renderizar();;
////		if(criaturas_a_renderizar.containsKey(tipo)) {
////			Entidade ob=criaturas_a_renderizar.get(tipo);
////			ob.getTextura().bind(1);//Bindou ao sampler número 1 ¯\_(ツ)_/¯
////			Matrix4f pos=new Matrix4f().translate(2*x,2*y,z);//O modelo tem a origem no centro, e ele escala pros dois lados!
////			Matrix4f mat=new Matrix4f();
////			cam.getRawProjec().mul(mundo, mat);
////			mat.mul(pos);
////					
////			shad.setUniforme("localizacao_da_textura_tambem_chamada_de_sampler", 1);//Setamos o sampler para 0, onde está a nossa textura!
////			shad.setUniforme("projecao", mat);
////			
////			ob.getModelo().renderizar();
////		}else 
////			throw new IllegalStateException("Textura de criatura inválida!");
////		
////		
////	}
//	
//}
