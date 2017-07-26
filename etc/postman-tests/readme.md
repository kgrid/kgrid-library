Postman tests readme:

Running in postman:
1.  Import the environment into postman by clicking the gear icon in the top right corner of postman
2.  Click "Manage Environments" in the drop-down list
3.  Click the "Import" button at the bottom of the dialog box that pops up
4.  Click choose files and navigate to this directory or wherever you have the environment saved and select the Smoketest environment
5.  Click on the name "Smoketest Environment" and change the url to point to your library instance if necessary (default is localhost:8080) and change the username and password variables to valid login credentials =
6.  Close out of the dialog box
7.  Click the "Import" button at the top left corner of postman
8.  Make sure "Import File" is elected and click the "Choose Files" button
9.  Navigate to the Library_Smoke_Tests.postman_collection and select it
10. The collection should now be in the list on the left
11. Make sure the Smoketest environment is selected and run the collection

Running in the command line with newman:
1. Navigate to the etc/postman-tests directory
2. Use the command `newman run Library_Smoke_Tests.postman_collection.json -e Smoketest-Environment.postman_environment.json` to run the collection using the smoketest environment with default parameters
3. To set custom url, username, or password parameters copy the Smoketest-Environment.postman_environment.json to another directory, make edits to the variables and use the command `newman run Library_Smoke_Tests.postman_collection.json -e /path/to/file/Smoketest-Environment.postman_environment.json`