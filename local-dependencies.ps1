cd ..

function Clone-Repo {

    param (
        $RepoName
    )

    if (Test-Path -Path $RepoName) {
        cd $RepoName
        git pull
    } else {
        git clone https://github.com/lightningkite/$RepoName
        cd $RepoName
    }
    .\gradlew.bat publishToMavenLocal
    cd ..
}

Clone-Repo khrysalis
Clone-Repo rxkotlin-plus
Clone-Repo android-layout-translator
Clone-Repo lightning-server
