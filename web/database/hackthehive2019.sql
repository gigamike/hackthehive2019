-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: May 25, 2019 at 09:25 PM
-- Server version: 5.7.23
-- PHP Version: 7.0.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hackthehive2019`
--

-- --------------------------------------------------------

--
-- Table structure for table `call_history`
--

CREATE TABLE `call_history` (
  `id` bigint(20) NOT NULL,
  `call_type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `date_time_called` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `call_role` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `caller_mobile_no` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `callee_mobile_no` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `callee_id` int(11) NOT NULL,
  `caller_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `country`
--

CREATE TABLE `country` (
  `id` int(10) UNSIGNED NOT NULL,
  `country_code` char(2) NOT NULL,
  `country_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `country`
--

INSERT INTO `country` (`id`, `country_code`, `country_name`) VALUES
(1, 'A1', 'Anonymous Proxy'),
(2, 'A2', 'Satellite Provider'),
(3, 'O1', 'Other Country'),
(4, 'AD', 'Andorra'),
(5, 'AE', 'United Arab Emirates'),
(6, 'AF', 'Afghanistan'),
(7, 'AG', 'Antigua and Barbuda'),
(8, 'AI', 'Anguilla'),
(9, 'AL', 'Albania'),
(10, 'AM', 'Armenia'),
(11, 'AO', 'Angola'),
(12, 'AP', 'Asia/Pacific Region'),
(13, 'AQ', 'Antarctica'),
(14, 'AR', 'Argentina'),
(15, 'AS', 'American Samoa'),
(16, 'AT', 'Austria'),
(17, 'AU', 'Australia'),
(18, 'AW', 'Aruba'),
(19, 'AX', 'Aland Islands'),
(20, 'AZ', 'Azerbaijan'),
(21, 'BA', 'Bosnia and Herzegovina'),
(22, 'BB', 'Barbados'),
(23, 'BD', 'Bangladesh'),
(24, 'BE', 'Belgium'),
(25, 'BF', 'Burkina Faso'),
(26, 'BG', 'Bulgaria'),
(27, 'BH', 'Bahrain'),
(28, 'BI', 'Burundi'),
(29, 'BJ', 'Benin'),
(30, 'BL', 'Saint Bartelemey'),
(31, 'BM', 'Bermuda'),
(32, 'BN', 'Brunei Darussalam'),
(33, 'BO', 'Bolivia'),
(34, 'BQ', 'Bonaire, Saint Eustatius and Saba'),
(35, 'BR', 'Brazil'),
(36, 'BS', 'Bahamas'),
(37, 'BT', 'Bhutan'),
(38, 'BV', 'Bouvet Island'),
(39, 'BW', 'Botswana'),
(40, 'BY', 'Belarus'),
(41, 'BZ', 'Belize'),
(42, 'CA', 'Canada'),
(43, 'CC', 'Cocos (Keeling) Islands'),
(44, 'CD', 'Congo, The Democratic Republic of the'),
(45, 'CF', 'Central African Republic'),
(46, 'CG', 'Congo'),
(47, 'CH', 'Switzerland'),
(48, 'CI', 'Cote d\'Ivoire'),
(49, 'CK', 'Cook Islands'),
(50, 'CL', 'Chile'),
(51, 'CM', 'Cameroon'),
(52, 'CN', 'China'),
(53, 'CO', 'Colombia'),
(54, 'CR', 'Costa Rica'),
(55, 'CU', 'Cuba'),
(56, 'CV', 'Cape Verde'),
(57, 'CW', 'Curacao'),
(58, 'CX', 'Christmas Island'),
(59, 'CY', 'Cyprus'),
(60, 'CZ', 'Czech Republic'),
(61, 'DE', 'Germany'),
(62, 'DJ', 'Djibouti'),
(63, 'DK', 'Denmark'),
(64, 'DM', 'Dominica'),
(65, 'DO', 'Dominican Republic'),
(66, 'DZ', 'Algeria'),
(67, 'EC', 'Ecuador'),
(68, 'EE', 'Estonia'),
(69, 'EG', 'Egypt'),
(70, 'EH', 'Western Sahara'),
(71, 'ER', 'Eritrea'),
(72, 'ES', 'Spain'),
(73, 'ET', 'Ethiopia'),
(74, 'EU', 'Europe'),
(75, 'FI', 'Finland'),
(76, 'FJ', 'Fiji'),
(77, 'FK', 'Falkland Islands (Malvinas)'),
(78, 'FM', 'Micronesia, Federated States of'),
(79, 'FO', 'Faroe Islands'),
(80, 'FR', 'France'),
(81, 'GA', 'Gabon'),
(82, 'GB', 'United Kingdom'),
(83, 'GD', 'Grenada'),
(84, 'GE', 'Georgia'),
(85, 'GF', 'French Guiana'),
(86, 'GG', 'Guernsey'),
(87, 'GH', 'Ghana'),
(88, 'GI', 'Gibraltar'),
(89, 'GL', 'Greenland'),
(90, 'GM', 'Gambia'),
(91, 'GN', 'Guinea'),
(92, 'GP', 'Guadeloupe'),
(93, 'GQ', 'Equatorial Guinea'),
(94, 'GR', 'Greece'),
(95, 'GS', 'South Georgia and the South Sandwich Islands'),
(96, 'GT', 'Guatemala'),
(97, 'GU', 'Guam'),
(98, 'GW', 'Guinea-Bissau'),
(99, 'GY', 'Guyana'),
(100, 'HK', 'Hong Kong'),
(101, 'HM', 'Heard Island and McDonald Islands'),
(102, 'HN', 'Honduras'),
(103, 'HR', 'Croatia'),
(104, 'HT', 'Haiti'),
(105, 'HU', 'Hungary'),
(106, 'ID', 'Indonesia'),
(107, 'IE', 'Ireland'),
(108, 'IL', 'Israel'),
(109, 'IM', 'Isle of Man'),
(110, 'IN', 'India'),
(111, 'IO', 'British Indian Ocean Territory'),
(112, 'IQ', 'Iraq'),
(113, 'IR', 'Iran, Islamic Republic of'),
(114, 'IS', 'Iceland'),
(115, 'IT', 'Italy'),
(116, 'JE', 'Jersey'),
(117, 'JM', 'Jamaica'),
(118, 'JO', 'Jordan'),
(119, 'JP', 'Japan'),
(120, 'KE', 'Kenya'),
(121, 'KG', 'Kyrgyzstan'),
(122, 'KH', 'Cambodia'),
(123, 'KI', 'Kiribati'),
(124, 'KM', 'Comoros'),
(125, 'KN', 'Saint Kitts and Nevis'),
(126, 'KP', 'Korea, Democratic People\'s Republic of'),
(127, 'KR', 'Korea, Republic of'),
(128, 'KW', 'Kuwait'),
(129, 'KY', 'Cayman Islands'),
(130, 'KZ', 'Kazakhstan'),
(131, 'LA', 'Lao People\'s Democratic Republic'),
(132, 'LB', 'Lebanon'),
(133, 'LC', 'Saint Lucia'),
(134, 'LI', 'Liechtenstein'),
(135, 'LK', 'Sri Lanka'),
(136, 'LR', 'Liberia'),
(137, 'LS', 'Lesotho'),
(138, 'LT', 'Lithuania'),
(139, 'LU', 'Luxembourg'),
(140, 'LV', 'Latvia'),
(141, 'LY', 'Libyan Arab Jamahiriya'),
(142, 'MA', 'Morocco'),
(143, 'MC', 'Monaco'),
(144, 'MD', 'Moldova, Republic of'),
(145, 'ME', 'Montenegro'),
(146, 'MF', 'Saint Martin'),
(147, 'MG', 'Madagascar'),
(148, 'MH', 'Marshall Islands'),
(149, 'MK', 'Macedonia'),
(150, 'ML', 'Mali'),
(151, 'MM', 'Myanmar'),
(152, 'MN', 'Mongolia'),
(153, 'MO', 'Macao'),
(154, 'MP', 'Northern Mariana Islands'),
(155, 'MQ', 'Martinique'),
(156, 'MR', 'Mauritania'),
(157, 'MS', 'Montserrat'),
(158, 'MT', 'Malta'),
(159, 'MU', 'Mauritius'),
(160, 'MV', 'Maldives'),
(161, 'MW', 'Malawi'),
(162, 'MX', 'Mexico'),
(163, 'MY', 'Malaysia'),
(164, 'MZ', 'Mozambique'),
(165, 'NA', 'Namibia'),
(166, 'NC', 'New Caledonia'),
(167, 'NE', 'Niger'),
(168, 'NF', 'Norfolk Island'),
(169, 'NG', 'Nigeria'),
(170, 'NI', 'Nicaragua'),
(171, 'NL', 'Netherlands'),
(172, 'NO', 'Norway'),
(173, 'NP', 'Nepal'),
(174, 'NR', 'Nauru'),
(175, 'NU', 'Niue'),
(176, 'NZ', 'New Zealand'),
(177, 'OM', 'Oman'),
(178, 'PA', 'Panama'),
(179, 'PE', 'Peru'),
(180, 'PF', 'French Polynesia'),
(181, 'PG', 'Papua New Guinea'),
(182, 'PH', 'Philippines'),
(183, 'PK', 'Pakistan'),
(184, 'PL', 'Poland'),
(185, 'PM', 'Saint Pierre and Miquelon'),
(186, 'PN', 'Pitcairn'),
(187, 'PR', 'Puerto Rico'),
(188, 'PS', 'Palestinian Territory'),
(189, 'PT', 'Portugal'),
(190, 'PW', 'Palau'),
(191, 'PY', 'Paraguay'),
(192, 'QA', 'Qatar'),
(193, 'RE', 'Reunion'),
(194, 'RO', 'Romania'),
(195, 'RS', 'Serbia'),
(196, 'RU', 'Russian Federation'),
(197, 'RW', 'Rwanda'),
(198, 'SA', 'Saudi Arabia'),
(199, 'SB', 'Solomon Islands'),
(200, 'SC', 'Seychelles'),
(201, 'SD', 'Sudan'),
(202, 'SE', 'Sweden'),
(203, 'SG', 'Singapore'),
(204, 'SH', 'Saint Helena'),
(205, 'SI', 'Slovenia'),
(206, 'SJ', 'Svalbard and Jan Mayen'),
(207, 'SK', 'Slovakia'),
(208, 'SL', 'Sierra Leone'),
(209, 'SM', 'San Marino'),
(210, 'SN', 'Senegal'),
(211, 'SO', 'Somalia'),
(212, 'SR', 'Suriname'),
(213, 'SS', 'South Sudan'),
(214, 'ST', 'Sao Tome and Principe'),
(215, 'SV', 'El Salvador'),
(216, 'SX', 'Sint Maarten'),
(217, 'SY', 'Syrian Arab Republic'),
(218, 'SZ', 'Swaziland'),
(219, 'TC', 'Turks and Caicos Islands'),
(220, 'TD', 'Chad'),
(221, 'TF', 'French Southern Territories'),
(222, 'TG', 'Togo'),
(223, 'TH', 'Thailand'),
(224, 'TJ', 'Tajikistan'),
(225, 'TK', 'Tokelau'),
(226, 'TL', 'Timor-Leste'),
(227, 'TM', 'Turkmenistan'),
(228, 'TN', 'Tunisia'),
(229, 'TO', 'Tonga'),
(230, 'TR', 'Turkey'),
(231, 'TT', 'Trinidad and Tobago'),
(232, 'TV', 'Tuvalu'),
(233, 'TW', 'Taiwan'),
(234, 'TZ', 'Tanzania, United Republic of'),
(235, 'UA', 'Ukraine'),
(236, 'UG', 'Uganda'),
(237, 'UM', 'United States Minor Outlying Islands'),
(238, 'US', 'United States'),
(239, 'UY', 'Uruguay'),
(240, 'UZ', 'Uzbekistan'),
(241, 'VA', 'Holy See (Vatican City State)'),
(242, 'VC', 'Saint Vincent and the Grenadines'),
(243, 'VE', 'Venezuela'),
(244, 'VG', 'Virgin Islands, British'),
(245, 'VI', 'Virgin Islands, U.S.'),
(246, 'VN', 'Vietnam'),
(247, 'VU', 'Vanuatu'),
(248, 'WF', 'Wallis and Futuna'),
(249, 'WS', 'Samoa'),
(250, 'YE', 'Yemen'),
(251, 'YT', 'Mayotte'),
(252, 'ZA', 'South Africa'),
(253, 'ZM', 'Zambia'),
(254, 'ZW', 'Zimbabwe');

