package XMLHandler;

public class XMLReaderSim {

    public String[] readXML(String path) throws IOException, SAXException, ParserConfigurationException {

        String output[] = {"test"};

        File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();


        return output;
    }
}
