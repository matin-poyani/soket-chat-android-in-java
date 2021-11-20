-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Aug 29, 2018 at 01:49 AM
-- Server version: 5.7.21
-- PHP Version: 5.6.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chat`
--

-- --------------------------------------------------------

--
-- Table structure for table `articles`
--

CREATE TABLE `articles` (
  `id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_persian_ci NOT NULL,
  `body` text COLLATE utf8mb4_persian_ci NOT NULL,
  `media` varchar(255) COLLATE utf8mb4_persian_ci DEFAULT NULL,
  `ts` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `articles`
--

INSERT INTO `articles` (`id`, `channel_id`, `title`, `body`, `media`, `ts`, `deleted`) VALUES
(1, 1, 'Planning Importance', 'Failing to plan is planning to fail.', NULL, 1535388796, 0),
(2, 1, 'Priority', 'Tha\'s the problem with putting others first: You\'ve taught them you come second.', NULL, 1535388844, 0);

-- --------------------------------------------------------

--
-- Table structure for table `channels`
--

CREATE TABLE `channels` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_persian_ci NOT NULL,
  `link` varchar(255) COLLATE utf8mb4_persian_ci NOT NULL,
  `ts` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `channels`
--

INSERT INTO `channels` (`id`, `user_id`, `name`, `link`, `ts`, `deleted`) VALUES
(1, 1, 'Success For Everyone', 'success4everyone', 1533200155, 0),
(2, 1, 'Learn Programming', 'prglearn', 1535388697, 0),
(3, 2, 'nCIS.ir News', 'ncis_ir', 1535388739, 0);

-- --------------------------------------------------------

--
-- Table structure for table `directs`
--

CREATE TABLE `directs` (
  `id` int(11) NOT NULL,
  `user1_id` int(11) NOT NULL,
  `user2_id` int(11) NOT NULL,
  `ts` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `directs`
--

INSERT INTO `directs` (`id`, `user1_id`, `user2_id`, `ts`, `deleted`) VALUES
(1, 1, 2, 1533216945, 0);

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_persian_ci NOT NULL,
  `link` varchar(255) COLLATE utf8mb4_persian_ci NOT NULL,
  `ts` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `groups`
--

INSERT INTO `groups` (`id`, `user_id`, `name`, `link`, `ts`, `deleted`) VALUES
(1, 1, 'Android Development with Java and Android Studio', 'androiddev', 1535388920, 0),
(2, 2, 'Photoshop Tricks and Tips', 'photoshoptt', 1535388937, 0);

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `ts` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `members`
--

INSERT INTO `members` (`user_id`, `group_id`, `ts`, `deleted`) VALUES
(1, 1, 1535388964, 0),
(1, 2, 1535388971, 0),
(2, 2, 1535368735, 0);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `direct_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `body` text COLLATE utf8mb4_persian_ci NOT NULL,
  `media` varchar(255) COLLATE utf8mb4_persian_ci DEFAULT NULL,
  `ts` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `direct_id`, `user_id`, `body`, `media`, `ts`, `deleted`) VALUES
(1, 1, 1, 'Hello', NULL, 1535060474, 0),
(2, 1, 2, 'Hi. How r u?', NULL, 1535060493, 0),
(3, 1, 1, 'Thank you. And what about you?', NULL, 1535060519, 0),
(4, 1, 2, 'Not bad!', NULL, 1535469885, 0),
(5, 1, 1, 'OK.', 'null', 1535469897, 0);

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE `posts` (
  `id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `body` text COLLATE utf8mb4_persian_ci NOT NULL,
  `media` varchar(255) COLLATE utf8mb4_persian_ci DEFAULT NULL,
  `ts` int(11) NOT NULL,
  `pinned` tinyint(1) NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`id`, `group_id`, `user_id`, `parent_id`, `body`, `media`, `ts`, `pinned`, `deleted`) VALUES
(1, 1, 1, NULL, 'Welcome yo your group. In this group you can answer and/or reply questions about Android development.', NULL, 1533216715, 1, 0),
(2, 2, 2, NULL, 'Welcome to this group.', NULL, 1533216870, 0, 0),
(3, 2, 1, 2, 'Thank you.', NULL, 1533216900, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `subscriptions`
--

CREATE TABLE `subscriptions` (
  `user_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `ts` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `subscriptions`
--

INSERT INTO `subscriptions` (`user_id`, `channel_id`, `ts`) VALUES
(1, 1, 1533200155),
(1, 2, 1535389284),
(2, 1, 1535389306),
(2, 2, 1535389313),
(2, 3, 1535389292);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_persian_ci NOT NULL,
  `user` varchar(255) COLLATE utf8mb4_persian_ci NOT NULL,
  `pass` char(40) COLLATE utf8mb4_persian_ci NOT NULL,
  `deleted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `user`, `pass`, `deleted`) VALUES
(1, 'Mohammad Mostafa Shahraki', 'mmshfe', '7c4a8d09ca3762af61e59520943dc26494f8941b', 0),
(2, 'nCIS.ir', 'ncis', '7c4a8d09ca3762af61e59520943dc26494f8941b', 0),
(3, 'Fatemeh', 'fatemeh', 'dd5fef9c1c1da1394d6d34b248c51be2ad740840', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `articles`
--
ALTER TABLE `articles`
  ADD PRIMARY KEY (`id`),
  ADD KEY `channel_id` (`channel_id`);

--
-- Indexes for table `channels`
--
ALTER TABLE `channels`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `directs`
--
ALTER TABLE `directs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user1_id` (`user1_id`),
  ADD KEY `user2_id` (`user2_id`);

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`user_id`,`group_id`),
  ADD KEY `fk_member_group` (`group_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `direct_id` (`direct_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `group_id` (`group_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `parent_id` (`parent_id`);

--
-- Indexes for table `subscriptions`
--
ALTER TABLE `subscriptions`
  ADD PRIMARY KEY (`user_id`,`channel_id`),
  ADD KEY `fk_subscription_channel` (`channel_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `articles`
--
ALTER TABLE `articles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `channels`
--
ALTER TABLE `channels`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `directs`
--
ALTER TABLE `directs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `groups`
--
ALTER TABLE `groups`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `posts`
--
ALTER TABLE `posts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `articles`
--
ALTER TABLE `articles`
  ADD CONSTRAINT `fk_article_channel` FOREIGN KEY (`channel_id`) REFERENCES `channels` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `channels`
--
ALTER TABLE `channels`
  ADD CONSTRAINT `fk_channel_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `directs`
--
ALTER TABLE `directs`
  ADD CONSTRAINT `fk_direct_user1` FOREIGN KEY (`user1_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_direct_user2` FOREIGN KEY (`user2_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `groups`
--
ALTER TABLE `groups`
  ADD CONSTRAINT `fk_group_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `members`
--
ALTER TABLE `members`
  ADD CONSTRAINT `fk_member_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_member_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_message_direct` FOREIGN KEY (`direct_id`) REFERENCES `directs` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_message_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `posts`
--
ALTER TABLE `posts`
  ADD CONSTRAINT `fk_post_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_post_parent` FOREIGN KEY (`parent_id`) REFERENCES `posts` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_post_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `subscriptions`
--
ALTER TABLE `subscriptions`
  ADD CONSTRAINT `fk_subscription_channel` FOREIGN KEY (`channel_id`) REFERENCES `channels` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_subscription_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
