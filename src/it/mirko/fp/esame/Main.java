package it.mirko.fp.esame;

import it.mirko.fp.esame.Parsing.Reader;
import it.mirko.fp.esame.PathFinding.RoadMap;

import javax.xml.stream.XMLStreamException;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        RoadMap map = Reader.generateMapFromChoice();
    }
}
