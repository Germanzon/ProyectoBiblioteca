
package utilerias;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class JPanelFondo extends JLabel {
    private final ImageIcon imagenFondo;

    public JPanelFondo(ImageIcon icon) {
        this.imagenFondo = icon;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.imagenFondo.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
