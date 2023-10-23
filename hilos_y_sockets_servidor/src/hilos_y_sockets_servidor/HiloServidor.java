package hilos_y_sockets_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import peliculas.Pelicula;

public class HiloServidor implements Runnable{
    // Contador para llevar un registro del número de clientes que se conectan
	private static int numCliente = 0;
    
    // El hilo que se iniciará para cada conexión de cliente
	private Thread hilo;

    // El socket que conecta con el cliente
	private Socket socketToClient;	

    // Constructor que toma un socket como parámetro
	public HiloServidor(Socket socketToClient) {
        // Incrementa el contador de clientes
		numCliente++;

        // Inicializa el hilo y asigna un nombre basado en el número del cliente
		hilo = new Thread(this, "Cliente_" + numCliente);

        // Almacena el socket para su uso en el hilo
		this.socketToClient = socketToClient;

        // Inicia el hilo
		hilo.start();
	}

    // Método para buscar películas por ID
	public static Pelicula buscarPeliculaPorId(List<Pelicula> peliculas, int id) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getId() == id) {
                return pelicula;
            }
        }
        return null;
    }
	
	// Método para buscar películas por título
    public static Pelicula buscarPeliculaPorTitulo(List<Pelicula> peliculas, String titulo) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getTitulo().equalsIgnoreCase(titulo)) {
                return pelicula;
            }
        }
        return null;
    }

    // Método para buscar películas por director
    public static List<Pelicula> buscarPeliculaPorDirector(List<Pelicula> peliculas, String director) {
        List<Pelicula> peliculaDirectores = new ArrayList<>();
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getDirector().equalsIgnoreCase(director)) {
                peliculaDirectores.add(pelicula);
            }
        }
        if (peliculaDirectores.isEmpty()) {
            return null;
        } else {
            return peliculaDirectores;
        }
    }

    // Lista estática de películas
    static List<Pelicula> peliculas = new ArrayList<>();

    static {
        // Agregar películas a la lista
        peliculas.add(new Pelicula(1, "Pulp Fiction", "Quentin Tarantino", 15.99));
        peliculas.add(new Pelicula(2, "El Padrino", "Francis Ford Coppola", 19.95));
        peliculas.add(new Pelicula(3, "Scarface", "Brian De Palma", 9.90));
        peliculas.add(new Pelicula(4, "Million Dollar Baby", "Clint Eastwood", 5.95));
        peliculas.add(new Pelicula(5, "Kill Bill", "Quentin Tarantino", 17.5));
    }

    // Método run que se ejecutará cuando se inicie el hilo
	@Override
	public void run() {
	    try (
            // Inicialización de los streams de entrada/salida
            PrintStream salida = new PrintStream(socketToClient.getOutputStream());
            InputStreamReader entrada = new InputStreamReader(socketToClient.getInputStream());
            BufferedReader bf = new BufferedReader(entrada)
        ) {
            Boolean continuar = true;
            String stringRecibido;

            while (continuar) {
                // Leer la petición del cliente
                stringRecibido = bf.readLine();

                System.out.println("Ha llegado el siguiente String: " + stringRecibido);

                String[] peticionCliente = stringRecibido.split("-");
                
                int opcion = Integer.parseInt(peticionCliente[0]);

                Object respuesta = null;
                Pelicula pelicula = null;

                switch (opcion) {
                    case 1:
                        // Buscar película por ID
                        int idPelicula = Integer.parseInt(peticionCliente[1]);

                        pelicula = buscarPeliculaPorId(peliculas, idPelicula);
                        if (pelicula != null) {
                            respuesta = "ID: " + pelicula.getId() + " - Título: " + pelicula.getTitulo() + " - Director: " + pelicula.getDirector() + " - Precio: " + pelicula.getPrecio() + "€";
                        } else {
                            respuesta = "No se encontró la película con el ID especificado.";
                        }
                        salida.println(respuesta);
                        break;
                    case 2:
                        // Buscar película por título
                        String tituloPelicula = peticionCliente[1];

                        pelicula = buscarPeliculaPorTitulo(peliculas, tituloPelicula);
                        if (pelicula != null) {
                            respuesta = "ID: " + pelicula.getId() + " - Título: " + pelicula.getTitulo() + " - Director: " + pelicula.getDirector() + " - Precio: " + pelicula.getPrecio() + "€";
                        } else {
                            respuesta = "No se encontró la película con el título especificado.";
                        }
                        salida.println(respuesta);
                        break;
                    case 3:
                        // Buscar película por director
                        String directorPelicula = peticionCliente[1];
                        List<Pelicula> directoresLista = buscarPeliculaPorDirector(peliculas, directorPelicula);

                        if (directoresLista != null) {
                            respuesta = "";
                            for (Pelicula p : directoresLista) {
                                respuesta += "ID: " + p.getId() + " - Título: " + p.getTitulo() + " - Director: " + p.getDirector() + " - Precio: " + p.getPrecio() + "€&";
                            }
                            respuesta = ((String)respuesta).substring(0, ((String)respuesta).length() - 1); // Remove the last "&"
                        } else {
                            respuesta = "No se encontró la película con el director especificado.";
                        }
                        salida.println(respuesta);
                        break;
                    case 4:
                        // Añadir una nueva película
                        int id = Integer.parseInt(peticionCliente[1]);
                        String titulo = peticionCliente[2];
                        String director = peticionCliente[3];
                        double precio = Double.parseDouble(peticionCliente[4]);

                        synchronized(peliculas) {
                            peliculas.add(new Pelicula(id, titulo, director, precio));
                        }
                        respuesta = "Película añadida correctamente";
                        salida.println(respuesta);
                        break;
                    case 5:
                        // Cerrar la conexión
                        respuesta = "Hasta pronto";
                        salida.println(respuesta);
                        continuar = false;
                        break;
                }
            }
            // Cerrar el socket al finalizar
            socketToClient.close();
        } catch (IOException e) {
            System.err.println("SERVIDOR: Error de entrada/salida");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("SERVIDOR: Error");
            e.printStackTrace();
        }
    }
}