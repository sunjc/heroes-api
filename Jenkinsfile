pipeline {
  agent any
  tools {
    jdk 'jdk8'
    maven 'maven-3.6'
  }
  stages {
    stage("Clone Source") {
      steps {
        checkout([$class: 'GitSCM',
          branches: [[name: '*/master']],
          extensions: [
            [$class: 'RelativeTargetDirectory', relativeTargetDir: 'heroes-api']
          ],
          userRemoteConfigs: [[url: 'https://github.com/sunjc/heroes-api.git']]
        ])
      }
    }
    stage("Build Backend") {
      steps {
        dir('heroes-api') {
          sh 'mvn clean package -Pdev -Dmaven.test.skip=true'
        }
      }
    }
    stage("Build Image") {
      steps {
        dir('heroes-api/target') {
          sh 'oc start-build heroes-api --from-dir . --follow'
        }
      }
    }
  }
}