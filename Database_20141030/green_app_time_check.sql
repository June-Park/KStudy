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
-- Table structure for table `time_check`
--

DROP TABLE IF EXISTS `time_check`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `time_check` (
  `team_name` varchar(70) NOT NULL COMMENT '팀이름 ',
  `study_type` int(1) NOT NULL COMMENT '유형 ',
  `assigned_time` int(13) NOT NULL COMMENT '한학기 학습(시간) ',
  `week3` int(3) DEFAULT NULL COMMENT '3주차 ',
  `week4` int(3) DEFAULT NULL COMMENT '4주차 ',
  `week5` int(3) DEFAULT NULL COMMENT '5주차 ',
  `week6` int(3) DEFAULT NULL COMMENT '6주차 ',
  `week7` int(3) DEFAULT NULL COMMENT '7주차 ',
  `week8` int(3) DEFAULT NULL COMMENT '8주차 ',
  `week9` int(3) DEFAULT NULL COMMENT '9주차 ',
  `week10` int(3) DEFAULT NULL COMMENT '10주차 ',
  `week11` int(3) DEFAULT NULL COMMENT '11주차 ',
  `week12` int(3) DEFAULT NULL COMMENT '12주차 ',
  `week13` int(3) DEFAULT NULL COMMENT '13주차 ',
  `sum` int(4) NOT NULL COMMENT '합계(분) ',
  `left_time` int(4) NOT NULL COMMENT '남은 시간(분) ',
  `note` varchar(50) DEFAULT NULL COMMENT '비고 ',
  `no` int(11) NOT NULL AUTO_INCREMENT COMMENT 'AI',
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='금주/누적 시간 확인 ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_check`
--

LOCK TABLES `time_check` WRITE;
/*!40000 ALTER TABLE `time_check` DISABLE KEYS */;
INSERT INTO `time_check` VALUES ('cs3',3,3000,300,400,250,30,175,350,100,NULL,NULL,NULL,NULL,1605,1395,NULL,1),('fk',1,600,60,60,60,75,60,60,50,NULL,NULL,NULL,NULL,425,175,NULL,2),('sjsj',3,3000,400,432,351,250,300,277,200,NULL,NULL,NULL,NULL,2210,790,NULL,3),('aaa',1,600,0,75,90,35,60,80,50,NULL,NULL,NULL,NULL,390,210,NULL,4),('간호세미나_삼사오',4,300,0,60,0,60,0,60,60,NULL,NULL,NULL,NULL,240,60,NULL,6),('모세오경_공공공',4,300,60,60,60,60,0,60,60,NULL,NULL,NULL,NULL,360,-60,NULL,7),('아동건강교육_김이름',4,300,0,0,0,60,60,60,60,NULL,NULL,NULL,NULL,240,60,NULL,8),('final',2,1800,150,200,175,100,125,125,150,NULL,NULL,NULL,NULL,1025,775,NULL,9),('ssss',2,1800,210,185,145,190,85,185,150,NULL,NULL,NULL,NULL,1150,650,NULL,10),('static',3,3000,400,400,400,400,400,400,300,NULL,NULL,NULL,NULL,2700,300,NULL,11),('웹시스템개발_김공오',4,300,60,60,60,80,75,60,60,NULL,NULL,NULL,NULL,455,-155,NULL,12);
/*!40000 ALTER TABLE `time_check` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-10-30 16:42:18
