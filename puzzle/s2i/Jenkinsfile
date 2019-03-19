pipeline {
    agent { label 'maven-persistent' }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timeout(time: 10, unit: 'MINUTES')
        timestamps()  // Timestamper Plugin
        ansiColor('xterm')  // AnsiColor Plugin
    }
    stages {
        stage('Build App') {
            steps {
                sh "mvn clean package"
            }
        }
        stage('Build Image') {
          steps {
            script {
              openshift.withCluster() {
                artifact = findFiles(glob: "target/*.jar")[0].path
                def bc = openshift.selector("bc/spring-boot-app-monitoring")
                bc.startBuild("--from-file=${artifact}", "-F")
                // bc.logs("-f")
              }
            }
          }
        }
    }
}
