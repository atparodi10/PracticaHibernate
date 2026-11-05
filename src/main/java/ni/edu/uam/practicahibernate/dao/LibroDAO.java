package ni.edu.uam.practicahibernate.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.uam.practicahibernate.database.JPAutil;
import ni.edu.uam.practicahibernate.model.Libro;

import java.util.List;

public class LibroDAO {

    public void guardar(Libro libro) {

        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Decirle a Hibernate que prepare la persistencia del objeto
            em.persist(libro);

            // Confirmar los cambios en la base de datos
            tx.commit();

        } catch (Exception ex) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("Error al guardar el libro", ex);

        } finally {
            em.close();
        }
    }

    public List<Libro> listar (){
        EntityManager em = JPAutil.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM Libro l ORDER BY l.id", Libro.class).getResultList();        }
        finally {
            em.close();
        }
    }
}