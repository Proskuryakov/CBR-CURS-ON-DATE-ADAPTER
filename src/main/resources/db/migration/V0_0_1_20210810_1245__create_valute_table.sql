create table valute
(
    id serial primary key ,
    code varchar(10) unique,
    interval integer
);
