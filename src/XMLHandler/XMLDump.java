package XMLHandler;

import Helpers.Element;
import Interfaces.Observable;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class XMLDump {

    public static void packageXML(Element[] elements, String path) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer;

        writer = new PrintWriter(path, "UTF-8");

        String output = "<Data>\n";

        for (int i = 0; i < elements.length; i++) {
            if (elements[i] instanceof Observable) {
                output += ((Observable) elements[i]).getObservedValues();
                output += "\n";
            }
        }

        output += "</Data>";

        writer.write(output);
        writer.flush();
        writer.close();
    }

}