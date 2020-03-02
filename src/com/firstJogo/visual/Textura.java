package com.firstJogo.visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Textura {
private int id,width,height;
public Textura(String fileNome) {
	BufferedImage bi;
	try {
		bi=ImageIO.read(new File(fileNome));
		width=bi.getWidth();
		height=bi.getHeight();
		int[] prgba=new int[width*height*4];
		prgba=bi.getRGB(0, 0, width, height, null, 0, width);
		ByteBuffer pixels=BufferUtils.createByteBuffer(width*height*4);
		for(int c=0;c<width;c++) {
			for(int a=0;a<height;a++) {
				//System.out.println((prgba[c*width+a]&0x0000FF00)>>8);
				//RED,GREEN,BLUE,ALPHA!
				pixels.put((byte) ((prgba[c*width+a]&0x00FF0000)>>16));
				pixels.put((byte) ((prgba[c*width+a]&0x0000FF00)>>8));
				pixels.put((byte) ((prgba[c*width+a]&0x000000FF)));
				pixels.put((byte) ((prgba[c*width+a]&0xFF000000)>>>24));
			}
		}
/*
 * Acima utilizou o >>> porque em Java, o >> completa com 0 se 
 * o número for positivo e com 1 se o número for negativo. Exem
 * plo: 10... >> 01.... (caso seja positivo), e 10... >> 11...
 * (Caso seja negativo).
 *
 * Pra saber se é positivo ou negativo é só olhar o 1 bit, apar
 * entemente...
 */
				pixels.flip();
/*
 * Esse flip passa o buffer do "modo escrever" para o "modo 
 * ler"
 */
				id=GL11.glGenTextures();//Gera 1 textura e o id
				
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
				
				
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST );
/*
 * Aparentemente esse "nearest" faz o pixel mais próximo ser
 * entregue (quando pede o pixel que está na posição 0.3, por 
 * exemplo). Se setasse para "linear" ele ia fazer uma média
 * dos pixels mais próximos e (aparentemente) ia fazer uma
 * imagem meio borrada!
 * Parece que esse "min" é pra caso a imagem tenha que ser me
 * nor do que ela é.
 */
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_NEAREST );	
/*
 *Aparentemente é a mesma coisa do "MIN", só que pra quando a
 *imagem tiver que ser ampliada, "magnificação". 
 */
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,  GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);

				/*
 * TODO mudar "level" pra outro valor e ver se fica borrado,//Só some
 * TODO tentar mudar "border" pra ver no que dá.
 * TODO tentar mudar o internal_format pra ver se é escolhível.//Só o 1 é escolhível
 * TODO tentar mudar de byte pra unsigned byte.//é unsigned
 */
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}catch(IOException i) {
			i.printStackTrace();
		}
	}

public Textura(String fileNome,int filtromin,int filtromag) {
	BufferedImage bi;
	try {
		bi=ImageIO.read(new File(fileNome));
		width=bi.getWidth();
		height=bi.getHeight();
		int[] prgba=new int[width*height*4];
		prgba=bi.getRGB(0, 0, width, height, null, 0, width);
		ByteBuffer pixels=BufferUtils.createByteBuffer(width*height*4);
		for(int c=0;c<width;c++) {
			for(int a=0;a<height;a++) {
				//System.out.println((prgba[c*width+a]&0x0000FF00)>>8);
				//RED,GREEN,BLUE,ALPHA!
				pixels.put((byte) ((prgba[c*width+a]&0x00FF0000)>>16));
				pixels.put((byte) ((prgba[c*width+a]&0x0000FF00)>>8));
				pixels.put((byte) ((prgba[c*width+a]&0x000000FF)));
				pixels.put((byte) ((prgba[c*width+a]&0xFF000000)>>>24));
			}
		}

				pixels.flip();

				id=GL11.glGenTextures();
				
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
				
				
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,filtromin );

				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,filtromag );	

				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,  GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}catch(IOException i) {
			i.printStackTrace();
		}
	}

	public void bind(int witchsampler) {
		if(witchsampler>32||witchsampler<0) {
			{
				System.err.println("Sampler fora dos limites!");
				System.exit(1);
			}
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0+witchsampler);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
//Aparentemente registra essa textura!
	}
}
