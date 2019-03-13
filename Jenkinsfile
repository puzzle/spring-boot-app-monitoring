pipeline {
    agent { label 'maven' }
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
              sh "mvn clean package -Popenshift"
              sh "oc whoami"
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
                artifact = findFiles(glob: "target/*.jar")[0].path
                openshift.selector("bc/sprint-boot-app-monitoring").startBuild("--wait") // "--from-file=${artifact}",
              }
            }
          }
        }
    }
}
