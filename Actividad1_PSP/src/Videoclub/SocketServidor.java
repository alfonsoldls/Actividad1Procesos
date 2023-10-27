package Videoclub;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javabean.Pelicula;


	public class SocketServidor {
		
		public static final int PUERTO = 2000;
		public static final String IP_SERVER = "192.168.98.137";

		private static List<Pelicula> VideoClub = new ArrayList<>();
		
		public static void main(String[] args) {

			crearVideoClub();
			
			System.out.println("      APLICACIÓN DEL SERVIDOR      ");
			System.out.println("-----------------------------------");
			System.out.println("Servidor creado y escuchando .... ");
	       
			int peticion = 0;
			try {ServerSocket servidor = new ServerSocket();
			InetSocketAddress direccion = new InetSocketAddress(IP_SERVER,PUERTO);
			servidor.bind(direccion);

	           
				while(true) {
					Socket socketAlCliente = servidor.accept();
	                System.out.println("Petición número "+ ++peticion +" recibida.");
					new HiloClienteServidor(socketAlCliente);
				}

	       
			} catch (IOException e) {
				System.err.println("SERVIDOR: Error de entrada/salida");
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("SERVIDOR: Error");
				e.printStackTrace();
			}
		}
	
		
private static void crearVideoClub(){
		
		
		Pelicula p1 = new Pelicula ("1","La Patrulla Canina", "Cal Brunker", 14.50);
		Pelicula p2 = new Pelicula ("2", "Trolls 3", "Mike Mitchell", 15.00 );
		Pelicula p3 = new Pelicula ("3", "Momias", "Juan Jesus Garcia", 13.50);
		Pelicula p4 = new Pelicula ("4", "Hanna y los monstruos", "Lorena Ares", 10.50);
		Pelicula p5 = new Pelicula ("5", "Peter Pan y Wendy", "David Lowery", 12.00);
		
		
		VideoClub.add(p1);
		VideoClub.add(p2);
		VideoClub.add(p3);
		VideoClub.add(p4);
		VideoClub.add(p5);
	}
	
	
	
	public static List<Pelicula> getPeliculas(){
		
		return VideoClub;
		
		
	}
		
	public static synchronized void addPelicula(Pelicula nueva) {
		
		VideoClub.add(nueva);
			
		try {
			Thread.sleep(1000);
			System.out.println("añadido....");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}		