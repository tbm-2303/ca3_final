package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@NamedQueries(@NamedQuery(name = "Spot.deleteAllRows", query = "DELETE FROM Spot"))
@Table(name = "Spot")
public class Spot  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "timestamp")
    private LocalDate timeStamp;

    @ManyToOne
    private Location location;

    @ManyToOne
    private Timeline timeline;

    //Constructors
    public Spot(){

    }
    public Spot(String name, String description, LocalDate timeStamp, Location location, Timeline timeline){
        this.name = name;
        this.description = description;
        this.timeStamp = timeStamp;
        this.location = location;
        this.timeline = timeline;
    }




    //gettere and settere
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDate timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", location='" + location + '\'' +
                ", timeline="  + timeline +
                '}';
    }
}