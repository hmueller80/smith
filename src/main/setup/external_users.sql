create table external_users(userid varchar(100) not null, password varchar(64) not null, first_password varchar(10), primary key(userid));

create table external_groups(userid varchar(100) not null, groupid varchar(20) not null, primary key(userid, groupid));

alter table external_groups add constraint FK_USERID foreign key(userid) references external_users(userid);