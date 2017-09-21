# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  status                        varchar(8),
  blocking_reason               varchar(1000),
  type                          varchar(6),
  name                          varchar(255),
  api_key                       varchar(255),
  constraint ck_app_status check ( status in ('APPROVED','BLOCKED','PENDING','DELETED')),
  constraint ck_app_type check ( type in ('WEB','MOBILE')),
  constraint pk_app primary key (id)
);

create table news (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  status                        varchar(8),
  blocking_reason               varchar(1000),
  title                         varchar(255),
  short_description             varchar(255),
  description                   varchar(4096),
  constraint ck_news_status check ( status in ('APPROVED','BLOCKED','PENDING','DELETED')),
  constraint pk_news primary key (id)
);

create table role (
  role                          varchar(7) not null,
  constraint ck_role_role check ( role in ('edit','create','deleted','view')),
  constraint pk_role primary key (role)
);

create table session (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  user_id                       uuid,
  token                         varchar(1000),
  constraint uq_session_token unique (token),
  constraint pk_session primary key (id)
);

create table users (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  status                        varchar(8),
  blocking_reason               varchar(1000),
  login                         varchar(255),
  password                      bytea,
  type                          varchar(12),
  app_id                        uuid,
  email                         varchar(255),
  name                          varchar(255),
  language                      varchar(255),
  constraint ck_users_status check ( status in ('APPROVED','BLOCKED','PENDING','DELETED')),
  constraint ck_users_type check ( type in ('WEB_CLIENT','APPLIACATION')),
  constraint pk_users primary key (id)
);

create table users_role (
  users_id                      uuid not null,
  role_role                     varchar(7) not null,
  constraint pk_users_role primary key (users_id,role_role)
);

create index ix_news_title on news (title);
alter table users_role add constraint fk_users_role_users foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_users_role_users on users_role (users_id);

alter table users_role add constraint fk_users_role_role foreign key (role_role) references role (role) on delete restrict on update restrict;
create index ix_users_role_role on users_role (role_role);


# --- !Downs

alter table if exists users_role drop constraint if exists fk_users_role_users;
drop index if exists ix_users_role_users;

alter table if exists users_role drop constraint if exists fk_users_role_role;
drop index if exists ix_users_role_role;

drop table if exists app cascade;

drop table if exists news cascade;

drop table if exists role cascade;

drop table if exists session cascade;

drop table if exists users cascade;

drop table if exists users_role cascade;

drop index if exists ix_news_title;
