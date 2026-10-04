package conecta4.ai;

import conecta4.model.Ficha;
import conecta4.model.Tablero;

public class MinimaxAlfaBeta
{
    private static final int INFINITO = 1_000_000_000;
    private static final int PUNTAJE_GANAR = 1_000_000;

    private final int profundidadMaxima;
    private final HeuristicaVentana heuristica;
    private final boolean poda;

    private long nodos;
    private long podas;

    public MinimaxAlfaBeta(int profundidadMaxima, HeuristicaVentana heuristica)
    {
        this(profundidadMaxima, heuristica, true);
    }

    public MinimaxAlfaBeta(int profundidadMaxima, HeuristicaVentana heuristica, boolean poda)
    {
        if (profundidadMaxima < 1)
            throw new IllegalArgumentException("La profundidad debe ser >= 1");
        
        this.profundidadMaxima = profundidadMaxima;
        this.heuristica = heuristica;
        this.poda = poda;
    }

    public ResultadoBusqueda elegirMovimiento(Tablero tablero, Ficha jugador)
    {
        nodos = 0;
        podas = 0;
        long inicio = System.nanoTime();

        int mejorColumna = -1;
        int mejorPuntuacion = -INFINITO;
        int alpha = -INFINITO;
        int beta = INFINITO;

        for (int columna : tablero.movimientosValidos())
        {
            Tablero hijo = tablero.soltar(columna, jugador);
            int puntuacion = minimax(hijo, profundidadMaxima - 1, alpha, beta, false, jugador);
            
            if (puntuacion > mejorPuntuacion)
            {
                mejorPuntuacion = puntuacion;
                mejorColumna = columna;
            }
            
            alpha = Math.max(alpha, mejorPuntuacion);
        }

        long milisegundos = (System.nanoTime() - inicio) / 1_000_000;
        return new ResultadoBusqueda(mejorColumna, mejorPuntuacion, nodos, podas, milisegundos);
    }

    private int minimax(Tablero tablero, int profundidad, int alpha, int beta, boolean maximizando, Ficha yo)
    {
        nodos++;

        Ficha ganador = tablero.ganador();
        if (ganador == yo)
            return PUNTAJE_GANAR + profundidad;
        
        if (ganador != Ficha.NINGUNA)
            return -PUNTAJE_GANAR - profundidad;
        
        if (tablero.estaLleno())
            return 0;

        if (profundidad == 0)
            return heuristica.evaluar(tablero, yo);
        
        Ficha turno = maximizando ? yo : yo.oponente();

        if (maximizando)
        {
            int mejor = -INFINITO;
            
            for (int columna : tablero.movimientosValidos())
            {
                Tablero hijo = tablero.soltar(columna, turno);
                mejor = Math.max(mejor, minimax(hijo, profundidad - 1, alpha, beta, false, yo));
                alpha = Math.max(alpha, mejor);
                
                if (poda && alpha >= beta)
                {
                    podas++;
                    break;
                }
            }
            
            return mejor;
        }
        else
        {
            int mejor = INFINITO;
            
            for (int columna : tablero.movimientosValidos())
            {
                Tablero hijo = tablero.soltar(columna, turno);
                mejor = Math.min(mejor, minimax(hijo, profundidad - 1, alpha, beta, true, yo));
                beta = Math.min(beta, mejor);
                if (poda && alpha >= beta)
                {
                    podas++;
                    break;
                }
            }
            return mejor;
        }
    }
}
