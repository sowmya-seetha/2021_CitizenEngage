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
public class JPAWardRepository implements WardRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAWardRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }


    @Override
    public CompletionStage<Ward> login(String Name,String Password) {
        return supplyAsync(() -> wrap(em -> log(em,Name,Password)), executionContext);
    }



    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }



    private Ward log(EntityManager em,String Name,String Password)
    {
        Ward ward = em.createQuery("select p from user p where p.Name= :Name and p.Password = :Password", Ward.class).setParameter("Name",Name).setParameter("Password",Password).getSingleResult();
        return ward;
    }


}