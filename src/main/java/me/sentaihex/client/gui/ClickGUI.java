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
import java.awt.geom.RoundRectangle2D;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClickGUI extends JFrame implements NativeKeyListener {

    private static final Color BG         = new Color(6, 36, 17);
    private static final Color CARD_BG    = new Color(16, 56, 30);
    private static final Color ACCENT     = new Color(212, 175, 55);
    private static final Color TEXT_LIGHT = new Color(240, 245, 242);
    private static final Color MUTED      = new Color(135, 160, 145);
    private static final Color BORDER     = new Color(25, 75, 45);

    private static final int    CORNER_RADIUS = 24;
    private static final String FONT_NAME     = "Segoe UI";

    private ClientModule listeningModule = null;
    private String       listeningSlot   = null;
    private JButton      listeningBtn    = null;

    private Point dragPoint = null;

    public ClickGUI() {
        setTitle("SentaiHex Premium");
        setSize(850, 480);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);

        if (getContentPane() instanceof JComponent jc) {
            jc.setOpaque(false);
        }

        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS));
            }
        });

        setContentPane(createMainPanel());
        GlobalScreen.addNativeKeyListener(this);
    }

    // --- MINECRAFT MOUSE HELPER (REFLECTION) ---
    private Object getMinecraftMouseHelper() {
        try {
            Class<?> mcClass = Class.forName("net.minecraft.client.Minecraft");
            Object mc = mcClass.getMethod("getMinecraft").invoke(null);
            if (mc == null) return null;
            for (Field f : mcClass.getDeclaredFields()) {
                f.setAccessible(true);
                Object val = f.get(mc);
                if (val != null && val.getClass().getSimpleName().toLowerCase().contains("mouse"))
                    return val;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private void executeMouseAction(String keyword) {
        Object mouseHelper = getMinecraftMouseHelper();
        if (mouseHelper == null) return;
        try {
            for (Method method : mouseHelper.getClass().getDeclaredMethods()) {
                method.setAccessible(true);
                String name = method.getName().toLowerCase();
                if (name.contains(keyword) && method.getParameterCount() == 0) {
                    method.invoke(mouseHelper);
                    break;
                }
            }
        } catch (Exception ignored) {}
    }

    private void unlockMinecraftMouse() { executeMouseAction("ungrab"); }
    private void lockMinecraftMouse()   { executeMouseAction("grab"); }

    private boolean isMinecraftFocused() {
        try {
            Class<?> mcClass = Class.forName("net.minecraft.client.Minecraft");
            Object mc = mcClass.getMethod("getMinecraft").invoke(null);
            if (mc == null) return false;
            for (Field f : mcClass.getDeclaredFields()) {
                f.setAccessible(true);
                String fname = f.getName().toLowerCase();
                if (fname.contains("ingamehasfocus") || fname.contains("hasfocus") || fname.contains("field_71415_x")) {
                    Object val = f.get(mc);
                    if (val instanceof Boolean b) return b;
                }
            }
            Class<?> displayClass = Class.forName("org.lwjgl.opengl.Display");
            return (Boolean) displayClass.getMethod("isActive").invoke(null);
        } catch (Exception e) {
            return true;
        }
    }

    // --- PANEL BUILDER ---
    private JPanel createMainPanel() {
        JPanel mainPanel = createBackgroundPanel();
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createContentGrid(), BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createBackgroundPanel() {
        JPanel bgPanel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                g2d.dispose();
            }
        };
        bgPanel.setOpaque(false);
        return bgPanel;
    }

    private JPanel createContentGrid() {
        JPanel contentGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        contentGrid.setOpaque(false);
        contentGrid.setBorder(new EmptyBorder(10, 25, 25, 25));

        for (ClientModule m : SentaiHex.INSTANCE.moduleManager.getByCategory("Macro")) {
            contentGrid.add(createMacroCard(m));
        }

        int loaded = SentaiHex.INSTANCE.moduleManager.getByCategory("Macro").size();
        for (int i = loaded; i < 4; i++) contentGrid.add(createEmptyCard());

        return contentGrid;
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(850, 65));

        JLabel title = new JLabel("🔱 Thượng đỉnh, thượng hạng 🔱", SwingConstants.CENTER);
        title.setFont(new Font(FONT_NAME, Font.BOLD, 18));
        title.setForeground(ACCENT);
        header.add(title, BorderLayout.CENTER);

        JButton closeBtn = new JButton("✕ ");
        closeBtn.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
        closeBtn.setForeground(MUTED);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> hideGUI());
        header.add(closeBtn, BorderLayout.EAST);

        header.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { dragPoint = e.getPoint(); }
        });
        header.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                if (dragPoint != null) {
                    Point p = e.getLocationOnScreen();
                    setLocation(p.x - dragPoint.x, p.y - dragPoint.y);
                }
            }
        });

        return header;
    }

    private JPanel createMacroCard(ClientModule m) {
        JPanel card = createCardContainer();

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        buildCardBodySlots(m, body);
        body.add(createSlotRow(m, "mainBind", "Bind"));
        card.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));
        footer.add(createNameLabel(m), BorderLayout.NORTH);
        footer.add(createToggleSwitch(m), BorderLayout.SOUTH);

        card.add(footer, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createCardContainer() {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(BORDER);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(15, 12, 15, 12));
        return container;
    }

    // [FIX #3] Label đúng với slot mapping trong getModuleSlotKey / updateModuleSlotKey
    private void buildCardBodySlots(ClientModule m, JPanel body) {
        if (m instanceof AnchorMacro) {
            body.add(createSlotRow(m, "slot1", "Anchor"));
            body.add(createSlotRow(m, "slot2", "Glow"));
            body.add(createSlotRow(m, "slot3", "Totem"));
        } else if (m instanceof TNTCartMacro) {
            body.add(createSlotRow(m, "slot1", "Rail"));
            body.add(createSlotRow(m, "slot2", "Cart"));   // Fix: "Tnt" → "Cart" cho đúng nghĩa
            body.add(createSlotRow(m, "slot3", "Crossbow"));
        } else if (m instanceof MaceTech1) {
            body.add(createSlotRow(m, "slot1", "Pearl"));      // [FIX #3] slot1 = Pearl (không phải Mace)
            body.add(createSlotRow(m, "slot2", "Wind"));
        } else if (m instanceof MaceTech2) {
            body.add(createSlotRow(m, "slot1", "Mace"));       // [FIX #3] slot1 = Mace, slot2 = Axe
            body.add(createSlotRow(m, "slot2", "Axe"));
        }
    }

    private JLabel createNameLabel(ClientModule m) {
        String displayName = switch (m.getName()) {
            case "Anchor Bomb"              -> "Respawn Anchor";
            case "TNT Cart"                 -> "TNT Cart";
            case "Mace Tech 1 (Pearl+Wind)" -> "Pearl + Wind";
            case "Mace Tech 2 (Stun Slam)"  -> "Stun Slam";
            default                         -> m.getName();
        };
        JLabel nameLabel = new JLabel(displayName, SwingConstants.CENTER);
        nameLabel.setFont(new Font(FONT_NAME, Font.BOLD, 13));
        nameLabel.setForeground(TEXT_LIGHT);
        return nameLabel;
    }

    private JPanel createSlotRow(ClientModule module, String slotName, String labelText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(200, 32));
        row.setBorder(new EmptyBorder(4, 0, 4, 0));

        JLabel nameLbl = new JLabel(labelText);
        nameLbl.setFont(new Font(FONT_NAME, Font.PLAIN, 13));
        nameLbl.setForeground(TEXT_LIGHT);
        row.add(nameLbl, BorderLayout.WEST);

        JLabel dots = new JLabel(" .........................", SwingConstants.CENTER);
        dots.setFont(new Font(FONT_NAME, Font.PLAIN, 10));
        dots.setForeground(new Color(25, 85, 45));
        row.add(dots, BorderLayout.CENTER);

        row.add(createBindButton(module, slotName), BorderLayout.EAST);
        return row;
    }

    private JButton createBindButton(ClientModule module, String slotName) {
        int currentKey = slotName.equals("mainBind") ? module.getKeybind() : getModuleSlotKey(module, slotName);
        String keyText = formatKeyText(currentKey == -1 ? "..." : NativeKeyEvent.getKeyText(currentKey));

        JButton btn = new JButton(keyText);
        btn.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        btn.setForeground(ACCENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            listeningModule = module;
            listeningSlot   = slotName;
            listeningBtn    = btn;
            btn.setText(">>>");
            btn.setForeground(Color.RED);
        });
        return btn;
    }

    private JComponent createToggleSwitch(ClientModule m) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        container.setOpaque(false);

        JButton switchBtn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(m.isEnabled() ? ACCENT : new Color(10, 30, 15));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2d.setColor(TEXT_LIGHT);
                int knobSize = getHeight() - 6;
                if (m.isEnabled()) g2d.fillOval(getWidth() - knobSize - 3, 3, knobSize, knobSize);
                else               g2d.fillOval(3, 3, knobSize, knobSize);
                g2d.dispose();
            }
        };
        switchBtn.setPreferredSize(new Dimension(46, 22));
        switchBtn.setContentAreaFilled(false);
        switchBtn.setBorderPainted(false);
        switchBtn.setFocusPainted(false);
        switchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        switchBtn.addActionListener(e -> {
            if (isMinecraftFocused()) {
                m.toggle();
                switchBtn.repaint();
                SentaiHex.INSTANCE.configManager.save();
            }
        });

        // [FIX #2] Lắng nghe PropertyChange từ module — khi thread macro gọi setEnabled(false),
        // GUI sẽ tự repaint switch về trạng thái OFF mà không cần can thiệp thủ công
        m.addPropertyChangeListener(evt -> {
            if ("enabled".equals(evt.getPropertyName())) {
                SwingUtilities.invokeLater(switchBtn::repaint);
            }
        });

        container.add(switchBtn);
        return container;
    }

    private JPanel createEmptyCard() {
        JPanel card = new JPanel();
        card.setOpaque(false);
        return card;
    }

    private int getModuleSlotKey(ClientModule module, String slotName) {
        return switch (module) {
            case AnchorMacro m -> switch (slotName) {
                case "slot1" -> m.getSlotAnchor();
                case "slot2" -> m.getSlotGlowstone();
                case "slot3" -> m.getSlotTotem();
                default -> -1;
            };
            case TNTCartMacro m -> switch (slotName) {
                case "slot1" -> m.getSlotRail();
                case "slot2" -> m.getSlotCart();
                case "slot3" -> m.getSlotCrossbow();
                default -> -1;
            };
            case MaceTech1 m -> switch (slotName) {
                case "slot1" -> m.getSlotPearl();
                case "slot2" -> m.getSlotWindCharge();
                default -> -1;
            };
            case MaceTech2 m -> switch (slotName) {
                case "slot1" -> m.getSlotMace();
                case "slot2" -> m.getSlotAxe();
                default -> -1;
            };
            default -> -1;
        };
    }

    private void showGUI() {
        unlockMinecraftMouse();
        SwingUtilities.invokeLater(() -> { setVisible(true); toFront(); requestFocus(); });
    }

    private void hideGUI() {
        SwingUtilities.invokeLater(() -> setVisible(false));
        lockMinecraftMouse();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int code = e.getKeyCode();

        // Chế độ đang chờ gán phím bind
        if (listeningModule != null && listeningSlot != null && listeningBtn != null) {
            // Capture và xóa ngay trên native thread để tránh race condition:
            // nếu để null trong invokeLater (EDT), native thread có thể nhận thêm
            // key event trước khi EDT chạy → phím sai bị capture vào slot bind.
            final ClientModule capturedModule = listeningModule;
            final String       capturedSlot   = listeningSlot;
            final JButton      capturedBtn    = listeningBtn;
            listeningModule = null;
            listeningSlot   = null;
            listeningBtn    = null;

            final String finalKeyText = formatKeyText(NativeKeyEvent.getKeyText(code));
            SwingUtilities.invokeLater(() -> {
                if (capturedSlot.equals("mainBind")) {
                    capturedModule.setKeybind(code);
                } else {
                    updateModuleSlotKey(capturedModule, capturedSlot, code);
                }
                capturedBtn.setText(finalKeyText);
                capturedBtn.setForeground(ACCENT);
                SentaiHex.INSTANCE.configManager.save();
            });
            return;
        }

        // INSERT: mở/đóng GUI
        if (code == NativeKeyEvent.VC_INSERT) {
            if (isVisible()) hideGUI(); else showGUI();
            return;
        }

        // Trigger combo nếu module đang ON — ModuleManager xử lý, GUI không can thiệp
        // (ClickGUI cũng là NativeKeyListener nên phải forward xuống ModuleManager)
        for (ClientModule m : SentaiHex.INSTANCE.moduleManager.getModules()) {
            m.triggerIfEnabled(code);
        }
    }

    private void updateModuleSlotKey(ClientModule module, String slot, int keyCode) {
        switch (slot) {
            case "slot1" -> {
                switch (module) {
                    case AnchorMacro  m -> m.setSlotAnchor(keyCode);
                    case TNTCartMacro m -> m.setSlotRail(keyCode);
                    case MaceTech1    m -> m.setSlotPearl(keyCode);
                    case MaceTech2    m -> m.setSlotMace(keyCode);
                    default -> {}
                }
            }
            case "slot2" -> {
                switch (module) {
                    case AnchorMacro  m -> m.setSlotGlowstone(keyCode);
                    case TNTCartMacro m -> m.setSlotCart(keyCode);
                    case MaceTech1    m -> m.setSlotWindCharge(keyCode);
                    case MaceTech2    m -> m.setSlotAxe(keyCode);
                    default -> {}
                }
            }
            case "slot3" -> {
                switch (module) {
                    case AnchorMacro  m -> m.setSlotTotem(keyCode);
                    case TNTCartMacro m -> m.setSlotCrossbow(keyCode);
                    default -> {}
                }
            }
        }
    }

    // Helper: cắt ngắn text key nếu quá dài để hiển thị gọn trên button
    private String formatKeyText(String raw) {
        return (raw.length() > 5) ? raw.substring(0, 4) + "." : raw;
    }

    @Override public void nativeKeyReleased(NativeKeyEvent e) {}
    @Override public void nativeKeyTyped(NativeKeyEvent e) {}
}