plugins {
	id("fabric-loom").version("1.10-SNAPSHOT")
	id("maven-publish")
}

loom {
	runs {
		register("datagen") {
			client()
			name = "Data Generation"

			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.modid=${BuildConfig.modId}")
			vmArg("-Dfabric-api.datagen.output-dir=${project.file("src/main/generated")}")
			runDir("build/datagen")

			ideConfigGenerated(true)
		}
		configureEach {
			if (name == "client") {
				programArgs.add("--username=Ladybrine")
				programArgs.add("--uuid=5d66606c-949c-47ce-ba4c-a1b9339ba3c8")
			}
		}
	}
}

sourceSets {
	main {
		resources.srcDir("src/main/generated")
		resources.exclude(".cache")
		resources.exclude("daisySMP.aseprite")
		resources.exclude("assets/daisy/sounds/convert_to_ogg.sh")
	}
}

version = BuildConfig.modVersion + "+1.20"
group = BuildConfig.mavenGroup

base {
	archivesName.set(BuildConfig.modId)
}

repositories {
	maven {
		name = "Modrinth"
		url = uri("https://api.modrinth.com/maven")
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${BuildConfig.minecraftVersion}")
	mappings("net.fabricmc:yarn:${BuildConfig.yarnMappings}:v2")
	modImplementation("net.fabricmc:fabric-loader:${BuildConfig.loaderVersion}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${BuildConfig.fabricVersion}")

	implementation("io.github.llamalad7:mixinextras-fabric:0.5.0-rc.2")
	annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.5.0-rc.2")
	include("io.github.llamalad7:mixinextras-fabric:0.5.0-rc.2:slim")

	modImplementation("maven.modrinth:macu-lib:${BuildConfig.macuLibVersion}-${BuildConfig.minecraftVersion}-fabric")
}

tasks.processResources {
	filesMatching("fabric.mod.json") {
		expand(
			"version" to BuildConfig.modVersion,
			"modId" to BuildConfig.modId,
			"modName" to BuildConfig.modName,
			"description" to BuildConfig.description,
			"license" to BuildConfig.license,
			"loaderVersion" to BuildConfig.loaderVersion,
			"minecraftVersion" to BuildConfig.minecraftVersion,
			"minecraftVersionRange" to BuildConfig.minecraftVersionRange,
			"macuLibVersion" to BuildConfig.macuLibVersion
		)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(17)
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_${BuildConfig.modId}"}
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = BuildConfig.modId
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}