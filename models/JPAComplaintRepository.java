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
import java.lang.*;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAComplaintRepository implements ComplaintRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAComplaintRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Complaint> add(Complaint complaint) {
        return supplyAsync(() -> wrap(em -> insert(em, complaint)), executionContext);
    }

//    public CompletionStage<Complaint> del(String Name) {
//        return supplyAsync(() -> wrap(em -> delete(em, Name)), executionContext);
//    }
//
//    public CompletionStage<Complaint> log(String EMAIL,String PASSWORD) {
//        return supplyAsync(() -> wrap(em -> login(em, EMAIL,PASSWORD)), executionContext);
//    }

    @Override
    public CompletionStage<Stream<Complaint>> alllist() {
        return supplyAsync(() -> wrap(em -> alllist(em)), executionContext);
    }

    public CompletionStage<String> viewAndClose(int Cid,String ClosedImage,String ClosedDescription,String Status,String ClosedAt){
        return supplyAsync(() -> wrap(em -> viewAndClose(em,Cid,ClosedImage,ClosedDescription,Status,ClosedAt)), executionContext);
    }

    public CompletionStage<Stream<Complaint>> allUserList(int id) {
        return supplyAsync(() -> wrap(em -> allUserList(em,id)), executionContext);
    }

    public CompletionStage<Stream<Complaint>> conditionUserList(int id,String Status) {
        return supplyAsync(() -> wrap(em -> conditionUserList(em,id,Status)), executionContext);
    }

    public CompletionStage<Stream<Complaint>> conditionlist(String Status) {
        return supplyAsync(() -> wrap(em -> conditionlist(em,Status)), executionContext);
    }

    public CompletionStage<Stream<Complaint>> recentlyclosed(String login) {
        return supplyAsync(() -> wrap(em -> recentlyclosed(em,login)), executionContext);
    }

    public CompletionStage<Stream<Complaint>> recentlycreated(String logout) {
        return supplyAsync(() -> wrap(em -> recentlycreated(em,logout)), executionContext);
    }
    public CompletionStage<Stream<Object>> userleaderboard() {
        return supplyAsync(() -> wrap(em -> userleaderboard(em)), executionContext);
    }
    public CompletionStage<Stream<Object>> topRankedComplaint() {
        return supplyAsync(() -> wrap(em -> topRankedComplaint(em)), executionContext);
    }
    public CompletionStage<Stream<Complaint>> adminIconMap() {
        return supplyAsync(() -> wrap(em -> adminIconMap(em)), executionContext);
    }
    public CompletionStage<Stream<Complaint>> userIconMap(int Id) {
        return supplyAsync(() -> wrap(em -> userIconMap(em,Id)), executionContext);
    }
    public CompletionStage<String> forgotPassword(String Email,String Password) {
        return supplyAsync(() -> wrap(em -> forgotPassword(em,Email,Password)), executionContext);
    }
    public CompletionStage<Stream<Object>> activeRegions() {
        return supplyAsync(() -> wrap(em -> activeRegions(em)), executionContext);
    }
    public CompletionStage<Stream<Complaint>> locationComplaints(String location) {
        return supplyAsync(() -> wrap(em -> locationComplaints(em,location)), executionContext);
    }
    public CompletionStage<Stream<Complaint>> topRankedList(String location,String category) {
        return supplyAsync(() -> wrap(em -> topRankedList(em,location,category)), executionContext);
    }
    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Complaint insert(EntityManager em, Complaint complaint) {
        em.persist(complaint);
        return complaint;
    }


//    private  Complaint delete(EntityManager em,String Name)
//    {
//        TypedQuery<Complaint> query = em.createQuery("select p from User p where p.Name= :Name", User.class);
//        User user =query.setParameter("Name", Name).getSingleResult();
//        em.remove(user);
//        return user;
//    }

