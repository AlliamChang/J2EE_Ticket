use easyticket

/*用户*/
CREATE TABLE user(
	id int NOT NULL AUTO_INCREMENT,
	email varchar(100) NOT NULL,
	password varchar(100) NOT NULL,
	token varchar(255) NOT NULL,	#用于验证的token
	real_name varchar(20) NOT NULL,
	username varchar(100) NOT NULL,
	points int NOT NULL DEFAULT 0,	#积分
	flag int(2) NOT NULL DEFAULT 0,	#用于标识该账户状态
	regist_time datetime NOT NULL,	#注册时间
	latest_time datetime NOT NULL,	#最新操作时间
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*场馆*/
CREATE TABLE venue(
	id int(7) NOT NULL,
	name varchar(100) NOT NULL,
	regist_time datetime NOT NULL,	#注册时间
	location varchar(255) NOT NULL,
	account double(10, 2) NOT NULL DEFAULT 0,	#记录在该网赚到的钱
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*模拟银行账号*/
CREATE TABLE account(
	id int NOT NULL,
	password varchar(255) NOT NULL,
	balance double(10, 2) NOT NULL DEFAULT 0,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*座位区，规定为矩形*/
CREATE TABLE seat(
	venue_id int(7) NOT NULL,		#所属场馆号
	area int NOT NULL,				#座位区号
	length int NOT NULL DEFAULT 1,	#长
	width int NOT NULL DEFAULT 1,	#宽
	direction varchar(1) NOT NULL,	#座位朝向
	PRIMARY KEY(venue_id, area)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*展演计划*/
CREATE TABLE plan(
	id int NOT NULL AUTO_INCREMENT,
	venue_id int(7) NOT NULL,
	time datetime NOT NULL,
	description varchar(255) NOT NULL,
	type varchar(10) NOT NULL,
	lowest_price double(6, 2) NOT NULL DEFAULT 0,	#最低价格
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*展演计划座位价格*/
CREATE TABLE plan_price(
	plan_id int NOT NULL,
	area int NOT NULL,
	price double(6, 2) NOT NULL DEFAULT 0,
	PRIMARY KEY(plan_id, area)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*展演座位情况(在计划公布后将座位一次性发布，方便查询和记录)*/
CREATE TABLE seat_state(
	plan_id int NOT NULL,
	area int NOT NULL,
	row int NOT NULL,
	column int NOT NULL,
	state int(3) NOT NULL DEFAULT 0,
	PRIMARY KEY(plan_id, area, row, column)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*购买到的演出票*/
CREATE TABLE ticket(
	id int NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	plan_id int NOT NULL,
	venue_id int NOT NULL,
	area int NOT NULL,
	row int NOT NULL,
	column int NOT NULL,
	time datetime NOT NULL,					#购买时间
	state int(3) NOT NULL DEFAULT 0,		#票的状态
	original_price double(6, 2) NOT NULL,	#原始价格
	actual_price double(6, 2) NOT NULL,		#实际价格(会员价)
	is_online int(1) NOT NULL DEFAULT 1,	#是否在网上购票
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*经理账号*/
CREATE TABLE manager(
	username varchar(10) NOT NULL,
	password varchar NOT NULL,
	PRIMARY KEY(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*场馆累积赚的钱，由经理结算*/
CREATE TABLE earning(
	venue_id int(8) NOT NULL,	#id=10000000代表该网站的盈利
	earning double(10, 2) NOT NULL DEFAULT 0,
	PRIMARY KEY(venue_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;