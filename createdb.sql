Create Table YelpBusiness (
	business_id VARCHAR2(22),
	city VARCHAR2(50),
	full_address VARCHAR2(256),
	name VARCHAR2(256),
	open CHAR(1),
	review_count Integer,
	stars BINARY_DOUBLE,
	state VARCHAR2(5),
	checkin_count Integer,
	primary key (business_id)
);

create table Hours (
	business_id VARCHAR2(22),
	dayOfWeek VARCHAR2(10),
	openHour CHAR(5),
	closeHour CHAR(5),
	primary key (business_id, dayOfWeek),
	foreign key (business_id) references YelpBusiness(business_id) on delete set null
);

create table YelpBusinessAttributes(
	business_id VARCHAR2(22),
	attributes VARCHAR2(256),
	primary key (business_id, attributes),
	foreign key (business_id) references YelpBusiness(business_id) on delete set null
);

create table YelpMainCategory (
	business_id VARCHAR2(22),
	category VARCHAR2(50),
	primary key (business_id, category),
	foreign key (business_id) references YelpBusiness(business_id) on delete set null
 );

 create table YelpSubCategory (
	business_id VARCHAR2(22),
	category VARCHAR2(50),
	primary key (business_id, category),
	foreign key (business_id) references YelpBusiness(business_id) on delete set null
 );

create table Yelp_Review(
	business_id VARCHAR2(22),
	review_date date,
	review_id VARCHAR2(22),
	stars Integer,
	text CLOB,
	user_id VARCHAR2(22),
	votes_cool Integer,
	votes_funny Integer,
	votes_useful Integer,
	user_name VARCHAR2(256),
	primary key (review_id)
);
