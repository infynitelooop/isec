-- MySQL dump 10.13  Distrib 8.0.42, for macos15 (arm64)
--
-- Host: localhost    Database: isecure
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` char(36) NOT NULL,
  `account_expiry_date` date DEFAULT NULL,
  `account_non_expired` bit(1) NOT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `credentials_expiry_date` date DEFAULT NULL,
  `credentials_non_expired` bit(1) NOT NULL,
  `email` varchar(50) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `is_two_factor_enabled` bit(1) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `password` varchar(120) DEFAULT NULL,
  `phone` varchar(10) NOT NULL,
  `sign_up_method` varchar(255) DEFAULT NULL,
  `tenant_id` char(36) DEFAULT NULL,
  `two_factor_secret` varchar(255) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('435db2fa-5755-4f2d-b81e-78aa3265ae58','2027-01-12',_binary '',_binary '','2026-01-12 10:50:50.563099','2027-01-12',_binary '','sanjay.ranga.au@gmail.com',_binary '','Sanjay',_binary '\0','Ranga','$2a$10$kc1as89tWBTm9/12uBvGd.IP02L0GbDjc9v9L6SP7hfymaBtPHuEa','9876543212','email','91d26718-25c6-4f0f-8ea2-b09258aebc66',NULL,'2026-01-12 10:50:50.563106','rangasanju'),('56620ea4-1f72-4665-9818-36b553f4dddd','2027-01-12',_binary '',_binary '\0','2026-01-12 10:50:50.401215','2027-01-12',_binary '','user1@example.com',_binary '','Rajiv',_binary '\0','Pandey','$2a$10$eRJUCmYIC4x.lMip9toWC.JXGncgNaFCpfAt2pTgw./ebUxUoJcra','9876543210','email','9bb973ff-2980-4ea1-a373-7fee435871b4',NULL,'2026-01-12 10:50:50.401253','user1'),('58381733-e97c-4f51-b29e-a55b2b01fa51','2027-01-12',_binary '',_binary '','2026-01-12 10:50:50.486838','2027-01-12',_binary '','admin@example.com',_binary '','Super',_binary '\0','Admin','$2a$10$N3OlKajT/fcvNkE3b9vEK.u4n9z3Q32KIXG.Xd1PfN/Wr5ED0X1Xe','9876543211','email','9bb973ff-2980-4ea1-a373-7fee435871b4',NULL,'2026-01-12 10:50:50.486847','admin'),('b07c602f-dfdf-47a2-9888-47eaedfd2bcd','2027-01-12',_binary '',_binary '','2026-01-12 10:50:50.640230','2027-01-12',_binary '','rtm1001@gmail.com',_binary '','John',_binary '\0','Wick','$2a$10$k1l0Qwr8Q/zaYQDZU1qSk.AZgppTPeGgpDvd.GfuOwH6MbNGXygQS','9876543212','email',NULL,NULL,'2026-01-12 10:50:50.640241','RTM1001');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-16 23:51:54
