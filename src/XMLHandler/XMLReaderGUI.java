package XMLHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLReaderGUI {

    public static void readXML(String path) throws IOException, SAXException, ParserConfigurationException {
        String name;

        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();


        NodeList nodeList = doc.getElementsByTagName("NODE");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element;
                element = (Element) node;
                name = element.getAttribute("name");

                NodeList nList = element.getElementsByTagName("v");

                switch (name) {
                    case "PC":
                        toPC(nList);
                        break;
                    case "STACK":
                        toSTACK(nList);
                        break;
                    case "WREGISTER":
                        toWREGISTER(nList);
                        break;
                    case "RAM":
                        toRAM(nList);
                        break;
                    case "PORT":
                        toPorts(nList);
                    case "OFFSET":
                        toOffset(nList);
                    default:
                        System.err.println("XMLReaderGUI: Some unknown NODES");
                        break;
                }

            }
        }
    }


    private static void toPC(NodeList nodeList) {
        Node node = nodeList.item(0);
        Element e = (Element) node;

        //TODO: Push into GUI_PC
        System.out.println("PC: IDX: " + e.getAttribute("val"));
    }

    private static void toSTACK(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element e = (Element) node;

            //TODO: Push into GUI_STACK
            System.out.println("STACK: VAL: " + e.getAttribute("val"));
        }
    }

    private static void toWREGISTER(NodeList nodeList) {
        Node node = nodeList.item(0);
        Element e = (Element) node;

        //TODO: Push into GUI_WREGISTER
        System.out.println("WREG: IDX: " + e.getAttribute("val"));
    }

    private static void toRAM(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element e = (Element) node;

            //TODO: Push into GUI_RAM
            System.out.println("RAM: IDX: " + e.getAttribute("idx"));
            System.out.println("RAM: VAL: " + e.getAttribute("val"));
        }
    }

    private static void toPorts(NodeList nodeList) {
        //TODO: ADD PORTS
    }

    private static void toOffset(NodeList nodeList) {
        Node node = nodeList.item(0);
        Element e = (Element) node;

        //TODO: Push into GUI_WREGISTER
        System.out.println("OFFSET: " + e.getAttribute("val"));
    }

}
