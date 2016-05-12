package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {
	private static MenjacnicaGUI menjacnica;
	private static MenjacnicaInterface menjacnicaInterfejs;
	private static ObrisiKursGUI obrisiKurs;
	private static DodajKursGUI dodajKurs;
	private static IzvrsiZamenuGUI izvrsiZamenu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menjacnica = new MenjacnicaGUI();
					menjacnica.setVisible(true);
					menjacnicaInterfejs = new Menjacnica();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(menjacnica.getContentPane(),
				"Da li ZAISTA zelite da izadjete iz aplikacije?", "Izlazak", JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static void prikaziAboutProzor() {
		JOptionPane.showMessageDialog(menjacnica.getContentPane(), "Autor: Bojan Tomic, Verzija 1.0",
				"O programu Menjacnica", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(menjacnica.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				menjacnicaInterfejs.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnica.getContentPane(), e1.getMessage(), "Greska",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(menjacnica.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				menjacnicaInterfejs.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute();
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnica.getContentPane(), e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void prikaziSveValute() {
		MenjacnicaTableModel model = (MenjacnicaTableModel) (menjacnica.getTable().getModel());
		model.staviSveValuteUModel(menjacnicaInterfejs.vratiKursnuListu());
	}

	public static void prikaziDodajKursGUI() {
		
		dodajKurs = new DodajKursGUI(menjacnica);
		dodajKurs.setLocationRelativeTo(menjacnica.getContentPane());
		dodajKurs.setVisible(true);
	}

	public static void prikaziObrisiKursGUI() {

		if (menjacnica.getTable().getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (menjacnica.getTable().getModel());
			obrisiKurs = new ObrisiKursGUI(menjacnica,
					model.vratiValutu(menjacnica.getTable().getSelectedRow()));
			obrisiKurs.setLocationRelativeTo(menjacnica.getContentPane());
			obrisiKurs.setVisible(true);
		}
	}

	public static void prikaziIzvrsiZamenuGUI() {
		if (menjacnica.getTable().getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (menjacnica.getTable().getModel());
			izvrsiZamenu = new IzvrsiZamenuGUI(menjacnica,
					model.vratiValutu(menjacnica.getTable().getSelectedRow()));
			izvrsiZamenu.setLocationRelativeTo(menjacnica.getContentPane());
			izvrsiZamenu.setVisible(true);
		}
	}
	
	public static void unesiKurs() {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(dodajKurs.getTextFieldNaziv().getText());
			valuta.setSkraceniNaziv(dodajKurs.getTextFieldSkraceniNaziv().getText());
			valuta.setSifra((Integer) (dodajKurs.getSpinnerSifra().getValue()));
			valuta.setProdajni(Double.parseDouble(dodajKurs.getTextFieldProdajniKurs().getText()));
			valuta.setKupovni(Double.parseDouble(dodajKurs.getTextFieldKupovniKurs().getText()));
			valuta.setSrednji(Double.parseDouble(dodajKurs.getTextFieldSrednjiKurs().getText()));

			// Dodavanje valute u kursnu listu
			menjacnicaInterfejs.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			prikaziSveValute();

			// Zatvaranje DodajValutuGUI prozora
			dodajKurs.dispose();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(dodajKurs.getContentPane(), e1.getMessage(), "Greska",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void obrisiValutu(Valuta valuta) {
		try {
			menjacnicaInterfejs.obrisiValutu(valuta);

			prikaziSveValute();
			obrisiKurs.dispose();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(obrisiKurs.getContentPane(), e1.getMessage(), "Greska",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void izvrsiZamenu(Valuta valuta){
		try{
			double konacniIznos = 
					menjacnicaInterfejs.izvrsiTransakciju(valuta,
							izvrsiZamenu.getRdbtnProdaja().isSelected(), 
							Double.parseDouble(izvrsiZamenu.getTextFieldIznos().getText()));

			izvrsiZamenu.getTextFieldKonacniIznos().setText(""+konacniIznos);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(izvrsiZamenu.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
}
