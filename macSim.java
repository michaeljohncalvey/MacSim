import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class MacSim extends Applet implements ActionListener {

    // App variables
    InputManager input;
    SimCanvas c;
    Graph graph;

    // Simulation variables
    Country country;
    long tick;

    public void init(){
        startSimulation();
    }

    public void startSimulation() {
        // Setup instant vars
        log("Starting Simulation...");
        log("");
        setLayout(new BorderLayout());
        reset(20);
        graph = new Graph();
        add("Center", graph);

        Timer timer = new Timer(0, this);
        timer.setInitialDelay(1000);
        timer.start();
    }

    Country createCountry(double rate) {

        return new Country(15000, 500, rate);
    }

    void sim(Country country) {
        tick++; // advance simulation by 1 tick

        if(tick == 2500) {
            // log("\n\n\n");
            log("Total GDP at start: " + country.startGdp);
            log("Total GDP at finish: " + country.yGdp);
            log("Total growth rate since start: " + country.getTotalGrowthRate());
            log("Annualized growth since start: " + country.getAnnualizedGrowthRate());
            log("   or: " + country.getAnnualizedGrowthRate() * 100 + "%");
            log(" \n ");
            reset(35);
            return;
        }
        // p(".");
        if(tick%100 == 0 && tick > 0) {
            // One year
            // log(" ");
            // log(" ");
            // log("Year: " + tick/100);
            // log("A year has passed");
            // log(" ");
        } else if(tick%25 == 0 && tick > 25) {
            // One quarter
            // log(" ");
            // log("A quarter has passed");
        }
        country.tick(tick);

        graph.addPoint(new Coord((long)(tick), country.gdp));
        graph.repaint();
    }

    void reset(double rate) {
        country = createCountry(rate);
        tick = 0;
    }

    // simloop scheduler
    public void actionPerformed(ActionEvent e) {
        if(tick > 2500) return;
        sim(country);
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public static void p(String s) {
        System.out.print(s);
    }
}

class SimCanvas extends Canvas {
    // References for drawing

        Dimension size;
        MacSim parent;
        Image offscreen;
        Dimension offscreensize;
        Graphics g2;

        // Constructor
        public SimCanvas(MacSim m){
            parent = m;
            size = new Dimension(600, 400);
        }

        // Redraw canvas handling
        public void paint(Graphics g) {
            update(g);
        }

        // Redraw logic
        public void update(Graphics g) {
            // initially (or when size changes) create new image
            if ((offscreen == null)
                || (size.width != offscreensize.width)
                || (size.height != offscreensize.height)) {
                offscreen = createImage(size.width, size.height);
                offscreensize = size;
                g2 = offscreen.getGraphics();
                g2.setFont(getFont());
            }

            // erase old contents:
            g2.setColor(getBackground());
            g2.fillRect(0, 0, size.width, size.height);

            // now, draw as usual, but use g2 instead of g
            // draw stuff here boi

            // finally, draw the image on top of the old one
            g.drawImage(offscreen, 0, 0, null);
        }
}

class InputManager extends KeyAdapter {

    // Key array
    boolean[] keys = new boolean[65536];

    // returns key array
    public boolean[] getInput() {
        boolean[] temp = keys;
        resetKeys();
        return temp;
    }

    // clears key array
    void resetKeys() {
        keys = new boolean[65536];
    }

    // called on keyPressed event run
    public void keyPressed(KeyEvent e) { // handles keypresses
        int keyCode = e.getKeyCode(); // gets keycode
        keys[keyCode] = true;
    }
}
