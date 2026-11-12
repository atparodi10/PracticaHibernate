package ni.edu.uam.practicahibernate.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="libro", schema = "catalogos")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "autor", nullable = false, length = 150)
    private String autor;

    @Column(name = "anio_publicacion", nullable = false)
    private int anioPublicacion;

    @Column(name = "precio", nullable = false,
            precision = 12, scale = 2)
    private BigDecimal precio;

    // Relación muchos a uno:
    // muchos libros pueden compartir una categoría.
    //
    // LAZY permite cargar la categoría cuando se necesita.
    // optional=false indica que la relación es obligatoria.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)

    // categoria_id es la columna de clave foránea
    // que apunta al identificador de Categoria.
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Libro() {
    }

    public Libro(Long id, String titulo, String autor,
                 int anioPublicacion, BigDecimal precio) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    // Nueva propiedad que representa la relación.
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}