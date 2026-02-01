package pweb.aula2909.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CadastroController {

    @GetMapping("/escolher-tipo-cadastro")
    public ModelAndView escolherTipoCadastro() {
        return new ModelAndView("escolher-tipo-cadastro");
    }
}
