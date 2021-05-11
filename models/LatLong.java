package models;
import  javax.persistence.*;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
@Entity
public class LatLong {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long Id;

    public String Location;

    public String Latitude;

    public String Longitude;


}