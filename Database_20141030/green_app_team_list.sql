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
-- Table structure for table `team_list`
--

DROP TABLE IF EXISTS `team_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team_list` (
  `team_name` varchar(30) NOT NULL COMMENT '팀 이름 ',
  `team_pw` varchar(16) NOT NULL COMMENT '비밀번호 ',
  `study_type` varchar(1) NOT NULL COMMENT '1-1, 2-3, 3-익스트림, 4-CIS ',
  `subject` varchar(16) NOT NULL COMMENT '기부 동의 여부. T/F. ',
  `donation_consent` varchar(1) NOT NULL COMMENT '오티 참석 인원 ',
  `orientation` int(1) NOT NULL,
  PRIMARY KEY (`team_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='팀 목록';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_list`
--

LOCK TABLES `team_list` WRITE;
/*!40000 ALTER TABLE `team_list` DISABLE KEYS */;
INSERT INTO `team_list` VALUES ('aaa','aaa','1','네트워크구성및관리','f',0),('cs3','0123','3','null','f',0),('final','0000','2','null','t',2),('fk','p','1','기독교철학','t',2),('kbuctl','0000','3','null','f',0),('sjsj','0000','3','null','f',0),('ssss','ssss','2','null','f',0),('static','0','3','null','f',0),('간호세미나_삼사오','aaa','4','간호세미나','f',0),('모세오경_공공공','aaa','4','모세오경','f',0),('아동건강교육_김이름','0000','4','아동건강교육','f',0),('웹시스템개발_김공오','web','4','웹시스템개발','f',0);
/*!40000 ALTER TABLE `team_list` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-10-30 16:42:15
