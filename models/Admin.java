package models;
import  javax.persistence.*;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long Id;

    public String Adminname;

    public String Password;

    public String Department;


}
