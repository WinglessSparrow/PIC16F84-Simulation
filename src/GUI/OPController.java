package GUI;

import Elements.ProgramCounter;
import GUI.TableView.OPCodeLine;
import Helpers.ProgramCodeParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class OPController extends Controller {
    @FXML
    private TableView<OPCodeLine> tw_table;
    @FXML
    private TableColumn<Object, Object> tc_BreakPoint;
    @FXML
    private TableColumn<OPCodeLine, Integer> tc_Line;
    @FXML
    private TableColumn<OPCodeLine, String> tc_PC;
    @FXML
    private TableColumn<OPCodeLine, String> tc_ProgCode;

    private ObservableList<OPCodeLine> list;

    //Integers are Boxed, so that we could change them without actively moving data
    private Integer breakPointLine = -1;

    private int pc = 0;
    private boolean[] pcPresenceData;
    private ProgramCounter programCounter;


    public void initialize() {
        //linking the data with columns
        tc_Line.setCellValueFactory(new PropertyValueFactory<>("line"));
        tc_Line.setSortable(false);
        tc_Line.setEditable(false);
        tc_PC.setCellValueFactory(new PropertyValueFactory<>("pc"));
        tc_PC.setSortable(false);
        tc_PC.setEditable(false);
        tc_ProgCode.setCellValueFactory(new PropertyValueFactory<>("opCode"));
        tc_ProgCode.setSortable(false);
        tc_ProgCode.setEditable(false);
        tc_BreakPoint.setCellValueFactory(new PropertyValueFactory<>("breakPoint"));
        tc_BreakPoint.setSortable(false);
        tc_BreakPoint.setEditable(false);

        tc_BreakPoint.setStyle("-fx-text-fill: red");

        //creating the list
        list = FXCollections.observableArrayList();

        //setting the list
        tw_table.setItems(list);

        //setting onClick handler, to handle break points
        tw_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if (simGUI.getSim().isFlagPause()) {
                        //setting/resetting the breakpoint
                        try {
                            //getting the line number from the cell
                            int chosenLine = tw_table.getFocusModel().getFocusedCell().getRow();
                            int chosenPC = Integer.parseInt(tc_PC.getCellData(chosenLine));

                            //this is a Program Code line (not Blank or a comment)
                            if (pcPresenceData[chosenLine]) {
                                int prevBrP = breakPointLine;
                                //clearing the old break point
                                clearBreakPoint();

                                //if not the same line, create new breakpoint
                                if (chosenPC != prevBrP) {
                                    breakPointLine = chosenPC;
                                    list.get(findPc(breakPointLine)).setBreakPoint(true);
                                }
                            }
                        } catch (NumberFormatException e) {
                            //nothing to parse, which means it's not on the program part
                            clearBreakPoint();
                        }

                        simGUI.getSim().setBreakpointLine(breakPointLine);

                        moveProgramPointer();
                    }
                } catch (NullPointerException e) {
                    System.out.println("No simulation");
                }
            }
        });
    }

    public void clearBreakPoint() {
        if (breakPointLine > -1) {
            //clearing breakpoint
            list.get(findPc(breakPointLine)).setBreakPoint(false);
            //counter to -1, so that the simulation won't go nuts
            breakPointLine = -1;
        }
    }

    private void moveProgramPointer() {
        tw_table.requestFocus();
        tw_table.getSelectionModel().select(findPc(pc));
        tw_table.getFocusModel().focus(findPc(pc));

        //bp has been reached, no need for one anymore
        if (pc == breakPointLine) {
            clearBreakPoint();
        }
    }

    /**
     * @return idx of the row where the current Program Counter is
     */
    private int findPc(int pc) {
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).pcProperty().get()).equals(pc + "")) {
                //found the correct pointer
                //return
                return i;
            }
        }

        //no program code
        return -1;
    }

    /**
     * References must be set
     */
    public void setData(ProgramCodeParser parser, ProgramCounter programCounter) {
        pc = 0;

        //this.pcPresenceData = pcPresenceData;
        String[] data = parser.getProgramData();
        pcPresenceData = parser.getPcPresenceData();
        this.programCounter = programCounter;

        int pcCount = 0;

        //Clear list
        list = FXCollections.observableArrayList();
        tw_table.setItems(list);

        for (int i = 0; i < data.length; i++) {
            //i + 1 because it should start by the line 1
            if (pcPresenceData[i]) {
                //a code line -> PC is being registered
                list.add(new OPCodeLine(i + 1, pcCount, data[i]));
                pcCount++;
            } else {
                //here not a code line
                list.add(new OPCodeLine(i + 1, -1, data[i]));
            }
        }

        moveProgramPointer();
        tw_table.scrollTo(findPc(pc));
    }

    @Override
    public void update() {
        try {
            //-1 one so the pointer always points on the the next command
            pc = programCounter.getCountedValue() - 1;

        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Program Counter Wrong Format (can't parse to int) in OPController");
            //WARNING, it's just to make sure no wrong value will end up in pc
            return;
        }

        moveProgramPointer();
    }
}
