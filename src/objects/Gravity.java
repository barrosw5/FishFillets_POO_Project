package objects;


// A Interface Gravity passa a estar integrada diretamente em cada objeto em vez de na classe Movable
// Isto acontece porque a partir de agora nem todos têm os mesmo movimentos por ação da gravidade
// Uns sobem (outros deixem)
// Por isso é que também mudei o nome dos métodos. Passou de canFall para canSpecialNov e de fall para specialMov 
// Se quiseres mudar o nome estás à vontade. Tens mais criatividade do que eu, portanto, tua escolha
public interface Gravity {
    public boolean canSpecialMov();
    public void specialmov();
    public void specialAbillity();
}
