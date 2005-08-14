package com.mihail.clock;

import java.util.Observable;
import java.util.TimerTask;
import java.util.Timer;

/**
 * El funcionamiento de la clase Reloj se basa en el paradigma Modelo-Vista.
 * Reloj representa un objeto observable. Una aplicacion que quiera usar un
 * reloj ha de implementar la interfaz Observer, como explica la documentacion
 * de la superclase Observable.<br>
 * Reloj utiliza un Timer que, cada decima de segundo, decrementa el valor del
 * reloj. Se pueden utilizar recargas, si bien se realizan de forma asincrona,
 * estando obligado el observador a recargar el Reloj cuando lo desee. Presenta
 * dos estados, parado y en marcha. Estos estados nos permiten parar el Reloj
 * cuando sea el turno del oponente.<br>
 * El metodo Observer.update() sera llamado cada vez que haya un cambio de
 * estado en el Reloj, esto es, cada vez que se decrementa el tiempo. Es
 * responsabilidad del observador controlar el fin de tiempo.<br>
 * El manejo de la clase Reloj se realiza basicamente con tres metodos: comenzar
 * (), setParado (boolean b) y recargar ().<br>
 * Una vez instanciada la clase, se llamara a comenzar(). Esto hace que el Timer
 * comience a funcionar. El estado inicial del Reloj sera de parado. Para
 * ponerlo en marcha y pararlo sucesivamente, segun cambie el turno de juego en
 * la partida, usaremos setParado (boolean b). Cuando el observador desee
 * recargar el reloj, llamara a recarga (), sumandose en el reloj la cantidad
 * establecida previamente en el constructor o usando setRecarga(int recarga).<br>
 * 
 * @author Pedro Suárez Casal
 * @author Iago Porto Díaz
 * 
 * @see java.util.Observable
 * @see java.util.Observer
 * @see java.util.Timer
 */
public class Clock extends Observable {
	protected int horas, minutos, segundos, decimas;

	protected int recarga;

	protected boolean parado;

	protected static Timer tiempo = null;

	public Clock(int h, int m, int s) throws ClockException {
		parado = true;
		horas = h;
		minutos = m;
		segundos = s;
		if (h < 0)
			throw new ClockException("Horas fuera de rango");
		if (m < 0 || m > 59)
			throw new ClockException("Minutos fuera de rango");
		if (s < 0 || s > 59)
			throw new ClockException("Segundos fuera de rango");
		decimas = 0;
		recarga = 0;
	}

	public Clock(int h, int m, int s, int rec) throws ClockException {
		parado = true;
		horas = h;
		minutos = m;
		segundos = s;
		if (h < 0)
			throw new ClockException("Horas fuera de rango");
		if (m < 0 || m > 59)
			throw new ClockException("Minutos fuera de rango");
		if (s < 0 || s > 59)
			throw new ClockException("Segundos fuera de rango");
		decimas = 0;
		recarga = rec;
		if (rec < 0)
			throw new ClockException("Recarga fuera de rango");
	}

	public void comenzar() {
		if (tiempo == null) {
			tiempo = new Timer();
		}
		parado = false;
		tiempo.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				tareasRun();
			}
		}, 0, 100);
	}

	protected void tareasRun() {
		if (!parado) {
			if (decimas != 0) {
				decimas--;
			} else {
				if (segundos != 0) {
					segundos--;
				} else {
					if (minutos != 0) {
						minutos--;
					} else {
						horas--;
						minutos = 59;
					}
					segundos = 59;
				}
				decimas = 9;
			}
			// Notifica que se han producido cambios en el reloj.
			this.setChanged();
			// Avisa a sus observadores de que se han producido cambios. Este
			// metodo ya llama a clearChanged().
			this.notifyObservers();
			// // Sirve para que no aparezcan tiempos negativos en el
			// // reloj (por ejemplo, al acabarse el tiempo).
			// if (HORAS < 0 || MINUTOS < 0 || SEGUNDOS < 0) {
			// HORAS = 0;
			// MINUTOS = 0;
			// SEGUNDOS = 0;
			// DECIMAS = 0;
			// }
		}
	}

	public void setParado(boolean b) {
		parado = b;
	}

	public void setTiempo(int h, int m, int s, int d) throws ClockException {
		horas = h;
		if (h < 0)
			throw new ClockException("Horas fuera de rango");
		if (m < 0 || m > 59)
			throw new ClockException("Minutos fuera de rango");
		else
			minutos = m;
		if (s < 0 || s > 59)
			throw new ClockException("Segundos fuera de rango");
		else
			segundos = s;
		decimas = d;
	}

	public void recargar() {
		int simul, resto;
		simul = segundos + recarga;
		horas += simul / 3600;
		resto = simul % 3600;
		minutos += resto / 60;
		segundos = resto % 60;
	}

	public boolean isFinTiempo() {
		return horas < 0;
	}

	public void setRecarga(int segs) {
		recarga = segs;
	}

	public int getHoras() {
		return horas;
	}

	public int getMinutos() {
		return minutos;
	}

	public int getSegundos() {
		return segundos;
	}

	public int getDecimas() {
		return decimas;
	}
}
