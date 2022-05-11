package facades;

import dtos.LocationDTO;
import entities.Location;
import entities.Role;
import entities.Timeline;
import entities.User;
import errorhandling.NotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationFacadeTest {

    private static EntityManagerFactory emf;
    private static LocationFacade locationFacade;
    Location location;
    Location location1;
    Timeline timeline;


    @BeforeAll
    static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        locationFacade = LocationFacade.getLocationFacade(emf);
    }

    @AfterAll
    public static void tearDownClass(){

    }

    @BeforeEach
    public void setUp(){
        EntityManager em = emf.createEntityManager();
        List<Role> basic = new ArrayList<>();
        basic.add(new Role("basic"));
        User user = new User("Hans", "pass", "email1", basic);


        location = new Location("Q1", "La La Land", "Country");
        location1 = new Location("Q2", "Ingenmandsland", "Country");

        timeline = new Timeline("First", "Det her er den første tidslinje",
                "1990", "2000", user);

        try{
            em.getTransaction().begin();
            em.createNamedQuery("Spot.deleteAllRows").executeUpdate();
            em.createNamedQuery("Location.deleteAllRows").executeUpdate();
            em.createNamedQuery("Timeline.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.persist(user);
            em.persist(location);
            em.persist(location1);
            em.persist(timeline);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    @Test
    //virker
    void createLocation(){
        String id = "ab13";
        String name = "Wonderland";
        String type = "Country";
        LocationDTO expected = new LocationDTO(id, name, type);
        LocationDTO actual = locationFacade.createLocation(id, name, type);

        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    //virker
    public void deleteLocationTest() throws NotFoundException {
        String expected = "Deleted location with id: " + location1.getId();
        String id = location1.getId();
        String actual = locationFacade.deleteLocation(id);

        assertEquals(expected, actual);
    }
}
