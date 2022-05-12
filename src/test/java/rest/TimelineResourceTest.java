package rest;

import dtos.TimelineDTO;
import dtos.UserDTO;
import entities.Role;
import entities.Spot;
import entities.Timeline;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimelineResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    private static Timeline timeline1, timeline2;
    private static User user1, user2;



    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp(){
        EntityManager em = emf.createEntityManager();
        List<Role> admin = new ArrayList<>();
        List<Role> basic = new ArrayList<>();
        admin.add(new Role("admin"));
        basic.add(new Role("basic"));
        List<Spot> spotlist = new ArrayList<>();
        Spot spot1 = new Spot();
        Spot spot2 = new Spot();
        spotlist.add(spot1);
        spotlist.add(spot2);
        timeline1 = new Timeline("timeline1", "First timeline", "1900", "2020", spotlist, user1);
        timeline2 = new Timeline("timeline2", "Second timeline", "1800", "2022", spotlist, user2);
        try{
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(timeline1);
            em.persist(timeline2);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }
    
    private static String securityToken;

    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void getAllTimelinesTest(){
        //brug login
        List<TimelineDTO> timelineDTOList;
        timelineDTOList = given()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/info/allTimelines")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("", TimelineDTO.class);

                assertEquals(2, timelineDTOList.size());

    }
}
