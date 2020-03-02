#version 130

uniform sampler2D localizacao_da_textura_tambem_chamada_de_sampler;

//varying vec2 coordenadas_da_difamada_e_famosa_textura;
in vec2 coordenadas_da_difamada_e_famosa_textura;


void main(){
	//gl_FragColor=vec4(0.5,green,0.5,0.5);
	gl_FragColor=texture2D(localizacao_da_textura_tambem_chamada_de_sampler,coordenadas_da_difamada_e_famosa_textura);
	
}