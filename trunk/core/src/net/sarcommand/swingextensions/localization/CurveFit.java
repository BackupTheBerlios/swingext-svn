package net.sarcommand.swingextensions.localization;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

public class CurveFit extends JPanel implements ActionListener {
    ButtonManager buttonManager;
    Point[] points;
    Path2D.Double path;
    Line2D.Double[] connectors;
    boolean showConnections = false;
    boolean removePoint = false;
    boolean firstTime = true;

    CurveFit() {
        int[][] cds = {
            { 100, 150 }, { 220, 300 }, { 260, 150 }
        };
        points = new Point[cds.length];
        for(int j = 0; j < cds.length; j++) {
            points[j] = new Point(cds[j][0], cds[j][1]);
        }
        path = new Path2D.Double();
    }

    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if(ac.equals("add")) {
            connectors = new Line2D.Double[points.length-1];
            showConnections = true;
            repaint();
        }
        if(ac.equals("cancel")) {
            showConnections = false;
            removePoint = false;
            repaint();
        }
        if(ac.equals("remove")) {
            removePoint = true;
        }
    }

    public void addPoint(Point p, int index) {
        int size = points.length;
        Point[] temp = new Point[size+1];
        System.arraycopy(points, 0, temp, 0, index);
        temp[index] = p;
        System.arraycopy(points, index, temp, index+1, size-index);
        points = temp;
        buttonManager.reset();
        showConnections = false;
        setPath();
        repaint();
    }

    public void removePoint(Point p) {
        int size = points.length;
        Point[] temp = new Point[size-1];
        for(int j = 0, k = 0; j < size; j++) {
            if(points[j] == p)
                continue;
            temp[k++] = points[j];
        }
        points = temp;
        buttonManager.reset();
        removePoint = false;
        setPath();
        repaint();
    }

    public void setPoint(Point p, int x, int y) {
        p.setLocation(x, y);
        setPath();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                            RenderingHints.VALUE_STROKE_PURE);
        if(firstTime) {
            firstTime = false;
            setPath();
        }
        g2.setPaint(Color.green.darker());
        g2.draw(path);
        g2.setPaint(Color.red);
        for(int j = 0; j < points.length; j++) {
            mark(g2, points[j]);
        }
        // For adding a point.
        if(showConnections) {
            g2.setPaint(Color.yellow);
            for(int j = 0; j < points.length-1; j++) {
                connectors[j] = new Line2D.Double(points[j], points[j+1]);
                g2.draw(connectors[j]);
            }
        }
    }

    /**
     *  P(t) = B(n,0)*P0 + B(n,1)*P1 + ... + B(n,n)*Pn
     *      0 <= t <= 1
     *
     *  B(n,m) = mth coefficient of nth degree Bernstein polynomial
     *         = C(n,m) * t^(m) * (1 - t)^(n-m)
     *  C(n,m) = Combinations of n things, taken m at a time
     *         = n! / (m! * (n-m)!)
     */
    private void setPath() {
        path.reset();
        int n = points.length;
        int w = getWidth();
        for(int j = 0; j <= w; j++) {
            double t = (double)j/w;          // [0 <= t <= 1.0]
            double x = 0;
            double y = 0;
            for(int k = 0; k < n; k++) {
                x += B(n-1,k,t)*points[k].x;
                y += B(n-1,k,t)*points[k].y;
            }
            if(j > 0)
                path.lineTo(x,y);
            else
                path.moveTo(x,y);
        }
    }

    private double B(int n, int m, double t) {
        return C(n,m) * Math.pow(t, m) * Math.pow(1.0 - t, n-m);
    }

    private double C(int n, int m) {
        return factorial(n) / (factorial(m)*factorial(n-m));
    }

    private int factorial(int n) {
        return (n > 1) ? n*factorial(n-1) : 1;
    }

    private void mark(Graphics2D g2, Point p) {
        g2.fill(new Ellipse2D.Double(p.x-2, p.y-2, 4, 4));
    }

    private JPanel getButtonPanel() {
        buttonManager = new ButtonManager();
        String[] ids = { "add", "cancel", "remove" };
        JPanel panel = new JPanel();
        for(int j = 0; j < ids.length; j++) {
            JButton button = new JButton(ids[j]);
            button.setEnabled(j != 1);
            buttonManager.add(button);
            button.setActionCommand(ids[j]);
            button.addActionListener(this);
            panel.add(button);
        }
        return panel;
    }

    public static void main(String[] args) {
        CurveFit test = new CurveFit();
        PointMover mover = new PointMover(test);
        test.addMouseListener(mover);
        test.addMouseMotionListener(mover);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test);
        f.add(test.getButtonPanel(), "Last");
        f.setSize(500,500);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}

