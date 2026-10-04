package conecta4.ui;

import conecta4.ai.HeuristicaVentana;
import conecta4.ai.MinimaxAlfaBeta;
import conecta4.ai.ResultadoBusqueda;
import conecta4.game.Juego;
import conecta4.model.Ficha;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

public class VentanaConecta4 extends JFrame
{
    private final HeuristicaVentana heuristica;
    private final Juego juego;
    private final PanelTablero panelTablero;
    private final JSpinner profundidadSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
    private final JLabel etiquetaEstado = new JLabel();
    private boolean computadoraPensando = false;

    public VentanaConecta4(HeuristicaVentana heuristica)
    {
        super("Conecta-4 - Minimax con poda alfa-beta");
        this.heuristica = heuristica;
        this.juego = new Juego(nuevaEstrategia());
        this.panelTablero = new PanelTablero(this::alHacerClicEnColumna);

        JButton nuevoJuego = new JButton("Nuevo juego");
        nuevoJuego.addActionListener(e -> reiniciarJuego());

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        superior.add(new JLabel("Profundidad:"));
        superior.add(profundidadSpinner);
        superior.add(nuevoJuego);

        add(superior, BorderLayout.NORTH);
        add(panelTablero, BorderLayout.CENTER);
        add(etiquetaEstado, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        actualizar("Tu turno (rojo). Haz clic en una columna.");
    }

    private MinimaxAlfaBeta nuevaEstrategia()
    {
        return new MinimaxAlfaBeta((Integer) profundidadSpinner.getValue(), heuristica);
    }

    private void reiniciarJuego()
    {
        if (computadoraPensando)
            return;
        
        juego.establecerEstrategiaComputadora(nuevaEstrategia());
        juego.reiniciar();
        actualizar("Nuevo juego. Tu turno (rojo).");
    }

    private void alHacerClicEnColumna(int columna)
    {
        if (computadoraPensando || !juego.jugarHumano(columna))
            return;
        
        actualizar(null);
        
        if (!juego.estaFinalizado())
            jugarComputadoraEnSegundoPlano();
    }
    
    private void jugarComputadoraEnSegundoPlano()
    {
        computadoraPensando = true;
        juego.establecerEstrategiaComputadora(nuevaEstrategia());
        etiquetaEstado.setText("La computadora está pensando...");

        new SwingWorker<ResultadoBusqueda, Void>()
        {
            @Override
            protected ResultadoBusqueda doInBackground()
            {
                return juego.jugarComputadora();
            }

            @Override
            protected void done()
            {
                computadoraPensando = false;
                try
                {
                    ResultadoBusqueda resultado = get();
                    actualizar(String.format("Computadora: columna %d | nodos: %,d | podas: %,d | %d ms",
                            resultado.columna() + 1, resultado.nodos(), resultado.podas(), resultado.milisegundos()));
                }
                catch (InterruptedException | ExecutionException ex)
                {
                    etiquetaEstado.setText("Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void actualizar(String mensaje)
    {
        panelTablero.establecerTablero(juego.tablero());
        if (juego.estaFinalizado())
        {
            Ficha ganador = juego.ganador();
            String resultado = ganador == Juego.HUMANO ? "¡Ganaste!" : ganador == Juego.COMPUTADORA ? "Gana la computadora." : "Empate.";
            etiquetaEstado.setText(resultado + (mensaje != null ? "  [" + mensaje + "]" : ""));
        }
        else if (mensaje != null)
        {
            etiquetaEstado.setText(mensaje);
        }
    }
}
