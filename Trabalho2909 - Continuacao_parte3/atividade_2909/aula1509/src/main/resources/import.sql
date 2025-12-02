


insert into produto (descricao, valor) values ('Fone de Ouvido', 150);
insert into produto (descricao, valor) values ('Suporte de Celular', 20);
insert into produto (descricao, valor) values ('Monitor', 720);
insert into produto (descricao, valor) values ('Mouse Pad', 12.00);
insert into produto (descricao, valor) values ('Mouse', 150);
insert into produto (descricao, valor) values ('Suporte para Notebook', 35);
insert into produto (descricao, valor) values ('Teclado', 90);
insert into produto (descricao, valor) values ('Nobreak', 450);

-- Pessoas Físicas
insert into pessoa (id, email, telefone) values (1,'henriquefontes@gmail.com', '63984526378');
insert into pessoa_fisica (id, cpf, nome) values (1, '06767965485','Henrique Fontes');

insert into pessoa (id, email, telefone) values (3, 'maria.silva@gmail.com', '63999998888');
insert into pessoa_fisica (id, cpf, nome) values (3, '12345678901', 'Maria Silva');

-- Pessoas Jurídicas
insert into pessoa (id, email, telefone) values (2,'htech@gmail.com', '32127123');
insert into pessoa_juridica (id, cnpj, razao_social) values (2,  '12852269040199','HTech');

insert into pessoa (id, email, telefone) values (4, 'mercadocentral@gmail.com', '6332124455');
insert into pessoa_juridica (id, cnpj, razao_social) values (4, '11222333000155', 'Mercado Central');

insert into venda (data, pessoa_id) values ('2025-10-10T10:00:00', 1);
insert into venda (data, pessoa_id) values ('2025-10-13T12:00:00', 2);
insert into venda (data, pessoa_id) values ('2025-10-25T09:00:00', 3);
insert into venda (data, pessoa_id) values ('2025-11-14T11:30:00', 4);

-- Itens da venda 1
insert into item_venda (quantidade, produto_id, venda_id) values (2, 1, 1);
insert into item_venda (quantidade, produto_id, venda_id) values (1, 2, 1);

-- Itens da venda 2
insert into item_venda (quantidade, produto_id, venda_id) values (3, 3, 2);
insert into item_venda (quantidade, produto_id, venda_id) values (1, 4, 2);

-- Itens da venda 3
insert into item_venda (quantidade, produto_id, venda_id) values (2, 5, 3);
insert into item_venda (quantidade, produto_id, venda_id) values (1, 2, 3);

-- Itens da venda 4
insert into item_venda (quantidade, produto_id, venda_id) values (4, 1, 4);
insert into item_venda (quantidade, produto_id, venda_id) values (1, 3, 4);
