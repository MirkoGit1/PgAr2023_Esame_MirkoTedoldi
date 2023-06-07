package it.mirko.fp.esame.Game;

import it.ayman.fp.lib.Menu;
import it.ayman.fp.lib.Title;
import it.mirko.fp.esame.Main;
import it.mirko.fp.esame.Parsing.Reader;
import it.mirko.fp.esame.PathFinding.RoadMap;
import it.mirko.fp.esame.TamaGolem.Tamagolem;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private static final String END_NODE = "END_NODE";
    private static final String STARTING_MENU_HEADER = "Scegli in quale mondo vuoi essere catapultato!";
    private static final String[] MODE_CHOICES = {"Modalità normale;", "Tamagolem;"};
    private static final String WELCOME_TAMAGOLEM = "Benvenuto in TamaGolem!";
    private static final String MENU_HEADER = "Scegli quale sarà la tua prossima tappa:";
    private static final String[] MAP_CHOICES = {"Mappa 1", "Mappa 2", "Mappa 3"};

    private static final String INITIAL_POINTS= "Attualmente possiedi %d punti\n";
    private static final String STATS_CHANGE = "STATS_CHANGE";
    private static final String STATS_CHANGING = "Caspita! i tuoi hp sono cambiati di %d e il tuo danno è cambiato di %d\n";
    private static final String BATTLE = "BATTLE";
    private static final String DAMAGE_DEALT = "Hai inflitto al nemico %d danni! Ottimo lavoro!\n";
    private static final String DAMAGE_RECIEVED = "Hai subito %d danni! Fai attenzione!\n";
    private static final String CONGRATS = "Congratulazioni! Hai sconfitto il nemico! Ora prosegui con la tua avventura\n";
    private static final String BOSS_MESSAGE = "(Boss)'Mi hai finalmente raggiunto, sciocco avventuriero. Preparati ad affrontare" +
            "la più grande minaccia che tu abbia mai visto!'\n";
    private static final String BOSS_DEFEATED = "Evvai! Il temible Cammo è stato sconfitto!\n";
    private static final String POINTS_EARNED = "Hai guadagnato %d punti! Congratulazioni!\n";
    private static final String DEATH_MESSAGE = "Oh no! Sei stato sconfitto!";
    private static final String TITOLO = "A STRANGE WORLD";
    private static final String ELEMENT_HEADER = "Scegli un elemento dalla lista:";
    private static final String[] ELEMENTS = {"Water", "Fire", "Hearth", "Electro", "Air", "Ether", "Plasma",  "Antimatter", "Light", "Darkness"};
    private static final int ELEMENTS_N = 10;


    private static Player player = null;
    private static int deathCount = 0;
    private static int points = 0;

    public Game(Player player) {
        this.player = player;
    }

    /**
     * Metodo per scegliere la mappa desiderata dall'utente
     * @return l'int relativo alla scelta del path
     */
    public static int choosePath () {

        System.out.printf(INITIAL_POINTS, points);
        Menu fileMenu = new Menu(MENU_HEADER, MAP_CHOICES);

        return fileMenu.choose(true, false) - 1;
    }

    /**
     * Metodo che restituisce l'id del nodo scelto dall'utente
     * @param map mappa in cui si trovano i nodi
     * @param i
     * @return l'id del nodo scelto
     */

    private static int choosePath (RoadMap map, int i){
        List<String> options = new ArrayList<>();
        for(Integer nodeId : map.getNodeAdjagencies().get(i)){
            options.add(nodeId.toString());
        }
        String[] optionsArray = options.toArray(new String[options.size()]);
        Menu menu = new Menu(MENU_HEADER, optionsArray);
        return map.getNodeAdjagencies().get(i).get(menu.choose(true, false) - 1);
    }

    private static void randomStatsChange(Player player){
        int hpModifyer, dmgModifyer;
        Random rand = new Random();
        hpModifyer = rand.nextInt(16) - 5;
        dmgModifyer = rand.nextInt(7) - 3;
        System.out.printf(STATS_CHANGING, hpModifyer, dmgModifyer);

        player.setHp(player.getHp() + hpModifyer);
        player.setDamage(player.getDamage() + dmgModifyer);
    }

    /**
     * Metodo che effettua un unico set di mosse player-nemico, ovvero un solo turno
     * @param player in campo
     * @param enemy in campo
     */
    private static void startTurn(Player player, Enemy enemy){
        int damageDealt = enemy.getHp() - player.getDamage();
        System.out.printf(DAMAGE_DEALT, damageDealt);
        enemy.setHp(enemy.getHp() - player.getDamage());
        //Se il nemico non è gia morto
        if(enemy.getHp() > 0){
            int damageRecieved = player.getHp() - enemy.getDamage();
            player.setHp(damageRecieved);
            System.out.printf(DAMAGE_RECIEVED, damageRecieved);
        }
    }

    /**
     * Metodo che effettua un intero ciclo di battaglia, fino alla morte di uno dei due combattenti
     * @param player in campo
     * @param enemy in campo
     * @return 0 se ha vinto il player, 1 se ha vinto il nemico
     */
    private static int startBattleCycle (Player player, Enemy enemy){
        while(player.getHp() > 0 && enemy.getHp() > 0){
            startTurn(player, enemy);
            if(player.getHp() <= 0)
                return 0;
            else
                return 1;
        }
        return 0;
    }

    //Metodi che cambiano i valori di tamagolem e delle loro pietre
    private static void changeGolemStats(Player player){
        Random rand = new Random();
        int randomNum = rand.nextInt(2);
        if(randomNum == 0){
            for (Tamagolem golem : player.getGolemList()){
                golem.setHp(20);
            }
            else if (randomNum == 1){
                Random rand1 = new Random();
                int randomNum1 = rand.nextInt(player.getGolemList().size());
                player.getGolemList().get(randomNum1).setHp(0);
            }
        }
    }

    private static void changeStoneStats(Balance balance){
        Random rand = new Random();
        int randomNum = rand.nextInt(2);
        if (randomNum == 0) {
            Menu menu = new Menu(ELEMENT_HEADER, ELEMENTS);
            int choice1 = menu.choose(true, true);
            int choice2 = menu.choose(true, true);
            for (int i = 0; i < ELEMENTS_N; i++) {
                if (balance.getBalance()[1][i] != choice2) {
                    balance.getBalance()[choice1][i] -= 1;
                }
                else{
                    balance.getBalance()[choice1][i] += ELEMENTS_N - 2;
                }
            }
        }

        else if(randomNum == 1){
            Menu menu = new Menu(ELEMENT_HEADER, ELEMENTS);
            int choice1 = menu.choose(true, true);
            int choice2 = menu.choose(true, true);
            for (int i = 0; i < ELEMENTS_N; i++) {
                if (balance.getBalance()[1][i] != choice2) {
                    balance.getBalance()[choice1][i] += 1;
                }
                else{
                    balance.getBalance()[choice1][i] -= ELEMENTS_N - 2;
                }
            }
        }
        else if(randomNum == 2){
            for (int i = 0; i < ELEMENTS_N; i++){
                for (int j = 0; j < ELEMENTS_N; j++){
                    balance.getBalance()[i][j] *= 2;
                }
            }
        }
    }

    private static void randomTamaStatsChange (Player player, Balance balance){
        Random rand = new Random();
        int randomNum = rand.nextInt(2);
        if (randomNum == 0){
            changeGolemStats(player);
        }
        else{
            changeStoneStats(balance);
        }
    }

    private static int startGame (RoadMap map){
        //Setto il player e la sua posizione corrente, ovvero al nodo 1
        player = new Player();
        player.setCurrentPosition(1);

        //Fino a quando non mi trovo nel nodo finale, ovvero alla battaglia contro il Golem boss, continuo a procedere nei nodi classici
        while(!map.getNodes().get(player.getCurrentPosition()).getType().equals(END_NODE)){

            //Faccio scegliere al giocatore la prossima posizione da occupare
            player.setCurrentPosition(choosePath(map, player.getCurrentPosition()));

            //Se si tratta di un nodo che cambia le statistiche, agisco richiamando il metodo adatto
            if (map.getNodes().get(player.getCurrentPosition()).getType().equals(STATS_CHANGE)) {
                randomStatsChange(player);
                if(player.getHp() <= 0)
                    return 1;
            }

            //In caso contrario, inizio la battaglia
            else if (map.getNodes().get(player.getCurrentPosition()).getType().equals(BATTLE)){
                Enemy enemy = new Enemy();
                if(startBattleCycle(player, enemy) == 0){
                    System.out.println(CONGRATS);
                }
                else{
                    deathCount++;
                    System.out.println(DEATH_MESSAGE);
                    return 1;
                }
            }
        }
        if (map.getNodes().get(player.getCurrentPosition()).getType().equals(END_NODE)){
            Enemy boss = new Enemy();
            boss.bossGenerator();
            System.out.println(BOSS_MESSAGE);
            if(startBattleCycle(player, boss) ==0){
                System.out.println(BOSS_DEFEATED);
                System.out.printf(POINTS_EARNED, 10);
                if(!map.isAlreadyDefeated()){
                    points += 10;
                    map.setAlreadyDefeated(true);
                }
                return 0;
            }
            else return 1;
        }
        return 0;
    }


    private static int startTamaGame (Player player, Balance balance, RoadMap map){
        player = new Player();
        player.setCurrentPosition(1);

        //Fino a quando non mi trovo nel nodo finale, ovvero alla battaglia contro il boss, continuo a procedere nei nodi classici
        while(!map.getNodes().get(player.getCurrentPosition()).getType().equals(END_NODE)){

            //Faccio scegliere al giocatore la prossima posizione da occupare
            player.setCurrentPosition(choosePath(map, player.getCurrentPosition()));

            //Se si tratta di un nodo che cambia le statistiche, agisco richiamando il metodo adatto
            if (map.getNodes().get(player.getCurrentPosition()).getType().equals(STATS_CHANGE)) {
                randomTamaStatsChange(player, balance);
            }

            //In caso contrario, inizio la battaglia
            else if (map.getNodes().get(player.getCurrentPosition()).getType().equals(BATTLE)){
                Tamagolem tamagolem = new Tamagolem();
                if(startTamaBattleCycle(player, tamagolem) == 0){
                    System.out.println(CONGRATS);
                }
                else{
                    deathCount++;
                    System.out.println(DEATH_MESSAGE);
                    return 1;
                }
            }
        }
        if (map.getNodes().get(player.getCurrentPosition()).getType().equals(END_NODE)){
            Enemy boss = new Enemy();
            boss.bossGenerator();
            System.out.println(BOSS_MESSAGE);
            if(startBattleCycle(player, boss) ==0){
                System.out.println(BOSS_DEFEATED);
                System.out.printf(POINTS_EARNED, 10);
                if(!map.isAlreadyDefeated()){
                    points += 10;
                    map.setAlreadyDefeated(true);
                }
                return 0;
            }
            else return 1;
        }
        return 0;
    }

    /**
     * Scegli la modalità con cui giocare, se normale o tamagolem
     * @return
     * @throws XMLStreamException
     */
    public static void gameInit () throws XMLStreamException {

        System.out.println(Title.createTitle(TITOLO, true));
        while (Game.getDeathCount() < 10) {
            Menu startingMenu = new Menu(STARTING_MENU_HEADER, MODE_CHOICES);
            int choice = startingMenu.choose(true, false) - 1;
            if(choice == 0) {
                RoadMap map = Reader.generateMapFromChoice();
                if (Game.startGame(map) == 0)
                    System.out.println("Hai vinto");
                else
                    System.out.println("Hai perso!");
            }
            else{
                System.out.println(WELCOME_TAMAGOLEM);
                RoadMap map = Reader.generateMapFromChoice();
                Balance balance = new Balance(3, Tamagolem.getHp());
                startTamaGame(player, balance, map);
            }
        }
    }

    public static int getDeathCount() {
        return deathCount;
    }
}
