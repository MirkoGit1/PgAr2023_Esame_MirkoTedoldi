package it.mirko.fp.esame.Parsing;

import it.ayman.fp.lib.Menu;
import it.mirko.fp.esame.Game.Game;
import it.mirko.fp.esame.PathFinding.Node;
import it.mirko.fp.esame.PathFinding.RoadMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reader {
    private static final String READER_ERROR = "Error during the reader initialization:";
    private static final String INPUT_DIRECTORY_PATH = "./InputData/Mappe.xml";
    private static final String FINE = "FINE";
    private static final String NODO = "NODO";
    private static final String COLLEGAMENTI = "COLLEGAMENTI";
    private static final String COLLEGAMENTO = "COLLEGAMENTO";


    /**
     * Metodo che restituisce la prima mappa data dall'xml
     * @return la roadMap relativa alla mappa scelta
     * @throws XMLStreamException
     */
    private static RoadMap getFirstMap() throws XMLStreamException {
        //Inizializzazione degli oggetti per il parsing di lettura
        XMLInputFactory xmlInputFactory;
        XMLStreamReader nodeXmlReader = null;

        try {
            xmlInputFactory = XMLInputFactory.newInstance();
            nodeXmlReader = xmlInputFactory.createXMLStreamReader(Reader.INPUT_DIRECTORY_PATH, new FileInputStream(Reader.INPUT_DIRECTORY_PATH));
        } catch (Exception e) {
            System.out.println(READER_ERROR);
            System.out.println(e.getMessage());
        }

        //Mappe che andranno riempite leggendo l'xml
        var nodesMap = new HashMap<Integer, Node>();
        var nodeRoutesMap = new HashMap<Integer, List<Integer>>();
        List <Integer> linkList = new ArrayList<>();

        //variabile che tiene conto dell'id del nodo in cui mi trovo, utile per tenere in memoria il nodo corrente
        int currentId = 0;

        do {
            assert nodeXmlReader != null;
            nodeXmlReader.next();
            //Salvo i dati solamente se si tratta di uno start element
            if (nodeXmlReader.getEventType() == XMLEvent.START_ELEMENT) {
                String tag = nodeXmlReader.getLocalName();
                switch (tag) {
                    //Nel caso sia uno start element e un tag "city", creo la nuova città (nodo) con i dati forniti
                    case (NODO) -> currentId = Integer.parseInt(nodeXmlReader.getAttributeValue(0));
                    //Nel caso sia uno start element con tag "link", devo aggiungere il collegamento al nodo
                    //corrispondente al currentId
                    case (COLLEGAMENTO) -> //Se si trova su un nodo collegamento, salvo l'id del collegamento nella lista
                            linkList.add(Integer.parseInt(nodeXmlReader.getText()));
                }
            }
            else if(nodeXmlReader.getEventType() == XMLEvent.END_ELEMENT){
                nodeRoutesMap.put(currentId, linkList);
                //Una volta creato il nuovo nodo, lo aggiungo alla nodesMap, assieme al suo id
                //e ad un numero casuale, che corrisponde al tipo del nodo corrente
                int typeNumber = (int) (Math.random() * 2);
                Node node = new Node(typeNumber);
                nodesMap.put(currentId, node);
                nodeXmlReader.next();
                nodeXmlReader.next();
            }
        } while (!nodeXmlReader.getLocalName().equals(FINE));

        return new RoadMap(nodesMap, nodeRoutesMap);
    }

    /**
     * Metodo che restituisce la seconda mappa dell'xml
     * @return la roadMap relativa alla seconda mappa
     * @throws XMLStreamException
     */
    private static RoadMap getSecondMap() throws XMLStreamException {
        //Inizializzazione degli oggetti per il parsing di lettura
        XMLInputFactory xmlInputFactory;
        XMLStreamReader nodeXmlReader = null;

        try {
            xmlInputFactory = XMLInputFactory.newInstance();
            nodeXmlReader = xmlInputFactory.createXMLStreamReader(Reader.INPUT_DIRECTORY_PATH, new FileInputStream(Reader.INPUT_DIRECTORY_PATH));
        } catch (Exception e) {
            System.out.println(READER_ERROR);
            System.out.println(e.getMessage());
        }
        do{
            assert nodeXmlReader != null;
            nodeXmlReader.next();
        }while(nodeXmlReader.getEventType() != XMLEvent.END_ELEMENT && !nodeXmlReader.getLocalName().equals("MAPPA"));

        var nodesMap = new HashMap<Integer, Node>();
        var nodeRoutesMap = new HashMap<Integer, List<Integer>>();
        List <Integer> linkList = new ArrayList<Integer>();

        //variabile che tiene conto dell'id del nodo in cui mi trovo, utile per tenere in memoria il nodo corrente
        int currentId = 0;

        do {
            nodeXmlReader.next();
            //Salvo i dati solamente se si tratta di uno start element
            if (nodeXmlReader.getEventType() == XMLEvent.START_ELEMENT) {
                String tag = nodeXmlReader.getLocalName();
                switch (tag) {
                    //Nel caso sia uno start element e un tag "city", creo la nuova città (nodo) con i dati forniti
                    case (NODO) -> {
                        currentId = Integer.parseInt(nodeXmlReader.getAttributeValue(0));
                    }
                    //Nel caso sia uno start element con tag "link", devo aggiungere il collegamento al nodo
                    //corrispondente al currentId
                    case (COLLEGAMENTO) -> {
                        //Se si trova su un nodo collegamento, salvo l'id del collegamento nella lista
                        linkList.add(Integer.parseInt(nodeXmlReader.getText()));
                    }
                }
            }
            else if(nodeXmlReader.getLocalName().equals(COLLEGAMENTI) && nodeXmlReader.getEventType() == XMLEvent.END_ELEMENT){
                nodeRoutesMap.put(currentId, linkList);
                int link = Integer.parseInt(nodeXmlReader.getText());
                //Una volta creato il nuovo nodo, lo aggiungo alla nodesMap, assieme al suo id
                //e ad un numero casuale, che corrisponde al tipo del nodo corrente
                int typeNumber = (int) (Math.random() * 2);
                Node node = new Node(typeNumber);
                nodesMap.put(currentId, node);
            }
        } while (!nodeXmlReader.getLocalName().equals(FINE));

        return new RoadMap(nodesMap, nodeRoutesMap);
    }


    /**
     * Generatore della mappa di default
     * @return la mappa di default
     */
    private static RoadMap defaultMapGenerator(){
        //Dichiaro gli attributi necessari per generare la nuova mappa
        var nodesMap = new HashMap<Integer, Node>();
        var nodeRoutesMap = new HashMap<Integer, List<Integer>>();
        List <Integer> linkList = new ArrayList<Integer>();

        //Aggiungo il primo nodo
        int currentId = 1;
        nodesMap.put(currentId, new Node(2));
        linkList.add(2);

        nodeRoutesMap.put(currentId, linkList);

        linkList.clear();
        currentId++;

        //Aggiungo il secondo nodo
        nodesMap.put(currentId, Node.nodeGenerator());
        linkList.add(1);
        linkList.add(4);
        linkList.add(3);

        nodeRoutesMap.put(currentId, linkList);

        linkList.clear();
        currentId++;

        //Aggiungo il terzo nodo
        nodesMap.put(currentId, Node.nodeGenerator());
        linkList.add(2);
        linkList.add(5);

        nodeRoutesMap.put(currentId, linkList);

        linkList.clear();
        currentId++;

        //Aggiungo il quarto nodo
        nodesMap.put(currentId, Node.nodeGenerator());
        linkList.add(2);
        linkList.add(5);

        nodeRoutesMap.put(currentId, linkList);

        linkList.clear();
        currentId++;

        //Aggiungo il quinto nodo
        nodesMap.put(currentId, Node.nodeGenerator());
        linkList.add(3);
        linkList.add(4);
        linkList.add(6);

        nodeRoutesMap.put(currentId, linkList);

        linkList.clear();
        currentId++;

        nodesMap.put(currentId, new Node(3));
        linkList.add(2);

        nodeRoutesMap.put(currentId, linkList);

        return new RoadMap(nodesMap, nodeRoutesMap);
    }

    /**
     * Metodo che genera la roadMap in base alla mappa scelta dal player
     * @return a roadMap richiesta
     * @throws XMLStreamException
     */
    public static RoadMap generateMapFromChoice () throws XMLStreamException {
        int menuChoice = Game.choosePath();
        switch (menuChoice + 1) {
            case 1 -> {
                return defaultMapGenerator();
            }
            case 2 -> {
                return getFirstMap();
            }
            case 3 -> {
                return getSecondMap();
            }
            default -> System.out.println("Invalid choice.");
        }
        return null;
    }
}
