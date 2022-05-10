package facades;

import com.google.gson.JsonObject;
import dtos.UserDTO;
import entities.Role;
import entities.Timeline;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import errorhandling.NotFoundException;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;
import utils.Utility;

import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public JsonObject getRandomCatFact() {
        return Utility.fetchData("https://catfact.ninja/fact");
    }

    public JsonObject getRandomJoke() {
        return Utility.fetchData("https://api.chucknorris.io/jokes/random");
    }


    //-------------------------------------------nye metoder

    //dette er kun for alm bruger(altid user role)
    public UserDTO create(UserDTO userDTO){
        EntityManager em = getEntityManager();
        User user = new User(userDTO.getUserName(), userDTO.getPassword());
        Role role = new Role("user");
        user.addRole(role);
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }

    public UserDTO getById(String username) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);
        return new UserDTO(user);
    }

    public Long getUserCount(){
        EntityManager em = getEntityManager();
        try{
            return (Long) em.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
        }finally{
            em.close();
        }
    }
    public User delete(String username) throws NotFoundException {
        EntityManager em = getEntityManager();
        User user = em.find(User.class, username);
        if (user == null)
            throw new NotFoundException("Could not remove Profile with id: "+username);

        em.getTransaction().begin();

        List<Timeline> timelineList = user.getTimelinelist();
        for (Timeline tl : timelineList) {
            user.removeTimeline(tl);
        }
        em.merge(user);
        em.remove(user);
        em.getTransaction().commit();
        return user;
    }


    public List<UserDTO> getAllUsers(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> userList = query.getResultList();
        return UserDTO.getDtos(userList);
    }

}
