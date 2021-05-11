package models;
import  javax.persistence.*;


@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String Name;

    public String Email;

    public String Mobile;

    public String Password;

}
