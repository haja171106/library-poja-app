alter table sales
    add column book_edition_id uuid;

alter table sales
    add constraint sales_book_editions_fk
        foreign key  (book_edition_id) references book_editions (id) on delete restrict;