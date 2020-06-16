package com.firstJogo.estrutura;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.firstJogo.Mundos.MundoCarregado;
import com.firstJogo.utils.GlobalVariables;

public class CollisionDetector {
	
	public static ArrayList<Vector2i> getColidBlocos(Vector2f mundoPos,MundoCarregado mundo, Vector2f[] hitBox) {//Obter os blocos que ele pode estar colidindo
//		float deslocx=GlobalVariables.intperbloco*(hitboxPos[2][0]-hitboxPos[0][0])/2;
//		float deslocy=GlobalVariables.intperbloco*(hitboxPos[2][1]-hitboxPos[0][1])/2;
		float deslocx=GlobalVariables.intperbloco*(hitBox[2].x-hitBox[0].x)/2;
		float deslocy=GlobalVariables.intperbloco*(hitBox[2].y-hitBox[0].y)/2;
		float xi=(mundoPos.x-deslocx)/GlobalVariables.intperbloco;
		float yi=(mundoPos.y-deslocy)/GlobalVariables.intperbloco;
		float xf=(mundoPos.x+deslocx)/GlobalVariables.intperbloco;
		float yf=(mundoPos.y+deslocy)/GlobalVariables.intperbloco;
		int xmax=(int) Math.floor(Math.abs(2*deslocx/GlobalVariables.intperbloco)+1);//Arredondando pra cima quantos blocos a entidade ocupa.
		int ymax=(int) Math.floor(Math.abs(2*deslocy/GlobalVariables.intperbloco)+1);//Tá meio incorreto isso aqui...
		ArrayList<Vector2i> coords=new ArrayList<Vector2i>();
		Vector2i blocoCoords=MundoCarregado.getBlocoCoords(mundoPos);
		for(int ix=-1-xmax/2;ix<xmax/2+1;ix++)
			for(int iy=-1-ymax/2;iy<ymax/2+1;iy++)
//		for(int ix=-xmax/2;ix<xmax/2;ix++)
//			for(int iy=-ymax/2;iy<ymax/2;iy++z
//		int ix=0;
//		int iy=0;
//		System.out.println("X: "+blocoCoords.x);
//		System.out.println("Y: "+blocoCoords.y);
				if(
//						colide(xi, yi, xf, yf, getBlocoCoords()[0]+ix, getBlocoCoords()[1]+iy,getBlocoCoords()[0]+ix+1, getBlocoCoords()[1]+iy+1)
						colide(xi, yi, xf, yf, blocoCoords.x+ix, blocoCoords.y+iy,blocoCoords.x+ix+1f, blocoCoords.y+iy+1f)
						)
					coords.add(new Vector2i (
							blocoCoords.x+ix,
							blocoCoords.y+iy
				));
		return coords;
	}
	
	private static boolean colide(float xi,float yi,float xf,float yf,float xi2,float yi2,float xf2,float yf2) {
		if(
				dentrode(xi,yi,xf,yf,xi2,yi2)||dentrode(xi,yi,xf,yf,xf2,yf2)
				||
				dentrode(xi,yi,xf,yf,xi2,yf2)||dentrode(xi,yi,xf,yf,xf2,yi2)
				||
				dentrode(xi2,yi2,xf2,yf2,xi,yi)||dentrode(xi2,yi2,xf2,yf2,xf,yf)
				||
				dentrode(xi2,yi2,xf2,yf2,xf,yi)||dentrode(xi2,yi2,xf2,yf2,xi,yf)
				)return true;
		return false;
	}
	
	private static boolean dentrode(float xi,float yi,float xf,float yf,float x, float y) {
		if(x>xi&&x<xf&&y>yi&&y<yf)return true;
		return false;
	}
}
