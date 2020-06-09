package it.uniba.ventricellisardone.itss.ui;

import javax.swing.*;

public class MainPanel {
    private JPanel MainPanel;
    private JButton AnalysisButton;
    private JButton ETLButton;


    public MainPanel() {
        AnalysisButton.addActionListener(e -> {
            JFrame frame = new JFrame("Data analysis");
            frame.setContentPane(new DataAnalysis().getDataAnalysis());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ETL tool");
        frame.setContentPane(new MainPanel().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
