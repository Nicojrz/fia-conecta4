package conecta4.ui;

import conecta4.model.Ficha;
import conecta4.model.Tablero;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.IntConsumer;
import javax.swing.JPanel;

public class PanelTablero extends JPanel
{
    private static final int CELDA = 80;
    private static final int MARGEN = 8;

    private Tablero tablero = new Tablero();

    public PanelTablero(IntConsumer alHacerClicEnColumna)
    {
        setPreferredSize(new Dimension(Tablero.COLUMNAS * CELDA, Tablero.FILAS * CELDA));
        setBackground(new Color(30, 80, 190));
        addMouseListener
        (
            new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    alHacerClicEnColumna.accept(e.getX() / CELDA);
                }
            }
        );
    }

    public void establecerTablero(Tablero tablero)
    {
        this.tablero = tablero;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int fila = 0; fila < Tablero.FILAS; fila++)
        {
            for (int columna = 0; columna < Tablero.COLUMNAS; columna++)
            {
                g2.setColor(colorDeFicha(tablero.get(fila, columna)));
                g2.fillOval(columna * CELDA + MARGEN, fila * CELDA + MARGEN, CELDA - 2 * MARGEN, CELDA - 2 * MARGEN);
            }
        }
    }

    private Color colorDeFicha(Ficha ficha)
    {
        return switch (ficha)
        {
            case ROJA -> new Color(220, 40, 40);
            case AMARILLA -> new Color(250, 220, 40);
            default -> Color.WHITE;
        };
    }
}
