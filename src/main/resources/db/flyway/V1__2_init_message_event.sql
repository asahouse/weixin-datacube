DROP TABLE IF EXISTS `receive_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MPDatacube`.`receive_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event` varchar(255) DEFAULT NULL,
  `event_key` varchar(255) DEFAULT NULL,
  `message_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


/*!40101 SET character_set_client = @saved_cs_client */;
