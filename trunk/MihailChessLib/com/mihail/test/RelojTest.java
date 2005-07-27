package com.mihail.test;

import java.util.Observable;
import java.util.Observer;

import com.mihail.clock.Reloj;
import com.mihail.clock.RelojException;

public class RelojTest implements Observer {

	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof Reloj) {
			Reloj r = (Reloj) arg0;
			if (r.getDecimas() == 0) {
				System.out.println(r.getHoras() + ":" + r.getMinutos() + ":"
						+ r.getSegundos() + "." + r.getDecimas());
			}
			if (r.isFinTiempo())
				System.out.println("FIN TIEMPO");
		}
	}

	public static void main(String args[]) {
		Reloj reloj = null;
		RelojTest relojTest = null;
		try {
			reloj = new Reloj(0, 0, 10);
		} catch (RelojException e) {
		}
		relojTest = new RelojTest();

		reloj.addObserver(relojTest);
		reloj.comenzar();
		reloj.setParado(false);
	}
}
