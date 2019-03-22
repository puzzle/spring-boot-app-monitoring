pipeline {
  agent { label 'maven-persistent' }
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    timeout(time: 10, unit: 'MINUTES')
  }
  environment {
    TO_IMAGE = "registry.ose3.puzzle.ch/dtschan/spring-boot-jib"
  }
  stages {
    stage('Build App') {
      steps {
        withMaven(mavenSettingsConfig: 'openshift-registry') {
          sh "mvn clean compile jib:build"
        }
      }
    }
  }
}
