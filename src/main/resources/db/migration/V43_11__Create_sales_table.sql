create type sale_status as enum ('PENDING', 'DONE', 'BOOKED', 'CANCELED');

create table if not exists sales
(
    id       uuid default gen_random_uuid() constraint sales_pk primary key,
    price    numeric(10, 2) not null,
    nbr_sale integer        not null,
    status   sale_status    not null
);
