package Videoclub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

import javabean.Pelicula;

				
public class HiloClienteServidor implements Runnable{
		private Thread hilo;
		private static int numCliente = 0;
		private Socket socketAlCliente;	
		private List<Pelicula> VideoClub;
		
		public HiloClienteServidor(Socket socketAlCliente) {
	        
			numCliente++;	       
			hilo = new Thread(this, "Cliente_" + numCliente);  
			
			VideoClub = SocketServidor.getPeliculas();
			this.socketAlCliente = socketAlCliente;    
			
			hilo.start();
		}
			
	public void run() {
		System.out.println("Conectando con " + hilo.getName());
		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBuffer = null;
		
	    try {
	    		salida = new PrintStream(socketAlCliente.getOutputStream());
	            entrada = new InputStreamReader(socketAlCliente.getInputStream());
	            BufferedReader bf = new BufferedReader(entrada);
	    
	 	        Boolean continuar = true;
	            String mensajeRecibido = "";
			
            while (continuar) {
				
            	entradaBuffer = new BufferedReader(entrada);
            	mensajeRecibido = entradaBuffer.readLine();
				System.out.println("Mensaje recibido del cliente: " + mensajeRecibido);
            	
				 
	                String[] peticionCliente = mensajeRecibido.split("/");
	                
	                int opcion = Integer.parseInt(peticionCliente[0]);

	                Object respuesta = null;
	                Pelicula pelicula = null;
	                              	                
	                
				
	                switch (opcion) {
                   case 1:
                      
                	   String idPelicula = peticionCliente[1];

                       pelicula = buscarPeliculaPorId(idPelicula);
                      
                       if (pelicula != null) {
                           respuesta = "ID: " + pelicula.toString();
                       } else {
                           respuesta = "No se encontro la pelicula con el ID especificado.";
                       }
                       System.out.println(respuesta);
                       salida.println(respuesta);
                       break;
				
                       
                   case 2:
                   	
                   	String titulo = peticionCliente[1];
                       pelicula = buscarPeliculaPorTitulo(titulo);
                       if (pelicula != null) {
                           respuesta = "ID: " + pelicula.toString();
                       } else {
                           respuesta = "No hay peliculas con ese titulo";
                       }
                       salida.println(respuesta);
                       break;

                       
                   case 3:
                   	
                   	String director = peticionCliente[1];
                   	pelicula = buscarPeliculaPorDirector(director);
                       if (pelicula != null) {
                           respuesta = "ID: " + pelicula.toString();
                       } else {
                           respuesta = "No hay peliculas con ese director";
                       }
                       salida.println(respuesta);
                       break;
                       
                   case 4:
                	   
                	   if(addPelicula(peticionCliente))
							salida.println("Pelicula añadida con exito");
						else
							salida.println("No se puede añadir la pelicula");
						break;
                       
                   case 5:
                	   continuar = false;
                	   System.out.println("Comunicacion con cliente_" + numCliente + " finalizada");
                	   break;
				}
               }
           
				
	        	              
			socketAlCliente.close();

		} catch (IOException e) {
			System.err.println("SERVIDOR: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error");
			e.printStackTrace();
		}
	}

	
	private Pelicula buscarPeliculaPorId(String id) {
		Pelicula p = null;
		for(Pelicula pelicula : VideoClub){
			if(pelicula.getIdPelicula().equalsIgnoreCase(id))
				p = pelicula;
		}
		
		return p;
	
   }
	
	private Pelicula buscarPeliculaPorTitulo(String titulo){
		Pelicula p = null;
		for(Pelicula pelicula : VideoClub){
			if(pelicula.getTitulo().equalsIgnoreCase(titulo))
				p = pelicula;
		}
		
		return p;
	}
			
	private Pelicula buscarPeliculaPorDirector(String director){
		Pelicula p = null;
		for (Pelicula pelicula : VideoClub) {
			if(pelicula.getDirector().equalsIgnoreCase(director))
				p = pelicula;
		}
		return p;
		
	}


	private boolean addPelicula(String[] peticionCliente){
		Pelicula nuevaPelicula = new Pelicula();
		nuevaPelicula.setIdPelicula(peticionCliente[1]);
		nuevaPelicula.setTitulo(peticionCliente[2]);
		nuevaPelicula.setDirector(peticionCliente[3]);
		nuevaPelicula.setPrecio(Double.parseDouble(peticionCliente[4]));
		
	
		if(VideoClub.contains(nuevaPelicula))
			return false;
		
		else {
			System.out.println("Añadiendo pelicula....");
			
			SocketServidor.addPelicula(nuevaPelicula);
			return true;
		} 
		}

	
}

