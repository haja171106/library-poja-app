create table if not exists books
(
    id           uuid default gen_random_uuid() constraint books_pk primary key,
    title        varchar(255)   not null,
    isbn         varchar(20) unique,
    nbr_page     integer        not null,
    price        numeric(10, 2) not null,
    release_date timestamp
);
