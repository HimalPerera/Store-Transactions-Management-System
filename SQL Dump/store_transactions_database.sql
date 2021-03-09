SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

CREATE DATABASE IF NOT EXISTS `store_transactions_database` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `store_transactions_database`;

CREATE TABLE IF NOT EXISTS `purchases` (
  `transaction_ID` varchar(10) NOT NULL,
  `itemCode` varchar(10) NOT NULL,
  `amount` int(11) NOT NULL,
  `totalPrice` double NOT NULL,
  PRIMARY KEY (`transaction_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `purchases` (`transaction_ID`, `itemCode`, `amount`, `totalPrice`) VALUES
('P-1', 'AA1111', 10, 1000),
('P-2', 'BB2222', 20, 2000),
('P-3', 'CC3333', 3, 3000),
('P-4', 'DD4444', 4, 4000),
('P-5', 'EE5555', 55, 5000);

CREATE TABLE IF NOT EXISTS `sales` (
  `transaction_ID` varchar(10) NOT NULL,
  `itemCode` varchar(10) NOT NULL,
  `amount` int(11) NOT NULL,
  `totalPrice` double NOT NULL,
  `givenDiscount` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`transaction_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `sales` (`transaction_ID`, `itemCode`, `amount`, `totalPrice`, `givenDiscount`) VALUES
('S-1', 'AA1111', 2, 200, 0),
('S-10', 'EE5555', 7, 2800, 700),
('S-11', 'EE5555', 6, 2400, 600),
('S-2', 'BB2222', 4, 800, 0),
('S-3', 'EE5555', 3, 1200, 300),
('S-4', 'EE5555', 4, 1600, 400),
('S-5', 'EE5555', 6, 2400, 600),
('S-6', 'EE5555', 8, 3200, 800),
('S-7', 'BB2222', 2, 400, 0),
('S-8', 'EE5555', 11, 4400, 1100),
('S-9', 'AA1111', 1, 100, 0);

CREATE TABLE IF NOT EXISTS `stock` (
  `itemCode` varchar(10) NOT NULL,
  `itemName` varchar(20) NOT NULL,
  `unitPrice` double NOT NULL,
  `discountRate` double NOT NULL,
  `discountLevel` int(11) NOT NULL,
  `currentStock` int(11) NOT NULL,
  PRIMARY KEY (`itemCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `stock` (`itemCode`, `itemName`, `unitPrice`, `discountRate`, `discountLevel`, `currentStock`) VALUES
('AA1111', 'aaaa', 100, 10, 10, 7),
('BB2222', 'bbbb', 200, 20, 5, 14),
('CC3333', 'cccc', 300, 30, 3, 3),
('DD4444', 'dddd', 400, 25, 10, 4),
('EE5555', 'eeee', 500, 20, 2, 10),
('FF6666', 'ffff', 600, 10, 6, 0),
('GG7777', 'gggg', 700, 20, 7, 0),
('HH8888', 'hhhh', 800, 30, 8, 0),
('II9999', 'iiii', 900, 40, 5, 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
