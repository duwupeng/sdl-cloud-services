-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 192.168.1.158    Database: admindb
-- ------------------------------------------------------
-- Server version	5.6.23-log

use `admindb`;

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
-- Dumping data for table `t_permission`
--

LOCK TABLES `t_permission` WRITE;
/*!40000 ALTER TABLE `t_permission` DISABLE KEYS */;
INSERT INTO `t_permission` VALUES (1,'全局帐号设定','c0_0',0),(2,'通知模板','c0_1',0),(3,'使用统计','c0_2',0),(4,'创建/修改项目','c1_0',1),(5,'启用/禁用项目','c1_1',1),(6,'删除项目','c1_2',1),(7,'通知日志','c1_3',1),(8,'帐号管理','c1_4',1),(9,'新建/修改帐号','c1_5',1),(10,'重置密码','c1_6',1),(11,'发送通知','c1_7',1),(12,'导出帐号','c1_8',1),(13,'移除帐号','c1_9',1),(14,'帐号信息设定','c1_10',1),(15,'数据管理','c1_11',1),(16,'导出详细数据','c1_12',1),(17,'创建/修改试卷','c3_0',3),(18,'复制/预览试卷','c3_1',3),(19,'导出试卷','c3_2',3),(20,'删除试卷','c3_3',3),(21,'分组管理','c2_0',2),(22,'管理员列表','c2_1',2),(23,'角色管理','c2_2',2),(24,'角色-权限管理','c2_3',2),(25,'企业信息','c2_4',2),(26,'修改个人密码','c2_5',2),(27,'通知模板(顾问)','c99_1',99),(28,'移除帐号(顾问)','c99_2',99),(29,'创建/修改试卷(顾问)','c99_3',99),(30,'删除试卷(顾问)','c99_4',99),(31,'更新试卷权限(顾问)','c99_5',99),(32,'考官','c4_1',4),(33,'复制项目','c1_13',1),(34,'管理后台基础权限','c5_1',5);
/*!40000 ALTER TABLE `t_permission` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-11 11:08:59
