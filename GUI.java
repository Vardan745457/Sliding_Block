import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI {
    public static void init() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Test Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
          
        JButton northButton = new JButton("North");
        panel.add(northButton, BorderLayout.NORTH);
          
        JButton southButton = new JButton("South");
        panel.add(southButton, BorderLayout.SOUTH);
          
        JButton eastButton = new JButton("East");
        panel.add(eastButton, BorderLayout.EAST);
          
        JButton westButton = new JButton("West");
        panel.add(westButton, BorderLayout.WEST);
          
        JButton centerButton = new JButton("Center");
        panel.add(centerButton, BorderLayout.CENTER);
          
        frame.getContentPane().add(panel);          
        frame.setPreferredSize(new Dimension(500, 400));
          
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }  
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                init();
            }
        });
    }
}