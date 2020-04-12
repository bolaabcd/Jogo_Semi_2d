package com.firstJogo.utils;

//Variáveis globais a serem acessadas por qualquer classe de qualquer lugar.
public class GlobalVariables {
	public static String imagem_path = "./imgs/";//Pasta de imagens
	public static String imagem_formato = ".png";//Formato de imagens
	public static String mundos_pasta = "./mundos/";//Pasta de mundos
	public static String[] mundos;//Lista dos nomes de mundos
	public static String plugins_pasta = "./plugins/";//Pasta de plugins
	public static String[] plugins;//Lista dos nomes de plugins
	public static float[] ClearColor = new float[] { 1f, 0f, 0f, 0f };//Cor de limpeza da tela

	public static int TicksPorSegundo = 0;//Quantidade de Ticks Por Segundo realizados

	public static int contador = 0;//Contador global para fins de desenvolvimento

	public static boolean debugue = false;//Modo "debug" para fins de desenvolvimento e análise

	public static final short intperbloco = 30;//Constante de ints por bloco (cada +30 na câmera ou na posição de alguma entidade é +1 bloco).

}
