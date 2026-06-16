create table if not exists genres
(
    id   uuid default gen_random_uuid() constraint genres_pk primary key,
    type varchar(100) not null
);
