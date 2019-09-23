plugins {
    application
//    kotlin("jvm") version "1.3.10"

}

application {
    mainClassName = ".AppKt"
}
dependencies {

    compile("org.jetbrains.kotlin", "kotlin-stdlib", "1.3.50")
    compile ("com.github.shaunxiao:kotlinGameEngine:v0.0.4")
}
//写一级目录
repositories {
    maven {

        url = uri("http://maven.aliyun.com/nexus/content/groups/public/")
        url = uri("http://maven.aliyun.com/nexus/content/repositories/jcenter")
        url = uri("https://jitpack.io")
        mavenCentral()

    }


}
buildscript {
    repositories {
        maven {

            url = uri("https://jitpack.io")
            url = uri("http://maven.aliyun.com/nexus/content/groups/public/")
            url = uri("http://maven.aliyun.com/nexus/content/repositories/jcenter")
            mavenCentral()
        }


    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")

    }
}

apply(plugin = "org.jetbrains.kotlin.jvm")