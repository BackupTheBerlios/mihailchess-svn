package com.mihail.test;

import java.util.Observable;
import java.util.Observer;

import com.mihail.clock.Clock;
import com.mihail.clock.ClockException;

public class RelojTest implements Observer {

	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof Clock) {
			Clock r = (Clock) arg0;
			if (r.getTenths() == 0) {
				System.out.println(r.getHours() + ":" + r.getMinutes() + ":"
						+ r.getSeconds() + "." + r.getTenths());
			}
			if (r.isTimeFinished())
				System.out.println("FIN TIEMPO");
		}
	}

	public static void main(String args[]) {
		Clock reloj = null;
		RelojTest relojTest = null;
		try {
			reloj = new Clock(0, 0, 10);
		} catch (ClockException e) {
		}
		relojTest = new RelojTest();

		reloj.addObserver(relojTest);
		reloj.start();
		reloj.setStopped(false);
	}
}
