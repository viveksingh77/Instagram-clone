plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.iswherevivek.instaclone"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iswherevivek.instaclone"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding = true
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //picasso
    implementation("com.squareup.picasso:picasso:2.8")
    //dynamic sizes
    implementation("com.github.MrNouri:DynamicSizes:1.0")
    //circular image view
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //lottie animation
    implementation("com.airbnb.android:lottie:6.0.0")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")
    //time ago
    implementation("com.github.marlonlom:timeago:4.0.3")
    //circle number
    implementation("com.github.3llomi:CircularStatusView:V1.0.3")
    //story view
    implementation("com.github.OMARIHAMZA:StoryView:1.0.2-alpha")
    //shimmer effect
//    implementation("com.github.sharish:ShimmerRecyclerView:v1.3")
    }