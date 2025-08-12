pipeline {
    agent any

    environment {
        BACKEND_REPO = 'https://github.com/repoe2e/desenvolvimento_backend_mysql.git'
       // TESTS_REPO = 'https://github.com/e2e-coders/loja_e2e_api_tests.git'
       // TESTS_DIR = 'api_tests'
    }

    stages {

        stage('Clonar aplicação') {
            steps {
                git branch: 'main', url: "${BACKEND_REPO}"
            }
        }

        stage('Build aplicação') {
            steps {
                dir("${BACKEND_DIR}") {
                    bat 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Start aplicação') {
            steps {
                dir("${BACKEND_DIR}") {
                    bat 'start /B mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"'
                    sleep time: 30, unit: 'SECONDS' // Aguarda a API subir
                }
            }
        }

      /*  stage('Clonar testes') {
            steps {
                dir("${TESTS_DIR}") {
                    git branch: 'main', url: "${TESTS_REPO}"
                }
            }
        }*/
    }

    post {
        always {
            echo "Aplicação no ar"
        }
    }
}
