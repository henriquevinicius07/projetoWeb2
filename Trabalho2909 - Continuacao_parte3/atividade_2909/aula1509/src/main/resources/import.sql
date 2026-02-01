-- ======================================================================
-- import.sql (H2)
-- ======================================================================
-- Este arquivo popula o banco H2 em memória com dados iniciais.
-- IMPORTANTE: no seu modelo, Pessoa é a tabela “base” e PessoaFisica/PessoaJuridica
-- referenciam Pessoa (FK). Então sempre inserimos primeiro em `pessoa`,
-- depois em `pessoa_fisica` / `pessoa_juridica`.

-- Roles
insert into role (id, nome) values (1, 'ROLE_ADMIN');
insert into role (id, nome) values (2, 'ROLE_CLIENTE');

-- Pessoas (tabela base)
insert into pessoa (id, email, telefone) values
                                             (1, 'admin@teste.com', '(63)99999-0000'),
                                             (2, 'cliente@teste.com', '(63)99999-1111');

-- Pessoas físicas (detalhes)
insert into pessoa_fisica (id, nome, cpf, senha) values
                                                     (1, 'Administrador', '00000000000', 'admin'),
                                                     (2, 'Cliente',       '11111111111', '123');

-- Produtos
insert into produto (id, descricao, valor, imagem) values
                                                       (1, 'Notebook Dell', 3500.00, 'notebook.jpg'),
                                                       (2, 'Mouse Logitech', 150.00, 'mouse.jpg'),
                                                       (3, 'Teclado Mecânico', 300.00, 'teclado.jpg'),
                                                       (4, 'Monitor LG 24"', 900.00, 'monitor.jpg'),
                                                       (5, 'Impressora HP', 650.00, 'impressora.jpg'),
                                                       (6, 'Fone Bluetooth', 200.00, 'fone.jpg'),
                                                       (7, 'Cadeira Gamer', 1200.00, 'cadeira.jpg'),
                                                       (8, 'Smartphone Samsung', 2500.00, 'smartphone.jpg');

-- Usuários com senhas BCrypt corretas
-- Senha padrão (admin e cliente): 123
insert into usuario (id, login, password, pessoa_id) values
    (1, 'admin',   '$2a$10$Vo7WqfiLN16dgWRrgEiq8uBoQ4FRam3.oNAdJ9T5VBH7iNq2XZxPG', 1);

insert into usuario (id, login, password, pessoa_id) values
    (2, 'cliente', '$2a$10$Vo7WqfiLN16dgWRrgEiq8uBoQ4FRam3.oNAdJ9T5VBH7iNq2XZxPG', 2);

-- Relação usuário-role (nomes corretos conforme @JoinTable do Usuario)
insert into usuario_roles (usuarios_id, roles_id) values (1, 1);
insert into usuario_roles (usuarios_id, roles_id) values (2, 2);

-- Vendas (exemplo)
insert into venda (id, pessoa_id, data) values
                                            (1, 2, '2025-10-12 10:00:00'),
                                            (2, 2, '2025-10-13 12:00:00'),
                                            (3, 2, '2025-10-14 14:30:00'),
                                            (4, 2, '2025-10-15 16:45:00');

-- Itens das vendas
-- (ItemVenda não tem valor_unitario no seu modelo)
insert into item_venda (id, venda_id, produto_id, quantidade) values
                                                                  (1, 1, 1, 1),
                                                                  (2, 1, 2, 2),
                                                                  (3, 2, 3, 1),
                                                                  (4, 2, 4, 1),
                                                                  (5, 3, 5, 1),
                                                                  (6, 3, 6, 2),
                                                                  (7, 4, 7, 1),
                                                                  (8, 4, 8, 1);

-- ======================================================================
-- IMPORTANTE (H2): Ajusta os IDs auto-gerados para não colidir com os IDs
-- inseridos manualmente acima (corrige o erro 23505 / PK)
-- ======================================================================
alter table role      alter column id restart with 3;
alter table produto   alter column id restart with 9;
alter table usuario   alter column id restart with 3;
alter table venda     alter column id restart with 5;
alter table item_venda alter column id restart with 9;
