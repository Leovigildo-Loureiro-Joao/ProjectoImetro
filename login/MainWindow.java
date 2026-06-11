import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

  private CustomTextField1 textField1;

  public MainWindow() {
    setTitle("MainWindow");
    setSize(1024, 768);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(null);

    textField1 = new CustomTextField1();
    textField1.setBounds(85, 520, 180, 36);
    this.add(textField1);

    setLocationRelativeTo(null);
  }
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainWindow frame = new MainWindow();
      frame.setVisible(true);
    });
  }
}
