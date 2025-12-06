package objects;

// Interface a implementar nos objetos que têm movimentos que ocorrem conforme os ticks do jogo 
public interface Gravity {
    public boolean canSpecialMov(); // Verifica se esse movimento pode ocorrer
    public void specialmov(); // O movimento em si 
    public void specialAbillity(); // E se há algum tipo de habilidade especial no fnal do movimento 
}
