plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.ivos.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":common")))

    //Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.mvi.kotlin)
    implementation(libs.mvi.kotlin.main)
    implementation(libs.mvi.kotlin.extensions.coroutines)
    implementation(libs.decompose)
    implementation(libs.decompose.extensions)

    //Compose UI
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material.icons)
    implementation(libs.splash.screen)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicator)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.animation)

    //Arrow
    implementation(platform(libs.arrow.stack))
    implementation(libs.arrow.core)
    implementation(libs.arrow.fx.coroutines)

    //Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //Coil
    implementation(libs.coil)
    implementation(libs.coil.svg)
    implementation(libs.coil.compose)

    //Dagger
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.hilt.navigation)

    //Dagger Test
    androidTestImplementation(libs.dagger.hilt.testing)
    kspAndroidTest(libs.dagger.hilt.android.compiler)

    //Viewmodel Ktx
    implementation(libs.viewmodel.ktx)
}
