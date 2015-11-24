-- SQL script designed for MySQL 5.6 with InnoDB storage engine

-- drop the schema
drop schema if exists normal_partner;

-- create the schema
create schema normal_partner;

set storage_engine=innodb;

-- create the tables
create table normal_partner.users
(
    email      		varchar(100)    primary key,
    password    	varchar(200)    not null,
    created     	date            not null,
    acct_type   	char(1)         not null,
	last_login		timestamp		not null,
	activated		boolean			not null,
	activation_key	varchar(200)
);

create table normal_partner.admin
(
    adminID     bigint       	auto_increment primary key,
    email      	varchar(100)   	not null,
    lastname    varchar(50)     not null,
    firstname   varchar(50)     not null,
	constraint foreign key(email) references normal_partner.users(email) on delete cascade on update cascade
);

create table normal_partner.student
(
    studentID               bigint          auto_increment primary key,
    email                  	varchar(100)    not null,
    lastname                varchar(50)     not null,
    firstname               varchar(50)     not null,
    gender                  char(1)         not null,
    homephone               varchar(13)     not null,
    cellphone               varchar(13)     not null,
    college                 varchar(50)     not null,
    first_time              boolean         not null,
    ortn_complete           boolean         not null,
    bckgrnd_check_complete  boolean         not null,
    notes                   varchar(100),
	is_spec_ed				boolean			not null,
	is_lang_ed				boolean			not null,
    two_chldn               boolean         not null,
	constraint foreign key(email) references normal_partner.users(email) on delete cascade on update cascade
);

create table normal_partner.child
(
    childID             bigint       	auto_increment primary key,
    email	            varchar(100)    not null,
    lastname            varchar(50)     not null,
    firstname           varchar(50)     not null,
    gender              char(1)         not null,
    age                 integer         not null,
    grade               varchar(50)         not null,
    homephone           varchar(13)     not null,
    cellphone           varchar(13)     not null,
    special_needs       boolean         not null,
    language_needs      boolean         not null,
    pnt_gdn_one         varchar(50)     not null,
    pnt_gdn_two         varchar(50),
    notes               varchar(100),
	constraint foreign key(email) references normal_partner.users(email) on delete cascade on update cascade
);

create table normal_partner.availability_student
(
    studentID       bigint          not null,
    day_avail       varchar(10)     not null,
    hrs_avail       varchar(50)     not null,
    primary key(studentID, day_avail, hrs_avail),
	constraint foreign key(studentID) references normal_partner.student(studentID) on delete cascade
);

create table normal_partner.availability_child
(
    childID         bigint          not null,
    day_avail       varchar(10)     not null,
    hrs_avail       varchar(50)     not null,
    primary key(childID, day_avail, hrs_avail),
	constraint foreign key(childID) references normal_partner.child(childID) on delete cascade
);

create table normal_partner.pairs
(
    studentID       bigint          not null,
    childID         bigint          not null,
    session_day     varchar(10)     not null,
    session_hour    varchar(10)     not null,
    primary key(studentID, childID),
	constraint foreign key(studentID) references normal_partner.student(studentID) on delete cascade,
	constraint foreign key(childID) references normal_partner.child(childID) on delete cascade
);

create table normal_partner.student_requested_partner
(
	studentID		bigint			not null,
	firstname		varchar(100)	not null,
	lastname		varchar(100)	not null,
	phone			varchar(50)		not null,
    primary key(studentID, firstname, lastname, phone),
	constraint foreign key(studentID) references normal_partner.student(studentID) on delete cascade
);

create table normal_partner.child_requested_partner
(
	childID			bigint			not null primary key,
	firstname		varchar(100)	not null,
	lastname		varchar(100)	not null,
	phone			varchar(50)		not null,
	constraint foreign key(childID) references normal_partner.child(childID) on delete cascade
);

create table normal_partner.attendance
(
    studentID       bigint          not null,
    date_time       timestamp       not null,
    primary key(studentID, date_time),
	constraint foreign key(studentID) references normal_partner.student(studentID) on delete cascade
);

create table normal_partner.pending
(
    studentID       bigint          not null,
    childID         bigint          not null,
    session_day     varchar(10)     not null,
    session_hour    varchar(10)     not null,
    primary key(studentID, childID),
	constraint foreign key(studentID) references normal_partner.student(studentID) on delete cascade,
	constraint foreign key(childID) references normal_partner.child(childID) on delete cascade
);

create table normal_partner.settings
(
	registration_open	boolean		not null,
	suspense_date		date		not null,
    start_date          date        not null,
    end_date            date        not null
);