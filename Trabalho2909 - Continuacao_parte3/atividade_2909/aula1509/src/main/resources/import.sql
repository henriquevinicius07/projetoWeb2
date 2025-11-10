insert into produto (descricao, valor) values ('Leite', 4.50);
insert into produto (descricao, valor) values ('Arroz', 42.00);
insert into produto (descricao, valor) values ('Café', 16.00);
insert into produto (descricao, valor) values ('Feijão', 12.00);


insert into pessoa (id, email, telefone) values (1,'henriquefontes@gmail.com', '63984526378');
insert into pessoa_fisica (id, cpf, nome) values (1, '06767965485','Henrique Fontes');

insert into pessoa (id, email, telefone) values (2,'htech@gmail.com', '32127123');
insert into pessoa_juridica (id, cnpj, razao_social) values (2,  '12852269040199','HTech');


insert into venda (data, pessoa_id) values ('2025-10-13T10:00:00', 1);
insert into venda (data, pessoa_id) values ('2025-10-13T12:00:00', 2);

insert into item_venda (quantidade, id, produto_id, venda_id) values (2, 1, 1,1);
insert into item_venda (quantidade, id, produto_id, venda_id) values (1, 2, 2,1);
insert into item_venda (quantidade, id, produto_id, venda_id) values (3, 3, 3,2);
insert into item_venda (quantidade, id, produto_id, venda_id) values (1, 4, 4,2);


