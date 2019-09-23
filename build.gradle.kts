plugins {
    application
//    kotlin("jvm") version "1.3.10"

}

//application {
//    mainClassName = "demo"
//}
dependencies {

    compile("org.jetbrains.kotlin", "kotlin-stdlib", "1.3.50")
    compile ("com.github.shaunxiao:kotlinGameEngine:v0.0.4")
}
//写一级目录
repositories {
    maven {

        url = uri("http://maven.aliyun.com/nexus/content/groups/public/")
        url = uri("http://maven.aliyun.com/nexus/content/repositories/jcenter")
        mavenCentral()
        url = uri("https://jitpack.io")
    }


}
buildscript {
    repositories {
        maven {
            mavenCentral()
            url = uri("https://jitpack.io")
            url = uri("http://maven.aliyun.com/nexus/content/groups/public/")
            url = uri("http://maven.aliyun.com/nexus/content/repositories/jcenter")

        }


    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")

    }
}

apply(plugin = "org.jetbrains.kotlin.jvm")