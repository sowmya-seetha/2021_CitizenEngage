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
public class JPAUserRepository implements UserRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAUserRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<User> add(User user) {
        return supplyAsync(() -> wrap(em -> insert(em, user)), executionContext);
    }
@Override
    public CompletionStage<User> del(String Name) {
        return supplyAsync(() -> wrap(em -> delete(em, Name)), executionContext);
    }
    public CompletionStage<User> profile(int Id) {
        return supplyAsync(() -> wrap(em -> profile(em, Id)), executionContext);
    }

    public CompletionStage<User> login(String EMAIL,String PASSWORD) {
        return supplyAsync(() -> wrap(em -> log(em,EMAIL,PASSWORD)), executionContext);
    }

    public CompletionStage<String> editProfile(int Id,String Name,String Email,String Mobile) {
        return supplyAsync(() -> wrap(em -> editProfile(em,Id,Name,Email,Mobile)), executionContext);
    }

    @Override
    public CompletionStage<Stream<User>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private User insert(EntityManager em, User user) {
        em.persist(user);
        return user;
    }

    private User log(EntityManager em,String Email,String Password)
    {
        User user = em.createQuery("select p from User p where p.Email= :Email and p.Password = :Password", User.class).setParameter("Email",Email).setParameter("Password",Password).getSingleResult();
        return user;
    }

    private  User delete(EntityManager em,String Name)
    {
        TypedQuery<User> query = em.createQuery("select p from User p where p.Name= :Name", User.class);
        User user =query.setParameter("Name", Name).getSingleResult();
        em.remove(user);
        return user;
    }
//  @Override
//    public  User login(String Email,String Password)
//    {
//        EntityManagerFactory em = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
//        EntityManager em1 = em.createEntityManager();
//        em1.getTransaction().begin();
//
//        User user = em1.createQuery("select p from User p where p.Email= :Email and p.Password = :Password", User.class).setParameter("Email",Email).setParameter("Password",Password).getSingleResult();
//
//
//        return user;
//    }
//@Override
//    public User profile(int Id) throws NoResultException {
//        try{
//            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("");
//            EntityManager em= entityManagerFactory.createEntityManager();
//            em.getTransaction().begin();
//
//            User foundUser = em.createQuery("select p from User p where Id=:Id ",User.class).setParameter("Id", Id).getSingleResult();
//            //em.remove(foundPerson);
//            return foundUser;
//        }
//        catch(NoResultException e){
//            return null;
//        }
//
//
//}
    public User profile(EntityManager em,int Id) throws NoResultException {
        try{
//            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("");
//            EntityManager em= entityManagerFactory.createEntityManager();
//            em.getTransaction().begin();

            User foundUser = em.createQuery("select p from User p where Id=:Id ",User.class).setParameter("Id", Id).getSingleResult();
            //em.remove(foundPerson);
            return foundUser;
        }
        catch(NoResultException e){
            return null;
        }


    }

    private String editProfile(EntityManager em,int Id,String Name,String Email,String Mobile)
    {
      int count = em.createQuery("Update User set Name =:Name,Email= :Email, Mobile = :Mobile where Id=:Id").setParameter("Name",Name).setParameter("Email",Email).setParameter("Mobile",Mobile).setParameter("Id",Id).executeUpdate();
      int count1 = em.createQuery("Update Complaint set Email= :Email ,Name = :Name where Id=:Id").setParameter("Email",Email).setParameter("Name",Name).setParameter("Id",Id).executeUpdate();

        if(count==0 || count1==0){
            return null;
        }
        else {
            return "OK";
        }
    }

    private Stream<User> list(EntityManager em) {
        List<User> users = em.createQuery("select p from User p", User.class).getResultList();
        return users.stream();
    }
}
