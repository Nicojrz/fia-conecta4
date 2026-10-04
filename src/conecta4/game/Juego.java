package conecta4.game;

import conecta4.ai.MinimaxAlfaBeta;
import conecta4.ai.ResultadoBusqueda;
import conecta4.model.Ficha;
import conecta4.model.Tablero;

/**
 * Reglas y turnos de una partida humano (ROJA) vs computadora (AMARILLA).
 * No conoce la interfaz gráfica y usa Minimax para los turnos de la computadora.
 */
public class Juego {

    public static final Ficha HUMANO = Ficha.ROJA;
    public static final Ficha COMPUTADORA = Ficha.AMARILLA;

    private Tablero tablero = new Tablero();
    private Ficha turno = HUMANO;
    private MinimaxAlfaBeta estrategiaComputadora;

    public Juego(MinimaxAlfaBeta estrategiaComputadora) {
        this.estrategiaComputadora = estrategiaComputadora;
    }

    public void establecerEstrategiaComputadora(MinimaxAlfaBeta estrategia) {
        this.estrategiaComputadora = estrategia;
    }

    public void reiniciar() {
        tablero = new Tablero();
        turno = HUMANO;
    }

    public Tablero tablero() {
        return tablero;
    }

    public Ficha turno() {
        return turno;
    }

    public boolean estaFinalizado() {
        return tablero.estaTerminal();
    }

    public Ficha ganador() {
        return tablero.ganador();
    }

    public boolean esTurnoHumano() {
        return !estaFinalizado() && turno == HUMANO;
    }

    /** Jugada del humano. Devuelve false si no es su turno o la jugada es inválida. */
    public boolean jugarHumano(int columna) {
        if (!esTurnoHumano() || columna < 0 || columna >= Tablero.COLUMNAS || tablero.estaColumnaLlena(columna)) {
            return false;
        }
        aplicarMovimiento(columna);
        return true;
    }

    /** Calcula y aplica la jugada de la computadora. */
    public ResultadoBusqueda jugarComputadora() {
        if (estaFinalizado() || turno != COMPUTADORA) {
            throw new IllegalStateException("No es el turno de la computadora");
        }
        ResultadoBusqueda resultado = estrategiaComputadora.elegirMovimiento(tablero, COMPUTADORA);
        aplicarMovimiento(resultado.columna());
        return resultado;
    }

    private void aplicarMovimiento(int columna) {
        tablero = tablero.soltar(columna, turno);
        turno = turno.oponente();
    }
}
