create table affiliation (
    organization_name VARCHAR(100),
    department VARCHAR(100),
    address VARCHAR(200),
    url VARCHAR(2048),
    PRIMARY KEY(organization_name,department)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

alter table user
add column organization_name VARCHAR(100), add column organization_department VARCHAR(100),
add constraint fk_affiliation  foreign key (organization_name,organization_department) references affiliation(organization_name,department);


 create table request (
    id int auto_increment,
    user_id int(11) not null,
    req_date date,
    status varchar(100),
    primary key(id),
    foreign key (user_id) references user(user_id)
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
 
 create table request_library (
	id int auto_increment,
    request_id int not null,
    lib_name varchar(1000),
    lib_type varchar(1000),
    read_mode varchar(2),
    read_length smallint,
    lanes smallint,
    volume double,
    dna_concentration double,
    total_size double,
    primary key(id),
    foreign key (request_id) references request(id)
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;

 create table request_sample (
	id int auto_increment,
    library_id int not null,
    name varchar(1000),
	description varchar(1000),
    organism varchar(1000),
    index_i7 varchar(100),
    adapter_i7 varchar(1000),
    index_i5 varchar(100),
    adapter_i5 varchar(1000),
	primer_index varchar(100),
    primer_name varchar(1000),
    primer_type varchar(1000),
    primary key(id),
    foreign key (library_id) references request_library(id)
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;  