package hilos_y_sockets_servidor;

import java.io.*;
import java.net.*;

public class Servidor {

    // Constante para el puerto en el que el servidor escuchará las conexiones entrantes
	public static final int PUERTO = 9999;

	public static void main(String[] args) {

        // Mensaje inicial indicando que la aplicación del servidor está en ejecución
		System.out.println("      APLICACIÓN DEL SERVIDOR      ");
		System.out.println("-----------------------------------");

        // Variable para contar el número de peticiones recibidas
		int peticion = 0;

        // Intentar establecer una conexión, escuchar peticiones y manejar excepciones
		try (ServerSocket serverSocket = new ServerSocket()){

            // Establecer la dirección en la que el servidor escuchará las conexiones entrantes
			InetSocketAddress direccion = new InetSocketAddress(PUERTO);
			serverSocket.bind(direccion);

            // Bucle infinito para aceptar múltiples conexiones de los clientes
			while(true) {

                // Aceptar la conexión entrante de un cliente
				Socket socketToClient = serverSocket.accept();

                // Imprimir mensaje indicando que se ha recibido una nueva petición
				System.out.println("Petición número "+ ++peticion +" recibida.");

                // Crear un nuevo hilo para manejar la petición del cliente, permitiendo así la concurrencia
				new HiloServidor(socketToClient);
			}

        // Manejar las excepciones específicas que puedan ocurrir
		} catch (IOException e) {
			System.err.println("SERVIDOR: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error");
			e.printStackTrace();
		}
	}
}
