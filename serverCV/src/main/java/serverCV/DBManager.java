//NICOLAS GUARINI 745508 VA
//DOMENICO RIZZO 745304 VA

package serverCV;

import java.sql.*;

/**
 * La classe <code>DBManager</code>, implementando il pattern Singleton, inizializza il database e permette alle altre classi di accedervi
 *
 * @author Nicolas Guarini
 * @author Domenico Rizzo
 */
public class DBManager {
    /**
     * Oggetto per poter gestire la connessione ed effettuare query al database
     */
    Connection connection;

    /**
     * Indirizzo dell'host del server Postgres
     */
    String host;

    /**
     * Porta del server Postgres
     */
    String port;

    /**
     * Username per accedere al server Postgres
     */
    String username;

    /**
     * Password per accedere al server Postgres
     */
    String password;

    /**
     * Nome del database dell'applicazione. Se questo non esiste gi� verr� creato.
     */
    final String dbName = "dbcv";

    /**
     * Oggetto <code>DBManager</code> necessario a implementare il pattern Singleton, rappresenta l'istanza corrente della classe.
     */
    static DBManager DBManagerInstance = null;

    /**
     * Metodo che ritorna l'istanza della classe. Se non � ancora stata istanziata lo fa, altrimenti ritorna l'istanza esistente.
     *
     * @return l'istanza corrente della classe <code>DBManager</code>
     *
     * @author Nicolas Guarini
     */
    public static DBManager getInstance(){
        if(DBManagerInstance == null) DBManagerInstance = new DBManager();
        return DBManagerInstance;
    }

    /**
     * Metodo che realizza la connessione al server Postgres tramite il driver JDBC
     *
     * @param host indirizzo del server Postgres
     * @param port porta del server Postgres
     * @param username username per accedere al server Postgres
     * @param password password per accedere al server Postgres
     * @return esito della connessione tramite l'enumerazione {@link ConnectionResult}
     *
     * @author Nicolas Guarini
     */
    public ConnectionResult connect(String host, String port, String username, String password) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

