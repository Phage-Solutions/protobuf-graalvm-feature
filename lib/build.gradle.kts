plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.0"

    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.graalvm.nativeimage:svm:24.1.1")
    compileOnly("com.google.protobuf:protobuf-java:4.29.1")

    implementation("org.reflections:reflections:0.10.2")

}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Phage-Solutions/protobuf-graalvm-feature")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks.named("javadocJar").get())
            artifact(tasks.named("sourcesJar").get())
        }
    }
}
