node {
    checkout scm
    stage('Test') {
        bat 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent test'
    }
    stage('Sonar') {
        bat 'mvn sonar:sonar'
    }
    stage('Package') {
        bat 'mvn clean package -Dmaven.test.skip=true'
    }
}