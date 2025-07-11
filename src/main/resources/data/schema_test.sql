
DROP DATABASE IF EXISTS wallet_microservice_test_bd;
DROP DATABASE IF EXISTS wallet_microservice_test_db;

CREATE DATABASE wallet_microservice_test_db;
USE wallet_microservice_test_db;


create table wallet
(
    wallet_id      varchar(50) not null,
    balance decimal(19, 2),
    primary key (wallet_id)
);

create table wallet_transaction
(
    id         varchar(50) not null,
    wallet_id       varchar(50) not null,
    type varchar(20),
    amount     decimal(19, 2),
    time timestamp,
    primary key (id),
    foreign key (wallet_id) references wallet (wallet_id)
);



insert into wallet (wallet_id, balance) values ('walletId1', 1000);
