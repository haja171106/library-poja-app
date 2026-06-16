create table if not exists book_editions
(
    id   uuid default gen_random_uuid() constraint book_editions_pk primary key,
    type varchar(100) not null
);
