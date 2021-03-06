begin;

drop schema if exists chat cascade;
create schema chat;

drop table if exists chat.user;
create table chat.user (
  id        serial             not null,
  login     varchar(20) unique not null,
  password  varchar(20)        not null,

  primary key (id)
);

drop table if exists chat.message;
create table chat.message (
  id     serial       not null,
  user_id integer     not null references chat.user on delete cascade,
  time   varchar(5)   not null,
  value  varchar(140) not null,

  primary key (id)
);

commit;
