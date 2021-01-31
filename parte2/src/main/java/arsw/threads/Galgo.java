package arsw.threads;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;





/**
 * Un galgo que puede correr en un carril
 * 
 * @author rlopez
 * 
 */
public class Galgo extends Thread {
	private int paso;
	private Carril carril;
	private Semaphore mutex;
	private RegistroAvanceTotal rat;
	RegistroLlegada regl;


	public Galgo(Carril carril, String name, RegistroLlegada reg, Semaphore mutex, RegistroAvanceTotal rat) {
		super(name);
		this.mutex = mutex;
		this.rat = rat;
		this.carril = carril;
		paso = 0;
		this.regl=reg;
	}

	public  void corra() throws InterruptedException {
		while (paso < carril.size()) {	
			Thread.sleep(100);
			rat.setAvanceTotal(1);
			carril.setPasoOn(paso++);
			carril.displayPasos(paso);
			int ubicacion = 0;
			if (paso == carril.size()) {
				carril.finish();
				try {
					mutex.acquire();
					ubicacion = regl.getUltimaPosicionAlcanzada();
					regl.setUltimaPosicionAlcanzada(ubicacion + 1);
					System.out.println("El galgo " + this.getName() + " llego en la posicion " + ubicacion);
				} finally {
					mutex.release();
				}

				if (ubicacion == 1) {
					regl.setGanador(this.getName());
				}
			}
			
				
			
		}
	}

	@Override
	public void run() {
		
		try {
			corra();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
