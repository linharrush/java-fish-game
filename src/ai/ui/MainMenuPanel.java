package ai.ui;

import base.Params;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import shared.MainRouter;

public class MainMenuPanel extends JPanel {
    private final MainRouter mainRouter;
    private int selectedSkin = 1;
    private JButton[] skinButtons;

    public MainMenuPanel(MainRouter mainRouter) {
        this.mainRouter = mainRouter;
        this.skinButtons = new JButton[3];

        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(35, 55, 45, 55));

        JLabel title = new JLabel("Choose Your Fish", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        add(title, BorderLayout.NORTH);

        JPanel skinPanel = new JPanel(new GridLayout(1, 3, 28, 0));
        skinPanel.setOpaque(false);
        for (int skin = 1; skin <= 3; skin++) {
            skinPanel.add(createSkinButton(skin));
        }
        add(skinPanel, BorderLayout.CENTER);

        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startPanel.setOpaque(false);
        JButton startButton = new JButton("START");
        startButton.setPreferredSize(new Dimension(220, 54));
        startButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(20, 120, 190));
        startButton.setFocusPainted(false);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.addActionListener(e -> mainRouter.route("/ocean/start", Params.of(selectedSkin)));
        startPanel.add(startButton);
        add(startPanel, BorderLayout.SOUTH);

        updateSelectedSkin();
    }

    private JButton createSkinButton(int skin) {
        JButton button = new JButton("Fish " + skin, loadFishIcon(skin));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(18, 18, 18, 18));
        button.addActionListener(e -> {
            selectedSkin = skin;
            updateSelectedSkin();
        });

        skinButtons[skin - 1] = button;
        return button;
    }

    private ImageIcon loadFishIcon(int skin) {
        java.net.URL url = getClass().getResource("images/fish" + skin + "R.png");
        if (url == null) {
            return null;
        }

        Image image = new ImageIcon(url).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private void updateSelectedSkin() {
        for (int i = 0; i < skinButtons.length; i++) {
            if (skinButtons[i] == null) {
                continue;
            }

            if (i + 1 == selectedSkin) {
                skinButtons[i].setBorder(BorderFactory.createLineBorder(new Color(255, 220, 80), 4));
            } else {
                skinButtons[i].setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 2));
            }
        }
    }
}
