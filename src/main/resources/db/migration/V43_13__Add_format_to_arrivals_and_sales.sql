create type book_format as enum ('PAPERBACK', 'HARDCOVER', 'SOFTCOVER', 'LARGE_PRINT');

alter table arrivals add column format book_format not null;
alter table sales    add column format book_format not null;