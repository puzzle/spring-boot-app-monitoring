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
    KUBECONFIG = "${WORKSPACE}/.kube/config"
    OPENSHIFT_LOGIN = credentials('dtschan-registry')
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
        sh 'oc login ose3-master.puzzle.ch:8443 ' +
           '--namespace=dtschan --token=${OPENSHIFT_LOGIN_PSW}'
        sh 'mvn fabric8:build'
      }
    }
  }
}
