package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class ProgramCodeParser {

    private ArrayList<String> programData = new ArrayList<>();
    private ArrayList<String> data = new ArrayList<>();

    private int offset = 0;

    //Parses a given File .LST File
    public int[] parse(String location) {
        int[] retData;

        readFile(location);
        parseFile();


        retData = convert();

        return retData;
    }

    //Reads file an puts it line by line
    private void readFile(String location) {

        File file = new File(location);
        Scanner scanner = null;

        try {
            //IDEA wanted to have try/catch here, so I made it
            scanner = new Scanner(file, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert scanner != null;
        while (scanner.hasNextLine()) {
            programData.add(scanner.nextLine());
        }
        scanner.close();
    }


    private void parseFile() {
        boolean offsetFound = false;
        String instructions, code, tmpOrg;

        //Instructions: Lines and Code,Code: Code without lines
        //Get line numbers and hex code
        for (String programDatum : programData) {

            instructions = programDatum;

            //Calc offset
            if (!offsetFound) {
                offset++;
            }


            //Test for ORG
            if (instructions.contains("org ")) {
                tmpOrg = instructions.split("org ")[1];
                tmpOrg = tmpOrg.replaceAll("[Hh]", "");          //Replace H/h in Hex values like 100H
                int org = Integer.parseInt(tmpOrg, 16);

                //Fills Spaces with NOP operations
                while (data.size() < org) {
                    data.add("0");
                }
            } else {
                instructions = instructions.split(" {11}")[0];

                //Add the Code to the list
                if (!instructions.equals("")) {
                    code = instructions.split(" ")[1];
                    data.add(code);

                    //Disable offsetCounter
                    if (offsetFound == false) {
                        offsetFound = true;
                    }
                }
            }
        }
    }


    //Converts the Data-list to an int[] Array
    private int[] convert() {
        int[] retData = new int[data.size()];

        for (int i = 0; i < retData.length; i++) {
            retData[i] = Integer.parseInt(data.get(i), 16);
        }
        return retData;
    }

    public String[] getProgramData() {
        String[] programDataString = new String[programData.size()];

        for (int i = 0; i < programDataString.length; i++) {
            programDataString[i] = programData.get(i);
        }

        return programDataString;
    }

    public int getOffset() {
        return (offset - 1);
    }
}