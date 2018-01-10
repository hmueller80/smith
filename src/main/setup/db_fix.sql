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
