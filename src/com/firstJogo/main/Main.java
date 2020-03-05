package com.firstJogo.main;


public class Main {
	public static void main(String[] args) {
		Thread prep=new Thread(new Prepare());
		Thread rend=new Thread(new Renderer());
		try {
			prep.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		prep.start();
		rend.start();
		
	}
	
}
