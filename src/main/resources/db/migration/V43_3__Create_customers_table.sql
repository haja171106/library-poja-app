create table if not exists customers
(
    user_id uuid constraint customers_pk primary key
        constraint customers_users_fk references users (id) on delete cascade
);
