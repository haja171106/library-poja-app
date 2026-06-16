create table if not exists payments
(
    id    uuid default gen_random_uuid() constraint payments_pk primary key,
    type  varchar(100)   not null,
    price numeric(10, 2) not null
);
