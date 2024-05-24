import java.util.Properties

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.ncorti.ktfmt.gradle") version "0.16.0"
  id("com.google.gms.google-services")
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
  id("org.sonarqube") version "4.4.1.3373"
  id("jacoco")
}

android {
  namespace = "com.example.triptracker"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.example.triptracker"
    minSdk = 29
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables { useSupportLibrary = true }
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  signingConfigs {
    create("release") {
      storeFile = file("keystore.jks")
      storePassword = System.getenv("SIGNING_STORE_PASSWORD")
      keyAlias = System.getenv("SIGNING_KEY_ALIAS")
      keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
    }
  }

  buildTypes {
    // Load properties from the local.properties file
    val properties =
        Properties().apply { load(project.rootProject.file("local.properties").inputStream()) }
    val api = properties.getProperty("API_KEY")

    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    debug {
      // Safely add API_KEY as a build config field, with a fallback value if not found
      buildConfigField("String", "API_KEY", "\"$api\"")
      // Add api as a resource value, similar fallback approach
      resValue("string", "api", api)

      enableUnitTestCoverage = true
      enableAndroidTestCoverage = true
    }
  }

  testCoverage { jacocoVersion = "0.8.8" }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions { jvmTarget = "1.8" }

  composeOptions { kotlinCompilerExtensionVersion = "1.5.1" }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      merges += "META-INF/LICENSE.md"
      merges += "META-INF/LICENSE-notice.md"
    }
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
      isReturnDefaultValues = false
    }
  }

  // Robolectric needs to be run only in debug. But its tests are placed in the shared source set
  // (test)
  // The next lines transfers the src/test/* from shared to the testDebug one
  //
  // This prevent errors from occurring during unit tests
  sourceSets.getByName("testDebug") {
    val test = sourceSets.getByName("test")

    java.setSrcDirs(test.java.srcDirs)
    res.setSrcDirs(test.res.srcDirs)
    resources.setSrcDirs(test.resources.srcDirs)
  }

  sourceSets.getByName("test") {
    java.setSrcDirs(emptyList<File>())
    res.setSrcDirs(emptyList<File>())
    resources.setSrcDirs(emptyList<File>())
  }
}

sonarqube {
  properties {
    property("sonar.projectKey", "EPFL-SwEnt-2024-LaStartUp_TripTracker")
    property("sonar.projectName", "La Start Up")
    property("sonar.organization", "epfl-swent-2024-lastartup")
    property("sonar.host.url", "https://sonarcloud.io")
    // Comma-separated paths to the various directories containing the *.xml JUnit report files.
    // Each path may be absolute or relative to the project base directory.
    property(
        "sonar.junit.reportPaths",
        "${project.layout.buildDirectory.get()}/test-results/testDebugunitTest/")
    // Paths to xml files with Android Lint issues. If the main flavor is changed, this file will
    // have to be changed too.
    property(
        "sonar.androidLint.reportPaths",
        "${project.layout.buildDirectory.get()}/reports/lint-results-debug.xml")
    // Paths to JaCoCo XML coverage report files.
    property(
        "sonar.coverage.jacoco.xmlReportPaths",
        "${project.layout.buildDirectory.get()}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")

    property("sonar.gradle.skipCompile", "true")
  }
}

// When a library is used both by robolectric and connected tests, use this function
fun DependencyHandlerScope.globalTestImplementation(dep: Any) {
  androidTestImplementation(dep)
  testImplementation(dep)
}