        try{
            connection = DriverManager.getConnection(url, username, password);
            setupTables();
            return ConnectionResult.OK;
        } catch (SQLException exception) {
            exception.printStackTrace();
            if(exception.getMessage().contains(dbName)){
                createDB();
                return  ConnectionResult.DB_CREATED;
            }else return ConnectionResult.FAILED;
        }
    }

    /**
     * Metodo che si occupa di creare il database e le relative tabelle nel caso in cui quest'ultimo non esista gi�
     *
     * @author Nicolas Guarini
     */
    private void createDB(){
        try{
            Connection connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/", username, password);
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE " + dbName);
            this.connection = DriverManager.getConnection("jdbc:postgresql:" + dbName, username, password);
            setupTables();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Metodo che si occupa della creazione, nel caso in cui non esistano gi�, delle tabelle del database
     *
     * @throws SQLException eccezione che da informazioni sull'errore o sugli errori che sono avvenuti
     *
     * @author Nicolas Guarini
     */
    private void setupTables() throws SQLException{
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS CentriVaccinali(\n" +
                "    idCentroVaccinale VARCHAR(5) PRIMARY KEY NOT NULL,\n" +
                "    nomeCentroVaccinale VARCHAR(50) NOT NULL,\n" +
                "    tipologia VARCHAR(20) NOT NULL,\n" +
                "    indirizzo VARCHAR(100) NOT NULL\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS Vaccinazioni(\n" +
                "    idVaccinazione VARCHAR(16) PRIMARY KEY NOT NULL,\n" +
                "    nomeVaccinato VARCHAR(50) NOT NULL, \n" +
                "    cognomeVaccinato VARCHAR(50)NOT NULL, \n" +
                "    cfVaccinato VARCHAR(16) UNIQUE NOT NULL,\n" +
                "    dataVaccinazione DATE NOT NULL,\n" +
                "    idCentroVaccinale VARCHAR(5) NOT NULL,\n" +
                "    nomeVaccino VARCHAR(50) NOT NULL,\n" +
                "    registrato BOOLEAN NOT NULL,\n" +
                "    username VARCHAR(100) NULL,\n" +
                "    email VARCHAR(100) NULL,\n" +
                "    password VARCHAR(64) NULL,\n" +
                "    CONSTRAINT fk_centrivaccinali\n" +
                "        FOREIGN KEY (idCentroVaccinale)\n" +
                "        REFERENCES CentriVaccinali(idCentroVaccinale)\n" +
                "        ON UPDATE CASCADE\n" +
                "        ON DELETE RESTRICT\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS EventiAvversi(\n" +
                "    nomeEventoAvverso VARCHAR(100) NOT NULL,\n" +
                "    severita SMALLINT NOT NULL,\n" +
                "    noteAggiuntive VARCHAR(5000) NULL,\n" +
                "    idVaccinazione VARCHAR(16) NOT NULL,\n" +
                "    PRIMARY KEY (nomeEventoAvverso, idVaccinazione),\n" +
                "    CONSTRAINT fk_vaccinazione \n" +
                "        FOREIGN KEY(idVaccinazione)\n" +
                "        REFERENCES Vaccinazioni(idVaccinazione)\n" +
                "        ON DELETE CASCADE\n" +
                "        ON UPDATE CASCADE\n" +
                ");";
        statement.execute(query);
    }

    /**
     * Metodo che genera una discreta quantit� di dati di test per poter testare l'applicazione senza doverli inserire manualmente.
     *
     * @throws SQLException eccezione che fornisce informazioni su errori riguardanti il database
     *
     * @author Nicolas Guarini
     */
    public void insertDataset() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "DELETE FROM eventiavversi;\n" +
                "DELETE FROM vaccinazioni;\n" +
                "DELETE FROM centrivaccinali;\n" +
                "\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00001', 'Schiranna', 'Hub', 'Piazza Roma 1, Varese, VA, 21100');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00002', 'Ospedale Santa Maria Goretti', 'Ospedaliero', 'Via Lucia Scaravelli 1, Latina, LT, 04100');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00003', 'Policlinico Gemelli', 'Ospedaliero', 'Via della Pineta Sacchetti 217, Roma, RM, 00168');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00004', 'Policlinico Umberto I', 'Ospedaliero', 'Viale Regina Elena 324, Roma, RM, 00161');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00005', 'Humanitas Mirasole', 'Hub', 'Via Alessandro Manzoni 56, Rozzano, MI, 20089');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00006', 'ASST Vimercate', 'Ospedaliero', 'Via Santi Cosma e Damiano 10, Vimercate, MB, 20871');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00007', 'ASST Sette Laghi', 'Ospedaliero', 'Viale Luigi Borri 57, Varese, VA, 21100');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00008', 'Unifarm', 'Hub', 'Via Provina 3, Ravina, TN, 38123');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00009', 'ASL Novara', 'Aziendale', 'Viale Roma 7, Novara, NO, 28100');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00010', 'Consorziale Policlinico', 'Ospedaliero', 'Piazza Giulio Cesare 11, Bari, BA, 70124');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00011', 'AOU di Sassari', 'Aziendale', 'Viale S. Pietro 43, Sassari, SS, 07100');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00012', 'ISMETT Palermo', 'Aziendale', 'Via Ernesto Tricomi 5, Palermo, PA, 90127');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00013', 'Azienda Ospedaliera di Perugia', 'Ospedaliero', 'Piazza Giorgio Meneghini 3, Perugia, PG, 06129');\n" +
                "INSERT INTO centrivaccinali(idcentrovaccinale, nomecentrovaccinale, tipologia, indirizzo) \n" +
                "    VALUES('00014', 'Ospedale di Vicenza', 'Ospedaliero', 'Via Ferdinando Rodolfi 37, Vicenza, VI, 36100');\n" +
                "\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password) \n" +
                "    VALUES('0000100000000001', 'Marco', 'Verdi', 'VRDMRC66E01F205F', '2022-08-18', '00001', 'Pfizer', true, 'marcoverdi', 'marcoverdi1997@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0001400000000001', 'Luca', 'Morosi', 'MRSLCU66E01F205G', '2022-08-03', '00014', 'Moderna', true, 'lucamorosi', 'lucamorosi24@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000800000000001', 'Nicola', 'Marconi', 'MRCNLS66E01F839X', '2022-08-20', '00008', 'Astra Zeneca', true, 'nicolamarconi', 'nicolamarconi.py@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000300000000001', 'Emanuele', 'Portese', 'PRTMNL56D05H501G', '2022-08-20', '00003', 'Pfizer', true, 'emanueleportese', 'portesema01@outlook.it', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000300000000002', 'Manuel', 'Guzzanti', 'GZZMNL56H02F839N', '2022-08-19', '00003', 'Moderna', true, 'manuelguzzanti', 'guzzantino69@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000300000000003', 'Pietro', 'Esposito', 'SPSPTR43H03F704W', '2022-08-19', '00003', 'Pfizer', true, 'pietroesposito', 'pietroesposito111@libero.it', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000300000000004', 'Mario', 'Rossi', 'RSSMRA43D43L379E', '2022-07-23', '00003', 'Moderna', true, 'mariorossi', 'rossimarione@hotmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000700000000001', 'Elena', 'Pellegrini', 'PLLLNE92L53D869L', '2022-06-12', '00007', 'Astra Zeneca', true, 'elenapellegrini', 'elenapelle04@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000900000000001', 'Fabio', 'Colombo', 'CLMFBA15S04H648G', '2022-08-20', '00009', 'Moderna', true, 'fabiocolombo', 'colombofabio84@libero.it', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0001000000000001', 'Edoardo', 'De Marco', 'DMRDRD01D12L021S', '2022-08-17', '00010', 'Pfizer', true, 'edoardodemarco', 'edodemarch00@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000900000000002', 'Yassin', 'Muhammad', 'YSSMMM01L07C115F', '2022-08-10', '00009', 'Moderna', true, 'yassinmuhammad', 'muhammadyassin@libero.it', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000100000000002', 'Nicolas', 'Guarini', 'GRNNLS01P04L682K', '2022-08-30', '00001', 'Moderna', true, 'nicolasguarini', 'nikiguarincia@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000100000000003', 'Martina', 'Smeraldi', 'SMRMTN98E44A662S', '2022-08-18', '00001', 'Astra Zeneca', true, 'martinasmeraldi', 'martysmera0@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0001200000000001', 'Paolo', 'Bonolis', 'BNLPLA82L05H501Q', '2022-03-02', '00012', 'Moderna', true, 'paolobonolis', 'info@avantiunaltro.it', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000400000000001', 'Ellie', 'Goulding', 'GLDLLE82D45G395C', '2022-08-11', '00004', 'Astra Zeneca', true, 'elliegoulding', 'ellie999@yahoo.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000500000000001', 'Giuseppe', 'Draghi', 'DRGGPP82D04G479N', '2022-08-08', '00005', 'Moderna', true, 'giuseppedraghi', 'dragonerosso01@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000500000000002', 'Marco', 'Beri', 'BREMRC65R44A345J', '2022-08-09', '00005', 'Moderna', true, 'marcoberi', 'marcoberi123@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000600000000001', 'Fabio', 'Guarini', 'GRNFBA65M06H147I', '2022-08-10', '00006', 'Pfizer', true, 'fabioguarini', 'fabietto09@gmail.com', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "INSERT INTO vaccinazioni(idvaccinazione, nomevaccinato, cognomevaccinato, cfvaccinato, datavaccinazione, idcentrovaccinale, nomevaccino, registrato, username, email, password)\n" +
                "    VALUES('0000600000000002', 'Emma', 'Rossi', 'RSSMME65H06D242L', '2022-08-14', '00006', 'Pfizer', true, 'emmarossi', 'rossiemma1@libero.it', 'vXkubgP5yrztVQXkHwUlDLMnpYPOYrp6QTyFsXkMy0c=');\n" +
                "\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Male al braccio', 2, 'Dolore ai muscoli nella zona della puntura', '0000100000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Febbre', 3, '', '0000500000000002');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Spossatezza', 3, '', '0000600000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Male alle ossa', 5, 'Non riesco a stare in piedi', '0000300000000002');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Mal di testa', 4, '', '0000900000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Febbre', 5, 'Febbre a 40 gradi tutta la notte', '0000300000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Male al braccio', 4, '', '0000800000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Spossatezza', 2, '', '0000500000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Febbre', 1, '', '0000100000000002');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Male al braccio', 3, '', '0000100000000002');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Mal di testa', 5, '', '0000700000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Febbre', 3, '', '0001400000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Nausea', 4, '', '0000100000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Nausea', 2, '', '0000300000000004');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Vertigini', 4, '', '0000500000000002');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Febbre', 5, '', '0001200000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Vomito', 5, '', '0000100000000003');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Svenimenti', 5, '', '0000300000000003');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Mal di testa', 1, '', '0000600000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Febbre', 3, '', '0000500000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Febbre', 2, '', '0000900000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Dolore muscolare', 3, '', '0000700000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Nausea', 4, '', '0000500000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Mal di stomaco', 2, '', '0000900000000002');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Vertigini', 4, '', '0000700000000001');\n" +
                "INSERT INTO eventiavversi(nomeeventoavverso, severita, noteaggiuntive, idvaccinazione)\n" +
                "    VALUES('Febbre', 1, '', '0000600000000002');\n";
        statement.executeUpdate(query);
    }
}

