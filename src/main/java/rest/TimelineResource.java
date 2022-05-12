package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.TimelineDTO;
import dtos.UserDTO;
import entities.User;
import facades.TimelineFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.ejb.ConcurrencyManagement;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("timeline")
public class TimelineResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final TimelineFacade FACADE = TimelineFacade.getTimelineFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    //ikke testet
    //id'et skal bruges til at oprette timelinen
    //det skal sættes i frontenden - laves med en metode
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("basic")
    @Path("/create")
    public String createTimeline(String timeline){
        TimelineDTO timelineDTO = GSON.fromJson(timeline, TimelineDTO.class);
        TimelineDTO createdTimeline = FACADE.createTimeline(timelineDTO);
        return GSON.toJson(createdTimeline);

    }
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    @Path("/allTimelines")
    public String getAllTimelines(String user){
        UserDTO userDTO = GSON.fromJson(user, UserDTO.class);
        List<TimelineDTO> timelineDTOList = FACADE.getAll(userDTO);
        return "All timelines: " + timelineDTOList;
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("basic")
    public String deleteTimeline(@PathParam("id") Integer id){
        return "{\"result\":\"" + FACADE.deleteTimeline(id) + "\"}";
    }
    //see timeline

    @GET
    @Path("/seeTimeline/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("basic")
    public String seeTimeline(@PathParam("id") Integer id){
        return GSON.toJson(FACADE.seeTimeline(id));
    }

    @POST
    @Path("/editTimeline/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("basic")
    public String editTimeline (@PathParam("id") Integer id, String startDate, String endDate){
        TimelineDTO updated = FACADE.editInterval(id, startDate, endDate);
        return GSON.toJson(updated);
    }
    //edit interval
}