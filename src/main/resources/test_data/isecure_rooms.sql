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
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `id` char(36) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `tenant_id` char(36) NOT NULL,
  `ac` bit(1) NOT NULL,
  `attachment` enum('C','I','W') DEFAULT NULL,
  `capacity` int NOT NULL,
  `crew_type` enum('ASST_LOCO_PILOT','GUARD','LOCO_PILOT') DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `floor` int NOT NULL,
  `room_category` enum('FAMILY','FEMALE','GUEST','MALE','STAFF') DEFAULT NULL,
  `room_number` varchar(255) NOT NULL,
  `status` enum('AVAILABLE','BLOCKED','CLEANING','MAINTENANCE','OCCUPIED') DEFAULT NULL,
  `type` enum('DOUBLE','SINGLE','SUITE') DEFAULT NULL,
  `building_id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKoxe0jqawffwhrvieogga8l18q` (`room_number`,`tenant_id`),
  KEY `FKpqy1bbihbdehvp14q3ymjk4y1` (`building_id`),
  CONSTRAINT `FKpqy1bbihbdehvp14q3ymjk4y1` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES ('3fdc354a-12a7-4921-913c-c40e0211313c','2026-01-11 23:50:50.270705','9bb973ff-2980-4ea1-a373-7fee435871b4',_binary '','I',1,'LOCO_PILOT',NULL,0,'MALE','NB102','AVAILABLE','SINGLE','ee9de246-8851-4392-aa6d-839a10a83480'),('5219c0f5-43e5-4f3b-9099-9d226d2b7275','2026-01-11 23:50:50.273835','91d26718-25c6-4f0f-8ea2-b09258aebc66',_binary '','I',1,'LOCO_PILOT',NULL,0,'MALE','L103','AVAILABLE','SINGLE','33dd9458-e130-4eb9-8518-1969cf347b9c'),('5b5e12a5-6367-42bd-8635-0d4eccb31408','2026-01-11 23:50:50.276769','91d26718-25c6-4f0f-8ea2-b09258aebc66',_binary '','I',1,'LOCO_PILOT',NULL,0,'MALE','L104','AVAILABLE','SINGLE','33dd9458-e130-4eb9-8518-1969cf347b9c'),('85be7e69-038d-4e46-8fc1-0a2466e61170','2026-01-11 23:50:50.263080','9bb973ff-2980-4ea1-a373-7fee435871b4',_binary '','I',1,'LOCO_PILOT',NULL,0,'MALE','N101','AVAILABLE','SINGLE','c9006c22-093b-4a6c-90ae-30104bb7663f'),('aae1dee1-c64a-4097-b674-dc959248b793','2026-01-11 23:50:50.267817','9bb973ff-2980-4ea1-a373-7fee435871b4',_binary '','I',1,'LOCO_PILOT',NULL,0,'MALE','N102','AVAILABLE','SINGLE','c9006c22-093b-4a6c-90ae-30104bb7663f');
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
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
