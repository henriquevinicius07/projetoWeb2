package pweb.aula2909.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import pweb.aula2909.model.entity.Venda;

import java.util.List;

@Repository
public class VendaRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Venda> listar() {
        Query query = em.createQuery("from Venda");
        return query.getResultList();
    }

    public Venda buscarPorId(Long id) {
        return em.find(Venda.class, id);
    }
}
