package org.openrsc.editor.gui.dialog;

import org.openrsc.editor.model.GeneratorAlgorithm;
import org.openrsc.editor.model.configuration.GenerateLandscapeConfiguration;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.util.function.Consumer;

public class GenerateLandscapeDialog extends JFrame {
    private final GenerateLandscapeConfiguration.GenerateLandscapeConfigurationBuilder builder;

    public GenerateLandscapeDialog(Consumer<GenerateLandscapeConfiguration> onComplete) throws HeadlessException {
        super();

        builder = GenerateLandscapeConfiguration.builder();

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel algorithmLabel = new JLabel("Algorithm");
        JComboBox<GeneratorAlgorithm> algorithm = new JComboBox<>(
                GeneratorAlgorithm.values()
        );
        algorithm.addActionListener(evt -> builder.algorithm((GeneratorAlgorithm) algorithm.getSelectedItem()));
        algorithm.setSelectedItem(GeneratorAlgorithm.PERLIN_NOISE);
        panel.add(algorithmLabel);
        panel.add(algorithm);

        JLabel seedLabel = new JLabel("Seed");
        JTextField seed = new JTextField(5);
        panel.add(seedLabel);
        panel.add(seed);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(evt -> {
            setVisible(false);
        });
        JButton submit = new JButton("Submit");
        submit.addActionListener(evt -> {
            int seedInt = Integer.parseInt(seed.getText());
            builder.seed(seedInt);
            setVisible(false);
            onComplete.accept(builder.build());
        });

        panel.add(cancel);
        panel.add(submit);

        add(panel);
        pack();
        setVisible(true);
    }
}
