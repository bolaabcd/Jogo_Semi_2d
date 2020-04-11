package com.firstJogo.visual;

import java.util.HashMap;

import org.joml.Matrix4f;

import com.firstJogo.estrutura.Camera;
import com.firstJogo.utils.GlobalVariables;

public class AzRenderer {
	private HashMap<String, Textura> texturas;
	private static final Modelo quadrado = new Modelo(new float[] {
			0, 0, 0, // índice 0
			1.1f, 1.1f, 0, // índice 1
			1.1f, 0, 0, // índice 2
			0, 1.1f, 0,// índice 3
			// 1.1f É MUITO MELHOR PRA EVITAR AS LINHAS VERMELHAS ENTRE QUADRADOS!
	}, new int[] { // Pra só declarar os pontos uma vez!
			0, 1, 2, 
			0, 3, 1 
	}).addtex(new float[] { // Parece q a origem aqui é o topo da esquerda...
			0,    1.1f, 
			1.1f, 0, 
			1.1f, 1.1f, 
			0,    0,// Dá pra fazer MUITA maluquice com esses números aqui...
	}
			);

	public AzRenderer() {
		texturas = new HashMap<String, Textura>();
		for (TipodeBloco az : TipodeBloco.azulejos) {
			if (az != null)
				if (!texturas.containsKey(az.getTextura()))
					texturas.put(az.getTextura(), new Textura(
							GlobalVariables.imagem_path + az.getTextura() + GlobalVariables.imagem_formato));
		}
	}

	public void Renderizar(char id, char x, char y, Shaders shad, Matrix4f mundo, Camera cam) {
		shad.bindar();
		if (texturas.containsKey(TipodeBloco.azulejos[id].getTextura()))
			texturas.get(TipodeBloco.azulejos[id].getTextura()).bind(0);// Bindou ao sampler número 0 ¯\_(ツ)_/¯
		else
			throw new IllegalStateException("Textura de criatura inválida!");

		// Se trocar para 1 vira o humano !!?????!!
		Matrix4f pos = new Matrix4f().translate(2 * x, 2 * y, 0);// O modelo tem a origem no centro, e ele escala pros
																	// dois lados!
		Matrix4f mat = new Matrix4f();
		cam.getProjec().mul(mundo, mat);
		mat.mul(pos);

		shad.setUniforme("localizacao_da_textura_tambem_chamada_de_sampler", 0);
		// Setamos o sampler para 0, onde está a nossa textura de bloco!
																				
		shad.setUniforme("projecao", mat);

		quadrado.renderizar();
	}

}
