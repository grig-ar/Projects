create sequence photos_sqs;

create table if not exists Photos
(
  photo_id         bigint primary key default nextval('photos_sqs'),
  photo_paper_type varchar(256)                                                       not null,
  photo_format     varchar(256)                                                       not null,
  film_id          bigint references films (film_id) on delete cascade on update cascade not null
);