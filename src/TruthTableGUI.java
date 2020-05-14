import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.TreeMap;


/**
 * <h1>GUI for truth tables</h1>
 * This class generates the GUI and
 * displays all the expressions and evaluations conducted in
 * LogicalValueGenerator
 * <p>
 *
 * @author  Aniket Kumar Gupta
 */

public class TruthTableGUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private boolean[] truthValues;
    private String[][] binaryGrid;
    private ObservableList<ObservableList> data;

    public static void main(String[] args) {
        launch();
    }

    /**
     * This method is responsible for developing the user interface to view the
     * truth table with the 2D True/False grid as well as the evaluated expression.
     * Error handling is done here as well for validation purposes.
     * @param stage This is the initial stage for the GUI component to which other nodes will be added
     */

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Truth Table Generator");
        stage.setWidth(1000);
        stage.setHeight(600);

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

                boolean valid = validateExpressionWithConsecutiveLetters(cleanExpression);

                char lastChar = cleanExpression.charAt(cleanExpression.length() - 1);
                if ((!Character.isLetter(lastChar) &&
                        lastChar != ')' && lastChar != ']' && lastChar != '}') || !valid) {
                    try {
                        throw new InvalidSymbolException("Invalid Expression Syntax");
                    } catch (InvalidSymbolException e) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("Invalid Expression");
                        errorAlert.setContentText(e.getMessage());
                        errorAlert.showAndWait();
                        e.printStackTrace();
                        return;
                    }
                }


                LogicalValueGenerator generator = new LogicalValueGenerator(cleanExpression);
                try {
                    truthValues = generator.truthValuesGenerator();
                    binaryGrid = generator.getBinaryGrid();

                    TreeMap<Character, Boolean> variables = generator.getVariables();

                    int iterator = 0;
                    for (Character character : variables.keySet()) {
                        final int i = iterator;
                        TableColumn<ObservableList<String>, String> column = new TableColumn<>(Character.toString(character));
                        column.setResizable(false);
                        column.setCellValueFactory(observableListStringCellDataFeatures ->
                                new ReadOnlyObjectWrapper<>(observableListStringCellDataFeatures.getValue().get(i)));
                        tableView.getColumns().add(column);
                        ++iterator;
                    }

                    final int lastColumnIdx = iterator;
                    TableColumn<ObservableList<String>, String> column = new TableColumn<>(cleanExpression);
                    column.setCellValueFactory(observableListStringCellDataFeatures ->
                            new ReadOnlyObjectWrapper<>(observableListStringCellDataFeatures.getValue().get(lastColumnIdx)));
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
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Invalid Expression");
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.showAndWait();
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

    /**
     * This method is one of the expression validation methods to ensure that there
     * are no recurring consecutive letters.
     * @param cleanExpression This is the string in its clean form (non-RPN)
     * @return boolean This returns whether the string passed in has consecutive letters (invalid expression)
     */

    private boolean validateExpressionWithConsecutiveLetters(String cleanExpression) {
        for (int i = 0; i < cleanExpression.length() - 1; i++) {
            if (Character.isLetter(cleanExpression.charAt(i)) && Character.isLetter(cleanExpression.charAt(i + 1))) {
                return false;
            }
        }
        return true;
    }

}
