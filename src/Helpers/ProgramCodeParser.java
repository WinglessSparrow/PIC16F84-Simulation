package Helpers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class ProgramCodeParser {

    private ArrayList<String> programData = new ArrayList<>();
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<Boolean> pcPresenceData = new ArrayList<>();


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

            //Test for ORG
            if (instructions.contains("org ")) {
                tmpOrg = instructions.split("org ")[1];
                tmpOrg = tmpOrg.replaceAll("[Hh]", "");          //Replace H/h in Hex values like 100H
                tmpOrg = tmpOrg.replaceAll(" ", "");
                int org = Integer.parseInt(tmpOrg, 16);

                //Added false, because otherwise ArrayList is missing one false
                pcPresenceData.add(false);

                //Fills Spaces with NOP operations
                while (data.size() < org) {
                    data.add("0");
                    pcPresenceData.add(false);
                }
            } else {
                instructions = instructions.split(" {11}")[0];

                //Add the Code to the list
                if (!instructions.equals("")) {
                    code = instructions.split(" ")[1];
                    data.add(code);
                    pcPresenceData.add(true);


                } else {
                    pcPresenceData.add(false);
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
        String[] tmpProgramData;

        for (int i = 0; i < programDataString.length; i++) {
            if (pcPresenceData.get(i)) {
                tmpProgramData = programData.get(i).split(" ", 2);
                programDataString[i] = tmpProgramData[1];
            } else {
                programDataString[i] = programData.get(i).substring(4);
            }
            //System.out.println(pcPresenceData.get(i) + "    " + programDataString[i]);
        }

        cutUseless(programDataString);

        return programDataString;
    }

    private void cutUseless(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
//            int count = 0;
//            //search for the begin of the important part
//            try {
//                //goes through string until it encounters lower case character (ASCII from 97 to 122) or ';'
//                //!WARNING! only works good with lst given by our Prof
//                while (!((lines[i].charAt(count) >= 97 && lines[i].charAt(count) <= 122) || (lines[i].charAt(count) == ';'))) {
//                    count++;
//                }
//
//                //using the count index to get the needed String
//                lines[i] = lines[i].substring(count);
//
//                //inserts a TAB if it not a jump point
//                boolean insertTab = !(lines[i].contains("loop") || lines[i].contains("start") || lines[i].contains("ende")
//                        || lines[i].contains("tabelle") || lines[i].contains("links") || lines[i].contains("rechts")
//                        || lines[i].contains("screien") || lines[i].contains("lesen") || lines[i].contains("warte")
//                        && !(lines[i].contains("goto") || lines[i].contains("call")));
//
//                //tabs on places where it matters
//                if (insertTab) {
//                    lines[i] = "\t" + lines[i];
//                }
//            } catch (StringIndexOutOfBoundsException ignored) {
//                //has no information
//                lines[i] = "";
//            }

            //hope everyone starts at 23
            lines[i] = lines[i].substring(23);
        }
    }

    public boolean[] getPcPresenceData() {
        boolean[] pcPresenceDataString = new boolean[pcPresenceData.size()];

        for (int i = 0; i < pcPresenceDataString.length; i++) {
            pcPresenceDataString[i] = pcPresenceData.get(i);
        }

        return pcPresenceDataString;
    }
}