-- --------------------------------------------------------

--
-- Table structure for table `language`
--

CREATE TABLE `language` (
  `id` int(10) UNSIGNED NOT NULL,
  `language` varchar(255) NOT NULL,
  `code` char(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `language`
--

INSERT INTO `language` (`id`, `language`, `code`) VALUES
(1, 'Azerbaijan', 'az'),
(2, 'Albanian', 'sq'),
(3, 'Amharic', 'am'),
(4, 'English', 'en'),
(5, 'Arabic', 'ar'),
(6, 'Armenian', 'hy'),
(7, 'Afrikaans', 'af'),
(8, 'Basque', 'eu'),
(9, 'Bashkir', 'ba'),
(10, 'Belarusian', 'be'),
(11, 'Bengali', 'bn'),
(12, 'Burmese', 'my'),
(13, 'Bulgarian', 'bg'),
(14, 'Bosnian', 'bs'),
(15, 'Welsh', 'cy'),
(16, 'Hungarian', 'hu'),
(17, 'Vietnamese', 'vi'),
(18, 'Haitian (Creole)', 'ht'),
(19, 'Galician', 'gl'),
(20, 'Dutch', 'nl'),
(21, 'Hill Mari', 'mr'),
(22, 'Greek', 'el'),
(23, 'Georgian', 'ka'),
(24, 'Gujarati', 'gu'),
(25, 'Danish', 'da'),
(26, 'Hebrew', 'he'),
(27, 'Yiddish', 'yi'),
(28, 'Indonesian', 'id'),
(29, 'Irish', 'ga'),
(30, 'Italian', 'it'),
(31, 'Icelandic', 'is'),
(32, 'Spanish', 'es'),
(33, 'Kazakh', 'kk'),
(34, 'Kannada', 'kn'),
(35, 'Catalan', 'ca'),
(36, 'Kyrgyz', 'ky'),
(37, 'Chinese', 'zh'),
(38, 'Korean', 'ko'),
(39, 'Xhosa', 'xh'),
(40, 'Khmer', 'km'),
(41, 'Laotian', 'lo'),
(42, 'Latin', 'la'),
(43, 'Latvian', 'lv'),
(44, 'Lithuanian', 'lt'),
(45, 'Luxembourgish', 'lb'),
(46, 'Malagasy', 'mg'),
(47, 'Malay', 'ms'),
(48, 'Malayalam', 'ml'),
(49, 'Maltese', 'mt'),
(50, 'Macedonian', 'mk'),
(51, 'Maori', 'mi'),
(52, 'Marathi', 'mr'),
(53, 'Mari', 'mh'),
(54, 'Mongolian', 'mn'),
(55, 'German', 'de'),
(56, 'Nepali', 'ne'),
(57, 'Norwegian', 'no'),
(58, 'Punjabi', 'pa'),
(59, 'Papiamento', 'pa'),
(60, 'Persian', 'fa'),
(61, 'Polish', 'pl'),
(62, 'Portuguese', 'pt'),
(63, 'Romanian', 'ro'),
(64, 'Russian', 'ru'),
(65, 'Cebuano', 'ce'),
(66, 'Serbian', 'sr'),
(67, 'Sinhala', 'si'),
(68, 'Slovakian', 'sk'),
(69, 'Slovenian', 'sl'),
(70, 'Swahili', 'sw'),
(71, 'Sundanese', 'su'),
(72, 'Tajik', 'tg'),
(73, 'Thai', 'th'),
(74, 'Tagalog', 'tl'),
(75, 'Tamil', 'ta'),
(76, 'Tatar', 'tt'),
(77, 'Telugu', 'te'),
(78, 'Turkish', 'tr'),
(79, 'Udmurt', 'ud'),
(80, 'Uzbek', 'uz'),
(81, 'Ukrainian', 'uk'),
(82, 'Urdu', 'ur'),
(83, 'Finnish', 'fi'),
(84, 'French', 'fr'),
(85, 'Hindi', 'hi'),
(86, 'Croatian', 'hr'),
(87, 'Czech', 'cs'),
(88, 'Swedish', 'sv'),
(89, 'Scottish', 'gd'),
(90, 'Estonian', 'et'),
(91, 'Esperanto', 'eo'),
(92, 'Javanese', 'jv'),
(93, 'Japanese', 'ja');

-- --------------------------------------------------------

--
-- Table structure for table `organization`
--

CREATE TABLE `organization` (
  `id` int(10) UNSIGNED NOT NULL,
  `organization` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `organization`
--

INSERT INTO `organization` (`id`, `organization`) VALUES
(9, 'AKO OFW Inc. - Advocates & Keepers Organization of OFW'),
(6, 'Atikha Overseas Workers and Communities Initiative, Inc.'),
(8, 'Buhay OFW Foundation Incorporated'),
(1, 'Catholic Ministry'),
(3, 'Global Filipino Movement Foundation Inc.'),
(7, 'Iglesia ni Cristo'),
(4, 'KAKAMMPI'),
(2, 'N.G.O'),
(10, 'National Commission on Muslim Filipino'),
(5, 'OWWA Volunteers');

-- --------------------------------------------------------

--
-- Table structure for table `payment_transaction`
--

CREATE TABLE `payment_transaction` (
  `transaction_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `transaction_code` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `credits_buyed` int(11) NOT NULL,
  `date_created` datetime NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `payment_transaction`
--

INSERT INTO `payment_transaction` (`transaction_id`, `user_id`, `transaction_code`, `status`, `credits_buyed`, `date_created`) VALUES
(1, 1, 'MjAxOS0wNS0yMFQyMjo0ODozOC4wNDcxMTAw', 'PENDING', 100, '2019-05-20 14:48:38'),
(2, 1, 'MjAxOS0wNS0yMFQyMjo1NToyNC42NzcxMTAw', 'PENDING', 100, '2019-05-20 14:55:25'),
(3, 1, 'MjAxOS0wNS0yMFQyMjo1ODoyOS4wMDkxMTAw', 'PENDING', 100, '2019-05-20 14:58:29'),
(4, 2, 'MjAxOS0wNS0yMFQyMzoxMzowMC4zNTIyNTAw', 'PENDING', 500, '2019-05-20 15:13:00'),
(5, 2, 'MjAxOS0wNS0yMFQyMzoxMzoxMy41MDkyNTAw', 'PENDING', 500, '2019-05-20 15:13:14'),
(6, 2, 'MjAxOS0wNS0yMFQyMzoxODoxMC40MjMyNTAw', 'PENDING', 500, '2019-05-20 15:18:10'),
(7, 2, 'MjAxOS0wNS0yMFQyMzoyNzozMS40ODcyNTAw', 'PAID', 500, '2019-05-20 15:27:31'),
(8, 2, 'MjAxOS0wNS0yMFQyMzoyNzozOC44MzUyNTAw', 'PENDING', 500, '2019-05-20 15:27:39'),
(9, 2, 'MjAxOS0wNS0yMFQyMzozNToxNi43NTIyNTAw', 'PENDING', 500, '2019-05-20 15:35:17'),
(10, 2, 'MjAxOS0wNS0yMFQyMzo0MTowNS4xNTUyNTAw', 'PENDING', 500, '2019-05-20 15:41:05'),
(11, 2, 'MjAxOS0wNS0yMVQwMDowNDozOC40NDcyNTAw', 'PENDING', 500, '2019-05-20 16:04:38'),
(12, 2, 'MjAxOS0wNS0yMFQxNjowNDoyOC44NzIyNTAw', 'PAID', 500, '2019-05-20 16:04:29'),
(13, 1, 'MjAxOS0wNS0yMFQxNjoxMzoyNi40NTAxMTAw', 'PENDING', 100, '2019-05-20 16:13:26'),
(14, 1, 'MjAxOS0wNS0yMFQxNjoxNDo0MC4yMDMxMTAw', 'PENDING', 100, '2019-05-20 16:14:40'),
(15, 1, 'MjAxOS0wNS0yMFQxNjoxNjo1Ny4yNDQxMTAw', 'PENDING', 100, '2019-05-20 16:16:57'),
(16, 1, 'MjAxOS0wNS0yMFQxNjoyODoxNy42NDUxMTAw', 'PENDING', 100, '2019-05-20 16:28:18'),
(17, 1, 'MjAxOS0wNS0yMFQxNjoyOTowNy4zMjUxMTAw', 'PENDING', 100, '2019-05-20 16:29:07'),
(18, 1, 'MjAxOS0wNS0yMFQxNjoyOToxNi4yODgxMTAw', 'PENDING', 100, '2019-05-20 16:29:16'),
(19, 1, 'MjAxOS0wNS0yMFQxNjoyOTo1Ny40MDMxMTAw', 'PENDING', 100, '2019-05-20 16:29:57'),
(20, 1, 'MjAxOS0wNS0yMFQxNjozMDozOS40OTUxMTAw', 'PENDING', 100, '2019-05-20 16:30:39'),
(21, 1, 'MjAxOS0wNS0yMFQxNjozMToxMC45MzgxMTAw', 'PENDING', 100, '2019-05-20 16:31:11'),
(22, 1, 'MjAxOS0wNS0yMFQxNjozMTozOC4xMjkxMTAw', 'PENDING', 100, '2019-05-20 16:31:38'),
(23, 1, 'MjAxOS0wNS0yMFQxNjozMjo0My4zMDAxMTAw', 'PENDING', 100, '2019-05-20 16:32:43'),
(24, 1, 'MjAxOS0wNS0yMFQxNjozMzo1Ny40MDcxMTAw', 'PENDING', 100, '2019-05-20 16:33:57'),
(25, 1, 'MjAxOS0wNS0yMFQxNjozNDo1NC42ODMxMTAw', 'PENDING', 100, '2019-05-20 16:34:55'),
(26, 1, 'MjAxOS0wNS0yMFQxNjozNjoxMC42ODgxMTAw', 'PENDING', 100, '2019-05-20 16:36:11'),
(27, 1, 'MjAxOS0wNS0yMFQxNjozNjo1OC44NDYxMTAw', 'PENDING', 100, '2019-05-20 16:36:59'),
(28, 1, 'MjAxOS0wNS0yMFQxNjozNzozNC45MjMxMTAw', 'PENDING', 100, '2019-05-20 16:37:35'),
(29, 1, 'MjAxOS0wNS0yMFQxNjozOToxNC45MjUxMTAw', 'PENDING', 100, '2019-05-20 16:39:15'),
(30, 1, 'MjAxOS0wNS0yMFQxNjozOTo0Ny44ODQxMTAw', 'PENDING', 100, '2019-05-20 16:39:48'),
(31, 1, 'MjAxOS0wNS0yMFQxNjo0MDoxMy44MDcxMTAw', 'PENDING', 100, '2019-05-20 16:40:14'),
(32, 1, 'MjAxOS0wNS0yMFQxNjo0MDozNC42ODYxMTAw', 'PENDING', 100, '2019-05-20 16:40:35'),
(33, 1, 'MjAxOS0wNS0yMFQxNjo0MToyMS4wODUxMTAw', 'PENDING', 100, '2019-05-20 16:41:21'),
(34, 1, 'MjAxOS0wNS0yMFQxNjo0MTozMS42NDMxMTAw', 'PENDING', 100, '2019-05-20 16:41:32'),
(35, 1, 'MjAxOS0wNS0yMFQxNjo0NDo0Ny4wODMxMTAw', 'PENDING', 100, '2019-05-20 16:44:47'),
(36, 1, 'MjAxOS0wNS0yMFQxNjo0NTozMC4zNjIxMTAw', 'PENDING', 100, '2019-05-20 16:45:30'),
(37, 1, 'MjAxOS0wNS0yMFQxNjo0NTo1NC43NjMxMTAw', 'PENDING', 100, '2019-05-20 16:45:55'),
(38, 1, 'MjAxOS0wNS0yMFQxNjo0NjoyMS45NzExMTAw', 'PENDING', 100, '2019-05-20 16:46:22'),
(39, 1, 'MjAxOS0wNS0yMFQxNjo0Njo0OS4xNjQxMTAw', 'PENDING', 100, '2019-05-20 16:46:49'),
(40, 1, 'MjAxOS0wNS0yMFQxNjo0NzowOC41MjMxMTAw', 'PENDING', 100, '2019-05-20 16:47:09'),
(41, 1, 'MjAxOS0wNS0yMFQxNjo0NzozNi41MjQxMTAw', 'PENDING', 100, '2019-05-20 16:47:37'),
(42, 1, 'MjAxOS0wNS0yMFQxNjo0ODoyOS4xNjkxMTAw', 'PENDING', 100, '2019-05-20 16:48:29'),
(43, 1, 'MjAxOS0wNS0yMFQxNjo1MDo1OC40NDQxMTAw', 'PENDING', 100, '2019-05-20 16:50:58'),
(44, 1, 'MjAxOS0wNS0yMFQxNjo1MTowNS43MjYxMTAw', 'PENDING', 100, '2019-05-20 16:51:06'),
(45, 1, 'MjAxOS0wNS0yMFQxNjo1MToxMy41NzUxMTAw', 'PENDING', 100, '2019-05-20 16:51:14'),
(46, 1, 'MjAxOS0wNS0yMFQxNjo1MTo0Ny44MDQxMTAw', 'PENDING', 100, '2019-05-20 16:51:48'),
(47, 1, 'MjAxOS0wNS0yMFQxNjo1MzoxOS4xNjYxNTAw', 'PENDING', 500, '2019-05-20 16:53:19'),
(48, 1, 'MjAxOS0wNS0yMFQxNjo1MzoyNS40ODIxMTAwMA==', 'PENDING', 1000, '2019-05-20 16:53:25'),
(49, 1, 'MjAxOS0wNS0yMFQxNjo1Mzo1Ni44NDQxMTAwMA==', 'PENDING', 1000, '2019-05-20 16:53:57'),
(50, 1, 'MjAxOS0wNS0yMFQxNjo1NDowMC41MjQxNTAw', 'PENDING', 500, '2019-05-20 16:54:01'),
(51, 1, 'MjAxOS0wNS0yMFQxNjo1NDoyNy45NjYxMTAwMA==', 'PENDING', 1000, '2019-05-20 16:54:28'),
(52, 1, 'MjAxOS0wNS0yMFQxNjo1NDozOS4yNDQxMTAwMA==', 'PENDING', 1000, '2019-05-20 16:54:39'),
(53, 1, 'MjAxOS0wNS0yMFQxNjo1NToxMC42ODQxNTAw', 'PENDING', 500, '2019-05-20 16:55:11'),
(54, 1, 'MjAxOS0wNS0yMFQxNjo1NTozMC42ODUxMTAwMA==', 'PENDING', 1000, '2019-05-20 16:55:31'),
(55, 1, 'MjAxOS0wNS0yMFQxNjo1NzozNS4wODMxMTAw', 'PENDING', 100, '2019-05-20 16:57:35'),
(56, 1, 'MjAxOS0wNS0yMFQxNjo1NzozOS43MjQxMTAwMA==', 'PENDING', 1000, '2019-05-20 16:57:40'),
(57, 1, 'MjAxOS0wNS0yMFQxNjo1Nzo0My45NjMxNTAw', 'PENDING', 500, '2019-05-20 16:57:44'),
(58, 1, 'MjAxOS0wNS0yMFQxNjo1ODozNi44NDYxNTAw', 'PENDING', 500, '2019-05-20 16:58:37'),
(59, 1, 'MjAxOS0wNS0yMFQxNzowMToyMi4yMDMxMTAw', 'PENDING', 100, '2019-05-20 17:01:22'),
(60, 1, 'MjAxOS0wNS0yMFQxNzowNDowNC4xMjIxMTAwMA==', 'PENDING', 1000, '2019-05-20 17:04:04'),
(61, 1, 'MjAxOS0wNS0yMFQxNzowNjowNi4zNjUxNTAw', 'PENDING', 500, '2019-05-20 17:06:06'),
(62, 1, 'MjAxOS0wNS0yMFQxNzowNjoxMC42ODMxMTAw', 'PENDING', 100, '2019-05-20 17:06:11'),
(63, 1, 'MjAxOS0wNS0yMFQxNzowNjo1OC45MzQxMTAw', 'PENDING', 100, '2019-05-20 17:06:59'),
(64, 1, 'MjAxOS0wNS0yMFQxNzowODowNi42MDMxMTAw', 'PENDING', 100, '2019-05-20 17:08:07'),
(65, 1, 'MjAxOS0wNS0yMFQxNzowODoxMi45MjMxMTAwMA==', 'PENDING', 1000, '2019-05-20 17:08:13'),
(66, 1, 'MjAxOS0wNS0yMFQxNzowODo0NS44MDMxMTAw', 'PENDING', 100, '2019-05-20 17:08:46'),
(67, 1, 'MjAxOS0wNS0yMFQxNzowOToxNC44NDIxMTAw', 'PENDING', 100, '2019-05-20 17:09:15'),
(68, 1, 'MjAxOS0wNS0yMFQxNzowOToyMS42NDIxMTAw', 'PENDING', 100, '2019-05-20 17:09:22'),
(69, 1, 'MjAxOS0wNS0yMFQxNzoxOTo0My4wODkxMTAw', 'PENDING', 100, '2019-05-20 17:19:43'),
(70, 1, 'MjAxOS0wNS0yMFQxNzozMDowOS40MDIxMTAw', 'PENDING', 100, '2019-05-20 17:30:09'),
(71, 1, 'MjAxOS0wNS0yMFQxODowMDo0My4zMjMxMTAwMA==', 'PENDING', 1000, '2019-05-20 18:00:43'),
(72, 1, 'MjAxOS0wNS0yMFQxODoyMzowNy42MTIxNTAw', 'PAID', 500, '2019-05-20 18:23:08'),
(73, 1, 'MjAxOS0wNS0yMFQxODozNDowNS41MTIxMTAw', 'PAID', 100, '2019-05-20 18:34:06'),
(74, 1, 'MjAxOS0wNS0yMFQxODozODowOC4yMzIxMTAwMA==', 'PAID', 1000, '2019-05-20 18:38:08'),
(75, 1, 'MjAxOS0wNS0yMFQxODo0ODo1OS4wMzIxMTAw', 'PENDING', 100, '2019-05-20 18:48:59'),
(76, 1, 'MjAxOS0wNS0yMVQwNjozMTowNS4xMDgxMTAw', 'PENDING', 100, '2019-05-21 06:31:05'),
(77, 1, 'MjAxOS0wNS0yMVQwNjozMTowNS4zMDcxNTAw', 'PENDING', 500, '2019-05-21 06:31:05'),
(78, 1, 'MjAxOS0wNS0yMVQwNjozMToxMC40MjAxMTAwMA==', 'PENDING', 1000, '2019-05-21 06:31:10'),
(79, 1, 'MjAxOS0wNS0yMVQxMjoxOTozNi4yMzIxMTAw', 'PENDING', 100, '2019-05-21 12:19:36'),
(80, 1, 'MjAxOS0wNS0yMVQxMjoxOTozNi41NzAxNTAw', 'PAID', 500, '2019-05-21 12:19:37'),
(81, 1, 'MjAxOS0wNS0yMVQxNjozMjoxMC4zNTAxMTAw', 'PENDING', 100, '2019-05-21 16:32:10'),
(82, 1, 'MjAxOS0wNS0yMVQxNjozMjoxMC44NjYxMTAw', 'PENDING', 100, '2019-05-21 16:32:11'),
(83, 10, 'MjAxOS0wNS0yMVQxODo1Njo0MC43NjUxMDIwMA==', 'PENDING', 200, '2019-05-21 18:56:41'),
(84, 10, 'MjAxOS0wNS0yMVQxODo1NzozMi4zMTIxMDIwMA==', 'PENDING', 200, '2019-05-21 18:57:32'),
(85, 10, 'MjAxOS0wNS0yMVQxOTowNjo1NS44NDUxMDIwMA==', 'PENDING', 200, '2019-05-21 19:06:56'),
(86, 10, 'MjAxOS0wNS0yMVQxOTowODo0NC41MTcxMDIwMA==', 'PENDING', 200, '2019-05-21 19:08:45'),
(87, 10, 'MjAxOS0wNS0yMVQxOTowOToyNy44OTAxMDIwMA==', 'PENDING', 200, '2019-05-21 19:09:28'),
(88, 0, 'MjAxOS0wNS0yMVQxOToxMDo0NC45MTEwMA==', 'PENDING', 0, '2019-05-21 19:10:45'),
(89, 0, 'MjAxOS0wNS0yMVQxOToxMDo0NS41NzEwMA==', 'PENDING', 0, '2019-05-21 19:10:46'),
(90, 10, 'MjAxOS0wNS0yMVQxOToxMToxMy4wNTkxMDIwMA==', 'PENDING', 200, '2019-05-21 19:11:13'),
(91, 10, 'MjAxOS0wNS0yMVQxOToxMjo0NS45NTAxMDIwMA==', 'PENDING', 200, '2019-05-21 19:12:46'),
(92, 10, 'MjAxOS0wNS0yMVQxOToxMzozNC40OTcxMDIwMA==', 'PENDING', 200, '2019-05-21 19:13:34'),
(93, 10, 'MjAxOS0wNS0yMVQxOToxNDo0MC42MjkxMDIwMA==', 'PENDING', 200, '2019-05-21 19:14:41'),
(94, 10, 'MjAxOS0wNS0yMVQxOToxNTo0MS40NzgxMDIwMA==', 'PENDING', 200, '2019-05-21 19:15:41'),
(95, 10, 'MjAxOS0wNS0yMVQxOToxNjoyMC40NjgxMDIwMA==', 'PENDING', 200, '2019-05-21 19:16:20'),
(96, 10, 'MjAxOS0wNS0yMVQxOToxNzozNC40MDkxMDIwMA==', 'PENDING', 200, '2019-05-21 19:17:34'),
(97, 1, 'Kz6s4nAdmYAZcjYkDgBk27', 'PENDING', 200, '2019-05-21 19:28:43'),
(98, 10, 'LuHnHNNiVmc7K4SQ5rHLp5', 'PENDING', 500, '2019-05-21 19:31:10'),
(99, 10, '8PYSTxTaR4zPZNV9eZb8mx', 'PAID', 200, '2019-05-21 19:35:19'),
(100, 10, 'MjAxOS0wNS0yMVQxOTo0MDoxMi45NzMxMDEwMA==', 'PENDING', 100, '2019-05-21 19:40:13'),
(101, 10, '4dVp4th1rHTjaBHWLRxHjL', 'PENDING', 500, '2019-05-21 19:40:20'),
(102, 1, 'MjAxOS0wNS0yMVQxOTo0ODoxNS41OTcxMTAw', 'PENDING', 100, '2019-05-21 19:48:16'),
(103, 1, 'MjAxOS0wNS0yMVQxOTo0ODo0My42ODgxNTAw', 'PAID', 500, '2019-05-21 19:48:44'),
(104, 10, 'RgTJNUByFpQoKaJ1oMknAn', 'PENDING', 200, '2019-05-22 03:19:05'),
(105, 10, 'G7WQR4YB3p5EMtiWWCavKH', 'PENDING', 200, '2019-05-22 03:25:19'),
(106, 10, 'MjAxOS0wNS0yMlQwMzoyNjo0NC40MjAxMDEwMA==', 'PENDING', 100, '2019-05-22 03:26:44'),
(107, 10, 'Xmw9WT4vCaXJsEDVFQwJjw', 'PENDING', 200, '2019-05-22 17:15:43'),
(108, 10, 'LgsP84XxpdwqWZ9ggvE7y9', 'PENDING', 200, '2019-05-22 17:15:47'),
(109, 10, 'MjAxOS0wNS0yMlQxNzoxNjowNy42MTIxMDEwMA==', 'PENDING', 100, '2019-05-22 17:16:08'),
(110, 10, 'CyrzfUCa946Ptub96uzBVG', 'PENDING', 200, '2019-05-24 17:15:37'),
(111, 10, 'CZMtDY2qWqerr2sMokd8jo', 'PAID', 200, '2019-05-24 17:16:38'),
(112, 10, 'MjAxOS0wNS0yNFQxNzoyMzoyOC43MDAxMDEwMA==', 'PENDING', 100, '2019-05-24 17:23:29'),
(113, 10, '4SGbU7f2MfsTKaty1Hv4Br', 'PENDING', 1000, '2019-05-24 17:23:46'),
(114, 10, 'Wtc1aZZ7RFKHED1UoKv21o', 'PENDING', 200, '2019-05-24 17:24:03'),
(115, 10, 'NBsCsTaPiNajtfwc78RNvH', 'PENDING', 500, '2019-05-24 17:24:17'),
(116, 10, 'MjAxOS0wNS0yNVQwMTozMTowMi42MTIxMDUwMA==', 'PAID', 500, '2019-05-25 01:31:03'),
(117, 10, 'U6dZ1Sgis7k1H9tpmeDAD7', 'PENDING', 200, '2019-05-25 01:31:52'),
(118, 10, 'JixRBxV91wwCuTPJ6hLpG', 'PENDING', 200, '2019-05-25 01:32:35'),
(119, 10, '5QU9NaC4NW4gTQsKdJXvd8', 'PENDING', 200, '2019-05-25 01:33:12'),
(120, 10, '3G6wXEfYUwXqvNEFKaokBQ', 'PENDING', 200, '2019-05-25 01:33:31'),
(121, 10, 'V1PVgQJyHetHME8DLtLiRs', 'PENDING', 200, '2019-05-25 01:34:12'),
(122, 10, 'S31qV2RZbXKXzWTzYPSVv7', 'PENDING', 1000, '2019-05-25 01:34:56'),
(123, 1, '9KCR3nCVcdXvdAWghfVRGF', 'PENDING', 200, '2019-05-25 10:27:17'),
(124, 1, 'J6UrFCJm2KaYjchKTmfeHE', 'PENDING', 200, '2019-05-25 10:28:13'),
(125, 1, 'JwqMDirbkYTv4GBfcVVWDB', 'PENDING', 200, '2019-05-25 10:32:18'),
(126, 1, 'Q9Y68U1e5VE8zxY4yVA7vE', 'PENDING', 200, '2019-05-25 10:32:22'),
(127, 1, 'B2voTQkBde8xJ3jgpMHSwj', 'PAID', 200, '2019-05-25 10:32:51'),
(128, 10, 'FVXbAd2gv69ECxcG8td87T', 'PENDING', 200, '2019-05-25 12:00:14');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `role` enum('admin','volunteer','ofw') CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `active` enum('N','Y') NOT NULL,
  `country_id` int(10) UNSIGNED NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `mobile_no` varchar(255) NOT NULL,
  `profile_pic` varchar(255) DEFAULT NULL,
  `organization_id` int(10) UNSIGNED NOT NULL,
  `last_login_datetime` datetime NOT NULL,
  `created_datetime` datetime NOT NULL,
  `created_user_id` bigint(10) UNSIGNED NOT NULL,
  `modified_datetime` datetime DEFAULT NULL,
  `modified_user_id` bigint(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `role`, `email`, `password`, `first_name`, `middle_name`, `last_name`, `salt`, `active`, `country_id`, `city`, `mobile_no`, `profile_pic`, `organization_id`, `last_login_datetime`, `created_datetime`, `created_user_id`, `modified_datetime`, `modified_user_id`) VALUES
(1, 'volunteer', 'volunteer1@gigamike.net', '2bfe978ec2fa37efd1cd7d2d5f85928f', 'Carlo', 'Cruz', 'Sampan', '1a~2lB7U7E\'oi~itM^8\"IL/Wb`T\"g/\\wp\\*]}A35f:&P9nFeN^', 'Y', 182, 'Pasay', '639086087306', '10842281_10153221698703454_2784909659624885851_o.jpg', 3, '2019-05-25 20:38:48', '2019-04-30 08:12:21', 0, NULL, NULL),
(2, 'volunteer', 'volunteer2@gigamike.net', '824a8408dd4181e2581af92987045db0', 'Caloy', 'Magsakay', 'Diño', 'UJ>+aoA,!;uWIy/oOP;h>h(^,9HO:K+nuIxW::c:UYq}T\"n%R*', 'Y', 182, 'Pasay', '649086087306', '43641649_10155540003986783_3342528950600466432_o.jpg', 1, '2019-05-25 20:41:06', '2019-04-30 08:23:37', 0, NULL, NULL),
(3, 'volunteer', 'volunteer3@gigamike.net', '5a138e05fc481f9d7ba462cf292ec851', 'Rev. Fr. Vicente', 'Cruz', 'Gabut', 'f{zQ/?yl&{-fY|u\"}`k9r^aBHzx)`p+Gm\'w{FqiLmu3Hs)Jqh6', 'Y', 182, 'Pasay', '649086087306', '59093883_10213923153337925_1345913390446411776_o.jpg', 1, '2019-04-30 08:28:30', '2019-04-30 08:28:30', 0, NULL, NULL),
(4, 'volunteer', 'volunteer4@gigamike.net', '162a5ed6ec457510a0d96dd5d1f0b4f9', 'Anthony', 'Cruz', 'Reyes', '&}\'^%R\'Vxe(fAk2Vs.O\"9>H5kK\'*vPz{O#[TTb,NI35j~FAsTp', 'Y', 182, 'Pasay', '639086087306', 'photo-750x450.jpg', 7, '2019-04-30 09:10:57', '2019-04-30 09:10:57', 0, NULL, NULL),
(5, 'volunteer', 'volunteer5@gigamike.net', 'ff281114c9a7eb3ab6a0a84c71c1a35b', 'Najeeb', 'Singkee', 'Singkee', '&WByF2x%F1.xn0f\'FV>A22r`#6n>%)n+`2%\'D},j/:c}JJ&o\"C', 'Y', 182, 'Pasay', '649086087306', 'photo-750x450.jpg', 10, '2019-04-30 09:21:48', '2019-04-30 09:21:47', 0, NULL, NULL),
(6, 'volunteer', 'volunteer6@gigamike.net', 'd80696dc7ce40cc101809c97c75eb3f0', 'Neil ', 'Cruz', 'Rojas', 'SPNEAi:&p~Z>\"PaEIV-r\\A%ahY./8a#j3P1T;JY,I5IKd,o/b|', 'Y', 182, 'Pasay', '649086087306', 'photo-750x450.jpg', 4, '2019-04-30 09:25:16', '2019-04-30 09:25:16', 0, NULL, NULL),
(7, 'volunteer', 'volunteer7@gigamike.net', '1a057f7651f8ca9d23767f81a32e1b07', 'Edward', 'Buenaventura', 'Cawicaan', 'qj8<=;59&y~sMWU=`K2axtL=o\"b=qnOdYf!u\"6/(1/z]fPzGz-', 'Y', 182, 'Pasay', '649086087306', 'photo-750x450.jpg', 2, '2019-04-30 09:29:17', '2019-04-30 09:29:17', 0, NULL, NULL),
(8, 'volunteer', 'volunteer8@gigamike.net', '5f6c5a0de0ae45700d5532a10ccb6d93', 'OWWA', 'Volunteers', 'Volunteers', 'ybGb$N$tF2+\"hc4GnJ,JA)fCAg\"|sf/mIVPM%TBJeMLO2`v\"+#', 'Y', 182, 'Pasay', '649086087306', 'photo-750x450.jpg', 5, '2019-04-30 09:32:36', '2019-04-30 09:32:36', 0, NULL, NULL),
(9, 'volunteer', 'volunteer9@gigamike.net', '46be4f6bb9f0d82de34f4d36c5a4c907', 'Japan', 'Volunteers', 'Volunteers', '4T%sW;ct@kdJMuNS:M_%[xgZC3_45#EIWJ=0d\"%%lhO;^}nxKN', 'Y', 119, 'Tokyo', '649086087306', 'photo-750x450.jpg', 5, '2019-04-30 09:36:26', '2019-04-30 09:36:26', 0, NULL, NULL),
(13, 'volunteer', 'volunteer12@gigamike.net', 'c3c7ebd0d1767d21f2102379bf6540c6', 'Zeev', 'Espedillon', 'Galon', '[JnMi^cnK`w\'6D/c{\'9+o8D_GLt5L+J(U:T@w90Cy(I0LXsH_-', 'Y', 182, 'Pasay', '649086087306', 'photo-orig.jpg', 0, '2019-05-11 14:05:26', '2019-05-11 14:05:26', 0, NULL, NULL),
(17, 'ofw', 'ofw1@gigamike.net', 'f8a0d3867e9801a34ac2611401c057c0', 'Mik', 'Tupas', 'Galon', '(~;WU$841:#I\"I1u&2,mfw0?7n4ggz)ozDGQG_dWxf\"y12p6D{', 'Y', 182, 'Paranaque City', '639086087306', 'mik-galon.jpg', 0, '2019-05-25 20:53:03', '2019-05-25 20:53:03', 0, NULL, NULL),
(18, 'ofw', 'ofw2@gigamike.net', 'fec6351643a7abb1120cec4bc47b62f0', 'Amah', 'Santiago', 'Buenaventura', 'BF]AgA%I*kW\\Df,~%.P^bOOut~-<8HlYmKyVl~!ujWS0>_/Bl^', 'Y', 182, 'Rizal', '639086087306', 'amah.jpg', 0, '2019-05-25 20:57:26', '2019-05-25 20:57:26', 0, NULL, NULL),
(19, 'ofw', 'ofw3@gigamike.net', '53b2f8906dae589b0b6639c1bf4df3b8', 'Tristan', 'Rosales', 'Rosales', '\"P.PG$PT@h{.BjX=BE<B<(yn78OFZ=&[l3-57]hWFedgQ>%sbA', 'Y', 182, 'Cavite', '639086087306', '39403768_2048522648545871_1501565973075853312_o.jpg', 0, '2019-05-25 20:59:09', '2019-05-25 20:59:09', 0, NULL, NULL),
(20, 'ofw', 'ofw4@gigamike.net', 'b60008c405dcc3952997bf67d01f0469', 'Alfred', 'Argarin', 'Borja', '7~H1n_I>\'$Z,_H?l]VJG.p.qX^1]Ss~jrHza(D!/HZ:)$Yt`0@', 'Y', 182, 'Paranaque City', '639086087306', '14045562_10206588900433553_4995757893943319502_n.jpg', 0, '2019-05-25 21:01:42', '2019-05-25 21:01:42', 0, NULL, NULL),
(21, 'ofw', 'ofw5@gigamike.net', 'c1e73ae301af6391bf14ea21344835e0', 'Ann', 'Minor', 'Minor', '^71AQHiW4>6CNG.C]|(lEScE$S9}eOSDfde8.PnAm&c=Lp`+nh', 'Y', 182, 'Baras', '639086087306', 'ann.jpg', 0, '2019-05-25 21:08:04', '2019-05-25 21:08:04', 0, NULL, NULL),
(22, 'ofw', 'ofw6@gigamike.net', 'd336387468cf761bdcee12f7057ed31e', 'Alyssa', 'Castaneda', 'Pateña', 'wf\'g$&=hVLD=9]4FSbM)~Up5>~ju%2z{w#dz(#c]N)zff0-:qY', 'Y', 182, 'Las Pinas', '63908607306', 'alyssa.jpg', 0, '2019-05-25 21:11:27', '2019-05-25 21:11:27', 0, NULL, NULL),
(23, 'ofw', 'ofw7@gigamike.net', '801cb3f039cf3029cc746ade305f1514', 'Zcaren', 'Santiago', 'Castaneda', '?Q5;w%?h[hAr&//,%0!+9%[jM0nJnJ7/zLIsPh]-R}!X./c3>d', 'Y', 182, 'Pasay', '63908607306', 'czaren.jpg', 0, '2019-05-25 21:14:08', '2019-05-25 21:14:07', 0, NULL, NULL),
(24, 'ofw', 'ofw8@gigamike.net', '44c7ed1e57b59c464985a089f3de7e13', 'Cherry', 'Mique', 'Ornum', 'c74+V^(YLv^\'sLCDC\"nw{~zJp&5u,R)ni=y@z\"xHxWNl%q1Gr!', 'Y', 182, 'Quezon City', '63908607306', 'mique.jpg', 0, '2019-05-25 21:20:25', '2019-05-25 21:20:25', 0, NULL, NULL),
(25, 'ofw', 'ofw9@gigamike.net', 'c38e7ca57b1e172237cb193463291ff0', 'Joven', 'Flores', 'Bernados II', '5\\1I;%_^*d4qBcy\"l9<E.-ojDCewuoh,MyTg}4G)w[z;?t<,/X', 'Y', 119, 'Tokyo', '63908607306', '50853183_1490935164373530_7580299316716109824_o.jpg', 0, '2019-05-25 21:23:46', '2019-05-25 21:23:46', 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user_location`
--

CREATE TABLE `user_location` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `country_code` varchar(2) DEFAULT NULL,
  `country_name` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `created_datetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_location`
--

INSERT INTO `user_location` (`id`, `user_id`, `ip`, `country_code`, `country_name`, `city`, `latitude`, `longitude`, `created_datetime`) VALUES
(1, 1, '110.54.222.231', 'PH', 'Philippines', 'Quezon City', '14.6488', '121.0509', '2019-04-30 08:12:21'),
(2, 2, '121.58.217.194', 'PH', 'Philippines', 'Makati City', '14.5683', '121.0478', '2019-04-30 08:23:37'),
(3, 3, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.582260', '120.9958', '2019-04-30 08:28:30'),
(4, 4, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.5456', '120.974800', '2019-04-30 09:10:57'),
(5, 5, '112.210.106.129', 'PH', 'Philippines', 'Quezon City', '14.633160', '121.044690', '2019-04-30 09:21:48'),
(6, 6, '112.210.106.129', 'PH', 'Philippines', 'Paranaque', '14.488430', '121.033960', '2019-04-30 09:25:16'),
(7, 7, '112.210.106.129', 'PH', 'Philippines', 'Manila', '14.582260', '120.974800', '2019-04-30 09:29:17'),
(8, 8, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.5456', '120.9958', '2019-04-30 09:32:36'),
(9, 9, '112.210.106.129', 'PH', 'Philippines', 'Tokyo', '35.686960', '139.749460', '2019-04-30 09:36:26'),
(13, 13, '112.210.60.15', 'PH', 'Philippines', 'Pasay', '14.5456', '120.9958', '2019-05-11 14:05:26'),
(17, 17, '112.210.106.129', 'PH', 'Philippines', 'Paranaque City', '14.5456', '120.9958', '2019-05-25 20:53:03'),
(18, 18, '112.210.106.129', 'PH', 'Philippines', 'Rizal', '14.5456', '120.9958', '2019-05-25 20:57:26'),
(19, 19, '112.210.106.129', 'PH', 'Philippines', 'Cavite', '14.5456', '120.9958', '2019-05-25 20:59:09'),
(20, 20, '112.210.106.129', 'PH', 'Philippines', 'Paranaque City', '14.5456', '120.9958', '2019-05-25 21:01:42'),
(21, 21, '112.210.106.129', 'PH', 'Philippines', 'Baras', '14.5456', '120.9958', '2019-05-25 21:08:04'),
(22, 22, '112.210.106.129', 'PH', 'Philippines', 'Las Pinas', '14.5456', '120.9958', '2019-05-25 21:11:27'),
(23, 23, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.5456', '120.9958', '2019-05-25 21:14:08'),
(24, 24, '112.210.106.129', 'PH', 'Philippines', 'Quezon City', '14.5456', '120.9958', '2019-05-25 21:20:25'),
(25, 25, '112.210.106.129', 'PH', 'Philippines', 'Tokyo', '14.5456', '120.9958', '2019-05-25 21:23:46');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `call_history`
--
ALTER TABLE `call_history`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `country`
--
ALTER TABLE `country`
  ADD PRIMARY KEY (`id`),
  ADD KEY `country_code` (`country_code`),
  ADD KEY `country_name` (`country_name`);

--
-- Indexes for table `organization`
--
ALTER TABLE `organization`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `organization` (`organization`);

--
-- Indexes for table `payment_transaction`
--
ALTER TABLE `payment_transaction`
  ADD PRIMARY KEY (`transaction_id`),
  ADD UNIQUE KEY `transaction_code` (`transaction_code`),
  ADD KEY `user_id` (`user_id`) USING BTREE;

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `country_id` (`country_id`),
  ADD KEY `organization_id` (`organization_id`);

--
-- Indexes for table `user_location`
--
ALTER TABLE `user_location`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `latitude` (`latitude`),
  ADD KEY `longitude` (`longitude`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `call_history`
--
ALTER TABLE `call_history`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `organization`
--
ALTER TABLE `organization`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `payment_transaction`
--
ALTER TABLE `payment_transaction`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=129;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `user_location`
--
ALTER TABLE `user_location`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
