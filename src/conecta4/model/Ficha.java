package conecta4.model;

public enum Ficha
{
    NINGUNA, ROJA, AMARILLA;

    public Ficha oponente()
    {
        return switch (this)
        {
            case ROJA -> AMARILLA;
            case AMARILLA -> ROJA;
            default -> NINGUNA;
        };
    }
}