package com.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Helper.DBConnection;
import Model.Clinic;
import Model.Hasta;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class HastaGUI extends JFrame {

    private JPanel w_pane;
    private static Hasta hasta = new Hasta();
    private Clinic clinic = new Clinic();
    private DefaultTableModel doktorModel = null;
    private Object[] doktorData = null;
    private JTable table_doktor;
    private JComboBox select_clinic;
    private JComboBox comboBox;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HastaGUI frame = new HastaGUI(hasta);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public HastaGUI(Hasta hasta) {
        setTitle("Hasta Yönetim Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 507, 403);
        w_pane = new JPanel();
        w_pane.setBackground(Color.WHITE);
        w_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(w_pane);
        w_pane.setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(10, 88, 473, 268);
        w_pane.add(tabbedPane);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        tabbedPane.addTab("Randevu Sistemi", null, panel, null);
        panel.setLayout(null);

        doktorModel = new DefaultTableModel();
        Object[] coldoktorName = new Object[1];
        coldoktorName[0] = "Doktor Adı";

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 70, 227, 161);
        panel.add(scrollPane);

        doktorModel.setColumnIdentifiers(coldoktorName);
        doktorData = new Object[1];

        table_doktor = new JTable(doktorModel);
        scrollPane.setViewportView(table_doktor);

        select_clinic = new JComboBox();
        select_clinic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rows = table_doktor.getRowCount();
                for (int i = 0; i < rows; i++) {
                    doktorModel.removeRow(0);
                }
                doktorModel.getDataVector().removeAllElements();

                DBConnection con = new DBConnection();
                Connection c = con.connDb();

                String clinic_name = select_clinic.getSelectedItem().toString();
                String sql_cumle = "SELECT * FROM clinic WHERE name='" + clinic_name + "'";

                Statement st;
                try {
                    st = c.createStatement();
                    ResultSet rs = st.executeQuery(sql_cumle);
                    int clinic_id = 0;
                    while (rs.next()) {
                        clinic_id = rs.getInt("id");
                    }

                    String sql_cumle2 = "SELECT * FROM eslesme WHERE clinic_id='" + clinic_id + "'";
                    ResultSet rs2 = st.executeQuery(sql_cumle2);
                    ArrayList<Integer> alist = new ArrayList<Integer>();

                    while (rs2.next()) {
                        alist.add(rs2.getInt("doctor_id"));
                    }

                    for (int i = 0; i < alist.size(); i++) {
                        String sql_cumle3 = "SELECT * FROM user WHERE id='" + alist.get(i) + "'";
                        ResultSet rs3 = st.executeQuery(sql_cumle3);
                        String doktoradi = "";
                        while (rs3.next()) {
                            doktoradi = rs3.getString("name");
                            doktorData[0] = doktoradi;
                            doktorModel.addRow(doktorData);
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        select_clinic.setToolTipText("--Poliklinik--");
        try {
            ArrayList<String> veri = combolist();

            for (int i = 0; i < veri.size(); i++) {
                select_clinic.addItem(veri.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        select_clinic.setBounds(260, 31, 157, 21);
        panel.add(select_clinic);

        JLabel lblNewLabel_1 = new JLabel("Poliklinik Adı");
        lblNewLabel_1.setBackground(Color.WHITE);
        lblNewLabel_1.setBounds(260, 14, 81, 13);
        panel.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Doktor Listesi");
        lblNewLabel_2.setBackground(new Color(255, 255, 255));
        lblNewLabel_2.setBounds(23, 14, 81, 13);
        panel.add(lblNewLabel_2);

        comboBox = new JComboBox();
        comboBox.setBounds(20, 31, 157, 21);
        panel.add(comboBox);
        readDoctorNamesFromFile("doctornames.txt", comboBox);

        JButton btnRandevuAl = new JButton("Randevu Al");
        btnRandevuAl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                randevuEkle();
            }
        });
        btnRandevuAl.setBounds(260, 200, 157, 21);
        panel.add(btnRandevuAl);

        JLabel lblNewLabel = new JLabel("Hoşgeldiniz Sayın " + hasta.getName());
        lblNewLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 13));
        lblNewLabel.setToolTipText("");
        lblNewLabel.setBounds(10, 37, 235, 18);
        w_pane.add(lblNewLabel);

        JButton btnNewButton = new JButton("Çıkış Yap");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginGUI lGUI = new LoginGUI();
                lGUI.setVisible(true);
                dispose();
            }
        });
        btnNewButton.setBackground(Color.WHITE);
        btnNewButton.setBounds(380, 37, 103, 28);
        w_pane.add(btnNewButton);

        LoginGUI lGUI = new LoginGUI();
        lGUI.setVisible(true);
        dispose();
    }

    private void randevuEkle() {
        String secilenDoktor = comboBox.getSelectedItem().toString();
        String secilenPoliklinik = select_clinic.getSelectedItem().toString();

        DBConnection con = new DBConnection();
        Connection c = con.connDb();

        int doktorId = getDoktorId(secilenDoktor);
        int poliklinikId = getPoliklinikId(secilenPoliklinik);

        try {
            String sql = "INSERT INTO randevu (hasta_id, doktor_id, poliklinik_id) VALUES (?, ?, ?)";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, hasta.getId());
            pstmt.setInt(2, doktorId);
            pstmt.setInt(3, poliklinikId);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Randevu başarıyla alındı!");
            System.out.println("Randevu alındı!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<String> combolist() throws SQLException {
        DBConnection con = new DBConnection();
        Connection c = con.connDb();

        String sql_cumlesi = "SELECT * FROM clinic";

        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(sql_cumlesi);

        ArrayList<String> degisken = new ArrayList<String>();

        while (rs.next()) {
            String clinic_name = rs.getString("name");
            degisken.add(clinic_name);
        }

        return degisken;
    }

    private void readDoctorNamesFromFile(String fileName, JComboBox comboBox) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                comboBox.addItem(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getDoktorId(String doktorAdi) {
        int doktorId = 0;

        DBConnection con = new DBConnection();
        Connection c = con.connDb();

        try {
            String sql_cumle = "SELECT * FROM user WHERE name=?";
            PreparedStatement pstmt = c.prepareStatement(sql_cumle);
            pstmt.setString(1, doktorAdi);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                doktorId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doktorId;
    }

    private int getPoliklinikId(String poliklinikAdi) {
        int poliklinikId = 0;

        DBConnection con = new DBConnection();
        Connection c = con.connDb();

        try {
            String sql_cumle = "SELECT * FROM clinic WHERE name=?";
            PreparedStatement pstmt = c.prepareStatement(sql_cumle);
            pstmt.setString(1, poliklinikAdi);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                poliklinikId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return poliklinikId;
    }
}
