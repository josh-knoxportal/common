INSERT INTO `sample` (`id`,`name`,`test_id`,`reg_id`,`reg_dt`,`mod_id`,`mod_dt`) VALUES (1,'s',3,'1','1','1','1');
INSERT INTO `sample` (`id`,`name`,`test_id`,`reg_id`,`reg_dt`,`mod_id`,`mod_dt`) VALUES (2,'ss',3,'2','2','2','2');

INSERT INTO `sample_test` (`sample_id`,`test_id`,`reg_id`,`reg_dt`,`mod_id`,`mod_dt`) VALUES (1,3,'5','5','5','5');
INSERT INTO `sample_test` (`sample_id`,`test_id`,`reg_id`,`reg_dt`,`mod_id`,`mod_dt`) VALUES (1,4,'6','6','6','6');
INSERT INTO `sample_test` (`sample_id`,`test_id`,`reg_id`,`reg_dt`,`mod_id`,`mod_dt`) VALUES (2,3,'7','7','7','7');
INSERT INTO `sample_test` (`sample_id`,`test_id`,`reg_id`,`reg_dt`,`mod_id`,`mod_dt`) VALUES (2,4,'8','8','8','8');

INSERT INTO `test` (`id`,`name`,`sample_id`,`reg_id`,`reg_dt`,`mod_id`,`mod_dt`) VALUES (3,'t',1,'3','3','3','3');
INSERT INTO `test` (`id`,`name`,`sample_id`,`reg_id`,`reg_dt`,`mod_id`,`mod_dt`) VALUES (4,'tt',1,'4','4','4','4');