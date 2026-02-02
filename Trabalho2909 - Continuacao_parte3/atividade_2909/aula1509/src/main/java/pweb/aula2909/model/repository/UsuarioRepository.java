package pweb.aula2909.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pweb.aula2909.model.entity.Usuario;

import java.util.List;

@Repository
public class UsuarioRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void salvar(Usuario usuario) {
        em.persist(usuario);
        em.flush();
    }

    public Usuario findByLogin(String login) {
        Query query = em.createQuery("FROM Usuario u WHERE u.login = :login");
        query.setParameter("login", login);
        List<Usuario> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public boolean existsByLogin(String login) {
        Query query = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.login = :login");
        query.setParameter("login", login);
        return (Long) query.getSingleResult() > 0;
    }
}



