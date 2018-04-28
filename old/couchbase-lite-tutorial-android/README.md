# Couchbase Lite Android tutorial


This repository contains the complete Android app used in the [tutorial for Couchbase Lite Android](http://developer.couchbase.com/mobile/develop/training/build-first-android-app/index.html). The code was developed in Android Studio, the recommended IDE to use for developing Couchbase Lite Android apps. If you want to run it in Eclipse, refer to the tutorial.

The tutorial shows how to bring the Couchbase Lite into your app, set up dependencies, create a database, and do basic CRUD operations. To focus on using Couchbase Lite, the app is highly simplified and provides only console output in the simulator. (There’s no UI output other than a plain white background screen that displays the text "Hello World"").

## Downloading and running the tutorial app

1. Change to the directory in which you want to place the code:

	$ cd ~/repos

2. Clone the repository:

	$ git clone https://github.com/couchbaselabs/couchbase-lite-tutorial-android.git

3. Change to the directory that contains the repo:

	$ cd couchbase-lite-tutorial-android

4. In Android Studio, select **File > Import Project** and navigate to the **/couchbase-lite-tutorial-android/HelloWorld** folder.

5. Select either the **build.gradle** or **settings.gradle** file and click **OK**.

6. Click **Run**.
