import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Stack;
import java.util.TreeMap;

public class TruthTableGUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private boolean[] truthValues;
    private String[][] binaryGrid;
    private ObservableList<ObservableList> data;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Group());
        stage.setTitle("Truth Table Generator");
        stage.setWidth(1000);
        stage.setHeight(700);

        Label label = new Label("Disclaimer: Please use Well Formed Formulae (WFF) for maximum accuracy. " +
                "Software uses conventional notation.");
        label.setFont(new Font("Arial", 14));

        TextField expressionInput = new TextField("");

        TilePane expressionPane = new TilePane();

        Label inputCommand = new Label("Please enter the expression in propositional logic here: ");
        inputCommand.setFont(new Font("Arial", 14));

        Button submitExpression = new Button("Generate Table");

        submitExpression.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tableView.getItems().clear();
                tableView.getColumns().clear();


                String expression = expressionInput.getText();
                String cleanExpression = expression.replaceAll("\\s", "");
                cleanExpression = cleanExpression.toLowerCase();
                LogicalValueGenerator generator = new LogicalValueGenerator(cleanExpression);
                try {
                    truthValues = generator.truthValuesGenerator();
                    binaryGrid = generator.getBinaryGrid();

                    TreeMap<Character, Boolean> variables = generator.getVariables();

                    int iterator = 0;
                    for (Character character : variables.keySet()) {
                        System.out.println(character);
                        final int i = iterator;
                        TableColumn<ObservableList<String>, String> column = new TableColumn<>(Character.toString(character));

                        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                            @Override
                            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> observableListStringCellDataFeatures) {
                                return new ReadOnlyObjectWrapper<>(observableListStringCellDataFeatures.getValue().get(i));
                            }
                        });
                        tableView.getColumns().add(column);
                        ++iterator;
                    }

                    final int iterator2 = iterator;
                    //TableColumn expressionColumn = new TableColumn(expression);
                    TableColumn<ObservableList<String>, String> column = new TableColumn<>(expression);
                    column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> observableListStringCellDataFeatures) {
                            return new ReadOnlyObjectWrapper<>(observableListStringCellDataFeatures.getValue().get(iterator2));
                        }
                    });

                    tableView.getColumns().add(column);

                    data = FXCollections.observableArrayList();

                    for (int i = 0; i < binaryGrid.length; i++) {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for (int j = 0; j < binaryGrid[0].length; j++) {
                            row.add(binaryGrid[i][j]);
                        }
                        if (!truthValues[i]) {
                            row.add("False");
                        } else {
                            row.add("True");
                        }

                        data.add(row);
                        tableView.getItems().add(row);

                    }

                } catch (InvalidSymbolException e) {
                    e.printStackTrace(); // add proper exception handling for GUI
                }
            }
        });

        expressionPane.getChildren().add(inputCommand);
        expressionPane.getChildren().add(expressionInput);
        expressionPane.getChildren().add(submitExpression);

        final VBox vbox = new VBox();

        vbox.setSpacing(20);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, expressionPane, tableView);
        Label placeholder = new Label("Empty Truth Table");
        tableView.setPlaceholder(placeholder);
        tableView.setMaxWidth(stage.getWidth());


        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();

    }
}
