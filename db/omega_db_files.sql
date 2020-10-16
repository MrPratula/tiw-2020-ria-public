-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: omega_db
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `files` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `extension` varchar(255) NOT NULL,
  `creation_date` date NOT NULL,
  `summary` text,
  `owner` int NOT NULL,
  `user_owner` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
INSERT INTO `files` VALUES (1,'tachipirina','pdf','2020-10-11','Da prendere con la febbre',1,1),(2,'oki','pdf','2020-01-01','Da prendere a stomaco pieno',1,1),(3,'morfina','pdf','2015-12-15','Antidolorifico molto potente, controllare ben eil dosaggio',2,1),(4,'cortisone','pdf','2016-02-04','Da comprare, in magazzino ce ne sono poche scorte',2,1),(5,'maria','cal','2020-01-04','L\'ultima volta è arrivata in ritardo',3,1),(6,'sandro','cal','1998-08-06','Per un mese non verrà agli appuntamenti',3,1),(7,'giorgio','cal','1995-04-16','Sta facendo un ottimo lavoro',4,1),(8,'alessia','cal','2016-07-17','Grossi problemi in famiglia, trattare con cautela',4,1),(9,'103568','pdf','1997-01-17','Caso irrisolto',6,2),(10,'103569','pdf','2004-06-05','Prossima udienza è conclusiva',5,2),(11,'203367','pdf','2007-09-04','Udienza molto bizzarra',6,2),(12,'203368','pdf','2003-11-21','Il giudice era ubriaco',6,2),(13,'via_filippo_corridoni','maps','2013-09-06','Si trova in area C, parcheggiare nella via accanto',7,2),(14,'via_francesco_sforza','maps','2014-05-13','Ingresso dal parcheggio sotterraneo',7,2),(15,'via_reggia_di_portici','maps','2001-09-03','Chiuso il sabato',8,2),(16,'via_concezio_muzy','maps','2000-01-01','C\'è il parcheggiatore, avvicinarsi all\'ingresso e lasciare le chiavi all\'accoglienza',8,2),(17,'monza6343','jpg','2016-09-05','Vicino alla COOP',9,3),(18,'milano3234','jpg','2020-01-04','Zona navigli, molto affolata la sera',9,3),(19,'segrate9756','jpg','2020-06-04','Ottima zona, vicino all\'idroscalo',10,3),(20,'pioltello2343','jpg','2020-12-06','Brutto quartiere nella periferia di milano',10,3),(21,'noli7358','jpg','1998-01-24','Villa sul mare con spiaggia',11,3),(22,'savona8023','jpg','1993-05-16','Villetta vicino al porto',11,3),(23,'sestriere7009','jpg','2020-01-04','Zona molto turistica con impianti sci molto grossi',12,3),(24,'torino3760','jpg','1998-05-04','Zona centrale con box sotterraneo',12,3),(50,'deletme','del','1234-11-12','blablabla',1,1),(51,'cancellami','canc','1111-02-03','some text',2,1);
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-16 14:36:33
