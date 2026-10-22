package org.example.wwtbam.ui.components;

import org.example.wwtbam.ui.UITheme;

import javax.swing.*;
import java.awt.*;

public final class GameShowButton extends JButton {
    public enum State {
        NORMAL,
        BLINK_ON,
        BLINK_OFF,
        CORRECT,
        WRONG
    }

    private final Color fill;
    private final Color hoverFill;
    private State state = State.NORMAL;
    private String prefix = "";
    private String labelText = "";
    private boolean hovered;

    public GameShowButton(String text, Color fill, Color hoverFill) {
        super();
        this.fill = fill;
        this.hoverFill = hoverFill;
        this.labelText = extractLabelText(text);

        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 18));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(10, 36, 10, 36));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        updateText();

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                if (state == State.NORMAL) {
                    hovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                hovered = false;
                repaint();
            }
        });
    }

    public static String extractLabelText(String text) {
        if (text == null) {
            return "";
        }
        if (text.contains("<") && text.contains(">")) {
            String stripped = text.replaceAll("<[^>]*>", "");
            stripped = stripped.replace("&nbsp;", " ");
            stripped = stripped.replace("◆", "").trim();
            return stripped;
        }
        return text.trim();
    }

    public static String formatWithShape(String text) {
        String goldHex = String.format("#%02x%02x%02x", UITheme.GOLD.getRed(), UITheme.GOLD.getGreen(), UITheme.GOLD.getBlue());
        return "<html><font color='" + goldHex + "'>◆</font>&nbsp;&nbsp;" + text + "</html>";
    }

    public void setButtonState(State state) {
        this.state = state;
        if (state != State.NORMAL) {
            hovered = false;
        }
        updateText();
    }

    public State getButtonState() {
        return state;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = extractLabelText(labelText);
        updateText();
    }

    public void setPrefix(String prefix) {
        this.prefix = (prefix != null) ? prefix : "";
        updateText();
    }

    public String getPrefix() {
        return prefix;
    }

    private void updateText() {
        if (labelText == null || labelText.isEmpty()) {
            setText("");
            return;
        }
        String diamondHex;
        String prefixHex;
        String textHex;
        switch (state) {
            case BLINK_ON:
                diamondHex = "#070c2a";
                prefixHex = "#070c2a";
                textHex = "#070c2a";
                break;
            case CORRECT:
            case WRONG:
                diamondHex = "#ffffff";
                prefixHex = "#ffffff";
                textHex = "#ffffff";
                break;
            case NORMAL:
            case BLINK_OFF:
            default:
                diamondHex = String.format("#%02x%02x%02x", UITheme.GOLD.getRed(), UITheme.GOLD.getGreen(), UITheme.GOLD.getBlue());
                prefixHex = diamondHex;
                textHex = "#ffffff";
                break;
        }
        String prefixHtml = (prefix == null || prefix.isEmpty())
            ? ""
            : "<font color='" + prefixHex + "'><b>" + prefix + "</b></font>&nbsp;&nbsp;";
        setText("<html><font color='" + diamondHex + "'>◆</font>&nbsp;&nbsp;" + prefixHtml + "<font color='" + textHex + "'>" + labelText + "</font></html>");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        int point = Math.min(24, h / 2);
        Polygon shape = new Polygon(
            new int[]{0, point, w - point, w, w - point, point},
            new int[]{h / 2, 0, 0, h / 2, h, h},
            6
        );

        Graphics2D copy = (Graphics2D) graphics.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color currentFill;
        Color currentBorder;

        switch (state) {
            case BLINK_ON:
                currentFill = UITheme.YELLOW;
                currentBorder = Color.WHITE;
                break;
            case BLINK_OFF:
                currentFill = fill;
                currentBorder = UITheme.GOLD;
                break;
            case CORRECT:
                currentFill = UITheme.GREEN;
                currentBorder = Color.WHITE;
                break;
            case WRONG:
                currentFill = UITheme.RED;
                currentBorder = Color.WHITE;
                break;
            case NORMAL:
            default:
                currentFill = hovered ? hoverFill : fill;
                currentBorder = UITheme.GOLD;
                break;
        }

        copy.setColor(currentFill);
        copy.fillPolygon(shape);
        copy.setColor(currentBorder);
        copy.setStroke(new BasicStroke(2f));
        copy.drawPolygon(shape);
        copy.dispose();
        super.paintComponent(graphics);
    }
}
