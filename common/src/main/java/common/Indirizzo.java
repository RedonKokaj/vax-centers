//NICOLAS GUARINI 745508 VA
//DOMENICO RIZZO 745304 VA

package common;

import java.io.Serializable;

/**
 * Classe che modella le caratteristiche di un indirizzo
 *
 * @author Nicolas Guarini
 * @author Domenico Rizzo
 * @author Redon Kokaj
 * @author Filippo Alzati
 */
public class Indirizzo implements Serializable {
    /**
     * Proprieta' che permette il funzionamento dei processi di serializzazione e deserializzazione degli oggetti di tipo <code>Indirizzo</code>
     */
    private static final long serialVersionUID = -1709094232830800244L;

    /**
     * Qualificatore dell'indirizzo
     */
    private final String qualificatore;

    /**
     * Nome dell'indirizzo
     */
    private final String nome;

    /**
     * Numero civico dell'indirizzo
     */
    private final String numeroCivico;

    /**
     * Comune dell'indirizzo
     */
    private final String comune;

    /**
     * Provincia dell'indirizzo
     */
    private final String provincia;

    /**
     * Codice di avviamento postale (CAP) a 5 cifre dell'indirizzo
     */
    private final String CAP;

    /**
     * Costruttore che salva i parametri nelle proprieta' private della classe
     *
     * @param qualificatore: via / viale / piazza
     * @param nome: nome dell'indirizzo
     * @param numeroCivico: numero civico dell'indirizzo
     * @param comune: comune dell'indirizzo
     * @param provincia: provincia dell'indirizzo
     * @param CAP: codice di avviamento postale di 5 cifre dell'indirizzo
     *
     * @author Nicolas Guarini
     */
    public Indirizzo(String qualificatore, String nome, String numeroCivico, String comune, String provincia, String CAP) {
        this.qualificatore = qualificatore;
        this.nome = nome;
        this.numeroCivico = numeroCivico;
        this.comune = comune;
        this.provincia = provincia;
        this.CAP = CAP;
    }

    /**
     * Costruttore che salva i parametri nelle proprieta' private della classe facendo il parsing di un indirizzo esistente in formato testuale
     *
     * @param indirizzo l'inirizzo del quale effettuare il parsing
     *
     * @author Nicolas Guarini
     */
    public Indirizzo(String indirizzo){
        String[] parts = indirizzo.split(",");
        this.qualificatore = parts[0].split(" ")[0].trim();
        this.numeroCivico = parts[0].split(" ")[parts[0].split(" ").length - 1].trim();
        this.comune = parts[1].trim();
        this.provincia = parts[2].trim();
        this.CAP = parts[3].trim();
        this.nome = parts[0].replace(qualificatore, "").replace(numeroCivico, "").trim();
    }

    /**
     * Permette di accedere a metodi e classi esterne alla proprietÓ privata <code>comune</code>
     *
     * @return  comune dell'indirizzo
     *
     * @author Nicolas Guarini
     */
    public String getComune(){return comune;}

    /**
     * Raggruppa in una stringa le principali informazioni dell'indirizzo
     *
     * @return  string che contine le informazioni principali dell'indirizzo
     *
     * @author Nicolas Guarini
     */
    public String toString(){
        return qualificatore + " " + nome + " " + numeroCivico + ", " + comune + ", " + provincia + ", " + CAP;
    }
}
