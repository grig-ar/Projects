create sequence films_sqs;

create table if not exists Films
(
  film_id      bigint primary key default nextval('films_sqs'),
  film_name    varchar(256) not null,
  sold_item_id bigint references sold_items (sold_item_id) on delete cascade on update cascade,
  order_id     bigint references orders (order_id) on delete cascade on update cascade
);