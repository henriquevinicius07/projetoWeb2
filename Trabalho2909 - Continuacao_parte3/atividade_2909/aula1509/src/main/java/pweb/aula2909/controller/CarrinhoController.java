package pweb.aula2909.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.ItemVenda;
import pweb.aula2909.model.entity.Pessoa;
import pweb.aula2909.model.entity.Produto;
import pweb.aula2909.model.entity.Usuario;
import pweb.aula2909.model.entity.Venda;
import pweb.aula2909.model.repository.PessoaFisicaRepository;
import pweb.aula2909.model.repository.PessoaJuridicaRepository;
import pweb.aula2909.model.repository.ProdutoRepository;
import pweb.aula2909.model.repository.VendaRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/add/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView adicionarAoCarrinho(
            @PathVariable Long id,
            @RequestParam(value = "quantidade", required = false) Integer quantidade,
            HttpSession session) {

        adicionarAoCarrinhoInterno(id, quantidade, session);
        session.setAttribute("msgSucesso", "Produto adicionado ao carrinho!");
        return new ModelAndView("redirect:/produto/listVenda");
    }

    private void adicionarAoCarrinhoInterno(Long id, Integer quantidade, HttpSession session) {

        if (quantidade == null || quantidade < 1) {
            quantidade = 1;
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
    public ModelAndView visualizarCarrinho(ModelMap model,
                                           HttpSession session,
                                           @AuthenticationPrincipal Usuario usuarioLogado) {

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);

        double total = 0.0;
        for (ItemVenda item : carrinho) {
            total += item.getTotal();
        }

        model.addAttribute("itens", carrinho);
        model.addAttribute("total", total);

        boolean isAdmin = isAdmin(usuarioLogado);
        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            // ADMIN: pode escolher qualquer cliente
            List<Object> clientes = new ArrayList<>();
            clientes.addAll(pessoaFisicaRepo.pessoasFisicas());
            clientes.addAll(pessoaJuridicaRepo.pessoasJuridicas());
            model.addAttribute("clientes", clientes);
        } else {
            // CLIENTE: usa o cliente vinculado ao usuário logado
            Pessoa clienteLogado = (usuarioLogado != null) ? usuarioLogado.getPessoa() : null;
            model.addAttribute("clienteLogado", clienteLogado);
        }

        return new ModelAndView("/Carrinho/carrinho", model);
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> atualizarQuantidade(@PathVariable Long id,
                                                                   @RequestParam(required = false) Integer quantidade,
                                                                   HttpSession session) {

        if (quantidade == null || quantidade < 1) {
            quantidade = 1;
        }

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);

        Double totalItem = null;
        boolean encontrou = false;

        List<ItemVenda> carrinhoAtualizado = new ArrayList<>();
        for (ItemVenda item : carrinho) {
            if (item.getProduto() != null && item.getProduto().getId().equals(id)) {
                item.setQuantidade(quantidade);
                totalItem = item.getTotal();
                encontrou = true;
            }
            carrinhoAtualizado.add(item);
        }

        if (!encontrou) {
            return ResponseEntity.notFound().build();
        }

        session.setAttribute(SESSAO_CARRINHO, carrinhoAtualizado);

        double totalCarrinho = 0.0;
        for (ItemVenda item : carrinhoAtualizado) {
            totalCarrinho += item.getTotal();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("sucesso", true);
        resp.put("totalItem", totalItem);
        resp.put("totalCarrinho", totalCarrinho);

        return ResponseEntity.ok(resp);
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
    public ModelAndView finalizarCompra(@RequestParam(value = "clienteId", required = false) Long clienteId,
                                        HttpSession session,
                                        @AuthenticationPrincipal Usuario usuarioLogado) {

        List<ItemVenda> carrinho = getCarrinhoFromSession(session);
        if (carrinho.isEmpty()) {
            return new ModelAndView("redirect:/carrinho/carrinho");
        }

        boolean isAdmin = isAdmin(usuarioLogado);

        Pessoa cliente = null;

        if (isAdmin) {
            // ADMIN: precisa escolher um cliente
            if (clienteId == null) {
                return new ModelAndView("redirect:/carrinho/carrinho");
            }

            cliente = pessoaFisicaRepo.buscarPorId(clienteId);
            if (cliente == null) {
                cliente = pessoaJuridicaRepo.buscarPorId(clienteId);
            }
        } else {
            // CLIENTE: cliente é sempre o vinculado ao usuário logado
            cliente = (usuarioLogado != null) ? usuarioLogado.getPessoa() : null;
        }

        if (cliente == null) {
            return new ModelAndView("redirect:/carrinho/carrinho");
        }

        Venda venda = new Venda();
        venda.setData(LocalDateTime.now());
        venda.setCliente(cliente);

        List<ItemVenda> itensVenda = new ArrayList<>();
        for (ItemVenda itemSessao : carrinho) {
            if (itemSessao == null || itemSessao.getProduto() == null) continue;

            Produto produtoFresco = produtoRepository.buscarPorId(itemSessao.getProduto().getId());
            if (produtoFresco == null) continue;

            ItemVenda item = new ItemVenda();
            item.setProduto(produtoFresco);

            Integer qtd = itemSessao.getQuantidade();
            if (qtd == null || qtd < 1) qtd = 1;
            item.setQuantidade(qtd);

            item.setVenda(venda);
            itensVenda.add(item);
        }

        venda.setItens(itensVenda);
        vendaRepository.salvar(venda);

        session.removeAttribute(SESSAO_CARRINHO);

        return new ModelAndView("redirect:/venda/list");
    }

    private boolean isAdmin(Usuario usuarioLogado) {
        if (usuarioLogado == null || usuarioLogado.getAuthorities() == null) return false;

        for (GrantedAuthority a : usuarioLogado.getAuthorities()) {
            if (a != null && "ROLE_ADMIN".equals(a.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private List<ItemVenda> getCarrinhoFromSession(HttpSession session) {

        Object o = session.getAttribute(SESSAO_CARRINHO);

        if (o instanceof List<?>) {
            try {
                return (List<ItemVenda>) o;
            } catch (ClassCastException e) {
                // ignora e recria
            }
        }

        List<ItemVenda> carrinho = new ArrayList<>();
        session.setAttribute(SESSAO_CARRINHO, carrinho);
        return carrinho;
    }
}
