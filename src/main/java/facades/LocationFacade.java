package facades;

import dtos.LocationDTO;
import entities.Location;
import errorhandling.API_Exception;
import errorhandling.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class LocationFacade {
    private static LocationFacade instance;
    private static EntityManagerFactory emf;

    private LocationFacade(){

    }

    public static LocationFacade getLocationFacade(EntityManagerFactory _emf){
        if(instance == null){
            emf=_emf;
            instance = new LocationFacade();
        }
        return instance;
    }
    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
    //test og endpoint mangler - ed enpoint skal det sættes så det kun er admin der kan
    public LocationDTO createLocation(String id, String name, String type){
        Location location = new Location(id, name, type);
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
        return new LocationDTO(location);
    }

    public String deleteLocation(String id) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Location> query = em.createQuery("DELETE FROM Location l WHERE l.id=:id", Location.class);
        query.setParameter("id", id);
        return ("Deleted location with id: " + id);
    }

    public LocationDTO findLocation(String locationID){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            TypedQuery<Location> query = em.createQuery("SELECT l FROM Location l WHERE l.id = :id", Location.class);
            query.setParameter("id", locationID);
            Location location = query.getSingleResult();
            em.getTransaction().commit();
            return new LocationDTO(location);
        }
        finally {
            em.close();
        }

        /*
        * EntityManager em = emf.createEntityManager();
        try {
            User user1 = em.find(User.class, user.getUserName());
            String username = user.getUserName();
            TypedQuery<Timeline> query = em.createQuery("SELECT t FROM Timeline t WHERE t.user.userName = :username", Timeline.class);
            query.setParameter("username", username);
            List<Timeline> timelines = query.getResultList();
            return TimelineDTO.getDtos(timelines);
        }finally {
            em.close();
        }*/

    }

}