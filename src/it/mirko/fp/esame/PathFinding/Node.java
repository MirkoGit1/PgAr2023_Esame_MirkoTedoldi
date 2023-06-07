package it.mirko.fp.esame.PathFinding;

public class Node {
    private final String type;
    private final boolean alreadyVisited = false;

    public Node(int num) {
        this.type = getTypeFromNum(num);
    }

    private static String getTypeFromNum (int n){
        if(n == 0){
            return NodeType.BATTLE.toString();
        }
        else if (n == 1){
            return NodeType.STATS_CHANGE.toString();
        }
        else if(n == 2){
            return NodeType.STARTING_NODE.toString();
        }
        else if(n == 3){
            return NodeType.END_NODE.toString();
        }
        return null;
    }

    /**
     * Generatore randomico di nodi, ovvero genera casualmento lo stato di nodo-combattimento o nodo-cambioStats
     * @return il nodo generato
     */
    public static Node nodeGenerator(){
        int typeNumber = (int) (Math.random() * 2);
        Node node = new Node(typeNumber);
        return node;
    }
}
