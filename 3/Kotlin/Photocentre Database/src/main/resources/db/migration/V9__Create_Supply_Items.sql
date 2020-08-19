create sequence supply_items_sqs;

create table if not exists Supply_Items
(
  supply_item_id     bigint primary key default nextval('supply_items_sqs'),
  supply_item_name   varchar(256)                        not null,
  supply_item_amount int check (supply_item_amount >= 0) not null,
  supply_item_type   varchar(256)                        not null,
  supply_id          bigint references supplies (supply_id) on delete cascade on update cascade
);