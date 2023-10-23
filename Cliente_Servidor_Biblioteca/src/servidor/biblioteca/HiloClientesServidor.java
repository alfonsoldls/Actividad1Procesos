package servidor.biblioteca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javabean.Pelicula;


public class HiloClientesServidor implements Runnable{
	
	private Thread hilo;
	private static int numCliente = 0;
	private Socket socketAlCliente;
	private List<Pelicula> biblioteca;
	
	
	public HiloClientesServidor(Socket socketAlCliente) {
		
		numCliente++;
		hilo = new Thread(this, "Cliente_"+numCliente);
		
	//Obtenemos la lista de peliculas del servidor mediante su método estatico y lo guardamos.
		biblioteca = SocketServidorBiblioteca.getPeliculas();
		this.socketAlCliente = socketAlCliente;
	//Iniciamos el hilo en el constructor.
		hilo.start();
		
	}
	
	@Override
	public void run() {
	//Se crean los objetos de entrada y salida para la comunicacion con el cliente.
		
		System.out.println("Estableciendo comunicacion con " + hilo.getName());
		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBuffer = null;
		
		try {
			
			salida = new PrintStream(socketAlCliente.getOutputStream());
			entrada = new InputStreamReader(socketAlCliente.getInputStream());
			
			String stringRecibido = "";
			boolean continuar = true;
			
			
			while (continuar) {
				
	//Se prepara el buffer de entrada.
			
				entradaBuffer = new BufferedReader(entrada);
				
	//Y el servidor aquí espera hasta que reciba algo.
				
				stringRecibido = entradaBuffer.readLine();
				System.out.println("SERVIDOR: Me ha llegado del cliente: " + stringRecibido);
				
	//Hacemos un Split de lo que me ha llegado del cliente y lo guardamos en un array.
				
				String[] opciones = stringRecibido.split("-");
				
	//Hacemos un Switch para ver que opción ha elegido el cliente. Esta ubicada en la posicion 0 del array.
			
				switch (opciones[0]) {
				
	//Se le manda al cliente la Pelicula segun el ID que le haya llegado ubicado en la segunda posicion del array
			
					case "1":
						salida.println(buscarPeliPorId(opciones[1]));
						break;
						
	//Se le manda al cliente la Pelicula segun el titulo que le haya llegado ubicado en la segunda posicion del array				
					
					case "2":
						salida.println(buscarPeliPorTitulo(opciones[1]));
						break;
						
	//Se le manda al cliente una lista con las Peliculas segun el director que le haya llegado ubicado en la segunda posicion del array					
						
					case "3": 
						salida.println(buscarPelicPorDirector(opciones[1]));
						break;
						
	//Se utiliza la funcion de añadir película pasandole el array de opciones, si devuelve true se ha podido añadir
	//Si devuelve false no se ha añadido ya que tiene el mismo id que otra.
						
					case "4":
						
						if(addPelicula(opciones))
							salida.println("Se ha añadido correctamente la pelicula");
						else
							salida.println("No se puede añadir la pelicula ya que tiene el mismo identificador que otra");
						break;
						
	//Se cierra el socket y se acaba la comunicacion, y cambiamos la condicion del bucle para que no vuelva a repetir.	
					
					case "5":
						continuar = false;
						socketAlCliente.close();
						System.out.println("Comunicacion con cliente_" + numCliente + " Terminada");
						break;
				}
				
			}
			
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
		
		
	}
	
	//Funcion que añade una pelicula segun el array con las opciones que me llega por parametro. 
	
	private boolean addPelicula(String[] opciones){
		Pelicula nuevaPeli = new Pelicula();
		nuevaPeli.setId(opciones[1]);
		nuevaPeli.setTitulo(opciones[2]);
		nuevaPeli.setDirector(opciones[3]);
		nuevaPeli.setPrecio(Double.parseDouble(opciones[4]));
		
	//Aquí comprobamos si nuestra biblioteca contiene otra que contenga el mismo ID.
		
		if(biblioteca.contains(nuevaPeli))
			return false;
		else {
			System.out.println("Añadiendo....");
	
	//Se utiliza el metodo sincronizado y estatico del servidor para añadir la pelicula , pasandosela por parametro
			SocketServidorBiblioteca.addPelicula(nuevaPeli);
			return true;
		} 
	}
	
	//Metodo que se encarga de buscar la pelicula en biblioteca según el ID que llega por parametro. Devuelve la pelicula si la encuentra y sino Null
	
	private Pelicula buscarPeliPorId(String id) {
		Pelicula p = new Pelicula();
		p.setId(id);
		int i = biblioteca.indexOf(p);
		if (i == -1)
			return null;
		
		return biblioteca.get(i);
	}
	
	
	//Metodo que se encarga de buscar una pelicula por el Titulo. Devuelve la pelicula si la encuentra. Y sino null.
	
	private Pelicula buscarPeliPorTitulo(String titulo){
		Pelicula p = null;
		for(Pelicula pelicula : biblioteca){
			if(pelicula.getTitulo().equalsIgnoreCase(titulo))
				p = pelicula;
		}
		
		return p;
	}
	
	//Metodo que devuelve una lista de peliculas segun el director que le haya llegado por parametro.
	
	private List<Pelicula> buscarPelicPorDirector(String director){
		List<Pelicula> mylist = new ArrayList<>();
		for (Pelicula pelicula : biblioteca) {
			if(pelicula.getDirector().equalsIgnoreCase(director))
				mylist.add(pelicula);
		}
		return mylist;
		
	}
}
