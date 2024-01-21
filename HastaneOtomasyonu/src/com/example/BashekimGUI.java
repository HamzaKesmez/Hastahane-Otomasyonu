package com.example;

import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import Model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JTextPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.JTable;
import Helper.*;
import Helper.DBConnection;
import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.JComboBox;

public class BashekimGUI extends JFrame {
	
	static Bashekim bashekim = new Bashekim();

	private JPanel w_pane;
	private JTable table_doctor;
	private JTable table_clinic;
	private JTable table_eslesme;
	private DefaultTableModel doctorModel = null;
	private DefaultTableModel eslesmeModel = new DefaultTableModel();
	private DefaultTableModel clinicModel = null;
	
	

	private Object[] doctorData = null;
	private Object[] clinicData = null;
	private Object[] eslesmeData = null;
	private JTextField fld_dName;
	private JTextField fld_dTcno;
	private JPasswordField fld_dPass;
	private JTextField fld_clinicname;
	private JTextField fld_doctorID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BashekimGUI frame = new BashekimGUI(bashekim);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void writeDoctorNamesToFile(String fileName, List<Doctor> doctorList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (Doctor doctor : doctorList) {
                writer.write(doctor.getDName());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public BashekimGUI(Bashekim bashekim) throws SQLException {
		setTitle("Hastane Yönetim Sistemi - Başhekim Paneli");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		w_pane = new JPanel();
		w_pane.setBackground(Color.WHITE);
		w_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(w_pane);
		w_pane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Hoşgeldiniz Sayın " + bashekim.getName());
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 16));
		lblNewLabel.setBounds(10, 52, 410, 22);
		w_pane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("\u00C7\u0131k\u0131\u015F Yap");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginGUI().setVisible(true);
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 10));
		btnNewButton.setBounds(594, 80, 89, 31);
		w_pane.add(btnNewButton);
		
		JTabbedPane w_tab = new JTabbedPane(JTabbedPane.TOP);
		w_tab.setBounds(10, 121, 716, 332);
		w_pane.add(w_tab);
		
		JTabbedPane tabbedPane2 = new JTabbedPane(JTabbedPane.TOP);
		w_tab.setBounds(10, 121, 716, 332);
		w_pane.add(w_tab);
		
		doctorModel = new DefaultTableModel();
		Object[] colDoctorName = new Object[4];
		colDoctorName[0] = "ID";
		colDoctorName[1] = "Ad Soyad";
		colDoctorName[2] = "TC NO";
		colDoctorName[3] = "Şifre";
		
		doctorModel.setColumnIdentifiers(colDoctorName);
		doctorData = new Object[4];
		
	/*	ArrayList<User> doktorlar = doktorlarigetir();
		*/
		
		// ...
		for (User doctor : bashekim.getDoctorList()) {
		    doctorData[0] = doctor.getId();
		    doctorData[1] = doctor.getName();
		    doctorData[2] = doctor.getTcno();
		    doctorData[3] = doctor.getPassword();
		    doctorModel.addRow(doctorData);
		}
		// ...

		
		JPanel w_doctor = new JPanel();
		w_doctor.setBackground(Color.WHITE);
		w_tab.addTab("Doktor Yönetimi", null, w_doctor, null);
		w_tab.setEnabledAt(0, true);
		w_doctor.setLayout(null);
		
		JScrollPane w_scrollDoctor = new JScrollPane();
		w_scrollDoctor.setBounds(10, 10, 535, 273);
		w_doctor.add(w_scrollDoctor);
	
		
		table_doctor = new JTable(doctorModel);
		w_scrollDoctor.setViewportView(table_doctor);
		table_doctor.getSelectionModel().addListSelectionListener(new ListSelectionListener() {	
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try{
					fld_doctorID.setText(table_doctor.getValueAt(table_doctor.getSelectedRow(),0).toString());
				}catch(Exception ex) {
					
				}
				
			}
		});
		
		
		JLabel label = new JLabel("Ad Soyad");
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 14));
		label.setBounds(555, 10, 62, 20);
		w_doctor.add(label);
		
		fld_dName = new JTextField();
		fld_dName.setColumns(10);
		fld_dName.setBounds(555, 40, 144, 20);
		w_doctor.add(fld_dName);
		
		JLabel label_1 = new JLabel("Tc no");
		label_1.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 14));
		label_1.setBounds(555, 69, 45, 13);
		w_doctor.add(label_1);
		
		fld_dTcno = new JTextField();
		fld_dTcno.setColumns(10);
		fld_dTcno.setBounds(555, 92, 144, 20);
		w_doctor.add(fld_dTcno);
		
		JLabel label_2 = new JLabel("Şifre");
		label_2.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 14));
		label_2.setBounds(555, 120, 45, 13);
		w_doctor.add(label_2);
		
		fld_dPass = new JPasswordField();
		fld_dPass.setBounds(555, 138, 144, 20);
		w_doctor.add(fld_dPass);
		
		List<Doctor> doctorList = new ArrayList<>();
		
		
		JButton btn_addDoctor = new JButton("Ekle");
		btn_addDoctor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fld_dName.getText().length() == 0 || fld_dPass.getText().length() == 0 || fld_dTcno.getText().length() == 0) {
					Helper.showMsg("fill");
				}else {
					try {
						boolean control = bashekim.addDoctor(fld_dTcno.getText(),fld_dPass.getText(),fld_dName.getText());
						if(control) {
							Doctor newDoctor = new Doctor(fld_dName.getText());
		                    doctorList.add(newDoctor);
		                    writeDoctorNamesToFile("doctornames.txt", doctorList);
							Helper.showMsg("success");
							fld_dName.setText(null);
							fld_dTcno.setText(null);
							fld_dPass.setText(null);
							updateDoctorModel();
							
						}
					} catch (SQLException e1) {
						
						e1.printStackTrace();
					}
				}
				
			}
		});
		btn_addDoctor.setForeground(Color.BLACK);
		btn_addDoctor.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 14));
		btn_addDoctor.setBackground(Color.GRAY);
		btn_addDoctor.setBounds(555, 166, 144, 31);
		w_doctor.add(btn_addDoctor);
		
		JLabel label_3 = new JLabel("Kullanıcı ID");
		label_3.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 14));
		label_3.setBounds(555, 209, 74, 13);
		w_doctor.add(label_3);
		
		JButton btn_delDoctor = new JButton("Sil");
		btn_delDoctor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fld_doctorID.getText().length() == 0) {
					Helper.showMsg("Lütfen geçerli doktor seçiniz");
				}else {
					if(Helper.confirm("sure")) {
						int selectID = Integer.parseInt(fld_doctorID.getText());
						try {
							boolean control = bashekim.deleteDoctor(selectID);
							if(control) {
								Helper.showMsg("success");
								fld_doctorID.setText(null);
								updateDoctorModel();
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
						
				}
			}
		});
		btn_delDoctor.setForeground(Color.BLACK);
		btn_delDoctor.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 14));
		btn_delDoctor.setBackground(Color.GRAY);
		btn_delDoctor.setBounds(555, 252, 144, 31);
		w_doctor.add(btn_delDoctor);
		
		fld_doctorID = new JTextField();
		fld_doctorID.setEnabled(false);
		fld_doctorID.setColumns(10);
		fld_doctorID.setBounds(555, 233, 144, 20);
		w_doctor.add(fld_doctorID);
		
		JPanel w_clinic = new JPanel();
		w_clinic.setBackground(Color.WHITE);
		w_tab.addTab("Poliklinikler", null, w_clinic, null);
		w_clinic.setLayout(null);
		
		JScrollPane w_scrollClinic = new JScrollPane();
		w_scrollClinic.setBounds(10, 10, 233, 285);
		w_clinic.add(w_scrollClinic);
		
		// Clinic Model
		clinicModel = new DefaultTableModel();
		Object[] colClinicName = new Object[2];
		colClinicName[0] = "ID";
		colClinicName[1] = "Klinik İsmi";
		
		clinicModel.setColumnIdentifiers(colClinicName);
		clinicData = new Object[2];
		clinicData = new Object[2];
		
		ArrayList<Clinic> clinicler = clinicgetir();
		
		
		for(int i=0; i<clinicler.size();i++) {
			clinicData[0] = clinicler.get(i).getId();
			clinicData[1] = clinicler.get(i).getName();
	
			clinicModel.addRow(clinicData);
		}
		
		
		table_clinic = new JTable(clinicModel);
		w_scrollClinic.setViewportView(table_clinic);
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(468, 10, 233, 285);
		w_clinic.add(scrollPane_1);
		
		DefaultTableModel eslesme = null;
		
		DBConnection con = new DBConnection();
		Connection c = con.connDb();
		
		String sql_cumlesi = "SELECT * FROM eslesme";
		
		try {
			Statement st = c.createStatement();
			ResultSet rs = st.executeQuery(sql_cumlesi);
			
			clinicModel = new DefaultTableModel();
			Object[] coleslesmeName = new Object[2];
			coleslesmeName[0] = "Doktor Adı";
			coleslesmeName[1] = "Klinik Adı";
			
			eslesmeModel.setColumnIdentifiers(coleslesmeName);
			eslesmeData = new Object[2];
			eslesmeData = new Object[2];
			
			while(rs.next()) {
				int doctor_id = rs.getInt("doctor_id");
				int clinic_id = rs.getInt("clinic_id");
				String sql_cumlesi1 = "SELECT * FROM user WHERE id="+doctor_id;
				String sql_cumlesi2 = "SELECT * FROM clinic WHERE id="+clinic_id;
				
				Statement st1 = c.createStatement();
				ResultSet rs1 = st1.executeQuery(sql_cumlesi1);
				
				Statement st2 = c.createStatement();
				ResultSet rs2 = st2.executeQuery(sql_cumlesi2);
				String doctor_adi = "";
				String clinic_adi = "";
				
				while(rs1.next()) {
					 doctor_adi = rs1.getString("name");
					
				}
				while(rs2.next()) {
					clinic_adi = rs2.getString("name");
					
				}
				
				eslesmeData[0] = doctor_adi;
				eslesmeData[1] = clinic_adi;
		
				eslesmeModel.addRow(eslesmeData);
				
				eslesme = eslesmeModel;
				
				
				
			
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}

		
		table_eslesme = new JTable(eslesme);
		scrollPane_1.setViewportView(table_eslesme);

		JLabel lblPoliklinikAd = new JLabel("Poliklinik Adı:");
		lblPoliklinikAd.setBounds(253, 10, 85, 13);
		w_clinic.add(lblPoliklinikAd);
		
		fld_clinicname = new JTextField();
		fld_clinicname.setBounds(253, 26, 205, 19);
		w_clinic.add(fld_clinicname);
		fld_clinicname.setColumns(10);
		
		JButton btn_addClinic = new JButton("Ekle");
		btn_addClinic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DBConnection con = new DBConnection();
				Connection connection = con.connDb();
				String clinic_adi = fld_clinicname.getText();
				
				String sql_cumlesi = "INSERT INTO clinic (name) VALUES('"+clinic_adi+"')";
				
				Statement statement;
				try {
					statement = connection.createStatement();
					statement.executeUpdate(sql_cumlesi);

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				BashekimGUI bGUI = null;
				try {
					bGUI = new BashekimGUI(bashekim);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				bGUI.setVisible(true);
				dispose();
				

				
				
			}
		});
		btn_addClinic.setBounds(253, 48, 205, 33);
		w_clinic.add(btn_addClinic);
		
		JComboBox<String> select_doctor = new JComboBox<String>();
		ArrayList<User> veri = doktorlarigetir();

		for (int i = 0; i < veri.size(); i++) {
		    // Doktor adı ve id'sini birleştirip ekliyoruz
		    String item = veri.get(i).getName() + " (ID: " + veri.get(i).getId() + ")";
		    select_doctor.addItem(item);
		}

		select_doctor.setBounds(253, 245, 205, 19);
		w_clinic.add(select_doctor);
		
		JButton btn_addWorker = new JButton("Poliklniğe Ekle");
		btn_addWorker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DBConnection cn = new DBConnection();
				Connection c = cn.connDb();
				
				Object doctor = select_doctor.getSelectedItem();
				String doctor_name = doctor.toString();
				int selected = table_clinic.getSelectedRow();
				String value = table_clinic.getModel().getValueAt(selected, 0).toString();
				int clinic_id = Integer.valueOf(value);
				String sql_cumle = "SELECT * FROM user WHERE name='"+doctor_name+"' AND type='doktor'"; 
				
				Statement st;
				try {
					st = c.createStatement();
					ResultSet rs = st.executeQuery(sql_cumle);
					int doctor_id = 0;
					while(rs.next()){
						doctor_id= rs.getInt("id");
					}
					
					String sql_cumle1 = "SELECT * FROM eslesme WHERE doctor_id='"+doctor_id+"' AND clinic_id='"+clinic_id+"'";
					String sql_cumle2 = "INSERT INTO eslesme (doctor_id, clinic_id) VALUES ('"+doctor_id+"','"+clinic_id+"')";
					
					Statement statement;
					
					
					try {
						statement = c.createStatement();
						
						ResultSet rs2 = statement.executeQuery(sql_cumle1);
						boolean varmi = false;
						while(rs2.next()) {
							varmi = true;
						}
						
						
						if(!varmi) {
							statement.executeUpdate(sql_cumle2);
											

					}} catch (SQLException e1) {
						// TODO Auto-generated catch block
						System.out.println("Hata 2");
					}

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("Hata");
					
				}
				
				
				
				BashekimGUI bGUI = null;
				try {
					bGUI = new BashekimGUI(bashekim);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				bGUI.setVisible(true);
				dispose();
				
				
				
			}
		});
		btn_addWorker.setBounds(253, 267, 205, 28);
		w_clinic.add(btn_addWorker);
		
		
		
	}
	


	public ArrayList<User> doktorlarigetir() {
	
		DBConnection c1 =  new DBConnection();
		Connection conn = c1.connDb();
		ArrayList<User> arraylist = new ArrayList<>();
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM user WHERE type='doktor'");
			
			while(rs.next()) {
				User doktor = new User(rs.getInt("id"),rs.getString("tcno"),rs.getString("name"),rs.getString("password"),rs.getString("type"));
				arraylist.add(doktor);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arraylist;
		
	}
	public JLabel getLblNewLabel_4() {
		return getLblNewLabel_4();
	}
	
	public ArrayList<Clinic> clinicgetir() {
		
		DBConnection c1 =  new DBConnection();
		Connection conn = c1.connDb();
		ArrayList<Clinic> arraylist = new ArrayList<>();
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM clinic");
			
			while(rs.next()) {
				Clinic clinic = new Clinic(rs.getInt("id"),rs.getString("name"));
				arraylist.add(clinic);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arraylist;
		
	}
	public void updateDoctorModel() throws SQLException {
		DefaultTableModel clearModel = (DefaultTableModel) table_doctor.getModel();
		clearModel.setRowCount(0);
		// ...
		for (User doctor : bashekim.getDoctorList()) {
		    doctorData[0] = doctor.getId();
		    doctorData[1] = doctor.getName();
		    doctorData[2] = doctor.getTcno();
		    doctorData[3] = doctor.getPassword();
		    doctorModel.addRow(doctorData);
		}
		// ...

	}
}
