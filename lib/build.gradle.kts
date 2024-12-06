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

group = "sk.phage"
version = project.version

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<Jar>("sourcesJar") {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

tasks.register<Jar>("javadocJar") {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
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
            groupId = "sk.phage"
            artifactId = project.name

            from(components["java"])

            // Include the sources JAR
            artifact(tasks.named("sourcesJar").get())

            // Optionally include the JavaDocs JAR
            artifact(tasks.named("javadocJar").get())
        }
    }
}
