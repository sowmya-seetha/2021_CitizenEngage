package models;
import  javax.persistence.*;


@Entity
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String Name;

    public String Password;

    public String Category;
}