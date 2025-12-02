package pweb.aula2909.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pweb.aula2909.model.entity.Produto;

import java.util.List;

@Repository
public class ProdutoRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Produto> produtos() {
        Query q = em.createQuery("from Produto");
        return q.getResultList();
    }

    public Produto buscarPorId(Long id) {
        return em.find(Produto.class, id);
    }

    @Transactional
    public void salvar(Produto produto) {
        em.persist(produto);
    }

    @Transactional
    public void atualizar(Produto produto) {
        em.merge(produto);
    }

    @Transactional
    public void excluir(Long id) {
        Produto p = buscarPorId(id);
        if (p != null) em.remove(p);
    }

    public List<Produto> buscarPorNome(String nome) {
        Query q = em.createQuery("from Produto p where lower(p.descricao) like lower(:nome)");
        q.setParameter("nome", "%" + nome + "%");
        return q.getResultList();
    }
}
