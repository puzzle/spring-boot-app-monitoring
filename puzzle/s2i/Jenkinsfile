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
        withMaven(mavenSettingsConfig: 'openshift-registry') {
          sh "mvn clean package"
        }
      }
    }
    stage('Build Image') {
      steps {
        script {
          openshift.verbose()
          openshift.withCluster('OpenShiftPuzzleProduction', 'dtschan-jenkins') {
            openshift.withProject('dtschan') {
              def bc = openshift.selector("bc/spring-boot-s2i")
              bc.startBuild("--from-file=target/app.jar")
              bc.logs("-f")
            }
          }
        }
      }
    }
  }
}
