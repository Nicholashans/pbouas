-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 08, 2023 at 06:46 PM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pos_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `kode` int(11) NOT NULL,
  `nama` varchar(255) DEFAULT NULL,
  `harga` float DEFAULT NULL,
  `exp_date` date DEFAULT NULL,
  `item_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`kode`, `nama`, `harga`, `exp_date`, `item_type`) VALUES
(1, 'Chitato', 12000, '2025-06-01', 'makanan'),
(2, 'Oreo', 8000, '2025-06-01', 'makanan'),
(3, 'Coca-Cola', 10000, '2025-06-01', 'makanan'),
(4, 'QTela', 12000, '2025-06-01', 'makanan'),
(5, 'Fanta', 8000, '2025-06-01', 'makanan'),
(6, 'Sprite', 8000, '2025-06-01', 'makanan'),
(7, 'Fruit Tea', 6000, '2025-06-01', 'makanan'),
(8, 'Indomie', 2500, '2025-06-01', 'makanan'),
(9, 'Taro', 7000, '2025-06-01', 'makanan'),
(10, 'Tango', 8000, '2025-06-01', 'makanan'),
(11, 'Pop Mie', 6000, '2025-06-01', 'makanan'),
(12, 'Chocolatos', 3000, '2025-06-01', 'makanan'),
(13, 'Pocky', 9000, '2025-06-01', 'makanan'),
(14, 'Pepsi', 10000, '2025-06-01', 'makanan'),
(15, 'Pocari Sweat', 8000, '2025-06-01', 'makanan'),
(16, 'Big Cola', 8000, '2025-06-01', 'makanan'),
(17, 'Potabee', 12000, '2025-06-01', 'makanan'),
(18, 'Popcorn', 10000, '2025-06-07', 'makanan'),
(19, 'PLN20', 22000, NULL, 'token'),
(20, 'PLN50', 52000, NULL, 'token'),
(21, 'PLN75', 77000, NULL, 'token'),
(22, 'PLN100', 102000, NULL, 'token'),
(23, 'PLN150', 152000, NULL, 'token'),
(24, 'PLN200', 202000, NULL, 'token'),
(25, 'PLN500', 502000, NULL, 'token'),
(26, 'PLN750', 752000, NULL, 'token'),
(27, 'PLN1JT', 1002000, NULL, 'token'),
(28, 'Pulsa 5k', 7000, NULL, 'pulsa'),
(29, 'Pulsa 10k', 12000, NULL, 'pulsa'),
(30, 'Pulsa 15k', 17000, NULL, 'pulsa'),
(31, 'Pulsa 20k', 21000, NULL, 'pulsa'),
(32, 'Pulsa 25k', 26000, NULL, 'pulsa'),
(33, 'Pulsa 30k', 31000, NULL, 'pulsa'),
(34, 'Pulsa 50k', 50000, NULL, 'pulsa'),
(35, 'Pulsa 75k', 75000, NULL, 'pulsa'),
(36, 'Pulsa 100k', 100000, NULL, 'pulsa');

-- --------------------------------------------------------

--
-- Table structure for table `detail_transaksi`
--

CREATE TABLE `detail_transaksi` (
  `id_detail_transaksi` int(11) NOT NULL,
  `id_barang` int(11) DEFAULT NULL,
  `id_transaksi` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int(255) NOT NULL,
  `total_belanja` varchar(255) DEFAULT NULL,
  `total_bayar` varchar(255) DEFAULT NULL,
  `kembalian` varchar(255) DEFAULT NULL,
  `waktu_transaksi` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `user_type`) VALUES
(1, 'admin', '-969161597', 'admin'),
(18, 'Christo', '46792755', 'user');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`kode`),
  ADD UNIQUE KEY `nama` (`nama`);

--
-- Indexes for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  ADD PRIMARY KEY (`id_detail_transaksi`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `id_transaksi` (`id_transaksi`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `barang`
--
ALTER TABLE `barang`
  MODIFY `kode` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  MODIFY `id_detail_transaksi` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_transaksi` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  ADD CONSTRAINT `detail_transaksi_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`kode`),
  ADD CONSTRAINT `detail_transaksi_ibfk_2` FOREIGN KEY (`id_transaksi`) REFERENCES `transaksi` (`id_transaksi`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
