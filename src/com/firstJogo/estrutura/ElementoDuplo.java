package com.firstJogo.estrutura;

public class ElementoDuplo <T1,T2>{
	public final T1 primeiro;
	public final T2 segundo;
	public final boolean temOrdem;
	public ElementoDuplo(T1 primeiro,T2 segundo, boolean temOrdem){
		this.primeiro=primeiro;
		this.segundo=segundo;
		this.temOrdem=temOrdem;
	}
	@Override
	public int hashCode() {
		return primeiro.hashCode()*segundo.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementoDuplo))return false;
//		try {
		@SuppressWarnings( "rawtypes" )
		ElementoDuplo el=(ElementoDuplo) obj;
		if(temOrdem)
			return el.primeiro.equals(primeiro)&&el.segundo.equals(segundo);
		else
				return (el.primeiro.equals(primeiro) && el.segundo.equals(segundo))
						|| (el.primeiro.equals(segundo) && el.segundo.equals(primeiro));
//		}catch(ClassCastException cce) {
//			return false;
//		}
	}
}
