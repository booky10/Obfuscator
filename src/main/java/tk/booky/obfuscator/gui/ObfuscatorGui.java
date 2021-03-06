package tk.booky.obfuscator.gui;
// Created by booky10 in Obfuscator (16:17 31.10.20)

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import tk.booky.obfuscator.main.Obfuscator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObfuscatorGui extends JFrame {

    private JTextField inputJarText;
    private JLabel inputJarLabel;
    private JLabel outputJarLabel;
    private JTextField outputJarText;
    private JScrollPane textPane;
    private JTextArea renamingExcluded;
    private JButton browseInput;
    private JButton browseOutput;
    private JButton obfuscate;
    private JPanel panel;
    private JLabel transformersLabel;
    private JCheckBox accessTransformer;
    private JCheckBox constantTransformer;
    private JCheckBox crasherTransformer;
    private JCheckBox fieldTransformer;
    private JCheckBox renamingTransformer;
    private JCheckBox shuffleTransformer;
    private JCheckBox stringTransformer;

    public ObfuscatorGui() {
        super("Obfuscator");
        $$$setupUI$$$();
        ThemeManager.setDefault();

        setContentPane($$$getRootComponent$$$());
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        obfuscate.addActionListener(event -> {
            if (!obfuscate.isEnabled()) return;

            String excluded;
            if (renamingExcluded.getText() == null || renamingExcluded.getText().isEmpty()) excluded = "none";
            else excluded = renamingExcluded.getText().replace('\n', ',');

            if (inputJarText.getText() == null || inputJarText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please specify an input jar!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (outputJarText.getText() == null || outputJarText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please specify an output jar!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File input = new File(inputJarText.getText());
            File output = new File(outputJarText.getText());
            List<String> renamingExcluded = Arrays.asList(excluded.split(","));
            List<String> excludedTransformers = new ArrayList<>();

            if (!accessTransformer.isSelected()) excludedTransformers.add(accessTransformer.getToolTipText());
            if (!constantTransformer.isSelected()) excludedTransformers.add(constantTransformer.getToolTipText());
            if (!crasherTransformer.isSelected()) excludedTransformers.add(crasherTransformer.getToolTipText());
            if (!fieldTransformer.isSelected()) excludedTransformers.add(fieldTransformer.getToolTipText());
            if (!renamingTransformer.isSelected()) excludedTransformers.add(renamingTransformer.getToolTipText());
            if (!shuffleTransformer.isSelected()) excludedTransformers.add(shuffleTransformer.getToolTipText());
            if (!stringTransformer.isSelected()) excludedTransformers.add(stringTransformer.getToolTipText());

            obfuscate.setEnabled(false);
            new Thread(() -> {
                try {
                    new Obfuscator(input, output, renamingExcluded, excludedTransformers, false);
                    JOptionPane.showMessageDialog(this, "Success!\nYou're jar is now obfuscated.", "Finished", JOptionPane.INFORMATION_MESSAGE);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    JOptionPane.showMessageDialog(this, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                obfuscate.setEnabled(true);
            }).start();
        });

        browseInput.addActionListener(event -> {
            String file = GuiUtils.chooseFile(null, this, new JarFileFilter());
            if (file != null) inputJarText.setText(file);
        });

        browseOutput.addActionListener(event -> {
            String file = GuiUtils.chooseFile(null, this, new JarFileFilter());
            if (file != null) outputJarText.setText(file);
        });

        setVisible(true);
        System.out.println("Gui done");
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(8, 4, new Insets(15, 15, 15, 15), -1, -1));
        inputJarText = new JTextField();
        inputJarText.setText("");
        inputJarText.setToolTipText("The jar that needs to be obfuscated");
        panel.add(inputJarText, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textPane = new JScrollPane();
        textPane.setAutoscrolls(true);
        panel.add(textPane, new GridConstraints(7, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(500, 500), null, 0, false));
        renamingExcluded = new JTextArea();
        renamingExcluded.setText("none");
        textPane.setViewportView(renamingExcluded);
        browseInput = new JButton();
        browseInput.setText("Browse");
        panel.add(browseInput, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outputJarText = new JTextField();
        outputJarText.setToolTipText("The jar which will be obfuscated");
        panel.add(outputJarText, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseOutput = new JButton();
        browseOutput.setText("Browse");
        panel.add(browseOutput, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outputJarLabel = new JLabel();
        outputJarLabel.setText("Output Jar");
        outputJarLabel.setToolTipText("The jar which will be obfuscated");
        panel.add(outputJarLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        inputJarLabel = new JLabel();
        inputJarLabel.setText("Input Jar");
        inputJarLabel.setToolTipText("The jar that needs to be obfuscated");
        panel.add(inputJarLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, 98), null, 0, false));
        obfuscate = new JButton();
        obfuscate.setText("Obfuscate");
        obfuscate.setToolTipText("Start the obfuscation process");
        panel.add(obfuscate, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        transformersLabel = new JLabel();
        transformersLabel.setText("Transformers");
        panel.add(transformersLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accessTransformer = new JCheckBox();
        accessTransformer.setSelected(true);
        accessTransformer.setText("Randomize Access");
        accessTransformer.setToolTipText("access");
        panel.add(accessTransformer, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        constantTransformer = new JCheckBox();
        constantTransformer.setSelected(true);
        constantTransformer.setText("Obfuscate Constants");
        constantTransformer.setToolTipText("constant");
        panel.add(constantTransformer, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crasherTransformer = new JCheckBox();
        crasherTransformer.setSelected(true);
        crasherTransformer.setText("Crash Decompiler");
        crasherTransformer.setToolTipText("crasher");
        panel.add(crasherTransformer, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldTransformer = new JCheckBox();
        fieldTransformer.setSelected(true);
        fieldTransformer.setText("Rename Fields");
        fieldTransformer.setToolTipText("field");
        panel.add(fieldTransformer, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        renamingTransformer = new JCheckBox();
        renamingTransformer.setSelected(true);
        renamingTransformer.setText("Rename Classes");
        renamingTransformer.setToolTipText("renaming");
        panel.add(renamingTransformer, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shuffleTransformer = new JCheckBox();
        shuffleTransformer.setSelected(true);
        shuffleTransformer.setText("Shuffle Components");
        shuffleTransformer.setToolTipText("shuffle");
        panel.add(shuffleTransformer, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stringTransformer = new JCheckBox();
        stringTransformer.setSelected(true);
        stringTransformer.setText("Transform Strings");
        stringTransformer.setToolTipText("string");
        panel.add(stringTransformer, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outputJarLabel.setLabelFor(outputJarText);
        inputJarLabel.setLabelFor(inputJarText);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}