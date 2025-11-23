package pweb.aula2909.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.Produto;
import pweb.aula2909.model.repository.ProdutoRepository;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @GetMapping("/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("produtos", repository.produtos());
        return new ModelAndView("/produto/list", model);
    }

    @GetMapping("/novoProduto")
    public ModelAndView novoProduto(ModelMap model) {
        model.addAttribute("produto", new Produto());
        return new ModelAndView("/produto/form", model);
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("produto", repository.buscarPorId(id));
        return new ModelAndView("/produto/form", model);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(Produto produto) {
        repository.salvar(produto);
        return new ModelAndView("redirect:/produto/list");
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        repository.excluir(id);
        return new ModelAndView("redirect:/produto/list");
    }

    @PostMapping("/atualizar")
    public ModelAndView atualizar(Produto produto) {
        repository.atualizar(produto);
        return new ModelAndView("redirect:/produto/list");
    }


    @GetMapping("/filtrar")
    public ModelAndView filtrar(String nome, ModelMap model) {

        if (nome == null || nome.trim().isEmpty()) {
            model.addAttribute("produtos", repository.produtos());
        } else {
            model.addAttribute("produtos", repository.buscarPorNome(nome));
        }

        model.addAttribute("nome", nome);

        return new ModelAndView("/produto/list", model);
    }

}
