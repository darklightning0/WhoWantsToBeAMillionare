package org.example.wwtbam.ui.components;

import java.awt.*;
import java.util.Map;
import javax.swing.*;
import org.example.wwtbam.ui.UITheme;

public final class JokerFeedbackCard extends RoundedPanel {

    private final JLabel titleLabel;
    private final JLabel contentLabel;

    public JokerFeedbackCard() {
        super(new Color(11, 23, 62, 235), 18);
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(245, 190, 63, 190), 2, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        setPreferredSize(new Dimension(340, 120));
        setMaximumSize(new Dimension(360, 160));
        setMinimumSize(new Dimension(280, 80));

        titleLabel = new JLabel("◆ JOKER");
        titleLabel.setForeground(UITheme.GOLD);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        contentLabel = new JLabel();
        contentLabel.setForeground(UITheme.TEXT);
        contentLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        contentLabel.setVerticalAlignment(SwingConstants.TOP);

        add(titleLabel, BorderLayout.NORTH);
        add(contentLabel, BorderLayout.CENTER);

        setVisible(false);
    }

    public void setFeedback(String title, String message) {
        titleLabel.setText("◆ " + title.toUpperCase());
        contentLabel.setText("<html><table width='290' cellpadding='0' cellspacing='0'><tr><td style='color: #f5f7ff; font-family: SansSerif; font-size: 13px; line-height: 1.3;'>"
            + escapeHtml(message)
            + "</td></tr></table></html>");
        setVisible(true);
        revalidate();
        repaint();
    }

    public void setAudienceFeedback(String title, Map<String, Integer> votes) {
        titleLabel.setText("◆ " + title.toUpperCase());
        StringBuilder sb = new StringBuilder();
        sb.append("<html><table width='290' cellpadding='2' cellspacing='0' style='color: #f5f7ff; font-family: SansSerif; font-size: 11px;'>");
        if (votes != null) {
            for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                int pct = Math.max(0, Math.min(100, entry.getValue()));
                int bars = Math.max(0, Math.min(10, Math.round(pct / 10.0f)));
                String barStr = "■".repeat(bars) + "□".repeat(10 - bars);
                String label = entry.getKey();
                if (label == null) {
                    label = "";
                }
                if (label.length() > 12) {
                    label = label.substring(0, 10) + "..";
                }
                sb.append("<tr>")
                  .append("<td style='white-space: nowrap; padding-right: 8px;'><b>").append(escapeHtml(label)).append(":</b></td>")
                  .append("<td style='color: #4db3ff; letter-spacing: -1px; font-family: monospace;'>").append(barStr).append("</td>")
                  .append("<td style='color: #f5be3f; text-align: right; width: 38px;'><b>").append(pct).append("%</b></td>")
                  .append("</tr>");
            }
        }
        sb.append("</table></html>");
        contentLabel.setText(sb.toString());
        setVisible(true);
        revalidate();
        repaint();
    }

    public void clear() {
        titleLabel.setText("");
        contentLabel.setText("");
        setVisible(false);
    }

    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
