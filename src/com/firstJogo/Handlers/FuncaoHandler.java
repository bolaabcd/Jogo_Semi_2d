//package com.firstJogo.utils;
//
//public class FuncaoHandler {
//	private final Funcao<Object> funcao;
//	private Object argumento;
//	public FuncaoHandler(Funcao<Object> func,Object arg,Object ifNulo) {
//		this.funcao=func;
//		if(arg==null)this.argumento=ifNulo;
//		else if(arg instanceof Object[]) {
//			Object[] e=(Object[])arg;
//			
//			for(int i= 1;i<(int)e[0];i++)
//				if(e[i]==null)
//					e[i]=ifNulo;//Acho que ia dar errado enhanced for pq ia alterar o endereço do elemento sob o scope do for apenas!
//			
//			this.argumento=arg;
//		}
//		else this.argumento=arg;
//	}
//	public void setArgumento(Object argumento) {
//		this.argumento=argumento;
//	}
//	public void run() {
//		funcao.run(argumento);
//	}
//}
package com.firstJogo.Handlers;

import com.firstJogo.utils.Funcao;

public class FuncaoHandler<V> {
	private final Funcao<V> funcao;
	private V argumento;
//	public FuncaoHandler(Funcao<V> func,V arg,V ifNulo) {
//		this.funcao=func;
//		if(arg==null)this.argumento=ifNulo;
//		else if(arg instanceof Object[]) {
//			Object[] e=(Object[])arg;
//			
//			for(int i= 1;i<(int)e[0];i++)
//				if(e[i]==null)
//					e[i]=ifNulo;//Acho que ia dar errado enhanced for pq ia alterar o endereço do elemento sob o scope do for apenas!
//			
//			this.argumento=arg;
//		}
//		else this.argumento=arg;
////		this.argumento=arg;
//	}
	public FuncaoHandler(Funcao<V> func,V arg) {
		this.funcao=func;
//		if(arg==null)this.argumento=ifNulo;
//		else if(arg instanceof Object[]) {
//			Object[] e=(Object[])arg;
//			
//			for(int i= 1;i<(int)e[0];i++)
//				if(e[i]==null)
//					e[i]=ifNulo;//Acho que ia dar errado enhanced for pq ia alterar o endereço do elemento sob o scope do for apenas!
//			
//			this.argumento=arg;
//		}
//		else this.argumento=arg;
		this.argumento=arg;
	}
	public void setArgumento(V argumento) {
		this.argumento=argumento;
	}
	public void run() {
		funcao.run(argumento);
	}
	
	
	public void run(V argumento) {
		funcao.run(argumento);
	}
}
