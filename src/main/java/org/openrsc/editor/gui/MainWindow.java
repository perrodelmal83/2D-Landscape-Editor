package org.openrsc.editor.gui;

import org.openrsc.editor.gui.controls.SectorNavigator;
import org.openrsc.editor.gui.controls.TileControlPanel;
import org.openrsc.editor.gui.graphics.EditorCanvas;
import org.openrsc.editor.gui.menu.EditorMenuBar;
import org.openrsc.editor.gui.menu.toolbar.ToolSelector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
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

        TileControlPanel tileControlPanel = new TileControlPanel(800, 87);
        add(tileControlPanel);

        SectorNavigator sectorNavigator = new SectorNavigator(800, 580);
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

        setTitle("RSC Landscape Editor");
        setLocationRelativeTo(this.getOwner());

        setVisible(true);
        gamePanel.setBackground(Color.GRAY);
        gamePanel.setSize(1, 1);
        gamePanel.setVisible(false);
        setFocusable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
