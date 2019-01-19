package com.email.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.email.scanner.ReadProperties.readPropertiesFromConfigFile;

public class SimpleGUI extends JFrame {

    Logger logger = LoggerFactory.getLogger(ReceiveEmailWithAttachment.class);
    private JButton buttonGo = new JButton("Start");
    private JButton buttonFiles = new JButton("Files to analyze");
    private JLabel messageSubject = new JLabel("messageSubject");
    public JTextField messageSubjectText = new JTextField("ttt", 5);
    private JLabel regDate = new JLabel("столбец B (Дата регистрации)");
    private JTextField regDateText = new JTextField("21.04.2018", 5);
    private JTextField inputNameFile = new JTextField("Name", 5);

    private JLabel labelNameFile = new JLabel("  Save Analyze results to file:");
    private JLabel labelQuartal = new JLabel("  Send emails?");
    private Checkbox checkboxQuartal = new Checkbox();
    public SimpleGUI() {
        super("naumen");
        this.setAlwaysOnTop(true);
        this.setBounds(220, 300, 450, 200);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5, 2, 2, 2));
        container.add(this.regDate);
        container.add(this.regDateText);
        container.add(this.messageSubject);
        container.add(this.messageSubjectText);
        container.add(this.labelQuartal);
        container.add(this.checkboxQuartal);
        container.add(this.labelNameFile);
        container.add(this.inputNameFile);
        container.add(this.buttonFiles);
        container.add(this.buttonGo);

        this.buttonGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.info("New run. Initializing.");
                SimpleGUI.super.setVisible(false);
                readPropertiesFromConfigFile();
                ReceiveEmailWithAttachment.main(new String[]{});
            }
        });

        this.buttonFiles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimpleGUI.this.setAlwaysOnTop(false);
                JFileChooser jfc = new JFileChooser("C:\\");
                jfc.setMultiSelectionEnabled(true);
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.FILES_ONLY) {
                    File[] selectedFiles = jfc.getSelectedFiles();
                    for (File f : selectedFiles) {
                        System.out.println(f.getAbsolutePath());
                        App.startread(f.getAbsolutePath());
                    }
                }
            }
        });
    }

    public String getMessageSubjectText() {
        return messageSubjectText.getText();
    }
}
