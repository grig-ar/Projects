create sequence workers_sqs;

create table if not exists Workers
(
  worker_id           bigint primary key default nextval('workers_sqs'),
  worker_name         varchar(256) not null,
  worker_area_of_work varchar(256) not null,
  worker_position     varchar(256) not null
);