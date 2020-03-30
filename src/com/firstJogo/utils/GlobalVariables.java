package com.firstJogo.utils;

import java.util.concurrent.CopyOnWriteArraySet;

public class GlobalVariables {
public static float tam=0;
public static String imagem_path="./imgs/";
public static String imagem_formato=".png";
public static String mundos_pasta="./mundos/";
public static String[] mundos;
public static String plugins_pasta="./plugins/";
public static String[] plugins;
public static float[] ClearColor=new float[] {1f,0f,0f,0f};

public static boolean debugue=false;

public static CopyOnWriteArraySet<Integer> Keys=new CopyOnWriteArraySet<Integer>();

public static final double raizde2inv=1/Math.sqrt(2);
public static final short intperbloco=32;


}
