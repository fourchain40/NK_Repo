package src;
import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class DBManager {
    private Connection connection;
    private String url = "jdbc:sqlite:Reviews.sqlite3";
    private String file_name = "Reviews.sqlite3";

    public void connect() {
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
        }
        catch(SQLException | ClassNotFoundException e){
            throw new IllegalStateException(e);
        }
    }
    public boolean checkTables(){
        File file = new File(file_name);
        File otherFile = new File(url);
        if (!file.exists()) {
            try{
                file.createNewFile();
            }
            catch(IOException e){
                System.out.print(e.getMessage());
            }
            return false;
        }
        return true;
    }
    public void createTables(){
        if (connection == null) {
            throw new IllegalStateException("No connection to Manager yet");
        }
        String studentTable = "CREATE TABLE Students (ID INTEGER PRIMARY KEY AUTOINCREMENT, Login VARCHAR(255) UNIQUE NOT NULL,\n" +
                "                    Password VARCHAR(255) NOT NULL)";
        String courseTable = "CREATE TABLE Courses (ID INTEGER PRIMARY KEY AUTOINCREMENT, Department VARCHAR(255) NOT NULL,\n" +
                "                    Catalog VARCHAR(255) NOT NULL)";
        String reviewTable =  "CREATE TABLE Reviews (ID INTEGER PRIMARY KEY AUTOINCREMENT, StudentID INTEGER NOT NULL,\n" +
                "                    CourseID INTEGER NOT NULL, Review TEXT NOT NULL, Rating INTEGER NOT NULL CHECK (Rating >= 1 AND Rating <= 5), \n" +
                "                    FOREIGN KEY (StudentID) REFERENCES Students(ID) ON DELETE CASCADE, FOREIGN KEY (CourseID) REFERENCES Courses(ID) ON DELETE CASCADE)";
        try {
            Statement statement = connection.createStatement();
            statement.execute(studentTable);
            statement.execute(courseTable);
            statement.execute(reviewTable);
            statement.execute("DELETE FROM sqlite_sequence WHERE name='Students'");
            statement.execute("DELETE FROM sqlite_sequence WHERE name='Courses'");
            statement.execute("DELETE FROM sqlite_sequence WHERE name='Reviews'");

            statement.close();


        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void addData(){
        String insertUsers = "INSERT INTO Students (Login, Password) VALUES " +
                "('nk8pw', 'Hello'), " +
                "('vds6ce', 'Hey'), " +
                "('fqu6ha', 'Howdy')";
        String insertCourses = "INSERT INTO Courses (Department, Catalog) VALUES " +
                "('CS', '3140'), " +
                "('CS', '3130'), " +
                "('CS', '3100')";
        String insertReviews = "INSERT INTO Reviews (StudentID, CourseID, Review, Rating) VALUES " +
                "(1, 1, 'Great course and professor, I learned a lot!', 5), " +
                "(2, 1, 'Very challenging class, but rewarding and informative.', 4), " +
                "(1, 2, 'Interesting material, however pace was too fast.', 3), " +
                "(2, 3, 'The course was well-structured and engaging. Mark Floryan is a great lecturer.', 5), " +
                "(3, 3, 'A solid class and continuation of 2100.', 4), " +
                "(3, 2, 'Difficult, but the professor and TA''s were helpful.', 4)";
        try{
            Statement statement = connection.createStatement();
            statement.execute(insertUsers);
            statement.execute(insertCourses);
            statement.execute(insertReviews);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void clear() {
        if (connection == null) {
            throw new IllegalStateException("No connection to Manager yet");
        }
        try {
            //https://stackoverflow.com/questions/1601151/how-do-i-check-in-sqlite-whether-a-table-exists
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultStudents = metaData.getTables(null, null, "Students", null);
            ResultSet resultCourses = metaData.getTables(null, null, "Courses", null);
            ResultSet resultReviews = metaData.getTables(null, null, "Reviews", null);

            if (!(resultStudents.next()&&resultCourses.next()&&resultReviews.next())) {
                throw new IllegalStateException("One or more of these tables don't exist");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return;
        }
        String firstDelete = "DELETE FROM Students";
        String secondDelete = "DELETE FROM Courses";
        String thirdDelete = "DELETE FROM Reviews";
        try{
            Statement statement = connection.createStatement();
            statement.execute(firstDelete);
            statement.execute(secondDelete);
            statement.execute(thirdDelete);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return;
        }
    }
    public void deleteTables() {
        if (connection == null) {
            throw new IllegalStateException("No connection to Manager yet");
        }
        try {
            //https://stackoverflow.com/questions/1601151/how-do-i-check-in-sqlite-whether-a-table-exists
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultStops = metaData.getTables(null, null, "Students", null);
            ResultSet resultBuslines = metaData.getTables(null, null, "Courses", null);
            ResultSet resultRoutes = metaData.getTables(null, null, "Reviews", null);

            if (!(resultStops.next()&&resultBuslines.next()&&resultRoutes.next())) {
                throw new IllegalStateException("One or more of these tables don't exist");
            }
            resultStops.close();
            resultBuslines.close();
            resultRoutes.close();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return;
        }
        String firstDelete = "DROP TABLE Students";
        String secondDelete = "DROP TABLE Courses";
        String thirdDelete = "DROP TABLE Reviews";
        try{
            Statement statement = connection.createStatement();
            statement.execute(firstDelete);
            statement.execute(secondDelete);
            statement.execute(thirdDelete);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return;
        }
    }
    public boolean login(String username, String password){
       try {
           String getLogin = "SELECT Login FROM Students WHERE Login = '" + username + "' AND Password = '" + password + "'";
           Statement statement = connection.createStatement();
           ResultSet rs = statement.executeQuery(getLogin);
           if(rs.next()){
               return true;
           }
       }
       catch(SQLException e){
           return false;
       }
       return false;

    }
    public boolean addLogin(String username, String password){
        String insertUser = "INSERT INTO Students (Login, Password) VALUES " +
                "('" + username + "', '" + password + "')";
        try{
            Statement statement = connection.createStatement();
            statement.execute(insertUser);
            return true;
        }
        catch(SQLException e){
            return false;
        }
    }
    public boolean courseDepartmentValid(String course){
        String[] split = course.split(" ");
        if(split[0].length()>4||!(split[0].toUpperCase().equals(split[0]))||split[1].length()!=4||split.length!=2){
            return false;
        }
        for (int i = 0; i < split[0].length(); i++) {
            if (Character.isLetter(split[0].charAt(i)) == false) {
                return false;
            }
        }
        for (int j = 0; j < split[1].length(); j++) {
            if (split[1].charAt(j) < '0' || split[1].charAt(j) > '9') {
                return false;
            }
        }
        return true;
    }
    public boolean hasReviewed(String course, String username){
        try {
            String[] split = course.split(" ");
            String getCourse = "SELECT ID FROM Courses WHERE Department = '" + split[0] + "' AND Catalog = '" + split[1] + "'";
            String getLogin = "SELECT ID FROM Students WHERE Login = '" + username + "'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getLogin);
            int id = rs.getInt("ID");
            rs = statement.executeQuery(getCourse);
            if(!(rs.next())){
                String insertCourse = "INSERT INTO Courses (Department, Catalog) VALUES " +
                        "('" + split[0] + "', '" + split[1] + "')";
                statement.execute(insertCourse);
                rs = statement.executeQuery(getCourse);
            }
            int courseID = rs.getInt("ID");
            String getID = "SELECT * FROM Reviews WHERE StudentID = " + id + " AND CourseID = " + courseID + "";
            ResultSet rs1 = statement.executeQuery(getID);
            if(rs1.next()){
                return false;
            }
        }
        catch(SQLException e){
            return false;
        }
        return true;

    }
    public void addReview(String course, int rating, String username, String review){
        try{
            Statement statement = connection.createStatement();
            String[] split = course.split(" ");
            String getCourse = "SELECT ID FROM Courses WHERE Department = '" + split[0] + "' AND Catalog = '" + split[1] + "'";
            String getLogin = "SELECT ID FROM Students WHERE Login = '" + username + "'";
            ResultSet rs = statement.executeQuery(getLogin);
            int id = rs.getInt("ID");
            rs = statement.executeQuery(getCourse);
            int courseID = rs.getInt("ID");
            String insertCourse = "INSERT INTO Reviews (StudentID, CourseID, Review, Rating) VALUES " +
                    "('" + id + "', '" + courseID + "', '" + review + "', '" + rating + "')";
            statement.execute(insertCourse);

        }
        catch(SQLException e){
            return;
        }

    }
    public boolean hasReviews(String course){
        try{
            Statement statement = connection.createStatement();
            String[] split = course.split(" ");
            String getCourse = "SELECT ID FROM Courses WHERE Department = '" + split[0] + "' AND Catalog = '" + split[1] + "'";
            ResultSet rs = statement.executeQuery(getCourse);
            int id = rs.getInt("ID");
            String getReviews = "SELECT * FROM Reviews WHERE CourseID = " + id + "";
            rs = statement.executeQuery(getReviews);
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }


        }
        catch(SQLException e){
            return false;
        }

    }
    public ArrayList<String> returnArrayOfReviews(String course){
        try{
            Statement statement = connection.createStatement();
            String[] split = course.split(" ");
            String getCourse = "SELECT ID FROM Courses WHERE Department = '" + split[0] + "' AND Catalog = '" + split[1] + "'";
            ResultSet rs = statement.executeQuery(getCourse);
            int id = rs.getInt("ID");
            String getReviews = "SELECT * FROM Reviews WHERE CourseID = " + id + "";
            rs = statement.executeQuery(getReviews);
            int average = 0;
            int count = 0;
            ArrayList<String> reviewsList = new ArrayList<String>();
            while(rs.next()){
                String review = rs.getString("Review");
                int rating = rs.getInt("Rating");
                reviewsList.add(review);
                System.out.println(rating);
                average += rating;
                count++;
            }
            //this is an atrocious way to do this, but it's easy
            double avg = ((double) average)/count;
            System.out.println("Average: " + avg);
            reviewsList.add(String.valueOf(avg));

            System.out.println("Course Average " + average/count + "/5");
            return reviewsList;

        }
        catch(SQLException e){
            throw new RuntimeException("Something went wrong");
        }
    }

    public void printReviews(String course){
        try{
            Statement statement = connection.createStatement();
            String[] split = course.split(" ");
            String getCourse = "SELECT ID FROM Courses WHERE Department = '" + split[0] + "' AND Catalog = '" + split[1] + "'";
            ResultSet rs = statement.executeQuery(getCourse);
            int id = rs.getInt("ID");
            String getReviews = "SELECT * FROM Reviews WHERE CourseID = " + id + "";
            rs = statement.executeQuery(getReviews);
            int average = 0;
            int count = 0;
            while(rs.next()){
                String review = rs.getString("Review");
                int rating = rs.getInt("Rating");
                System.out.println(review);
                average += rating;
                count++;
            }
            System.out.println("Course Average " + average/count + "/5");


        }
        catch(SQLException e){
            return;
        }
    }







}
