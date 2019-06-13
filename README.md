# ProjectManager
Android Client App using RESTful API

This app was built to test the API exposed by my project_manager project, which was built using the Laravel framework. The mobile app authenticates using a password grant token model, then can query the API. 

ProjectManager has also been serving as a learning exercise so I can familiarize myself with a number of Android Architecture components and models:

  * Kotlin Language
  * Dependency Injection
  * ViewBinding/Adapters
  * ViewModel
  * LiveData
  * Repository
  * Room
  * NetworkBoundResource
  * And other things picked up along the way
  
  ## Project Build

**Test Server Configs**

As mentioned before, this app queries the project_manager API to retrieve its data.  Currently the Project Manager Web App is not hosted on an accessable server, so you will need to download that project and set it up on your own machine :)

In order for the app to reach the test server, you will need to enter the IP/Name of your machine in the gradle.properties file. 
```
TEST_SERVER_IP_ADDRESS="yourServerIP"
```
**API Keys**

Keys should all be saved in the gradle.properties file.  
For the required keys listed below, use the variable name specified with "yourKeyName" 
replaced with the API key you receive for your project.

Required keys:
* project_manager API Key: ```TEST_API_SECRET="yourGeneratedKey"```
* project_manager client id: ```TEST_CLIENT_ID="yourGeneratedIdForMobileApp```
* Pusher API Key: ```PUSHER_APP_KEY="yourPusherKey```

gradle.properties file:

```
TEST_SERVER_IP_ADDRESS="yourServerIP"
TEST_API_SECRET="yourGeneratedKey"
TEST_CLIENT_ID="yourGeneratedIdForMobileApp
PUSHER_APP_KEY="yourPusherKey
```
