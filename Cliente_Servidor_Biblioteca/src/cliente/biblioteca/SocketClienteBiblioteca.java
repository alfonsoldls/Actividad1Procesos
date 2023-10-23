package cliente.biblioteca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class SocketClienteBiblioteca {

	public static final int PUERTO = 2023;
	public static final String IP_SERVER = "localhost";
	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("        APLICACIÓN CLIENTE         ");
		System.out.println("-----------------------------------\n");
	
		
	//Se crea un objeto InetSocketAddress con la IP y el Puerto del Servidor.
		
		InetSocketAddress direccion = new InetSocketAddress(IP_SERVER, PUERTO);
		
	// Se crea el socketAlServer e Hacemos la peticion de conexion.
		
		try (Socket socketAlServidor = new Socket()){
			System.out.println("Esperando a tener conexion con el servidor");
			socketAlServidor.connect(direccion);
			System.out.println("Conexion Completada con Exito");
			
	//Se crean los objetos de entrada y salida de datos.
			
			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader bf = new BufferedReader(entrada);
			
			boolean continuar = true;
			
			while (continuar) {
			
				System.out.println("\n------------------------------");
				System.out.println("1.Consultar película por ID");
				System.out.println("2.Consultar película por Titulo");
				System.out.println("3.Consultar película por Director");
				System.out.println("4.Añadir Pelicula");
				System.out.println("5.Salir de la Aplicacion");
				System.out.println("------------------------------");
				String opcion = sc.nextLine();
				String consulta;
			
				//Pedimos la opcion al Usuario. 
				
				switch (opcion) {
					
				//Pedimos el ID de la Pelicula y le mandamos al servidor la opcion + la consulta que sera el ID.
				//Posteriormente el cliente queda esperando la respuesta del servidor para imprimirla por pantalla.
				
					case "1": 
						System.out.println("Introduzca El ID de la Pelicula");
						consulta = sc.nextLine();
						salida.println(opcion + "-" + consulta);
						System.out.println(bf.readLine());
						break;
						
				//Pedimos el titulo de la Pelicula y le mandamos al servidor la opcion + la consulta que sera el titulo.
				//Posteriormente el cliente queda esperando la respuesta del servidor para imprimirla por pantalla.
						
					case "2":
						System.out.println("Introduzca el Titulo de la Pelicula");
						consulta = sc.nextLine();
						salida.println(opcion + "-" + consulta);
						System.out.println(bf.readLine());
						break;
						
				//Pedimos el nombre de la pelicula y le mandamos al servidor la opcion + la consulta que sera el director.
				//Posteriormente el cliente queda esperando la respuesta del servidor para imprimirla por pantalla.
						
					case "3":
						System.out.println("Introduzca el nombre del director");
						consulta = sc.nextLine();
						salida.println(opcion + "-" + consulta);
						System.out.println(bf.readLine());
						break;
						
				//Para no ensuciar el codigo se ha creado la funcion pidePelicula que devuelve un String con los atributos de la Pelicula separados por guiones		
				//Posteriormente el cliente queda esperando la respuesta del servidor para imprimirla por pantalla.		
					case "4":
						consulta = pidePelicula();
						salida.println(opcion + "-" + consulta);
						System.out.println(bf.readLine());
						break;
				
				//La variable continuar se pone a false y se le manda al servidor la opcion para que sepa que la conexion ha terminado.
					case "5":
						System.out.println("Cerrando programa");
						continuar = false;
						salida.println(opcion);
						break;
						
					default: 
						System.out.println("Caracter Introducido no valido");
				}				
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	//Metodo que devuelve un String con los atributos de la pelicula separados por guiones.
	
	private static String pidePelicula() {
		String consulta;
			System.out.println("Introduzca el ID de la pelicula: ");
			consulta = sc.nextLine() + "-";
			System.out.println("Introduzca el titulo de la pelicula: ");
			consulta += sc.nextLine() + "-" ;
			System.out.println("Introduzca el director de la pelicula: ");
			consulta += sc.nextLine() + "-" ;
			System.out.println("Introduzca el precio de la pelicula: ");
			consulta += sc.nextLine();
		
		return consulta;
	}
	
}
