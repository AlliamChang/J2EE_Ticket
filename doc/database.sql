USE easyticket;

# DROP TABLE IF EXISTS user;
# DROP TABLE IF EXISTS account;
# DROP TABLE IF EXISTS earning;
# DROP TABLE IF EXISTS manager;
# DROP TABLE IF EXISTS plan;
# DROP TABLE IF EXISTS plan_price;
# DROP TABLE IF EXISTS seat;
# DROP TABLE IF EXISTS seat_state;
DROP TABLE IF EXISTS venue;
DROP TABLE IF EXISTS venue_to_approve;
# DROP TABLE IF EXISTS ticket;

/*用户*/
CREATE TABLE user (
  id           INT          NOT NULL AUTO_INCREMENT,
  email        VARCHAR(100) NOT NULL UNIQUE,
  password     VARCHAR(100) NOT NULL,
  token        VARCHAR(255) NOT NULL, #用于验证身份的token
  verification VARCHAR(255) NOT NULL, #用于验证操作的token
  real_name    VARCHAR(20)  NOT NULL,
  username     VARCHAR(100) NOT NULL,
  total_points INT          NOT NULL DEFAULT 0, #总积分
  rest_points  INT          NOT NULL DEFAULT 0, #剩余积分
  flag         INT(2)       NOT NULL DEFAULT 0, #用于标识该账户状态
  regist_time  DATETIME     NOT NULL, #注册时间
  latest_time  DATETIME     NOT NULL, #最新操作时间
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*场馆*/
CREATE TABLE venue (
  id           INT(7)        NOT NULL AUTO_INCREMENT,
  email        VARCHAR(100)  NOT NULL, #用于联系的邮箱，不能用于登录
  password     VARCHAR(255)  NOT NULL,
  token        VARCHAR(255)  NOT NULL,
  flag         INT(2)        NOT NULL DEFAULT 0,
  name         VARCHAR(100)  NOT NULL,
  regist_time  DATETIME      NOT NULL, #注册时间
  location     VARCHAR(255)  NOT NULL,
  account      DOUBLE(10, 2) NOT NULL DEFAULT 0, #记录在该网赚到的钱
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*待审核的场馆信息*/
CREATE TABLE venue_to_approve (
  id           INT(7)        NOT NULL,
  email        VARCHAR(100)  NOT NULL, #用于联系的邮箱，不能用于登录
  flag         INT(2)        NOT NULL DEFAULT 0,
  name         VARCHAR(100)  NOT NULL,
  location     VARCHAR(255)  NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*在新场馆申请注册后，往待审批的表中同时插入一条相同的信息*/
CREATE TRIGGER add_venue_to_approve AFTER INSERT ON venue
  FOR EACH ROW
  INSERT INTO venue_to_approve(id, email, flag, name, location)
      VALUE (new.id, new.email, new.flag, new.name, new.location);

/*模拟银行账号*/
CREATE TABLE account (
  id       INT           NOT NULL,
  password VARCHAR(255)  NOT NULL,
  balance  DOUBLE(10, 2) NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*座位区，规定为矩形*/
CREATE TABLE seat (
  venue_id  INT(7)     NOT NULL, #所属场馆号
  area      INT        NOT NULL, #座位区号
  length    INT        NOT NULL DEFAULT 1, #长
  width     INT        NOT NULL DEFAULT 1, #宽
  direction VARCHAR(1) NOT NULL, #座位朝向
  PRIMARY KEY (venue_id, area)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*展演计划*/
CREATE TABLE plan (
  id           INT          NOT NULL AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL ,
  venue_id     INT(7)       NOT NULL,
  venue_name VARCHAR(100) NOT NULL ,
  time         DATETIME     NOT NULL,
  description  VARCHAR(255) NOT NULL,
  type         VARCHAR(20)  NOT NULL,
  lowest_price DOUBLE(6, 2) NOT NULL DEFAULT 0, #最低价格
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*展演计划座位价格*/
CREATE TABLE plan_price (
  plan_id INT          NOT NULL,
  area    INT          NOT NULL,
  price   DOUBLE(6, 2) NOT NULL DEFAULT 0,
  PRIMARY KEY (plan_id, area)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*展演座位情况(在计划公布后将座位一次性发布，方便查询和记录)*/
CREATE TABLE seat_state (
  plan_id INT    NOT NULL,
  area    INT    NOT NULL,
  row     INT    NOT NULL,
  col     INT    NOT NULL,
  state   INT(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (plan_id, area, row, col)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*购买到的演出票*/
CREATE TABLE ticket (
  id             INT          NOT NULL AUTO_INCREMENT,
  user_id        INT          NOT NULL,
  plan_id        INT          NOT NULL,
  venue_id       INT          NOT NULL,
  area           INT          NOT NULL,
  row            INT          NOT NULL,
  col            INT          NOT NULL,
  time           DATETIME     NOT NULL, #购买时间
  state          INT(3)       NOT NULL DEFAULT 0, #票的状态
  original_price DOUBLE(6, 2) NOT NULL, #原始价格
  actual_price   DOUBLE(6, 2) NOT NULL, #实际价格(会员价)
  is_online      INT(1)       NOT NULL DEFAULT 1, #是否在网上购票
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*经理账号*/
CREATE TABLE manager (
  id INT NOT NULL UNIQUE ,
  username VARCHAR(10)  NOT NULL,
  password VARCHAR(255) NOT NULL,
  token    VARCHAR(255) NOT NULL,
  PRIMARY KEY (username)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*场馆累积赚的钱，由经理结算*/
CREATE TABLE earning (
  venue_id INT(8)        NOT NULL, #id=10000000代表该网站的盈利
  earning  DOUBLE(10, 2) NOT NULL DEFAULT 0,
  PRIMARY KEY (venue_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# INSERT INTO earning VALUE (10000000, 0);