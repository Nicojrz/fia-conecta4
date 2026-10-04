package conecta4.ai;

import conecta4.model.Ficha;
import conecta4.model.Tablero;

public class HeuristicaVentana
{
    private static final int TRES_PROPIAS = 5;
    private static final int DOS_PROPIAS = 2;
    private static final int TRES_OPONENTE = -4;
    private static final int DOS_OPONENTE = -1;
    private static final int CENTRO = 3;

    public int evaluar(Tablero tablero, Ficha yo)
    {
        Ficha rival = yo.oponente();
        int puntuacion = puntajeCentro(tablero, yo, rival);

        for (int fila = 0; fila < Tablero.FILAS; fila++)
        {
            for (int columna = 0; columna < Tablero.COLUMNAS; columna++)
            {
                for (int[] direccion : Tablero.DIRECCIONES)
                {
                    int finFila = fila + direccion[0] * (Tablero.CONECTA - 1);
                    int finColumna = columna + direccion[1] * (Tablero.CONECTA - 1);
                    
                    if (Tablero.enLimites(finFila, finColumna))
                    {
                        puntuacion += puntuarVentana(tablero, fila, columna, direccion[0], direccion[1], yo, rival);
                    }
                }
            }
        }
        
        return puntuacion;
    }

    private int puntajeCentro(Tablero tablero, Ficha yo, Ficha rival)
    {
        int centro = Tablero.COLUMNAS / 2;
        int puntuacion = 0;
        
        for (int fila = 0; fila < Tablero.FILAS; fila++)
        {
            Ficha ficha = tablero.get(fila, centro);

            if (ficha == yo)
                puntuacion += CENTRO;
            else if (ficha == rival)
                puntuacion -= CENTRO;
        }
        
        return puntuacion;
    }

    private int puntuarVentana(Tablero tablero, int fila, int columna, int incrementoFila, int incrementoColumna, Ficha yo, Ficha rival)
    {
        int mias = 0;
        int suyas = 0;
        for (int i = 0; i < Tablero.CONECTA; i++)
        {
            Ficha ficha = tablero.get(fila + incrementoFila * i, columna + incrementoColumna * i);
            if (ficha == yo)
            {
                mias++;
            }
            else if (ficha == rival)
            {
                suyas++;
            }
        }
        
        if (mias > 0 && suyas > 0)
        {
            return 0;
        }
        
        if (mias == 3) return TRES_PROPIAS;
        if (mias == 2) return DOS_PROPIAS;
        if (suyas == 3) return TRES_OPONENTE;
        if (suyas == 2) return DOS_OPONENTE;
        
        return 0;
    }
}
