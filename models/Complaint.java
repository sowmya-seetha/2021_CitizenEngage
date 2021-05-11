package models;
import  javax.persistence.*;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public int Cid;

    public int Id;

    public String Name;

    public String Email;

    public String Category;

    public String CreatedImage;

    public String ClosedImage;

    public String Location;

    public String CreatedDescription;

    public String ClosedDescription;

    public String CreatedAt;

    public String ClosedAt;

    public String Status;



}
