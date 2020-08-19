package nsu.fit.g16203.grigorovich.controller;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class MainFrame extends JFrame {
//    public JButton globalSelectButton = null;
//    public JButton globalAbsorptionButton = null;
//    public JButton globalEmissionButton = null;
    private static final long serialVersionUID = 1L;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JPanel statusBar;
    public JLabel statusLabel;


    public MainFrame() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
//        filterMaker = new FilterMaker();
//        settingsMaker = new SettingsMaker();
//        try {
//            filterMaker.init();
//            settingsMaker.init();
//        } catch (Exception e) {
//            System.out.println("Error: can't create factory!");
//            return;
//        }
        toolBar = new JToolBar("Main toolbar");
        toolBar.setRollover(true);
        add(toolBar, BorderLayout.PAGE_START);

        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusBar.setPreferredSize(new Dimension(this.getWidth(), 16));
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Ready!");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

    }

    public MainFrame(int x, int y, String title) {
        this();
        setSize(x, y);
        setLocationByPlatform(true);
        setTitle(title);
    }


    public JMenuItem createMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod) throws SecurityException, NoSuchMethodException {
        JMenuItem item = new JMenuItem(title);
        if (actionMethod.equals("onToolBar") || actionMethod.equals("onStatusBar")) {
            item = new JCheckBoxMenuItem(title);
            item.setSelected(true);
        }
        if (title.equals("Select") || title.equals("Enable absorption") || title.equals("Enable emission")) {
            item = new JCheckBoxMenuItem(title);
            item.setSelected(false);
        }
        item.setMnemonic(mnemonic);
        item.setToolTipText(tooltip);
        if (icon != null)
            item.setIcon(new ImageIcon(getClass().getResource("resources/" + icon), title));
        Method method;
        try {
            method = getClass().getMethod(actionMethod);
        } catch (NoSuchMethodException ex) {
            try {
                Class[] methodArg = new Class[1];
                methodArg[0] = String.class;
                method = getClass().getMethod(actionMethod, methodArg);
            } catch (NoSuchMethodException ex2) {
                System.out.println("NoSuchMethod");
                return null;
            }
        }
        Method finalMethod = method;
        int test = finalMethod.getParameterCount();
        item.addActionListener(evt -> {
            try {
                if (finalMethod.getParameterCount() == 1)
                    finalMethod.invoke(MainFrame.this, title);
                else
                    finalMethod.invoke(MainFrame.this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return item;
    }

    public void addMenuToolbarGroup(String title1, String tooltip1, int mnemonic1, String icon1, String actionMethod1) throws NoSuchMethodException {
        MenuElement selectElement = getParentMenuElement(title1);
        JMenuItem selectItem = createMenuItem(getMenuPathName(title1), tooltip1, mnemonic1, icon1, actionMethod1);
        ((JPopupMenu) selectElement).add(selectItem);
        JCheckBoxMenuItem selectMenuButton = (JCheckBoxMenuItem) getMenuElement(title1);
        selectMenuButton.setSelected(false);
        JButton selectButton = new JButton(selectMenuButton.getIcon());

        for (ActionListener listener : selectMenuButton.getActionListeners())
            selectButton.addActionListener(listener);

        selectButton.setToolTipText(selectMenuButton.getToolTipText());
        selectButton.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                statusLabel.setText(selectMenuButton.getToolTipText());
            }
        });

        selectMenuButton.addActionListener(e -> {
            selectMenuButton.setSelected(selectMenuButton.isSelected());
            selectButton.setSelected(selectMenuButton.isSelected());
        });

        selectButton.addActionListener(e -> {
            selectButton.setSelected(!selectButton.isSelected());
            selectMenuButton.setSelected(selectButton.isSelected());
        });
//        switch (title1) {
//            case "Edit/Select":
//                globalSelectButton = selectButton;
//                break;
//            case "Edit/Enable absorption":
//                globalAbsorptionButton = selectButton;
//                break;
//            case "Edit/Enable emission":
//                globalEmissionButton = selectButton;
//                break;
//            default:
//                break;
//        }
        toolBar.add(selectButton);
    }

    public JMenuItem createMenuItem(String title, String tooltip, int mnemonic, String actionMethod) throws SecurityException, NoSuchMethodException {
        return createMenuItem(title, tooltip, mnemonic, null, actionMethod);
    }

    public JMenu createSubMenu(String title, int mnemonic) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        return menu;
    }

    public void addSubMenu(String title, int mnemonic) {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenu subMenu = createSubMenu(getMenuPathName(title), mnemonic);
        if (element instanceof JMenuBar)
            ((JMenuBar) element).add(subMenu);
        else if (element instanceof JMenu)
            ((JMenu) element).add(subMenu);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(subMenu);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    public void addMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod) throws SecurityException, NoSuchMethodException {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenuItem item = createMenuItem(getMenuPathName(title), tooltip, mnemonic, icon, actionMethod);
        if (element instanceof JMenu)
            ((JMenu) element).add(item);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    public void addMenuItem(String title, String tooltip, int mnemonic, String actionMethod) throws SecurityException, NoSuchMethodException {
        addMenuItem(title, tooltip, mnemonic, null, actionMethod);
    }

    public void addMenuSeparator(String title) {
        MenuElement element = getMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        if (element instanceof JMenu)
            ((JMenu) element).addSeparator();
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).addSeparator();
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    private String getMenuPathName(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        if (pos > 0)
            return menuPath.substring(pos + 1);
        else
            return menuPath;
    }

    private MenuElement getParentMenuElement(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        if (pos > 0)
            return getMenuElement(menuPath.substring(0, pos));
        else
            return menuBar;
    }

    public MenuElement getMenuElement(String menuPath) {
        MenuElement element = menuBar;
        for (String pathElement : menuPath.split("/")) {
            MenuElement newElement = null;
            for (MenuElement subElement : element.getSubElements()) {
                if ((subElement instanceof JMenu && ((JMenu) subElement).getText().equals(pathElement))
                        || (subElement instanceof JMenuItem && ((JMenuItem) subElement).getText().equals(pathElement))) {
                    if (subElement.getSubElements().length == 1 && subElement.getSubElements()[0] instanceof JPopupMenu)
                        newElement = subElement.getSubElements()[0];
                    else
                        newElement = subElement;
                    break;
                }
            }
            if (newElement == null) return null;
            element = newElement;
        }
        return element;
    }

    public JButton createToolBarButton(JMenuItem item) {
        JButton button = new JButton(item.getIcon());
        for (ActionListener listener : item.getActionListeners())
            button.addActionListener(listener);
        button.setToolTipText(item.getToolTipText());
        if (item.getText().equals("Impact")) {
            button.addActionListener(e -> {
                boolean buttonState = button.isSelected();
                button.setSelected(!buttonState);
                item.setSelected(!buttonState);
            });
            item.addActionListener(e -> {
                boolean itemState = item.isSelected();
                button.setSelected(itemState);
                item.setSelected(itemState);
            });
        }

        button.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                statusLabel.setText(item.getToolTipText());
            }
        });
        return button;
    }

    public JButton createToolBarButton(String menuPath) {
        JMenuItem item = (JMenuItem) getMenuElement(menuPath);
        if (item == null)
            throw new InvalidParameterException("Menu path not found: " + menuPath);
        return createToolBarButton(item);
    }

    public void addToolBarButton(String menuPath) {
        toolBar.add(createToolBarButton(menuPath));
    }

    public void addToolBarSeparator() {
        toolBar.addSeparator();
    }

    public void setToolBarState(boolean state) {
        toolBar.setVisible(state);
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public void setStatusBarState(boolean state) {
        statusBar.setVisible(state);
    }

    public JPanel getStatusBar() {
        return statusBar;
    }
}
