#Leadfiles application

With this application you can browse ShareFile's folder structure that shows how to use the KidoZen SDK for Android with the following services:

- `Storage`
- `Enterprise APIs`

##How to use it

Get the souces from <a href="https://github.com/kidozen/kido-android-leadfiles.git">`github`</a> and open it using Android Studio or your favorite Android IDE

Once deployed in the device launch the application and configure it to use your kidozen credentials by pressing the Action Button on the device. Here you must provide the following values:

- Tenant: The url of the KidoZen marketplace
- Application: The application name
- User: The user account
- Password: The password for the user
- ShareFile user: Sharefile user name
- ShareFile Password: Sharefile password

Open the solution using Android Studio. To use Eclipse execute the following steps:

- Open your workspace. Import "Existing Android Code Into Workspace"
- Unckeck "LeadFiles/build/manifests/debug"
- Select the "java" folder. Right click on it, then select "build path" -> "use as a source folder" 
- Finally go to project, righ click on it, in the "configure build path" add kz.client-0.0.2.jar as a external library



##User interface

The application shows a single screen to display sharefile's folder. If the item is a folder you can browse it, if the item is a file you can get the file information

##Main classes

__LeadFilesModel__

A class that implements an simple model that interacts with the KidoZen API. 
The class implements the interface *ModelCallbacks* to interchange messages with the android fragments and use simplified method call to interact with other KidoZen API Services


The following is just a few list of some methods

- `InitializeAndAuthenticate` initializes the KidoZen SDK and executes a callback with the result of the operation
- `GetFiles` pulls the ShareFile folder and files information

##Packages

The application has only one package:

- `kidozen.samples.leadfiles`

##Dependencies

The application depends on the following .jars files

- `kz.client-0.0.2.jar` Kidozen SDK with Service support
