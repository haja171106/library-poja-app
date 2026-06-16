create table if not exists libraries
(
    id   uuid default gen_random_uuid() constraint libraries_pk primary key,
    name varchar(255) not null
);
