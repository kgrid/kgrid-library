UPDATE ObjectTellerDB.server_details
SET setting_type = 'FEDORA_SERVER', ip_address = '10.16.6.32', svr_name = 'dlhs-fedora-dev-a.umms.med.umich.edu',
  svr_username   = 'fedoraAdmin', svr_passwd = 'secret321', svr_port = 8080,
  complete_url   = 'http://dlhs-fedora-dev-a.umms.med.umich.edu:8080/fcrepo/rest/'
WHERE id = 1;
UPDATE ObjectTellerDB.server_details
SET setting_type = 'FUSEKI_SERVER', ip_address = 'undefined', svr_name = 'dlhs-fedora-dev-a.umms.med.umich.edu',
  svr_username   = 'undefined', svr_passwd = 'undefined', svr_port = 8080,
  complete_url   = 'http://dlhs-fedora-dev-a.umms.med.umich.edu:8080/fuseki/test/query'
WHERE id = 2;
UPDATE ObjectTellerDB.server_details
SET setting_type = 'SMTP_SERVER', ip_address = '10.16.6.32', svr_name = 'Fake SMTP Server',
  svr_username   = 'fedoraAdmin', svr_passwd = 'Not Configured', svr_port = NULL, complete_url = NULL
WHERE id = 3;