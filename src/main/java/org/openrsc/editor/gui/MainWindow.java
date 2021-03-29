package org.openrsc.editor.gui;

import org.openrsc.editor.Actions;
import org.openrsc.editor.EditorCanvas;
import org.openrsc.editor.MoveDirection;
import org.openrsc.editor.Util;
import org.openrsc.editor.gui.menu.EditorMenuBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xEnt/Vrunk/Peter GUI designed with JFormDesigner. Some stuff done by
 * hand added.
 */
public class MainWindow extends JFrame {
    public static int rotation = 0;
    public static Timer timer = null;

    public MainWindow() {
        super();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Unable to match look and feel with operating system, ignore
        }
        this.setBackground(Color.black);
        this.setResizable(false);
        this.setTitle("RSC Landscape Editor");

        initComponents();

        EditorCanvas can = new EditorCanvas(this);
        new Thread(can).start();
    }

    private void initComponents() {
        JPanel gamePanel = new JPanel();

        Container contentPane = getContentPane();

        EditorMenuBar menuBar = new EditorMenuBar();
        setJMenuBar(menuBar);

        final JLabel textureLabel = new JLabel();
        textureLabel.setVisible(true);
        textureLabel.setText("Texture: 0");
        textureLabel.setLocation(800 - 263, 37);
        textureLabel.setSize(200, 20);
        add(textureLabel);

        int temp = 30;
        final JLabel diagonalWallLabel = new JLabel();
        diagonalWallLabel.setVisible(false);
        diagonalWallLabel.setText("Diagonal wall: 0");
        diagonalWallLabel.setLocation(800 - 263, 37 + temp);
        diagonalWallLabel.setSize(200, 20);
        add(diagonalWallLabel);

        temp += 30;
        final JLabel verticalWallLabel = new JLabel();
        verticalWallLabel.setVisible(false);
        verticalWallLabel.setText("Vertical wall: 0");
        verticalWallLabel.setLocation(800 - 263, 37 + temp);
        verticalWallLabel.setSize(200, 20);
        add(verticalWallLabel);

        temp += 30;
        final JLabel horizontalWallLabel = new JLabel();
        horizontalWallLabel.setVisible(false);
        horizontalWallLabel.setText("Horizontal wall: 0");
        horizontalWallLabel.setLocation(800 - 263, 37 + temp);
        horizontalWallLabel.setSize(200, 20);
        add(horizontalWallLabel);

        temp += 30;
        final JLabel overlayLabel = new JLabel();
        overlayLabel.setVisible(false);
        overlayLabel.setText("Overlay: 0");
        overlayLabel.setLocation(800 - 263, 37 + temp);
        overlayLabel.setSize(200, 20);
        add(overlayLabel);

        temp += 30;
        final JLabel roofTextureLabel = new JLabel();
        roofTextureLabel.setVisible(false);
        roofTextureLabel.setText("Roof texture: 0");
        roofTextureLabel.setLocation(800 - 263, 37 + temp);
        roofTextureLabel.setSize(200, 20);
        add(roofTextureLabel);

        temp += 30;
        final JLabel elevationLabel = new JLabel();
        elevationLabel.setVisible(false);
        elevationLabel.setText("Elevation: 0");
        elevationLabel.setLocation(800 - 263, 37 + temp);
        elevationLabel.setSize(200, 20);
        add(elevationLabel);


        /**
         * TODO just adding a mark here so i can find easier.
         */
        textureJS = new JSlider();
        textureJS.setSize(250, 30);
        textureJS.setVisible(true);
        textureJS.setLocation(816 - 201, 17 + 20);
        textureJS.setEnabled(true);
        textureJS.setOrientation(JSlider.HORIZONTAL);
        textureJS.setMinorTickSpacing(5);
        textureJS.setMaximum(254);
        textureJS.setPaintTicks(true);
        textureJS.setPaintTrack(true);
        textureJS.setMinimum(0);
        textureJS.setValue(0);
        textureJS.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                textureLabel.setText("Texture (" + textureJS.getValue() + ")");
                if (Util.STATE == Util.State.RENDER_READY || Util.STATE == Util.State.TILE_NEEDS_UPDATING) {
                    if (Util.selectedTile != null && !brushes.getSelectedItem().equals("Configure your own")) {
                        MainWindow.groundtexture.setText("GroundTexture: " + Util.selectedTile.getGroundTextureInt());
                        Util.selectedTile.setGroundTexture((byte) (textureJS.getValue() - 0xff));
                        Util.STATE = Util.State.TILE_NEEDS_UPDATING;
                    }
                }
            }
        });
        add(textureJS);
        temp = 30;

        diagonalWallJS = new JSlider();
        diagonalWallJS.setSize(230, 30);
        diagonalWallJS.setLocation(816 - 181, 17 + 20 + temp);
        diagonalWallJS.setVisible(false);
        diagonalWallJS.setEnabled(true);
        diagonalWallJS.setValue(0);
        diagonalWallJS.setOrientation(JSlider.HORIZONTAL);
        diagonalWallJS.setMinorTickSpacing(5);
        diagonalWallJS.setMaximum(254);
        diagonalWallJS.setPaintTicks(true);
        diagonalWallJS.setPaintTrack(true);
        diagonalWallJS.setMinimum(0);
        diagonalWallJS.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                diagonalWallLabel.setText("Diagonal wall: " + diagonalWallJS.getValue() + "");
            }
        });
        add(diagonalWallJS);

        temp += 30;
        verticalWallJS = new JSlider();
        verticalWallJS.setSize(230, 30);
        verticalWallJS.setLocation(816 - 181, 17 + 20 + temp);
        verticalWallJS.setVisible(false);
        verticalWallJS.setEnabled(true);
        verticalWallJS.setValue(0);
        verticalWallJS.setOrientation(JSlider.HORIZONTAL);
        verticalWallJS.setMinorTickSpacing(5);
        verticalWallJS.setMaximum(254);
        verticalWallJS.setPaintTicks(true);
        verticalWallJS.setPaintTrack(true);
        verticalWallJS.setMinimum(0);
        verticalWallJS.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                verticalWallLabel.setText("Vertical wall: " + verticalWallJS.getValue() + "");
            }
        });
        add(verticalWallJS);

        temp += 30;
        horizontalWallJS = new JSlider();
        horizontalWallJS.setSize(220, 30);
        horizontalWallJS.setLocation(816 - 171, 17 + 20 + temp);
        horizontalWallJS.setVisible(false);
        horizontalWallJS.setEnabled(true);
        horizontalWallJS.setValue(0);
        horizontalWallJS.setOrientation(JSlider.HORIZONTAL);
        horizontalWallJS.setMinorTickSpacing(5);
        horizontalWallJS.setMaximum(254);
        horizontalWallJS.setPaintTicks(true);
        horizontalWallJS.setPaintTrack(true);
        horizontalWallJS.setMinimum(0);
        horizontalWallJS.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                horizontalWallLabel.setText("Horizontal wall: " + horizontalWallJS.getValue() + "");
            }
        });
        add(horizontalWallJS);

        temp += 30;
        overlayJS = new JSlider();
        overlayJS.setSize(220, 30);
        overlayJS.setLocation(816 - 171, 17 + 20 + temp);
        overlayJS.setVisible(false);
        overlayJS.setEnabled(true);
        overlayJS.setValue(0);
        overlayJS.setOrientation(JSlider.HORIZONTAL);
        overlayJS.setMinorTickSpacing(5);
        overlayJS.setMaximum(254);
        overlayJS.setPaintTicks(true);
        overlayJS.setPaintTrack(true);
        overlayJS.setMinimum(0);
        overlayJS.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                overlayLabel.setText("Overlay: " + overlayJS.getValue() + "");
            }
        });
        add(overlayJS);

        temp += 30;
        roofTextureJS = new JSlider();
        roofTextureJS.setSize(220, 30);
        roofTextureJS.setLocation(816 - 171, 17 + 20 + temp);
        roofTextureJS.setVisible(false);
        roofTextureJS.setEnabled(true);
        roofTextureJS.setValue(0);
        roofTextureJS.setOrientation(JSlider.HORIZONTAL);
        roofTextureJS.setMinorTickSpacing(5);
        roofTextureJS.setMaximum(254);
        roofTextureJS.setPaintTicks(true);
        roofTextureJS.setPaintTrack(true);
        roofTextureJS.setMinimum(0);
        roofTextureJS.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                roofTextureLabel.setText("Roof texture: " + roofTextureJS.getValue() + "");
            }
        });
        add(roofTextureJS);

        temp += 30;
        elevationJS = new JSlider();
        elevationJS.setSize(220, 30);
        elevationJS.setLocation(816 - 171, 17 + 20 + temp);
        elevationJS.setVisible(false);
        elevationJS.setEnabled(true);
        elevationJS.setValue(0);
        elevationJS.setOrientation(JSlider.HORIZONTAL);
        elevationJS.setMinorTickSpacing(5);
        elevationJS.setMaximum(254);
        elevationJS.setPaintTicks(true);
        elevationJS.setPaintTrack(true);
        elevationJS.setMinimum(0);
        elevationJS.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                elevationLabel.setText("Elevation: " + elevationJS.getValue() + "");
            }
        });
        add(elevationJS);

        loadData = new JButton();
        loadData.setSize(100, 16);
        loadData.setText("Load data");
        loadData.setLocation(761, 250);
        loadData.setVisible(false);
        loadData.setEnabled(true);

        add(loadData);
        loadData.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (Util.selectedTile != null) {
                    elevationJS.setValue(Util.selectedTile.getGroundElevationInt());
                    overlayJS.setValue(Util.selectedTile.getGroundOverlayInt());
                    roofTextureJS.setValue(Util.selectedTile.getRoofTexture());
                    textureJS.setValue(Util.selectedTile.getGroundTextureInt());
                    diagonalWallJS.setValue(Util.selectedTile.getDiagonalWallsInt());
                    verticalWallJS.setValue(Util.selectedTile.getVerticalWallInt());
                    horizontalWallJS.setValue(Util.selectedTile.getHorizontalWallInt());
                }

            }
        });

        // ======== GamePanel ========
        {
            gamePanel.setBorder(UIManager.getBorder("InternalFrame.optionDialogBorder"));

            // JFormDesigner evaluation mark
            gamePanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EmptyBorder(0, 0, 0, 0), null, javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), gamePanel.getBorder()));
            gamePanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    if ("border".equals(e.getPropertyName()))
                        throw new RuntimeException();
                }
            });

            gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(contentPaneLayout.createParallelGroup().addGroup(
                contentPaneLayout.createSequentialGroup().addContainerGap().addComponent(gamePanel,
                        GroupLayout.PREFERRED_SIZE, 636, GroupLayout.PREFERRED_SIZE).addContainerGap(216,
                        Short.MAX_VALUE)));
        contentPaneLayout.setVerticalGroup(contentPaneLayout.createParallelGroup().addGroup(
                GroupLayout.Alignment.TRAILING,
                contentPaneLayout.createSequentialGroup().addContainerGap().addComponent(gamePanel,
                        GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE).addContainerGap()));
        int size = 3;

        setSize(870, 600);

        /**
         * Below are the Sector left/right/up/down buttons.
         */
        JLabel sectorLabel = new JLabel();
        sectorLabel.setSize(200, 30);
        sectorLabel.setText("Move Sector");
        sectorLabel.setLocation(565, 396);
        sectorLabel.setVisible(true);
        add(sectorLabel);

        JButton sectorLeft = new JButton();
        sectorLeft.setSize(70, 30);
        sectorLeft.setText("Left");
        sectorLeft.addActionListener(evt -> {
            if (Util.sectorX + 1 > 68 || Util.sectorY > 57 || Util.sectorX < 48 || Util.sectorY < 37) {
                JOptionPane.showMessageDialog(
                        this,
                        "This area is Out of Bounds." +
                                "\r\nThere is no Existing sector on your Left" +
                                "\r\nYou have not moved anywhere"
                );
                return;
            }
            // Move sector left.
            Actions.onMove(MoveDirection.LEFT);
        });
        sectorLeft.setLocation(532, 450 + 10);
        sectorLeft.setVisible(true);
        add(sectorLeft);

        JButton sectorRight = new JButton();
        sectorRight.setSize(70, 30);
        sectorRight.setText("Right");
        sectorRight.addActionListener(evt -> {
            // Move sector right.
            if (Util.sectorX > 68 || Util.sectorY > 57 || Util.sectorX - 1 < 48 || Util.sectorY < 37) {
                JOptionPane
                        .showMessageDialog(this,
                                "This area is Out of Bounds.\r\nThere is no Existing sector on your Right\r\nYou have not moved anywhere");
                return;
            }
            Actions.onMove(MoveDirection.RIGHT);
        });
        sectorRight.setLocation(532 + 75, 450 + 10);
        sectorRight.setVisible(true);
        add(sectorRight);

        JButton sectorUp = new JButton();
        sectorUp.setSize(70, 30);
        sectorUp.setText("Up");
        sectorUp.addActionListener(evt -> {
            if (Util.sectorX > 68 || Util.sectorY > 57 || Util.sectorX < 48 || Util.sectorY - 1 < 37) {
                JOptionPane
                        .showMessageDialog(this,
                                "This area is Out of Bounds.\r\nThere is no Existing sector above this\r\nYou have not moved anywhere");
                return;
            }
            // Move sector up.
            Actions.onMove(MoveDirection.UP);
        });
        sectorUp.setLocation(532 + 35, 415 + 10);
        sectorUp.setVisible(true);
        add(sectorUp);

        JButton sectorDown = new JButton();
        sectorDown.setSize(70, 30);
        sectorDown.setText("Down");
        sectorDown.addActionListener(evt -> {
            if (Util.sectorX > 68 || Util.sectorY + 1 > 57 || Util.sectorX < 48 || Util.sectorY < 37) {
                JOptionPane
                        .showMessageDialog(this,
                                "This area is Out of Bounds.\r\nThere is no Existing sector below this\r\nYou have not moved anywhere");
                return;
            }
            // Move sector down.
            Actions.onMove(MoveDirection.DOWN);
        });
        sectorDown.setLocation(532 + 35, 485 + 10);
        sectorDown.setVisible(true);
        add(sectorDown);

        /*****************************************/

        /**
         * All below is the Labels for the Tile info.
         */
        size += 245;
        tile = new JLabel();
        tile.setLocation(532, size);
        tile.setForeground(Color.BLACK);
        tile.setSize(200, 16);

        tile.setVisible(true);
        size += 22;

        add(tile);

        elevation = new JLabel();
        elevation.setLocation(532, size);
        elevation.setForeground(Color.BLACK);
        elevation.setSize(200, 16);

        elevation.setVisible(true);
        size += 16;
        roofTexture = new JLabel();
        roofTexture.setLocation(532, size);
        roofTexture.setForeground(Color.BLACK);
        roofTexture.setSize(200, 16);

        roofTexture.setVisible(true);
        size += 16;
        overlay = new JLabel();
        overlay.setLocation(532, size);
        overlay.setForeground(Color.BLACK);
        overlay.setSize(200, 16);

        overlay.setVisible(true);
        size += 16;
        horizontalWall = new JLabel();
        horizontalWall.setLocation(532, size);
        horizontalWall.setForeground(Color.BLACK);
        horizontalWall.setSize(200, 16);

        horizontalWall.setVisible(true);
        size += 16;

        verticalWall = new JLabel();
        verticalWall.setLocation(532, size);
        verticalWall.setForeground(Color.BLACK);
        verticalWall.setSize(200, 16);

        verticalWall.setVisible(true);
        size += 16;

        diagonalWall = new JLabel();
        diagonalWall.setLocation(532, size);
        diagonalWall.setForeground(Color.BLACK);
        diagonalWall.setSize(200, 16);

        diagonalWall.setVisible(true);
        size += 16;

        groundtexture = new JLabel();
        groundtexture.setLocation(532, size);
        groundtexture.setForeground(Color.BLACK);
        groundtexture.setSize(200, 16);

        groundtexture.setVisible(true);
        size += 16;

        add(groundtexture);
        add(diagonalWall);
        add(verticalWall);
        add(horizontalWall);
        add(overlay);
        add(roofTexture);
        add(elevation);

        /**
         * TODO another mark, nvm this.
         */

        brushes = new JComboBox(Util.BRUSH_LIST);
        brushes.setLocation(536, 235 + 20 - 250);
        brushes.setSize(320, 20);
        brushes.addActionListener(evt -> {
            if (brushes.getSelectedItem().equals("Configure your own")) {
                diagonalWallJS.setVisible(true);
                verticalWallJS.setVisible(true);
                horizontalWallJS.setVisible(true);
                overlayJS.setVisible(true);
                roofTextureJS.setVisible(true);
                elevationJS.setVisible(true);
                diagonalWallLabel.setVisible(true);
                verticalWallLabel.setVisible(true);
                horizontalWallLabel.setVisible(true);
                overlayLabel.setVisible(true);
                roofTextureLabel.setVisible(true);
                elevationLabel.setVisible(true);
                loadData.setVisible(true);
            } else {
                diagonalWallJS.setVisible(false);
                verticalWallJS.setVisible(false);
                horizontalWallJS.setVisible(false);
                overlayJS.setVisible(false);
                roofTextureJS.setVisible(false);
                elevationJS.setVisible(false);
                diagonalWallLabel.setVisible(false);
                verticalWallLabel.setVisible(false);
                horizontalWallLabel.setVisible(false);
                overlayLabel.setVisible(false);
                roofTextureLabel.setVisible(false);
                elevationLabel.setVisible(false);
                loadData.setVisible(false);
            }
            if (brushes.getSelectedItem().equals("Elevation")) {
                int ele = Integer.parseInt(
                        JOptionPane.showInputDialog("Enter new Elevation Value")
                );
                if ((ele >= 0 && ele <= 255)) {
                    Util.eleReady = true;
                    Util.newEle = (byte) ele;
                } else {
                    JOptionPane.showMessageDialog(this, "That was not a correct value \nMust enter between 0-255\nIf you are unsure"
                            + " on what elevation you need\nClick another tile,"
                            + " then click Advanced>Toggle Tile Info\nand you will see other tile's Elevation(height) values");
                }
            } else {
                Util.eleReady = false;
            }
        });

        add(brushes);

        /********************************************/
        setTitle("RSC Landscape Editor");
        setLocationRelativeTo(this.getOwner());

        setVisible(true);
        gamePanel.setBackground(Color.GRAY);
        gamePanel.setSize(1, 1);
        gamePanel.setVisible(false);
        setFocusable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static JLabel elevation;
    public static JLabel tile;
    public static JLabel diagonalWall;
    public static JLabel roofTexture;
    public static JLabel overlay;
    public static JLabel groundtexture;
    public static JLabel horizontalWall;
    public static JLabel verticalWall;
    public static JComboBox brushes;

    public static JSlider textureJS;
    public static JSlider diagonalWallJS;
    public static JSlider verticalWallJS;
    public static JSlider horizontalWallJS;
    public static JSlider overlayJS;
    public static JSlider roofTextureJS;
    public static JSlider elevationJS;
    public static JButton loadData;


}
