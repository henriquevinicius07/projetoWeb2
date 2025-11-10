package pweb.aula2909.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.Venda;
import pweb.aula2909.model.repository.VendaRepository;

@Controller
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaRepository repository;

    @GetMapping("/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("vendas", repository.listar()); // usar listar()
        return new ModelAndView("/venda/list", model);
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detalhes(@PathVariable("id") Long id, ModelMap model) {
        Venda venda = repository.buscarPorId(id);
        if (venda != null) {
            model.addAttribute("venda", venda);
        }
        return new ModelAndView("/venda/detail", model);
    }

}
