package me.sentaihex.client.gui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import me.sentaihex.client.SentaiHex;
import me.sentaihex.client.module.ClientModule;
import me.sentaihex.client.module.macros.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ClickGUI extends JFrame implements NativeKeyListener {

    private static final Color BG        = new Color(13, 17, 23);
    private static final Color PANEL_BG  = new Color(20, 26, 36);
    private static final Color HEADER_BG = new Color(16, 21, 30);
    private static final Color ACCENT    = new Color(0, 200, 120);
    private static final Color RED       = new Color(220, 60, 80);
    private static final Color YELLOW    = new Color(220, 180, 50);
    private static final Color TEXT      = new Color(220, 230, 240);
    private static final Color MUTED     = new Color(100, 110, 125);
    private static final Color BORDER    = new Color(30, 38, 52);

    // ✅ Keybind listener state
    private ClientModule listeningModule = null;
    private String listeningSlot = null; // "macro", "slot1", "slot2", "slot3"
    private JButton listeningBtn = null;

    public ClickGUI() {
        setTitle("SentaiHex");
        setSize(500, 580);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setAlwaysOnTop(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 120, 80), 1));

        root.add(makeTitleBar(), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 12, 10, 12));

        for (ClientModule m : SentaiHex.INSTANCE.moduleManager.getModules()) {
            content.add(buildCard(m));
            content.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(14);

        root.add(scroll, BorderLayout.CENTER);
        root.add(makeFooter(), BorderLayout.SOUTH);
        setContentPane(root);

        GlobalScreen.addNativeKeyListener(this);
    }

    // ==================== TITLE BAR ====================
    private JPanel makeTitleBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(HEADER_BG);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(10, 14, 10, 12)
        ));

        JLabel title = new JLabel("  SentaiHex  //  Macro Panel");
        title.setFont(new Font("Courier New", Font.BOLD, 13));
        title.setForeground(ACCENT);

        JButton closeBtn = new JButton("  ✕  ");
        closeBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        closeBtn.setForeground(MUTED);
        closeBtn.setBackground(HEADER_BG);
        closeBtn.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { closeBtn.setForeground(RED); }
            public void mouseExited(MouseEvent e) { closeBtn.setForeground(MUTED); }
        });
        closeBtn.addActionListener(e -> setVisible(false));

        bar.add(title, BorderLayout.WEST);
        bar.add(closeBtn, BorderLayout.EAST);

        MouseAdapter drag = new MouseAdapter() {
            Point start;
            public void mousePressed(MouseEvent e) { start = e.getPoint(); }
            public void mouseDragged(MouseEvent e) {
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - start.x, loc.y + e.getY() - start.y);
            }
        };
        bar.addMouseListener(drag);
        bar.addMouseMotionListener(drag);
        return bar;
    }

    // ==================== FOOTER ====================
    private JPanel makeFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        footer.setBackground(HEADER_BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));
        JLabel hint = new JLabel("INSERT = an/hien GUI   |   Click [SET KEY] de doi phim");
        hint.setFont(new Font("Courier New", Font.PLAIN, 10));
        hint.setForeground(MUTED);
        footer.add(hint);
        return footer;
    }

    // ==================== MODULE CARD ====================
    private JPanel buildCard(ClientModule module) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(0, 0, 8, 0)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9999));

        // Header
        card.add(makeCardHeader(module));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBackground(BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(PANEL_BG);
        body.setBorder(new EmptyBorder(8, 14, 4, 14));

        // Macro keybind
        body.add(makeKeyRow("Macro Key:", module, "macro", module.getKeybind()));
        body.add(Box.createVerticalStrut(6));

        // Slot keybinds + delays tùy loại
        if (module instanceof AnchorMacro m) {
            body.add(makeSectionLabel("SLOTS"));
            body.add(makeKeyRow("Anchor slot:", m, "slot1", m.getSlotAnchor()));
            body.add(Box.createVerticalStrut(4));
            body.add(makeKeyRow("Glowstone slot:", m, "slot2", m.getSlotGlowstone()));
            body.add(Box.createVerticalStrut(4));
            body.add(makeKeyRow("Totem slot:", m, "slot3", m.getSlotTotem()));
            body.add(Box.createVerticalStrut(8));
            body.add(makeSectionLabel("DELAYS"));
            body.add(makeDelayRow("Anchor -> Glowstone:", m.getDelay1(), m::setDelay1));
            body.add(Box.createVerticalStrut(4));
            body.add(makeDelayRow("Glowstone -> Totem:", m.getDelay2(), m::setDelay2));
            body.add(Box.createVerticalStrut(4));
            body.add(makeDelayRow("Totem -> End:", m.getDelay3(), m::setDelay3));

        } else if (module instanceof TNTCartMacro m) {
            body.add(makeSectionLabel("SLOTS"));
            body.add(makeKeyRow("Rail slot:", m, "slot1", m.getSlotRail()));
            body.add(Box.createVerticalStrut(4));
            body.add(makeKeyRow("TNT Cart slot:", m, "slot2", m.getSlotCart()));
            body.add(Box.createVerticalStrut(4));
            body.add(makeKeyRow("Crossbow slot:", m, "slot3", m.getSlotCrossbow()));
            body.add(Box.createVerticalStrut(8));
            body.add(makeSectionLabel("DELAYS"));
            body.add(makeDelayRow("Rail -> Cart:", m.getDelay1(), m::setDelay1));
            body.add(Box.createVerticalStrut(4));
            body.add(makeDelayRow("Cart -> Crossbow:", m.getDelay2(), m::setDelay2));
            body.add(Box.createVerticalStrut(4));
            body.add(makeDelayRow("Crossbow -> End:", m.getDelay3(), m::setDelay3));

        } else if (module instanceof MaceTech1 m) {
            body.add(makeSectionLabel("SLOTS"));
            body.add(makeKeyRow("Pearl slot:", m, "slot1", m.getSlotPearl()));
            body.add(Box.createVerticalStrut(4));
            body.add(makeKeyRow("Wind Charge slot:", m, "slot2", m.getSlotWindCharge()));
            body.add(Box.createVerticalStrut(8));
            body.add(makeSectionLabel("DELAYS"));
            body.add(makeDelayRow("Pearl -> Wind:", m.getDelay1(), m::setDelay1));
            body.add(Box.createVerticalStrut(4));
            body.add(makeDelayRow("Wind -> End:", m.getDelay2(), m::setDelay2));

        } else if (module instanceof MaceTech2 m) {
            body.add(makeSectionLabel("SLOTS"));
            body.add(makeKeyRow("Axe slot:", m, "slot1", m.getSlotAxe()));
            body.add(Box.createVerticalStrut(4));
            body.add(makeKeyRow("Mace slot:", m, "slot2", m.getSlotMace()));
            body.add(Box.createVerticalStrut(8));
            body.add(makeSectionLabel("DELAYS"));
            body.add(makeDelayRow("Axe -> Mace:", m.getDelay1(), m::setDelay1));
            body.add(Box.createVerticalStrut(4));
            body.add(makeDelayRow("Mace -> End:", m.getDelay2(), m::setDelay2));
        }

        card.add(body);
        return card;
    }

    // ==================== CARD HEADER ====================
    private JPanel makeCardHeader(ClientModule module) {
        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel name = new JLabel(module.getName());
        name.setFont(new Font("Courier New", Font.BOLD, 13));
        name.setForeground(TEXT);

        // RUN button
        JButton runBtn = new JButton("  RUN  ");
        runBtn.setFont(new Font("Courier New", Font.BOLD, 11));
        runBtn.setForeground(ACCENT);
        runBtn.setBackground(new Color(0, 200, 120, 18));
        runBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 200, 120, 100), 1),
                new EmptyBorder(4, 10, 4, 10)
        ));
        runBtn.setFocusPainted(false);
        runBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        runBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { runBtn.setBackground(new Color(0, 200, 120, 35)); }
            public void mouseExited(MouseEvent e) { runBtn.setBackground(new Color(0, 200, 120, 18)); }
        });
        runBtn.addActionListener(e -> {
            if (runBtn.isEnabled()) {
                runBtn.setText(" ... ");
                runBtn.setEnabled(false);
                new Thread(() -> {
                    try { module.execute(); }
                    catch (InterruptedException ignored) {}
                    finally {
                        SwingUtilities.invokeLater(() -> {
                            runBtn.setText("  RUN  ");
                            runBtn.setEnabled(true);
                        });
                    }
                }).start();
            }
        });

        header.add(name, BorderLayout.WEST);
        header.add(runBtn, BorderLayout.EAST);
        return header;
    }

    // ==================== SECTION LABEL ====================
    private JLabel makeSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Courier New", Font.BOLD, 10));
        label.setForeground(MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(4, 0, 4, 0));
        return label;
    }

    // ==================== KEY ROW ====================
    private JPanel makeKeyRow(String labelText, ClientModule module, String slot, int currentKey) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setBackground(PANEL_BG);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Courier New", Font.PLAIN, 11));
        label.setForeground(MUTED);
        label.setPreferredSize(new Dimension(140, 20));

        String keyName = currentKey == -1 ? "NONE" : KeyEvent.getKeyText(currentKey);
        JButton keyBtn = makeSmallBtn("[" + keyName + "]", YELLOW);
        keyBtn.setPreferredSize(new Dimension(100, 24));

        keyBtn.addActionListener(e -> {
            listeningModule = module;
            listeningSlot = slot;
            listeningBtn = keyBtn;
            keyBtn.setText("[Press...]");
            keyBtn.setForeground(RED);
        });

        row.add(label, BorderLayout.WEST);
        row.add(keyBtn, BorderLayout.EAST);
        return row;
    }

    // ==================== DELAY ROW ====================
    interface IntSetter { void set(int v); }

    private JPanel makeDelayRow(String labelText, int current, IntSetter setter) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setBackground(PANEL_BG);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Courier New", Font.PLAIN, 11));
        label.setForeground(MUTED);
        label.setPreferredSize(new Dimension(140, 20));

        // Right side: [ - ] [ 100ms ] [ + ]
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
        controls.setBackground(PANEL_BG);

        JLabel valLabel = new JLabel(current + "ms");
        valLabel.setFont(new Font("Courier New", Font.BOLD, 11));
        valLabel.setForeground(ACCENT);
        valLabel.setPreferredSize(new Dimension(55, 20));
        valLabel.setHorizontalAlignment(SwingConstants.CENTER);

        int[] val = {current};

        JButton minus = makeSmallBtn(" - ", MUTED);
        JButton plus  = makeSmallBtn(" + ", MUTED);

        minus.addActionListener(e -> {
            val[0] = Math.max(0, val[0] - 10);
            valLabel.setText(val[0] + "ms");
            setter.set(val[0]);
            SentaiHex.INSTANCE.configManager.save();
        });
        plus.addActionListener(e -> {
            val[0] = Math.min(1000, val[0] + 10);
            valLabel.setText(val[0] + "ms");
            setter.set(val[0]);
            SentaiHex.INSTANCE.configManager.save();
        });

        controls.add(minus);
        controls.add(valLabel);
        controls.add(plus);

        row.add(label, BorderLayout.WEST);
        row.add(controls, BorderLayout.EAST);
        return row;
    }

    private JButton makeSmallBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Courier New", Font.PLAIN, 10));
        btn.setForeground(color);
        btn.setBackground(HEADER_BG);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80), 1),
                new EmptyBorder(2, 6, 2, 6)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ==================== KEY LISTENER ====================
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int code = e.getKeyCode();

        // INSERT toggle GUI
        if (code == NativeKeyEvent.VC_INSERT) {
            SwingUtilities.invokeLater(() -> {
                if (isVisible()) setVisible(false);
                else { setVisible(true); toFront(); }
            });
            return;
        }

        // ✅ Đang chờ keybind
        if (listeningModule != null && listeningBtn != null) {
            final int keyCode = code;
            final String keyText = NativeKeyEvent.getKeyText(code);

            SwingUtilities.invokeLater(() -> {
                // Gán key cho đúng slot
                switch (listeningSlot) {
                    case "macro" -> listeningModule.setKeybind(keyCode);
                    case "slot1" -> {
                        if (listeningModule instanceof AnchorMacro m) m.setSlotAnchor(keyCode);
                        else if (listeningModule instanceof TNTCartMacro m) m.setSlotRail(keyCode);
                        else if (listeningModule instanceof MaceTech1 m) m.setSlotPearl(keyCode);
                        else if (listeningModule instanceof MaceTech2 m) m.setSlotAxe(keyCode);
                    }
                    case "slot2" -> {
                        if (listeningModule instanceof AnchorMacro m) m.setSlotGlowstone(keyCode);
                        else if (listeningModule instanceof TNTCartMacro m) m.setSlotCart(keyCode);
                        else if (listeningModule instanceof MaceTech1 m) m.setSlotWindCharge(keyCode);
                        else if (listeningModule instanceof MaceTech2 m) m.setSlotMace(keyCode);
                    }
                    case "slot3" -> {
                        if (listeningModule instanceof AnchorMacro m) m.setSlotTotem(keyCode);
                        else if (listeningModule instanceof TNTCartMacro m) m.setSlotCrossbow(keyCode);
                    }
                }
                listeningBtn.setText("[" + keyText + "]");
                listeningBtn.setForeground(YELLOW);
                SentaiHex.INSTANCE.configManager.save();

                listeningModule = null;
                listeningSlot = null;
                listeningBtn = null;
            });
            return;
        }

        // ✅ Trigger macro qua keybind CHỈ khi GUI đang mở
        if (!isVisible()) return;
        for (ClientModule m : SentaiHex.INSTANCE.moduleManager.getModules()) {
            if (m.getKeybind() == code) {
                new Thread(() -> {
                    try { m.execute(); }
                    catch (InterruptedException ignored) {}
                }).start();
            }
        }
    }

    @Override public void nativeKeyReleased(NativeKeyEvent e) {}
    @Override public void nativeKeyTyped(NativeKeyEvent e) {}
}