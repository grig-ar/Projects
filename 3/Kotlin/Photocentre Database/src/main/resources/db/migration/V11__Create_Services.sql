create sequence services_sqs;

create table if not exists Services
(
  service_id       bigint primary key default nextval('services_sqs'),
  service_name     varchar(256)                                                                             not null,
  service_cost     numeric(10, 2) check (service_cost >= 0)                                                 not null,
  branch_office_id bigint references branch_offices (branch_office_id) on delete cascade on update cascade     not null
);