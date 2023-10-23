package peliculas;

// Clase que representa la entidad Pelicula
public class Pelicula {
	
	// Atributos de la Pelicula
	private int id;
	private String titulo;
	private String director;
	private double precio;
	
	// Constructor para inicializar los atributos
	public Pelicula(int id, String titulo, String director, double precio) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.director = director;
		this.precio = precio;
	}

	// Métodos getter y setter 
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	// Método toString
	@Override
	public String toString() {
		return "Pelicula [id=" + id + ", titulo=" + titulo + ", director=" + director + ", precio=" + precio + "]";
	}
}
