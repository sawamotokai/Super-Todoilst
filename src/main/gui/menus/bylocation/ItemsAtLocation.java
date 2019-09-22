package gui.menus.bylocation;

import model.item.Item;
import model.locationmap.LocationMap;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ItemsAtLocation extends JFrame implements ActionListener {

    User user;
    ArrayList<Item> items;
    LocationMap locationMap;
    JButton back;

    public ItemsAtLocation(User user, String location) {
        super("Awesome Todo List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));


        setLayout(new FlowLayout());
        this.user = user;
        this.locationMap = user.getLocationMap();
        items = locationMap.getItemsAtLocation(location);

        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(this);

        JPanel items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));

        for (Item i : this.items) {
            JLabel item = new JLabel(i.getRepr());
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
            new LocationMenu(user);
        }
        setVisible(false);
    }
}
