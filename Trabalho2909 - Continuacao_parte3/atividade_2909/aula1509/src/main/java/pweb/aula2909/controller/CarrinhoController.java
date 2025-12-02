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
    public ModelAndView adicionarAoCarrinhoPost(@PathVariable("id") Long id, @RequestParam(value = "quantidade", required = false) Double quantidade, HttpSession session) {
        adicionarAoCarrinho(id, quantidade, session);

        session.setAttribute("msgSucesso", "Produto adicionado ao carrinho!");

        return new ModelAndView("redirect:/produto/listVenda");
    }

    @GetMapping("/add/{id}")
    public ModelAndView adicionarAoCarrinhoGet(@PathVariable("id") Long id, @RequestParam(value = "quantidade", required = false) Double quantidade, HttpSession session) {
        adicionarAoCarrinho(id, quantidade, session);

        session.setAttribute("msgSucesso", "Produto adicionado ao carrinho!");

        return new ModelAndView("redirect:/produto/listVenda");
    }

    private void adicionarAoCarrinho(Long id, Double quantidade, HttpSession session) {

        if (quantidade == null || quantidade <= 0) {
            quantidade = 1.0;
        }

        Produto produto = produtoRepository.buscarPorId(id);
        if (produto == null) {
            return;
        }

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);


        boolean atualizado = false;
        for (ItemVenda item : carrinho) {
            if (item.getProduto() != null && item.getProduto().getId().equals(produto.getId())) {
                Double qtdAtual = (item.getQuantidade() != null ? item.getQuantidade() : 0.0);
                item.setQuantidade(qtdAtual + quantidade);
                atualizado = true;
                break;
            }
        }

        if (!atualizado) {
            ItemVenda novo = new ItemVenda();
            novo.setProduto(produto);
            novo.setQuantidade(quantidade);
            carrinho.add(novo);
        }

        session.setAttribute(SESSAO_CARRINHO, carrinho);
    }

    @GetMapping({"/carrinho", ""})
    public ModelAndView visualizarCarrinho(ModelMap model, HttpSession session) {
        List<ItemVenda> carrinho = getCarrinhoFromSession(session);

        double total = 0.0;
        for (ItemVenda item : carrinho) {
            if (item != null && item.getTotal() != null) {
                total += item.getTotal();
            }
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
    public ModelAndView atualizarQuantidade(@PathVariable("id") Long id, @RequestParam(value = "qtd", required = false) Double qtd, HttpSession session) {
        if (qtd == null || qtd <= 0) {
            return new ModelAndView("redirect:/carrinho/carrinho");
        }

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);
        for (ItemVenda item : carrinho) {
            if (item.getProduto() != null && item.getProduto().getId().equals(id)) {
                item.setQuantidade(qtd);
                break;
            }
        }
        session.setAttribute(SESSAO_CARRINHO, carrinho);

        return new ModelAndView("redirect:/carrinho/carrinho");
    }

    @PostMapping("/remove/{id}")
    public ModelAndView removerDoCarrinho(@PathVariable("id") Long id, HttpSession session) {
        List<ItemVenda> carrinho = getCarrinhoFromSession(session);
        carrinho.removeIf(item -> item.getProduto() != null && item.getProduto().getId().equals(id));
        session.setAttribute(SESSAO_CARRINHO, carrinho);
        return new ModelAndView("redirect:/carrinho/carrinho");
    }

    @PostMapping("/finalizar")
    public ModelAndView finalizarCompra(@RequestParam("clienteId") Long clienteId, HttpSession session) {
        List<ItemVenda> carrinho = getCarrinhoFromSession(session);
        if (carrinho == null || carrinho.isEmpty()) {
            return new ModelAndView("redirect:/carrinho/carrinho");
        }

        // localizar cliente
        Pessoa cliente = pessoaFisicaRepo.buscarPorId(clienteId);
        if (cliente == null) {
            cliente = pessoaJuridicaRepo.buscarPorId(clienteId);
        }

        if (cliente == null) {
            return new ModelAndView("redirect:/carrinho/carrinho");
        }

        // montar venda
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
                List<?> raw = (List<?>) o;
                for (Object el : raw) {
                    if (el != null && !(el instanceof ItemVenda)) {
                        return new ArrayList<>();
                    }
                }
                return (List<ItemVenda>) raw;
            } catch (ClassCastException ex) {
                return new ArrayList<>();
            }
        }
        List<ItemVenda> carrinho = new ArrayList<>();
        session.setAttribute(SESSAO_CARRINHO, carrinho);
        return carrinho;
    }

}
