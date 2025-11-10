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
        Query query = em.createQuery("from Produto");
        return query.getResultList();
    }

    public Produto buscarPorId(Long id){
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
        Produto produto = buscarPorId(id);
        if (produto != null) {
            em.remove(produto);
        }
    }

}
