create table if not exists arrivals
(
    id       uuid default gen_random_uuid() constraint arrivals_pk primary key,
    nbr_book integer not null
);
