package com.firstJogo.elementosJogo;

public class NotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException(String msg) {
		super(msg);
	} 
	public NotFoundException(RuntimeException rEx) {
		super(rEx);
	} 
}
