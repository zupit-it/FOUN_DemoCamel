# POC Demo Camel Azure

> Progetto dimostrativo basato su Apache Camel per l'integrazione con Azure Service Bus, progettato per orchestrare il flusso di gestione della protocollazione attraverso rotte Camel.

## Tecnologie utilizzate

- Java 17
- Spring Boot
- Apache Camel
- Azure Service Bus SDK
- Maven

## Setup del progetto

### Prerequisiti

- Java 17 o superiore
- Maven 3.8+
- Connessione ad Azure Service Bus

### Prerequisiti ambiente Azure
L'ambiente Azure dovrà contenere le seguenti rotte per permettere il corretto funzionamento

siag-sa-03-procedimenti-domandapresentata-request-queue
siag-sa-03-procedimenti-creafascicolo-request-queue
siag-sa-03-procedimenti-creafascicolo-reply-queue
siag-sa-03-procedimenti-creaprotocollo-request-queue
siag-sa-03-procedimenti-creaprotocollo-reply-queue
siag-sa-03-procedimenti-depositadocumento-request-queue
siag-sa-03-procedimenti-depositaallegato-request-queue
siag-sa-03-procedimenti-domandaregistrata-request-queue

### Come connettersi all'ambiente Azure
La connessione all'ambiente Azure è automatizzata: basterà riportare la 'Stringa di connessione primaria' nel file application.properties

## Come testare - test base
E' predisposto un endpoint POST '/api/domande' che accetta un body di questo tipo

{
  "richiedente": {
    "nome": "Giovanni", 
    "cognome": "Pepe"
  }, 
  "domanda": {
    "descrizione": "Domanda 123"  
  }    
}

Questo innescherà una sola iterazione del flusso di protocollazione come richiesto da requisito

## Come testare - stress test
Per procedere con gli stress test sarà necessario valorizzare nell'application.properties i seguenti valori

test.frequency che indica la frequenza di invio dei messaggi 'Domanda presentata' sulla coda in millisecondi
test.request.max che indica il numero di richieste inviate durante lo stress test

## Avvio
All'interno del repository appena clonato eseguire i comandi

```bash
mvn clean install
mvn spring-boot:run
