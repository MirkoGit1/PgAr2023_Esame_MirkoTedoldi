package it.mirko.fp.esame.PathFinding;

import java.util.*;

public class RoadMap {

    private final Map<Integer, Node> nodes;
    private final Map<Integer, List<Integer>> nodeAdjagencies;
    private static boolean alreadyDefeated = false;

    public RoadMap(Map<Integer, Node> nodes, Map<Integer, List<Integer>> nodeAdjagencies) {
        this.nodes = nodes;
        this.nodeAdjagencies = nodeAdjagencies;
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public Map<Integer, List<Integer>> getNodeAdjagencies() {
        return nodeAdjagencies;
    }

    public static boolean isAlreadyDefeated() {
        return alreadyDefeated;
    }

    public static void setAlreadyDefeated(boolean alreadyDefeated) {
        RoadMap.alreadyDefeated = alreadyDefeated;
    }

    @Override
    public String toString() {
        return "RoadMap{" +
                "nodes=" + nodes +
                ", nodeAdjagencies=" + nodeAdjagencies +
                '}';
    }
}
