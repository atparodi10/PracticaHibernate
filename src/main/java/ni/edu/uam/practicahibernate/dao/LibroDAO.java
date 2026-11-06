package ni.edu.uam.practicahibernate.dao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.uam.practicahibernate.database.JPAutil;
import ni.edu.uam.practicahibernate.model.Libro;
import static ni.edu.uam.practicahibernate.database.JPAutil.getEntityManager;

import java.util.List;
public class LibroDAO {
    public void guardar(Libro libro){
        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            //Iniciar la transaccion en PostgreSQL
            tx.begin();
            //Decirle a Hibernate que prepare la persistencia del objeto libro
            em.persist(libro);
            //Confirmar los cambios en la base de datos
            tx.commit();
        }catch(Exception e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }


    public List<Libro> listar(){
        EntityManager em = JPAutil.getEntityManager();
        try{
            return em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();
        }finally {
            em.close();
        }
    }

    public static boolean modificar(Libro libro){
        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Libro libroBD = em.find(Libro.class, libro.getId());
            if(libroBD == null){
                tx.rollback();
                return false;
            }
            libroBD.setTitulo(libro.getTitulo());
            libroBD.setAutor(libro.getAutor());
            libroBD.setAnioPublicacion(libro.getAnioPublicacion());
            libroBD.setPrecio(libro.getPrecio());
            tx.commit();
            return true;
        } catch(RuntimeException ex){
            if(tx.isActive()){
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }

    }

    public boolean eliminar(Long id){
        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Libro libroBD = em.find(Libro.class, id);
            if(libroBD == null){
                tx.rollback();
                return false;
            }
            em.remove(libroBD);
            tx.commit();
            return true;
        } catch(RuntimeException ex){
            if(tx.isActive()){
                tx.rollback();
            }
            throw ex;
        }finally {
            em.close();
        }
    }
}