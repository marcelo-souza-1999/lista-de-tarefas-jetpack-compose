plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
    //alias(libs.plugins.google.gms.services)
    alias(libs.plugins.detekt)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

ksp {
    arg("KOIN_DEFAULT_MODULE", "true")
    arg("KOIN_CONFIG_CHECK", "true")
}
apply(plugin = "shot")
apply(from = "../config/detekt/detekt.gradle")

android {
    namespace = "com.marcelo.souza.listadetarefas"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.marcelo.souza.listadetarefas"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += listOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
        }
    }

    configurations.all {
        resolutionStrategy {
            force("com.google.errorprone:error_prone_annotations:2.36.0")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.bundles.composeIcons)

    ksp(libs.koin.ksp.compiler)

    implementation(platform(libs.firebase.bom))

    implementation(libs.bundles.firebase)
    implementation(libs.bundles.coil.images)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.navigation3)
    implementation(libs.bundles.retrofit)
    implementation(libs.mockwebserver)
    implementation(libs.compose.shimmer)
    implementation(libs.compose.alert.dialog)

    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk.io)
    testImplementation(libs.mockk.android)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.mockk.io)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.shot.android)
    androidTestImplementation(libs.bundles.koin.test)
    androidTestImplementation(libs.coil.test)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektAll") {
    parallel = true
    buildUponDefaultConfig = true
    setSource(files(projectDir))
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/build/**")
    reports {
        xml.required.set(false)
        html.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        sarif.outputLocation.set(file("build/reports/detekt/detekt.sarif"))
    }
}