package facades;

import dtos.RoleDTO;
import entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class RoleFacade {
    private static RoleFacade instace;
    private static EntityManagerFactory emf;

    private RoleFacade(){

    }
    public static RoleFacade getRoleFacade(EntityManagerFactory _emf){
        if (instace == null) {
            emf = _emf;
        }
        return instace;
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    public RoleDTO createRole(RoleDTO roleDTO){
        Role role = new Role (roleDTO.getRole());
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(role);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new RoleDTO(role);
    }

}
