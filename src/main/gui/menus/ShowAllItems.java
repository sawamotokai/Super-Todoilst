package gui.menus;

import gui.Util;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowAllItems extends JFrame implements ActionListener {

    User user;
    JButton back;


    public ShowAllItems(User user) {
        super("Awesome Todo List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        setLayout(new FlowLayout());
        this.user = user;
        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(this);



        String itemReprs = user.showAllItems();
        String[] itemReprsArray = Util.split(itemReprs);

        JPanel items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));

        for (String itemRepr: itemReprsArray) {
            JLabel item = new JLabel(itemRepr);
            items.add(item);
        }

        add(items);
        add(back);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("back")) {
            new TopMenu(user);
        }
        setVisible(false);
    }
}