dependencies {

  // --------------- Android Compose ----------------
  implementation(platform("androidx.compose:compose-bom:2024.04.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")

  implementation("androidx.compose.material:material")

  // Material3
  implementation("androidx.compose.material3:material3")
  implementation("androidx.compose.material3:material3-android")
  implementation("androidx.compose.material:material-icons-extended") // extra icons

  // Ambiant user and live data
  implementation("androidx.compose.runtime:runtime")
  implementation("androidx.compose.runtime:runtime-rxjava2")
  implementation("androidx.compose.runtime:runtime-livedata")
    implementation(libs.androidx.exifinterface)
  implementation(libs.core.ktx)

  // Camera
  val cameraVersion = "1.3.1"
  implementation("androidx.camera:camera-lifecycle:$cameraVersion")
  implementation("androidx.camera:camera-camera2:$cameraVersion")
  implementation("androidx.camera:camera-view:$cameraVersion")
  implementation("androidx.camera:camera-core:$cameraVersion")

  // Testing compose
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")


  // --------------- AndroidActivity ----------------
  val activityVersion = "1.8.2"
  implementation("androidx.activity:activity:$activityVersion")
  implementation("androidx.activity:activity-compose:$activityVersion")
  implementation("androidx.activity:activity-ktx:$activityVersion")


  // --------------- Navigation ----------------
  val navVersion = "2.7.7"
  implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
  implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
  implementation("androidx.navigation:navigation-compose:$navVersion")

  implementation("androidx.fragment:fragment:1.5.5")

  // Testing navigation
  androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")


  // --------------- Location ----------------
  implementation("com.google.android.gms:play-services-location:21.2.0")
  implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")


  // --------------- Firebase ----------------
  // When using the BoM, you don't specify versions in Firebase library dependencies
  implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
  implementation("com.google.firebase:firebase-storage")
  implementation("com.google.firebase:firebase-firestore")
  implementation("com.google.firebase:firebase-analytics")
  implementation("com.google.firebase:firebase-auth-ktx")
  implementation("com.google.firebase:firebase-database-ktx")

  implementation("com.google.android.play:core-ktx:1.8.1")
  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
  
  // --------------- Google Authentication ----------------
  implementation("com.google.android.gms:play-services-auth:21.1.0")

  // --------------- Google Maps ----------------
  val mapsVersion = "4.3.3"
  implementation("com.google.maps.android:maps-compose:$mapsVersion")
  implementation("com.google.maps.android:maps-compose-utils:$mapsVersion")
  implementation("com.google.android.gms:play-services-maps:18.2.0")

  // --------------- Coil ----------------
  // Async Image
  implementation("io.coil-kt:coil-compose:2.6.0")

  // --------------- OkHttp ----------------
  implementation("com.squareup.okhttp3:okhttp:4.12.0")

  // --------------- JSON ----------------
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

  // --------------- Espresso ----------------
  val espressoVersion = "3.5.1"
  androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
  androidTestImplementation("androidx.test.espresso:espresso-intents:$espressoVersion")

  // --------------- Kaspresso ----------------
  val kaspressoVersion = "1.4.3"
  androidTestImplementation("com.kaspersky.android-components:kaspresso:$kaspressoVersion")
  androidTestImplementation("com.kaspersky.android-components:kaspresso-allure-support:$kaspressoVersion")
  androidTestImplementation("com.kaspersky.android-components:kaspresso-compose-support:$kaspressoVersion")

  // --------------- Robolectric ----------------
  testImplementation(libs.robolectric)

  // --------------- Mockk ----------------
  val mockVersion = "1.13.10"
  testImplementation("io.mockk:mockk:$mockVersion")
  testImplementation("io.mockk:mockk-android:$mockVersion")
  testImplementation("io.mockk:mockk-agent:$mockVersion")
  androidTestImplementation("io.mockk:mockk-android:$mockVersion")
  androidTestImplementation("io.mockk:mockk-agent:$mockVersion")

  // --------------- Mockito ----------------
  testImplementation ("org.mockito:mockito-core:4.0.0")
  androidTestImplementation ("org.mockito:mockito-android:4.0.0")
  testImplementation ("org.mockito:mockito-inline:2.13.0")
  testImplementation ("org.robolectric:robolectric:4.6.1")


  // --------------- JUnit ---------------
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")

  // --------------- Appcompat ---------------
  implementation("androidx.appcompat:appcompat:1.5.1")

}

configurations.all {
  resolutionStrategy.force("com.google.protobuf:protobuf-javalite:3.22.3")
}

tasks.withType<Test> {
  // Configure Jacoco for each tests
  configure<JacocoTaskExtension> {
    isIncludeNoLocationClasses = true
    excludes = listOf("jdk.internal.*")
  }
}

tasks.register("jacocoTestReport", JacocoReport::class) {
  mustRunAfter("testDebugUnitTest", "connectedDebugAndroidTest")

  reports {
    xml.required = true
    html.required = true
  }

  val fileFilter =
      listOf(
          "**/R.class",
          "**/R$*.class",
          "**/BuildConfig.*",
          "**/Manifest*.*",
          "**/*Test*.*",
          "android/**/*.*",
      )
  val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") { exclude(fileFilter) }
  val mainSrc = "${project.projectDir}/src/main/java"

  sourceDirectories.setFrom(files(mainSrc))
  classDirectories.setFrom(files(debugTree))
  executionData.setFrom(
      fileTree(project.buildDir) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        include("outputs/code_coverage/debugAndroidTest/connected/*/coverage.ec")
      })
}
