-- SQL script designed for MySQL 5.6

delimiter //
-- stored procedure to truncate availability and pairs
create procedure normal_partner.truncate_eos()
    begin
        truncate table normal_partner.pairs;
        truncate table normal_partner.availability_child;
        truncate table normal_partner.availability_student;
		truncate table normal_partner.child_requested_partner;
		truncate table noraml_partner.student_requested_partner;
    end //

-- stored procedure to toggle registration if reg_date has passed
create procedure normal_partner.toggle_registration()
	begin
		set @today := curdate();
		select @closeDate := suspense_date from normal_partner.settings;

		select @diff:= datediff(@closeDate, @today);

		if @diff < 0 then
			truncate table normal_partner.settings;
			insert into normal_partner.settings(registration_open, suspense_date)
				values(false, @closeDate);
		end if;
	end //
    
delimiter ;