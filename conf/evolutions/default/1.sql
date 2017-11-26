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

create table brand (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  status                        varchar(8),
  blocking_reason               varchar(1000),
  name                          varchar(255),
  description                   varchar(255),
  icon_id                       uuid,
  constraint ck_brand_status check ( status in ('APPROVED','BLOCKED','PENDING','DELETED')),
  constraint uq_brand_name unique (name),
  constraint uq_brand_icon_id unique (icon_id),
  constraint pk_brand primary key (id)
);

create table comment (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  user_id                       uuid,
  message                       varchar(255),
  constraint pk_comment primary key (id)
);

create table file (
  id                            uuid not null,
  type                          varchar(255),
  length                        integer not null,
  data                          bytea,
  thumbnail                     bytea,
  constraint pk_file primary key (id)
);

create table goods (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  status                        varchar(8),
  blocking_reason               varchar(1000),
  name                          varchar(255) not null,
  model                         varchar(255),
  description                   varchar(8192),
  cover_id                      uuid,
  brand_id                      uuid,
  constraint ck_goods_status check ( status in ('APPROVED','BLOCKED','PENDING','DELETED')),
  constraint uq_goods_cover_id unique (cover_id),
  constraint uq_goods_brand_id unique (brand_id),
  constraint pk_goods primary key (id)
);

create table goods_media (
  goods_id                      uuid not null,
  media_id                      uuid not null,
  constraint pk_goods_media primary key (goods_id,media_id)
);

create table goods_property_item (
  goods_id                      uuid not null,
  property_item_id              uuid not null,
  constraint pk_goods_property_item primary key (goods_id,property_item_id)
);

create table goods_tree (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  parent_id                     uuid,
  children_id                   uuid,
  constraint pk_goods_tree primary key (id)
);

create table log (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  user_id                       uuid,
  subject_id                    uuid,
  old_value                     varchar(255),
  new_value                     varchar(255),
  mode                          integer,
  constraint ck_log_mode check ( mode in (0,1)),
  constraint pk_log primary key (id)
);

