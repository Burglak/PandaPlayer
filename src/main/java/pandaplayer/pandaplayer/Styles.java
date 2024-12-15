package pandaplayer.pandaplayer;

import javax.swing.*;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Styles {
    private PlayerPanel panel;
    private JTextField trackName;
    private JTextField trackTime;
    private JProgressBar trackProgressBar;
    private JButton[] trackButtons;
    private JLabel playlistLabel;
    private JButton[] playlistButtons;
    private JTable tracksList;
    private JSlider slider;
    private JLabel sliderLabel;
    private DefaultTableModel tableModel;

    public Styles(PlayerPanel panel, JTextField trackName, JTextField trackTime, JProgressBar trackProgressBar, JButton[] trackButtons, JLabel playlistLabel, JButton[] playlistButtons, JTable tracksList, JSlider slider, JLabel sliderLabel, DefaultTableModel tableModel) {
        this.panel = panel;
        this.trackName = trackName;
        this.trackTime = trackTime;
        this.trackProgressBar = trackProgressBar;
        this.trackButtons = trackButtons;
        this.playlistLabel = playlistLabel;
        this.playlistButtons = playlistButtons;
        this.tracksList = tracksList;
        this.slider = slider;
        this.sliderLabel = sliderLabel;
        this.tableModel = tableModel;
    }
    public void applyStyles(){
        //playerPanel
        panel.setBackground(new Color(36, 36, 36));

        //Track name JTextField
        trackName.setBackground(new Color(90, 90, 90));
        trackName.setBorder(null);
        trackName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        trackName.setMargin(new Insets(5, 5, 5, 5));
        trackName.setFont(new Font("Verdana", Font.BOLD, 10));
        trackName.setForeground(Color.white);

        //Track time JTextField
        trackTime.setBackground(new Color(90, 90, 90));
        trackTime.setBorder(null);
        trackTime.setHorizontalAlignment(SwingConstants.CENTER);
        trackTime.setFont(new Font("Verdana", Font.BOLD, 12));
        trackTime.setForeground(Color.white);

        //Progress Bar
        trackProgressBar.setBorder(null);
        trackProgressBar.setUI((ProgressBarUI) BasicProgressBarUI.createUI(trackProgressBar));
        trackProgressBar.setBackground(new Color(90, 90, 90));
        trackProgressBar.setForeground(new Color(35, 170, 50));

        //Track buttons JButton
        UIManager.put("Button.select", Color.DARK_GRAY);
        UIManager.put("Button.hover", new Color(70, 70, 70));
        for(int i = 0; i < trackButtons.length; i++){
            trackButtons[i].setFocusable(false);
            trackButtons[i].setBorder(null);
            trackButtons[i].setBackground(new Color(90, 90, 90));
        }

        //Volume slider label
        sliderLabel.setForeground(Color.white);

        //Volume slider JSlider
        slider.setBorder(null);
        slider.setBackground(new Color(90, 90, 90));
        slider.setUI(new BasicSliderUI(slider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Rectangle trackBounds = trackRect;

                g2d.setPaint(new Color(90, 90, 90));
                g2d.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }
        });

        //Playlist label
        playlistLabel.setForeground(Color.white);

        //Playlist buttons
        for(int i = 0; i < playlistButtons.length; i++){
            playlistButtons[i].setFocusable(false);
            playlistButtons[i].setBorder(null);
            playlistButtons[i].setBackground(new Color(90, 90, 90));
            playlistButtons[i].setForeground(Color.white);
        }

        //Playlist Table JTable
        tracksList.setBackground(new Color(65, 65, 65));
        tracksList.setForeground(Color.white);
        tracksList.setShowGrid(false);

    }

}

