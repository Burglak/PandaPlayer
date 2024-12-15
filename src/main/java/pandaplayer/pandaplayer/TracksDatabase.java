package pandaplayer.pandaplayer;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;


public class TracksDatabase {
    private static final String DRIVER = "org.sqlite.JDBC";
    //private static String DB_URL = "jdbc:sqlite:src\\db\\trackDB.db";
    private static String DB_URL = "jdbc:sqlite:db\\trackDB.db";
    private static Connection connection;
    private File currentFile;
    private String currentFilePath;
    private String currentFileName;
    private JTextField name;
    private JTextField time;
    private DefaultTableModel table;
    private JProgressBar progressBar;
    JFXPanel fxPanel = new JFXPanel();



    Media media;
    MediaPlayer mediaPlayer;


    public TracksDatabase(JTextField name, JTextField time, DefaultTableModel table, JProgressBar progressBar) {
        this.name = name;
        this.time = time;
        this.table = table;
        this.progressBar = progressBar;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            if (connection != null) {
                System.out.println("Connection to the database has been established.");
                Statement statement = connection.createStatement();

                String sql = "SELECT * FROM tracks LIMIT 1";
                ResultSet resultSet = statement.executeQuery(sql);
                if(resultSet.next()){
                    String trackName = resultSet.getString("track_name");
                    String trackPath = resultSet.getString("track_path");

                    currentFile = new File(trackPath);
                    currentFilePath = trackPath;
                    currentFileName = trackName;
                }
                //filling up table
                sql = "SELECT * FROM tracks";
                resultSet = statement.executeQuery(sql);
                while(resultSet.next()){
                    table.addRow(new Object[]{resultSet.getString("track_name"), resultSet.getString("track_duration")});
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to establish connection to the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getCurrentFile(){
        return currentFile;
    }

    public String getCurrentFilePath(){
        return currentFilePath;
    }

    public String getCurrentFileName(){
        return currentFileName;
    }
    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    //dodac zmiane tytulu kiedy nie ma zadnego utworu w bazie
    public void addTrack() {
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("mp3 File", "mp3");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("Wybrany plik MP3: " + filePath);

            try {
                //checking if the selected track is in the base
                String sql = "SELECT COUNT(*) AS count FROM tracks WHERE track_path = ?";
                String trackPath = filePath;

                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, trackPath);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.getInt("count") > 0){
                    JOptionPane.showMessageDialog(null, "Track already added");
                    return;
                }

                //if database is empty change track name field for selected track
                String sql2 = "SELECT COUNT(*) AS count FROM tracks";
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(sql2);
                if(resultSet2.getInt("count") == 0){
                    name.setText(selectedFile.getName());
                    time.setText("00:00");
                    currentFile = new File(trackPath);
                    currentFilePath = trackPath;
                    currentFileName = currentFile.getName();
                }

                //getting track duration
                AtomicReference<String> trackDuration = new AtomicReference<>("00:00");

                JFXPanel fxPanel = new JFXPanel();
                Media media = new Media(selectedFile.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);

                mediaPlayer.setOnReady(() -> {
                    Duration duration = media.getDuration();
                    int minutes = (int) duration.toMinutes();
                    int seconds = (int) (duration.toSeconds() % 60);
                    trackDuration.set(String.format("%02d:%02d", minutes, seconds));

                    try {
                        addToDatabase(selectedFile.getName(), trackDuration.get(), filePath);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to load track");
            }
        }
    }
    public void removeTrack() {
        try{
            mediaPlayer.stop();
        }
        catch(Exception e){

        }
        try {
            String sql = "DELETE FROM tracks WHERE track_path = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, currentFilePath);

            statement.executeUpdate();

            int rowCount = table.getRowCount();
            for(int i = rowCount - 1; i >= 0; i--){
                if(table.getValueAt(i, 0).equals(currentFileName)){
                    table.removeRow(i);
                }
            }


            Statement statement2 = connection.createStatement();
            sql = "SELECT * FROM tracks LIMIT 1";

            ResultSet resultSet = statement2.executeQuery(sql);
            if(resultSet.next()){
                String trackName = resultSet.getString("track_name");
                String trackPath = resultSet.getString("track_path");

                currentFile = new File(trackPath);
                currentFilePath = trackPath;
                currentFileName = trackName;
                name.setText(currentFileName);
                media = new Media(currentFile.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                beginTimer();
            }
            else{
                currentFilePath = null;
                currentFile = null;
                currentFileName = null;
                media = null;
                mediaPlayer = null;
                name.setText("NO TRACKS HAVE BEEN ADDED");
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAll(){
        try{
            mediaPlayer.stop();
        }
        catch(NullPointerException e){

        }
        try{
            String sql = "DELETE FROM tracks";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            int rowCount = table.getRowCount();
            for(int i = rowCount - 1; i >= 0; i--){
                table.removeRow(i);
            }

            currentFilePath = null;
            currentFile = null;
            currentFileName = null;
            media = null;
            mediaPlayer = null;
            name.setText("NO TRACKS HAVE BEEN ADDED");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void playTrack(){

        try {
            if(media == null) {
                media = new Media(currentFile.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
            }
            mediaPlayer.play();
            beginTimer();
        }
        catch(Exception e){
        }
    }
    public void pauseTrack(){
        mediaPlayer.pause();
    }
    public void stopTrack(){
        mediaPlayer.stop();
    }
    public void nextTrack(){
        try {
            mediaPlayer.stop();
        }
        catch (NullPointerException e){

        }
        String sql = "SELECT * FROM tracks";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int rowCount = 0;

            while(resultSet.next()){
                rowCount++;
            }
            if(rowCount == 0){
                return;
            }
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getString("track_path").equals(currentFilePath)){
                    if(resultSet.next()) {
                        currentFile = new File(resultSet.getString("track_path"));
                        currentFileName = currentFile.getName();
                        currentFilePath = currentFile.getPath();
                        name.setText(currentFileName);
                        media = new Media(currentFile.toURI().toString());
                        mediaPlayer = new MediaPlayer(media);
                        playTrack();
                        return;
                    }
                }
            }
            resultSet = statement.executeQuery();
            currentFile = new File(resultSet.getString("track_path"));
            currentFileName = currentFile.getName();
            currentFilePath = currentFile.getPath();
            name.setText(currentFileName);
            time.setText("00:00");
            media = new Media(currentFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            playTrack();
        }
        catch(Exception e){

        }
    }
    public void previousTrack(){
        try{
            mediaPlayer.stop();
        }
        catch(Exception e){

        }
        try {
            String sql = "SELECT * FROM tracks";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int rowCount = 0;
            while(resultSet.next()){
                rowCount++;
            }
            if(rowCount <= 1){
                return;
            }
            resultSet = statement.executeQuery();
            String filePath = resultSet.getString("track_path");
            while(resultSet.next()){
                if(resultSet.getString("track_path").equals(currentFilePath)){
                    currentFile = new File(filePath);
                    currentFileName = currentFile.getName();
                    currentFilePath = currentFile.getPath();
                    name.setText(currentFileName);
                    time.setText("00:00");
                    media = new Media(currentFile.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    playTrack();
                    break;
                }
                else{
                    filePath = resultSet.getString("track_path");
                }
            }

        }
        catch (Exception e){

        }
    }
    //copied from chat
    public void beginTimer(){
        mediaPlayer.setOnReady(() -> {
            Duration duration = media.getDuration();
            progressBar.setMaximum((int) duration.toSeconds() - 1);
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                progressBar.setValue((int) newValue.toSeconds());
                //change text area with timer
                int seconds = (int) newValue.toSeconds() % 60;
                int minutes = (int) newValue.toSeconds() / 60;
                String formattedTime = String.format("%02d:%02d", minutes, seconds);
                time.setText(formattedTime);
                if((int) newValue.toSeconds() == (int) duration.toSeconds() - 1){
                    nextTrack();
                }
            });
        });
    }

    private void addToDatabase(String name, String duration, String path) throws SQLException {
        String sql = "INSERT INTO tracks (track_name, track_duration, track_path) VALUES (?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, name);
        statement.setString(2, duration);
        statement.setString(3, path);

        statement.executeUpdate();

        table.addRow(new Object[]{name, duration});
    }

}

