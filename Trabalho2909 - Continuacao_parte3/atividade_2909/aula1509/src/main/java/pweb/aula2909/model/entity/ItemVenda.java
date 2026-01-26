package pweb.aula2909.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
    @Min(value = 1, message = "Apenas números inteiros são permitidos")
    @Positive(message = "Quantidade deve ser um número inteiro positivo")
    private Integer quantidade;

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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }


    @Transient
    public Double getTotal() {
        if (produto == null || produto.getValor() == null || quantidade == null) {
            return 0.0;
        }
        return produto.getValor() * quantidade.doubleValue();
    }
}
