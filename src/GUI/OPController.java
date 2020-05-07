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

    //TODO make sure this will end up in the simulation, so that we don't need constantly move this pointer
    private Integer breakPointLine = -1;
    private int offset;

    public OPController() {
    }

    public void initialize() {
        //linking the data with columns
        tc_Line.setCellValueFactory(new PropertyValueFactory<>("line"));
        tc_PC.setCellValueFactory(new PropertyValueFactory<>("pc"));
        tc_ProgCode.setCellValueFactory(new PropertyValueFactory<>("opCode"));
        tc_BreakPoint.setCellValueFactory(new PropertyValueFactory<>("breakPoint"));

        //creating the list
        list = FXCollections.observableArrayList();

        //setting the list
        tw_table.setItems(list);

        //setting onClick handler, to handle break points
        tw_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //getting the line user clicked on
                //need to parse it, cause it's saved as String
                try {
                    //resetting old breakpoint
                    if (breakPointLine != -1) {
                        list.get(breakPointLine).setBreakPoint(false);
                    }
                    //getting place of a new one, accounting for teh offset
                    breakPointLine = Integer.parseInt(tc_PC.getCellData(tw_table.getFocusModel().getFocusedCell().getRow())) + offset;
                    //setting it
                    list.get(breakPointLine).setBreakPoint(true);
                } catch (NumberFormatException e) {
                    breakPointLine = -1;
                }
            }
        });
    }

    public void clearBreakPoint() {
        //TODO implement
        //clearing breakpoint
        list.get(breakPointLine).setBreakPoint(false);
        //counter to -1, so that the simulation won't go nuts
        breakPointLine = -1;
    }

    /**
     * @param data   OP Code
     * @param offset show where the program really is
     */
    public void setData(String[] data, int offset) {
        this.offset = offset;

        for (int i = 0; i < data.length; i++) {
            list.add(new OPCodeLine(i, i - offset, data[i]));
        }
    }

    @Override
    public void update(String[] data) {
        //TODO move pointer (visuals) according to PC value
    }
}
