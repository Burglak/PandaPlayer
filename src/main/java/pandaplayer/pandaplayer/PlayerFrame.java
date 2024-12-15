package pandaplayer.pandaplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerFrame extends JFrame {
    private int FRAME_WIDTH = 350;
    private int FRAME_HEIGTH = 600;
    private PlayerPanel playerPanel;

    public PlayerFrame(){
        this.setTitle("Panda Player v0.1");
        this.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        playerPanel = new PlayerPanel();
        LogoPanel logoPanel = new LogoPanel();
        this.add(logoPanel);

        this.setVisible(true);

        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(logoPanel);
                add(playerPanel);
                revalidate();
                repaint();
            }
        });

        timer.setRepeats(false);
        timer.start();
    }
}

