create sequence supplies_sqs;

create table if not exists Supplies
(
  supply_id              bigint primary key default nextval('supplies_sqs'),
  supply_cost            numeric(10, 2) check (supply_cost >= 0)                                                         not null,
  supply_date            date default current_date                                                                       not null,
  supply_completion_date date,
  supplier_id            bigint references suppliers (supplier_id) on delete cascade on update cascade                      not null
);