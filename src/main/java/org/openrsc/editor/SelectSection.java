package org.openrsc.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectSection extends JDialog implements ActionListener {
    private final int WINDOW_WIDTH = 150;
    private final int WINDOW_HEIGHT = 475;

    private JList list1 = null;
    private JTabbedPane defaultTab = null;

    public SelectSection() {
        prepareWindow();
        initializeComponents();
    }

    private void prepareWindow() {
        setTitle("Sectors selection");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        int WINDOW_TOP = 200;
        int WINDOW_LEFT = 400;
        setLocation(WINDOW_LEFT, WINDOW_TOP);
    }

    private void initializeComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        defaultTab = new JTabbedPane();
        String[] names1 = Util.getSectionNames();
        list1 = new JList(names1);

        JScrollPane sectorsList1 = new JScrollPane(list1);
        sectorsList1.setVisible(true);
        sectorsList1.setSize(WINDOW_WIDTH - 11, WINDOW_HEIGHT - 32 - 76);
        sectorsList1.setAutoscrolls(true);
        sectorsList1.setLocation(0, 0);
        list1.setSelectedIndex(0);
        JLabel label = new JLabel();
        label.setText("Preset");
        label.setLocation(185, -5);
        label.setSize(100, 30);

        /*
         * private JButton varrock = null; private JButton falador = null;
         * private JButton lumbridge = null; private JButton ardougne = null;
         * private JButton draynor = null;
         */
        int locY = 25;
        JButton varrock = new JButton("Varrock");
        varrock.setVisible(true);
        varrock.setLocation(155, locY);
        varrock.setSize(120, 30);
        varrock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Util.sectorH = 0;
                Util.sectorX = 50;
                Util.sectorY = 47;
                Util.STATE = Util.State.LOADED;
            }
        });

        locY += 37;

        JButton falador = new JButton("Falador");
        falador.setVisible(true);
        falador.setLocation(155, locY);
        falador.setSize(120, 30);
        falador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Util.sectorH = 0;
                Util.sectorX = 54;
                Util.sectorY = 48;
                Util.STATE = Util.State.LOADED;
            }
        });

        locY += 37;

        JButton draynor = new JButton("Draynor");
        draynor.setVisible(true);
        draynor.setLocation(155, locY);
        draynor.setSize(120, 30);
        draynor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Util.sectorH = 0;
                Util.sectorX = 52;
                Util.sectorY = 50;
                Util.STATE = Util.State.LOADED;
            }
        });

        locY += 37;

        JButton lumbridge = new JButton("Lumbridge");
        lumbridge.setVisible(true);
        lumbridge.setLocation(155, locY);
        lumbridge.setSize(120, 30);
        lumbridge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Util.sectorH = 0;
                Util.sectorX = 50;
                Util.sectorY = 50;
                Util.STATE = Util.State.LOADED;
            }
        });

        locY += 37;

        JButton ardougne = new JButton("Ardougne");
        ardougne.setVisible(true);
        ardougne.setLocation(155, locY);
        ardougne.setSize(120, 30);
        ardougne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Util.sectorH = 0;
                Util.sectorX = 59;
                Util.sectorY = 49;
                Util.STATE = Util.State.LOADED;
            }
        });

        locY += 37;

        JButton karamaja = new JButton("Karamaja");
        karamaja.setVisible(true);
        karamaja.setLocation(155, locY);
        karamaja.setSize(120, 30);
        karamaja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Util.sectorH = 0;
                Util.sectorX = 55;
                Util.sectorY = 51;
                Util.STATE = Util.State.LOADED;
            }
        });

        locY += 37;

        JButton alkharid = new JButton("Al Kharid");
        alkharid.setVisible(true);
        alkharid.setLocation(155, locY);
        alkharid.setSize(120, 30);
        alkharid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Util.sectorH = 0;
                Util.sectorX = 49;
                Util.sectorY = 51;
                Util.STATE = Util.State.LOADED;
            }
        });

        locY += 37;
        // btnOK
        JButton btnOk = new JButton("OK");
        btnOk.setVisible(true);
        btnOk.setLocation(10, 404);
        btnOk.setSize(75, 30);
        btnOk.setActionCommand("OK");
        btnOk.addActionListener(this);

        JButton jump = new JButton("Jump to Coords");
        jump.setVisible(true);
        jump.setLocation(120, 404);
        jump.setSize(150, 30);
        jump.setActionCommand("jump");
        jump.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Util.handleJumpToCoords();
            }
        });

        JPanel panel1 = new JPanel(null);
        panel1.add(sectorsList1);
        panel1.setLocation(0, 0);
        panel1.setSize(WINDOW_WIDTH - 6 + 100, WINDOW_HEIGHT - 32);

        defaultTab.addTab("Sectors", panel1);
        defaultTab.setLocation(0, 0);
        defaultTab.setSize(WINDOW_WIDTH - 6, WINDOW_HEIGHT - 32 - 50);
        contentPane.add(jump);
        contentPane.add(label);
        contentPane.add(falador);
        contentPane.add(alkharid);
        contentPane.add(varrock);
        contentPane.add(draynor);
        contentPane.add(lumbridge);
        contentPane.add(ardougne);
        contentPane.add(karamaja);
        contentPane.add(defaultTab);
        contentPane.add(btnOk);
        setSize(WINDOW_WIDTH + 140, WINDOW_HEIGHT);
        setResizable(false);
    }

    public void actionPerformed(ActionEvent e) {
        if ("OK".equals(e.getActionCommand())) {
            String[] names = null;
            String selected = "";
            if (defaultTab.getSelectedIndex() == 0) {
                names = Util.getSectionNames();
                selected = names[list1.getSelectedIndex()];
            }
            int hIndex = selected.lastIndexOf('h');
            int xIndex = selected.lastIndexOf('x');
            int yIndex = selected.lastIndexOf('y');

            if (hIndex < 0 || xIndex < 0 || yIndex < 0)
                return;

            Util.sectorH = Integer.parseInt(selected.substring(hIndex + 1, xIndex));
            Util.sectorX = Integer.parseInt(selected.substring(xIndex + 1, yIndex));
            Util.sectorY = Integer.parseInt(selected.substring(yIndex + 1));
            Util.STATE = Util.State.LOADED;
            this.setVisible(false);
        }
    }
}
