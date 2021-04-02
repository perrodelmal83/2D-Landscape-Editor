package org.openrsc.editor.gui;

import com.google.common.eventbus.EventBus;
import org.openrsc.editor.event.EventBusFactory;
import org.openrsc.editor.gui.controls.SectorNavigator;
import org.openrsc.editor.gui.controls.TileControlPanel;
import org.openrsc.editor.gui.controls.toolbar.ToolSelector;
import org.openrsc.editor.gui.graphics.EditorCanvas;
import org.openrsc.editor.gui.menu.EditorMenuBar;
import org.openrsc.editor.model.brush.BrushOption;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * @author xEnt/Vrunk/Peter GUI designed with JFormDesigner. Some stuff done by
 * hand added.
 */
public class MainWindow extends JFrame {
    public static int rotation = 0;
    public static Timer timer = null;
    private static final EventBus eventBus = EventBusFactory.getEventBus();
    public static JComboBox<BrushOption> brushes;
    private EditorCanvas canvas;

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
        new Thread(canvas).start();
    }

    private void initComponents() {
        EditorMenuBar menuBar = new EditorMenuBar();
        setJMenuBar(menuBar);

        canvas = new EditorCanvas();
        add(canvas);

        add(new ToolSelector("Tool Selector"), BorderLayout.NORTH);

        TileControlPanel tileControlPanel = new TileControlPanel(800, 37);
        add(tileControlPanel);

        SectorNavigator sectorNavigator = new SectorNavigator(800, 500);
        add(sectorNavigator);

        JPanel gamePanel = new JPanel();
        gamePanel.setBorder(UIManager.getBorder("InternalFrame.optionDialogBorder"));
        gamePanel.setBorder(
                new CompoundBorder(
                        new TitledBorder(
                                new EmptyBorder(0, 0, 0, 0),
                                null,
                                TitledBorder.CENTER,
                                TitledBorder.BOTTOM,
                                new Font("Dialog", Font.BOLD, 12),
                                Color.red
                        ),
                        gamePanel.getBorder()
                )
        );
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
        gamePanel.setLocation(1, 1);
        add(gamePanel);

        setSize(1200, 1000);

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

    public static JButton loadData;


}