create table media (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  status                        varchar(8),
  blocking_reason               varchar(1000),
  type                          varchar(5),
  title                         varchar(255),
  url                           text,
  thumb                         text,
  constraint ck_media_status check ( status in ('APPROVED','BLOCKED','PENDING','DELETED')),
  constraint ck_media_type check ( type in ('Image','Video','Html')),
  constraint pk_media primary key (id)
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

create table news_media (
  news_id                       uuid not null,
  media_id                      uuid not null,
  constraint pk_news_media primary key (news_id,media_id)
);

create table orders (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  user_id                       uuid,
  status                        varchar(9),
  price                         bigint,
  constraint ck_orders_status check ( status in ('none','deleted','pending','done','decline','suspended','archive')),
  constraint pk_orders primary key (id)
);

create table orders_comment (
  orders_id                     uuid not null,
  comment_id                    uuid not null,
  constraint pk_orders_comment primary key (orders_id,comment_id)
);

create table orders_log (
  orders_id                     uuid not null,
  log_id                        uuid not null,
  constraint pk_orders_log primary key (orders_id,log_id)
);

create table order_item (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  goods_id                      uuid,
  order_id                      uuid,
  status                        varchar(9),
  amount                        integer,
  by_date                       bigint,
  constraint ck_order_item_status check ( status in ('none','deleted','pending','done','decline','suspended','archive')),
  constraint pk_order_item primary key (id)
);

create table order_item_comment (
  order_item_id                 uuid not null,
  comment_id                    uuid not null,
  constraint pk_order_item_comment primary key (order_item_id,comment_id)
);

create table property (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  status                        varchar(8),
  blocking_reason               varchar(1000),
  name                          varchar(255),
  multi_choose                  boolean default false not null,
  constraint ck_property_status check ( status in ('APPROVED','BLOCKED','PENDING','DELETED')),
  constraint pk_property primary key (id)
);

create table property_item (
  id                            uuid not null,
  when_created                  bigint,
  when_updated                  bigint,
  when_deleted                  bigint,
  status                        varchar(8),
  blocking_reason               varchar(1000),
  name                          varchar(255),
  property_id                   uuid,
  constraint ck_property_item_status check ( status in ('APPROVED','BLOCKED','PENDING','DELETED')),
  constraint pk_property_item primary key (id)
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
alter table brand add constraint fk_brand_icon_id foreign key (icon_id) references media (id) on delete restrict on update restrict;

alter table comment add constraint fk_comment_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_comment_user_id on comment (user_id);

alter table goods add constraint fk_goods_cover_id foreign key (cover_id) references media (id) on delete restrict on update restrict;

alter table goods add constraint fk_goods_brand_id foreign key (brand_id) references brand (id) on delete restrict on update restrict;

alter table goods_media add constraint fk_goods_media_goods foreign key (goods_id) references goods (id) on delete restrict on update restrict;
create index ix_goods_media_goods on goods_media (goods_id);

alter table goods_media add constraint fk_goods_media_media foreign key (media_id) references media (id) on delete restrict on update restrict;
create index ix_goods_media_media on goods_media (media_id);

alter table goods_property_item add constraint fk_goods_property_item_goods foreign key (goods_id) references goods (id) on delete restrict on update restrict;
create index ix_goods_property_item_goods on goods_property_item (goods_id);

alter table goods_property_item add constraint fk_goods_property_item_property_item foreign key (property_item_id) references property_item (id) on delete restrict on update restrict;
create index ix_goods_property_item_property_item on goods_property_item (property_item_id);

alter table goods_tree add constraint fk_goods_tree_parent_id foreign key (parent_id) references goods (id) on delete restrict on update restrict;
create index ix_goods_tree_parent_id on goods_tree (parent_id);

alter table goods_tree add constraint fk_goods_tree_children_id foreign key (children_id) references goods (id) on delete restrict on update restrict;
create index ix_goods_tree_children_id on goods_tree (children_id);

alter table log add constraint fk_log_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_log_user_id on log (user_id);

alter table news_media add constraint fk_news_media_news foreign key (news_id) references news (id) on delete restrict on update restrict;
create index ix_news_media_news on news_media (news_id);

alter table news_media add constraint fk_news_media_media foreign key (media_id) references media (id) on delete restrict on update restrict;
create index ix_news_media_media on news_media (media_id);

alter table orders add constraint fk_orders_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_orders_user_id on orders (user_id);

alter table orders_comment add constraint fk_orders_comment_orders foreign key (orders_id) references orders (id) on delete restrict on update restrict;
create index ix_orders_comment_orders on orders_comment (orders_id);

alter table orders_comment add constraint fk_orders_comment_comment foreign key (comment_id) references comment (id) on delete restrict on update restrict;
create index ix_orders_comment_comment on orders_comment (comment_id);

alter table orders_log add constraint fk_orders_log_orders foreign key (orders_id) references orders (id) on delete restrict on update restrict;
create index ix_orders_log_orders on orders_log (orders_id);

alter table orders_log add constraint fk_orders_log_log foreign key (log_id) references log (id) on delete restrict on update restrict;
create index ix_orders_log_log on orders_log (log_id);

alter table order_item add constraint fk_order_item_goods_id foreign key (goods_id) references goods (id) on delete restrict on update restrict;
create index ix_order_item_goods_id on order_item (goods_id);

alter table order_item add constraint fk_order_item_order_id foreign key (order_id) references orders (id) on delete restrict on update restrict;
create index ix_order_item_order_id on order_item (order_id);

alter table order_item_comment add constraint fk_order_item_comment_order_item foreign key (order_item_id) references order_item (id) on delete restrict on update restrict;
create index ix_order_item_comment_order_item on order_item_comment (order_item_id);

alter table order_item_comment add constraint fk_order_item_comment_comment foreign key (comment_id) references comment (id) on delete restrict on update restrict;
create index ix_order_item_comment_comment on order_item_comment (comment_id);

alter table property_item add constraint fk_property_item_property_id foreign key (property_id) references property (id) on delete restrict on update restrict;
create index ix_property_item_property_id on property_item (property_id);

alter table users_role add constraint fk_users_role_users foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_users_role_users on users_role (users_id);

alter table users_role add constraint fk_users_role_role foreign key (role_role) references role (role) on delete restrict on update restrict;
create index ix_users_role_role on users_role (role_role);


# --- !Downs

alter table if exists brand drop constraint if exists fk_brand_icon_id;

alter table if exists comment drop constraint if exists fk_comment_user_id;
drop index if exists ix_comment_user_id;

alter table if exists goods drop constraint if exists fk_goods_cover_id;

alter table if exists goods drop constraint if exists fk_goods_brand_id;

alter table if exists goods_media drop constraint if exists fk_goods_media_goods;
drop index if exists ix_goods_media_goods;

alter table if exists goods_media drop constraint if exists fk_goods_media_media;
drop index if exists ix_goods_media_media;

alter table if exists goods_property_item drop constraint if exists fk_goods_property_item_goods;
drop index if exists ix_goods_property_item_goods;

alter table if exists goods_property_item drop constraint if exists fk_goods_property_item_property_item;
drop index if exists ix_goods_property_item_property_item;

alter table if exists goods_tree drop constraint if exists fk_goods_tree_parent_id;
drop index if exists ix_goods_tree_parent_id;

alter table if exists goods_tree drop constraint if exists fk_goods_tree_children_id;
drop index if exists ix_goods_tree_children_id;

alter table if exists log drop constraint if exists fk_log_user_id;
drop index if exists ix_log_user_id;

alter table if exists news_media drop constraint if exists fk_news_media_news;
drop index if exists ix_news_media_news;

alter table if exists news_media drop constraint if exists fk_news_media_media;
drop index if exists ix_news_media_media;

alter table if exists orders drop constraint if exists fk_orders_user_id;
drop index if exists ix_orders_user_id;

alter table if exists orders_comment drop constraint if exists fk_orders_comment_orders;
drop index if exists ix_orders_comment_orders;

alter table if exists orders_comment drop constraint if exists fk_orders_comment_comment;
drop index if exists ix_orders_comment_comment;

alter table if exists orders_log drop constraint if exists fk_orders_log_orders;
drop index if exists ix_orders_log_orders;

alter table if exists orders_log drop constraint if exists fk_orders_log_log;
drop index if exists ix_orders_log_log;

alter table if exists order_item drop constraint if exists fk_order_item_goods_id;
drop index if exists ix_order_item_goods_id;

alter table if exists order_item drop constraint if exists fk_order_item_order_id;
drop index if exists ix_order_item_order_id;

alter table if exists order_item_comment drop constraint if exists fk_order_item_comment_order_item;
drop index if exists ix_order_item_comment_order_item;

alter table if exists order_item_comment drop constraint if exists fk_order_item_comment_comment;
drop index if exists ix_order_item_comment_comment;

alter table if exists property_item drop constraint if exists fk_property_item_property_id;
drop index if exists ix_property_item_property_id;

alter table if exists users_role drop constraint if exists fk_users_role_users;
drop index if exists ix_users_role_users;

alter table if exists users_role drop constraint if exists fk_users_role_role;
drop index if exists ix_users_role_role;

drop table if exists app cascade;

drop table if exists brand cascade;

drop table if exists comment cascade;

drop table if exists file cascade;

drop table if exists goods cascade;

drop table if exists goods_media cascade;

drop table if exists goods_property_item cascade;

drop table if exists goods_tree cascade;

drop table if exists log cascade;

drop table if exists media cascade;

drop table if exists news cascade;

drop table if exists news_media cascade;

drop table if exists orders cascade;

drop table if exists orders_comment cascade;

drop table if exists orders_log cascade;

drop table if exists order_item cascade;

drop table if exists order_item_comment cascade;

drop table if exists property cascade;

drop table if exists property_item cascade;

drop table if exists role cascade;

drop table if exists session cascade;

drop table if exists users cascade;

drop table if exists users_role cascade;

drop index if exists ix_news_title;
