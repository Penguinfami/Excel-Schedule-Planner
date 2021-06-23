import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Font;

public class TitlePanel extends JPanel {

    private String titleStr = ("<HTML><H1>My Progress Planner</H1></HTML>");

    private JButton closeButton = new JButton("EXIT");

    public TitlePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void updateAgendaName(String name) {
        System.out.println("updating title name");
        this.removeAll();
        this.repaint();
        JLabel label = new JLabel(titleStr);
        label.setFont(new Font("Verdana", Font.BOLD, 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        JLabel label2 = new JLabel("<HTML><H1>Currently using schedule: <b>" + name + "<b></H1></HTML>");
        label2.setFont(new Font("Verdana", Font.BOLD, 18));
        label2.setHorizontalAlignment(JLabel.CENTER);
        this.add(label);
        this.add(label2);
        this.revalidate();
    }
}
