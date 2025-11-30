package pweb.aula2909.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pweb.aula2909.model.entity.PessoaFisica;

import java.util.List;

@Repository
public class PessoaFisicaRepository {

    @PersistenceContext
    private EntityManager em;

    public List<PessoaFisica> pessoasFisicas() {
        Query query = em.createQuery("from PessoaFisica");
        return query.getResultList();
    }

    public PessoaFisica buscarPorId(Long id){
        return em.find(PessoaFisica.class, id);
    }


    @Transactional
    public void salvar(PessoaFisica pessoa) {
        em.persist(pessoa);
    }

    @Transactional
    public void atualizar(PessoaFisica pessoa) {
        em.merge(pessoa);
    }

    @Transactional
    public void excluir(Long id) {
        PessoaFisica pessoa = buscarPorId(id);
        if (pessoa != null) {
            em.remove(pessoa);
        }
    }

    public List<PessoaFisica> filtrarPorNome(String nome) {
        Query query = em.createQuery("FROM PessoaFisica pf WHERE LOWER(pf.nome) LIKE LOWER(:nome)");
        query.setParameter("nome", "%" + nome + "%");
        return query.getResultList();
    }


}
