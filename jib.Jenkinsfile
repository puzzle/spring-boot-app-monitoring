pipeline {
    agent { label 'maven-persistent' }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timeout(time: 10, unit: 'MINUTES')
        timestamps()  // Timestamper Plugin
        ansiColor('xterm')  // AnsiColor Plugin
    }
    /*triggers {
        pollSCM('H/5 * * * *')
    }*/
    /*environment {
      M2_SETTINGS = credentials('m2_settings')
      KNOWN_HOSTS = credentials('known_hosts')
      ARTIFACTORY = credentials('jenkins-artifactory')
      ARTIFACT = "${env.JOB_NAME.split('/')[0]}-hello"
      REPO_URL = 'https://artifactory.puzzle.ch/artifactory/ext-release-local'
    }*/
    stages {
        stage('Build App') {
            steps {
              //cache(maxCacheSize: 250, caches: [
              //  [$class: 'ArbitraryFileCache', includes: '**/*', path: '${HOME}/.m2']
              //]) {
              withCredentials([sshUserPrivateKey(credentialsId: 'spring-boot-registry-cert', keyFileVariable: 'REGISTRY_CERT')]) {
                withMaven(mavenSettingsConfig: 'openshift-registry') {
                  sh "cp --no-preserve=all /usr/lib/jvm/jre/lib/security/cacerts ."
                  sh "keytool -import -noprompt -alias registry -keystore cacerts -file ${REGISTRY_CERT} -storepass changeit"

                  sh "mvn clean compile jib:build -Djavax.net.ssl.trustStore=cacerts"

                  // Archive workspace file listing for debugging purposes
                  sh "ls -lR >ls-lR.txt"
                  archive "ls-lR.txt"
                }
              }
              // }
            }
        }
    }
}
