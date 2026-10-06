package ai0929.gui;

import java.awt.*;

public class CenterFrame {
    public  static int[] getLocation(int w, int h){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screensizeDim = toolkit.getScreenSize();
        int sw = screensizeDim.width;
        int sh = screensizeDim.height;
        int x = (sw-w)/2;
        int y = (sh-h)/2;
        int[] location = {x,y};

        return location;
    }
}
