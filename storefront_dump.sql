-- MySQL dump 10.13  Distrib 8.2.0, for Win64 (x86_64)
--
-- Host: localhost    Database: storefront
-- ------------------------------------------------------
-- Server version	8.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `cartID` int NOT NULL AUTO_INCREMENT,
  `userEmail` varchar(255) NOT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`cartID`),
  KEY `userEmail` (`userEmail`),
  KEY `sku` (`sku`),
  CONSTRAINT `carts_ibfk_1` FOREIGN KEY (`userEmail`) REFERENCES `users` (`email`),
  CONSTRAINT `carts_ibfk_2` FOREIGN KEY (`sku`) REFERENCES `products` (`sku`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderproduct`
--

DROP TABLE IF EXISTS `orderproduct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderproduct` (
  `orderID` int NOT NULL,
  `sku` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`orderID`,`sku`),
  KEY `sku` (`sku`),
  CONSTRAINT `orderproduct_ibfk_1` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderID`),
  CONSTRAINT `orderproduct_ibfk_2` FOREIGN KEY (`sku`) REFERENCES `products` (`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderproduct`
--

LOCK TABLES `orderproduct` WRITE;
/*!40000 ALTER TABLE `orderproduct` DISABLE KEYS */;
INSERT INTO `orderproduct` VALUES (10000,'PROD002',1),(10001,'PROD002',1),(10001,'PROD003',3);
/*!40000 ALTER TABLE `orderproduct` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `orderID` int NOT NULL AUTO_INCREMENT,
  `shippingAddress` varchar(100) NOT NULL,
  `trackingNumber` int DEFAULT NULL,
  `userEmail` varchar(255) NOT NULL,
  `isShipped` tinyint(1) NOT NULL,
  PRIMARY KEY (`orderID`),
  UNIQUE KEY `trackingNumber` (`trackingNumber`),
  KEY `userEmail` (`userEmail`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`userEmail`) REFERENCES `users` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (10000,'25 Saint Catherine',NULL,'usercc4e08ef-e39f-4dca-9411-887a8a7e631c@storefront.com',0),(10001,'25 Saint Catherine',1234,'usercc4e08ef-e39f-4dca-9411-887a8a7e631c@storefront.com',1),(10002,'123test',NULL,'userb4aa178a-ffb6-419a-b4c4-4d79aa3db938@storefront.com',0);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `sku` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `vendor` varchar(255) DEFAULT NULL,
  `urlSlug` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`sku`),
  UNIQUE KEY `urlSlug` (`urlSlug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES ('PROD001','Smartphone XL','A large screen smartphone with the best display on the market.','ElectroGoods','smartphone-xl',799.99),('PROD002','Laptop Pro','A high-end laptop with powerful performance for professionals.','CompTech','laptop-pro',1499.99),('PROD003','E-Reader ReadWell','Compact e-reader with an eye-friendly display.','ReadTech','e-reader-readwell',129.99),('PROD004','Smartwatch HealthTrack','Advanced smartwatch with health and fitness tracking features.','WearableTech','smartwatch-healthtrack',199.99),('PROD005','Bluetooth Headphones','Wireless headphones with noise cancellation and high-fidelity sound.','SoundTech','bluetooth-headphones',249.99),('PROD006','Gaming Console BoxOne','Next-gen gaming console with 4K HDR gaming experience.','GameWorld','gaming-console-boxone',499.99),('PROD007','Portable Charger 10k','High-capacity portable charger to keep your devices powered on the go.','PowerPack','portable-charger-10k',39.99),('PROD008','Smart Home Speaker','Voice-activated smart speaker for home automation and music.','HomeTech','smart-home-speaker',99.99),('PROD009','Action Camera GoShoot','Durable action camera with 4K video recording capabilities.','ActionCam','action-camera-goshoot',299.99),('PROD010','Tablet Plus','Versatile tablet with a sharp display and fast performance.','ElectroGoods','tablet-plus',599.99);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'usercc4e08ef-e39f-4dca-9411-887a8a7e631c@storefront.com','usercc4e08ef-e39f-4dca-9411-887a8a7e631c@storefront.com','test@123'),(2,'userb4aa178a-ffb6-419a-b4c4-4d79aa3db938@storefront.com','userb4aa178a-ffb6-419a-b4c4-4d79aa3db938@storefront.com','user1*');
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

-- Dump completed on 2023-11-08 14:08:16
