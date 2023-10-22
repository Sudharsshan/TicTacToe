plugins {
    id("com.android.application")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile =
                file("C:\\Users\\Sudharsshan\\StudioProjects\\tictactoe2\\tictactoe-release-v1.0.keystore")
            storePassword = "Lumine2004."
            keyAlias = "tictactoe-v1.0-key"
            keyPassword = "Lumine2004."
        }
    }
    namespace = "andromeda.basicgames.tictactoe"
    compileSdk = 34

    defaultConfig {
        applicationId = "andromeda.basicgames.tictactoe"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}