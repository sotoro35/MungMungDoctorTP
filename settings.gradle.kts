pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //네이버 지도 SDK는 https://repository.map.naver.com/archive/maven Maven 저장소에서 배포됩니다. 루트 프로젝트의 build.gradle에 저장소 설정을 추가합니다.
        maven("https://repository.map.naver.com/archive/maven")

        //카카오 로그인
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")

    }
}

rootProject.name = "MungMungDoctorTP"
include(":app")
