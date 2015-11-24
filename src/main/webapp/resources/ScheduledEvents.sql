-- SQL script designed for MySQL 5.6

-- scheduled event to remove users inactive for 1 year
create event normal_partner.delete_inactive_users
    on schedule every 1 year starts '2013-12-31'
    do
        delete from normal_partner.users where year(last_login) < year(now());

-- scheduled events to delete pairs 
-- and availability every Aug 1 and Jan 1
create event normal_partner.delete_avail_pairs_aug1
    on schedule every 1 year starts '2014-08-01'
    do
        call normal_partner.truncate_eos();

create event normal_partner.delete_avail_pairs_jan1
    on schedule every 1 year starts '2014-01-01'
    do
        call normal_partner.truncate_eos();

create event normal_partner.check_reg_status
	on schedule every 1 day starts curdate()
	do
		call normal_partner.toggle_registration();
        