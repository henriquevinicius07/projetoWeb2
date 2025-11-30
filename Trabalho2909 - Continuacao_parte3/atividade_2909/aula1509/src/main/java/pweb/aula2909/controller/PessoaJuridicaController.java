package pweb.aula2909.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.PessoaJuridica;
import pweb.aula2909.model.repository.PessoaJuridicaRepository;

@Controller
@RequestMapping("pessoajuridica")
public class PessoaJuridicaController {

    @Autowired
    private PessoaJuridicaRepository repository;

    @GetMapping("/list")
    public ModelAndView listar(
            @RequestParam(value = "filtro", required = false) String filtro,
            ModelMap model) {

        if (filtro != null && !filtro.isEmpty()) {
            model.addAttribute("pessoas", repository.filtrarPorRazaoSocial(filtro));
        } else {
            model.addAttribute("pessoas", repository.pessoasJuridicas());
        }

        model.addAttribute("filtro", filtro);
        model.addAttribute("tipo", "juridica");

        return new ModelAndView("pessoa/list", model);
    }

    @GetMapping("/form")
    public ModelAndView novaPessoa(PessoaJuridica pessoaJuridica, ModelMap model) {
        model.addAttribute("pessoa", pessoaJuridica);
        return new ModelAndView("/pessoa/juridica", model);
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("pessoa", repository.buscarPorId(id));
        return new ModelAndView("/pessoa/juridica", model);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute("pessoa") PessoaJuridica pessoa) {
        repository.salvar(pessoa);
        return new ModelAndView("redirect:/pessoajuridica/list");
    }

    @PostMapping("/atualizar")
    public ModelAndView atualizar(PessoaJuridica pessoa) {
        repository.atualizar(pessoa);
        return new ModelAndView("redirect:/pessoajuridica/list");
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        repository.excluir(id);
        return new ModelAndView("redirect:/pessoajuridica/list");
    }
}
