pipeline {
    agent { label 'maven-persistent' }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timeout(time: 10, unit: 'MINUTES')
        //timestamps()  // Timestamper Plugin
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
                  sh "cp ${env.JAVA_HOME}/lib/security/cacerts ."
                  sh "keytool -import -noprompt -alias registry -storetype PKCS12 -keystore cacerts -file ${REGISTRY_CERT} -storepass changeit"
                  sh "mvn clean compile jib:build -Popenshift -Djavax.net.ssl.trustStore=cacerts"
                }
              }
              // }
/*                milestone(10)  // The first milestone step starts tracking concurrent build order
                withEnv(["JAVA_HOME=${tool 'jdk8_oracle'}", "PATH+MAVEN=${tool 'maven35'}/bin:${env.JAVA_HOME}/bin"]) {
                    sh 'mvn -B -V -U -e clean verify -DskipTests'
                }*/
            }
        }
        stage('Build Image') {
          steps {
            script {
              openshift.withCluster() {
                sh "mkdir -p input && cp Dockerfile target/*.jar input"
                def bc = openshift.selector("bc/spring-boot-docker")
                bc.startBuild("--from-dir=input")
                bc.logs("-f")
              }
            }
          }
        }
    }
}
