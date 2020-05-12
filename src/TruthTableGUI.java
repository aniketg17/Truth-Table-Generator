import javafx.application.Application;
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

import java.util.Stack;
import java.util.TreeMap;

public class TruthTableGUI extends Application {

    private TableView table = new TableView();
    private boolean[] truthValues;
    private String[][] binaryGrid;

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
                String expression = expressionInput.getText();
                String cleanExpression = expression.replaceAll("\\s", "");
                cleanExpression = cleanExpression.toLowerCase();
                LogicalValueGenerator generator = new LogicalValueGenerator(cleanExpression);
                try {
                    truthValues = generator.truthValuesGenerator();
                    binaryGrid = generator.getBinaryGrid();
                   // System.out.println(truthValues[3]);

                    TreeMap<Character, Boolean> variables = LogicalValueGenerator.getVariables();
                    TableColumn[] columns = new TableColumn[variables.size()];

                    int iterator = 0;
                    for (Character character : variables.keySet()) {
                        columns[iterator] = new TableColumn<String, String>(Character.toString(character));
                    }

                    TableColumn firstNameCol = new TableColumn("First Name");
                    TableColumn lastNameCol = new TableColumn("Last Name");
                    TableColumn emailCol = new TableColumn("Email");

                    table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

                } catch (InvalidSymbolException e) {
                    e.printStackTrace(); // add proper exception handling for GUI
                }
            }
        });

        expressionPane.getChildren().add(inputCommand);
        expressionPane.getChildren().add(expressionInput);
        expressionPane.getChildren().add(submitExpression);

//        table.setEditable(true);


        final VBox vbox = new VBox();

        vbox.setSpacing(20);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, expressionPane, table);
        Label placeholder = new Label("Empty Truth Table");
        table.setPlaceholder(placeholder);
        table.setMaxWidth(stage.getWidth());


        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();

//        label.setAlignment(Pos.CENTER);

    }
}
