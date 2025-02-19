#!/bin/bash

# Configurazioni
WILDFLY_HOME="./wildfly-26.0.0.Final"
DEPLOY_DIR="$WILDFLY_HOME/standalone/deployments"
WAR_NAME="DemoWildflyCamel-1.0-SNAPSHOT.war"
CONFIG_FILE="standalone-full.xml"

echo "Verifica di eventuali istanze di WildFly in esecuzione..."

# Controllo del sistema operativo
OS="$(uname -s)"
if [[ "$OS" == "Linux" || "$OS" == "Darwin" ]]; then
    # Linux/macOS: usa pgrep e kill
    PIDS=$(pgrep -f "wildfly-26.0.0.Final")
    if [ -n "$PIDS" ]; then
        echo "Terminazione di WildFly in esecuzione..."
        kill -9 $PIDS
        sleep 5
        echo "WildFly terminato."
    else
        echo "Nessuna istanza di WildFly trovata."
    fi
else
    # Windows: trova il processo WildFly e lo termina
    WILDFLY_PID=$(wmic process where "name='java.exe' and CommandLine like '%jboss-modules.jar%'" get ProcessId 2>nul | findstr [0-9])

    if [ -n "$WILDFLY_PID" ]; then
        echo "Terminazione di WildFly su Windows (PID: $WILDFLY_PID)..."
        echo "$WILDFLY_PID" | while read PID; do
            taskkill //PID $PID //F
        done
        sleep 5
        echo "WildFly terminato."
    else
        echo "Nessuna istanza di WildFly trovata."
    fi
fi

# Avvio di WildFly
echo "Avvio di WildFly con configurazione $CONFIG_FILE..."
nohup $WILDFLY_HOME/bin/standalone.sh -c $CONFIG_FILE > wildfly.log 2>&1 &
sleep 10  # Attendi che il server si avvii completamente

# Compilazione del progetto Maven
echo "Compilazione del progetto Maven..."
mvn clean package -DskipTests

# Verifica che il WAR sia stato generato correttamente
if [ ! -f "target/$WAR_NAME" ]; then
    echo "Errore: WAR non trovato. La compilazione potrebbe aver fallito."
    exit 1
fi

# Copia il WAR nella cartella di deploy di WildFly
echo "Deploy del WAR su WildFly..."
cp target/$WAR_NAME $DEPLOY_DIR/

echo "Deploy completato. WildFly sta girando con $CONFIG_FILE!"

# Segue i log in tempo reale
echo "Mostrando i log di WildFly..."
tail -f $WILDFLY_HOME/standalone/log/server.log