create table valutes
(
    id serial primary key,
    code varchar(3) unique not null,
    name varchar(100) unique not null
);

create table intervals
(
    id serial primary key,
    valute_id integer unique not null,
    interval integer not null,

    foreign key (valute_id) references valutes (id) on delete cascade
);


