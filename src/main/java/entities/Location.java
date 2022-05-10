package entities;

import dtos.LocationDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries(@NamedQuery(name = "Location.deleteAllRows", query = "DELETE FROM Location "))
@Table(name = "Location")
public class Location implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @OneToMany(mappedBy = "location", cascade = {CascadeType.PERSIST})
    private List<Spot> spotList = new ArrayList<>();

    public Location() {
    }

    public Location(String id, String name, String type, List<Spot> spotList) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.spotList = spotList;
    }

    public Location(String id, String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Location(LocationDTO locationDTO) {
        this.id = locationDTO.getId();
        this.name = locationDTO.getName();
        this.type = locationDTO.getType();
    }

    public Location(String name, String type) {
        this.name  = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Spot> getSpotList() {
        return spotList;
    }

    public void setSpotList(List<Spot> spotList) {
        this.spotList = spotList;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}