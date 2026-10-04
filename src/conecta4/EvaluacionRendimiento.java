package conecta4;

import conecta4.ai.HeuristicaVentana;
import conecta4.ai.MinimaxAlfaBeta;
import conecta4.ai.ResultadoBusqueda;
import conecta4.model.Ficha;
import conecta4.model.Tablero;

public class EvaluacionRendimiento
{
    public static void main(String[] args)
    {
        HeuristicaVentana heuristica = new HeuristicaVentana();
        compararCostoBusqueda(heuristica);
        jugarTorneo(heuristica);
    }

    private static Tablero posicionMedioJuego()
    {
        int[] movimientos = {3, 3, 4, 2, 4, 4, 2, 5};
        Tablero tablero = new Tablero();
        Ficha turno = Ficha.ROJA;
        
        for (int columna : movimientos)
        {
            tablero = tablero.soltar(columna, turno);
            turno = turno.oponente();
        }
        
        return tablero;
    }

    private static void compararCostoBusqueda(HeuristicaVentana heuristica)
    {
        Tablero tablero = posicionMedioJuego();
        System.out.println("=== Costo de búsqueda desde una posición de medio juego ===");
        System.out.println(tablero);
        System.out.printf("%-4s | %-26s | %-34s%n", "Prof", "Minimax puro", "Minimax + alfa-beta");
        System.out.printf("%-4s | %10s %8s %6s | %10s %8s %8s %6s%n", "", "nodos", "ms", "col", "nodos", "ms", "podas", "col");

        for (int profundidad = 1; profundidad <= 7; profundidad++)
        {
            ResultadoBusqueda puro = null;
            
            if (profundidad <= 6)
                puro = new MinimaxAlfaBeta(profundidad, heuristica, false).elegirMovimiento(tablero, Ficha.ROJA);
            
            ResultadoBusqueda podado = new MinimaxAlfaBeta(profundidad, heuristica, true).elegirMovimiento(tablero, Ficha.ROJA);

            String izquierda = puro == null
                    ? String.format("%10s %8s %6s", "-", "-", "-")
                    : String.format("%,10d %8d %6d", puro.nodos(), puro.milisegundos(), puro.columna() + 1);
            
            System.out.printf("%-4d | %s | %,10d %8d %,8d %6d%n", profundidad, izquierda,
                    podado.nodos(), podado.milisegundos(), podado.podas(), podado.columna() + 1);
        }
        
        System.out.println();
    }

    private static void jugarTorneo(HeuristicaVentana heuristica)
    {
        System.out.println("=== Partidas computadora vs computadora (ROJA mueve primero) ===");
        int[] profundidades = {1, 3, 5};
        System.out.printf("%-12s %-12s %-10s %-6s%n", "ROJA prof", "AMARILLA prof", "Resultado", "Jugadas");
        
        for (int roja : profundidades)
        {
            for (int amarilla : profundidades)
            {
                MinimaxAlfaBeta rojaAi = new MinimaxAlfaBeta(roja, heuristica);
                MinimaxAlfaBeta amarillaAi = new MinimaxAlfaBeta(amarilla, heuristica);
                jugarPartido(roja, amarilla, rojaAi, amarillaAi);
            }
        }
    }

    private static void jugarPartido(int profundidadRoja, int profundidadAmarilla, MinimaxAlfaBeta rojaAi, MinimaxAlfaBeta amarillaAi)
    {
        Tablero tablero = new Tablero();
        Ficha turno = Ficha.ROJA;
        int movimientos = 0;
        
        while (!tablero.estaTerminal())
        {
            MinimaxAlfaBeta ai = turno == Ficha.ROJA ? rojaAi : amarillaAi;
            tablero = tablero.soltar(ai.elegirMovimiento(tablero, turno).columna(), turno);
            turno = turno.oponente();
            movimientos++;
        }
        
        String resultado = tablero.ganador() == Ficha.ROJA ? "gana ROJA"
                : tablero.ganador() == Ficha.AMARILLA ? "gana AMARILLA" : "empate";
        
        System.out.printf("%-12d %-12d %-10s %-6d%n", profundidadRoja, profundidadAmarilla, resultado, movimientos);
    }
}
