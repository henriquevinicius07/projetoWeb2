insert into produto (descricao, valor) values ('Leite', 4.50);
insert into produto (descricao, valor) values ('Arroz', 42.00);
insert into produto (descricao, valor) values ('Café', 16.00);
insert into produto (descricao, valor) values ('Feijão', 12.00);
insert into produto (descricao, valor) values ('Macarrão', 7.80);

--insert into produto (descricao, valor) values ('Açúcar', 5.20);
--insert into produto (descricao, valor) values ('Óleo de Soja', 8.90);


--Pessoas Físicas
insert into pessoa (id, email, telefone) values (1,'henriquefontes@gmail.com', '63984526378');
insert into pessoa_fisica (id, cpf, nome) values (1, '06767965485','Henrique Fontes');

insert into pessoa (id, email, telefone) values (3, 'maria.silva@gmail.com', '63999998888');
insert into pessoa_fisica (id, cpf, nome) values (3, '12345678901', 'Maria Silva');

--insert into pessoa (id, email, telefone) values (4, 'joao.santos@gmail.com', '63988887777');
--insert into pessoa_fisica (id, cpf, nome) values (4, '98765432100', 'João Santos');


--Pessoas Jurídicas
insert into pessoa (id, email, telefone) values (2,'htech@gmail.com', '32127123');
insert into pessoa_juridica (id, cnpj, razao_social) values (2,  '12852269040199','HTech');

insert into pessoa (id, email, telefone) values (4, 'mercadocentral@gmail.com', '6332124455');
insert into pessoa_juridica (id, cnpj, razao_social) values (4, '11222333000155', 'Mercado Central');

--insert into pessoa (id, email, telefone) values (5, 'contato@supertec.com', '6333445566');
--insert into pessoa_juridica (id, cnpj, razao_social) values (5, '55443322000188', 'SuperTec Tecnologia LTDA');



insert into venda (data, pessoa_id) values ('2025-10-10T10:00:00', 1);
insert into venda (data, pessoa_id) values ('2025-10-13T12:00:00', 2);
insert into venda (data, pessoa_id) values ('2025-10-25T09:00:00', 3);
insert into venda (data, pessoa_id) values ('2025-11-14T11:30:00', 4);

--insert into venda (data, pessoa_id) values ('2025-11-17T15:00:00', 5);


-- Itens da venda 1
insert into item_venda (quantidade, id, produto_id, venda_id) values (2, 1, 1,1);
insert into item_venda (quantidade, id, produto_id, venda_id) values (1, 2, 2,1);

-- Itens da venda 2
insert into item_venda (quantidade, id, produto_id, venda_id) values (3, 3, 3,2);
insert into item_venda (quantidade, id, produto_id, venda_id) values (1, 4, 4,2);

-- Itens da venda 3
insert into item_venda (quantidade, id, produto_id, venda_id) values (2, 5, 5, 3);
insert into item_venda (quantidade, id, produto_id, venda_id) values (1, 6, 2, 3);

-- Itens da venda 4
insert into item_venda (quantidade, id, produto_id, venda_id) values (4, 7, 1, 4);
insert into item_venda (quantidade, id, produto_id, venda_id) values (1, 8, 3, 4);


-- Itens da venda 5
--insert into item_venda (quantidade, id, produto_id, venda_id) values (3, 9, 2, 5);
--insert into item_venda (quantidade, id, produto_id, venda_id) values (2, 10, 3, 5);
