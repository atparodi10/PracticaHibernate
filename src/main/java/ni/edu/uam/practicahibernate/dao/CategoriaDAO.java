package ni.edu.uam.practicahibernate.dao;

import jakarta.persistence.EntityManager;

import ni.edu.uam.practicahibernate.database.JPAutil;
import ni.edu.uam.practicahibernate.model.Categoria;

import java.util.List;

public class CategoriaDAO {

    // Obtiene las categorías registradas en PostgreSQL.
    public List<Categoria> listar() {

        EntityManager em = JPAutil.getEntityManager();

        try {
            // Consultamos objetos Categoria mediante JPQL.
            // Se ordenan por nombre para mostrarlos en JavaFX.
            return em.createQuery(
                    "SELECT c FROM Categoria c ORDER BY c.nombre",
                    Categoria.class
            ).getResultList();

        } finally {
            // Cerramos el gestor incluso si ocurre un error.
            // La excepción se propagará al controlador.
            em.close();
        }
    }
}