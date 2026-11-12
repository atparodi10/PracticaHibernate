package ni.edu.uam.practicahibernate.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Esta entidad representa la tabla catalogos.categorias.
@Entity
@Table(name = "categorias", schema = "catalogos")
public class Categoria {

    // Clave primaria generada por PostgreSQL.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // El nombre es obligatorio y no puede repetirse.
    @Column(name = "nombre", nullable = false,
            unique = true, length = 100)
    private String nombre;

    // La descripción es opcional.
    @Column(name = "descripcion", length = 250)
    private String descripcion;

    // Relación uno a muchos:
    // una categoría puede tener varios libros.
    //
    // mappedBy hace referencia al atributo "categoria"
    // definido dentro de la clase Libro.
    //
    // LAZY evita cargar todos los libros automáticamente.
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Libro> libros = new ArrayList<>();

    // Constructor vacío necesario para JPA.
    public Categoria() {
    }

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    // JavaFX utiliza toString() para representar
    // la categoría en el ComboBox.
    @Override
    public String toString() {
        return nombre;
    }
}