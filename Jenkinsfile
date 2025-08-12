pipeline {
  agent any

  environment {
    BACKEND_DIR = '.'      // ajuste se o pom.xml estiver em subpasta
    APP_PORT    = '8080'   // porta da app
  }

  stages {
    stage('Build aplicação (tests + package)') {
      steps {
        dir("${env.BACKEND_DIR}") {
          bat 'dir pom.xml'
          bat 'mvn -version'
          bat 'mvn clean verify'   // executa testes e empacota o jar
          bat 'dir target\\*.jar'  // mostra o jar gerado no console
        }
      }
    }

    stage('Start app (bg + log)') {
      steps {
        dir("${env.BACKEND_DIR}") {
          // garante que existe jar e sobe em background gravando log
          bat '''
          for /f "delims=" %%i in ('dir /b target\\*.jar') do set APP_JAR=target\\%%i
          if not defined APP_JAR (
            echo ERRO: Nenhum JAR encontrado em target\\*.jar
            exit /b 1
          )
          echo Iniciando %%APP_JAR%% na porta %APP_PORT% > boot.log
          start "" /B cmd /c "java -jar %%APP_JAR%% --server.port=%APP_PORT% >> boot.log 2>&1"
          '''
        }
        // espera ativa até a app responder (200/302/401)
        timeout(time: 90, unit: 'SECONDS') {
          waitUntil {
            def code = bat(script: "curl -s -o NUL -w %%{http_code} http://localhost:%APP_PORT%/", returnStdout: true).trim()
            echo "HTTP: ${code}"
            return (code == '200' || code == '302' || code == '401')
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
      echo "✅ App no ar em http://localhost:${env.APP_PORT}/ (deixando rodar)"
    }
    failure {
      echo "❌ Falhou. Veja o boot.log acima e o console."
    }
    always {
      // sem taskkill -> mantém a aplicação rodando
    }
  }
}
