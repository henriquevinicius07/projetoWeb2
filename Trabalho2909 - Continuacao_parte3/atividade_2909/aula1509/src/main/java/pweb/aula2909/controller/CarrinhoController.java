package pweb.aula2909.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.ItemVenda;
import pweb.aula2909.model.entity.Pessoa;
import pweb.aula2909.model.entity.Produto;
import pweb.aula2909.model.entity.Venda;
import pweb.aula2909.model.repository.PessoaFisicaRepository;
import pweb.aula2909.model.repository.PessoaJuridicaRepository;
import pweb.aula2909.model.repository.ProdutoRepository;
import pweb.aula2909.model.repository.VendaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepo;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepo;

    private static final String SESSAO_CARRINHO = "carrinho";

    @PostMapping("/add/{id}")
    public ModelAndView adicionarAoCarrinhoPost(
            @PathVariable Long id,
            @RequestParam(value = "quantidade", required = false) Double quantidade,
            HttpSession session) {

        adicionarAoCarrinho(id, quantidade, session);
        session.setAttribute("msgSucesso", "Produto adicionado ao carrinho!");
        return new ModelAndView("redirect:/produto/listVenda");
    }

    @GetMapping("/add/{id}")
    public ModelAndView adicionarAoCarrinhoGet(
            @PathVariable Long id,
            @RequestParam(value = "quantidade", required = false) Double quantidade,
            HttpSession session) {

        adicionarAoCarrinho(id, quantidade, session);
        session.setAttribute("msgSucesso", "Produto adicionado ao carrinho!");
        return new ModelAndView("redirect:/produto/listVenda");
    }

    private void adicionarAoCarrinho(Long id, Double quantidade, HttpSession session) {

        if (quantidade == null || quantidade < 1) {
            quantidade = 1.0;
        }

        Produto produto = produtoRepository.buscarPorId(id);
        if (produto == null) return;

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);

        for (ItemVenda item : carrinho) {
            if (item.getProduto().getId().equals(produto.getId())) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                session.setAttribute(SESSAO_CARRINHO, carrinho);
                return;
            }
        }

        ItemVenda novo = new ItemVenda();
        novo.setProduto(produto);
        novo.setQuantidade(quantidade);
        carrinho.add(novo);

        session.setAttribute(SESSAO_CARRINHO, carrinho);
    }

    @GetMapping({"/carrinho", ""})
    public ModelAndView visualizarCarrinho(ModelMap model, HttpSession session) {

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);

        double total = 0.0;
        for (ItemVenda item : carrinho) {
            total += item.getTotal();
        }

        model.addAttribute("itens", carrinho);
        model.addAttribute("total", total);

        List<Object> clientes = new ArrayList<>();
        clientes.addAll(pessoaFisicaRepo.pessoasFisicas());
        clientes.addAll(pessoaJuridicaRepo.pessoasJuridicas());
        model.addAttribute("clientes", clientes);

        return new ModelAndView("/Carrinho/carrinho", model);
    }

    @PostMapping("/update/{id}")
    public String atualizarQuantidade(@PathVariable Long id,
                                      @RequestParam Double quantidade,
                                      HttpSession session) {

        if (quantidade == null || quantidade < 1) {
            quantidade = 1.0;
        }

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);

        for (ItemVenda item : carrinho) {
            if (item.getProduto().getId().equals(id)) {
                item.setQuantidade(quantidade);
                break;
            }
        }

        session.setAttribute(SESSAO_CARRINHO, carrinho);
        return "redirect:/carrinho/carrinho";
    }

    @PostMapping("/remove/{id}")
    public String removerDoCarrinho(@PathVariable Long id, HttpSession session) {

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);
        carrinho.removeIf(item ->
                item.getProduto() != null &&
                        item.getProduto().getId().equals(id));

        session.setAttribute(SESSAO_CARRINHO, carrinho);
        return "redirect:/carrinho/carrinho";
    }

    @PostMapping("/finalizar")
    public ModelAndView finalizarCompra(@RequestParam("clienteId") Long clienteId,
                                        HttpSession session) {

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);
        if (carrinho.isEmpty()) {
            return new ModelAndView("redirect:/carrinho/carrinho");
        }

        Pessoa cliente = pessoaFisicaRepo.buscarPorId(clienteId);
        if (cliente == null) {
            cliente = pessoaJuridicaRepo.buscarPorId(clienteId);
        }

        if (cliente == null) {
            return new ModelAndView("redirect:/carrinho/carrinho");
        }

        Venda venda = new Venda();
        venda.setData(LocalDateTime.now());
        venda.setCliente(cliente);
        venda.setItens(carrinho);

        for (ItemVenda item : carrinho) {
            item.setVenda(venda);
        }

        vendaRepository.salvar(venda);

        session.removeAttribute(SESSAO_CARRINHO);

        return new ModelAndView("redirect:/venda/list");
    }

    private List<ItemVenda> getCarrinhoFromSession(HttpSession session) {

        Object o = session.getAttribute(SESSAO_CARRINHO);

        if (o instanceof List<?>) {
            try {
                return (List<ItemVenda>) o;
            } catch (ClassCastException e) {
                // ignorar
            }
        }

        List<ItemVenda> carrinho = new ArrayList<>();
        session.setAttribute(SESSAO_CARRINHO, carrinho);
        return carrinho;
    }
}
