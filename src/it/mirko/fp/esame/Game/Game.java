package it.mirko.fp.esame.Game;

import it.ayman.fp.lib.Menu;
import it.mirko.fp.esame.Main;
import it.mirko.fp.esame.PathFinding.RoadMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private static final String END_NODE = "END_NODE";
    private static final String MENU_HEADER = "Scegli quale sarà la tua prossima tappa:";
    private static final String STATS_CHANGE = "STATS_CHANGE";
    private static final String BATTLE = "BATTLE";
    private static final String DAMAGE_DEALT = "Hai inflitto al nemico %d danni! Ottimo lavoro!";
    private static final String DAMAGE_RECIEVED = "Hai subito %d danni! Fai attenzione!";
    private static final String CONGRATS = "Congratulazioni! Hai sconfitto il nemico! Ora prosegui con la tua avventura";


    private static Player player = null;
    private static int deathCount = 0;
    private static int points = 0;

    public Game(Player player) {
        this.player = player;
    }

    /**
     * Metodo che restituisce l'id del nodo scelto dall'utente
     * @param map mappa in cui si trovano i nodi
     * @param i
     * @return l'id del nodo scelto
     */

    private static int choosePath (RoadMap map, int i){
        List<String> options = new ArrayList<>();
        for (int j = 0; j < map.getNodeAdjagencies().size(); j++){
            options.add(map.getNodeAdjagencies().get(i).get(j).toString());
        }
        String[] optionsArray = options.toArray(new String[options.size()]);
        Menu menu = new Menu(MENU_HEADER, optionsArray);
        return menu.choose(true, false) - 1;
    }

    private static void randomStatsChange(Player player){
        int hpModifyer, dmgModifyer;
        Random rand = new Random();
        hpModifyer = rand.nextInt(16) - 5;
        dmgModifyer = rand.nextInt(7) - 3;
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
        enemy.setHp(enemy.getHp() - player.getDamage());
        System.out.printf(DAMAGE_DEALT, damageDealt);
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
        }
        if(player.getHp() <= 0)
            return 0;
        else
            return 1;
    }

    public static int startGame (RoadMap map){
        //Setto il player e la sua posizione corrente, ovvero al nodo 1
        player = new Player();
        player.setCurrentPosition(1);

        //Fino a quando non mi trovo nel nodo finale, ovvero alla battaglia contro il boss, continuo a procedere nei nodi classici
        while(!map.getNodes().get(player.getCurrentPosition()).getType().equals(END_NODE)){

            //Faccio scegliere al giocatore la prossima posizione da occupare
            player.setCurrentPosition(choosePath(map, player.getCurrentPosition()));

            //Se si tratta di un nodo che cambia le statistiche, agisco richiamando il metodo adatto
            if (map.getNodes().get(player.getCurrentPosition()).getType().equals(STATS_CHANGE))
                randomStatsChange(player);

            //In caso contrario, inizio la battaglia
            else if (map.getNodes().get(player.getCurrentPosition()).getType().equals(BATTLE)){
                Enemy enemy = new Enemy();
                if(startBattleCycle(player, enemy) == 0){
                    System.out.println(CONGRATS);
                }
                else{
                    return 1;
                }
            }
        }
        if (map.getNodes().get(player.getCurrentPosition()).getType().equals(END_NODE)){

            startBossFight(player)
        }
    }
}
