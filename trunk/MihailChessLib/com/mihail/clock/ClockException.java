package com.mihail.clock;

/**
 * Esta clase define una RelojException, que son las excepciones que se lanzan
 * cuando hay algun problema con los relojes.
 * 
 * @author Pedro Suarez Casal
 * @author Iago Porto Diaz
 * 
 */
public class ClockException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Construye una RelojException con el mensaje de detalle especificado.
	 * 
	 * @param s
	 *            Mensaje que detalla la excepcion.
	 */
	public ClockException(String s) {
		super(s);
	}
}