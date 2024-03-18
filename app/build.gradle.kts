plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.streamliners.ktscript"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.streamliners.ktscript"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // Exceptions added to avoid clashes (duplicate module)
    packagingOptions.resources.pickFirsts.add("kotlin/internal/internal.kotlin_builtins")
    packagingOptions.resources.pickFirsts.add("kotlin/reflect/reflect.kotlin_builtins")
    packagingOptions.resources.pickFirsts.add("kotlin/kotlin.kotlin_builtins")
    packagingOptions.resources.pickFirsts.add("kotlin/coroutines/coroutines.kotlin_builtins")
    packagingOptions.resources.pickFirsts.add("kotlin/ranges/ranges.kotlin_builtins")
    packagingOptions.resources.pickFirsts.add("kotlin/collections/collections.kotlin_builtins")
    packagingOptions.resources.pickFirsts.add("kotlin/annotation/annotation.kotlin_builtins")
}

configurations {
    "implementation" {
        exclude("org.jetbrains.compose.material", "material-desktop")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("io.apisense:rhino-android:1.0")

    implementation(kotlin("scripting-jsr223"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")

//    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.8.22")
//    compileOnly("org.jetbrains.kotlin:kotlin-script-util")
//    compileOnly("org.jetbrains.kotlin:kotlin-script-runtime")
//    implementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable")

    implementation("com.github.The-Streamliners.DroidLibs:compose-android:1.2.10")
    implementation("com.github.The-Streamliners.DroidLibs:base:1.2.10")
    implementation("com.github.The-Streamliners.DroidLibs:utils:1.2.10")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}