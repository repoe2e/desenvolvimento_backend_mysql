pipeline {
  agent any

  environment {
    BACKEND_DIR = '.'   // ajuste se o pom.xml estiver em subpasta
  }

  stages {
    stage('Build aplicação') {
      steps {
        dir("${env.BACKEND_DIR}") {        // use env.BACKEND_DIR para evitar MissingProperty
          bat 'dir pom.xml'
          bat 'mvn -version'
          bat 'mvn clean install -DskipTests'
        }
      }
    }

    stage('Start aplicação') {
      steps {
        dir("${env.BACKEND_DIR}") {
          // sobe em background e mantém rodando após o pipeline
          bat 'start /B mvn spring-boot:run'
          sleep time: 30, unit: 'SECONDS'
        }
      }
    }
  }

  post {
    always {
      echo 'Aplicação no ar'
      // sem taskkill -> app continua rodando
    }
  }
}
