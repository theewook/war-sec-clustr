--
-- Table structure for table `LOCK`
--

CREATE TABLE `APP_LOCK` (
  `NAME` varchar(50) NOT NULL,
  `VALUE` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table data for table `LOCK`
--

INSERT INTO `APP_LOCK` (`NAME`, `VALUE`)
VALUES ('CleanSession',0);

--
-- Table structure for table `ROLE`
--

CREATE TABLE `APP_ROLE` (
  `ID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `TOKEN` varchar(100) NOT NULL DEFAULT '',
  `ROLE` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Table structure for table `TOKEN`
--

CREATE TABLE `APP_TOKEN` (
  `TOKEN` varchar(100) NOT NULL DEFAULT '',
  `USERNAME` varchar(50) NOT NULL DEFAULT '',
  `EXTERNAL` int(1) NOT NULL DEFAULT '0',
  `EXPIRYDATE` datetime DEFAULT NULL,
  `LASTREQUESTTIME` datetime DEFAULT NULL,
  PRIMARY KEY (`TOKEN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
