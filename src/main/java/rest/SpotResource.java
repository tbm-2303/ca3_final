package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.SpotDTO;
import dtos.TimelineDTO;
import entities.Timeline;
import facades.SpotFacade;
import facades.TimelineFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.List;

public class SpotResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final SpotFacade FACADE = SpotFacade.getSpotFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @DELETE
    @Path("/delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("basic")
    public String deleteSpot(@PathParam("id") Integer id){
        return "{\"result\":\"" + FACADE.deleteSpot(id);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("basic")
    @Path("/createSpot")
    public String createSpot(String spot, String name, String description, LocalDate timeStamp, String locationID, TimelineDTO timeline){
        SpotDTO spotDTO = GSON.fromJson(spot, SpotDTO.class);
        SpotDTO createdSpot = FACADE.createSpot(name, description, timeStamp, locationID, timeline);
        return GSON.toJson(createdSpot);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("basic")
    @Path("/sortedSpots")
    public String sortedSpots(String timeline){
        TimelineDTO timelineDTO = GSON.fromJson(timeline,TimelineDTO.class );
        List<SpotDTO> spotDTOList = FACADE.timeSortedSpots(timelineDTO);
        return "{\"sorted spots\":\"" + spotDTOList;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("basic")
    @Path("seeSpot/{id}")
    public String seeSpot(@PathParam("id") Integer id){
        List<String> spotData = FACADE.seeSpot(id);
        return GSON.toJson(spotData);
    }



}
