package pandaplayer.pandaplayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerPanel extends JPanel {
    private JTextField trackName;
    private JTextField trackTime;
    private JProgressBar trackProgressBar;
    private JButton[] trackButtons;
    private JLabel playlistLabel;
    private JButton[] playlistButtons;
    private JTable tracksList;
    private JSlider slider;
    private JLabel sliderLabel;
    private TracksDatabase database;
    public PlayerPanel() {
        this.setLayout(null);

        //adding track name field
        trackName = new JTextField("NO TRACKS HAVE BEEN ADDED");
        trackName.setEditable(false);
        trackName.setBounds(20, 20, 220, 40);
        this.add(trackName);

        //adding track progress bar
        trackProgressBar = new JProgressBar();
        trackProgressBar.setBounds(20, 80, 300, 20);
        this.add(trackProgressBar);

        //adding track time field
        trackTime = new JTextField("00:00");
        trackTime.setEditable(false);
        trackTime.setBounds(250, 20, 70, 40);
        this.add(trackTime);

        //adding track buttons
        Icons icons = new Icons();
        trackButtons = new JButton[5];
        int xPosition = 0;
        for (int i = 0; i < trackButtons.length; i++) {
            trackButtons[i] = new JButton(icons.getIcon(i));
            trackButtons[i].setBounds(20 + xPosition, 120, 60, 40);
            this.add(trackButtons[i]);
            xPosition += 60;
        }
        //adding slider
        slider = new JSlider();
        slider.setBounds(20, 190, 300, 20);
        this.add(slider);

        //adding slider label
        sliderLabel = new JLabel("VOLUME");
        sliderLabel.setBounds(0, 155, 340, 40);
        sliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(sliderLabel);

        //adding playlist label
        playlistLabel = new JLabel("PLAYLIST");
        playlistLabel.setBounds(0, 200, 340, 40);
        playlistLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(playlistLabel);

        //adding playlist buttons
        playlistButtons = new JButton[3];
        xPosition = 0;
        for (int i = 0; i < playlistButtons.length; i++) {
            playlistButtons[i] = new JButton();
            this.add(playlistButtons[i]);
            playlistButtons[i].setBounds(20 + xPosition, 250, 100, 40);
            playlistButtons[i].setMargin(new Insets(5, 5, 5, 5));
            xPosition += 100;
        }
        playlistButtons[0].setText("ADD TRACK");
        playlistButtons[1].setText("REMOVE");
        playlistButtons[2].setText("REMOVE ALL");

        //adding tracksList
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Track name");
        tableModel.addColumn("Track duration");

        tracksList = new JTable(tableModel);
        tracksList.setBounds(20, 290, 300, 260);
        tracksList.setEnabled(false);
        this.add(tracksList);

        tracksList.getColumnModel().getColumn(1).setMaxWidth(60);

        //setting up database
        database = new TracksDatabase(trackName, trackTime, tableModel, trackProgressBar);

        if (database.getCurrentFile() != null) {
            trackName.setText(database.getCurrentFileName());
        }

        playlistButtons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.addTrack();
            }
        });
        playlistButtons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.removeTrack();
            }
        });
        playlistButtons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.removeAll();
            }
        });
        trackButtons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.playTrack();
            }
        });
        trackButtons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.pauseTrack();
            }
        });
        trackButtons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.stopTrack();
            }
        });
        trackButtons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.nextTrack();
            }
        });
        trackButtons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.previousTrack();
            }
        });
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                database.getMediaPlayer().setVolume(slider.getValue() * 0.01);
            }
        });
        //applying styles
        Styles styles = new Styles(this, trackName, trackTime, trackProgressBar, trackButtons, playlistLabel, playlistButtons, tracksList, slider, sliderLabel, tableModel);
        styles.applyStyles();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.white);
        g.drawLine(0, 210, 350, 210);
        g.drawLine(0, 230, 350, 230);
        g.drawLine(0, 165, 350, 165);
        g.drawLine(0, 185, 350, 185);
    }
}
