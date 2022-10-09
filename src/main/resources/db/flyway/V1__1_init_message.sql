DROP TABLE IF EXISTS `receive_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MPDatacube`.`receive_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text,
  `create_time` bigint(20) DEFAULT NULL,
  `from_user_name` varchar(255) DEFAULT NULL,
  `msg_type` varchar(255) DEFAULT NULL,
  `receive_time` datetime DEFAULT NULL,
  `to_user_name` varchar(255) DEFAULT NULL,
  `account_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*!40101 SET character_set_client = @saved_cs_client */;
