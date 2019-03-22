pipeline {
  agent { label 'buildnode' }
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    timeout(time: 10, unit: 'MINUTES')
  }
  environment {
    M2_HOME = tool('maven35')
    OC_HOME = tool('oc')
    PATH = "${M2_HOME}/bin:${OC_HOME}/bin:$PATH"
  }
  stages {
    stage('Build App') {
      steps {
        withMaven {
          sh "mvn clean package"
        }
      }
    }
    stage('Build Image') {
      steps {
        script {
          openshift.withCluster('OpenShiftPuzzleProduction', 'dtschan-jenkins') {
            openshift.withProject('dtschan') {
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
}
