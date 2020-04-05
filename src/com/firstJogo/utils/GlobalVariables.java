package com.firstJogo.utils;

import java.util.concurrent.CopyOnWriteArraySet;

public class GlobalVariables {
public static String imagem_path="./imgs/";
public static String imagem_formato=".png";
public static String mundos_pasta="./mundos/";
public static String[] mundos;
public static String plugins_pasta="./plugins/";
public static String[] plugins;
public static float[] ClearColor=new float[] {1f,0f,0f,0f};

public static int TicksPorSegundo=0;

<<<<<<< HEAD
public static int contador=0;

=======
>>>>>>> parent of 2b495ee... Corrigido bug de animação das texturas, e bug de 'janela não respondendo' no Windows.
public static boolean debugue=false;

public static CopyOnWriteArraySet<Integer> Keys=new CopyOnWriteArraySet<Integer>();

public static final short intperbloco=32;


}
