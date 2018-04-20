# Meals Made Easy Android Application

### Prerequisites
 - You must have version 1.8 or higher of the [Java runtime (JRE)](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) and the [Java development kit (JDK)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
 - Android Studio is not required, but highly recommended. The rest of this README will assume you have Android Studio with the latest varion of the Android build tools for API build level 27.
 - You must have an Android phone or emulator. This guide will assume you have an Android phone and appropriate USB cable.

### Dependencies
 - All dependencies are managed automatically with Gradle, which is included in this repo.

### Download
 - All code for the backend server exists in this repository. To download it, you can clone this repository, or [save it as a ZIP](https://github.com/MealsMadeEasy/Android/archive/master.zip).
 - Save or extract this repo where you can conveniently access it, such as `Downloads/Android/`.

### Build
 - This project uses Gradle as its build system through the Gradle Wrapper.
 - To build the application, run `gradlew jar` in a terminal from the root of the repository
   - On Windows, open the start menu, type `cmd`, and hit enter. On MacOS, open Spotlight by pressing `⌘ + space`, type `Terminal` into the search box, and hit return.
   - Open the folder where you cloned/extracted this repository in the terminal by typing `cd Downloads/Android` and then pressing enter (Replace `Downloads/Android` with wherever the repository is located).
   - On Windows, type `gradlew jar` and press enter. On MacOS, type `./gradlew jar` and press return.
   - Gradle will build the application and create a file in `build/libs/mealsmadeeasy-1.0.jar`.

### Installation
 - You must have an API key to access the server. For this, contact a member of this repository. These values will then need to be added to the system environmental variables. Learn [how to create custom environment variables](https://www.schrodinger.com/kb/1842), and add the following values:
 KEY           | Notes
 ------------- | -------------
 `MME_APP_ID`  | 24 characters
 `MME_API_KEY` | 64 characters
 - You will also need your SHA1 debug key. [Learn how to access this value in Android Studio.](https://stackoverflow.com/questions/27609442/how-to-get-the-sha-1-fingerprint-certificate-in-android-studio-for-debug-mode) This debug key will then need to be added to the server; to have this done, contact a member of this repository.
- Open Android Studio and select `Import Project (Gradle, Eclipse, ADT, etc.)`. Navigate to this repository's location on your local computer and select the `Android` folder.
- Connect your phone to your computer with a USB cable.
- Enable developer options and USB debugging on your phone. [Learn how to do this, here.](https://www.howtogeek.com/129728/how-to-access-the-developer-options-menu-and-enable-usb-debugging-on-android-4.2/)
- Click the run (play) button in the toolbar to run `app`. The app will be installed on your phone.
