# Project Setup

## 1. Keystore

1. Create a new folder named `keystore` in your project's root directory.
2. **Build > Generate Signed Bundle / APK**.
3. Create a new keystore file named `keystore.jks` in the `keystore` folder.

## 2. `keystore.properties`

1. Create `keystore.properties` in the `keystore` folder.
2. Add the following content to the `keystore.properties` file.
    ```properties
    storePassword=your_store_password
    keyPassword=your_key_password
    keyAlias=your_key_alias
    ```

## 3. Firebase

1. Go to [Firebase console](https://console.firebase.google.com/) and create a Firebase project.
2. [Add Firebase to your Android project](https://firebase.google.com/docs/android/setup) by following the instructions in the official documentation.
3. Download `google-services.json` and place it in the `app` module directory.
4. Sync project.

