package arsw.threads;

public class RegistroAvanceTotal {
	private int avanceTotal;
	private boolean blocked;
	public RegistroAvanceTotal() {
		blocked = false;
		avanceTotal=0;
	}
	
	public int getAvanceTotal() {
		return avanceTotal;
	}
	public synchronized void setBlocked(boolean blocked) {
		this.blocked = blocked;
		if(! blocked) {
			notifyAll();
		}
	}
	public synchronized void setAvanceTotal(int avanceTotal) {
		if(blocked) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.avanceTotal+=avanceTotal;
	}
}
