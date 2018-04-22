# Meals Made Easy Android Application

### Prerequisites
 - You must have version 1.8 or higher of the [Java runtime (JRE)](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) and the [Java development kit (JDK)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
 - Android Studio is not required, but highly recommended. The rest of this README will assume you have Android Studio with the latest varion of the Android build tools for API level 27. You can install the required build tools when launching Android Studio for the first time by following the wizard to download and install the Android SDK if you do not already have it.
 - You must have an Android phone or emulator. This guide will assume you have an Android phone and appropriate USB cable.

### Dependencies
 - All dependencies are managed automatically with Gradle, which is included in this repo.

### Download
 - All code for the backend server exists in this repository. To download it, you can clone this repository, or [save it as a ZIP](https://github.com/MealsMadeEasy/Android/archive/master.zip).
 - Save or extract this repo where you can conveniently access it, such as `Downloads/Android/`.

### Build
 - This project can be build easily with Android Studio.
 - From the welcome screen, you can open a new project by clicking "Open an existing Android Studio project." If you've used Android Studio before and it opens to another project, you can open Meals Made Easy by navigating to the "File" menu and choosing "Open." The steps afterwards are the same.
 - In the file picker, navigate to the folder where you extracted this repository. So if you saved it in `Downloads/Android/`, you'd choose the `Android` folder.
 - Android Studio will open a new window and automatically build the project as necessary.

### Installation
 - You must have an API key to access the server. For this, contact a member of this repository. These values will then need to be added to the system environmental variables. Learn [how to create custom environment variables](https://www.schrodinger.com/kb/1842), and add the following values:
 
 | KEY           | Notes         |
 | ------------- | ------------- |
 | `MME_APP_ID`  | 24 characters |
 | `MME_API_KEY` | 64 characters |
 
 - You will also need your SHA1 debug key. [Learn how to access this value in Android Studio.](https://stackoverflow.com/questions/27609442/how-to-get-the-sha-1-fingerprint-certificate-in-android-studio-for-debug-mode) This debug key will then need to be added to Firebase.
   - On the Firebase dashboard, click the cog near the top left of the window next to "Project Overview." Then select "Project settings."
   - On the "General" tab, find the "Your apps" section. Select the Android app with package name `com.mealsmadeeasy`, or create it if it doesn't exist.
   - Under SHA certificate values, click "Add fingerprint", and paste the SHA1 debug key you found earlier.
- When you're ready to start the app, connect your phone to your computer with a USB cable.
- If not already enabled, ensure that USB debugging is turned on for your phone. [You can learn how to do this here](https://www.howtogeek.com/129728/how-to-access-the-developer-options-menu-and-enable-usb-debugging-on-android-4.2/).
- Click the run (play) button in the toolbar to run the application. The app will be installed on your phone.

### Common Issues
 - If you aren't able to log into the application, ensure that it is properly connected to Firebase and that your SHA1 debug key has been correctly added to the Firebase console.
 - If the application is unable to connect to the server when logging in, simply click try again. The Meals Made Easy server is hosted on Heroku and goes to sleep after periods of inactivity. It can take a few minutes for the server to become available.
 - If the server is rejecting all requests and is running, make sure that the `MME_APP_ID` and `MME_API_KEY` environment variables have been set and are valid.
