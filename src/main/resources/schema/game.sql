begin;

drop schema if exists game cascade;
create schema game;

drop table if exists game.playerData;
create table game.player (
  id                serial             not null,
  name              varchar(20) unique not null,
  password          varchar(16)        not null,
  rating            integer            not null,
  playing            boolean            not null,

  primary key (id)
);

drop table if exists game.session_data;
create table game.sessionData (
  id        serial       not null,

  primary key (id)
);

drop table if exists game.chat_logs;
create table game.chatLogs (
  id        serial       not null,

  primary key (id)
);

commit;

