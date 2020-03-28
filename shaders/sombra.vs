#version 130

//attribute vec3 verticesar; //vec3 significa que é um vetor de 3 pontos-FLOAT!
//attribute vec2 texturalura;

in vec3 verticesar; 
in vec2 texturalura;

//varying vec2 coordenadas_da_difamada_e_famosa_textura;

out vec2 coordenadas_da_difamada_e_famosa_textura;

uniform mat4 projecao;

void main(){
	coordenadas_da_difamada_e_famosa_textura=texturalura;
	gl_Position=projecao*vec4(verticesar,0.5);
	
}