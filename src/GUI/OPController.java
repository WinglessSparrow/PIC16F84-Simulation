package GUI;

import GUI.TableView.OPCodeLine;
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
    //TODO Breakpoint visualization, thou dunno how yet
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
    //TODO make sure this will end up in the simulation, so that we don't need constantly move this pointer
    private Integer breakPointLine = -1;

    private int pc;
    private int offset;

    public OPController() {
    }

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
                //setting/resetting the breakpoint
                try {
                    //getting the line number from the cell, already offsetted
                    int offsettedLine = Integer.parseInt(tc_PC.getCellData(tw_table.getFocusModel().getFocusedCell().getRow()));

                    //number must be more than -1
                    if (offsettedLine >= 0) {

                        int prevBrP = breakPointLine;

                        clearBreakPoint();
                        //if not the same line, create new breakpoint
                        if (offsettedLine != prevBrP) {
                            breakPointLine = offsettedLine;
                            list.get(breakPointLine + offset).setBreakPoint(true);
                        }
                    }
                } catch (NumberFormatException e) {
                    //nothing to parse, which means it's not on the program part
                    clearBreakPoint();
                }

                moveProgramPointer();
            }
        });
    }

    public void clearBreakPoint() {
        //clearing breakpoint
        list.get(breakPointLine + offset).setBreakPoint(false);
        //counter to -1, so that the simulation won't go nuts
        breakPointLine = -1;
    }

    private void moveProgramPointer() {
        tw_table.requestFocus();
        tw_table.getSelectionModel().select(pc);
        tw_table.getFocusModel().focus(pc);
    }

    /**
     * @param data   OP Code
     * @param offset show where the program really starts
     */
    public void setData(String[] data, String[] pcAbscenceData, int offset) {
        this.offset = offset;
        pc = 0;

        for (int i = 0; i < data.length; i++) {
            //i + 1 because it should start by the line 1
            list.add(new OPCodeLine(i + 1, i - offset, data[i]));
        }
    }

    /**
     * @param data only the pc, so data[0] is the pc
     */
    @Override
    public void update(String[] data) {
        //TODO move pointer (visuals) according to PC value
        try {
            pc = Integer.parseInt(data[0]) + offset;

        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Program Counter Wrong Format (can't parse to int) in OPController");
            //WARNING, it's just to make sure no wrong value will end up in pc
            return;
        }

        moveProgramPointer();
    }
}
