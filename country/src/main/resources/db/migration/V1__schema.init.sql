create table if not exists users
(
    id binary(16) unique not null primary key,
    username varchar(100) unique  not null
);

create table if not exists visit
(
    id binary(16) unique not null primary key,
    user_id binary(16) not null,
    country varchar(100)  not null,
    rating int default 0,
    archived boolean not null default false,
    last_modify_date date not null ,
    foreign key (user_id) references users (id)
);