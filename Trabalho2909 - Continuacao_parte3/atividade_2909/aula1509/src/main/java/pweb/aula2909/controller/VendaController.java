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

    private LocalDateTime[] intervaloDia(String inicioData, String fimData) {
        LocalDate dtInicio;
        LocalDate dtFim;

        if (inicioData != null && !inicioData.isEmpty()) {
            dtInicio = LocalDate.parse(inicioData);
        } else if (fimData != null && !fimData.isEmpty()) {
            dtInicio = LocalDate.parse(fimData);
        } else {
            dtInicio = LocalDate.of(1, 1, 1);
        }

        if (fimData != null && !fimData.isEmpty()) {
            dtFim = LocalDate.parse(fimData);
        } else if (inicioData != null && !inicioData.isEmpty()) {
            dtFim = LocalDate.parse(inicioData);
        } else {
            dtFim = LocalDate.now();
        }

        return new LocalDateTime[]{
                dtInicio.atTime(0,0,0),
                dtFim.atTime(23, 59, 59)
        };
    }


    private List<Object> carregarClientes() {
        List<Object> clientes = new ArrayList<>();
        clientes.addAll(pessoaFisicaRepo.pessoasFisicas());
        clientes.addAll(pessoaJuridicaRepo.pessoasJuridicas());
        return clientes;
    }


    @GetMapping("/list")
    public ModelAndView listar(@RequestParam(required = false) Long clienteId, @RequestParam(required = false) String dataInicio, @RequestParam(required = false) String dataFim, ModelMap model) {

        List<Venda> vendas;

        model.addAttribute("clientes", carregarClientes());

        boolean temCliente = clienteId != null;
        boolean temData = ( (dataInicio != null && !dataInicio.isEmpty()) || (dataFim != null && !dataFim.isEmpty()) );

        if (temCliente && temData) {
            LocalDateTime[] intervalo = intervaloDia(dataInicio, dataFim);
            vendas = repository.listarPorClienteEData(clienteId, intervalo[0], intervalo[1]);
            model.addAttribute("clienteFiltro", clienteId);
            model.addAttribute("dataInicioFiltro", dataInicio);
            model.addAttribute("dataFimFiltro", dataFim);

        } else if (temCliente) {
            vendas = repository.listarPorCliente(clienteId);
            model.addAttribute("clienteFiltro", clienteId);

        } else if (temData) {

            LocalDateTime[] intervalo = intervaloDia(dataInicio, dataFim);
            vendas = repository.listarPorData(intervalo[0], intervalo[1]);
            model.addAttribute("dataInicioFiltro", dataInicio);
            model.addAttribute("dataFimFiltro", dataFim);

        } else {
            vendas = repository.listar();
        }

        model.addAttribute("vendas", vendas);

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
