package ni.edu.uam.practicahibernate.dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.uam.practicahibernate.database.JPAutil;
import ni.edu.uam.practicahibernate.model.Libro;
import ni.edu.uam.practicahibernate.model.Categoria;
import static ni.edu.uam.practicahibernate.database.JPAutil.getEntityManager;

import java.util.List;
public class LibroDAO {
    private Categoria obtenerCategoria(
            EntityManager em, Libro libro) {

        if (libro.getCategoria() == null
                || libro.getCategoria().getId() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una categoría."
            );
        }

        Long categoriaId = libro.getCategoria().getId();

        Categoria categoria =
                em.find(Categoria.class, categoriaId);

        // Evitamos guardar referencias inexistentes.
        if (categoria == null) {
            throw new IllegalArgumentException(
                    "La categoría seleccionada no existe."
            );
        }

        return categoria;
    }

    // CREATE: guarda un nuevo libro.
    public void guardar(Libro libro) {

        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Verificamos que la categoría exista.
            Categoria categoria = obtenerCategoria(em, libro);
            libro.setCategoria(categoria);

            // Hibernate prepara la inserción del registro.
            em.persist(libro);

            // Confirmamos los cambios.
            tx.commit();

        } catch (RuntimeException ex) {

            // Deshacemos la transacción si sigue activa.
            if (tx.isActive()) {
                tx.rollback();
            }

            throw ex;

        } finally {
            em.close();
        }
    }

    // UPDATE: actualiza un libro existente.
    public void actualizar(Libro libro) {

        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // El libro debe tener un ID válido.
            if (libro.getId() == null) {
                throw new IllegalArgumentException(
                        "El libro no tiene un ID."
                );
            }

            // Comprobamos que el registro todavía exista.
            if (em.find(Libro.class, libro.getId()) == null) {
                throw new IllegalArgumentException(
                        "El libro ya no existe en la base de datos."
                );
            }

            // Verificamos la nueva categoría.
            Categoria categoria = obtenerCategoria(em, libro);
            libro.setCategoria(categoria);

            // Copiamos el estado del objeto recibido
            // a una entidad gestionada por Hibernate.
            em.merge(libro);

            tx.commit();

        } catch (RuntimeException ex) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw ex;

        } finally {
            em.close();
        }
    }

    // DELETE: elimina un libro por su ID.
    public boolean eliminar(long id) {

        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Buscamos el registro correspondiente.
            Libro libro = em.find(Libro.class, id);

            if (libro == null) {
                tx.rollback();
                return false;
            }

            // Eliminamos únicamente el libro.
            // Su categoría permanece en PostgreSQL.
            em.remove(libro);

            tx.commit();
            return true;

        } catch (RuntimeException ex) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw ex;

        } finally {
            em.close();
        }
    }

    // READ: consulta todos los libros con sus categorías.
    public List<Libro> listar() {

        EntityManager em = JPAutil.getEntityManager();

        try {
            // JOIN FETCH carga también la categoría
            // antes de cerrar el EntityManager.
            //
            // Esto evita errores de carga LAZY cuando
            // JavaFX necesita mostrar el nombre.
            return em.createQuery(
                    "SELECT l FROM Libro l " +
                            "JOIN FETCH l.categoria " +
                            "ORDER BY l.id",
                    Libro.class
            ).getResultList();

        } finally {
            em.close();
        }
    }
}