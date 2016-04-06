package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;
import java.util.Scanner;

public class Main extends Application {

    Stage window;
    Button b1,b2,b3;
    Label l1,l2,l3;
    BorderPane border;
    MenuBar menu;
    TextArea textArea;
    int themeCheck=0; // 0 represents black colour while 1 presents white one

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Notes");

        setUp();

        //Making a layout boxes

        HBox hbox = new HBox(5);
        VBox vbox = new VBox(8);
        border = new BorderPane();

        vbox.getChildren().addAll(menu);
        hbox.getChildren().addAll(vbox);

        border.setTop(menu);
        border.setCenter(textArea);
        border.setBottom(l1);
        border.setId("Border");

        if(themeCheck == 0){
            border.getStylesheets().add("style.css");
        }
        else
         border.getStylesheets().addAll("lightStyle.css");
        Scene scene = new Scene(border, 500,400);
        window.setScene(scene);
        window.show();
    }

    //method that will set up initial or startup look of the app


    private void setUp(){
        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu help = new Menu("Help");
        Menu view = new Menu("View");

        MenuItem fileNew = new MenuItem("New");
        MenuItem fileSaveAs = new MenuItem("Save As");
        MenuItem fileSave = new MenuItem("Save");
        MenuItem fileOpen = new MenuItem("Open");
        MenuItem fileClose = new MenuItem("Close");
        MenuItem viewFull = new MenuItem("Full Screen");
        MenuItem viewExitFull = new MenuItem("Normal View");

        Menu editColour = new Menu("Change Theme");
        MenuItem darkTheme = new MenuItem("Dark Theme");
        MenuItem lightTheme = new MenuItem("Light Theme");
        editColour.getItems().addAll(darkTheme,lightTheme);

        MenuItem helpHelp = new MenuItem("Help");

        // adding menu items to the menus

        file.getItems().addAll(fileNew, fileSave, fileSaveAs,fileOpen, fileClose);
        edit.getItems().addAll(editColour);
        help.getItems().addAll(helpHelp);
        view.getItems().addAll(viewFull, viewExitFull);

        menu = new MenuBar();
        menu.getMenus().addAll(file,edit,view,help);

        //label to put in the bottom

        l1 = new Label("Notes, made by Me");
        l1.setPadding(new Insets(0,0,0,170));

        // making a text area to put in the middle

        textArea = new TextArea();
        textArea.setPromptText("Welcome to the Notes");

        //adding actions to the menu items
        fileClose.setOnAction(e-> System.exit(0));
        fileSaveAs.setOnAction(e -> {
            if(border.getCenter().equals(vbox)){

            }
            else {
                try {
                    saveText();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        fileOpen.setOnAction(e -> {
            try {
                openText();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        fileSave.setOnAction(e -> {
            try {
                normalSave();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        fileNew.setOnAction(e->{
            newNote();
        });
        darkTheme.setOnAction(e->{
            themeCheck = 0;
            border.getStylesheets().add("style.css");
        });
        lightTheme.setOnAction(e->{
            themeCheck = 1;
            border.getStylesheets().add("lightStyle.css");
        });
        viewFull.setOnAction(e-> {
            menu.setVisible(false);
            border.setTop(null);
            border.setBottom(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Full Screen");
            alert.setContentText("I am in full screen now, to exit hold CTRL and press G");
            alert.setHeaderText(null);
            alert.showAndWait();
        });
        viewExitFull.setOnAction(e-> {
            menu.setVisible(true);
            border.setTop(menu);
            border.setBottom(l1);
        });
        //adding key accelerators to the menu items
        fileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        fileClose.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        fileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        fileSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        viewFull.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        viewExitFull.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));
    }

    public static void main(String[] args) {
        launch(args);
    }

    //method that saves out note
    PrintWriter out;
    private void saveText()throws Exception{
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Save To A File");
        dialog.setHeaderText("Save the text?");
        dialog.setContentText("Enter note name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                PrintWriter print = new PrintWriter(new FileWriter("NoteNames.txt",true));
                print.println(name);
                print.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            name = name+".txt";
            File file = new File(name);
            try {
                out = new PrintWriter(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String textFromArea = textArea.getText();
            out.print(textFromArea);
            out.close();
            textArea.clear();
        });
    }

    String openNote= "";
    ListView<String> list;
    VBox vbox;
    private void openText()throws Exception{
        list = new ListView<>();
        Scanner scan = new Scanner(new File("NoteNames.txt"));

        //adding rows to the list view
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            Scanner token = new Scanner(line);
            while(token.hasNext()){
                String name = token.next();
                list.getItems().add(name);
            }
        }
        vbox = new VBox(5);
        l2 = new Label("List of your notes: ");
        vbox.getChildren().addAll(l2,list);

        b1 = new Button("Open");
        b2 = new Button("Close");

        VBox vbox1 = new VBox(10);
        vbox1.setPadding(new Insets(30,5,5,5));
        vbox1.getChildren().addAll(b1,b2);

        border.setRight(vbox1);
        border.setCenter(vbox);

        // adding a function to open a text to a b1 button
        b1.setOnAction(e->{
            String note = list.getSelectionModel().getSelectedItem();
            note = note+".txt";
            Scanner scanner = null;
            try {
                scanner = new Scanner(new File(note));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            String wholeText = "";
            String text = "";
            while (scanner.hasNextLine()){
                text = scanner.nextLine();
                wholeText = wholeText+text+"\n";
            }
            textArea.setText(wholeText);
            border.setCenter(textArea);
            border.setRight(null);
            window.setTitle(note);
            openNote = note;
        });
        b2.setOnAction(e->{
            try {
                start(window);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            openNote = "";
        });
    }

    private void normalSave() throws IOException {
        String title = window.getTitle();
        if(title.equals("Notes")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("I Cannot Save");
            alert.setHeaderText("Nothing to save");
            alert.setContentText("Please make a new note\nBy clicking on the File->New");
            alert.showAndWait();
        }
        else{
            PrintWriter print = new PrintWriter(new FileWriter("NoteNames.txt",true));
            // i need to cut te last 4 chars from the openNote string
            String cutNote = openNote.substring(0, openNote.length()-4);
            print.println(cutNote);
            print.close();
            File file = new File(openNote);
            PrintWriter out = new PrintWriter(file);
            out.print(textArea.getText());
            out.close();
        }
    }

    private void newNote(){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Note");
        dialog.setHeaderText("Make a new note?");
        dialog.setContentText("Enter note name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            name += ".txt";
            openNote = name;
            window.setTitle(name);
            textArea.clear();
        });
        border.setCenter(textArea);
        border.setRight(null);
        textArea.setPromptText("Start typing...");
    }
}