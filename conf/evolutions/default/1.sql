# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table actor (
  done                      tinyint(1) default 0,
  first_name                varchar(255),
  last_name                 varchar(255))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table actor;

SET FOREIGN_KEY_CHECKS=1;

