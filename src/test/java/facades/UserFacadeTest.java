package facades;

import entities.Role;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade userFacade;

    public UserFacadeTest(){

    }

    @BeforeAll
    public static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        userFacade = UserFacade.getUserFacade(emf);
    }

    @AfterAll
    public static void teardownClass(){
        // clean up database after test is done,
        // or use a persistence unit (test db) with drop-and-create to start up clean on every test
    }

    @BeforeEach
    public void setUp(){
        EntityManager em = emf.createEntityManager();
        List<Role> admin = new ArrayList<>();
        List<Role> basic = new ArrayList<>();
        admin.add(new Role("admin"));
        basic.add(new Role("basic"));
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Spot.deleteAllRows").executeUpdate();
            em.createNamedQuery("Location.deleteAllRows").executeUpdate();
            em.createNamedQuery("Timeline.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.persist(new User("Bente", "h"));
            em.persist(new User("Poul", "e"));
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown(){
        //Remove any data after running each test
    }

    @Disabled
    @Test
    public void testCount() throws Exception{

        assertEquals(2, userFacade.getUserCount());
    }

    //write your own tests here
}