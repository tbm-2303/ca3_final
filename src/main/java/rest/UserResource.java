package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import entities.Role;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade FACADE =  UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Context
    SecurityContext securityContext;
    @Context
    private UriInfo context;

    //test er ikke lavet
    @POST
    @Path("/createuser")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String createUser(String user){
        UserDTO userDTO = GSON.fromJson(user,UserDTO.class);
        List<Role> roles = new ArrayList<>();
        Role role = new Role("basic");
        roles.add(role);
        userDTO.setRoleList(roles);
        UserDTO createdUser = FACADE.create(userDTO);
        return GSON.toJson(createdUser);
    }

    @POST
    @Path("/createadmin")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String createAdmin(String user){
        UserDTO userDTO = GSON.fromJson(user, UserDTO.class);
        List<Role> roles = new ArrayList<>();
        Role role = new Role("admin");
        roles.add(role);
        userDTO.setRoleList(roles);
        UserDTO createdUser = FACADE.create(userDTO);
        return GSON.toJson(createdUser);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user")
    @RolesAllowed("basic")
    public String getFromUser(){
        String thisUser = securityContext.getUserPrincipal().getName();
        return "{msg:" + "Hello user:" + thisUser + "}";

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/admin")
    @RolesAllowed("admin")
    public String getFromAdmin(){
        String thisAdmin = securityContext.getUserPrincipal().getName();
        return "{msg:" + "Hello admin:" + thisAdmin + "}";

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/count")
    public String count(){
        //String count =
        return "hej med dig";
    }


    @DELETE
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response delete(@PathParam("username") String username) throws NotFoundException, errorhandling.NotFoundException {
        UserDTO userDTO = new UserDTO(FACADE.delete(username));
        return Response.ok().entity(GSON.toJson(userDTO)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/allUsers")
    @RolesAllowed("admin")
    public String getAllUsers(){
        List<UserDTO>userDTOList = FACADE.getAllUsers();
        return "All users:" + userDTOList;

    }

}
