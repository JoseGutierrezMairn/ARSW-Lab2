package edu.eci.arsw.primefinder;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Main extends JPanel {
	
	public Main(PrimeFinderThread ft, PrimeFinderThread pft, PrimeFinderThread pt) {
		KeyListener listener = new MyKeyListener(pft, ft, pt);
		addKeyListener(listener);
		setFocusable(true);
	}

	public static void main(String[] args) {
		

		PrimeFinderThread pft=new PrimeFinderThread(0, 10000000);
		PrimeFinderThread ft=new PrimeFinderThread(10000001, 20000000);
		PrimeFinderThread pt=new PrimeFinderThread(20000001, 30000000);
		/***TimedTaskJose tk =  new TimedTaskJose(pft, ft, pt);
		Timer t =  new Timer();
		t.schedule(tk,  1000);
		Main m = new Main(pft, ft, pt);
		JFrame frame = new JFrame("");
		frame.add(m);
		frame.setSize(1, 1);
		frame.setVisible(true);***/
		pft.start();
		ft.start();
		pt.start();
		
	}
}
class TimedTaskJose extends TimerTask{
	PrimeFinderThread uno;
	PrimeFinderThread dos;
	PrimeFinderThread tres;
	
	public TimedTaskJose(PrimeFinderThread uno, PrimeFinderThread dos, PrimeFinderThread tres) {
		this.uno= uno;
		this.dos= dos;
		this.tres= tres;
	}

	@Override
	public void run() {
		int answ = 0;
		answ+= uno.getPrimes().size() + dos.getPrimes().size() + tres.getPrimes().size();
		uno.setWait(true);
		dos.setWait(true);
		tres.setWait(true);
		System.out.println("Total de números primos encontrados hasta el momento: " + answ);
	}
	
}

class MyKeyListener implements KeyListener{
	PrimeFinderThread uno;
	PrimeFinderThread dos;
	PrimeFinderThread tres;
	public MyKeyListener(PrimeFinderThread ft, PrimeFinderThread pft, PrimeFinderThread pt) {
		uno = ft;
		dos = pt;
		tres = pft;
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(KeyEvent.getKeyText(e.getKeyCode()) == "Enter") {
			uno.setWait(false);
			dos.setWait(false);
			tres.setWait(false);
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
