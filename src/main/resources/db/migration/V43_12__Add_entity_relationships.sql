-- Existing rows were created without foreign keys and cannot satisfy the new constraints.
delete from payments;
delete from sales;
delete from arrivals;
delete from book_editions;

-- Sale (N) -> Library (1)
alter table sales
    add column library_id uuid not null;

alter table sales
    add constraint sales_libraries_fk
        foreign key (library_id) references libraries (id) on delete restrict;

-- Arrival (N) -> Library (1)
alter table arrivals
    add column library_id uuid not null;

alter table arrivals
    add constraint arrivals_libraries_fk
        foreign key (library_id) references libraries (id) on delete restrict;

-- Payment (1) -> Sale (1)
alter table payments
    add column sale_id uuid not null;

alter table payments
    add constraint payments_sales_fk
        foreign key (sale_id) references sales (id) on delete cascade;

alter table payments
    add constraint payments_sales_uk unique (sale_id);

-- BookEdition (N) -> Book (1)
alter table book_editions
    add column book_id uuid not null;

alter table book_editions
    add constraint book_editions_books_fk
        foreign key (book_id) references books (id) on delete cascade;

-- Library (N) <-> Book (N)
create table if not exists library_books
(
    library_id uuid not null
        constraint library_books_libraries_fk references libraries (id) on delete cascade,
    book_id    uuid not null
        constraint library_books_books_fk references books (id) on delete cascade,
    constraint library_books_pk primary key (library_id, book_id)
);

-- Book (N) <-> Genre (N)
create table if not exists book_genres
(
    book_id  uuid not null
        constraint book_genres_books_fk references books (id) on delete cascade,
    genre_id uuid not null
        constraint book_genres_genres_fk references genres (id) on delete cascade,
    constraint book_genres_pk primary key (book_id, genre_id)
);

-- Book (N) <-> Author (N)
create table if not exists book_authors
(
    book_id   uuid not null
        constraint book_authors_books_fk references books (id) on delete cascade,
    author_id uuid not null
        constraint book_authors_authors_fk references authors (id) on delete cascade,
    constraint book_authors_pk primary key (book_id, author_id)
);
