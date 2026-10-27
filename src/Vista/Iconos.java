/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
/**
 *
 * @author Nestor Marchizone
 */
public class Iconos {
       private static final String RUTA = "/imagenes/aborigym_icono.png";
    private static final int TAMANIO = 20;

    private static ImageIcon icono;

    private Iconos() {
    }

    public static ImageIcon ventana() {
        if (icono == null) {
            URL url = Iconos.class.getResource(RUTA);
            if (url == null) {
                System.err.println("No se encontró la imagen: " + RUTA);
                return null;
            }
            Image img = new ImageIcon(url).getImage()
                    .getScaledInstance(TAMANIO, TAMANIO, Image.SCALE_SMOOTH);
            icono = new ImageIcon(img);
        }
        return icono;
    }

    public static void aplicar(JInternalFrame ventana) {
        ImageIcon i = ventana();
        if (i != null) {
            ventana.setFrameIcon(i);
        }
    }
}
