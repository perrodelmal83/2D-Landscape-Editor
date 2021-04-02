package org.openrsc.editor.gui.graphics.stroke;

import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.function.Supplier;

public class DashedStrokeGenerator implements Supplier<Stroke> {
    private float dashPhase;
    private float[] dash;
    private BasicStroke currentStroke;

    public DashedStrokeGenerator() {
        dashPhase = 0.0f;
        dash = new float[]{5.0f, 5.0f};

        Timer t = new Timer(40, (evt) -> {
            dashPhase += 9.0f;
            currentStroke = new BasicStroke(
                    1.5f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_MITER,
                    1.5f, //miter limit
                    dash,
                    dashPhase
            );
        });
        t.start();
    }

    @Override
    public Stroke get() {
        return currentStroke;
    }
}
