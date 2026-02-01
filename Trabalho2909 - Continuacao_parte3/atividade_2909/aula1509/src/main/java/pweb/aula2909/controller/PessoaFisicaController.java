package pweb.aula2909.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.PessoaFisica;
import pweb.aula2909.model.entity.Usuario;
import pweb.aula2909.model.entity.Role;
import pweb.aula2909.model.repository.PessoaFisicaRepository;
import pweb.aula2909.model.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Controller
@RequestMapping("pessoafisica")
public class PessoaFisicaController {

    @Autowired
    private PessoaFisicaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/list")
    public ModelAndView listar(@RequestParam(value = "filtro", required = false) String filtro, ModelMap model) {

        if (filtro != null && !filtro.isEmpty()) {
            model.addAttribute("pessoas", repository.filtrarPorNome(filtro));
        } else {
            model.addAttribute("pessoas", repository.pessoasFisicas());
        }

        model.addAttribute("filtro", filtro);
        model.addAttribute("tipo", "fisica");

        return new ModelAndView("pessoa/list", model);
    }

    @GetMapping("/form")
    public ModelAndView novaPessoa(PessoaFisica pessoaFisica, ModelMap model) {
        model.addAttribute("pessoa", pessoaFisica);
        return new ModelAndView("/pessoa/fisica", model);
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("pessoa", repository.buscarPorId(id));
        return new ModelAndView("/pessoa/fisica", model);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid @ModelAttribute("pessoa") PessoaFisica pessoa,
                               BindingResult result,
                               ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("pessoa", pessoa);
            return new ModelAndView("/pessoa/fisica", model);
        }

        try {
            // Verificar se o login já existe
            if (usuarioRepository.existsByLogin(pessoa.getNome())) {
                model.addAttribute("pessoa", pessoa);
                model.addAttribute("erro", "Este nome de usuário já existe. Escolha outro.");
                return new ModelAndView("/pessoa/fisica", model);
            }

            // Salvar pessoa física
            repository.salvar(pessoa);

            // Criar usuário para a pessoa
            Usuario usuario = new Usuario();
            usuario.setLogin(pessoa.getNome()); // Login usando o nome da pessoa
            usuario.setPassword(passwordEncoder.encode(pessoa.getSenha()));
            usuario.setPessoa(pessoa);

            // Obter role CLIENTE
            Role roleCliente = (Role) em.createQuery("FROM Role r WHERE r.nome = 'ROLE_CLIENTE'").getSingleResult();
            usuario.getRoles().add(roleCliente);

            usuarioRepository.salvar(usuario);

            return new ModelAndView("redirect:/pessoafisica/list");
        } catch (Exception e) {
            model.addAttribute("pessoa", pessoa);
            model.addAttribute("erro", "Erro ao salvar: " + e.getMessage());
            return new ModelAndView("/pessoa/fisica", model);
        }
    }

    @PostMapping("/atualizar")
    public ModelAndView atualizar(@Valid @ModelAttribute("pessoa") PessoaFisica pessoa,
                                  BindingResult result,
                                  ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("pessoa", pessoa);
            return new ModelAndView("/pessoa/fisica", model);
        }

        try {
            repository.atualizar(pessoa);
            return new ModelAndView("redirect:/pessoafisica/list");
        } catch (Exception e) {
            model.addAttribute("pessoa", pessoa);
            model.addAttribute("erro", "Erro ao atualizar: " + e.getMessage());
            return new ModelAndView("/pessoa/fisica", model);
        }
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        repository.excluir(id);
        return new ModelAndView("redirect:/pessoafisica/list");
    }

}
