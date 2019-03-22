pipeline {
  agent { label 'buildnode' }
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    timeout(time: 10, unit: 'MINUTES')
  }
  environment {
    M2_HOME = tool('maven35')
    PATH = "${M2_HOME}/bin:$PATH"
    TO_IMAGE = "registry.ose3.puzzle.ch/dtschan/spring-boot-jib"
  }
  stages {
    stage('Build App') {
      steps {
        withMaven {
          sh "mvn clean compile"
        }
      }
    }
    stage('Build Image') {
      steps {
        withMaven(mavenSettingsConfig: 'openshift-registry') {
          sh "mvn jib:build"
        }
      }
    }
  }
}