class PointMover extends MouseInputAdapter {
    CurveFit component;
    Point selectedPoint;
    Cursor cursor;
    Cursor defaultCursor = Cursor.getDefaultCursor();
    Point offset = new Point();
    boolean dragging = false;
    final int PROX_DIST = 5;

    PointMover(CurveFit cf) {
        component = cf;
        BufferedImage image = getImage();
        Point hotspot = new Point(17,17);
        cursor = Toolkit.getDefaultToolkit()
                        .createCustomCursor(image, hotspot, null);
    }

    public void mousePressed(MouseEvent e) {
        if(selectedPoint != null) {
            if(component.removePoint) {                // remove
                component.removePoint(selectedPoint);
            } else {                                   // drag
                offset.x = e.getX() - selectedPoint.x;
                offset.y = e.getY() - selectedPoint.y;
                dragging = true;
            }
        } else if(component.showConnections) {         // add
            Point p = e.getPoint();
            Line2D.Double[] lines = component.connectors;
            for(int j = 0; j < lines.length; j++) {
                if(lines[j].ptSegDist(p) < PROX_DIST) {
                    component.addPoint(p, j+1);
                    break;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    public void mouseDragged(MouseEvent e) {
        if(dragging) {
            int x = e.getX() - offset.x;
            int y = e.getY() - offset.y;
            component.setPoint(selectedPoint, x, y);
        }
    }

    /** For point selection. */
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        Point[] pts = component.points;
        boolean hovering = false;
        for(int j = 0; j < pts.length; j++) {
            if(pts[j].distance(p) < PROX_DIST) {
                hovering = true;
                if(selectedPoint != pts[j]) {
                    selectedPoint = pts[j];
                    component.setCursor(cursor);
                    break;
                }
            }
        }

        if(!hovering && selectedPoint != null) {
            selectedPoint = null;
            component.setCursor(defaultCursor);
        }
    }

    private BufferedImage getImage() {
        int w = 27, h = 27,
            type = BufferedImage.TYPE_INT_ARGB_PRE;
        BufferedImage image = new BufferedImage(w, h, type);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new Color(0x333333));
        g2.draw(new Line2D.Double(w/2, 0, w/2, 8));    // n
        g2.draw(new Line2D.Double(0, h/2, 8, h/2));    // w
        g2.draw(new Line2D.Double(w/2, h-8, w/2, h));  // s
        g2.draw(new Line2D.Double(w-8, h/2, w, h/2));  // e
        g2.dispose();
        return image;
    }
}

class ButtonManager implements ActionListener {
    JButton[] buttons = new JButton[0];

    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if(ac.equals("add")) {
            getButton("remove").setEnabled(false);
            enable(true);
        } else if(ac.equals("remove")) {
            getButton("add").setEnabled(false);
            enable(true);
        } else {

            reset();
        }
    }

    public void reset() {
        getButton("add").setEnabled(true);
        getButton("remove").setEnabled(true);
        enable(false);
    }

    private void enable(boolean enable) {
        getButton("cancel").setEnabled(enable);
    }

    public void add(JButton button) {
        button.addActionListener(this);
        int size = buttons.length;
        JButton[] temp = new JButton[size+1];
        System.arraycopy(buttons, 0, temp, 0, size);
        temp[size] = button;
        buttons = temp;
    }

    private JButton getButton(String target) {
        for(int j = 0; j < buttons.length; j++) {
            if(buttons[j].getActionCommand().equals(target))
                return buttons[j];
        }
        return null;
    }
}