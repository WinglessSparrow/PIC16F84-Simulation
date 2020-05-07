package GUI;

import GUI.TableView.RegisterToBit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SFRPController extends Controller {
    @FXML
    private TableView<RegisterToBit> tw_table;
    @FXML
    private TableColumn<RegisterToBit, String> tc_type;
    @FXML
    private TableColumn<RegisterToBit, Integer> tc_0;
    @FXML
    private TableColumn<RegisterToBit, Integer> tc_1;
    @FXML
    private TableColumn<RegisterToBit, Integer> tc_2;
    @FXML
    private TableColumn<RegisterToBit, Integer> tc_3;
    @FXML
    private TableColumn<RegisterToBit, Integer> tc_4;
    @FXML
    private TableColumn<RegisterToBit, Integer> tc_5;
    @FXML
    private TableColumn<RegisterToBit, Integer> tc_6;
    @FXML
    private TableColumn<RegisterToBit, Integer> tc_7;

    private ObservableList<RegisterToBit> list;

    public void initialize() {
        //linking the columns with the data
        tc_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        tc_0.setCellValueFactory(new PropertyValueFactory<>("bit0"));
        tc_1.setCellValueFactory(new PropertyValueFactory<>("bit1"));
        tc_2.setCellValueFactory(new PropertyValueFactory<>("bit2"));
        tc_3.setCellValueFactory(new PropertyValueFactory<>("bit3"));
        tc_4.setCellValueFactory(new PropertyValueFactory<>("bit4"));
        tc_5.setCellValueFactory(new PropertyValueFactory<>("bit5"));
        tc_6.setCellValueFactory(new PropertyValueFactory<>("bit6"));
        tc_7.setCellValueFactory(new PropertyValueFactory<>("bit7"));

        //creating the list
        list = FXCollections.observableArrayList();

        //setting the list
        tw_table.setItems(list);
    }

    /**
     * @param values sorted array with all the sfr register
     * @param type   names of the registers
     */
    public void setData(String[] type, int values[]) {
        for (int i = 0; i < values.length; i++) {
            list.add(new RegisterToBit(type[i], values[i]));
        }
    }

    @Override
    public void update(String[] data) {
        //TODO call update on list and give it the values
        //compare names of registers or smth <<< Stefan frag mich, bin nicht ganz sicher
    }
}
