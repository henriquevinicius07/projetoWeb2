package pweb.aula2909.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.Venda;
import pweb.aula2909.model.repository.VendaRepository;
import pweb.aula2909.model.repository.PessoaFisicaRepository;
import pweb.aula2909.model.repository.PessoaJuridicaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepo;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepo;

    // ============================================================
    // LISTA DE VENDAS (COM FILTRO)
    // ============================================================
    @GetMapping("/list")
    public ModelAndView listar(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String data,
            ModelMap model) {

        List<Venda> vendas;

        // -----------------------------------------------------------
        // 🔹 LISTA UNIFICADA DE CLIENTES (PF + PJ)
        // -----------------------------------------------------------
        List<Object> clientes = new ArrayList<>();
        clientes.addAll(pessoaFisicaRepo.pessoasFisicas());
        clientes.addAll(pessoaJuridicaRepo.pessoasJuridicas());

        model.addAttribute("clientes", clientes);

        // -----------------------------------------------------------
        // 🔹 APLICAÇÃO DOS FILTROS
        // -----------------------------------------------------------
        if (clienteId != null && data != null && !data.isEmpty()) {

            LocalDate dt = LocalDate.parse(data);
            LocalDateTime inicio = dt.atStartOfDay();
            LocalDateTime fim = dt.atTime(23, 59, 59);

            vendas = repository.listarPorClienteEData(clienteId, inicio, fim);

            model.addAttribute("clienteFiltro", clienteId);
            model.addAttribute("dataFiltro", data);

        } else if (clienteId != null) {

            vendas = repository.listarPorCliente(clienteId);
            model.addAttribute("clienteFiltro", clienteId);

        } else if (data != null && !data.isEmpty()) {

            LocalDate dt = LocalDate.parse(data);
            LocalDateTime inicio = dt.atStartOfDay();
            LocalDateTime fim = dt.atTime(23, 59, 59);

            vendas = repository.listarPorData(inicio, fim);
            model.addAttribute("dataFiltro", data);

        } else {

            vendas = repository.listar();
        }

        model.addAttribute("vendas", vendas);

        return new ModelAndView("/venda/list", model);
    }

    // ============================================================
    // DETALHE DA VENDA (CORRIGIDO)
    // ============================================================
    @GetMapping("/detail/{id}")
    public ModelAndView detalhes(@PathVariable("id") Long id, ModelMap model) {
        Venda venda = repository.buscarPorId(id);
        if (venda != null) {
            model.addAttribute("venda", venda);
        }
        return new ModelAndView("/venda/detail", model);
    }

}
