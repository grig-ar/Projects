create sequence orders_sqs;

create table if not exists Orders
(
  order_id              bigint primary key default nextval('orders_sqs'),
  order_urgent          boolean                                                                                                                      not null,
  order_cost            numeric(10, 2) check (order_cost >= 0)                                                                                      not null,
  order_date            date default current_date                                                                                                   not null,
  order_completion_date date,
  order_type            varchar(256)                                                                                                                not null,
  branch_office_id      bigint references branch_offices (branch_office_id) on delete cascade on update cascade                                     not null,
  kiosk_id              bigint references kiosks (kiosk_id) on delete cascade on update cascade,
  customer_id           bigint references customers (customer_id) on delete cascade on update cascade                                               not null
);