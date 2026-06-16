create table if not exists admins
(
    user_id uuid constraint admins_pk primary key
        constraint admins_users_fk references users (id) on delete cascade
);
