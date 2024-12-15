package pandaplayer.pandaplayer;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoPanel extends JPanel {

    public LogoPanel() {
        this.setLayout(null);
        this.setBackground(new Color(90, 90, 90));
        this.setPreferredSize(new Dimension(350, 600));
        Icons icons = new Icons();
        JLabel label = new JLabel();
        label.setIcon(icons.getLogoIcon(0));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.BOTTOM);
        label.setBounds(120, 200, 100, 100);
        this.add(label);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setIcon(icons.getLogoIcon(1));
                ((Timer)e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
