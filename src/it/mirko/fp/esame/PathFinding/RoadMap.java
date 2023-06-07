package it.mirko.fp.esame.PathFinding;

import java.util.*;

public class RoadMap {

    private final Map<Integer, Node> nodes;
    private final Map<Integer, List<Integer>> nodeAdjagencies;

    public RoadMap(Map<Integer, Node> nodes, Map<Integer, List<Integer>> nodeAdjagencies) {
        this.nodes = nodes;
        this.nodeAdjagencies = nodeAdjagencies;
    }

    @Override
    public String toString() {
        return "RoadMap{" +
                "nodes=" + nodes +
                ", nodeAdjagencies=" + nodeAdjagencies +
                '}';
    }
}
