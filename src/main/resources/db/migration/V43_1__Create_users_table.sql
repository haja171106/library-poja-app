create table if not exists users
(
    id        uuid default gen_random_uuid() constraint users_pk primary key,
    firstname varchar(100)        not null,
    lastname  varchar(100)        not null,
    email     varchar(255) unique not null,
    birthday  timestamp,
    adress    varchar(255)
);
