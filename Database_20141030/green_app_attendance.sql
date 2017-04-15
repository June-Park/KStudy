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
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attendance` (
  `student_number` int(9) NOT NULL COMMENT '학번 ',
  `team_name` varchar(70) NOT NULL COMMENT '팀이름 ',
  `study_type` int(1) NOT NULL COMMENT '유형 ',
  `assigned_time` int(13) NOT NULL COMMENT '학학기 학습(시간) ',
  `week_3` int(3) DEFAULT NULL COMMENT '3주차 ',
  `week_4` int(3) DEFAULT NULL COMMENT '4주차 ',
  `week_5` int(3) DEFAULT NULL COMMENT '5주차 ',
  `week_6` int(3) DEFAULT NULL COMMENT '6주차 ',
  `week_7` int(3) DEFAULT NULL COMMENT '7주차 ',
  `week_8` int(3) DEFAULT NULL COMMENT '8주차 ',
  `week_9` int(3) DEFAULT NULL COMMENT '9주차 ',
  `week_10` int(3) DEFAULT NULL COMMENT '10주차 ',
  `week_11` int(3) DEFAULT NULL COMMENT '11주차 ',
  `week_12` int(3) DEFAULT NULL COMMENT '12주차 ',
  `week_13` int(3) DEFAULT NULL COMMENT '13주차 ',
  `sum` int(4) NOT NULL COMMENT '합계(분) ',
  `left_time` int(4) NOT NULL COMMENT '남은 시간(분) ',
  `note` varchar(50) DEFAULT NULL COMMENT '비고 ',
  `no` int(11) NOT NULL AUTO_INCREMENT COMMENT 'AI',
  PRIMARY KEY (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='금주/누적 시간 확인 ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-10-30 16:42:11
