package rest;

import dtos.UserDTO;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

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
        user1 = new User("bente", "password");
        user2 = new User("Hans", "pass");
        try{
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(user1);
            em.persist(user2);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }
    @Disabled
    @Test
    public void testDummyMsg() throws Exception{
        given()
                .contentType("application/json")
                .get("/info/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World"));
    }


    @Test
    public void testServerIsUp(){
        System.out.println("Server for testing is up");
        given().when().get("/user").then().statusCode(200);
    }

    @Disabled
    @Test
    //HUSK AT TJEKKE SENERE NÅR HJEMMESIDEN KØRER!!!
    public void getAllUsersTest(){
        List<UserDTO> userDTOList;

        userDTOList = given()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/user/allUsers")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("", UserDTO.class);

                assertEquals(2, userDTOList.size());
    }
    //create user
    //create admin
    //deleteuser
    @Test
    public void createUserTest () throws Exception{
        System.out.println("Creating basic user");
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UserDTO(new User("FedeFinnOgFunnyBoys", "FedeFinn")))
                .when()
                .post("user/createuser")
                .then()
                .body("username", equalTo("FedeFinnOgFunnyBoys"));



    }

    @Test
    public void createAdminTest() throws Exception{
        System.out.println("Creating admin user");
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UserDTO(new User("Frank", "FranksPassword")))
                .when()
                .post("user/createAdmin")
                .then()
                .body("user_name", equalTo("Frank"));
                //.body("password", equalTo("FranksPassword"))

    }
}
