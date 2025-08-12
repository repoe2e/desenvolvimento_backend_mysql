pipeline {
  agent any

  environment {
    BACKEND_DIR = '.'
    APP_PORT    = '8085'
    HEALTH_PATH = '/actuator/health'   // troque para '/' se não usar actuator
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
          // 1) Descobrir JAR e liberar porta
          bat '''
          for /f "delims=" %%i in ('dir /b target\\*.jar') do set "APP_JAR=target\\%%i"
          if not defined APP_JAR (
            echo ERRO: Nenhum JAR encontrado em target\\*.jar
            exit /b 1
          )

          rem Matar processo que estiver usando a porta (se houver)
          for /f "tokens=5" %%p in ('netstat -aon ^| findstr :%APP_PORT% ^| findstr LISTENING') do (
            echo Encerrando PID %%p que ocupa a porta %APP_PORT%...
            taskkill /PID %%p /F >NUL 2>&1
          )

          echo Iniciando %APP_JAR% na porta %APP_PORT% > boot.log
          start "" /B cmd /c "java -jar %APP_JAR% --server.port=%APP_PORT% >> boot.log 2>&1"
          '''
        }

        // 2) Esperar subir (até 180s) e aceitar 2xx–4xx
        timeout(time: 180, unit: 'SECONDS') {
          waitUntil {
            script {
              def url = "http://localhost:${env.APP_PORT}${env.HEALTH_PATH}"
              // tenta /actuator/health; se 404, tenta raiz
              def code = bat(script: "curl -s -o NUL -w %%{http_code} ${url}", returnStdout: true).trim()
              if (code == '404') {
                code = bat(script: "curl -s -o NUL -w %%{http_code} http://localhost:${env.APP_PORT}/", returnStdout: true).trim()
              }
              echo "HTTP: ${code}"
              // considera OK qualquer resposta que não seja 000 e não seja 5xx
              return (code != '000' && !code.startsWith('5'))
            }
          }
        }
      }
    }

    stage('Mostrar últimas linhas do log') {
      steps {
        dir("${env.BACKEND_DIR}") {
          bat 'powershell -Command "if (Test-Path boot.log) { Get-Content -Path boot.log -Tail 120 } else { Write-Host \\"boot.log não encontrado\\" }"'
        }
      }
    }
  }

  post {
    always {
      // Sempre anexar log ao final do job
      dir("${env.BACKEND_DIR}") {
        bat 'powershell -Command "if (Test-Path boot.log) { Write-Host \'\\n--- boot.log (tail) ---\'; Get-Content -Path boot.log -Tail 200 }"'
      }
    }
    success {
      echo "✅ App no ar em http://localhost:${env.APP_PORT}/ (mantendo em execução)"
    }
    failure {
      echo "❌ Falhou. Veja o boot.log acima e o console para detalhes."
    }
  }
}
