package facades;

import dtos.TimelineDTO;
import entities.Location;
import entities.Role;
import entities.Timeline;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimelineFacadeTest {

    private static EntityManagerFactory emf;
    private static TimelineFacade timelineFacade;
    Timeline timeline;


    public TimelineFacadeTest(){

    }

    @BeforeAll
    static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        timelineFacade = TimelineFacade.getTimelineFacade(emf);
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


    @Test
    //Virker
    public void createTimelineTest(){
        String name = "New Timeline";
        String description = "The newest timeline";
        String startDate = "1900";
        String endDate = "2022";
        List<Role> basic = new ArrayList<>();
        basic.add(new Role("test"));
        User user = new User("Hans", "pass");
        Timeline timeline = new Timeline(name, description, startDate, endDate, user);
        TimelineDTO timelineDTO = new TimelineDTO(timeline);

        String expected = timelineDTO.getName();
        String actual = timelineFacade.createTimeline(timelineDTO).getName();

        assertEquals(expected, actual);
    }

    //Virker
    @Test
    public void getAllTimelines(){
        EntityManager em = emf.createEntityManager();
        Role role = new Role("test");
        List<Role> roles = new ArrayList<>();
        User user= new User("Dorte", "Kodeord1");
        Timeline timeline = new Timeline("My life", "This is a timeline about my life", "1977", "2051", user);
        try{
            em.getTransaction().begin();
            em.persist(user);
            em.persist(timeline);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        List<TimelineDTO> timelinesFound = timelineFacade.getAll(user);
        String actual = timelinesFound.get(0).getName();

        String expected = timeline.getName();

        assertEquals(expected, actual);
    }

    @Disabled
    @Test
    public void testTimelineCount() throws Exception{
        assertEquals(1, timelineFacade.getTimelineCount());
    }

    @Test
    public void editIntervalTest(){
        Integer id = timeline.getId();

        String expected = "1900" + "2022";
        String actual = timelineFacade.editInterval(id, "1900", "2022");

        assertEquals(expected,actual);
    }

    @Test
    public void seeTimelineTest(){
        Integer id = timeline.getId();

        String expected = timeline.getName();
        String actual = timelineFacade.seeTimeline(id).getName();

        assertEquals(expected, actual);
    }

    @Test
    void deleteTimelineTest(){
        Integer id = timeline.getId();
        String actual = timelineFacade.deleteTimeline(id);
        String expected = "The timeline has been deleted with id: " + id;

        assertEquals(expected, actual);
    }
}

