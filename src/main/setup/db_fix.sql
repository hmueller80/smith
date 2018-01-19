#ADD A COLUMN TO THE SAMPLE TABLE WITH THE LIBRARY REFERENCE
alter table sample add column library_id int(11) unsigned;

#ADD TO EACH SAMPLE ITS OWN LIBRARY
SET SQL_SAFE_UPDATES=0;
update sample s
left join library l
on s.sam_id=l.sample_id
set s.library_id=l.library_id;

#RE-CREATE LIBRARY TABLE
CREATE TABLE table_temp AS SELECT distinct library_id,libraryName FROM library;

#SET PRIMARY KEY IN LIBRARY 
ALTER TABLE table_temp MODIFY COLUMN library_id INT(11) UNSIGNED PRIMARY KEY AUTO_INCREMENT;

#Take care of samples with no library
DELIMITER //
CREATE PROCEDURE fixNullLibraries ()
BEGIN
	declare finished int default 0;
    declare cur_sample_id int(11) default 0;
    
	declare cursor1 cursor for select sam_id from sample where library_id is null;
    declare continue handler for not found set finished = 1;
    
	open cursor1;
	fixLibrary: loop
		fetch cursor1 into cur_sample_id;
        if finished = 1 then 
			leave fixLibrary;
		end if;
		insert into table_temp (libraryName) values ("UNDEFINED");
		set @library_id := 123456; 
		select @library_id := max(library_id) from table_temp;
		UPDATE table_temp SET libraryName= CONCAT(libraryName,"_L",@library_id) WHERE `library_id`=@library_id;
		update sample set library_id=@library_id where sam_id = cur_sample_id;    
	end loop fixLibrary;
	close cursor1;
END;
//
DELIMITER ;

call fixNullLibraries();

#RE-CREATE LIBRARY TABLE
SET SQL_SAFE_UPDATES=0;
drop table library;
rename table table_temp to library;

#ADD FOREIGN KEY IN SAMPLE
alter table sample add constraint fk_library_id foreign key (library_id) references library(library_id);

 create table barcode (
	id INT unsigned not null auto_increment,
    sequence VARCHAR(100),
    barcode_type VARCHAR(10),
    primary key(id),
    unique(sequence,barcode_type)
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
 
 create table barcode_kit (
    kit_name varchar(100),
    sequence_name varchar(100),
    barcode_id int unsigned,
    primary key(kit_name,sequence_name),
    foreign key(barcode_id) references barcode(id)
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
 
 alter table sample 
 add column barcode_i7 int unsigned, 
 add column barcode_i5 int unsigned, 
 add constraint fk_barcode_i7 foreign key (barcode_i7) references barcode(id),
 add constraint fk_barcode_i5 foreign key (barcode_i5) references barcode(id);
 
 insert into barcode (sequence,barcode_type)
 values ('NONE','i5');
  insert into barcode (sequence,barcode_type)
 values ('NONE','i7');
  insert into barcode (sequence,barcode_type)
 values ('NO_DEMUX','i5');
  insert into barcode (sequence,barcode_type)
 values ('NO_DEMUX','i7');


ALTER TABLE user 
add column first_name varchar(100),
add column last_name varchar(100);

SET SQL_SAFE_UPDATES = 0;
update user
SET first_name = SUBSTRING_INDEX(username, ',', 1), 
last_name = SUBSTRING_INDEX(username, ',', -1);
SET SQL_SAFE_UPDATES = 1;
 
ALTER TABLE user drop column username;