alter table arrivals
alter column format type varchar(20) using format::varchar;

alter table sales
alter column format type varchar(20) using format::varchar;