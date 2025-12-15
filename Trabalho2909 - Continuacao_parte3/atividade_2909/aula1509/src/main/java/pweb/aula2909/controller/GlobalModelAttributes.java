package pweb.aula2909.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pweb.aula2909.model.entity.ItemVenda;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("cartCount")
    @SuppressWarnings("unchecked")
    public Integer populateCartCount(HttpSession session) {
        Object o = session.getAttribute("carrinho");
        if (o instanceof List<?>) {
            try {
                List<ItemVenda> carrinho = (List<ItemVenda>) o;
                return carrinho.size();
            } catch (ClassCastException e) {
                // se houver problema de cast, retornar 0
            }
        }
        return 0;
    }
}

