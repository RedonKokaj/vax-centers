package centrivaccinali.UI;

import centrivaccinali.CentriVaccinali;
import common.ServerInterface;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Objects;

public class UIConnectToServer extends JFrame implements ActionListener {
    JTextField tfHost = new JTextField();
    JTextField tfPort = new JTextField();
    JButton btnConnect = new JButton("CONNECT");

    public UIConnectToServer(){
        ImageIcon imgHome = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/clienthome.jpg")));
        ImageIcon imgHostph = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/hosticon.png")));
        ImageIcon imgPortph = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/porticon.png")));
        ImageIcon imgLogo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/logo100px.png")));
        Border border = new LineBorder(new Color(251, 186, 0), 2, true);
        Font fontSmall = new Font("Light", Font.PLAIN, 16);

        JLabel labelLogo = new JLabel();
        labelLogo.setIcon(imgLogo);
        labelLogo.setBounds(588, 150, 100, 100);

        JLabel labelTitolo = new JLabel("Benvenuto in Vax Centers");
        labelTitolo.setFont(new Font("Montserrat", Font.BOLD, 24));
        labelTitolo.setForeground(new Color(0, 0, 0));
        labelTitolo.setBounds(495, 265, 300, 30);

        JLabel labelDescrizione = new JLabel("Per accedere al server si prega di compilare tutti i campi");
        labelDescrizione.setFont(new Font("Montserrat", Font.BOLD, 18));
        labelDescrizione.setForeground(new Color(0, 0, 0));
        labelDescrizione.setBounds(400, 310, 500, 30);

        JLabel labelHost = new JLabel("Host (default: localhost)");
        labelHost.setFont(fontSmall);
        labelHost.setForeground(new Color(0, 0, 0));
        labelHost.setBounds(494, 354, 250, 30);
        tfHost.setPreferredSize(new Dimension(530, 30));
        tfHost.setBounds(473, 380, 335, 30);

        JLabel labelHostph = new JLabel();
        labelHostph.setIcon(imgHostph);
        labelHostph.setBounds(473, 361, 16, 16);

        JLabel labelPort = new JLabel("Porta (default: 1099)");
        labelPort.setFont(fontSmall);
        labelPort.setForeground(new Color(0, 0, 0));
        labelPort.setBounds(494, 423, 250, 30);
        tfPort.setPreferredSize(new Dimension(530, 30));
        tfPort.setBounds(473, 450, 335, 30);

        JLabel labelPortph = new JLabel();
        labelPortph.setIcon(imgPortph);
        labelPortph.setBounds(473, 432, 16, 16);

        btnConnect.setPreferredSize(new Dimension(150, 40));
        btnConnect.setFocusable(false);
        btnConnect.setFont(new Font("Montserrat", Font.BOLD, 15));
        btnConnect.setBackground(new Color(232, 47, 125));
        btnConnect.setForeground(Color.WHITE);
        btnConnect.setBorder(border);
        btnConnect.addActionListener(this);
        btnConnect.setBounds(550, 508, 170, 40);
        btnConnect.setOpaque(true);

        JLabel labelSfondo = new JLabel();
        labelSfondo.setIcon(imgHome);
        labelSfondo.setBounds(0, 0, 1280, 720);
        labelSfondo.add(labelLogo);
        labelSfondo.add(labelTitolo);
        labelSfondo.add(labelDescrizione);
        labelSfondo.add(labelHost);
        labelSfondo.add(tfHost);
        labelSfondo.add(labelHostph);
        labelSfondo.add(labelPort);
        labelSfondo.add(tfPort);
        labelSfondo.add(labelPortph);
        labelSfondo.add(btnConnect);

        this.setTitle("Vax Centers");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setLayout(null);
        this.setResizable(false);

        this.add(labelSfondo);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String port = tfPort.getText();
        String host = tfHost.getText();

        if(port.equals("")) port = "1099";
        if(host.equals("")) host = "localhost";

        try {
            CentriVaccinali.registry = LocateRegistry.getRegistry(host, Integer.parseInt(port));
            CentriVaccinali.server = (ServerInterface) CentriVaccinali.registry.lookup("ServerCV");

            this.dispose();
            new UIStartMenu();
        } catch (RemoteException | NotBoundException exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(this, "Impossibile connettersi al server remoto");
        }

    }
}
