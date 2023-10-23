package servidor.biblioteca;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javabean.Pelicula;



public class SocketServidorBiblioteca {
	
	public static final int PUERTO = 2023;
	private static List<Pelicula> biblioteca = new ArrayList<>();
	
	public static void main(String[] args) {
		
		//Llamamos a la funcion que se encargar de cargar las películas de la biblioteca.
		crearBiblioteca();
		
		System.out.println("      SERVIDOR BIBLIOTECA     ");
		System.out.println("------------------------------");		
		
		int peticion = 0;
		
		//Se crea el socket servidor.
		
		try (ServerSocket servidor = new ServerSocket()){
		
		//Se prepara un objeto InetSocketAddress con el puerto del servidor y se lo unimos al servidor mediante bind. 
			
			InetSocketAddress direccion = new InetSocketAddress(PUERTO);
			servidor.bind(direccion);
			System.out.println("SERVIDOR: Esperando peticion por el puerto " + PUERTO);
		
		//Se crea un objeto de socketAlcliente por c ada peticion de cliente aceptada por el servidor.
			while (true) {
				
				Socket socketAlCliente = servidor.accept();
				System.out.println("SERVIDOR: peticion numero " + ++peticion + " recibida");
				//Abrimos un hilo nuevo y liberamos el hilo principal para que pueda
				//recibir peticiones de otros clientes
				
				new HiloClientesServidor(socketAlCliente);
			}
			
		} catch (IOException e) {
			 System.out.println("Excepcion de entrada");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error");
		}
		
		
	}
	
	//Funcion que crea la Biblioteca de peliculas.
	
	private static void crearBiblioteca(){
		
		
		Pelicula p1 = new Pelicula ("1","El Secreto del Bosque","Alejandro Perez",12.99);
		Pelicula p2 = new Pelicula ("2","Cazadores de Sueños","María Gutiérrez",14.99);
		Pelicula p3 = new Pelicula ("3","El Misterio de la Luna Roja"," Juan Rodríguez",10.50);
		Pelicula p4 = new Pelicula ("4","El Último Baile","Sofia Lopez",14.75);
		Pelicula p5 = new Pelicula ("5","Aventuras en la Ciudad Perdida","Alejandro Perez",12.99);
		
		biblioteca.add(p1);
		biblioteca.add(p2);
		biblioteca.add(p3);
		biblioteca.add(p4);
		biblioteca.add(p5);
	}
	
	//Función que devuelve la biblioteca en una Lista
	
	public static List<Pelicula> getPeliculas(){
		
		return biblioteca;
		
	}
	
	//Función que añade peliculas a la biblioteca pasandole por parametro una Pelicula
	//Esta función tendra que ser sincronizada para que solo pueda acceder un hilo a la vez
	//para evitar conflictos.
	
	public static synchronized void addPelicula(Pelicula nueva) {
		
		biblioteca.add(nueva);
		
	//Se puede dormir el proceso para realizar pruebas de si pueden acceder varios hilos o no.
		try {
			Thread.sleep(10000);
			System.out.println("añadido....");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
