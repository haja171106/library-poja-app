create table if not exists authors
(
    id        uuid default gen_random_uuid() constraint authors_pk primary key,
    firstname varchar(100) not null,
    lastname  varchar(100) not null,
    birthday  timestamp,
    sex       varchar(10)
);
