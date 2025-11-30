package pweb.aula2909.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.PessoaFisica;
import pweb.aula2909.model.repository.PessoaFisicaRepository;

@Controller
@RequestMapping("pessoafisica")
public class PessoaFisicaController {

    @Autowired
    private PessoaFisicaRepository repository;

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
    public ModelAndView salvar(@ModelAttribute("pessoa") PessoaFisica pessoa) {
        repository.salvar(pessoa);
        return new ModelAndView("redirect:/pessoafisica/list");
    }

    @PostMapping("/atualizar")
    public ModelAndView atualizar(@ModelAttribute("pessoa") PessoaFisica pessoa) {
        repository.atualizar(pessoa);
        return new ModelAndView("redirect:/pessoafisica/list");
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        repository.excluir(id);
        return new ModelAndView("redirect:/pessoafisica/list");
    }

}
