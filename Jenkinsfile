pipeline {
  agent any

  environment {
    BACKEND_DIR  = '.'
    APP_DIR      = 'C:\\apps\\meu-sistema'
    APP_JAR_PATH = "${APP_DIR}\\app.jar"
    SERVICE_NAME = 'meu-sistema'
    APP_PORT     = '8085'
    NSSM         = 'C:\\nssm-2.24\\win64\\nssm.exe'
    
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

    stage('Deploy (copiar jar)') {
      steps {
        dir("${env.BACKEND_DIR}") {
          bat '''
          if not exist target\\*.jar (
            echo [ERRO] Nenhum JAR em target\\*.jar
            exit /b 1
          )
          if not exist "%APP_DIR%" mkdir "%APP_DIR%"
          for /f "delims=" %%i in ('dir /b target\\*.jar') do copy /Y "target\\%%i" "%APP_JAR_PATH%"
          '''
          bat 'dir "%APP_DIR%"'
        }
      }
    }

   stage('Instalar/Atualizar serviço (NSSM)') {
  steps {
    bat '''
    rem === valida se o java esta no PATH do Windows ===
    where java >NUL 2>&1
    if errorlevel 1 (
      echo [ERRO] Java nao encontrado no PATH do Windows. Configure JAVA_HOME e PATH como variaveis de ambiente do SISTEMA.
      exit /b 1
    )

    rem === instala se não existir ===
    sc query "%SERVICE_NAME%" >NUL 2>&1
    if errorlevel 1 (
      echo Instalando servico %SERVICE_NAME% via NSSM...
      "%NSSM%" install %SERVICE_NAME% java
    ) else (
      echo Servico %SERVICE_NAME% ja existe.
    )

    rem === aplica configuracoes SEMPRE ===
    "%NSSM%" set %SERVICE_NAME% AppDirectory "%APP_DIR%"
    "%NSSM%" set %SERVICE_NAME% AppParameters "-jar app.jar --server.port=%APP_PORT%"
    "%NSSM%" set %SERVICE_NAME% AppStdout "%APP_DIR%\\stdout.log"
    "%NSSM%" set %SERVICE_NAME% AppStderr "%APP_DIR%\\stderr.log"
    "%NSSM%" set %SERVICE_NAME% Start SERVICE_AUTO_START
    "%NSSM%" set %SERVICE_NAME% AppRestartDelay 5000
    "%NSSM%" set %SERVICE_NAME% AppExit Default Restart
    "%NSSM%" set %SERVICE_NAME% AppKillProcessTree 1

    echo Parando %SERVICE_NAME% (se estiver rodando)...
    "%NSSM%" stop %SERVICE_NAME% >NUL 2>&1

    rem aguarda ate 20s o servico parar
    for /L %%s in (1,1,20) do (
      sc query "%SERVICE_NAME%" | find /I "STOPPED" >NUL && goto :stopped
      ping -n 2 127.0.0.1 >NUL
    )
    :stopped

    echo Iniciando %SERVICE_NAME%...
    "%NSSM%" start %SERVICE_NAME%"
    '''
  }
}

    stage('Healthcheck') {
      steps {
        timeout(time: 180, unit: 'SECONDS') {
          waitUntil {
            script {
              // 1) Tenta actuator/health
              def ps1 = """
              \$ErrorActionPreference = 'SilentlyContinue'
              \$u = 'http://localhost:${APP_PORT}/actuator/health'
              try {
                \$r = Invoke-WebRequest -Uri \$u -UseBasicParsing -TimeoutSec 5
                if (\$r.StatusCode -eq 200 -and (\$r.Content -match '\"status\"\\s*:\\s*\"UP\"')) { exit 0 }
              } catch {}
              exit 1
              """
              def ok = powershell(script: ps1, returnStatus: true) == 0

              if (!ok) {
                // 2) Fallback: checa raiz (200/401/403/404 aceitos = Tomcat de pé)
                def code = bat(script: "curl -s -o NUL -w %%{http_code} http://localhost:%APP_PORT%/", returnStdout: true).trim()
                echo "HTTP raiz: ${code}"
                ok = (code in ['200','401','403','404'])
              }

              return ok
            }
          }
        }
      }
    }
  }

  post {
    always {
      echo "Logs do serviço (tails):"
      bat 'powershell -Command "if (Test-Path \'%APP_DIR%\\stdout.log\') { Write-Host \'\\n--- stdout.log (tail) ---\'; Get-Content -Path \'%APP_DIR%\\stdout.log\' -Tail 200 }"'
      bat 'powershell -Command "if (Test-Path \'%APP_DIR%\\stderr.log\') { Write-Host \'\\n--- stderr.log (tail) ---\'; Get-Content -Path \'%APP_DIR%\\stderr.log\' -Tail 200 }"'
      bat 'sc query "%SERVICE_NAME%"'
      bat 'netstat -aon | findstr :%APP_PORT% | findstr LISTENING'
    }
    success {
      echo "✅ Deploy concluído. Serviço '%SERVICE_NAME%' ativo em http://localhost:${env.APP_PORT}/"
    }
    failure {
      echo "❌ Falha no deploy. Veja os logs acima (stdout/stderr) e o estado do serviço."
    }
  }
}
