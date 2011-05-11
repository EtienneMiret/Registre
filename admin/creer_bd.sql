--
-- Table structure for table `tout`
--

DROP TABLE IF EXISTS `tout`;
CREATE TABLE `tout` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `titre` varchar(80) NOT NULL,
  `serie` varchar(80) default NULL,
  `proprietaire` varchar(20) default NULL,
  `type` enum('disque Blu-ray', 'DVD','cassette','livre','BD') NOT NULL,
  `emplacement` varchar(80) default NULL,
  `commentaire` text,
  `createur` varchar(20) default NULL,
  `creation` datetime default NULL,
  `dernier_editeur` varchar(20) default NULL,
  `derniere_edition` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `films`
--

DROP TABLE IF EXISTS `films`;
CREATE TABLE `films` (
  `id` int(10) unsigned NOT NULL,
  `realisateur` varchar(20) default NULL,
  `compositeur` varchar(20) default NULL,
  `genres` set('action','documentaire','fantastique','film de guerre',
    'histoire vraie','historique','humour','policier','romantique',
    'science-fiction') default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `acteurs`
--

DROP TABLE IF EXISTS `acteurs`;
CREATE TABLE `acteurs` (
  `id` int(10) unsigned NOT NULL,
  `acteur` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `bd`
--

DROP TABLE IF EXISTS `bd`;
CREATE TABLE `bd` (
  `id` int(10) unsigned NOT NULL,
  `dessinateur` varchar(20) default NULL,
  `scenariste` varchar(20) default NULL,
  `numero` int(10) unsigned default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `livres`
--

DROP TABLE IF EXISTS `livres`;
CREATE TABLE `livres` (
  `id` int(10) unsigned NOT NULL,
  `auteur` varchar(20) default NULL,
  `genres` set('fantastique','histoire vraie','historique','humour','policier',
    'romantique','science-fiction') default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `utilisateurs`
--

DROP TABLE IF EXISTS `utilisateurs`;
CREATE TABLE `utilisateurs` (
  `nom` varchar(40) NOT NULL,
  `sel` char(8) default NULL,
  `mdp` char(32) default NULL,
  PRIMARY KEY (`nom`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
CREATE TABLE `sessions` (
  `clef` char(20) NOT NULL,
  `nom` varchar(40) NOT NULL,
  `expiration` datetime NOT NULL,
  PRIMARY KEY  (`clef`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
