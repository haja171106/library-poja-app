ALTER TABLE arrivals
    ADD COLUMN book_edition_id UUID;

ALTER TABLE arrivals
    ADD CONSTRAINT fk_arrivals_book_edition
        FOREIGN KEY (book_edition_id) REFERENCES book_editions (id);