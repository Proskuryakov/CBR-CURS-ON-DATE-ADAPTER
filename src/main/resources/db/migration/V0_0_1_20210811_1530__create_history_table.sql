alter table intervals
    add is_actual boolean not null default true,
    drop constraint intervals_valute_id_key;

create table history
(
    id serial primary key,
    interval_id integer not null,
    curse numeric(100, 10) not null,
    datetime timestamp not null default now(),

    foreign key (interval_id) references intervals (id)
);