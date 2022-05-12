package facades;

import entities.Location;
import entities.Role;
import entities.Timeline;
import entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class RoleFacadeTest {
    private static EntityManagerFactory emf;
    private static RoleFacade roleFacade;
    Timeline timeline;



    public RoleFacadeTest (){

    }

    @BeforeAll
    static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        roleFacade = RoleFacade.getRoleFacade(emf);
    }

    @AfterAll
    public static void tearDownClass(){

    }

    @BeforeEach
    public void setUp(){
        EntityManager em = emf.createEntityManager();
        List<Role> basic = new ArrayList<>();
        basic.add(new Role("basic"));
        User user = new User("Hans", "pass");


        Location location = new Location("Q1", "La La Land", "Country");
        Location location1 = new Location("Q2", "Ingenmandsland", "Country");

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
}
