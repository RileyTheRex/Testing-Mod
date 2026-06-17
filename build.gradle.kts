plugins {
    id("java-library")
    id("net.neoforged.gradle.userdev") version "7.0.162"
}

version = "1.0.0"
group = "com.playerwarp"

base {
    archivesName = "playerwarp"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

runs {
    create("client") {
        client()
        programArguments("--username", "Dev")
        modSource(sourceSets["main"])
    }
    create("server") {
        server()
        modSource(sourceSets["main"])
    }
}

dependencies {
    implementation("net.neoforged:neoforge:21.1.233")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
