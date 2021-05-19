package org.openrsc.editor.gui.controls;

import org.openrsc.editor.Actions;
import org.openrsc.editor.Util;
import org.openrsc.editor.gui.MoveDirection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

public class SectorNavigator extends JPanel {
    public SectorNavigator(final int x, final int y) {
        super();
        setLayout(new GridLayout(4, 3));
        setSize(200, 250);
        setLocation(x, y);

        add(new JPanel());
        JLabel sectorLabel = new JLabel();
        sectorLabel.setSize(200, 30);
        sectorLabel.setText("Move");
        sectorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sectorLabel.setVisible(true);
        add(sectorLabel);
        add(new JPanel());

        add(new JPanel());
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
        sectorUp.setVisible(true);
        add(sectorUp);
        add(new JPanel());

        JButton sectorLeft = new JButton();
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
        sectorLeft.setVisible(true);
        add(sectorLeft);

        add(new JPanel());

        JButton sectorRight = new JButton();
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
        sectorRight.setVisible(true);
        add(sectorRight);

        add(new JPanel());
        JButton sectorDown = new JButton();
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
        sectorDown.setVisible(true);
        add(sectorDown);
        add(new JPanel());
    }
}
