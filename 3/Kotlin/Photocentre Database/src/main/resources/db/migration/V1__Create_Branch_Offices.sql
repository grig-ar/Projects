create sequence branch_office_sqs;

create table Branch_Offices
(
  branch_office_id                bigint primary key default nextval('branch_office_sqs'),
  branch_office_address           varchar(256)                                            not null,
  branch_office_amount_of_workers int check (branch_office_amount_of_workers >= 0)        not null

);