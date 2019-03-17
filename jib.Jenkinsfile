pipeline {
    agent { label 'maven-persistent || buildnode' }
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
        stage('Init') {
          when {
            not { environment name: 'KUBERNETES_PORT', value: '' }
          }
          steps {
            withCredentials([sshUserPrivateKey(credentialsId: 'spring-boot-registry-cert', keyFileVariable: 'REGISTRY_CERT')]) {
              sh "cp --no-preserve=all /usr/lib/jvm/jre/lib/security/cacerts ."
              sh "keytool -import -noprompt -alias registry -keystore cacerts -file ${REGISTRY_CERT} -storepass changeit"
            }
          }
        }

        stage('Build App') {
          steps {
            withMaven(mavenSettingsConfig: 'openshift-registry') {
              sh "mvn clean compile jib:build -Djavax.net.ssl.trustStore=cacerts"
            }

            // Archive workspace file listing for debugging purposes
            sh "ls -lR >ls-lR.txt"
            archiveArtifacts "ls-lR.txt"
          }
        }
    }
}
