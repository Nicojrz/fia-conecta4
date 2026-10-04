package conecta4.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tablero 
{
    public static final int FILAS = 6;
    public static final int COLUMNAS = 7;
    public static final int CONECTA = 4;

    private static final int[] ORDEN_COLUMNAS = {3, 2, 4, 1, 5, 0, 6};

    public static final int[][] DIRECCIONES = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

    private final Ficha[][] cuadrilla;
    private Ficha ganador = Ficha.NINGUNA;
    private int contadorMovimientos = 0;

    public Tablero()
    {
        cuadrilla = new Ficha[FILAS][COLUMNAS];
        
        for (Ficha[] fila : cuadrilla)
            Arrays.fill(fila, Ficha.NINGUNA);
    }

    private Tablero(Tablero otro)
    {
        cuadrilla = new Ficha[FILAS][];
        
        for (int fila = 0; fila < FILAS; fila++)
            cuadrilla[fila] = otro.cuadrilla[fila].clone();
        
        ganador = otro.ganador;
        contadorMovimientos = otro.contadorMovimientos;
    }
    
    public Ficha get(int fila, int columna)
    {
        return cuadrilla[fila][columna];
    }

    public static boolean enLimites(int fila, int columna)
    {
        return fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS;
    }

    public boolean estaColumnaLlena(int columna)
    {
        return cuadrilla[0][columna] != Ficha.NINGUNA;
    }

    public boolean estaLleno()
    {
        return contadorMovimientos == FILAS * COLUMNAS;
    }

    public Ficha ganador()
    {
        return ganador;
    }

    public boolean estaTerminal()
    {
        return ganador != Ficha.NINGUNA || estaLleno();
    }
    
    public List<Integer> movimientosValidos()
    {
        List<Integer> movimientos = new ArrayList<>(COLUMNAS);
        
        for (int columna : ORDEN_COLUMNAS)
            if (!estaColumnaLlena(columna))
                movimientos.add(columna);
        
        return movimientos;
    }

    public Tablero soltar(int columna, Ficha ficha)
    {
        if (columna < 0 || columna >= COLUMNAS || estaColumnaLlena(columna))
            throw new IllegalArgumentException("Jugada inválida: columna " + columna);
        
        int fila = FILAS - 1;
        
        while (cuadrilla[fila][columna] != Ficha.NINGUNA)
            fila--;
        
        Tablero siguiente = new Tablero(this);
        siguiente.cuadrilla[fila][columna] = ficha;
        siguiente.contadorMovimientos++;
        
        if (siguiente.completaCuatro(fila, columna, ficha))
            siguiente.ganador = ficha;
        
        return siguiente;
    }

    private boolean completaCuatro(int fila, int columna, Ficha ficha)
    {
        for (int[] direccion : DIRECCIONES)
        {
            int total = 1 + contarEnDireccion(fila, columna, direccion[0], direccion[1], ficha) + contarEnDireccion(fila, columna, -direccion[0], -direccion[1], ficha);
            
            if (total >= CONECTA)
                return true;
        }
        return false;
    }

    private int contarEnDireccion(int fila, int columna, int incrementoFila, int incrementoColumna, Ficha ficha)
    {
        int contador = 0;
        int filaActual = fila + incrementoFila;
        int columnaActual = columna + incrementoColumna;
        
        while (enLimites(filaActual, columnaActual) && cuadrilla[filaActual][columnaActual] == ficha)
        {
            contador++;
            filaActual += incrementoFila;
            columnaActual += incrementoColumna;
        }
        
        return contador;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        for (Ficha[] fila : cuadrilla)
        {
            for (Ficha ficha : fila)
                sb.append(ficha == Ficha.ROJA ? 'X' : ficha == Ficha.AMARILLA ? 'O' : '.').append(' ');
            
            sb.append('\n');
        }
        return sb.toString();
    }
}
