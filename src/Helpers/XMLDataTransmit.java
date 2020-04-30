package Helpers;

import Interfaces.Observable;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class XMLDataTransmit {

    public static void packageXML (Element[] elements) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer;

        writer = new PrintWriter("res/toGUI.xml","UTF-8");

        String output = "<Data>\n";

        for (int i = 0; i < elements.length ; i++) {
            if (elements[i] instanceof Observable) {
                output += ((Observable)elements[i]).getObservedValues();
                output += "\n";
            }
        }

        output += "</Data>";

        writer.write(output);
        writer.flush();
        writer.close();
    }

}