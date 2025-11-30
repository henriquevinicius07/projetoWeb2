package pweb.aula2909.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pweb.aula2909.model.entity.Venda;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class VendaRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Venda> listar() {
        return em.createQuery("FROM Venda v ORDER BY v.data DESC", Venda.class)
                .getResultList();
    }

    public Venda buscarPorId(Long id) {
        return em.find(Venda.class, id);
    }

    @Transactional
    public void salvar(Venda venda) {
        em.persist(venda);
    }

    public List<Venda> listarPorCliente(Long clienteId) {
        return em.createQuery("""
                SELECT v FROM Venda v 
                WHERE v.cliente.id = :id
                ORDER BY v.data DESC
                """, Venda.class)
                .setParameter("id", clienteId)
                .getResultList();
    }

    public List<Venda> listarPorData(LocalDateTime inicio, LocalDateTime fim) {
        return em.createQuery("""
                SELECT v FROM Venda v 
                WHERE v.data BETWEEN :inicio AND :fim
                ORDER BY v.data DESC
                """, Venda.class)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();
    }

    public List<Venda> listarPorClienteEData(Long clienteId, LocalDateTime inicio, LocalDateTime fim) {
        return em.createQuery("""
                SELECT v FROM Venda v 
                WHERE v.cliente.id = :id
                AND v.data BETWEEN :inicio AND :fim
                ORDER BY v.data DESC
                """, Venda.class)
                .setParameter("id", clienteId)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();
    }

}



