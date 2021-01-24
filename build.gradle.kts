plugins {
    kotlin("jvm") version "1.4.10"
}

group = "ru.nsu"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.8.6")
}
