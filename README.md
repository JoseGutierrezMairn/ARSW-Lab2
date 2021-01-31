
### Escuela Colombiana de Ingeniería

### Arquitecturas de Software – ARSW
## Laboratorio Programación concurrente, condiciones de carrera, esquemas de sincronización, colecciones sincronizadas y concurrentes - Caso Dogs Race

### Descripción:
Este ejercicio tiene como fin que el estudiante conozca y aplique conceptos propios de la programación concurrente.

### Parte I 
Antes de terminar la clase.

Creación, puesta en marcha y coordinación de hilos.

1. Revise el programa “primos concurrentes” (en la carpeta parte1), dispuesto en el paquete edu.eci.arsw.primefinder. Este es un programa que calcula los números primos entre dos intervalos, distribuyendo la búsqueda de los mismos entre hilos independientes. Por ahora, tiene un único hilo de ejecución que busca los primos entre 0 y 30.000.000. Ejecútelo, abra el administrador de procesos del sistema operativo, y verifique cuantos núcleos son usados por el mismo.
~~~
El resultado fue el siguiente
~~~
![UnHilo](https://github.com/JoseGutierrezMairn/ARSW-Lab2/blob/master/img/media/rendimientoUnHilo.PNG?raw=true)

2. Modifique el programa para que, en lugar de resolver el problema con un solo hilo, lo haga con tres, donde cada uno de éstos hará la tarcera parte del problema original. Verifique nuevamente el funcionamiento, y nuevamente revise el uso de los núcleos del equipo.  
![TresHilos](https://github.com/JoseGutierrezMairn/ARSW-Lab2/blob/master/img/media/rendimientoTresHilos.PNG?raw=true)

3. Lo que se le ha pedido es: debe modificar la aplicación de manera que cuando hayan transcurrido 5 segundos desde que se inició la ejecución, se detengan todos los hilos y se muestre el número de primos encontrados hasta el momento. Luego, se debe esperar a que el usuario presione ENTER para reanudar la ejecución de los mismo.



### Parte II 


Para este ejercicio se va a trabajar con un simulador de carreras de galgos (carpeta parte2), cuya representación gráfica corresponde a la siguiente figura:

![](./img/media/image1.png)

En la simulación, todos los galgos tienen la misma velocidad (a nivel de programación), por lo que el galgo ganador será aquel que (por cuestiones del azar) haya sido más beneficiado por el *scheduling* del
procesador (es decir, al que más ciclos de CPU se le haya otorgado durante la carrera). El modelo de la aplicación es el siguiente:

![](./img/media/image2.png)

Como se observa, los galgos son objetos ‘hilo’ (Thread), y el avance de los mismos es visualizado en la clase Canodromo, que es básicamente un formulario Swing. Todos los galgos (por defecto son 17 galgos corriendo en una pista de 100 metros) comparten el acceso a un objeto de tipo
RegistroLLegada. Cuando un galgo llega a la meta, accede al contador ubicado en dicho objeto (cuyo valor inicial es 1), y toma dicho valor como su posición de llegada, y luego lo incrementa en 1. El galgo que
logre tomar el ‘1’ será el ganador.

Al iniciar la aplicación, hay un primer error evidente: los resultados (total recorrido y número del galgo ganador) son mostrados antes de que finalice la carrera como tal. Sin embargo, es posible que una vez corregido esto, haya más inconsistencias causadas por la presencia de condiciones de carrera.

Parte III

1.  Corrija la aplicación para que el aviso de resultados se muestre
    sólo cuando la ejecución de todos los hilos ‘galgo’ haya finalizado.
    Para esto tenga en cuenta:

    a.  La acción de iniciar la carrera y mostrar los resultados se realiza a partir de la línea 38 de MainCanodromo.

    b.  Puede utilizarse el método join() de la clase Thread para sincronizar el hilo que inicia la carrera, con la finalización de los hilos de los galgos.
	
	~~~
	La solución propuesta se encuentra en la clase MainCanodromo, donde se busca  
	hacer join entre los hilos con el proceso principal, así éste los esperará  
	a que completen sus respectivas funciones.
	~~~
2.  Una vez corregido el problema inicial, corra la aplicación varias
    veces, e identifique las inconsistencias en los resultados de las
    mismas viendo el ‘ranking’ mostrado en consola (algunas veces
    podrían salir resultados válidos, pero en otros se pueden presentar
    dichas inconsistencias). A partir de esto, identifique las regiones
    críticas () del programa.
	~~~
	La región critica se encuentra en la clase Galgo, en el método  
	corra() en las líneas donde se consutlala última posición alcanzada  
	y cuando se cambia la última posición alcanzada en la clase  
	RegistroLLegada, la propuesta es bloquear esas líneas usando la clase
	Semaphore, para evitar las condiciones de carrera en el programa.
	~~~
	![CondiciónCarrera](https://github.com/JoseGutierrezMairn/ARSW-Lab2/blob/master/img/media/regionCritica.PNG?raw=true)
3.  Utilice un mecanismo de sincronización para garantizar que a dichas
    regiones críticas sólo acceda un hilo a la vez. Verifique los
    resultados.
	
	~~~
	Después de probar la propuesta para evitar las condiciones  
	de carrera el resultado fue el esperado, esta imagen es de  
	una de las pruebas que se hizo para comprobar que se  
	obtuviera el resultado adecuado:
	~~~
	![casoPrueba1](https://github.com/JoseGutierrezMairn/ARSW-Lab2/blob/master/img/media/casoPrueba.PNG?raw=true)
	![casoPrueba2](https://github.com/JoseGutierrezMairn/ARSW-Lab2/blob/master/img/media/casoPrueba1.PNG?raw=true)
	
	~~~
	Finalmente esta es la propuesta para evitar las condiciones  
	de carrera en el programa:
	~~~
	
	```
	public void corra() throws InterruptedException {
		while (paso < carril.size()) {			
			Thread.sleep(100);
			carril.setPasoOn(paso++);
			carril.displayPasos(paso);
			int ubicacion = 0;
			if (paso == carril.size()) {						
				carril.finish();
				try {
					mutex.acquire();
					ubicacion=regl.getUltimaPosicionAlcanzada();
					regl.setUltimaPosicionAlcanzada(ubicacion+1);
					System.out.println("El galgo "+this.getName()+" llego en la posicion "+ubicacion);
				}finally {
					mutex.release();
				}
				
				if (ubicacion==1){
					regl.setGanador(this.getName());
				}
				
			}
		}
	}
	```

4.  Implemente las funcionalidades de pausa y continuar. Con estas,
    cuando se haga clic en ‘Stop’, todos los hilos de los galgos
    deberían dormirse, y cuando se haga clic en ‘Continue’ los mismos
    deberían despertarse y continuar con la carrera. Diseñe una solución que permita hacer esto utilizando los mecanismos de sincronización con las primitivas de los Locks provistos por el lenguaje (wait y notifyAll).

~~~
Las funcionalidades de pausa y continuar fueron exitosamente
implementadas.
A continuación una prueba sencilla donde se evidencia
el buen funcionamiento de las funcionalidades pausa  
y continuar:
~~~
![pausa](https://github.com/JoseGutierrezMairn/ARSW-Lab2/blob/master/img/media/pausa.PNG?raw=true)
![continuar](https://github.com/JoseGutierrezMairn/ARSW-Lab2/blob/master/img/media/continuar.PNG?raw=true)
![completo](https://github.com/JoseGutierrezMairn/ARSW-Lab2/blob/master/img/media/completo.PNG?raw=true)
![completo2](https://github.com/JoseGutierrezMairn/ARSW-Lab2/blob/master/img/media/completo2.PNG?raw=true)