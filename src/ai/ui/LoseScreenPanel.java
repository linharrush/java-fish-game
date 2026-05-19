package ai.ui;

import base.Params;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import shared.MainRouter;

public class LoseScreenPanel extends JPanel {
    private final MainRouter mainRouter;

    public LoseScreenPanel(MainRouter mainRouter) {
        this.mainRouter = mainRouter;

        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(120, 60, 70, 60));

        JLabel loseLabel = new JLabel(loadLoseIcon(), SwingConstants.CENTER);
        if (loseLabel.getIcon() == null) {
            loseLabel.setText("LOSE");
            loseLabel.setForeground(new Color(220, 40, 40));
            loseLabel.setFont(new Font("SansSerif", Font.BOLD, 96));
        }
        add(loseLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton startNewGameButton = new JButton("Start New Game");
        startNewGameButton.setPreferredSize(new Dimension(260, 54));
        startNewGameButton.setFont(new Font("SansSerif", Font.BOLD, 22));
        startNewGameButton.setForeground(Color.WHITE);
        startNewGameButton.setBackground(new Color(20, 120, 190));
        startNewGameButton.setFocusPainted(false);
        startNewGameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startNewGameButton.addActionListener(e -> mainRouter.route("/ocean/menu", Params.of()));

        buttonPanel.add(startNewGameButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private ImageIcon loadLoseIcon() {
        java.net.URL url = getClass().getResource("images/LOSE.png");
        if (url == null) {
            return null;
        }

        Image image = new ImageIcon(url).getImage().getScaledInstance(540, 270, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
