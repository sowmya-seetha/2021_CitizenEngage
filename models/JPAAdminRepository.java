package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.xml.soap.Name;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.Exception;
import javax.persistence.NoResultException;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAAdminRepository implements AdminRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAAdminRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }


    @Override
    public CompletionStage<Admin> login(String Adminname,String Password) {
        return supplyAsync(() -> wrap(em -> log(em,Adminname,Password)), executionContext);
    }



    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }



    private Admin log(EntityManager em,String Adminname,String Password)
    {
        Admin admin = em.createQuery("select p from Admin p where p.Adminname= :Adminname and p.Password = :Password", Admin.class).setParameter("Adminname",Adminname).setParameter("Password",Password).getSingleResult();
        return admin;
    }


}
