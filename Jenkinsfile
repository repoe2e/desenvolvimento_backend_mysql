pipeline {
  agent any

  environment {
    BACKEND_DIR = '.'
    APP_PORT    = '8080'
  }

  stages {
    stage('Build aplicação (tests + package)') {
      steps {
        dir("${env.BACKEND_DIR}") {
          bat 'mvn -version'
          bat 'mvn clean verify'
          bat 'dir target\\*.jar'
        }
      }
    }

    stage('Start app (bg + log)') {
      steps {
        dir("${env.BACKEND_DIR}") {
          bat '''
          for /f "delims=" %%i in ('dir /b target\\*.jar') do set "APP_JAR=target\\%%i"
          if not defined APP_JAR (
            echo ERRO: Nenhum JAR encontrado em target\\*.jar
            exit /b 1
          )
          echo Iniciando %%APP_JAR%% na porta %APP_PORT% > boot.log
          start "" /B cmd /c "java -jar %%APP_JAR%% --server.port=%APP_PORT% >> boot.log 2>&1"
          '''
        }
        timeout(time: 90, unit: 'SECONDS') {
          waitUntil {
            script {
              def code = bat(script: "curl -s -o NUL -w %%{http_code} http://localhost:%APP_PORT%/", returnStdout: true).trim()
              echo "HTTP: ${code}"
              return (code == '200' || code == '302' || code == '401')
            }
          }
        }
      }
    }

    stage('Mostrar últimas linhas do log') {
      steps {
        dir("${env.BACKEND_DIR}") {
          bat 'powershell -Command "Get-Content -Path boot.log -Tail 80"'
        }
      }
    }
  }

  post {
    success {
      echo "✅ App no ar em http://localhost:${env.APP_PORT}/ (mantendo em execução)"
    }
    failure {
      echo "❌ Falhou. Verifique o boot.log e o console."
    }
  }
}
