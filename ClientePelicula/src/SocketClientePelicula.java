import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClientePelicula {
	
	public static final int PUERTO = 2000;
	public static final String IP_SERVER = "192.168.98.137";
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		
		System.out.println("        APLICACIÓN CLIENTE");
		System.out.println("-----------------------------------");
		
		
		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER,PUERTO);
		
		String consulta;
		String peticion;
		String respuesta;
		
				
		try {Socket socketAlServidor = new Socket();
			System.out.println("Esperando a que el servidor acepte la conexión");
			socketAlServidor.connect(direccionServidor);
			System.out.println("Comunicación establecida con el servidor");
			
						
			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader bf = new BufferedReader(entrada);
			
			boolean continuar = true;
		
			while (continuar){
				System.out.println();
				System.out.println("********* MENU *********");
				System.out.println("------------------------");				
				System.out.println("1. Consultar peliculas por Id");
				System.out.println("2. Consultar peliculas por Titulo");
				System.out.println("3. Consultar peliculas por Director");
				System.out.println("4. Añadir pelicula");
				System.out.println("5. Salir");
				System.out.println("Introduce una opcion");
				String opcion = sc.nextLine();
				
				switch (opcion) {
					case "1":
						System.out.println("Introduce el Id de la pelicula");
						consulta = sc.nextLine();
						peticion = opcion + "/" + consulta;
						salida.println(peticion);
						respuesta = bf.readLine();
						System.out.println(respuesta);
						break;
						
					case "2":
						System.out.println("Introduce el Titulo de la pelicula");
						consulta = sc.nextLine();
						peticion = opcion + "/" + consulta;
						salida.println(peticion);
						respuesta = bf.readLine();
						System.out.println(respuesta);
						break;
						
					case "3":
						System.out.println("Introduce el Director de la pelicula");
						consulta = sc.nextLine();
						peticion = opcion + "/" + consulta;
						salida.println(peticion);
						respuesta = bf.readLine();
						System.out.println(respuesta);
						break;
						
					case "4":
						System.out.println("Añadir una pelicula");
						consulta = nuevaPelicula();
						salida.println(opcion + "/" + consulta);
						System.out.println(bf.readLine());
						break;
						
						
					case "5":
						System.out.println("Has elegido salir. Hasta pronto");
						continuar = false;
						salida.println(opcion);
						break;
						
					default: 
						System.out.println("Numero introducido no valido");
					}
				}
			
			
			socketAlServidor.close();
			System.out.println("Comunicacion cerrada");
		
		} catch (UnknownHostException e) {
			System.out.println("No se puede establecer comunicacion con el servidor");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Error de Entrada/Salida");
			System.out.println(e.getMessage());
		}  
	}

	private static String nuevaPelicula() {
		String consulta;
			System.out.println("Introduce el Id de la pelicula: ");
			consulta = sc.nextLine() + "/";
			System.out.println("Introduce el titulo: ");
			consulta += sc.nextLine() + "/" ;
			System.out.println("Introduce el director: ");
			consulta += sc.nextLine() + "/" ;
			System.out.println("Introduce el precio: ");
			consulta += sc.nextLine();
		
		return consulta;
	}
	
}
	

