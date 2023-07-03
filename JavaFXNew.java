package src;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class JavaFXNew extends Application{
    DBManager dbm;
    String official_user = "";
    String current_course = "";
    private String btnStyle = "-fx-background-color: \n" +
            "        #c3c4c4,\n" +
            "        linear-gradient(#d6d6d6 50%, white 100%),\n" +
            "        radial-gradient(center 50% -40%, radius 200%, #e6e6e6 45%, rgba(230,230,230,0) 50%);\n" +
            "    -fx-background-radius: 30;\n" +
            "    -fx-background-insets: 0,1,1;\n" +
            "    -fx-text-fill: black;\n" +
            "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );\n" +
            "    -fx-padding: 0 10 0 0;\n" +
            "    -fx-alignment: center;\n" +
            "    -fx-font-size: 12;\n" +
            "    -fx-padding: 6;";

    private String btnStyleReview = "-fx-background-color: \n" +
            "        #c3c4c4,\n" +
            "        linear-gradient(#d6d6d6 50%, white 100%),\n" +
            "        radial-gradient(center 50% -40%, radius 200%, #e6e6e6 45%, rgba(230,230,230,0) 50%);\n" +
            "    -fx-background-radius: 30;\n" +
            "    -fx-background-insets: 0,1,1;\n" +
            "    -fx-text-fill: black;\n" +
            "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );\n" +
            "    -fx-padding: 0 10 0 0;\n" +
            "    -fx-font-size: 12;\n" +
            "    -fx-padding: 6;";
    private String mainScreenBtnStyle = "-fx-background-color: \n" +
            "        #c3c4c4,\n" +
            "        linear-gradient(#d6d6d6 50%, white 100%),\n" +
            "        radial-gradient(center 50% -40%, radius 200%, #e6e6e6 45%, rgba(230,230,230,0) 50%);\n" +
            "    -fx-background-radius: 30;\n" +
            "    -fx-background-insets: 0,1,1;\n" +
            "    -fx-text-fill: black;\n" +
            "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );\n" +
            "    -fx-content-display: left;\n" +
            "    -fx-padding: 0 10 0 0;\n" +
            "    -fx-alignment: center-left;\n" +
            "    -fx-font-size: 12;\n" +
            "    -fx-padding: 6 6 6 10;";
    // Design inspired from: http://fxexperience.com/2011/12/styling-fx-buttons-with-css/
    class FirstScreenRoot extends GridPane {

        public FirstScreenRoot(JavaFXNew main){
            Text courseReview = new Text("The Course Review");
            Button loginButton = new Button("Log In");
            loginButton.setStyle(mainScreenBtnStyle);
            loginButton.setMinWidth(220);
            loginButton.setTranslateY(3);
            loginButton.setPadding(new Insets(5, 5, 5, 5));
            Button createAcctButton = new Button("Create Account");
            createAcctButton.setStyle(mainScreenBtnStyle);
            createAcctButton.setMinWidth(220);
            createAcctButton.setAlignment(Pos.BASELINE_LEFT);
            loginButton.setOnAction(e -> {
                main.changePane(main.loginPane);
            });

            createAcctButton.setOnAction(e -> {
                main.changePane(main.createPane);
            });
            courseReview.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            courseReview.setLineSpacing(20);
            setPadding(new Insets(10, 10, 10, 10));
            setVgap(10);
            setHgap(5);
            add(courseReview, 0, 0);
            add(loginButton, 0, 1);
            add(createAcctButton, 0, 2);
        }

    }
    class LoginScreenRoot extends GridPane{
        public LoginScreenRoot(JavaFXNew main){
            setPadding(new Insets(10, 10, 10, 10));
            setVgap(5);
            setHgap(5);
            Label text = new Label("Login");
            text.setFont(Font.font("System", FontWeight.BOLD, 20));

            Label usernameLabel = new Label("Username: ");
            Label passwordLabel = new Label("Password: " );
            TextField username = new TextField();
            PasswordField password = new PasswordField();
            password.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            Button submit = new Button("Submit");
            submit.setStyle(btnStyle);
            submit.setMinWidth(75);
            submit.setAlignment(Pos.BASELINE_RIGHT);
            Button goBack = new Button("Back");
            goBack.setAlignment(Pos.BASELINE_LEFT);
            goBack.setMinWidth(75);
            goBack.setStyle(btnStyle);
            String user = username.getText();
            submit.setOnAction(e -> {
                System.out.println(username.getText() + ", " + password.getText());
                if(dbm.login(username.getText(), password.getText())){
                    official_user = username.getText();
                    main.changePane(main.menuPane);
                }else{
                    showAlert("Error", "Your username/password is incorrect!");
                }
            });
            goBack.setOnAction(e -> {
                System.out.println(password.getText());
                main.changePane(firstPane);
            });
            add(text, 0, 0);
            add(usernameLabel, 0, 1);
            add(username, 1, 1);
            add(passwordLabel, 0, 2);
            add(password, 1, 2);
            add(submit, 1, 3);
            add(goBack, 0, 3);
        }

    }
    class CreateAccountRoot extends GridPane{
        public CreateAccountRoot(JavaFXNew main){
            setPadding(new Insets(10, 10, 10, 10));
            setVgap(5);
            setHgap(5);
            Label text = new Label("Create Account");
            text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            TextField username = new TextField();
            PasswordField password = new PasswordField();
            password.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            PasswordField confirmPassword = new PasswordField();
            confirmPassword.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            Label usernameLabel = new Label("Username: ");
            Label passwordLabel = new Label("Password: " );
            Label confirmPasswordLabel = new Label("Confirm Password: " );

            Button submit = new Button("Submit");
            submit.setStyle(btnStyle);
            submit.setMinWidth(75);
            Button goBack = new Button("Back");
            goBack.setStyle(btnStyle);
            goBack.setMinWidth(75);
            String user = username.getText();
            submit.setOnAction(e -> {
                System.out.println(username.getText() + ", " + password.getText());
                if(password.getText().equals(confirmPassword.getText()) && !username.getText().strip().equals("")){
                    if(dbm.addLogin(username.getText(), password.getText())){
                        official_user = username.getText();
                        main.changePane(menuPane);
                    }else{
                        showAlert("Error", "This username already has an account!");
                    }
                }else{
                    showAlert("Error","Your Passwords don't match!");
                }

            });
            goBack.setOnAction(e -> {
                main.changePane(firstPane);
            });
            add(text, 0, 0);
            add(usernameLabel, 0, 1);
            add(username, 1, 1);
            add(passwordLabel, 0, 2);
            add(password, 1, 2);
            add(confirmPasswordLabel, 0, 3);
            add(confirmPassword, 1, 3);
            add(submit, 1, 4);
            add(goBack, 0, 4);

        }

    }
    class MenuScreenRoot extends GridPane{
        public MenuScreenRoot(JavaFXNew main){
            setPadding(new Insets(10, 10, 10, 10));
            setVgap(5);
            setHgap(5);
            Label text = new Label("Main Menu");
            text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            Button submitReviewButton = new Button("Submit A Review");
            submitReviewButton.setStyle("-fx-padding: 6 6 6 15;");
            submitReviewButton.setStyle(btnStyleReview);
            submitReviewButton.setMinWidth(150);
            submitReviewButton.setAlignment(Pos.BASELINE_LEFT);
            Button seeReviewButton = new Button("See a review");
            seeReviewButton.setStyle("-fx-padding: 6 6 6 15;");
            seeReviewButton.setStyle(btnStyleReview);
            seeReviewButton.setAlignment(Pos.BASELINE_LEFT);
            seeReviewButton.setMinWidth(150);
            Button logoutButton = new Button("Log Out");
            logoutButton.setStyle("-fx-padding: 6 6 6 15;");
            logoutButton.setStyle(btnStyleReview);
            logoutButton.setAlignment(Pos.BASELINE_LEFT);
            logoutButton.setMinWidth(150);

            TextField courseNumber = new TextField();
            Popup popup = new Popup();
            Label update = new Label();

            Button closePopupButton = new Button("Close");
            popup.getContent().add(update);
            popup.getContent().add(closePopupButton);

            closePopupButton.setOnAction(e ->{
                popup.hide();
            });

            submitReviewButton.setOnAction(e ->{
                String course = courseNumber.getText();
                if(dbm.courseDepartmentValid(course)) {
                    if(dbm.hasReviewed(course, official_user)){
                        current_course = course;
                        main.changePane(main.submitPane);
                    }else{
                        showAlert("Error","This course has already been reviewed by you");
                    }
                }else{
                    showAlert("Error","This is an Invalid Course Number!");
                }
            });

            seeReviewButton.setOnAction(e ->{
                String course = courseNumber.getText();
                if(dbm.courseDepartmentValid(courseNumber.getText())){
                    if(dbm.hasReviews(course)) {
                        current_course = course;
                        dbm.printReviews(current_course);
                        main.changePaneToReviewsList();
                    }else{
                        showAlert("Error","This course has no reviews");
                    }
                }else{
                    showAlert("Error","This is an Invalid Course Number!");
                }
            });

            logoutButton.setOnAction(e -> {
                main.changePane(main.firstPane);
            });

//            courseNumber.setPrefHeight(40);
            Label courseNLabel = new Label("Course Number:" );
            add(text, 0, 0);
            add(courseNLabel, 0, 1);
            add(courseNumber, 1, 1);
            add(submitReviewButton, 0, 2);
            add(seeReviewButton, 0, 3);
            add(logoutButton, 0, 4);

            //unfinished
        }
    }

    class SubmitReviewScreenRoot extends GridPane{
        public SubmitReviewScreenRoot(JavaFXNew main){
            setPadding(new Insets(10, 10, 10, 10));
            setVgap(5);
            setHgap(5);
            Label text = new Label("Submit A Review");
            text.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Label SubmitReviewPromptLabel = new Label("Write your review here:");
            TextArea review = new TextArea();
            review.setWrapText(true);
            review.setPrefWidth(80);
            review.setPrefHeight(80);

            Label SubmitRatingPromptLabel = new Label("Rate your review 1-5 here:");
            TextField rating = new TextField();

            Button submitButton = new Button("Submit your Review");

            submitButton.setOnAction(e ->{
                String ratingText = rating.getText();
                if(ratingText.strip().equals("")){
                    showAlert("Error", "You must provide a numerical rating!");
                }else {
                    int ratingInt = Integer.parseInt(rating.getText());
                    String reviewText = review.getText();
                    if (ratingInt > 5 || ratingInt < 1) {
                        showAlert("Error", "Your rating is not between 1 and 5!");
                    }
                    else if (reviewText.strip().equals("")) {
                        showAlert("Error", "You Must provide a written review!");
                    } else {
                        dbm.addReview(current_course, ratingInt, official_user, reviewText);
                        showAlert("Success!", "Your review has been submitted!");
                        main.changePane(menuPane);
                    }
                }
            });
            add(text, 0, 0);
            add(SubmitReviewPromptLabel, 0, 1);
            add(review, 1, 1);
            add(SubmitRatingPromptLabel, 0, 2);
            add(rating, 1, 2);
            add(submitButton, 0, 3);
            Button goBack = new Button("Go Back");
            goBack.setOnAction(e ->{
                main.changePane(menuPane);
            });
            add(goBack, 1, 3);
//            getChildren().add(text);
//            getChildren().add(SubmitReviewPromptLabel);
//            getChildren().add(review);
//            getChildren().add(SubmitRatingPromptLabel);
//            getChildren().add(rating);
//            getChildren().add(submitButton);
        }
    }
    class SeeReviewScreenRoot extends GridPane{
        public SeeReviewScreenRoot(JavaFXNew main){
            setPadding(new Insets(10, 10, 10, 10));
            setVgap(5);
            setHgap(5);
            Label reviewsLabel = new Label("Reviews:");
            reviewsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Text text = new Text();
            text.setWrappingWidth(300);

            ArrayList<String> reviews = dbm.returnArrayOfReviews(current_course);
            double ratingInt = Double.parseDouble(reviews.get(reviews.size()-1));
            reviews.remove(reviews.size()-1);
            String reviewText = "";
            for(int i=0; i<reviews.size(); i++){
                System.out.println("\nREVIEW TEXT: " + reviewText + "END\n");
                reviewText = reviewText + (i+1) + ": " + reviews.get(i) + "\n";
            }
//            reviewText = reviewText + "\nAverage Rating: " + ratingInt;
            text.setText(reviewText);


            add(reviewsLabel, 0, 0);
            add(text, 0, 1);
            Text rating = new Text("Average Rating: ");
            Text numberRating = new Text(ratingInt + "/5");
            numberRating.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            add(rating, 0, 2);
            add(numberRating, 0, 3);
            Button goBack = new Button("Go Back");
            goBack.setOnAction(e ->{
                main.changePane(menuPane);
            });
            add(goBack, 1, 1);

//            getChildren().add(reviewsLabel);
//            getChildren().add(text);
//            getChildren().add(goBack);

        }
    }

    public FirstScreenRoot firstPane;
    public LoginScreenRoot loginPane;
    public CreateAccountRoot createPane;
    public MenuScreenRoot menuPane;
    public SubmitReviewScreenRoot submitPane;
    public SeeReviewScreenRoot seeReviewPane;
    private Scene mainScene;
    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;

        firstPane = new FirstScreenRoot(this);
        firstPane.setAlignment(Pos.CENTER);
        loginPane = new LoginScreenRoot(this);
        loginPane.setAlignment(Pos.CENTER);
        createPane = new CreateAccountRoot(this);
        createPane.setAlignment(Pos.CENTER);
        menuPane = new MenuScreenRoot(this);
        submitPane = new SubmitReviewScreenRoot(this);
        mainScene = new Scene(firstPane, 400, 400);
        initializeDB();
        mainStage.setScene(mainScene);
        mainStage.setTitle("The Course Review");
        mainStage.show();
    }
    public void initializeDB(){
        dbm = new DBManager();
        if(!(dbm.checkTables())){
            dbm.connect();
            dbm.createTables();
            dbm.addData();
        }
        else{
            dbm.connect();
        }
    }
    public void changePane(Pane targetPane) {
        mainScene.setRoot(targetPane);
    }
    public void changePaneToReviewsList(){
        seeReviewPane = new SeeReviewScreenRoot(this);
        mainScene.setRoot(seeReviewPane);
    }
    public void showAlert(String header, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(header);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
