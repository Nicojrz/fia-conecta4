package conecta4;

import conecta4.ai.HeuristicaVentana;
import conecta4.ui.VentanaConecta4;

import javax.swing.SwingUtilities;

public class Principal
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new VentanaConecta4(new HeuristicaVentana()).setVisible(true));
    }
}
