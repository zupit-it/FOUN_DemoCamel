#!/bin/bash

# Configurazioni
MAIN_CLASS="org.example.routes.JmsSender"
WAR_NAME="DemoWildflyCamel-1.0-SNAPSHOT.war"

# Verifica se il WAR esiste
if [ ! -f "target/$WAR_NAME" ]; then
    echo "Errore: WAR non trovato. Eseguire prima 'start_wildfly.sh'."
    exit 1
fi

# Avvia la classe specificata con il classpath corretto
echo "Eseguendo $MAIN_CLASS..."
mvn exec:java -Dexec.mainClass="$MAIN_CLASS"