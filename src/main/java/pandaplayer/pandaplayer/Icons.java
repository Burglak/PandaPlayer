package pandaplayer.pandaplayer;

import javax.swing.*;
import java.awt.*;

public class Icons {
    private ImageIcon icons[];
    private ImageIcon logoIcons[];
    public Icons(){
        icons = new ImageIcon[5];
        for(int i = 0; i < icons.length; i++){
            icons[i] = new ImageIcon("img\\icon" + (i + 1) + ".png");
            Image originalImage = icons[i].getImage();
            icons[i] = new ImageIcon(originalImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        }

        logoIcons = new ImageIcon[2];
        for(int i = 0; i < logoIcons.length; i++){
            logoIcons[i] = new ImageIcon("img\\panda" + (i + 1) + ".png");
            Image originalImage = logoIcons[i].getImage();
            logoIcons[i] = new ImageIcon(originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        }
    }

    public ImageIcon getIcon(int i){
        if(i >= 0 && i <= 4){
            return icons[i];
        }
        else{
            return null;
        }
    }

    public ImageIcon getLogoIcon(int i){
        if(i >= 0 && i <= 1){
            return logoIcons[i];
        }
        else{
            return null;
        }
    }

}
//images from https://www.flaticon.com/

