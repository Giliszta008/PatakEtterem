package hu.petrik.pataketterem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HelloController {

    @FXML
    private Label labelHetiOsszBevetel;
    @FXML
    private  Button buttonLegDragabbDesszertek;
    @FXML
    private  Button buttonHetiOsszBevetel;
    @FXML
    private  Button buttonEtelekSzazalekosan;
    @FXML
    private  Button buttonFajlbaIr;
    @FXML
    private Button buttonFajlBeolvas;
    @FXML
    private Button buttonHibasAdatTorlese;
    @FXML
    private TableView<Etel> tableViewEtel;

    @FXML
    public void initialize() {
        tableViewEtel.setPlaceholder(new Label("Nincs megjeleníthető adat!"));
        //megjelenit(FajlKezelo.fajlBeolvas(new File("patak.csv")));
    }

    @FXML
    private void onButtonKilepClick(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    private void onButtonFajlBeolvasClick(ActionEvent actionEvent) {
        tableViewEtel.getItems().clear();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Szöveges állomány megnyitása");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV fájlok (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File fajl = fileChooser.showOpenDialog(null);
        megjelenit(FajlKezelo.fajlBeolvas(fajl));

        buttonHibasAdatTorlese.setDisable(false);
    }

    private void megjelenit(List<Etel> etelLista){
        TableColumn<Etel, String> oszlopAzonosito = new TableColumn<>("Azonosító");
        oszlopAzonosito.setCellValueFactory(new PropertyValueFactory<>("azonosito"));
        oszlopAzonosito.setSortType(TableColumn.SortType.ASCENDING);
        oszlopAzonosito.setSortable(false);
        oszlopAzonosito.setStyle("-fx-alignment: center;");

        TableColumn<Etel, String> oszlopNev = new TableColumn<>("Név");
        oszlopNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        oszlopNev.setStyle("-fx-alignment: center;");
        oszlopNev.setPrefWidth(300);

        TableColumn<Etel, String> oszlopKategoria = new TableColumn<>("Kategória");
        oszlopKategoria.setCellValueFactory(new PropertyValueFactory<>("kategoriaSzoveg"));
        oszlopKategoria.setStyle("-fx-alignment: center;");

        TableColumn<Etel, Integer> oszlopEgysegAr = new TableColumn<>("Egység ár");
        oszlopEgysegAr.setCellValueFactory(new PropertyValueFactory<>("egysegAr"));
        oszlopEgysegAr.setStyle("-fx-alignment: center;");

        TableColumn<Etel, Integer> oszlopEladottMennyiseg = new TableColumn<>("Eladott Mennyiség");
        oszlopEladottMennyiseg.setCellValueFactory(new PropertyValueFactory<>("eladottMennyiseg"));
        oszlopEladottMennyiseg.setStyle("-fx-alignment: center;");

        tableViewEtel.getColumns().add(oszlopAzonosito);
        tableViewEtel.getColumns().add(oszlopNev);
        tableViewEtel.getColumns().add(oszlopKategoria);
        tableViewEtel.getColumns().add(oszlopEgysegAr);
        tableViewEtel.getColumns().add(oszlopEladottMennyiseg);

        for (Etel etel: etelLista) {
            tableViewEtel.getItems().add(etel);
        }
    }

    @FXML
    private void onButtonHibasAdatTorleseClick(ActionEvent actionEvent) {
        tableViewEtel.getItems().removeIf(etel -> etel.isHibasAzonosito());
        buttonFajlBeolvas.setDisable(true);
        buttonHibasAdatTorlese.setDisable(true);

        buttonLegDragabbDesszertek.setDisable(false);
        buttonHetiOsszBevetel.setDisable(false);
        buttonEtelekSzazalekosan.setDisable(false);
        buttonFajlbaIr.setDisable(false);
    }

    @FXML
    private  void onButtonLegDragabbDesszertekClick(ActionEvent actionEvent) {
        Etel legDragabbEtel = tableViewEtel.getItems()
                .stream()
                .filter(etel -> etel.getKategoriaId() == 3)
                .max(Comparator.comparing(Etel::getEgysegAr))
                .get();

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Legdrágább desszert és egységár");
        alert.setContentText(String.format("A legdárább desszert neve/egységára: %s / %d Ft", legDragabbEtel.getNev(), legDragabbEtel.getEgysegAr()));
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();
    }

    @FXML
    private  void onButtonHetiOsszBevetelClick(ActionEvent actionEvent) {
        long bevetel = tableViewEtel.getItems().stream().mapToInt(etel -> etel.getEgysegAr()*etel.getEladottMennyiseg()).sum();

        labelHetiOsszBevetel.setOpacity(1);
        labelHetiOsszBevetel.setText(String.format("%d Ft", bevetel));
    }

    @FXML
    private  void onButtonEtelekSzazalekosanClick(ActionEvent actionEvent) {
        long db = tableViewEtel.getItems().stream().mapToInt(Etel::getEladottMennyiseg).sum();
        long levesDb = tableViewEtel.getItems()
                .stream()
                .filter(etel -> etel.getKategoriaId() == 1)
                .mapToInt(Etel::getEladottMennyiseg)
                .sum();

        long foEtelDb = tableViewEtel.getItems()
                .stream()
                .filter(etel -> etel.getKategoriaId() == 2)
                .mapToInt(Etel::getEladottMennyiseg)
                .sum();

        long desszertDb = tableViewEtel.getItems()
                .stream()
                .filter(etel -> etel.getKategoriaId() == 3)
                .mapToInt(Etel::getEladottMennyiseg)
                .sum();

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Arányok");
        alert.setContentText(String.format("Összes étel: %d db\n\nLeves: %.2f%%\nFőétel: %.2f%%\nDesszert: %.2f%%",
                db,
                (100.0*levesDb/db),
                (100.0*foEtelDb/db),
                (100.0*desszertDb/db)
                ));
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();

    }

    @FXML
    private  void onButtonFajlbaIrClick(ActionEvent actionEvent) {
        List<Etel> szurtEtelLista = tableViewEtel.getItems()
                .stream()
                .filter(etel -> etel.getEladottMennyiseg() > 90)
                .toList();

        boolean sikeresMentes = FajlKezelo.FajlBaIr(szurtEtelLista);
        if (sikeresMentes){
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Sikeres fájl mentés");
                alert.setContentText(String.format("Sikeresen mentésre kerültek a 90 több rendeléssel rendelkező ételek a rendeles.csv állományba."));
                alert.getButtonTypes().add(ButtonType.OK);
                alert.show();
        }
    }
}