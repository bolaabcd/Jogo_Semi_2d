package com.firstJogo.utils;

public class FuncaoHandler {
	private final Funcao<Object> funcao;
	private final Object argumento;
	public FuncaoHandler(Funcao<Object> func,Object arg,Object ifNulo) {
		this.funcao=func;
		if(arg==null)this.argumento=ifNulo;
		else if(arg instanceof Object[]) {
			Object[] e=(Object[])arg;
			
			for(int i= 1;i<(int)e[0];i++)
				if(e[i]==null)
					e[i]=ifNulo;//Acho que ia dar errado enhanced for pq ia alterar o endereço do elemento sob o scope do for apenas!
			
			this.argumento=arg;
		}
		else this.argumento=arg;
	}
	public void run() {
		funcao.run(argumento);
	}
}
