package GUI;

import GUI.TableView.OPCodeLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OPController extends Controller {
    @FXML
    private TableView<OPCodeLine> tw_table;
    //TODO Breakpoint visualization, thou dunno how yet
    @FXML
    private TableColumn tc_BreakPoint;
    @FXML
    private TableColumn<OPCodeLine, Integer> tc_Line;
    @FXML
    private TableColumn<OPCodeLine, Integer> tc_PC;
    @FXML
    private TableColumn<OPCodeLine, String> tc_ProgCode;

    private ObservableList<OPCodeLine> list;

    public OPController() {
    }

    public void initialize() {
        //linking the data with columns
        tc_Line.setCellValueFactory(new PropertyValueFactory<>("line"));
        tc_PC.setCellValueFactory(new PropertyValueFactory<>("pc"));
        tc_ProgCode.setCellValueFactory(new PropertyValueFactory<>("opCode"));

        //creating the list
        list = FXCollections.observableArrayList();

        //setting the list
        tw_table.setItems(list);
    }

    /**
     * @param data   OP Code
     * @param offset show where the program really is
     */
    public void setData(String[] data, int offset) {
        for (int i = 0; i < data.length; i++) {
            list.add(new OPCodeLine(i, i + offset, data[i]));
        }
    }

    @Override
    public void update(String[] data) {
        //TODO move pointer (visuals) according to PC value
    }
}
