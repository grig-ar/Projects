create sequence customers_sqs;

create table if not exists Customers
(
  customer_id       bigint primary key default nextval('customers_sqs'),
  customer_name     varchar(256)                                    not null,
  customer_discount int check (customer_discount between 0 and 100) not null,
  customer_experience   int check (customer_discount between 0 and 100) not null,
  branch_office_id        bigint references branch_offices (branch_office_id)
);