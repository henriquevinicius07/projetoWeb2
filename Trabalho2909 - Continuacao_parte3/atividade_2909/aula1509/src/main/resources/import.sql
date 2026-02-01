

-- Roles (com IDs sequenciais)
insert into role (id, nome) values (1, 'ROLE_ADMIN');
insert into role (id, nome) values (2, 'ROLE_CLIENTE');

-- Produtos (com IDs sequenciais)
insert into produto (id, descricao, valor) values (1, 'Fone de Ouvido', 150);
insert into produto (id, descricao, valor) values (2, 'Suporte de Celular', 20);
insert into produto (id, descricao, valor) values (3, 'Monitor', 720);
insert into produto (id, descricao, valor) values (4, 'Mouse Pad', 12.00);
insert into produto (id, descricao, valor) values (5, 'Mouse', 150);
insert into produto (id, descricao, valor) values (6, 'Suporte para Notebook', 35);
insert into produto (id, descricao, valor) values (7, 'Teclado', 90);
insert into produto (id, descricao, valor) values (8, 'Nobreak', 450);

-- Pessoas Físicas (com IDs sequenciais)
insert into pessoa (id, email, telefone) values (1, 'admin@email.com', '63988888888');
insert into pessoa_fisica (id, cpf, nome, senha) values (1, '98765432100', 'admin', 'admin');

insert into pessoa (id, email, telefone) values (2, 'cliente@email.com', '63999999999');
insert into pessoa_fisica (id, cpf, nome, senha) values (2, '12345678901', 'cliente', '123');

-- Pessoas para Vendas
insert into pessoa (id, email, telefone) values (3, 'henriquefontes@gmail.com', '63984526378');
insert into pessoa_fisica (id, cpf, nome, senha) values (3, '06767965485', 'Henrique Fontes', '123');

insert into pessoa (id, email, telefone) values (4, 'maria.silva@gmail.com', '63999998888');
insert into pessoa_fisica (id, cpf, nome, senha) values (4, '10123456789', 'Maria Silva', '123');

insert into pessoa (id, email, telefone) values (5, 'htech@gmail.com', '32127123');
insert into pessoa_juridica (id, cnpj, razao_social, senha) values (5, '12852269000199', 'HTech', 'htech123');

insert into pessoa (id, email, telefone) values (6, 'mercadocentral@gmail.com', '6332124455');
insert into pessoa_juridica (id, cnpj, razao_social, senha) values (6, '11222333000155', 'Mercado Central', 'mercado123');

-- Usuários com senhas BCrypt corretas
-- Senha 'admin' com BCrypt cost 10: $2a$10$slYQmyNdGzin7olVN3p5/.8aR7CPfBfALvD.eZXBXkDDaLUzTUQae
-- Senha '123' com BCrypt cost 10: $2a$10$QgSvqqUysrtfDZ18d7Lvu.lJ.8gYZx8KD5P9E2Bg7sUJVQknQzw/C
insert into usuario (login, password, pessoa_id)
values ('admin', '$2a$10$CzTx7V5x6mEPRbHJSp0uTeS6rZOa/kc5tS.mbaPlA1MGa9UCfGBqe', 1);

insert into usuario (login, password, pessoa_id)
values ('cliente', '$2a$10$Vo7WqfiLN16dgWRrgEiq8uBoQ4FRam3.oNAdJ9T5VBH7iNq2XZxPG', 2);


-- Obter IDs dos usuários para associar roles (usando subquery)
insert into usuario_roles (usuarios_id, roles_id) select u.id, 1 from usuario u where u.login = 'admin';
insert into usuario_roles (usuarios_id, roles_id) select u.id, 2 from usuario u where u.login = 'cliente';

-- Vendas (com IDs sequenciais)
insert into venda (id, data, pessoa_id) values (1, '2025-10-10 10:00:00', 3);
insert into venda (id, data, pessoa_id) values (2, '2025-10-13 12:00:00', 5);
insert into venda (id, data, pessoa_id) values (3, '2025-10-25 09:00:00', 4);
insert into venda (id, data, pessoa_id) values (4, '2025-11-14 11:30:00', 6);

-- Itens da venda 1
insert into item_venda (id, quantidade, produto_id, venda_id) values (1, 2, 1, 1);
insert into item_venda (id, quantidade, produto_id, venda_id) values (2, 1, 2, 1);

-- Itens da venda 2
insert into item_venda (id, quantidade, produto_id, venda_id) values (3, 3, 3, 2);
insert into item_venda (id, quantidade, produto_id, venda_id) values (4, 1, 4, 2);

-- Itens da venda 3
insert into item_venda (id, quantidade, produto_id, venda_id) values (5, 2, 5, 3);
insert into item_venda (id, quantidade, produto_id, venda_id) values (6, 1, 2, 3);

-- Itens da venda 4
insert into item_venda (id, quantidade, produto_id, venda_id) values (7, 4, 1, 4);
insert into item_venda (id, quantidade, produto_id, venda_id) values (8, 1, 3, 4);
