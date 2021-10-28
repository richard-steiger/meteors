drop table citizen cascade constraints;
drop table book cascade constraints;
drop sequence citizen_seq;

create sequence citizen_seq
  start with 1
  increment by 1
  cache 20
  order
;

create table citizen (
  local_id number primary key not null,
  name varchar2(255),
  date_of_birth date,
  hair_color varchar2(255),
  subscriber_id number,
  specialty varchar2(255)
);

create table book (
  name varchar2(255) primary key not null,
  checked_out_to number,
  year_published number,
  author varchar2(255),
  constraint fk_checked_out_to_local_id foreign key (checked_out_to) references citizen(local_id) initially deferred
);

