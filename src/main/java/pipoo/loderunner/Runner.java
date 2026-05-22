package pipoo.loderunner;

import pipoo.core.Movible;

public abstract class Runner extends Movible {
    protected boolean estaCayendo;

    public Runner(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.estaCayendo = false;
    }
    public boolean isEstaCayendo() {
        return estaCayendo;
    }

    public void setEstaCayendo(boolean estaCayendo) {
        this.estaCayendo = estaCayendo;
    }
}
