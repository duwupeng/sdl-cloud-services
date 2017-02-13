-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 192.168.1.158    Database: commondb
-- ------------------------------------------------------
-- Server version	5.6.23-log

use `commondb`;

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
-- Dumping data for table `t_code`
--

LOCK TABLES `t_code` WRITE;
/*!40000 ALTER TABLE `t_code` DISABLE KEYS */;
INSERT INTO `t_code` VALUES (1,'null','Industry','互联网/游戏/软件',1,'2016-12-01 03:44:31','system',1),(2,'null','Industry','电子/通信/硬件',1,'2016-12-01 03:44:31','system',1),(3,'null','Industry','房地产/建筑',1,'2016-12-01 03:48:15','system',1),(4,'null','Industry','物业',1,'2016-12-01 03:48:15','system',1),(5,'null','Industry','银行',1,'2016-12-01 03:48:15','system',1),(6,'null','Industry','保险',1,'2016-12-01 03:48:15','system',1),(7,'null','Industry','基金/证劵/期货/投资',1,'2016-12-01 03:48:15','system',1),(8,'null','Industry','互联网金融',1,'2016-12-01 03:48:15','system',1),(9,'null','Industry','快消',1,'2016-12-01 03:48:15','system',1),(10,'null','Industry','鞋服',1,'2016-12-01 03:48:15','system',1),(11,'null','Industry','零售',1,'2016-12-01 03:48:15','system',1),(12,'null','Industry','机械/制造',1,'2016-12-01 03:48:15','system',1),(13,'null','Industry','汽车',1,'2016-12-01 03:48:15','system',1),(14,'null','Industry','专业服务/教育/培训',1,'2016-12-01 03:48:15','system',1),(15,'null','Industry','餐饮/酒店/生活服务',1,'2016-12-01 03:48:15','system',1),(16,'null','Industry','制药/医疗',1,'2016-12-01 03:48:15','system',1),(17,'null','Industry','交通/物流',1,'2016-12-01 03:48:15','system',1),(18,'null','Industry','贸易/进出口',1,'2016-12-01 03:48:15','system',1),(19,'null','Industry','能源/化工/环保',1,'2016-12-01 03:48:15','system',1),(20,'null','Industry','多元化集团',1,'2016-12-01 03:48:15','system',1),(21,'null','Industry','政府/事业单位',1,'2016-12-01 03:48:15','system',1),(22,'null','Industry','其他',1,'2016-12-01 03:48:15','system',1),(23,'null','HighestEducation','初中及以下',1,'2016-12-05 17:21:41','system',1),(24,'null','HighestEducation','中技',1,'2016-12-05 17:24:12','system',2),(25,'null','HighestEducation','中专',1,'2016-12-05 17:24:12','system',3),(26,'null','HighestEducation','高中',1,'2016-12-05 17:24:12','system',4),(27,'null','HighestEducation','高中',1,'2016-12-05 17:24:12','system',5),(28,'null','HighestEducation','本科',1,'2016-12-05 17:24:12','system',6),(29,'null','HighestEducation','硕士',1,'2016-12-05 17:24:13','system',7),(30,'null','HighestEducation','MBA&EMBA',1,'2016-12-05 17:24:13','system',8),(31,'null','HighestEducation','博士及以上',1,'2016-12-05 17:24:13','system',9),(32,'null','WorkYears','学生',1,'2016-12-05 17:28:11','system',1),(33,'null','WorkYears','2年以内',1,'2016-12-05 17:28:11','system',2),(34,'null','WorkYears','3-5年',1,'2016-12-05 17:28:11','system',3),(35,'null','WorkYears','6-8年',1,'2016-12-05 17:28:11','system',4),(36,'null','WorkYears','9-10年',1,'2016-12-05 17:28:11','system',5),(37,'null','WorkYears','11-15年',1,'2016-12-05 17:28:11','system',6),(38,'null','WorkYears','16年以上',1,'2016-12-05 17:28:11','system',7),(39,'null','PoliticalStatus','群众',1,'2016-12-05 17:31:57','system',1),(40,'null','PoliticalStatus','共青团员',1,'2016-12-05 17:31:57','system',2),(41,'null','PoliticalStatus','共产党员',1,'2016-12-05 17:31:57','system',3),(42,'null','PoliticalStatus','其他民主党派',1,'2016-12-05 17:31:57','system',4);
/*!40000 ALTER TABLE `t_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `t_sms_channel`
--

LOCK TABLES `t_sms_channel` WRITE;
/*!40000 ALTER TABLE `t_sms_channel` DISABLE KEYS */;
INSERT INTO `t_sms_channel` VALUES (1,'昊博',1,'http://localhost:10006/sms/post','ms-sms');
/*!40000 ALTER TABLE `t_sms_channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `t_sms_gateway`
--

LOCK TABLES `t_sms_gateway` WRITE;
/*!40000 ALTER TABLE `t_sms_gateway` DISABLE KEYS */;
INSERT INTO `t_sms_gateway` VALUES (1,'sms-manager',1,1);
/*!40000 ALTER TABLE `t_sms_gateway` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-11 11:09:49
