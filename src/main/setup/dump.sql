CREATE DATABASE  IF NOT EXISTS `ngslims` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ngslims`;
-- MySQL dump 10.13  Distrib 5.6.11, for Win64 (x86_64)
--
-- Host: localhost    Database: ngslims2
-- ------------------------------------------------------
-- Server version	5.6.11-log

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
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
  `news_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `header` varchar(255) NOT NULL,
  `body` varchar(1024) NOT NULL,
  PRIMARY KEY (`news_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` VALUES (1,STR_TO_DATE('1-01-2014', '%d-%m-%Y'), '140228_SN880_0250_AC3PJ5ACXX', 'data have been analyzed and delivered to the users'), (2,STR_TO_DATE('1-01-2014', '%d-%m-%Y'), '131216_SN880_0231_BC2C1PACXX', '131216_SN880_0231_BC2C1PACXX has finished'), (3,STR_TO_DATE('1-01-2014', '%d-%m-%Y'), '131011_SN880_0217_BC2LA2ACXX', 'run has finished and analyzed data have been delivered to the users.');
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `communications`
--

DROP TABLE IF EXISTS `communications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `communications` (
  `user_id` int(11) NOT NULL,
  `collaborator_id` int(11) NOT NULL,
PRIMARY KEY (`user_id`,`collaborator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `communications`
--

LOCK TABLES `communications` WRITE;
/*!40000 ALTER TABLE `communications` DISABLE KEYS */;
/*!40000 ALTER TABLE `communications` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `attribute`
--

DROP TABLE IF EXISTS `attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute` (
  `idattribute` int(11) NOT NULL AUTO_INCREMENT,
  `attributename` varchar(45) NOT NULL,
  PRIMARY KEY (`idattribute`),
  UNIQUE KEY `attributename_UNIQUE` (`attributename`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute`
--

LOCK TABLES `attribute` WRITE;
/*!40000 ALTER TABLE `attribute` DISABLE KEYS */;
/*INSERT INTO `attribute` VALUES (3,'cell-line'),(2,'phenotype'),(1,'tissue');*/
/*!40000 ALTER TABLE `attribute` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application` (
  `application_id` int(11) NOT NULL AUTO_INCREMENT,
  `readlength` int(11) DEFAULT NULL,
  `readmode` varchar(100) DEFAULT NULL,
  `instrument` varchar(100) DEFAULT NULL,
  `applicationname` varchar(100) DEFAULT NULL,
  `depth` int(11) DEFAULT NULL,
  PRIMARY KEY (`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COMMENT='Can be reused, so we separete it from Sample';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES (1,50,'SR','HiSeq2000','Single read : 30 mio reads : one fiths of a lane : 50 bases',30),(2,50,'SR','HiSeq2000','Single read : 60 mio reads : two fiths of a lane : 50 bases',60),(3,50,'SR','HiSeq2000','Single read : 90 mio reads : three fiths of a lane : 50 bases',90),(4,50,'SR','HiSeq2000','Single read : 120 mio reads : four fiths of a lane : 50 bases',120),(5,50,'SR','HiSeq2000','Single read : 150 mio reads : full lane : 50 bases',150),(6,50,'PE','HiSeq2000','Paired end : 35 mio reads : one fourth of a lane : 50 bases',35),(7,50,'PE','HiSeq2000','Paired end : 70 mio reads : half a lane : 50 bases',70),(8,50,'PE','HiSeq2000','Paired end : 105 mio reads : three fourth of a lane : 50 bases',105),(9,50,'PE','HiSeq2000','Paired end : 140 mio reads : full lane : 50 bases',140),(10,75,'PE','HiSeq2000','Paired end : 35 mio reads : one fourth of a lane : 75 bases',35),(11,75,'PE','HiSeq2000','Paired end : 70 mio reads : half a lane : 75 bases',70),(12,75,'PE','HiSeq2000','Paired end : 105 mio reads : three fourth of a lane : 75 bases',105),(13,75,'PE','HiSeq2000','Paired end : 140 mio reads : full lane : 75 bases',140),(14,100,'PE','HiSeq2000','Paired end : 35 mio reads : one fourth of a lane : 100 bases',35),(15,100,'PE','HiSeq2000','Paired end : 70 mio reads : half a lane : 100 bases',70),(16,100,'PE','HiSeq2000','Paired end : 105 mio reads : three fourth of a lane : 100 bases',105),(17,100,'PE','HiSeq2000','Paired end : 140 mio reads : full lane : 100 bases',140),(18,75,'SR','HiSeq2000','Single read : 30 mio reads : one fiths of a lane : 75 bases',30),(19,75,'SR','HiSeq2000','Single read : 60 mio reads : two fiths of a lane : 75 bases',60),(20,75,'SR','HiSeq2000','Single read : 90 mio reads : three fiths of a lane : 75 bases',90),(21,75,'SR','HiSeq2000','Single read : 120 mio reads : four fiths of a lane : 75 bases',120),(22,75,'SR','HiSeq2000','Single read : 150 mio reads : full lane : 75 bases',150),(23,100,'SR','HiSeq2000','Single read : 30 mio reads : one fiths of a lane : 100 bases',30),(24,100,'SR','HiSeq2000','Single read : 60 mio reads : two fiths of a lane : 100 bases',60),(25,100,'SR','HiSeq2000','Single read : 90 mio reads : three fiths of a lane : 100 bases',90),(26,100,'SR','HiSeq2000','Single read : 120 mio reads : four fiths of a lane : 100 bases',120),(27,100,'SR','HiSeq2000','Single read : 150 mio reads : full lane : 100 bases',150),(28,50,'SR','HiSeq2000','ExomeSeq',30),(29,50,'SR','HiSeq2000','RRBS',30),(30,50,'PE','GA-II','ExomeSeq',90),(31,50,'SR','GA-II','mRNA-Seq',180),(32,36,'SR','HiSeq2000','mRNA-Seq',30),(33,50,'SR','GA-II','ExomeSeq',180),(34,50,'SR','GA-II','mRNA-Seq',30),(35,50,'SR','HiSeq2000','mRNA-Seq',30),(36,50,'SR','HiSeq2000','Methylation',30),(37,50,'SR','HiSeq2000','DNA-Seq',30);
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `login` varchar(100) NOT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `mailadress` varchar(100) DEFAULT NULL,
  `pi` int(11) NOT NULL,
  `userRole` varchar(45) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Admin, Admin','admin','123','mail',1,'admin'), (2,'Guest, Guest','guest','123','mail',1,'guest'), (3,'User, One','uone','123','mail',6,'user'),(4,'User, Two','utwo','123','mail',7,'user'),(5,'User, Three','uthree','123','mail',8,'user'),(6,'Groupleader, One','gone','123','mail',6,'groupleader'),(7,'Groupleader, Two','gtwo','123','mail',7,'groupleader'),(8,'Groupleader, Three','gthree','123','mail',8,'groupleader'),(9,'Technician, One','tone','123','mail',6,'technician'),(10,'Technician, Two','ttwo','123','mail',6,'technician'),(11,'Technician, Three','tthree','123','mail',6,'technician');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


--
-- Table structure for table `sequencingindexes`
--

DROP TABLE IF EXISTS `sequencingindexes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequencingindexes` (
  `idsequencingindexes` int(11) NOT NULL AUTO_INCREMENT,
  `index` varchar(45) NOT NULL,
  PRIMARY KEY (`idsequencingindexes`),
  UNIQUE KEY `index_UNIQUE` (`index`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequencingindexes`
--

LOCK TABLES `sequencingindexes` WRITE;
/*!40000 ALTER TABLE `sequencingindexes` DISABLE KEYS */;
INSERT INTO `sequencingindexes` VALUES (50,'AACGGA'),(51,'AAGAAC'),(6,'ACAGTG'),(26,'ACTGAT'),(52,'ACTTAG'),(9,'ACTTGA'),(14,'AGTCAA'),(15,'AGTTCC'),(2,'ATCACG'),(27,'ATGAGC'),(16,'ATGTCA'),(28,'ATTCCT'),(29,'CAAAAG'),(30,'CAACTA'),(31,'CACCGG'),(32,'CACGAT'),(33,'CACTCA'),(8,'CAGATC'),(34,'CAGGCG'),(35,'CATGGC'),(36,'CATTTT'),(37,'CCAACA'),(53,'CCGCGA'),(17,'CCGTCC'),(3,'CGATGT'),(38,'CGGAAT'),(54,'CGGACT'),(23,'CGTACG'),(55,'CTAATG'),(39,'CTAGCT'),(40,'CTATAC'),(41,'CTCAGA'),(56,'CTCTAT'),(13,'CTTGTA'),(57,'GAAGTC'),(42,'GACGAC'),(24,'GAGTGG'),(10,'GATCAG'),(58,'GCATCT'),(7,'GCCAAT'),(59,'GGCAAG'),(12,'GGCTAC'),(25,'GGTAGC'),(60,'GGTCGC'),(18,'GTAGAG'),(19,'GTCCGC'),(20,'GTGAAA'),(21,'GTGGCC'),(22,'GTTTCG'),(1,'none'),(43,'TAATCG'),(44,'TACAGC'),(11,'TAGCTT'),(45,'TATAAT'),(46,'TCATTC'),(47,'TCCCGA'),(48,'TCGAAG'),(49,'TCGGCA'),(61,'TCGTTC'),(5,'TGACCA'),(4,'TTAGGC');
/*!40000 ALTER TABLE `sequencingindexes` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `reagent`
--

DROP TABLE IF EXISTS `reagent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reagent` (
  `reagentbarcode` varchar(100) NOT NULL,
  `operator_user_id` int(11) NOT NULL,
  `application` varchar(100) DEFAULT NULL,
  `cataloguenumber` varchar(100) DEFAULT NULL,
  `supportedreactions` int(11) DEFAULT NULL,
  `receptiondate` date DEFAULT NULL,
  `expirationdate` date DEFAULT NULL,
  `price` double DEFAULT NULL,
  `comments` varchar(1024) DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `institute` varchar(100) NOT NULL,
  `costcenter` varchar(100) NOT NULL,
  PRIMARY KEY (`reagentbarcode`),
  UNIQUE KEY `reagentbarcode_UNIQUE` (`reagentbarcode`),
  KEY `operator_user_id_idx` (`operator_user_id`),
  KEY `owner_user_id_idx` (`owner_id`),
  CONSTRAINT `operator_user_id` FOREIGN KEY (`operator_user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `owner_user_id` FOREIGN KEY (`owner_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reagent`
--

LOCK TABLES `reagent` WRITE;
/*!40000 ALTER TABLE `reagent` DISABLE KEYS */;
/*!40000 ALTER TABLE `reagent` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `sample`
--

DROP TABLE IF EXISTS `sample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample` (
  `sam_id` int(11) NOT NULL AUTO_INCREMENT,
  `requester_user_id` int(11) NOT NULL,
  `application_id` int(11) NOT NULL,
  `organism` varchar(100) NOT NULL,
  `type` varchar(75) NOT NULL,
  `antibody` varchar(100) DEFAULT NULL,
  `librarysynthesisneeded` bit(1) NOT NULL,
  `concentration` double NOT NULL,
  `totalamount` double NOT NULL,
  `bulkfragmentsize` double NOT NULL,
  `costcenter` varchar(255) NOT NULL,
  `status` varchar(45) NOT NULL,
  `name` varchar(75) NOT NULL,
  `comment` varchar(1024) NOT NULL,
  `description` varchar(1024) NOT NULL,
  `requestdate` date DEFAULT NULL,
  `bioanalyzerdate` date DEFAULT NULL,
  `bionalyzerbiomolarity` double DEFAULT NULL,
  `timeSeriesStep` datetime DEFAULT NULL,
  `experimentName` varchar(45) DEFAULT NULL,
  `sequencingIndexId` int(11) DEFAULT NULL,
  PRIMARY KEY (`sam_id`),
  KEY `request_id_idx` (`requester_user_id`),
  KEY `application_id_idx` (`application_id`),
  KEY `seqIndex_id_idx` (`sequencingIndexId`),
  CONSTRAINT `application_id` FOREIGN KEY (`application_id`) REFERENCES `application` (`application_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `requester_user_id` FOREIGN KEY (`requester_user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `seqIndex_id` FOREIGN KEY (`sequencingIndexId`) REFERENCES `sequencingindexes` (`idsequencingindexes`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=311 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sample`
--

LOCK TABLES `sample` WRITE;
/*!40000 ALTER TABLE `sample` DISABLE KEYS */;
/*!40000 ALTER TABLE `sample` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `samplerun`
--

DROP TABLE IF EXISTS `samplerun`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplerun` (
  `run_id` int(11) NOT NULL AUTO_INCREMENT,
  `sam_id` int(11) NOT NULL,
  `operator_user_id` int(11) NOT NULL,
  `sampleprep_reagent_code` varchar(100) DEFAULT NULL,
  `clustergeneration_reagent_code` varchar(100) DEFAULT NULL,
  `sequencing_reagent_code` varchar(100) DEFAULT NULL,
  `flowcell` varchar(75) NOT NULL,
  `runfolder` varchar(1024) NOT NULL,
  `iscontrol` bit(1) NOT NULL,
  PRIMARY KEY (`run_id`,`sam_id`),
  KEY `operator_uder_id_idx` (`operator_user_id`),
  KEY `sam_id_idx` (`sam_id`),
  KEY `sampleprep_reagent_code_idx` (`sampleprep_reagent_code`),
  KEY `clustergeneration_reagent_code_idx` (`clustergeneration_reagent_code`),
  KEY `sequencing_reagent_code_idx` (`sequencing_reagent_code`),
  CONSTRAINT `clustergeneration_reagent_code` FOREIGN KEY (`clustergeneration_reagent_code`) REFERENCES `reagent` (`reagentbarcode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sampleprep_reagent_code` FOREIGN KEY (`sampleprep_reagent_code`) REFERENCES `reagent` (`reagentbarcode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sequencing_reagent_code` FOREIGN KEY (`sequencing_reagent_code`) REFERENCES `reagent` (`reagentbarcode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `operator_uder_id` FOREIGN KEY (`operator_user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sam_id` FOREIGN KEY (`sam_id`) REFERENCES `sample` (`sam_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samplerun`
--

LOCK TABLES `samplerun` WRITE;
/*!40000 ALTER TABLE `samplerun` DISABLE KEYS */;
/*!40000 ALTER TABLE `samplerun` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rawdata`
--

DROP TABLE IF EXISTS `rawdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rawdata` (
  `raw_id` int(11) NOT NULL AUTO_INCREMENT,
  `run_id` int(11) NOT NULL,
  `sam_id` int(11) NOT NULL,
  `folder_URL` varchar(576) NOT NULL,
  `bcltofastqcommnad` varchar(576) NOT NULL,
  `fastqcreferencer1` varchar(576) NOT NULL,
  `fastqcreferencer2` varchar(576) NOT NULL,
  PRIMARY KEY (`raw_id`),
  KEY `run_id_idx` (`run_id`,`sam_id`),
  CONSTRAINT `runSam_id` FOREIGN KEY (`run_id`, `sam_id`) REFERENCES `samplerun` (`run_id`, `sam_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rawdata`
--

LOCK TABLES `rawdata` WRITE;
/*!40000 ALTER TABLE `rawdata` DISABLE KEYS */;
/*!40000 ALTER TABLE `rawdata` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `aligneddata`
--

DROP TABLE IF EXISTS `aligneddata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aligneddata` (
  `processed_id` int(11) NOT NULL AUTO_INCREMENT,
  `raw_id` int(11) NOT NULL,
  `file_URL` varchar(576) NOT NULL,
  `assembly` varchar(45) NOT NULL,
  `fileformat` varchar(45) NOT NULL,
  `alalgo` varchar(100) NOT NULL,
  `alparams` varchar(1000) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`processed_id`),
  KEY `raw_id_idx` (`raw_id`),
  CONSTRAINT `raw_id` FOREIGN KEY (`raw_id`) REFERENCES `rawdata` (`raw_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aligneddata`
--

LOCK TABLES `aligneddata` WRITE;
/*!40000 ALTER TABLE `aligneddata` DISABLE KEYS */;
/*!40000 ALTER TABLE `aligneddata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `annotateddata`
--

DROP TABLE IF EXISTS `annotateddata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `annotateddata` (
  `processed_id` int(11) NOT NULL AUTO_INCREMENT,
  `rawdata_id` int(11) NOT NULL,
  `file_URL` varchar(576) NOT NULL,
  `assembly` varchar(45) NOT NULL,
  `fileformat` varchar(45) NOT NULL,
  `aligned_id` int(11) DEFAULT NULL,
  `analgo` varchar(100) NOT NULL,
  `anparams` varchar(1000) DEFAULT NULL,
  `inputdata_id` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`processed_id`),
  KEY `raw_id_idx` (`rawdata_id`),
  KEY `aligned_id_idx` (`aligned_id`),
  KEY `inputdata_idx` (`inputdata_id`),
  CONSTRAINT `aligned_id` FOREIGN KEY (`aligned_id`) REFERENCES `aligneddata` (`processed_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `inputdata` FOREIGN KEY (`inputdata_id`) REFERENCES `annotateddata` (`processed_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `rawdata_id` FOREIGN KEY (`rawdata_id`) REFERENCES `rawdata` (`raw_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `annotateddata`
--

LOCK TABLES `annotateddata` WRITE;
/*!40000 ALTER TABLE `annotateddata` DISABLE KEYS */;
/*!40000 ALTER TABLE `annotateddata` ENABLE KEYS */;
UNLOCK TABLES;





--
-- Table structure for table `attributevalue`
--

DROP TABLE IF EXISTS `attributevalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attributevalue` (
  `sampleid` int(11) NOT NULL,
  `attributeid` int(11) NOT NULL,
  `value` varchar(100) DEFAULT NULL,
  `numericvalue` float DEFAULT NULL,
  `attrvalid` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`attrvalid`),
  KEY `sampleid_idx` (`sampleid`),
  KEY `attributeid_idx` (`attributeid`),
  CONSTRAINT `attributeid` FOREIGN KEY (`attributeid`) REFERENCES `attribute` (`idattribute`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sampleid` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sam_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attributevalue`
--

LOCK TABLES `attributevalue` WRITE;
/*!40000 ALTER TABLE `attributevalue` DISABLE KEYS */;
/*INSERT INTO `attributevalue` VALUES (274,1,'lung',NULL,1),(280,1,'brain',NULL,2),(283,2,'tumor',NULL,3),(282,1,'brain',NULL,4),(13,2,'test',NULL,5),(12,1,'test',NULL,6),(295,3,'astg',NULL,7),(295,1,'brain',NULL,8);*/
/*!40000 ALTER TABLE `attributevalue` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `project_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `creator_user_id` int(11) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`project_id`),
  KEY `creator_user_id_idx` (`creator_user_id`),
  CONSTRAINT `creator_user_id` FOREIGN KEY (`creator_user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectsamples`
--

DROP TABLE IF EXISTS `projectsamples`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectsamples` (
  `project_id` int(11) NOT NULL,
  `sample_id` int(11) NOT NULL,
  PRIMARY KEY (`project_id`,`sample_id`),
  KEY `project_id_idx` (`project_id`),
  KEY `sample_id_idx` (`sample_id`),
  CONSTRAINT `project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sample_id` FOREIGN KEY (`sample_id`) REFERENCES `sample` (`sam_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectsamples`
--

LOCK TABLES `projectsamples` WRITE;
/*!40000 ALTER TABLE `projectsamples` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectsamples` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collaboration`
--

DROP TABLE IF EXISTS `collaboration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collaboration` (
  `collaborator_id` int(11) NOT NULL,
  `col_project_id` int(11) NOT NULL,
  `modify_permission` tinyint(4) NOT NULL,
  PRIMARY KEY (`collaborator_id`,`col_project_id`),
  KEY `collaborator_id_idx` (`collaborator_id`),
  KEY `project_id_idx` (`col_project_id`),
  CONSTRAINT `collaborator_id` FOREIGN KEY (`collaborator_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `col_project_id` FOREIGN KEY (`col_project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collaboration`
--

LOCK TABLES `collaboration` WRITE;
/*!40000 ALTER TABLE `collaboration` DISABLE KEYS */;
/*!40000 ALTER TABLE `collaboration` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Table structure for table `lane`
--

DROP TABLE IF EXISTS `lane`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lane` (
  `lane_id` int(11) NOT NULL AUTO_INCREMENT,
  `sam_id` int(11) NOT NULL,
  `run_id` int(11) NOT NULL,
  `lane_name` varchar(100) NOT NULL,
  PRIMARY KEY (`lane_id`),
  KEY `sam_id_idx` (`sam_id`,`run_id`),
  CONSTRAINT `sample_run` FOREIGN KEY (`sam_id`, `run_id`) REFERENCES `samplerun` (`sam_id`, `run_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lane`
--

LOCK TABLES `lane` WRITE;
/*!40000 ALTER TABLE `lane` DISABLE KEYS */;
/*!40000 ALTER TABLE `lane` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multiplerequest`
--

DROP TABLE IF EXISTS `multiplerequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multiplerequest` (
  `request_id` int(11) NOT NULL,
  `sample_id` int(11) NOT NULL,
  PRIMARY KEY (`request_id`,`sample_id`),
  KEY `req_sam_id_idx` (`sample_id`),
  CONSTRAINT `req_sam_id` FOREIGN KEY (`sample_id`) REFERENCES `sample` (`sam_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multiplerequest`
--

LOCK TABLES `multiplerequest` WRITE;
/*!40000 ALTER TABLE `multiplerequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `multiplerequest` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping events for database 'ngslims'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-04-15 16:38:53