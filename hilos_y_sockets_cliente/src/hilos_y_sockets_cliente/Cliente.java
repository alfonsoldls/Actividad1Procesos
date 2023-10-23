package hilos_y_sockets_cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

    // Constantes para el puerto y la IP del servidor
	public static final int PUERTO = 9999;
	public static final String IP_SERVER = "localhost";

    // Método principal donde inicia la aplicación
	public static void main(String[] args) {

        // Mensajes iniciales de la aplicación cliente
		System.out.println("      APLICACIÓN DEL CLIENTE       ");
		System.out.println("-----------------------------------");

        // Creación del objeto que contiene la dirección del servidor
		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);

        // Variables para almacenar los datos de entrada del usuario y las respuestas del servidor
		String peticion = "";
		String id = "";
		String titulo = "";
		String director = "";
		String precio = "";
		String busqueda = "";

        // Uso de try-with-resources para manejar el Scanner y asegurar que se cierre adecuadamente al final
		try (Scanner sc = new Scanner(System.in);) {

            // Variables locales para gestionar las opciones del menú, la continuidad del programa y las respuestas del servidor
			String opcion = "";			
			Boolean continuar = true;
			String respuesta = "";

            // Creación del socket para conectar con el servidor
			Socket socketToServer = new Socket();
			socketToServer.connect(direccionServidor);

            // Establecimiento de flujos de salida y entrada para la comunicación con el servidor
			PrintStream salida = new PrintStream(socketToServer.getOutputStream());
			InputStreamReader entrada = new InputStreamReader(socketToServer.getInputStream());
			BufferedReader bf = new BufferedReader(entrada);

            // Bucle principal para la interacción con el usuario
			while (continuar) {

                // Impresión del menú de opciones
				System.out.println("               MENÚ                ");
				System.out.println("-----------------------------------");
				System.out.println("1. Consultar película por ID       ");
				System.out.println("2. Consultar película por título   ");
				System.out.println("3. Consultar películas por director");
				System.out.println("4. Añadir película                 ");
				System.out.println("5. Salir de la aplicación          ");
				System.out.println("                                   ");
				System.out.println("Seleccione una opción:");

                // Captura de la opción seleccionada por el usuario
				opcion = sc.nextLine();

                // Manejo de la opción del usuario mediante una estructura switch
				switch (opcion) {
					case "1":
						System.out.println("Introduzca el ID de la película que desea buscar:");
						peticion = sc.nextLine();
						break;

					case "2":
						System.out.println("Introduzca el título de la película que desea buscar:");
						peticion = sc.nextLine();
						break;

					case "3":
						System.out.println("Introduzca el director de la/s película/s que desea buscar:");
						peticion = sc.nextLine();
						break;

					case "4":
                        // Verificación y entrada de datos para la nueva película
				        boolean esEntero = false;
				        while (!esEntero) {
				            System.out.println("Introduzca el ID de la película que desea añadir:");
				            id = sc.nextLine();

                            // Verificación del ID para asegurarse de que sea un entero
				            try {
				                Integer.parseInt(id);
				                esEntero = true;
				            } catch (NumberFormatException e) {
				                System.out.println("El ID introducido no es un número entero. Inténtalo de nuevo.");
				            }
				        }

						System.out.println("Introduzca el título de la película que desea añadir:");
						titulo = sc.nextLine();
						
						System.out.println("Introduzca el director de la película que desea añadir:");
						director = sc.nextLine();

                        // Verificación y entrada del precio de la nueva película
				        boolean esDouble = false;
				        while (!esDouble) {
				            System.out.println("Introduzca el precio de la película que desea añadir:");
				            precio = sc.nextLine();

                            // Verificación del precio para asegurarse de que sea un número decimal válido
				            try {
				            	Double.parseDouble(precio);
				            	esDouble = true;
				            } catch (NumberFormatException e) {
				                System.out.println("El precio introducido no es un número válido. Inténtalo de nuevo.");
				            }
				        }
				        
                        // Construcción de la petición con los datos ingresados
				        peticion = id + "-" + titulo + "-" + director + "-" + precio;
						break;
						
					case "5":
						System.out.println("Saliendo del programa...");
						break;
						
					default:
						System.out.println("Introduzca una opción válida:");
						break;
				}

                // Procesamiento de la opción seleccionada y envío/recepción de datos con el servidor
				if (opcion.equals("1") || opcion.equals("2") || opcion.equals("3") || opcion.equals("4") || opcion.equals("5")) {
					busqueda = opcion +"-"+peticion;

                    // Envío de la petición al servidor
					salida.println(busqueda);

                    // Recepción de la respuesta del servidor
					respuesta = bf.readLine();

                    // Procesamiento específico para la opción de buscar por director
					if (opcion.equals("3")) {
						String[] respuestaDirectores = respuesta.split("&");
						if (respuesta.equalsIgnoreCase("No se encontró la película con el director especificado.")) {
							System.out.println(respuesta);
						} else {
                            // Impresión de los detalles de las películas encontradas
							for (int i = 0; i < respuestaDirectores.length; i++) {
								String[] peliculaDirectores = respuestaDirectores[i].split("-");
								System.out.println("ID: " + peliculaDirectores[0] + " - Título: " + peliculaDirectores[1] + " - Director: " + peliculaDirectores[2] + " - Precio: " + peliculaDirectores[3] + "€");
							}
						}
					} else {
                        // Impresión de la respuesta del servidor para las demás opciones
						System.out.println(respuesta);
					}

                    // Condición para salir del bucle y finalizar el programa
					if (respuesta.equalsIgnoreCase("Hasta pronto")) {
						continuar = false;
					}
				}
			}

            // Cierre del socket al finalizar la comunicación
			socketToServer.close();

        // Manejo de excepciones específicas
		} catch (UnknownHostException e) {
			System.err.println("No encuentro el servidor en la dirección" + IP_SERVER);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error -> " + e);
			e.printStackTrace();
		}

        // Mensaje de finalización del programa
		System.out.println("Fin del programa");
	}
}