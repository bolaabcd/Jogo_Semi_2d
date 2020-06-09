package com.firstJogo.visual;

import java.util.HashMap;

import org.joml.Matrix4f;

import com.firstJogo.estrutura.Camera;
import com.firstJogo.utils.GlobalVariables;

public class AzRenderer {
	private HashMap<String, Textura> texturas;

	public AzRenderer() {
		texturas = new HashMap<String, Textura>();
		for (TipodeBloco az : TipodeBloco.azulejos) {
			if (az != null)
				if (!texturas.containsKey(az.getTextura()))
					texturas.put(az.getTextura(), new Textura(
							GlobalVariables.imagem_path + az.getTextura() + GlobalVariables.imagem_formato));
		}
	}

	public void Renderizar(char id, long x, long y, Shaders shad, Matrix4f mundo, Camera cam) {
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

		Textura.modeloPadrao.renderizar();
	}

}
