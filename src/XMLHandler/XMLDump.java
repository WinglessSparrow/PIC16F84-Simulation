package XMLHandler;

import Helpers.Element;
import Interfaces.Observable;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class XMLDump {

    private PrintWriter writer;
    private String outout = "";
    public static void packageXML(Element[] elements, String path) {

        String output = "<Data>\n";

        for (int i = 0; i < elements.length; i++) {
            if (elements[i] instanceof Observable) {
                output += ((Observable) elements[i]).getObservedValues();
                output += "\n";
            }
        }

        output += "</Data>";

        write(output, path);
    }

    private static void write(String output, String path) {
        PrintWriter writer = null;
        try {
            //Init Writer
            writer = new PrintWriter(path, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writer.write(output);
        writer.flush();
        writer.close();

    }


}