package perpus;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import javafx.scene.control.cell.PropertyValueFactory;

public class App extends Application {
    Connection conn = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    boolean flagEdit;
    TableView<Buku> tableView;
    TableColumn<Buku, Integer> idBuku;
    TableColumn<Buku, String> judul;
    TableColumn<Buku, String> penerbit;
    TableColumn<Buku, String> penulis;
    TableColumn<Buku, Integer> tahun_terbit;
    TableColumn<Buku, Boolean> status;

    TextField tfIdBuku;
    TextField tfJudul;
    TextField tfPenerbit;
    TextField tfPenulis;
    TextField tfTahunTerbit;

    Button bUpdate;
    Button bCancel;
    Button bAdd;
    Button bEdit;
    Button bDelete;
    BorderPane border;

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws Exception {
        border = new BorderPane();
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        border.setCenter(addVBoxHome());
        stage.setTitle("Sistem Perpustakaan");
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setWidth(1400);
        stage.show();
    }

    private HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10); // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");
        Text tjudul = new Text("Sistem Perpustakaan");
        // tjudul.setStyle("-fx-font-size:18pt;");
        tjudul.setFont(Font.font("Verdana", 20));
        tjudul.setFill(Color.WHITESMOKE);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(tjudul);
        return hbox;
    }

    private VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8); // Gap between nodes
        Text title = new Text("Perpustakaan");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);
        Hyperlink options[] = new Hyperlink[] {
                new Hyperlink("Home"),
                new Hyperlink("Buku"),
                new Hyperlink("Pustakawan"),
                new Hyperlink("Anggota"),
                new Hyperlink("Peminjaman"),
                new Hyperlink("Koleksi Buku"),
                new Hyperlink("Selesai") };
        for (int i = 0; i < 7; i++) {
            // Add offset to left side to indent from title
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }
        options[0].setOnAction(e -> {
            border.setCenter(addVBoxHome());
        });
        options[1].setOnAction(e -> {
            border.setCenter(viewBuku());
        });
        options[2].setOnAction(e -> {
            border.setCenter(vbPustakawan());
        });
        options[3].setOnAction(e -> {
            border.setCenter(vbAnggota());
        });
        options[4].setOnAction(e -> {
            border.setCenter(vbPeminjaman());
        });
        options[5].setOnAction(e -> {
            border.setCenter(vbKoleksi());
        });
        options[6].setOnAction(e -> {
            System.exit(0);
        });
        return vbox;
    }

    private VBox addVBoxHome() {
        VBox vb = new VBox();
        vb.setFillWidth(true);
        Text tjudul = new Text("Home");
        tjudul.setFont(Font.font("Arial", 18));
        vb.setAlignment(Pos.CENTER);
        FileInputStream input = null;
        try {
            input = new FileInputStream("src/img/banner.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Image image = new Image(input);
        ImageView imageview = new ImageView(image);
        imageview.setFitHeight(500);
        imageview.setPreserveRatio(true);
        vb.getChildren().addAll(tjudul,imageview);
        vb.setPadding(new Insets(10));
        vb.setSpacing(8);
        return vb;
    }

    @SuppressWarnings({ "unchecked", "static-access" })
    StackPane spTableBuku() {
        StackPane sp = new StackPane();
        Label title = new Label("Daftar Buku");
        title.setFont(Font.font("Arial", 18));
        title.setAlignment(Pos.CENTER);
        tableView = new TableView<Buku>();
        idBuku = new TableColumn<>("ID buku");
        judul = new TableColumn<>("Judul");
        penerbit = new TableColumn<>("Penerbit");
        penulis = new TableColumn<>("Penulis");
        tahun_terbit = new TableColumn<Buku, Integer>("tahun terbit");
        status = new TableColumn<Buku, Boolean>("Status");
        tableView.getColumns().addAll(idBuku, judul, penerbit, penulis, tahun_terbit, status);
        idBuku.setCellValueFactory(new PropertyValueFactory<Buku, Integer>("idBuku"));
        judul.setCellValueFactory(new PropertyValueFactory<Buku, String>("judul"));
        penerbit.setCellValueFactory(new PropertyValueFactory<Buku, String>("penerbit"));
        penulis.setCellValueFactory(new PropertyValueFactory<Buku, String>("penulis"));
        tahun_terbit.setCellValueFactory(new PropertyValueFactory<Buku, Integer>("tahunTerbit"));
        status.setCellValueFactory(new PropertyValueFactory<Buku, Boolean>("status"));

        status.setCellFactory(column -> {
            return new TableCell<Buku, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        Buku buku = getTableView().getItems().get(getIndex());
                        if (buku.getStatus().equals(false)) {
                            setText("Tidak tersedia");
                        } else if (buku.getStatus().equals(true)) {
                            setText("Tersedia");
                        }
                    }
                }
            };
        });
        idBuku.setPrefWidth(50);
        judul.setPrefWidth(300);
        penulis.setPrefWidth(200);
        penerbit.setPrefWidth(200);
        tahun_terbit.setPrefWidth(100);
        tableView.setPrefWidth(850);
        showListBuku();
        sp.getChildren().addAll(title, tableView);
        sp.setPadding(new Insets(10, 10, 0, 0));
        return sp;
    }

    GridPane gpFormBuku() {
        flagEdit = false;
        GridPane gp = new GridPane();
        gp.setPrefHeight(500);
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(10);
        gp.setHgap(10);
        gp.setPadding(new Insets(10, 10, 10, 10));
        Label lbIdBuku = new Label("Id Buku");
        Label lbJudul = new Label("Judul");
        Label lbPenerbit = new Label("Penerbit");
        Label lbPenulis = new Label("Penulis");
        Label lbTahunTerbit = new Label("Tahun Terbit");
        tfIdBuku = new TextField();
        tfJudul = new TextField();
        tfPenerbit = new TextField();
        tfPenulis = new TextField();
        tfTahunTerbit = new TextField();
        bUpdate = new Button("Update");
        bCancel = new Button("Cancel");
        bAdd = new Button("Add");
        bEdit = new Button("Edit");
        bDelete = new Button("Del");
        bAdd.setPrefWidth(100);
        bEdit.setPrefWidth(100);
        bDelete.setPrefWidth(100);
        bUpdate.setPrefWidth(100);
        bCancel.setPrefWidth(100);
        bUpdate.setOnAction(e -> {
            int idBuku, tahunTerbit;
            String judul, penulis, penerbit;
            Boolean status;
            idBuku = Integer.parseInt(tfIdBuku.getText());
            judul = tfJudul.getText();
            penulis = tfPenulis.getText();
            penerbit = tfPenerbit.getText();
            tahunTerbit = Integer.parseInt(tfTahunTerbit.getText());
            status = Boolean.parseBoolean("false");
            Buku b = new Buku(idBuku, judul, penerbit, penulis, tahunTerbit, status);
            if (flagEdit == false) {
                String sql = "insert into buku(idbuku,judul,penerbit,penulis,tahun_terbit, status) values (?,?,?,?,?,1)";
                conn = DBConnection.getConn();
                try {
                    st = conn.prepareStatement(sql);
                    st.setString(1, tfIdBuku.getText());
                    st.setString(2, tfJudul.getText());
                    st.setString(3, tfPenerbit.getText());
                    st.setString(4, tfPenulis.getText());
                    st.setString(5, tfTahunTerbit.getText());
                    st.executeUpdate();
                    Koleksi.updateJumlahKoleksi(idBuku);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                String sql = "update buku set judul=?,penerbit=?,penulis=?,tahun_terbit=? where idbuku=?";
                conn = DBConnection.getConn();
                try {
                    st = conn.prepareStatement(sql);
                    st.setString(1, tfJudul.getText());
                    st.setString(2, tfPenerbit.getText());
                    st.setString(3, tfPenulis.getText());
                    st.setString(4, tfTahunTerbit.getText());
                    st.setString(5, tfIdBuku.getText());
                    st.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            showListBuku();
            teksAktif(false);
            buttonAktif(false);
            clearTeks();
            flagEdit = true;
        });
        bEdit.setOnAction(e -> {
            buttonAktif(true);
            teksAktif(true);
            flagEdit = true;
            int idx = tableView.getSelectionModel().getSelectedIndex();
            tfIdBuku.setText(String.valueOf(tableView.getItems().get(idx).getIdBuku()));
            tfJudul.setText(tableView.getItems().get(idx).getJudul());
            tfPenerbit.setText(tableView.getItems().get(idx).getPenulis());
            tfPenulis.setText(tableView.getItems().get(idx).getPenulis());
            tfTahunTerbit.setText(String.valueOf(tableView.getItems().get(idx).getTahunTerbit()));
        });
        bAdd.setOnAction(e -> {
            flagEdit = false;
            clearTeks();
            teksAktif(true);
            buttonAktif(true);
        });
        bCancel.setOnAction(e -> {
            teksAktif(false);
            buttonAktif(false);
        });
        bDelete.setOnAction(e -> {
            int idbuku = tableView.getSelectionModel().getSelectedItem().getIdBuku();
            String sql = "delete from buku where idbuku=?";
            conn = DBConnection.getConn();
            try {
                st = conn.prepareStatement(sql);
                st.setString(1, String.valueOf(idbuku));
                st.executeUpdate();
                Koleksi.updateJumlahKoleksi(idbuku);
                showListBuku();
                clearTeks();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        TilePane tp1 = new TilePane();
        tp1.getChildren().addAll(bAdd, bEdit, bDelete, bUpdate, bCancel);
        gp.addRow(0, lbIdBuku, tfIdBuku);
        gp.addRow(1, lbJudul, tfJudul);
        gp.addRow(2, lbPenerbit, tfPenerbit);
        gp.addRow(3, lbPenulis, tfPenulis);
        gp.addRow(4, lbTahunTerbit, tfTahunTerbit);
        gp.addRow(5, new Label(""), tp1);
        teksAktif(false);
        buttonAktif(false);
        return gp;
    }

    private VBox addVBoxBuku() {
        VBox vb = new VBox();
        Text tjudul = new Text("Form Data Buku");
        tjudul.setFont(Font.font("Arial", 18));
        vb.getChildren().add(tjudul);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().add(spTableBuku());
        vb.getChildren().add(gpFormBuku());
        return vb;
    }

    private VBox viewBuku() {
        VBox vb = new VBox();
        Text tjudul = new Text("Data Buku");
        tjudul.setFont(Font.font("Arial", 18));
        vb.setAlignment(Pos.TOP_CENTER);
        vb.getChildren().addAll(tjudul,spTableBuku());
        vb.setPadding(new Insets(10,0,0,0));
        return vb;
    }

    public void buttonAktif(boolean nonAktif) {
        bAdd.setDisable(nonAktif);
        bEdit.setDisable(nonAktif);
        bDelete.setDisable(nonAktif);
        bUpdate.setDisable(!nonAktif);
        bCancel.setDisable(!nonAktif);
    }

    public void teksAktif(boolean aktif) {
        tfIdBuku.setEditable(aktif);
        tfJudul.setEditable(aktif);
        tfPenerbit.setEditable(aktif);
        tfPenulis.setEditable(aktif);
        tfTahunTerbit.setEditable(aktif);
    }

    public void clearTeks() {
        tfIdBuku.setText("");
        tfJudul.setText("");
        tfPenerbit.setText("");
        tfPenulis.setText("");
        tfTahunTerbit.setText("");
    }

    public void showListBuku() {
        ObservableList<Buku> listBuku = Buku.getlistBuku();
        tableView.setItems(listBuku);
    }

    public void getData() {
        Buku b = tableView.getSelectionModel().getSelectedItem();
        tfIdBuku.setText(String.valueOf(b.getIdBuku()));
        tfJudul.setText(b.getJudul());
        tfPenerbit.setText(b.getPenerbit());
        tfPenulis.setText(b.getPenulis());
        tfTahunTerbit.setText(String.valueOf(b.getTahunTerbit()));
    }

    private <T> VBox createVBoxForClass(
            String title,
            TableView<T> tableView,
            List<TableColumn<T, ?>> columns,
            List<Button> buttons,
            Runnable addButtonAction,
            Runnable deleteButtonAction,
            BiConsumer<TableView<T>, T> editButtonAction) {

        VBox vb = new VBox();
        Label tjudul = new Label(title);
        tjudul.setFont(Font.font("Arial", 18));
        tjudul.setStyle("-fx-padding:0 0 10 0;");
        vb.getChildren().add(tjudul);
        vb.setAlignment(Pos.TOP_CENTER);

        tableView.getColumns().addAll(columns);

        Button addButton = buttons.get(0);
        Button editButton = buttons.get(1);
        Button deleteButton = buttons.get(2);

        editButton.setDisable(true);
        deleteButton.setDisable(true);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editButton.setDisable(false);
                deleteButton.setDisable(false);
            } else {
                editButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });

        addButton.setOnAction(e -> addButtonAction.run());

        editButton.setOnAction(e -> {
            T selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editButtonAction.accept(tableView, selectedItem);
            }
        });

        deleteButton.setOnAction(e -> {
            T selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                deleteButtonAction.run();
                tableView.getItems().remove(selectedItem);
            }
        });

        HBox hbButtons = new HBox(10, addButton, editButton, deleteButton);
        hbButtons.setAlignment(Pos.CENTER);
        VBox.setMargin(tableView, new Insets(0, 0, 10, 0));
        vb.setPadding(new Insets(0, 20, 10, 0));
        vb.getChildren().addAll(tableView, hbButtons);
        return vb;
    }

    private VBox vbAnggota() {

        TableColumn<Anggota, Integer> idCol = new TableColumn<>("ID");
        TableColumn<Anggota, String> namaCol = new TableColumn<>("Nama");
        TableColumn<Anggota, String> alamatCol = new TableColumn<>("Alamat");
        TableColumn<Anggota, String> teleponCol = new TableColumn<>("Telepon");
        TableColumn<Anggota, String> emailCol = new TableColumn<>("Email");
        TableColumn<Anggota, Date> tanggalDaftarCol = new TableColumn<>("Tanggal Daftar");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        alamatCol.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        teleponCol.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        tanggalDaftarCol.setCellValueFactory(new PropertyValueFactory<>("tanggalDaftar"));
        List<TableColumn<Anggota, ?>> columns = List.of(idCol, namaCol, alamatCol, teleponCol, emailCol,
                tanggalDaftarCol);

        Button bDaftar = new Button("Tambahkan Anggota");
        Button bEdit = new Button("Edit Anggota");
        Button bDelete = new Button("Hapus Anggota");

        List<Button> buttons = List.of(bDaftar, bEdit, bDelete);
        TableView<Anggota> tableViewAnggota = new TableView<>();

        VBox vb = createVBoxForClass("Anggota", tableViewAnggota, columns, buttons,
                () -> showAnggotaForm(null, tableViewAnggota),
                () -> {
                    Anggota selectedAnggota = tableViewAnggota.getSelectionModel().getSelectedItem();
                    if (selectedAnggota != null) {
                        Anggota.deleteAnggota(selectedAnggota.getId());
                        showListAnggota(tableViewAnggota);
                    }
                },
                (tableView, selectedAnggota) -> showAnggotaForm(selectedAnggota, tableView));

        showListAnggota(tableViewAnggota);
        vb.setPadding(new Insets(10));
        return vb;
    }

    private void showAnggotaForm(Anggota anggota, TableView<Anggota> tableView) {
        Stage stage = new Stage();
        stage.setTitle(anggota == null ? "Daftar Anggota Baru" : "Edit Anggota");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);
        stage.setMinWidth(300);
        stage.setMinHeight(290);

        TextField tfId = new TextField();
        tfId.setPromptText("Id");
        TextField tfNama = new TextField();
        tfNama.setPromptText("Nama");
        TextField tfAlamat = new TextField();
        tfAlamat.setPromptText("Alamat");
        TextField tfTelepon = new TextField();
        tfTelepon.setPromptText("Telepon");
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("Email");
        DatePicker dpTanggalDaftar = new DatePicker();

        if (anggota != null) {
            tfId.setText(String.valueOf(anggota.getId()));
            tfId.setEditable(false);
            tfNama.setText(anggota.getNama());
            tfAlamat.setText(anggota.getAlamat());
            tfTelepon.setText(anggota.getTelepon());
            tfEmail.setText(anggota.getEmail());
            dpTanggalDaftar.setValue(anggota.getTanggalDaftar().toLocalDate());
        }

        grid.add(new Label("Id"), 0, 0);
        grid.add(tfId, 1, 0);
        grid.add(new Label("Nama"), 0, 1);
        grid.add(tfNama, 1, 1);
        grid.add(new Label("Alamat"), 0, 2);
        grid.add(tfAlamat, 1, 2);
        grid.add(new Label("Telepon"), 0, 3);
        grid.add(tfTelepon, 1, 3);
        grid.add(new Label("Email"), 0, 4);
        grid.add(tfEmail, 1, 4);
        grid.add(new Label("Tanggal Daftar"), 0, 5);
        grid.add(dpTanggalDaftar, 1, 5);

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tfId.getText());
                String nama = tfNama.getText();
                String alamat = tfAlamat.getText();
                String telepon = tfTelepon.getText();
                String email = tfEmail.getText();
                LocalDate tanggalDaftar = dpTanggalDaftar.getValue();

                if (nama.isEmpty() || alamat.isEmpty() || telepon.isEmpty() || email.isEmpty()
                        || tanggalDaftar == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Harap mengisi formulir dengan lengkap");
                    return;
                }

                if (anggota == null) {
                    Anggota newAnggota = new Anggota(id, nama, alamat, telepon, email, Date.valueOf(tanggalDaftar));
                    Anggota.registerAnggota(newAnggota);
                } else {
                    anggota.setId(id);
                    anggota.setNama(nama);
                    anggota.setAlamat(alamat);
                    anggota.setTelepon(telepon);
                    anggota.setEmail(email);
                    anggota.setTanggalDaftar(Date.valueOf(tanggalDaftar));
                    Anggota.updateAnggota(anggota);
                }
                showListAnggota(tableView);
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "ID harus berupa angka");
            }
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> stage.close());

        HBox hbButtons = new HBox(10, btnSave, btnCancel);
        hbButtons.setAlignment(Pos.CENTER);
        grid.add(hbButtons, 1, 6);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showListAnggota(TableView<Anggota> tableView) {
        ObservableList<Anggota> listAnggota = Anggota.getlistAnggota();
        tableView.setItems(listAnggota);
    }

    /**
     * Pustakawan
     */

    private HBox vbPustakawan() {
        TableView<Pustakawan> tableViewPustakawan = new TableView<>();
        TableColumn<Pustakawan, Integer> id = new TableColumn<>("Id");
        TableColumn<Pustakawan, String> nama = new TableColumn<>("Nama");
        TableColumn<Pustakawan, String> email = new TableColumn<>("Email");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));

        List<TableColumn<Pustakawan, ?>> columns = List.of(id, nama, email);

        Button bDaftar = new Button("Tambah Pustakawan");
        Button bEdit = new Button("Edit Pustakawan");
        Button bDelete = new Button("Hapus Pustakawan");

        List<Button> buttons = List.of(bDaftar, bEdit, bDelete);
        VBox vbPustakawan = createVBoxForClass("Pustakawan", tableViewPustakawan, columns, buttons,
                () -> showPustakawanForm(null, tableViewPustakawan),
                () -> {
                    Pustakawan selectedPustakawan = tableViewPustakawan.getSelectionModel().getSelectedItem();
                    if (selectedPustakawan != null) {
                        Pustakawan.deletePustakawan(selectedPustakawan.getId());
                        showListPuswakawan(tableViewPustakawan);
                    }
                },
                (tableView, selectedPustakawan) -> showPustakawanForm(selectedPustakawan, tableView));
        showListPuswakawan(tableViewPustakawan);

        VBox vbBuku = addVBoxBuku();

        HBox hb = new HBox();
        hb.getChildren().addAll(vbPustakawan, vbBuku);
        hb.setSpacing(10);
        hb.setPadding(new Insets(10, 10, 10, 10));

        vbPustakawan.prefWidthProperty().bind(hb.widthProperty().divide(2));
        vbBuku.prefWidthProperty().bind(hb.widthProperty().divide(2));

        return hb;
    }

    private void showPustakawanForm(Pustakawan pustakawan, TableView<Pustakawan> tableView) {
        Stage stage = new Stage();
        stage.setTitle(pustakawan == null ? "Tambah Pustakawan" : "Edit Pustakawan");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        stage.setMinWidth(300);
        stage.setMinHeight(290);
        TextField tfId = new TextField();
        tfId.setPromptText("ID");
        TextField tfNama = new TextField();
        tfNama.setPromptText("Nama Pustakawan");
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("Email");
        if (pustakawan != null) {
            tfId.setText(String.valueOf(pustakawan.getId()));
            tfId.setEditable(false);
            tfNama.setText(pustakawan.getNama());
            tfEmail.setText(pustakawan.getEmail());
        }
        grid.add(new Text("ID"), 0, 0);
        grid.add(tfId, 1, 0);
        grid.add(new Text("Nama"), 0, 1);
        grid.add(tfNama, 1, 1);
        grid.add(new Text("Email"), 0, 2);
        grid.add(tfEmail, 1, 2);
        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tfId.getText());
                String nama = tfNama.getText();
                String email = tfEmail.getText();

                if (nama.isEmpty() || email.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Harap mengisi formulir dengan lengkap");
                    return;
                }

                if (pustakawan == null) {
                    Pustakawan newPustakawan = new Pustakawan(id, nama, email);
                    Pustakawan.registerPustakawan(newPustakawan);
                } else {
                    pustakawan.setId(id);
                    pustakawan.setNama(nama);
                    pustakawan.setEmail(email);
                    Pustakawan.updatePustakawan(pustakawan);
                }

                showListPuswakawan(tableView);
                stage.close();

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "ID harus berupa angka");
            }
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> stage.close());
        HBox hbButtons = new HBox();
        hbButtons.getChildren().addAll(btnSave, btnCancel);
        hbButtons.setAlignment(Pos.CENTER);
        hbButtons.setSpacing(10);
        grid.add(hbButtons, 1, 3);
        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void showListPuswakawan(TableView<Pustakawan> tableView) {
        ObservableList<Pustakawan> listPustakawan = Pustakawan.getlistPustakawan();
        tableView.setItems(listPustakawan);
    }

    private Anggota getAnggotaById(int id) {
        for (Anggota anggota : Anggota.getlistAnggota()) {
            if (anggota.getId() == id) {
                return anggota;
            }
        }
        return null;
    }

    private Buku getBukuById(int id) {
        for (Buku buku : Buku.getlistBuku()) {
            if (buku.getIdBuku() == id) {
                return buku;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private VBox vbPeminjaman() {
        GridPane formPane = new GridPane();
        Label tjudul = new Label("Peminjaman");
        tjudul.setFont(Font.font("Arial", 18));
        formPane.setPadding(new Insets(10));
        formPane.setHgap(10);
        formPane.setVgap(10);

        Random rand = new Random();
        int randomInt = rand.nextInt((999999 - 100000) + 1) + 100000;
        TextField idField = new TextField(Integer.toString(randomInt));
        idField.setPromptText("ID Peminjaman");
        idField.setEditable(false);
        TextField anggotaIdField = new TextField();
        anggotaIdField.setPromptText("ID Anggota");
        TextField bukuIdField = new TextField();
        bukuIdField.setPromptText("ID Buku");
        DatePicker tanggalPinjamPicker = new DatePicker();
        tanggalPinjamPicker.setPromptText("Tanggal Pinjam");

        TableView<Peminjaman> tableViewPeminjaman = new TableView<>();
        Button addButton = new Button("Tambah Peminjaman");
        addButton.setOnAction(e -> {
            if (idField.getText().isEmpty() || anggotaIdField.getText().isEmpty() || bukuIdField.getText().isEmpty() ||
                    tanggalPinjamPicker.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Harap mengisi formulir dengan lengkap");
                return;
            }

            int id = Integer.parseInt(idField.getText());
            int anggotaId = Integer.parseInt(anggotaIdField.getText());
            int bukuId = Integer.parseInt(bukuIdField.getText());
            LocalDate tanggalPinjam = tanggalPinjamPicker.getValue();

            Anggota anggota = getAnggotaById(anggotaId);
            Buku buku = getBukuById(bukuId);

            if (anggota == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "ID Anggota tidak ditemukan!");
                return;
            }

            if (buku == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "ID Buku tidak ditemukan!");
                return;
            }

            if (buku.getStatus() == false) {
                showAlert(Alert.AlertType.ERROR, "Error", "Buku sudah dipinjam!");
                return;
            }

            Peminjaman peminjaman = new Peminjaman(id, anggota, buku, tanggalPinjam);
            peminjaman.save();
            idField.setText(Integer.toString(rand.nextInt(randomInt)));
            anggotaIdField.clear();
            bukuIdField.clear();
            tanggalPinjamPicker.setValue(null);
            showListPeminjaman(tableViewPeminjaman);
        });

        formPane.add(new Label("ID Peminjaman:"), 0, 0);
        formPane.add(idField, 1, 0);
        formPane.add(new Label("ID Anggota:"), 0, 1);
        formPane.add(anggotaIdField, 1, 1);
        formPane.add(new Label("ID Buku:"), 0, 2);
        formPane.add(bukuIdField, 1, 2);
        formPane.add(new Label("Tanggal Pinjam:"), 0, 3);
        formPane.add(tanggalPinjamPicker, 1, 3);
        formPane.add(addButton, 1, 5);

        TableColumn<Peminjaman, Integer> idCol = new TableColumn<>("ID");
        TableColumn<Peminjaman, Integer> idAnggotaCol = new TableColumn<>("ID Anggota");
        TableColumn<Peminjaman, String> anggotaCol = new TableColumn<>("Nama Anggota");
        TableColumn<Peminjaman, Integer> idBukuCol = new TableColumn<>("ID Buku");
        TableColumn<Peminjaman, String> bukuCol = new TableColumn<>("Judul Buku");
        TableColumn<Peminjaman, LocalDate> tanggalPinjamCol = new TableColumn<>("Tanggal Pinjam");
        TableColumn<Peminjaman, LocalDate> tanggalKembaliCol = new TableColumn<>("Tanggal Kembali");
        TableColumn<Peminjaman, String> statusCol = new TableColumn<>("Status");

        idCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        idAnggotaCol
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAnggota().getId()));
        anggotaCol.setCellValueFactory(
                cellData -> new SimpleObjectProperty<>(cellData.getValue().getAnggota().getNama()));
        idBukuCol
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBuku().getIdBuku()));
        bukuCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBuku().getJudul()));
        tanggalPinjamCol
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTanggalPinjam()));
        tanggalKembaliCol
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTanggalKembali()));
        statusCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));
        statusCol.setCellFactory(column -> new TableCell<Peminjaman, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    String color;
                    switch (status) {
                        case "belum dikembalikan":
                            color = "#FFFF00";
                            break;
                        case "telah dikembalikan":
                            color = "#7FFF00";
                            break;
                        case "terlambat kembali":
                            color = "#DC143C";
                            break;
                        default:
                            color = "";
                            break;
                    }
                    setStyle("-fx-background-color: " + color + ";");
                }
            }
        });

        tableViewPeminjaman.getColumns().addAll(idCol, idAnggotaCol, anggotaCol, idBukuCol, bukuCol, tanggalPinjamCol,
                tanggalKembaliCol, statusCol);

        Button kembalikanButton = new Button("Kembalikan Buku");
        kembalikanButton.setOnAction(e -> {
            Peminjaman selectedPeminjaman = tableViewPeminjaman.getSelectionModel().getSelectedItem();
            if (selectedPeminjaman != null) {

                String selectedStatus = selectedPeminjaman.getStatus();
                if ("telah dikembalikan".equals(selectedStatus) || "terlambat kembali".equals(selectedStatus)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Buku sudah dikembalikan!");
                    return;
                }

                LocalDate today = LocalDate.now();
                if (today.isAfter(selectedPeminjaman.getTanggalKembali())) {
                    selectedPeminjaman.setStatus("terlambat kembali");
                } else {
                    selectedPeminjaman.setStatus("telah dikembalikan");
                }
                Peminjaman.updatePeminjamanStatus(selectedPeminjaman);
                showListPeminjaman(tableViewPeminjaman);

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Pilih dahulu peminjaman yang ingin dikembalikan!");
            }
        });

        VBox vbox = new VBox(10, tjudul, formPane, tableViewPeminjaman, kembalikanButton);
        showListPeminjaman(tableViewPeminjaman);
        vbox.setPadding(new Insets(10, 10, 40, 0));
        vbox.setAlignment(Pos.TOP_CENTER);
        return vbox;
    }

    private void showListPeminjaman(TableView<Peminjaman> tableViewPeminjaman) {
        ObservableList<Peminjaman> listpeminjaman = Peminjaman.getListPeminjaman();
        tableViewPeminjaman.setItems(listpeminjaman);
    }

    /**
     * koleksi
     */

    private VBox vbKoleksi() {
        TableColumn<Koleksi, String> idCol = new TableColumn<>("ID Koleksi");
        TableColumn<Koleksi, String> jenisCol = new TableColumn<>("Jenis Koleksi");
        TableColumn<Koleksi, Integer> jumlahCol = new TableColumn<>("Jumlah");
        TableColumn<Koleksi, String> lokasiCol = new TableColumn<>("Lokasi");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idKoleksi"));
        jenisCol.setCellValueFactory(new PropertyValueFactory<>("idKoleksi"));

jenisCol.setCellFactory(column -> {
    return new TableCell<Koleksi, String>() {
        @Override
        protected void updateItem(String idKoleksi, boolean empty) {
            super.updateItem(idKoleksi, empty);
            if (empty || idKoleksi == null) {
                setText(null);
                setStyle("");
            } else {
                String jenisKoleksi = "";
                switch (idKoleksi) {
                    case "11":
                        jenisKoleksi = "Jurnal";
                        break;
                    case "21":
                        jenisKoleksi = "Kamus";
                        break;
                    case "31":
                        jenisKoleksi = "Sejarah";
                        break;
                    case "41":
                        jenisKoleksi = "Matematika";
                        break;
                    default:
                        jenisKoleksi = "Unknown";
                        break;
                }
                setText(jenisKoleksi);
            }
        }
    };
});

        

        jumlahCol.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        lokasiCol.setCellValueFactory(new PropertyValueFactory<>("lokasi"));

        jenisCol.setPrefWidth(200);
        List<TableColumn<Koleksi, ?>> columns = List.of(idCol, jenisCol, jumlahCol, lokasiCol);
        
        Button addButton = new Button("Add Koleksi");
        Button ediButton = new Button("Edit Koleksi");
        Button deleteButton = new Button("Delete Koleksi");
        
        List<Button> buttons = List.of(addButton, ediButton, deleteButton);
        TableView<Koleksi> tableViewKoleksi = new TableView<>();

        VBox vb = createVBoxForClass("Koleksi", tableViewKoleksi, columns, buttons,
                () -> showKoleksiForm(null, tableViewKoleksi),
                () -> {
                    Koleksi selectedKoleksi = tableViewKoleksi.getSelectionModel().getSelectedItem();
                    if (selectedKoleksi != null) {
                        Koleksi.deleteKoleksi(selectedKoleksi);
                        showListKoleksi(tableViewKoleksi);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Silahkan pilih koleksi yang ingin di hapus");
                    }
                },
                (tableView, selectedKoleksi) -> showKoleksiForm(selectedKoleksi, tableView));
        showListKoleksi(tableViewKoleksi);
        vb.setPadding(new Insets(10));

        return vb;
    }

    private void showKoleksiForm(Koleksi koleksi, TableView<Koleksi> tableViewKoleksi) {
        Stage stage = new Stage();
        stage.setTitle(koleksi == null ? "Add Koleksi" : "Edit Koleksi");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        stage.setMinWidth(300);
        stage.setMinHeight(290);

        TextField tfIdKoleksi = new TextField();
        tfIdKoleksi.setPromptText("ID Koleksi");
        TextField tfLokasi = new TextField();
        tfLokasi.setPromptText("Lokasi");

        if (koleksi != null) {
            tfIdKoleksi.setText(String.valueOf(koleksi.getBuku().getIdBuku()).substring(0, 2));
            tfIdKoleksi.setEditable(false);
            tfLokasi.setText(koleksi.getLokasi());
        }

        grid.add(new Text("ID Koleksi"), 0, 0);
        grid.add(tfIdKoleksi, 1, 0);
        grid.add(new Text("Lokasi"), 0, 1);
        grid.add(tfLokasi, 1, 1);

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> {
            if (tfIdKoleksi.getText().isEmpty() || tfLokasi.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Mohon isi semua kolom");
                return;
            }

            String idKoleksi = tfIdKoleksi.getText();
            String lokasi = tfLokasi.getText();

            try {
                if (koleksi == null) {
                    Koleksi.addKoleksi(idKoleksi, lokasi);
                } else {
                    Koleksi.updateKoleksi(koleksi, lokasi);
                }
                showListKoleksi(tableViewKoleksi);
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        grid.add(btnSave, 1, 2);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }

    private void showListKoleksi(TableView<Koleksi> tableViewKoleksi) {
        ObservableList<Koleksi> koleksiList = Koleksi.getListKoleksi();
        tableViewKoleksi.setItems(koleksiList);
    }

}