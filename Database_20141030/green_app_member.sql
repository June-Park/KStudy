CREATE DATABASE  IF NOT EXISTS `green_app` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `green_app`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: green_app
-- ------------------------------------------------------
-- Server version	5.1.73-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `member` (
  `no` int(3) NOT NULL AUTO_INCREMENT,
  `team_name` varchar(30) NOT NULL COMMENT '팀 명 ',
  `name` varchar(7) NOT NULL COMMENT '이름 ',
  `phone_number` varchar(11) NOT NULL COMMENT '연락처 ',
  `email_address` varchar(35) NOT NULL COMMENT '이메일 주소 ',
  `major` varchar(10) NOT NULL COMMENT '전공 ',
  `student_number` int(9) NOT NULL COMMENT '학번 ',
  `credits` int(2) DEFAULT NULL COMMENT '신청 학점(Extreme) ',
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='스터디 가입자 신상명세  (9)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'cs3','박','010','1','컴퓨터소프트웨어학',1234,0),(2,'cs3','쿼','4447','2','컴퓨터소프트웨어학',4567,0),(3,'cs3','리','4244','3','컴퓨터소프트웨어학',6787,0),(4,'fk','chj','856','vhd','신학',556,0),(5,'fk','jk','556','gjjd','영유아보육학',855,0),(6,'sjsj','박주원','01044474244','2102400@naver.com','컴퓨터소프트웨어학',201203005,0),(7,'sjsj','송은주','01026725085','songej324@naver.com','컴퓨터소프트웨어학',201203022,0),(8,'aaa','일이삼','01023456789','email@naver.com','신학',123456,0),(9,'aaa','구팔칠','01098765432','email2@naver.com','신학',987654,0),(22,'간호세미나_삼사오','삼사오','01034567890','email3@naver.com','간호학',20123456,0),(23,'간호세미나_삼사오','사오육','01045678901','email4@naver.com','간호학',20124567,0),(24,'모세오경_공공공','공공공','01000000000','00000000@naver.com','신학',200000000,0),(25,'모세오경_공공공','일일일','01011111111','11111111@naver.com','신학',211111111,0),(26,'모세오경_공공공','이이이','01022222222','22222222@naver.com','신학',22222222,0),(27,'아동건강교육_김이름','김이름','01049867859','djslgs@naver.com','영유아보육학',199903022,0),(28,'아동건강교육_김이름','박이름','01064588932','ovsbkyg@naver.com','영유아보육학',200103022,0),(29,'final','최이름','0103692580','pugcl@naver.com','사회복지학',1472580,0),(30,'final','정이름','0106807269','kgcaojg@naver.com','영유아보육학',3692580,0),(31,'ssss','dddd','123456','123456@naver.com','신학',123456,0),(32,'ssss','hhhh','0987654','0987654@naver.com','신학',987654,0),(33,'static','s','0101234','qwer@naver.com','신학',1,0),(34,'static','t','0102345','hidosg@naver.com','간호학',2,0),(35,'static','a','0103456','bdksh@naver.com','사회복지학',3,0),(36,'static','t2','0104567','hslsg@naver.com','컴퓨터소프트웨어학',4,0),(37,'static','i','0105678','gkhs@naver.com','영유아보육학',5,0),(38,'static','c','0106789','hsosjh@naver.com','사회복지학',6,0),(39,'웹시스템개발_김공오','김공오','01025802580','dsjs@naver.com','컴퓨터소프트웨어학',201403005,0),(40,'웹시스템개발_김공오','황이이','01013767913','ksksk@navro.com','컴퓨터소프트웨어학',201403022,0);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-10-30 16:42:14
