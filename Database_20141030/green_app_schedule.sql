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
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule` (
  `team_name` varchar(30) NOT NULL COMMENT '팀 이름',
  `t1` varchar(1) DEFAULT NULL COMMENT '요일 ',
  `t2` varchar(1) DEFAULT NULL COMMENT '요일 ',
  `t3` varchar(1) DEFAULT NULL COMMENT '요일 ',
  `t4` varchar(1) DEFAULT NULL,
  `t5` varchar(1) DEFAULT NULL,
  `t6` varchar(1) DEFAULT NULL,
  `t7` varchar(1) DEFAULT NULL,
  `t8` varchar(1) DEFAULT NULL,
  `t9` varchar(1) DEFAULT NULL,
  `t10` varchar(1) DEFAULT NULL,
  `t11` varchar(1) DEFAULT NULL,
  `t12` varchar(1) DEFAULT NULL,
  `t13` varchar(1) DEFAULT NULL,
  `t14` varchar(1) DEFAULT NULL,
  `t15` varchar(1) DEFAULT NULL,
  `t16` varchar(1) DEFAULT NULL,
  `t17` varchar(1) DEFAULT NULL,
  `t18` varchar(1) DEFAULT NULL,
  `t19` varchar(1) DEFAULT NULL,
  `t20` varchar(1) DEFAULT NULL,
  `t21` varchar(1) DEFAULT NULL,
  `t22` varchar(1) DEFAULT NULL,
  `t23` varchar(1) DEFAULT NULL,
  `t24` varchar(1) DEFAULT NULL,
  `t25` varchar(1) DEFAULT NULL,
  `t26` varchar(1) DEFAULT NULL,
  `t27` varchar(1) DEFAULT NULL,
  `t28` varchar(1) DEFAULT NULL,
  `t29` varchar(1) DEFAULT NULL,
  `t30` varchar(1) DEFAULT NULL,
  `t31` varchar(1) DEFAULT NULL,
  `t32` varchar(1) DEFAULT NULL,
  `t33` varchar(1) DEFAULT NULL,
  `t34` varchar(1) DEFAULT NULL,
  `t35` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`team_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='금주 시간표 ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES ('','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('aaa','f','f','f','f','f','t','f','f','f','f','f','f','f','f','f','f','f','f','t','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('cs3','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','t','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('final','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('fk','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','t','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('sjsj','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','t','f','f','f','f','f','f','f','f','f','t','f','f','f','f','t','f'),('ssss','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('static','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('간호세미나_삼사오','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('모세오경_공공공','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('아동건강교육_김이름','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f'),('웹시스템개발_김공오','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-10-30 16:42:12
