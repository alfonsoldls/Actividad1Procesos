package javabean;

public class Pelicula {

	private String idPelicula, titulo, director;
	private double precio;
	
	
	
	public Pelicula() {
		super();
	}

	
	public Pelicula(String idPelicula, String titulo, String director, double precio) {
		super();
		this.idPelicula = idPelicula;
		this.titulo = titulo;
		this.director = director;
		this.precio = precio;
	}


	public String getIdPelicula() {
		return idPelicula;
	}


	public void setIdPelicula(String idPelicula) {
		this.idPelicula = idPelicula;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getDirector() {
		return director;
	}


	public void setDirector(String director) {
		this.director = director;
	}


	public double getPrecio() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}


	@Override
	public String toString() {
		return "Pelicula [idPelicula=" + idPelicula + ", titulo=" + titulo + ", director=" + director + ", precio="
				+ precio + "]";
	}
	
}
	
	