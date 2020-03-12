package builder;

/**
 * 建造者模式：
 */
public class Computer {

    private Cpu cpu;

    private Ram ram;

    private GraphicsCard graphicsCard;

    //..........


    public Computer(Cpu cpu, Ram ram, GraphicsCard graphicsCard) {
        this.cpu = cpu;
        this.ram = ram;
        this.graphicsCard = graphicsCard;
    }

    public Cpu getCpu() {
        return cpu;
    }

    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    public Ram getRam() {
        return ram;
    }

    public void setRam(Ram ram) {
        this.ram = ram;
    }

    public GraphicsCard getGraphicsCard() {
        return graphicsCard;
    }

    public void setGraphicsCard(GraphicsCard graphicsCard) {
        this.graphicsCard = graphicsCard;
    }
}
