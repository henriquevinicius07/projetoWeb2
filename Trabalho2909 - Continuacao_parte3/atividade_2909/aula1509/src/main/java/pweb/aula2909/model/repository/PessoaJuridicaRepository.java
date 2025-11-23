package pweb.aula2909.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pweb.aula2909.model.entity.PessoaJuridica;

import java.util.List;

@Repository
public class PessoaJuridicaRepository {

    @PersistenceContext
    private EntityManager em;

    // Lista todas as pessoas jurídicas
    public List<PessoaJuridica> pessoasJuridicas() {
        Query query = em.createQuery("from PessoaJuridica");
        return query.getResultList();
    }

    // Buscar por ID
    public PessoaJuridica buscarPorId(int id){
        return em.find(PessoaJuridica.class, id);
    }

    // Salvar
    @Transactional
    public void salvar(PessoaJuridica pessoa) {
        em.persist(pessoa);
    }

    // Atualizar
    @Transactional
    public void atualizar(PessoaJuridica pessoa) {
        em.merge(pessoa);
    }

    // Excluir
    @Transactional
    public void excluir(int id) {
        PessoaJuridica pessoa = buscarPorId(id);
        if (pessoa != null) {
            em.remove(pessoa);
        }
    }

    public List<PessoaJuridica> filtrarPorRazaoSocial(String nome) {
        Query query = em.createQuery("FROM PessoaJuridica pj WHERE LOWER(pj.razaoSocial) LIKE LOWER(:nome)");
        query.setParameter("nome", "%" + nome + "%");
        return query.getResultList();
    }


}
