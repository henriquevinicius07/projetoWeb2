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
                                             (1, 'henrique@gmail.com', '(63)98420-0000'),
                                             (2, 'vinicius@gmail.com.com', '(63)99999-1111');


-- Pessoas físicas (detalhes)
insert into pessoa_fisica (id, nome, cpf, senha) values
                                                     (1, 'Henrique', '02612501460', '123'),
                                                     (2, 'Vinicius','06767902150', '123');


-- Pessoa (base)
insert into pessoa (id, email, telefone) values
    (3, 'techsoluções@gmail.com', '(63)98888-2222');

-- Pessoa Jurídica (detalhes da empresa)
-- (ajuste os campos conforme sua entidade: razao_social/nome_fantasia/cnpj etc.)
insert into pessoa_juridica (id, razao_social, cnpj, senha) values
    (3, 'Tech Soluções', '12345678000199', '123');

-- Usuário da empresa (senha BCrypt = 123)
insert into usuario (id, login, password, pessoa_id) values
    (3, 'Tech Soluções',
     '$2a$10$Vo7WqfiLN16dgWRrgEiq8uBoQ4FRam3.oNAdJ9T5VBH7iNq2XZxPG',
     3);

-- Role: cliente
insert into usuario_roles (usuarios_id, roles_id) values (3, 2);


-- Produtos
insert into produto (id, descricao, valor, imagem) values
                                                       (1, 'Fone de Ouvido', 250.00, '1.jpg'),
                                                       (2, 'Suporte para Celular', 20.00, '2.jpg'),
                                                       (3, 'Monitor 24', 300.00, '3.jpg'),
                                                       (4, 'Mouse Pad', 15.00, '4.jpg'),
                                                       (5, 'Mouse Sem Fio', 250.00, '5.jpg'),
                                                       (6, 'Suporte para Notebook', 35.00, '6.jpg'),
                                                       (7, 'Teclado', 50.00, '7.jpg'),
                                                       (8, 'Nobreak', 350.00, '8.jpg');

-- Usuários com senhas BCrypt corretas
-- Senha padrão (admin e cliente): 123
insert into usuario (id, login, password, pessoa_id) values
    (1, 'Henrique',   '$2a$10$Vo7WqfiLN16dgWRrgEiq8uBoQ4FRam3.oNAdJ9T5VBH7iNq2XZxPG', 1);

insert into usuario (id, login, password, pessoa_id) values
    (2, 'Vinicius', '$2a$10$Vo7WqfiLN16dgWRrgEiq8uBoQ4FRam3.oNAdJ9T5VBH7iNq2XZxPG', 2);

-- Relação usuário-role (nomes corretos conforme @JoinTable do Usuario)
insert into usuario_roles (usuarios_id, roles_id) values (1, 1);
insert into usuario_roles (usuarios_id, roles_id) values (2, 2);

-- Vendas (exemplo)
insert into venda (id, pessoa_id, data) values
                                            (1, 2, '2025-10-12 10:00:00'),
                                            (2, 2, '2025-10-13 12:00:00'),
                                            (3, 3, '2025-10-14 14:30:00'),
                                            (4, 3, '2025-10-15 16:45:00');

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
alter table usuario   alter column id restart with 4;
alter table venda     alter column id restart with 5;
alter table item_venda alter column id restart with 9;
