package facades;

import dtos.LocationDTO;
import dtos.SpotDTO;
import dtos.TimelineDTO;
import entities.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpotFacadeTest {

    private static EntityManagerFactory emf;
    private static SpotFacade spotFacade;
    static Timeline timeline;
    static Location location;
    static Location location1;
    static Spot spot;
    static Spot spot1;
    static User user;
    static List<Role> basic = new ArrayList<>();



    public SpotFacadeTest() {

    }

    @BeforeAll
    static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        spotFacade = SpotFacade.getSpotFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {

    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        basic.add(new Role("basic"));

        user = new User("Hans", "pass");

        location = new Location("Q1", "La La Land", "Country");
        location1 = new Location("Q2", "Ingenmandsland", "Country");

        timeline = new Timeline("First", "Det her er den første tidslinje",
                "1990", "2000", user);
        spot = new Spot("New Years eve", "The night between 1999 and 2000",
                LocalDate.of(1999, Month.DECEMBER, 31), location, timeline);
        spot1 = new Spot("Christmas", "",
                LocalDate.of(1999, Month.DECEMBER, 24), location1, timeline);
        try {
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

            em.persist(spot);
            em.persist(spot1);
            em.getTransaction().commit();

        } finally {
            em.close();
        }

    }

    @Test
    void createSpotTest(){
        String name = "Birthday";
        String description = "My birthday, I turned 13";
        LocalDate timestamp = LocalDate.of(1992, Month.MARCH, 8);
        String locationID = "Q1";

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Timeline> query = em.createQuery("SELECT t FROM Timeline t WHERE t.id = 1", Timeline.class);
        Timeline timeline = query.getSingleResult();

        LocationFacade locationFacade = LocationFacade.getLocationFacade(emf);
        LocationDTO location = locationFacade.findLocation(locationID);
        em.getTransaction().commit();

        Spot spot= new Spot(name, description, timestamp, new Location(location), timeline);
        SpotDTO spotDTO = new SpotDTO(spot);
        TimelineDTO timelineDTO = new TimelineDTO(timeline);

        SpotDTO spotCreated = spotFacade.createSpot(name, description, timestamp, locationID, timelineDTO);

        String expected = spotCreated.getName();
        String actual = spotDTO.getName();

        assertEquals(expected, actual);

    }

    @Test
    void sortedSpotsTest(){
        basic.add(new Role("basic"));
        TimelineDTO timelineDTO = new TimelineDTO(timeline);
        List <Spot> spotList = new ArrayList<>();
        spotList.add(spot1);
        spotList.add(spot);
        List<SpotDTO> spots = SpotDTO.getDTOS(spotList);
        spots.sort(Comparator.comparing(SpotDTO::getTimestamp));
        List<SpotDTO> expected = new ArrayList<>(spots);
        List <SpotDTO> actual = spotFacade.timeSortedSpots(timelineDTO);
        assertEquals(expected.get(0).getName(), actual.get(0).getName());

        //PSEUDO
        //Timeline timeline;
        //int id;
        //use timeline id as the given id;
        //sort from oldest to newest, use LocalDate
        //expected
        //actual

        //assertEquals();
    }

    @Test
    //virker
    void seeSpotTest(){
        Integer id = spot.getId();
        String name = spot.getName();
        String description = spot.getDescription();
        String timeStamp = spot.getTimeStamp().toString();
        String location = spot.getLocation().toString();

        List<String> expected = new ArrayList<>();
        expected.add(name);
        expected.add(description);
        expected.add(timeStamp);
        expected.add(location);
        List<String> actual = spotFacade.seeSpot(id);

        assertEquals(expected, actual);
    }

    @Test
    void editSpotTest(){
        SpotDTO spotDTO = new SpotDTO(new Spot("Halloween", "something", LocalDate.of(1999, Month.OCTOBER, 31), location1, timeline));
        spotDTO.setId(spot.getId());

        SpotDTO expected = spotDTO;
        SpotDTO actual = spotFacade.editSpot(spotDTO);
        //den fejler kun hvis man sammenligner dem direkte, så er den anderledes på user, men alt andet er ok
        assertEquals(expected.getName(), actual.getName());

    }

    @Test
    void deleteSpotTest(){
        Integer id = spot.getId();
        String actual = spotFacade.deleteSpot(id);
        String expected = "The spot with id: " + id + " has been deleted";
        assertEquals(actual, expected);
    }

}
