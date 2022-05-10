package dtos;

import entities.Location;
import entities.Spot;
import entities.Timeline;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SpotDTO {
    private Integer id;
    private String name;
    private String description;
    private LocalDate timestamp;

    private Location location;
    private Timeline timeline;

    public SpotDTO(){

    }

    public SpotDTO(Spot spot){
        this.id = spot.getId();
        this.name = spot.getName();
        this.description = spot.getDescription();
        this.timestamp = spot.getTimeStamp();
        this.location = spot.getLocation();
        this.timeline = spot.getTimeline();
    }

    public static List<SpotDTO> getDTOS(List <Spot> spot){
        List<SpotDTO> spotDTOS = new ArrayList<>();
        if(spot!=null){
            spot.forEach(s -> spotDTOS.add(new SpotDTO(s)));
        }
        return spotDTOS;
    }

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

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
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
        return "SpotDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", location=" + location +
                ", timeline=" + timeline +
                '}';
    }
}