package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	boolean wait;
	private List<Integer> primes=new LinkedList<Integer>();
	
	public PrimeFinderThread(int a, int b) {
		super();
		this.a = a;
		this.b = b;
		wait = false;
	}

	public void run(){
		for (int i=a;i<=b;i++){
			while (wait) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
					//El hilo espera hasta que la variable wait cambie su valor.
			}
			if (isPrime(i)){
				primes.add(i);
				System.out.println(i);
			}
			
			
		}
		
		
	}
	
	boolean isPrime(int n) {
	    if (n%2==0) return false;
	    for(int i=3;i*i<=n;i+=2) {
	        if(n%i==0)
	            return false;
	    }
	    return true;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	
	public void setWait(boolean wait) {
		this.wait = wait;
	}
	
	
	
	
	
}
