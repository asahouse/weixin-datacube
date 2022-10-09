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
  `account_id` int(11) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
  `add_to_fav_count` bigint(20) DEFAULT NULL,
  `add_to_fav_user` bigint(20) DEFAULT NULL,
  `int_page_read_count` bigint(20) DEFAULT NULL,
  `int_page_read_user` bigint(20) DEFAULT NULL,
  `msgid` varchar(255) DEFAULT NULL,
  `ori_page_read_count` bigint(20) DEFAULT NULL,
  `ori_page_read_user` bigint(20) DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ArticleSummary_M_R_A_UNIQUE` (`msgid`,`ref_date`,`account_id`),
  KEY `msgid_index` (`msgid`),
  KEY `ref_date_index` (`ref_date`),
  KEY `account_id_index` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `article_total`
--

DROP TABLE IF EXISTS `article_total`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_total` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
  `details` blob,
  `msgid` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ArticleTotal_M_R_A_UNIQUE` (`msgid`,`ref_date`,`account_id`),
  KEY `msgid_index` (`msgid`),
  KEY `ref_date_index` (`ref_date`),
  KEY `account_id_index` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `article_total_detail`
--

DROP TABLE IF EXISTS `article_total_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_total_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
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
  `share_count` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `stat_date` varchar(255) DEFAULT NULL,
  `target_user` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ArticleTotalDetail_M_R_S_A_UNIQUE` (`msgid`,`ref_date`,`stat_date`,`account_id`),
  KEY `msgid_index` (`msgid`),
  KEY `ref_date_index` (`ref_date`),
  KEY `stat_date_index` (`stat_date`),
  KEY `account_id_index` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `catch_event_log`
--

DROP TABLE IF EXISTS `catch_event_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catch_event_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `event_account` varchar(255) DEFAULT NULL,
  `event_detail_error` text,
  `event_name` varchar(255) DEFAULT NULL,
  `event_properties` text,
  `event_status` tinyint(4) DEFAULT NULL,
  `event_then_count` bigint(20) DEFAULT NULL,
  `event_then_limit` bigint(20) DEFAULT NULL,
  `thread_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_cumulate`
--

DROP TABLE IF EXISTS `user_cumulate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_cumulate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
  `cumulate_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UserCumulate_R_U_A_UNIQUE` (`ref_date`,`account_id`),
  KEY `ref_date_index` (`ref_date`),
  KEY `account_id_index` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_read`
--

DROP TABLE IF EXISTS `user_read`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_read` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
  `add_to_fav_count` bigint(20) DEFAULT NULL,
  `add_to_fav_user` bigint(20) DEFAULT NULL,
  `int_page_read_count` bigint(20) DEFAULT NULL,
  `int_page_read_user` bigint(20) DEFAULT NULL,
  `ori_page_read_count` bigint(20) DEFAULT NULL,
  `ori_page_read_user` bigint(20) DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `user_source` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UserRead_U_R_A_UNIQUE` (`user_source`,`ref_date`,`account_id`),
  KEY `user_source_index` (`user_source`),
  KEY `ref_date_index` (`ref_date`),
  KEY `account_id_index` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_read_hour`
--

DROP TABLE IF EXISTS `user_read_hour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_read_hour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
  `add_to_fav_count` bigint(20) DEFAULT NULL,
  `add_to_fav_user` bigint(20) DEFAULT NULL,
  `int_page_read_count` bigint(20) DEFAULT NULL,
  `int_page_read_user` bigint(20) DEFAULT NULL,
  `ori_page_read_count` bigint(20) DEFAULT NULL,
  `ori_page_read_user` bigint(20) DEFAULT NULL,
  `ref_hour` bigint(20) DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `total_online_time` bigint(20) DEFAULT NULL,
  `user_source` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UserReadHour_U_R_R_A_UNIQUE` (`user_source`,`ref_date`,`ref_hour`,`account_id`),
  KEY `user_source_index` (`user_source`),
  KEY `ref_date_index` (`ref_date`),
  KEY `ref_hour_index` (`ref_hour`),
  KEY `account_id_index` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_share`
--

DROP TABLE IF EXISTS `user_share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_share` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_scene` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `user_source` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UserShare_U_R_S_A_UNIQUE` (`user_source`,`ref_date`,`share_scene`,`account_id`),
  KEY `user_source_index` (`user_source`),
  KEY `ref_date_index` (`ref_date`),
  KEY `share_scene_index` (`share_scene`),
  KEY `account_id_index` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_share_hour`
--

DROP TABLE IF EXISTS `user_share_hour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_share_hour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
  `ref_hour` bigint(20) DEFAULT NULL,
  `share_count` bigint(20) DEFAULT NULL,
  `share_scene` bigint(20) DEFAULT NULL,
  `share_user` bigint(20) DEFAULT NULL,
  `user_source` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UserShareHour_U_R_R_S_A_UNIQUE` (`user_source`,`ref_date`,`ref_hour`,`share_scene`,`account_id`),
  KEY `user_source_index` (`user_source`),
  KEY `ref_date_index` (`ref_date`),
  KEY `ref_hour_index` (`ref_hour`),
  KEY `share_scene_index` (`share_scene`),
  KEY `account_id_index` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_summary`
--

DROP TABLE IF EXISTS `user_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `ref_date` varchar(255) DEFAULT NULL,
  `cancel_user` bigint(20) DEFAULT NULL,
  `new_user` bigint(20) DEFAULT NULL,
  `user_source` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UserSummary_U_R_A_UNIQUE` (`user_source`,`ref_date`,`account_id`),
  KEY `user_source_index` (`user_source`),
  KEY `ref_date_index` (`ref_date`),
  KEY `account_id_index` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-17 11:30:20
