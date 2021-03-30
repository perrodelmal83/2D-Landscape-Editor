package org.openrsc.editor.gui;

import com.google.common.eventbus.EventBus;
import org.openrsc.editor.Actions;
import org.openrsc.editor.Util;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.event.TerrainTemplateUpdateEvent;
import org.openrsc.editor.gui.controls.TileControlPanel;
import org.openrsc.editor.gui.graphics.EditorCanvas;
import org.openrsc.editor.gui.menu.EditorMenuBar;
import org.openrsc.editor.model.BrushOption;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * @author xEnt/Vrunk/Peter GUI designed with JFormDesigner. Some stuff done by
 * hand added.
 */
public class MainWindow extends JFrame {
    public static int rotation = 0;
    public static Timer timer = null;
    private static final EventBus eventBus = EventBusFactory.getEventBus();

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

        TileControlPanel tileControlPanel = new TileControlPanel(800, 37);
        add(tileControlPanel);

        int temp = 30;

        temp += 30;
        final JLabel verticalWallLabel = new JLabel();
        verticalWallLabel.setVisible(false);
        verticalWallLabel.setText("Vertical wall: 0");
        verticalWallLabel.setLocation(800 - 263, 67 + temp);
        verticalWallLabel.setSize(200, 20);
        add(verticalWallLabel);

        temp += 30;
        final JLabel horizontalWallLabel = new JLabel();
        horizontalWallLabel.setVisible(false);
        horizontalWallLabel.setText("Horizontal wall: 0");
        horizontalWallLabel.setLocation(800 - 263, 67 + temp);
        horizontalWallLabel.setSize(200, 20);
        add(horizontalWallLabel);

        temp += 30;
        final JLabel overlayLabel = new JLabel();
        overlayLabel.setVisible(false);
        overlayLabel.setText("Overlay: 0");
        overlayLabel.setLocation(800 - 263, 67 + temp);
        overlayLabel.setSize(200, 20);
        add(overlayLabel);

        temp += 30;
        final JLabel roofTextureLabel = new JLabel();
        roofTextureLabel.setVisible(false);
        roofTextureLabel.setText("Roof texture: 0");
        roofTextureLabel.setLocation(800 - 263, 67 + temp);
        roofTextureLabel.setSize(200, 20);
        add(roofTextureLabel);

        temp += 30;
        final JLabel elevationLabel = new JLabel();
        elevationLabel.setVisible(false);
        elevationLabel.setText("Elevation: 0");
        elevationLabel.setLocation(800 - 263, 67 + temp);
        elevationLabel.setSize(200, 20);
        add(elevationLabel);


        /**
         * TODO just adding a mark here so i can find easier.
         */

        verticalWallJS = new JSlider();
        verticalWallJS.setSize(230, 30);
        verticalWallJS.setLocation(816 - 181, 67 + temp);
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
        horizontalWallJS.setLocation(816 - 171, 67 + temp);
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
        overlayJS.setLocation(816 - 171, 67 + temp);
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
        roofTextureJS.setLocation(816 - 171, 67 + temp);
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
        elevationJS.setLocation(816 - 171, 67 + temp);
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

        setSize(1200, 1000);

        /**
         * Below are the Sector left/right/up/down buttons.
         */
        JLabel sectorLabel = new JLabel();
        sectorLabel.setSize(200, 30);
        sectorLabel.setText("Move Sector");
        sectorLabel.setLocation(865, 396);
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
        sectorLeft.setLocation(800, 450 + 10);
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
        sectorRight.setLocation(800 + 75, 450 + 10);
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
        sectorUp.setLocation(800 + 35, 415 + 10);
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
        sectorDown.setLocation(800 + 35, 485 + 10);
        sectorDown.setVisible(true);
        add(sectorDown);

        brushes = new JComboBox<>(BrushOption.values());
        brushes.setSelectedItem(BrushOption.NONE);
        brushes.setLocation(800, 10);
        brushes.setSize(320, 20);
        brushes.addActionListener(evt -> {
            BrushOption option = (BrushOption) brushes.getSelectedItem();
            eventBus.post(new TerrainTemplateUpdateEvent(
                    Objects.requireNonNull(option).getTemplate()
            ));
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
    public static JComboBox<BrushOption> brushes;

    public static JSlider textureJS;
    public static JSlider diagonalWallJS;
    public static JSlider verticalWallJS;
    public static JSlider horizontalWallJS;
    public static JSlider overlayJS;
    public static JSlider roofTextureJS;
    public static JSlider elevationJS;
    public static JButton loadData;


}
