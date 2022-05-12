/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.RoleDTO;
import dtos.SpotDTO;
import dtos.TimelineDTO;
import dtos.UserDTO;
import entities.*;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Populator {

    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        UserFacade userFacade = UserFacade.getUserFacade(emf);
        RoleFacade roleFacade = RoleFacade.getRoleFacade(emf);
        TimelineFacade timelineFacade = TimelineFacade.getTimelineFacade(emf);
        SpotFacade spotFacade = SpotFacade.getSpotFacade(emf);

        RoleDTO roleBasic = roleFacade.createRole(new RoleDTO(new Role("basic")));
        RoleDTO roleAdmin = roleFacade.createRole(new RoleDTO(new Role("admin")));

        List<Role> roleListBasic = new ArrayList<>();
        List<Role> roleListAdmin = new ArrayList<>();

        roleListBasic.add(new Role(roleBasic));
        roleListAdmin.add(new Role(roleAdmin));


        UserDTO user1 = userFacade.create(new UserDTO(new User("AnnikaRosenvinge", "ca3sysopgave")));
        UserDTO user2 = userFacade.create(new UserDTO(new User("TimothyBusk", "ca3eksamensopgave")));
        UserDTO user3 = userFacade.create(new UserDTO(new User("LouiseSitting", "eksamensopgave")));
        UserDTO user4 = userFacade.create(new UserDTO(new User("OliverJenbo", "syseksamensopgave")));

        user1.setRoleList(roleListBasic);
        user2.setRoleList(roleListAdmin);
        user3.setRoleList(roleListAdmin);
        user4.setRoleList(roleListBasic);



        TimelineDTO timelineDTO = timelineFacade.createTimeline(new TimelineDTO(new Timeline("Marvel Cinematic Universe",
                "This is a timeline over the movies, series, oneshot, after-creditscene and more", "1920", "2030", new User(user1))));
        spotFacade.createSpot("The creation of the first Iron Man", "This is the scences where Tony Stark creates the first Iron Man Suit", LocalDate.of(2008,Month.MAY, 20), "Q889", timelineDTO);
        spotFacade.createSpot("I am Iron Man", "Tony Stark tells the world he is Iron Man", LocalDate.of(2008, Month.OCTOBER, 25), "Q30", timelineDTO);
    }
    
    public static void main(String[] args) {
        populate();
    }
}
