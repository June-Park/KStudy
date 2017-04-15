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
-- Table structure for table `seating_chart`
--

DROP TABLE IF EXISTS `seating_chart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seating_chart` (
  `no` int(2) NOT NULL COMMENT 'PK. AI.',
  `s1` varchar(1) DEFAULT 'f' COMMENT '학습지원1 ',
  `s2` varchar(1) DEFAULT 'f' COMMENT '학습지원2 ',
  `s3` varchar(1) DEFAULT 'f' COMMENT '학습지원3 ',
  `m1` varchar(1) DEFAULT 'f' COMMENT '그룹학습실1 ',
  `m2` varchar(1) DEFAULT 'f' COMMENT '그룹학습실2 ',
  `m3` varchar(1) DEFAULT 'f' COMMENT '그룹학습실3',
  `m4` varchar(1) DEFAULT 'f' COMMENT '그룹학습실4 ',
  `m5` varchar(1) DEFAULT 'f' COMMENT '그룹학습실5 ',
  `m6` varchar(1) DEFAULT 'f' COMMENT '그룹학습실6 ',
  `m7` varchar(1) DEFAULT 'f' COMMENT '그룹학습실7 ',
  `m8` varchar(1) DEFAULT 'f' COMMENT '그룹학습실8 ',
  `m9` varchar(1) DEFAULT 'f' COMMENT '그룹학습실9 ',
  `m10` varchar(1) DEFAULT 'f' COMMENT '그룹학습실10 ',
  `m11` varchar(1) DEFAULT 'f' COMMENT '그룹학습실11 ',
  PRIMARY KEY (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='배치도 현황';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seating_chart`
--

LOCK TABLES `seating_chart` WRITE;
/*!40000 ALTER TABLE `seating_chart` DISABLE KEYS */;
INSERT INTO `seating_chart` VALUES (1,'f','f','t','t','t','f','f','t','t','f','t','f','f','f');
/*!40000 ALTER TABLE `seating_chart` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-10-30 16:42:16
