package com.firstJogo.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArquivosGerais {
	public static String lertudo(String arquivo) {
		try {
			return new String(Files.readAllBytes(Paths.get(arquivo)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
