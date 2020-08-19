create sequence kiosks_sqs;

create table Kiosks
(
  kiosk_id                bigint primary key default nextval('kiosks_sqs'),
  kiosk_address           varchar(256)                                                                         not null,
  kiosk_amount_of_workers int check (kiosk_amount_of_workers >= 0)                                             not null,
  branch_office_id        bigint references branch_offices (branch_office_id) on delete cascade on update cascade not null
);