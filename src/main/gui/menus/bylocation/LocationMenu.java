package gui.menus.bylocation;

import gui.menus.TopMenu;
import model.locationmap.LocationMap;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class LocationMenu extends JFrame implements ActionListener {

    User user;
    LocationMap locationMap;
    JComboBox locations;
    JPanel location;
    JLabel msg;
    JButton submit;
    JButton back;

    public LocationMenu(User user) {
        super("Awesome Todo List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        setLayout(new FlowLayout());
        this.user = user;
        this.locationMap = user.getLocationMap();
        Set<String> locationSet = locationMap.getLocations();
        for (String loation: locationSet) {
            System.out.println(loation);
        }
        String[] locationArray = new String[locationSet.size()];
        int k = 0;
        for (String s: locationSet) {
            locationArray[k++] = s;
        }
        locations = new JComboBox(locationArray);
        location = new JPanel();

        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(this);

        submit = new JButton("See Todos");
        submit.addActionListener(this);
        submit.setActionCommand("see");
        msg = new JLabel("Select the location you want to see.");

        location.add(msg);
        location.add(locations);

        add(location);
        add(submit);
        add(back);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("see")) {
            new ItemsAtLocation(user, (String) locations.getSelectedItem());
        } else if (e.getActionCommand().equals("back")) {
            new TopMenu(user);
        }
        setVisible(false);
    }
}
