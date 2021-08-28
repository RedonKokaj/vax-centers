//NICOLAS GUARINI 745508 VA
//FILIPPO ALZATI 745495 VA
//REDON KOKAJ 744959 VA
//DOMENICO RIZZO 745304 VA

package centrivaccinali;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

/**
 * Modella le caratteristiche di una vaccinazione
 *
 * @see centrivaccinali.UI.UIRegistraVaccinato
 *
 * @author Nicolas Guarini
 * @author Domenico Rizzo
 * @author Redon Kokaj
 * @author Filippo Alzati
 */
public class Vaccinazione implements Serializable {
    @Serial
    private static final long serialVersionUID = 5737449378412827456L;

    String nome, cognome, cf, id, nomeVaccino;
    Date data;
    CentroVaccinale centroVaccinale;
    LinkedList<EventoAvverso> eventiAvversi = new LinkedList<>();

    /**
     * Costruttore che salva i parametri nelle propriet� private della classe
     *
     * @param nome: nome del vaccinato
     * @param cognome: cognome del vaccinato
     * @param cf: codice fiscale del vaccinato
     * @param id: id univoco vaccinazione di 16 cifre
     * @param data: data della vaccinazione
     * @param centroVaccinale: centro vaccinale dove � stata effettuata la vaccinazione
     * @param nomeVaccino: nome del vaccino somministrato
     *
     * @author Nicolas Guarini
     */
    public Vaccinazione(String nome, String cognome, String cf, String id, Date data, CentroVaccinale centroVaccinale, String nomeVaccino){
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.id = id;
        this.nomeVaccino = nomeVaccino;
        this.centroVaccinale = centroVaccinale;
        this.data = data;
    }

    /**
     * Permette di accedere a metodi e classi esterne alla propriet� privata <code>eventiAvversi</code>
     *
     * @return lista degli eventi avversi
     *
     * @author Nicolas Guarini
     */
    public LinkedList<EventoAvverso> getEventiAvversi(){
        return eventiAvversi;
    }

    /**
     * Permette di accedere a metodi e classi esterne alla propriet� privata <code>centroVaccinale.nome</code>
     *
     * @return nome del centro vaccinale
     *
     * @author Nicolas Guarini
     */
    public String getNomeCentroVaccinale(){ return centroVaccinale.getNome(); }

    /**
     * Permette di accedere a metodi e classi esterne alla propriet� privata <code>id</code>
     *
     * @return id univoco vaccinazione di 16 cifre
     *
     * @author Nicolas Guarini
     */
    public String getIdVaccinazione(){return id;}

    /**
     * Permette di accedere a metodi e classi esterne alla propriet� privata <code>cf</code>
     *
     * @return codice fiscale del vaccinato
     *
     * @author Nicolas Guarini
     */
    public String getCf(){return cf;}
}
