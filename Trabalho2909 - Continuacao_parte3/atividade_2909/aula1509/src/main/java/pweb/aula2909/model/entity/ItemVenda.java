package pweb.aula2909.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @NotNull(message = "Produto é obrigatório")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "venda_id")
    @NotNull(message = "Venda é obrigatória")
    private Venda venda;

    @Column(nullable = false)
    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
    private Double quantidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    @Transient
    public Double getTotal() {
        if (produto == null || produto.getValor() == null || quantidade == null) {
            return 0.0;
        }
        return produto.getValor() * quantidade;
    }
}
