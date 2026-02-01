package pweb.aula2909.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula2909.model.entity.Venda;
import pweb.aula2909.model.entity.Usuario;
import pweb.aula2909.model.repository.PessoaFisicaRepository;
import pweb.aula2909.model.repository.PessoaJuridicaRepository;
import pweb.aula2909.model.repository.VendaRepository;

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
                dtInicio.atTime(0, 0, 0),
                dtFim.atTime(23, 59, 59)
        };
    }

    private List<Object> carregarClientes() {
        List<Object> clientes = new ArrayList<>();
        clientes.addAll(pessoaFisicaRepo.pessoasFisicas());
        clientes.addAll(pessoaJuridicaRepo.pessoasJuridicas());
        return clientes;
    }

    private boolean isAdmin(Usuario auth) {
        if (auth == null || auth.getAuthorities() == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    @GetMapping("/list")
    public ModelAndView listar(@RequestParam(required = false) Long clienteId,
                               @RequestParam(required = false) String dataInicio,
                               @RequestParam(required = false) String dataFim,
                               @AuthenticationPrincipal Usuario auth,
                               ModelMap model) {

        boolean admin = isAdmin(auth);
        model.addAttribute("isAdmin", admin);

        if (admin) {
            model.addAttribute("clientes", carregarClientes());
        } else {
            model.addAttribute("clienteLogado", auth != null ? auth.getPessoa() : null);
        }

        List<Venda> vendas;

        // CLIENTE: ignora filtro do select e mostra apenas as compras dele
        if (!admin) {
            if (auth == null || auth.getPessoa() == null) {
                vendas = new ArrayList<>();
            } else {
                Long pessoaId = auth.getPessoa().getId();

                boolean temData = ((dataInicio != null && !dataInicio.isEmpty())
                        || (dataFim != null && !dataFim.isEmpty()));

                if (temData) {
                    LocalDateTime[] intervalo = intervaloDia(dataInicio, dataFim);
                    vendas = repository.listarPorClienteEData(pessoaId, intervalo[0], intervalo[1]);
                    model.addAttribute("dataInicioFiltro", dataInicio);
                    model.addAttribute("dataFimFiltro", dataFim);
                } else {
                    vendas = repository.listarPorCliente(pessoaId);
                }
            }

            model.addAttribute("vendas", vendas);
            return new ModelAndView("/venda/list", model);
        }

        // ADMIN: mantém filtros normais
        model.addAttribute("clientes", carregarClientes());

        boolean temCliente = clienteId != null;
        boolean temData = ((dataInicio != null && !dataInicio.isEmpty())
                || (dataFim != null && !dataFim.isEmpty()));

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
    public ModelAndView detalhes(@PathVariable("id") Long id,
                                 @AuthenticationPrincipal Usuario auth,
                                 ModelMap model) {

        Venda venda = repository.buscarPorId(id);
        if (venda == null) return new ModelAndView("/venda/detail", model);

        boolean admin = isAdmin(auth);

        // Cliente só pode ver detalhes das vendas dele
        if (!admin) {
            if (auth == null || auth.getPessoa() == null) {
                return new ModelAndView("redirect:/venda/list");
            }
            Long pessoaId = auth.getPessoa().getId();
            if (venda.getCliente() == null || !pessoaId.equals(venda.getCliente().getId())) {
                return new ModelAndView("redirect:/venda/list");
            }
        }

        model.addAttribute("venda", venda);
        return new ModelAndView("/venda/detail", model);
    }
}
