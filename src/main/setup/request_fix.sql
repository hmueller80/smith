create table organization (
    organization_name VARCHAR(100),
    address VARCHAR(200),
    url VARCHAR(2048),
    PRIMARY KEY(organization_name)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

insert into organization values ("NONE","","");

create table department(
	department_name VARCHAR(100),
    organization_name VARCHAR(100),
    address VARCHAR(200),
    url VARCHAR(2048),
	PRIMARY KEY(department_name,organization_name),
	FOREIGN KEY(organization_name) references organization(organization_name)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

insert into department values ("NONE","NONE","","");

alter table user
add column organization_name VARCHAR(100) not null DEFAULT "NONE", add column organization_department VARCHAR(100) not null DEFAULT "NONE";
alter table user
add constraint fk_affiliation  foreign key (organization_department,organization_name) references department(department_name,organization_name);

 create table request (
    id int auto_increment,
    user_id int(11) not null,
    req_date date,
    billing_contact VARCHAR(100),
    billing_address VARCHAR(200),
    billing_code VARCHAR(200),
    auth_form_name VARCHAR(100),
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
 
 
insert into organization values ("CeMM","Lazarettgasse 14, AKH BT 25.3, 1090 Vienna, Austria","www.cemm.at");
insert into department values ("BSF","CeMM","","http://cemm.at/research/groups/biomedical-sequencing-facility-bsf/");
insert into department values ("NONE","CeMM","","");
