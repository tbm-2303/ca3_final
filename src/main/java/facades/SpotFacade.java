package facades;

import dtos.LocationDTO;
import dtos.SpotDTO;
import dtos.TimelineDTO;
import entities.Location;
import entities.Spot;
import entities.Timeline;
import errorhandling.ExceptionDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityResult;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpotFacade {
    private static SpotFacade instance;
    private static EntityManagerFactory emf;

    LocationFacade locationFacade = LocationFacade.getLocationFacade(emf);

    private SpotFacade(){

    }
    public static SpotFacade getSpotFacade(EntityManagerFactory _emf){
        if (instance == null){
            emf = _emf;
            instance = new SpotFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    //test er lavet og virker
    //skal måske laves om så til variabler i stedet for et spotDTO
    public SpotDTO createSpot(String name, String des, LocalDate localDate, String locationId, TimelineDTO timelineDTO){
        EntityManager em = getEntityManager();
        int timelineID = timelineDTO.getId();
        Timeline timeline = new Timeline(timelineDTO);
        timeline.setId(timelineID);
        //spotDTO.setTimeline(timeline);
        LocationDTO locationDTO = locationFacade.findLocation(locationId);
        Location spotLocation = new Location(locationDTO);
        Spot spot = new Spot(name, des, localDate, spotLocation, timeline);
        try{
            em.getTransaction().begin();;
            em.persist(spot);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
        return new SpotDTO(spot);
    }
    /*PSEUDO KODE til createSpot - gemmes
     * Create spot (Spot spot, Timeline timeline)
     *Spot spot = new spot...
     * Timeline timline = new Timeline
     * Find timeline, med entitymanager.
     * entity manager
     * try
     * begin
     * persist
     * commit
     * finally
     * close
     * return spot
     * */

    public List<SpotDTO> timeSortedSpots(TimelineDTO timelineDTO){
        EntityManager em = emf.createEntityManager();
        //Take in timeline as parameter
        //get timeline id
        int id = timelineDTO.getId();
        //search for spots where timeline id = the given id
        TypedQuery<Spot> query = em.createQuery("SELECT s FROM Spot s WHERE s.timeline.id = :id", Spot.class);
        query.setParameter("id", id);

        List <Spot> spots = query.getResultList();
        //sort the spots after date the oldest first - find the correct way for LocalDate
        List <SpotDTO> spotDTOS = SpotDTO.getDTOS(spots);
        spotDTOS.sort(Comparator.comparing(SpotDTO::getTimestamp));
        List<SpotDTO> sortedSpots = new ArrayList<>(spotDTOS);

        //sortedSpots.add();

        return sortedSpots;
    }

    //test virker
    public List<String> seeSpot(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            //find spot where id =
            TypedQuery <Spot> query = em.createQuery("SELECT s FROM Spot s WHERE s.id = :id", Spot.class);
            query.setParameter("id", id);
            Spot spot = query.getSingleResult();
            List<String> spotData = new ArrayList<>();
            String name = spot.getName();
            String description = spot.getDescription();
            String timeStamp = spot.getTimeStamp().toString();
            String location = spot.getLocation().toString();
            spotData.add(name);
            spotData.add(description);
            spotData.add(timeStamp);
            spotData.add(location);

            return spotData;
        }finally {
            em.close();
        }
    }
    //test virker, og endpoint mangler
    public synchronized SpotDTO editSpot(SpotDTO spotDTO){
        EntityManager em = emf.createEntityManager();
        Spot spotUpdated = em.find(Spot.class, spotDTO.getId());
        try{
            em.getTransaction().begin();
            spotUpdated.setName(spotDTO.getName());
            spotUpdated.setDescription(spotDTO.getDescription());
            spotUpdated.setTimeStamp(spotDTO.getTimestamp());
            spotUpdated.setLocation(spotDTO.getLocation());
            //spotUpdated.setId(spotDTO.getId());
            //spotUpdated.setTimeline(spotDTO.getTimeline());
            em.merge(spotUpdated);
            em.getTransaction().commit();
            return new SpotDTO(spotUpdated);
        }finally {
            em.close();
        }

    }
    //test virker, og endpoint mangler
    public String deleteSpot(Integer id){
        EntityManager em = emf.createEntityManager();
        Spot spot = em.find(Spot.class, id);
        if(spot == null){
            throw new WebApplicationException("The timeline does not exist");
        }
        else{
            try{
                em.getTransaction().begin();
                TypedQuery<Spot> query = em.createQuery("DELETE FROM Spot s WHERE s.id = :id", Spot.class);
                query.setParameter("id", id);
                query.executeUpdate();
                em.getTransaction().commit();
            }
            finally {
                em.close();
            }
        }
        return "The spot with id: " + id + " has been deleted";
    }


}