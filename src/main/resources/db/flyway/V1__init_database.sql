CREATE DATABASE  IF NOT EXISTS `MPDatacube` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `MPDatacube`;
-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: localhost    Database: MPDatacube
-- ------------------------------------------------------
-- Server version	5.7.11

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
-- Table structure for table `article_summary`
--

DROP TABLE IF EXISTS `article_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_to_fav_count` bigint(20) DEFAULT NULL,
  `add_to_fav_user` bigint(20) DEFAULT NULL,
  `int_page_read_count` bigint(20) DEFAULT NULL,
  `int_page_read_user` bigint(20) DEFAULT NULL,
  `msgid` varchar(255) DEFAULT NULL,
  `ori_page_read_count` bigint(20) DEFAULT NULL,
  `ori_page_read_user` bigint(20) DEFAULT NULL,
  `ref_date` date DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_source` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SUMMARY_UNIQUE` (`msgid`,`ref_date`),
  KEY `SUMMARY_MSGID_INDEX` (`msgid`),
  KEY `SUMMARY_REF_DATE_INDEX` (`ref_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_summary`
--

LOCK TABLES `article_summary` WRITE;
/*!40000 ALTER TABLE `article_summary` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_summary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_total`
--

DROP TABLE IF EXISTS `article_total`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_total` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `details` blob,
  `msgid` varchar(255) DEFAULT NULL,
  `ref_date` date DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_source` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TOTAL_DATE_INDEX` (`ref_date`),
  KEY `TOTAL_MSGID_INDEX` (`msgid`),
  KEY `TOTAL_UNIQUE` (`ref_date`,`msgid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_total`
--

LOCK TABLES `article_total` WRITE;
/*!40000 ALTER TABLE `article_total` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_total` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_total_detail`
--

DROP TABLE IF EXISTS `article_total_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_total_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_to_fav_count` bigint(20) DEFAULT NULL,
  `add_to_fav_user` bigint(20) DEFAULT NULL,
  `feed_share_from_feed_cnt` bigint(20) DEFAULT NULL,
  `feed_share_from_feed_user` bigint(20) DEFAULT NULL,
  `feed_share_from_other_cnt` bigint(20) DEFAULT NULL,
  `feed_share_from_other_user` bigint(20) DEFAULT NULL,
  `feed_share_from_session_cnt` bigint(20) DEFAULT NULL,
  `feed_share_from_session_user` bigint(20) DEFAULT NULL,
  `int_page_from_feed_read_count` bigint(20) DEFAULT NULL,
  `int_page_from_feed_read_user` bigint(20) DEFAULT NULL,
  `int_page_from_friends_read_count` bigint(20) DEFAULT NULL,
  `int_page_from_friends_read_user` bigint(20) DEFAULT NULL,
  `int_page_from_hist_msg_read_count` bigint(20) DEFAULT NULL,
  `int_page_from_hist_msg_read_user` bigint(20) DEFAULT NULL,
  `int_page_from_other_read_count` bigint(20) DEFAULT NULL,
  `int_page_from_other_read_user` bigint(20) DEFAULT NULL,
  `int_page_from_session_read_count` bigint(20) DEFAULT NULL,
  `int_page_from_session_read_user` bigint(20) DEFAULT NULL,
  `int_page_read_count` bigint(20) DEFAULT NULL,
  `int_page_read_user` bigint(20) DEFAULT NULL,
  `msgid` varchar(255) DEFAULT NULL,
  `ori_page_read_count` bigint(20) DEFAULT NULL,
  `ori_page_read_user` bigint(20) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `stat_date` date DEFAULT NULL,
  `target_user` bigint(20) DEFAULT NULL,
  `user_source` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TOTAL_DETAIL_MSGID_INDEX` (`msgid`),
  KEY `TOTAL_DETAIL_STAT_DATE_INDEX` (`stat_date`),
  KEY `TOTAL_DETAIL_REF_DATE_INDEX` (`ref_date`),
  KEY `TOTAL_DETAIL_UNIQUE` (`msgid`,`ref_date`,`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_total_detail`
--

LOCK TABLES `article_total_detail` WRITE;
/*!40000 ALTER TABLE `article_total_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_total_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_read`
--

DROP TABLE IF EXISTS `user_read`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_read` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_to_fav_count` bigint(20) DEFAULT NULL,
  `add_to_fav_user` bigint(20) DEFAULT NULL,
  `int_page_read_count` bigint(20) DEFAULT NULL,
  `int_page_read_user` bigint(20) DEFAULT NULL,
  `ori_page_read_count` bigint(20) DEFAULT NULL,
  `ori_page_read_user` bigint(20) DEFAULT NULL,
  `ref_date` date DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `user_source` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `READ_UNIQUE` (`ref_date`,`user_source`),
  KEY `READ_REF_DATE_INDEX` (`ref_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_read`
--

LOCK TABLES `user_read` WRITE;
/*!40000 ALTER TABLE `user_read` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_read` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_read_hour`
--

DROP TABLE IF EXISTS `user_read_hour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_read_hour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_to_fav_count` bigint(20) DEFAULT NULL,
  `add_to_fav_user` bigint(20) DEFAULT NULL,
  `int_page_read_count` bigint(20) DEFAULT NULL,
  `int_page_read_user` bigint(20) DEFAULT NULL,
  `ori_page_read_count` bigint(20) DEFAULT NULL,
  `ori_page_read_user` bigint(20) DEFAULT NULL,
  `ref_date` date DEFAULT NULL,
  `ref_hour` bigint(20) DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `total_online_time` bigint(20) DEFAULT NULL,
  `user_source` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `READ_HOUR_UNIQUE` (`ref_date`,`ref_hour`,`user_source`),
  KEY `READ_HOUR_REF_DATE_INDEX` (`ref_date`),
  KEY `READ_HOUR_REF_HOUR_INDEX` (`ref_hour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_read_hour`
--

LOCK TABLES `user_read_hour` WRITE;
/*!40000 ALTER TABLE `user_read_hour` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_read_hour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_share`
--

DROP TABLE IF EXISTS `user_share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_share` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_date` date DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_scene` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `user_source` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SHARE_UNIQUE` (`ref_date`,`user_source`),
  KEY `SHARE_REF_DATE_INDEX` (`ref_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_share`
--

LOCK TABLES `user_share` WRITE;
/*!40000 ALTER TABLE `user_share` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_share` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_share_hour`
--

DROP TABLE IF EXISTS `user_share_hour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_share_hour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_date` date DEFAULT NULL,
  `ref_hour` bigint(20) DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_scene` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `user_source` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SHARE_HOUR_UNIQUE` (`ref_hour`,`ref_date`,`user_source`),
  KEY `SHARE_HOUR_REF_DATE_INDEX` (`ref_date`),
  KEY `SHARE_HOUR_REF_HOUR_INDEX` (`ref_hour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_share_hour`
--

LOCK TABLES `user_share_hour` WRITE;
/*!40000 ALTER TABLE `user_share_hour` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_share_hour` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-07-03 14:50:46
