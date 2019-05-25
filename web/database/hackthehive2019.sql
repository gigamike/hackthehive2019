-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: May 02, 2019 at 10:15 AM
-- Server version: 5.7.23
-- PHP Version: 7.0.32

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
(1, 'volunteer', 'volunteer1@gigamike.net', '2bfe978ec2fa37efd1cd7d2d5f85928f', 'Carlo', 'Cruz', 'Sampan', '1a~2lB7U7E\'oi~itM^8\"IL/Wb`T\"g/\\wp\\*]}A35f:&P9nFeN^', 'Y', 182, 'Pasay', '639086087306', '10842281_10153221698703454_2784909659624885851_o.jpg', 3, '2019-05-02 10:14:24', '2019-04-30 08:12:21', 0, NULL, NULL),
(2, 'volunteer', 'volunteer2@gigamike.net', '824a8408dd4181e2581af92987045db0', 'Caloy', 'Magsakay', 'Diño', 'UJ>+aoA,!;uWIy/oOP;h>h(^,9HO:K+nuIxW::c:UYq}T\"n%R*', 'Y', 182, 'Pasay', '649086087306', '43641649_10155540003986783_3342528950600466432_o.jpg', 1, '2019-04-30 08:23:37', '2019-04-30 08:23:37', 0, NULL, NULL),
(3, 'volunteer', 'volunteer3@gigamike.net', '5a138e05fc481f9d7ba462cf292ec851', 'Rev. Fr. Vicente', 'Cruz', 'Gabut', 'f{zQ/?yl&{-fY|u\"}`k9r^aBHzx)`p+Gm\'w{FqiLmu3Hs)Jqh6', 'Y', 182, 'Pasay', '649086087306', '59093883_10213923153337925_1345913390446411776_o.jpg', 1, '2019-04-30 08:28:30', '2019-04-30 08:28:30', 0, NULL, NULL),
(4, 'volunteer', 'volunteer4@gigamike.net', '162a5ed6ec457510a0d96dd5d1f0b4f9', 'Anthony', 'Cruz', 'Reyes', '&}\'^%R\'Vxe(fAk2Vs.O\"9>H5kK\'*vPz{O#[TTb,NI35j~FAsTp', 'Y', 182, 'Pasay', '639086087306', 'photo-750x450.jpg', 7, '2019-04-30 09:10:57', '2019-04-30 09:10:57', 0, NULL, NULL),
(5, 'volunteer', 'volunteer5@gigamike.net', 'ff281114c9a7eb3ab6a0a84c71c1a35b', 'Najeeb', 'Singkee', 'Singkee', '&WByF2x%F1.xn0f\'FV>A22r`#6n>%)n+`2%\'D},j/:c}JJ&o\"C', 'Y', 182, 'Pasay', '649086087306', 'photo-750x450.jpg', 10, '2019-04-30 09:21:48', '2019-04-30 09:21:47', 0, NULL, NULL),
(6, 'volunteer', 'volunteer6@gigamike.net', 'd80696dc7ce40cc101809c97c75eb3f0', 'Neil ', 'Cruz', 'Rojas', 'SPNEAi:&p~Z>\"PaEIV-r\\A%ahY./8a#j3P1T;JY,I5IKd,o/b|', 'Y', 182, 'Pasay', '649086087306', 'photo-750x450.jpg', 4, '2019-04-30 09:25:16', '2019-04-30 09:25:16', 0, NULL, NULL),
(7, 'volunteer', 'volunteer7@gigamike.net', '1a057f7651f8ca9d23767f81a32e1b07', 'Edward', 'Buenaventura', 'Cawicaan', 'qj8<=;59&y~sMWU=`K2axtL=o\"b=qnOdYf!u\"6/(1/z]fPzGz-', 'Y', 182, 'Pasay', '649086087306', 'photo-750x450.jpg', 2, '2019-04-30 09:29:17', '2019-04-30 09:29:17', 0, NULL, NULL),
(8, 'volunteer', 'volunteer8@gigamike.net', '5f6c5a0de0ae45700d5532a10ccb6d93', 'OWWA', 'Volunteers', 'Volunteers', 'ybGb$N$tF2+\"hc4GnJ,JA)fCAg\"|sf/mIVPM%TBJeMLO2`v\"+#', 'Y', 182, 'Pasay', '649086087306', 'photo-750x450.jpg', 5, '2019-04-30 09:32:36', '2019-04-30 09:32:36', 0, NULL, NULL),
(9, 'volunteer', 'volunteer9@gigamike.net', '46be4f6bb9f0d82de34f4d36c5a4c907', 'Japan', 'Volunteers', 'Volunteers', '4T%sW;ct@kdJMuNS:M_%[xgZC3_45#EIWJ=0d\"%%lhO;^}nxKN', 'Y', 119, 'Tokyo', '649086087306', 'photo-750x450.jpg', 5, '2019-04-30 09:36:26', '2019-04-30 09:36:26', 0, NULL, NULL),
(10, 'ofw', 'ofw1@gigamike.net', '9eec4d4c3e28e6ce26a66300f974e5dd', 'Mik', 'Tupas', 'Galon', 'S<:G{9`5P2IRyQ-b@hW\")G6-4G;P<z/n8H74`wH1*qc%DogdY?', 'Y', 182, 'Pasay', '649086087306', 'photo-750x450.jpg', 0, '2019-05-02 10:14:34', '2019-04-30 10:42:53', 0, NULL, NULL);

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
(1, 1, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.5456', '120.9958', '2019-04-30 08:12:21'),
(2, 2, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.543610', '120.994450', '2019-04-30 08:23:37'),
(3, 3, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.582260', '120.9958', '2019-04-30 08:28:30'),
(4, 4, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.5456', '120.974800', '2019-04-30 09:10:57'),
(5, 5, '112.210.106.129', 'PH', 'Philippines', 'Quezon City', '14.633160', '121.044690', '2019-04-30 09:21:48'),
(6, 6, '112.210.106.129', 'PH', 'Philippines', 'Paranaque', '14.488430', '121.033960', '2019-04-30 09:25:16'),
(7, 7, '112.210.106.129', 'PH', 'Philippines', 'Manila', '14.582260', '120.974800', '2019-04-30 09:29:17'),
(8, 8, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.5456', '120.9958', '2019-04-30 09:32:36'),
(9, 9, '112.210.106.129', 'PH', 'Philippines', 'Tokyo', '35.686960', '139.749460', '2019-04-30 09:36:26'),
(10, 10, '112.210.106.129', 'PH', 'Philippines', 'Pasay', '14.5456', '120.9958', '2019-04-30 10:42:54');

--
-- Indexes for dumped tables
--

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
-- AUTO_INCREMENT for table `organization`
--
ALTER TABLE `organization`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `user_location`
--
ALTER TABLE `user_location`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
