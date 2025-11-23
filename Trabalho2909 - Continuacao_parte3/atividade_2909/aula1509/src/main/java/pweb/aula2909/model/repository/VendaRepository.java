package pweb.aula2909.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import pweb.aula2909.model.entity.Venda;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<Venda> listarPorData(LocalDate data) {
        // início do dia selecionado
        LocalDateTime inicio = data.atStartOfDay();

        // final do dia selecionado (23:59:59.999)
        LocalDateTime fim = data.atTime(23, 59, 59, 999999999);

        Query query = em.createQuery(
                "SELECT v FROM Venda v WHERE v.data BETWEEN :inicio AND :fim ORDER BY v.data ASC"
        );

        query.setParameter("inicio", inicio);
        query.setParameter("fim", fim);

        return query.getResultList();
    }
}


