package com.firstJogo.main;

import com.firstJogo.utils.GlobalVariables;

public class Main {
	public static void main(String[] args) {
		if(args!=null)if(args.length>0)if(args[0].equals("debug"))GlobalVariables.debugue=true;
		Thread prep=new Thread(new Prepare());
		Thread rend=new Thread(new Renderer());
		Thread ev=new Thread(new GeradorEventos());
		GeradorEventos.main=ev;
		Renderer.main=rend;
		prep.start();
		try {
			prep.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		rend.start();
		ev.start();
//		System.out.println("Prioridade ev:"+ev.getPriority());
		
	}
	
}
