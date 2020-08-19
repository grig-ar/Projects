create sequence office_items_sqs;

create table if not exists Office_Items
(
  office_item_id          bigint primary key default nextval('office_items_sqs'),
  office_item_for_sale    boolean                                                                              not null,
  office_item_amount      int check (office_item_amount >= 0)                                                  not null,
  office_item_recommended int check (office_item_recommended >= 0)                                             not null,
  office_item_critical    int check (office_item_critical >= 0)                                                not null,
  office_item_name        varchar(256)                                                                         not null,
  office_item_cost        numeric(10, 2) check (office_item_cost >= 0),
  office_item_type        varchar(256)                                                                         not null,
  branch_office_id        bigint references branch_offices (branch_office_id) on delete cascade on update cascade not null
);