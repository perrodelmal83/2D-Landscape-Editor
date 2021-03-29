package org.openrsc.editor;

import org.openrsc.editor.gui.menu.AdvancedMenu;
import org.openrsc.editor.gui.menu.EditMenu;
import org.openrsc.editor.gui.menu.FileMenu;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CompletableFuture;

/**
 * @author xEnt/Vrunk/Peter GUI designed with JFormDesigner. Some stuff done by
 * hand added.
 */
public class GUI {
    public static int rotation = 0;
    public static Timer timer = null;

    public GUI() {
        try {
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Canvas can = new Canvas(jframe);
        new Thread(can).start();
    }

    public static void main(String[] args) {
        try {
            new GUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Open Landscape.
    private void onOpenLandscape() {
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Locate Landscape.rscd");

        if (fc.showOpenDialog(jframe) == JFileChooser.APPROVE_OPTION) {
            Util.ourFile = fc.getSelectedFile();
            SelectSection ss = new SelectSection();
            ss.setVisible(true);
        }

    }

    private void onOpenSection() {
        if (Util.tileArchive == null) {
            return;
        }
        handleMove();
        new SelectSection().setVisible(true);
    }

    private void onSaveLandscape() {
        handleMove();
    }

    private void onRevertLandscape() {
        if (Util.STATE == Util.State.RENDER_READY) {
            Util.STATE = Util.State.CHANGING_SECTOR;
        }
    }

    private void onExit() {
        System.exit(0);
    }

    private void onUndo() {
        //TODO: Implement this functionality
    }

    private void onCopy() {
        Util.copiedTile = Util.selectedTile;
    }

    private void onPaste() {
        if (Util.copiedTile != null) {
            Util.selectedTile.setDiagonalWalls(Util.copiedTile.getDiagonalWalls());
            Util.selectedTile.setVerticalWall(Util.copiedTile.getVerticalWall());
            Util.selectedTile.setHorizontalWall(Util.copiedTile.getHorizontalWall());
            Util.selectedTile.setGroundElevation(Util.copiedTile.getGroundElevation());
            Util.selectedTile.setGroundTexture(Util.copiedTile.getGroundTexture());
            Util.selectedTile.setRoofTexture(Util.copiedTile.getRoofTexture());
            Util.selectedTile.setGroundOverlay(Util.copiedTile.getGroundOverlay());
            Util.STATE = Util.State.TILE_NEEDS_UPDATING;
        }
    }

    private void onShowUnderground() {
        changeSectorHeight(SectorHeight.UNDERGROUND);
    }

    private void onShowGroundLevel() {
        changeSectorHeight(SectorHeight.GROUND_FLOOR);
    }

    private void onShowUpstairs() {
        changeSectorHeight(SectorHeight.UPSTAIRS);
    }

    private void onShowSecondStory() {
        changeSectorHeight(SectorHeight.SECOND_LEVEL);
    }

    public void onJumpToCoords() {
        Util.handleJumpToCoords();
    }

    public CompletableFuture<Boolean> toggleShowNpcs() {
        return attemptUpdate(Util::toggleShowNpcs).thenApply(unused -> Util.showNpcs);
    }

    public CompletableFuture<Void> attemptUpdate(Runnable task) {
        return CompletableFuture.runAsync(() -> {
            if (Util.STATE == Util.State.RENDER_READY) {
                task.run();
                Util.STATE = Util.State.FORCE_FULL_RENDER;
            }
        });
    }

    public CompletableFuture<Boolean> toggleShowRoofs() {
        return attemptUpdate(Util::toggleShowRoofs).thenApply(unused -> Util.showRoofs);
    }

    private void changeSectorHeight(SectorHeight sectorHeight) {
        int converted = sectorHeight.ordinal();
        if (Util.sectorH != converted && Util.STATE == Util.State.RENDER_READY) {
            Util.sectorH = converted;
            Util.STATE = Util.State.CHANGING_SECTOR;
        }
    }

    public static int[][] arr = new int[256][3];

    private void initComponents() throws Exception {
        JPanel gamePanel = new JPanel();
        jframe = new JFrame();
        jframe.setBackground(Color.black);
        jframe.setResizable(false);

        Container contentPane = jframe.getContentPane();

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(
                new FileMenu(
                        this::onOpenLandscape,
                        this::onOpenSection,
                        this::onSaveLandscape,
                        this::onRevertLandscape,
                        this::onExit
                )
        );

        menuBar.add(
                new EditMenu(
                        this::onUndo,
                        this::onCopy,
                        this::onPaste
                )
        );

        menuBar.add(
                new AdvancedMenu(
                        this::onShowUnderground,
                        this::onShowGroundLevel,
                        this::onShowUpstairs,
                        this::onShowSecondStory,
                        this::onJumpToCoords,
                        this::toggleShowRoofs,
                        this::toggleShowNpcs
                )
        );

        final JLabel temp4 = new JLabel();
        temp4.setVisible(true);
        temp4.setText("Texture: 0");
        temp4.setLocation(800 - 263, 37);
        temp4.setSize(200, 20);
        jframe.add(temp4);
        int temp = 30;
        final JLabel temp5 = new JLabel();
        temp5.setVisible(false);
        temp5.setText("Diagonal wall: 0");
        temp5.setLocation(800 - 263, 37 + temp);
        temp5.setSize(200, 20);
        jframe.add(temp5);
        temp += 30;
        final JLabel temp6 = new JLabel();
        temp6.setVisible(false);
        temp6.setText("Vertical wall: 0");
        temp6.setLocation(800 - 263, 37 + temp);
        temp6.setSize(200, 20);
        jframe.add(temp6);
        temp += 30;
        final JLabel temp7 = new JLabel();
        temp7.setVisible(false);
        temp7.setText("Horizontal wall: 0");
        temp7.setLocation(800 - 263, 37 + temp);
        temp7.setSize(200, 20);
        jframe.add(temp7);

        temp += 30;
        final JLabel temp8 = new JLabel();
        temp8.setVisible(false);
        temp8.setText("Overlay: 0");
        temp8.setLocation(800 - 263, 37 + temp);
        temp8.setSize(200, 20);
        jframe.add(temp8);

        temp += 30;
        final JLabel temp9 = new JLabel();
        temp9.setVisible(false);
        temp9.setText("Roof texture: 0");
        temp9.setLocation(800 - 263, 37 + temp);
        temp9.setSize(200, 20);
        jframe.add(temp9);

        temp += 30;
        final JLabel temp10 = new JLabel();
        temp10.setVisible(false);
        temp10.setText("Elevation: 0");
        temp10.setLocation(800 - 263, 37 + temp);
        temp10.setSize(200, 20);
        jframe.add(temp10);


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
                temp4.setText("Texture (" + textureJS.getValue() + ")");
                if (Util.STATE == Util.State.RENDER_READY || Util.STATE == Util.State.TILE_NEEDS_UPDATING) {
                    if (Util.selectedTile != null && !brushes.getSelectedItem().equals("Configure your own")) {
                        GUI.groundtexture.setText("GroundTexture: " + Util.selectedTile.getGroundTextureInt());
                        Util.selectedTile.setGroundTexture((byte) (textureJS.getValue() - 0xff));
                        Util.STATE = Util.State.TILE_NEEDS_UPDATING;
                    }
                }
            }
        });
        jframe.add(textureJS);
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
                temp5.setText("Diagonal wall: " + diagonalWallJS.getValue() + "");
            }
        });
        jframe.add(diagonalWallJS);

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
                temp6.setText("Vertical wall: " + verticalWallJS.getValue() + "");
            }
        });
        jframe.add(verticalWallJS);

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
                temp7.setText("Horizontal wall: " + horizontalWallJS.getValue() + "");
            }
        });
        jframe.add(horizontalWallJS);

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
                temp8.setText("Overlay: " + overlayJS.getValue() + "");
            }
        });
        jframe.add(overlayJS);

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
                temp9.setText("Roof texture: " + roofTextureJS.getValue() + "");
            }
        });
        jframe.add(roofTextureJS);

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
                temp10.setText("Elevation: " + elevationJS.getValue() + "");
            }
        });
        jframe.add(elevationJS);

        loadData = new JButton();
        loadData.setSize(100, 16);
        loadData.setText("Load data");
        loadData.setLocation(761, 250);
        loadData.setVisible(false);
        loadData.setEnabled(true);

        jframe.add(loadData);
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
        jframe.setJMenuBar(menuBar);

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
        jframe.setSize(870, 600);
        /**
         * Below are the Sector left/right/up/down buttons.
         */
        JLabel sectorLabel = new JLabel();
        sectorLabel.setSize(200, 30);
        sectorLabel.setText("Move Sector");
        sectorLabel.setLocation(565, 396);
        sectorLabel.setVisible(true);
        jframe.add(sectorLabel);

        JButton sectorLeft = new JButton();
        sectorLeft.setSize(70, 30);
        sectorLeft.setText("Left");
        sectorLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Util.sectorX + 1 > 68 || Util.sectorY > 57 || Util.sectorX < 48 || Util.sectorY < 37) {
                    JOptionPane
                            .showMessageDialog(jframe,
                                    "This area is Out of Bounds.\r\nThere is no Existing sector on your Left\r\nYou have not moved anywhere");
                    return;
                }
                // Move sector left.
                handleMove();
                if (Util.STATE == Util.State.RENDER_READY) {
                    Util.sectorX++;
                    Util.STATE = Util.State.CHANGING_SECTOR;
                }

            }
        });
        sectorLeft.setLocation(532, 450 + 10);
        sectorLeft.setVisible(true);
        jframe.add(sectorLeft);

        JButton sectorRight = new JButton();
        sectorRight.setSize(70, 30);
        sectorRight.setText("Right");
        sectorRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Move sector right.
                if (Util.sectorX > 68 || Util.sectorY > 57 || Util.sectorX - 1 < 48 || Util.sectorY < 37) {
                    JOptionPane
                            .showMessageDialog(jframe,
                                    "This area is Out of Bounds.\r\nThere is no Existing sector on your Right\r\nYou have not moved anywhere");
                    return;
                }
                handleMove();
                if (Util.STATE == Util.State.RENDER_READY) {
                    Util.sectorX--;
                    Util.STATE = Util.State.CHANGING_SECTOR;
                }
            }
        });
        sectorRight.setLocation(532 + 75, 450 + 10);
        sectorRight.setVisible(true);
        jframe.add(sectorRight);

        JButton sectorUp = new JButton();
        sectorUp.setSize(70, 30);
        sectorUp.setText("Up");
        sectorUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Util.sectorX > 68 || Util.sectorY > 57 || Util.sectorX < 48 || Util.sectorY - 1 < 37) {
                    JOptionPane
                            .showMessageDialog(jframe,
                                    "This area is Out of Bounds.\r\nThere is no Existing sector above this\r\nYou have not moved anywhere");
                    return;
                }
                // Move sector up.
                handleMove();
                if (Util.STATE == Util.State.RENDER_READY) {
                    Util.sectorY--;
                    Util.STATE = Util.State.CHANGING_SECTOR;
                }
            }
        });
        sectorUp.setLocation(532 + 35, 415 + 10);
        sectorUp.setVisible(true);
        jframe.add(sectorUp);

        JButton sectorDown = new JButton();
        sectorDown.setSize(70, 30);
        sectorDown.setText("Down");
        sectorDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Util.sectorX > 68 || Util.sectorY + 1 > 57 || Util.sectorX < 48 || Util.sectorY < 37) {
                    JOptionPane
                            .showMessageDialog(jframe,
                                    "This area is Out of Bounds.\r\nThere is no Existing sector below this\r\nYou have not moved anywhere");
                    return;
                }
                // Move sector down.
                handleMove();
                if (Util.STATE == Util.State.RENDER_READY) {
                    Util.sectorY++;
                    Util.STATE = Util.State.CHANGING_SECTOR;
                }
            }
        });
        sectorDown.setLocation(532 + 35, 485 + 10);
        sectorDown.setVisible(true);
        jframe.add(sectorDown);

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

        jframe.add(tile);

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

        jframe.add(groundtexture);
        jframe.add(diagonalWall);
        jframe.add(verticalWall);
        jframe.add(horizontalWall);
        jframe.add(overlay);
        jframe.add(roofTexture);
        jframe.add(elevation);

        /**
         * TODO another mark, nvm this.
         */

        brushes = new JComboBox(Util.BRUSH_LIST);
        brushes.setLocation(536, 235 + 20 - 250);
        brushes.setSize(320, 20);
        brushes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (brushes.getSelectedItem().equals("Configure your own")) {
                    diagonalWallJS.setVisible(true);
                    verticalWallJS.setVisible(true);
                    horizontalWallJS.setVisible(true);
                    overlayJS.setVisible(true);
                    roofTextureJS.setVisible(true);
                    elevationJS.setVisible(true);
                    temp5.setVisible(true);
                    temp6.setVisible(true);
                    temp7.setVisible(true);
                    temp8.setVisible(true);
                    temp9.setVisible(true);
                    temp10.setVisible(true);
                    loadData.setVisible(true);
                } else {
                    diagonalWallJS.setVisible(false);
                    verticalWallJS.setVisible(false);
                    horizontalWallJS.setVisible(false);
                    overlayJS.setVisible(false);
                    roofTextureJS.setVisible(false);
                    elevationJS.setVisible(false);
                    temp5.setVisible(false);
                    temp6.setVisible(false);
                    temp7.setVisible(false);
                    temp8.setVisible(false);
                    temp9.setVisible(false);
                    temp10.setVisible(false);
                    loadData.setVisible(false);
                }
                if (brushes.getSelectedItem().equals("Elevation")) {
                    String temp = JOptionPane.showInputDialog("Enter new Elevation Value");
                    int ele = Integer.parseInt(temp);
                    if ((ele >= 0 && ele <= 255)) {
                        Util.eleReady = true;
                        Util.newEle = (byte) ele;
                    } else {
                        JOptionPane.showMessageDialog(jframe, "That was not a correct value \nMust enter between 0-255\nIf you are unsure"
                                + " on what elevation you need\nClick another tile,"
                                + " then click Advanced>Toggle Tile Info\nand you will see other tile's Elevation(height) values");
                    }
                } else {
                    Util.eleReady = false;
                }
            }
        });

        jframe.add(brushes);

        /********************************************/
        jframe.setTitle("RSC Landscape Editor");
        jframe.setLocationRelativeTo(jframe.getOwner());
        jframe.pack();

        jframe.setVisible(true);
        gamePanel.setBackground(Color.GRAY);
        gamePanel.setSize(1, 1);
        gamePanel.setVisible(false);
        jframe.setFocusable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void handleMove() {
        if (Util.sectorChanged && Util.tileArchive != null) {
            if (JOptionPane.showConfirmDialog(null,
                    "Changes have been made to this Section\r\nDo you wish to save the current map?", "Saving", 0) == 0) {

                if (Util.save()) {
                    JOptionPane.showMessageDialog(GUI.jframe, "Sucessfully saved to " + Util.ourFile.getPath());
                    Util.sectorChanged = false;
                } else {
                    JOptionPane.showMessageDialog(GUI.jframe, "Failed to saved to " + Util.ourFile.getPath());
                }
            } else {
                Util.sectorChanged = false;
            }
        }
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
    public static JFrame jframe;

    public static JSlider textureJS;
    public static JSlider diagonalWallJS;
    public static JSlider verticalWallJS;
    public static JSlider horizontalWallJS;
    public static JSlider overlayJS;
    public static JSlider roofTextureJS;
    public static JSlider elevationJS;
    public static JButton loadData;


}
