package pweb.aula2909.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.Produto;
import pweb.aula2909.model.repository.ProdutoRepository;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @GetMapping("/list")
    public ModelAndView listar(@RequestParam(value = "filtro", required = false) String filtro, ModelMap model) {

        if (filtro != null && !filtro.isEmpty()) {
            model.addAttribute("produtos", repository.buscarPorNome(filtro));
        } else {
            model.addAttribute("produtos", repository.produtos());
        }

        model.addAttribute("filtro", filtro);
        return new ModelAndView("/produto/list", model);
    }

    @GetMapping("/listVenda")
    public ModelAndView listarParaVenda(ModelMap model, HttpSession session) {

        model.addAttribute("produtos", repository.produtos());

        Object msg = session.getAttribute("msgSucesso");
        if (msg != null) {
            model.addAttribute("msgSucesso", msg.toString());
            session.removeAttribute("msgSucesso");
        }

        return new ModelAndView("/produto/listVenda", model);
    }

    @GetMapping("/form")
    public ModelAndView novoProduto(Produto produto, ModelMap model) {
        model.addAttribute("produto", produto);
        return new ModelAndView("/produto/form", model);
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id, ModelMap model) {
        model.addAttribute("produto", repository.buscarPorId(id));
        return new ModelAndView("/produto/form", model);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute("produto") Produto produto) {
        repository.salvar(produto);
        return new ModelAndView("redirect:/produto/list");
    }

    @PostMapping("/atualizar")
    public ModelAndView atualizar(@ModelAttribute Produto produto) {
        repository.atualizar(produto);
        return new ModelAndView("redirect:/produto/list");
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable Long id) {
        repository.excluir(id);
        return new ModelAndView("redirect:/produto/list");
    }
}