//    private  User login(EntityManager em,String EMAIL,String PASSWORD)
//    {
//        TypedQuery<User> query = em.createQuery("select p from User p where p.Email= :EMAIL and p.Password = PASSWORD", User.class);
//        User user =query.setParameter("Email", EMAIL).getSingleResult();
//        em.remove(user);
//        return user;
//    }

    private Stream<Complaint> alllist(EntityManager em) {
        List<Complaint> complaint = em.createQuery("select p from Complaint p", Complaint.class).getResultList();
        return complaint.stream();
    }
    private Stream<Complaint> conditionlist(EntityManager em,String Status) {

        List<Complaint> complaint = em.createQuery("select p from Complaint p where p.Status =: Status", Complaint.class).setParameter("Status",Status).getResultList();
        return complaint.stream();
    }
    private Stream<Complaint> allUserList(EntityManager em,int id) {
        List<Complaint> complaint = em.createQuery("select p from Complaint p where p.Id=:id", Complaint.class).setParameter("id",id).getResultList();
        return complaint.stream();
    }
    private Stream<Complaint> conditionUserList(EntityManager em,int id,String Status) {
        List<Complaint> complaint = em.createQuery("select p from Complaint p where p.Id=:id and p.Status=:Status", Complaint.class).setParameter("id",id).setParameter("Status",Status).getResultList();
        return complaint.stream();
    }
    private Stream<Object> userleaderboard(EntityManager em) {
        System.out.println("Hello");
//        List<Complaint> complaint = em.createQuery("select Email,count(Email) as Count from Complaint group by Email", Complaint.class).getResultList();
       //em.createQuery("SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''))");
       //em.createQuery(\"SET sql_mode=''");
        List<Object> complaint = em.createQuery("select Email,Name,count(Email) as Count from Complaint group by Email order by count(Email) desc").getResultList();
        System.out.println("Hello");
        System.out.println(complaint.stream());
        return complaint.stream();
    }

    private Stream<Object> topRankedComplaint(EntityManager em) {
        List<Object> complaint = em.createQuery("select Location,Category,count(Location) as Count from Complaint group by Location,Category having count(Category)>1 order by count(Category) desc").getResultList();
        System.out.println("Hello");
        System.out.println(complaint.stream());
        return complaint.stream();
    }

    private String viewAndClose(EntityManager em,int Cid,String ClosedImage,String ClosedDescription,String Status,String ClosedAt)
    {
        int count = em.createQuery("Update Complaint set ClosedImage=:ClosedImage,ClosedDescription=:ClosedDescription,Status=:Status,ClosedAt=:ClosedAt where Cid=:Cid and Status=:existingStatus").setParameter("Cid",Cid).setParameter("existingStatus","Pending").setParameter("ClosedDescription",ClosedDescription).setParameter("ClosedImage",ClosedImage).setParameter("Status",Status).setParameter("ClosedAt",ClosedAt).executeUpdate();
        System.out.println("Count:"+count);
        if(count==0){
            return null;
        }
        else {
            return "OK";}
    }

    private Stream<Complaint> recentlycreated(EntityManager em,String logout) {
        List<Complaint> complaint = em.createQuery("select p from Complaint p where p.CreatedAt>=:logout and p.Status = : status", Complaint.class).setParameter("logout",logout).setParameter("status","Pending").getResultList();
        return complaint.stream();
    }

    private Stream<Complaint> recentlyclosed(EntityManager em,String login) {
        List<Complaint> complaint = em.createQuery("select p from Complaint p where p.ClosedAt>=:login", Complaint.class).setParameter("login",login).getResultList();
        return complaint.stream();
    }

    private Stream<Complaint> adminIconMap(EntityManager em) {
        //System.out.println("Hello");
        List<Complaint> complaint = em.createQuery("select c.Cid,c.Category,l.Latitude,l.Longitude from Complaint c , LatLong l where c.Location = l.Location and Status = :Status").setParameter("Status","Pending").getResultList();
        //System.out.println("Hello");

        System.out.println(complaint.stream());
        return complaint.stream();

    }

    private Stream<Complaint> userIconMap(EntityManager em,int Id) {
        //System.out.println("Hello");
        List<Complaint> complaint = em.createQuery("select c.Cid,c.Category,l.Latitude,l.Longitude from Complaint c , LatLong l where c.Location = l.Location and Status = :Status and c.Id =: Id").setParameter("Status","Pending").setParameter("Id",Id).getResultList();
        //System.out.println("Hello");

        System.out.println(complaint.stream());
        return complaint.stream();

    }
    private String forgotPassword(EntityManager em,String Email,String Password)
    {
        int count = em.createQuery("Update User set Password =: Password where Email=:Email").setParameter("Email",Email).setParameter("Password",Password).executeUpdate();
        if(count==0){
            return null;
        }
        else {
            return "OK";}
    }

    private Stream<Object> activeRegions(EntityManager em) {
        List<Object> complaint = em.createQuery("select Location,count(Location) as Count from Complaint group by Location having count(Location)>1 order by count(Location) desc").getResultList();
        System.out.println("Hello");
        System.out.println(complaint.stream());
        return complaint.stream();
    }

    private Stream<Complaint> locationComplaints(EntityManager em,String location) {
        List<Complaint> complaint = em.createQuery("select p from Complaint p where Location=:location", Complaint.class).setParameter("location",location).getResultList();
        return complaint.stream();
    }

    private Stream<Complaint> topRankedList(EntityManager em,String location,String category) {
        List<Complaint> complaint = em.createQuery("select p from Complaint p where Location=:location and Category =: category", Complaint.class).setParameter("location",location).setParameter("category",category).getResultList();
        return complaint.stream();
    }
}
