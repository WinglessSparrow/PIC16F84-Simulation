package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OPController extends Controller {
    public TableView<OPCodeLine> tw_table;
    public TableColumn tc_BreakPoint;
    public TableColumn<OPCodeLine, Integer> tc_Line;
    public TableColumn<OPCodeLine, Integer> tc_PC;
    public TableColumn<OPCodeLine, String> tc_ProgCode;


    public OPController() {
    }

    public void initialize() {
        building();
    }

    private void building() {
        tc_Line.setCellValueFactory(
                new PropertyValueFactory<OPCodeLine, Integer>("line")
        );

        tc_PC.setCellValueFactory(
                new PropertyValueFactory<OPCodeLine, Integer>("pc")
        );

        tc_ProgCode.setCellValueFactory(
                new PropertyValueFactory<OPCodeLine, String>("opCode")
        );

        ObservableList<OPCodeLine> list = FXCollections.observableArrayList();

        tw_table.setItems(list);
    }

    @Override
    public void update(String[] data) {

    }
}
