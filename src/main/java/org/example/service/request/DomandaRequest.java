package org.example.service.request;

public class DomandaRequest {
    private Richiedente richiedente;
    private Domanda domanda;

    // Getters e Setters
    public Richiedente getRichiedente() {
        return richiedente;
    }

    public void setRichiedente(Richiedente richiedente) {
        this.richiedente = richiedente;
    }

    public Domanda getDomanda() {
        return domanda;
    }

    public void setDomanda(Domanda domanda) {
        this.domanda = domanda;
    }

    public class Richiedente {
        private String nome;
        private String cognome;

        // Getters e Setters
        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCognome() {
            return cognome;
        }

        public void setCognome(String cognome) {
            this.cognome = cognome;
        }
    }

    public class Domanda {
        private String descrizione;

        // Getters e Setters
        public String getDescrizione() {
            return descrizione;
        }

        public void setDescrizione(String descrizione) {
            this.descrizione = descrizione;
        }
    }
}