# Project Setup

## 1. Keystore

1. Create a new folder named `keystore` in your project's root directory.
2. **Build > Generate Signed Bundle / APK**.
3. Create a new keystore file named `keystore.jks` in the `keystore` folder.
4. Securely store the keystore password, key password, and alias.

## 2. `keystore.properties`

1. Create `keystore.properties` in the `keystore` folder.
2. Add the following content to the `keystore.properties` file.
    ```properties
    storePassword=your_store_password
    keyPassword=your_key_password
    keyAlias=your_key_alias
    ```
3. **Do not commit this file to version control!** Add it to `.gitignore`.

## 3. Firebase

1. Go to [Firebase console](https://console.firebase.google.com/).
2. Create a Firebase project and add your Android app.
3. Download `google-services.json` and place it in the `app` module directory.
4. Add the Google Services plugin to your project-level `build.gradle.kts`:
5. Sync project.

