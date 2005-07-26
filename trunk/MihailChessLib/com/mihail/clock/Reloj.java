package com.mihail.clock;

import javax.swing.JPanel;

import com.mihail.chess.Logica.Bando;

/**
 * @author Pedro Suárez Casal
 * @author Iago Porto Díaz
 */
public abstract class Reloj extends JPanel {
	protected int HORAS, MINUTOS, SEGUNDOS, DECIMAS;
	protected int recarga;
	protected static java.util.Timer tiempo = null;
	protected Bando bando;
}
