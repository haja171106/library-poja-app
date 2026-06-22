alter table sales
alter column status type varchar(20) using status::varchar;