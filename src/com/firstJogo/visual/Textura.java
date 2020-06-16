package com.firstJogo.visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import com.firstJogo.utils.GlobalVariables;

public class Textura {
	public static final Modelo modeloPadrao=new Modelo(new float[] {
			0,0,0,//índice 0
            1,1,0,//índice 1
            1,0,0,//índice 2
            0,1,0,//índice 3
//		  0,0,0,//índice 0
//          0.5f,0,0,//índice 1
//          (float) (1+Math.cos(60*Math.PI/180))/2,(float)Math.sin(60*Math.PI/180)/2,0,//índice 2
//          0.5f, (float)Math.sin(60*Math.PI/180),0,//índice 3
//		  0, (float)Math.sin(60*Math.PI/180),0,//índice 4
//          -(float) (Math.cos(60*Math.PI/180))/2,(float)Math.sin(60*Math.PI/180)/2,0,//índice 5
	},
	new int[] {//Pra só declarar os pontos uma vez!
			0,1,2,
			0,3,1
//			0,1,2,
//			2,3,4,
//			4,5,0,
//			0,2,4,
	}
	).addtex(
			new float[] {//Parece q a origem aqui é o topo da esquerda...
					0,1,
					1,0,
					1,1,
					0,0,//Dá pra fazer MUITA maluquice com esses números aqui...
//					(float)(Math.cos(60*Math.PI/180))/2,(float)Math.sin(60*Math.PI/180),
//					(float)(Math.cos(60*Math.PI/180)+1)/2,(float)Math.sin(60*Math.PI/180),
//					(float)(2*Math.cos(60*Math.PI/180)+1)/2,(float)Math.sin(60*Math.PI/180)/2,
//					(float)(Math.cos(60*Math.PI/180)+1)/2,0,
//					(float)(Math.cos(60*Math.PI/180))/2,0,
//					0,(float)Math.sin(60*Math.PI/180)/2,

					
			}
			);

	private int id;//, width, height;

	public Textura(int id) {
		this.id=id;
	}
	
	public Textura(String fileNome) {
		this(fileNome, true, true);
	}

	public Textura(String fileNome, boolean filtroMinNearest, boolean filtroMagNearest) {
		BufferedImage bi;
		int filtroMin = filtroMinNearest ? GL13.GL_NEAREST : GL13.GL_LINEAR;
		int filtroMag = filtroMinNearest ? GL13.GL_NEAREST : GL13.GL_LINEAR;
		try {
			bi = ImageIO.read(new File(fileNome));
			if (GlobalVariables.debugue == true)
				System.out.println("COMPRIMENTO DA TEXTURA:");
			int width = bi.getWidth();
			if (GlobalVariables.debugue == true)
				System.out.println(width);
			int height = bi.getHeight();
			if (GlobalVariables.debugue == true)
				System.out.println("ALTURA DA TEXTURA:");
			if (GlobalVariables.debugue == true)
				System.out.println(height);
			int[] prgba = new int[width * height * 4];
			prgba = bi.getRGB(0, 0, width, height, null, 0, width);
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			if (GlobalVariables.debugue == true)
				System.out.println();
			if (GlobalVariables.debugue == true)
				System.out.println();
			if (GlobalVariables.debugue == true)
				System.out.println("INICIANDO CONTAGEM DOS PIXELS:");
			for (int c = 0; c < width; c++) {
				for (int a = 0; a < height; a++) {
					// RED,GREEN,BLUE,ALPHA!
					if (GlobalVariables.debugue == true)
						System.out.println("COR DO PIXEL EM x=" + a + ", y=" + c);
					if (GlobalVariables.debugue == true)
						System.out.println("RED=" + ((prgba[c * height + a] & 0x00FF0000) >> 16));
					if (GlobalVariables.debugue == true)
						System.out.println("GREEN=" + ((prgba[c * height + a] & 0x0000FF00) >> 8));
					if (GlobalVariables.debugue == true)
						System.out.println("BLUE=" + ((prgba[c * height + a] & 0x000000FF)));
					if (GlobalVariables.debugue == true)
						System.out.println("ALPHA=" + ((prgba[c * height + a] & 0xFF000000) >>> 24));
					pixels.put((byte) ((prgba[c * height + a] & 0x00FF0000) >> 16));
					pixels.put((byte) ((prgba[c * height + a] & 0x0000FF00) >> 8));
					pixels.put((byte) ((prgba[c * height + a] & 0x000000FF)));
					pixels.put((byte) ((prgba[c * height + a] & 0xFF000000) >>> 24));
				}
			}
			if (GlobalVariables.debugue == true)
				System.out.println("FINALIZADA CONTAGEM DOS PIXELS!");
			if (GlobalVariables.debugue == true)
				System.out.println();
			if (GlobalVariables.debugue == true)
				System.out.println();
			/*
			 * Acima utilizou o >>> porque em Java, o >> completa com 0 se o número for
			 * positivo e com 1 se o número for negativo. Exem plo: 10... >> 01.... (caso
			 * seja positivo), e 10... >> 11... (Caso seja negativo).
			 *
			 * Pra saber se é positivo ou negativo é só olhar o 1 bit, apar entemente...
			 */
			pixels.flip();
			/*
			 * Esse flip passa o buffer do "modo escrever" para o "modo ler"
			 */
			id = GL13.glGenTextures();// Gera 1 textura e o id

			GL13.glBindTexture(GL13.GL_TEXTURE_2D, id);

			GL13.glTexParameterf(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MIN_FILTER, filtroMin);
			/*
			 * Aparentemente esse "nearest" faz o pixel mais próximo ser entregue (quando
			 * pede o pixel que está na posição 0.3, por exemplo). Se setasse para "linear"
			 * ele ia fazer uma média dos pixels mais próximos e (aparentemente) ia fazer
			 * uma imagem meio borrada! Parece que esse "min" é pra caso a imagem tenha que
			 * ser me nor do que ela é.
			 */
			GL13.glTexParameterf(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MAG_FILTER, filtroMag);

			/*
			 * Aparentemente é a mesma coisa do "MIN", só que pra quando a imagem tiver que
			 * ser ampliada, "magnificação".
			 */

			GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_WRAP_R, GL13.GL_CLAMP_TO_EDGE);
			GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_EDGE);
			GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_EDGE);
//Acima seta-se o modo de não repetência das texturas caso elas sejam colocadas em áreas maiores que o que podem ocupar. Facilita visualizar alguns erros.

			GL13.glTexImage2D(GL13.GL_TEXTURE_2D, 0, GL13.GL_RGBA, width, height, 0, GL13.GL_RGBA,
					GL13.GL_UNSIGNED_BYTE, pixels);

			/*
			 * Mudar "level" (o primeiro 0) pra outro valor faz sumir... Tentar mudar
			 * "border" (o segundo 0) faz a textura desaparecer em valores fora de 0 e 1, e
			 * 1 tira um pixel da borda ¯\_(ツ)_/¯ Só o 1 valor de formato (RGBA,RGB e etc) é
			 * escolhível. Se trocar de Unsigned_Byte pra Byte as cores trocam todas
			 */
			GL13.glBindTexture(GL13.GL_TEXTURE_2D, 0);
		} catch (IOException i) {
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
		
		GL13.glBindTexture(GL13.GL_TEXTURE_2D, id);
//Aparentemente registra essa textura!
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	}
