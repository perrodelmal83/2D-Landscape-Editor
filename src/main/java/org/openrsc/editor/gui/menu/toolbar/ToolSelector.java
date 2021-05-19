package org.openrsc.editor.gui.menu.toolbar;

import org.openrsc.editor.model.EditorTool;

import javax.swing.JToolBar;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;


public class ToolSelector extends JToolBar {

    public ToolSelector(String name) {
        super(name);
        setPreferredSize(new Dimension(60, 60));
        setFloatable(false);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(3);
        flowLayout.setHgap(5);

        setLayout(flowLayout);

        Arrays.stream(EditorTool.values()).forEach(editorTool -> {
            add(new ToolButton(editorTool));
        });
    }
}
