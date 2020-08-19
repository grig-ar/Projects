create sequence suppliers_sqs;

create table if not exists Suppliers
(
  supplier_id             bigint primary key default nextval('suppliers_sqs'),
  supplier_name           varchar(256) not null,
  supplier_specialization varchar(256) not null
);