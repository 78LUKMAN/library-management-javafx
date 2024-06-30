// package perpus;

// import javafx.application.Application;
// import javafx.beans.Observable;
// import javafx.beans.value.ObservableListValue;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.*;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;
// import javafx.scene.text.Text;
// import javafx.stage.Stage;

// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
// import java.util.ArrayList;
// import java.util.List;
// import javafx.scene.control.cell.PropertyValueFactory;

// public class App extends Application {
//     boolean flagEdit;
//     TableView<Buku> tableView;
//     TableColumn<Buku, Integer> idBuku;
//     TableColumn<Buku, String> judul;
//     TableColumn<Buku, String> penerbit;
//     TableColumn<Buku, String> penulis;
//     TableColumn<Buku, Integer> tahun_terbit;
//     TextField tfIdBuku;
//     TextField tfJudul;
//     TextField tfPenerbit;
//     TextField tfPenulis;
//     TextField tfTahunTerbit;
//     Button bUpdate;
//     Button bCancel;
//     Button bAdd;
//     Button bEdit;
//     Button bDel;
//     BorderPane border;

//     boolean flagEditPustakawan;
//     TableView<Pustakawan> tableViewPustakawan;
//     TableColumn<Pustakawan, Integer> idPustakawan;
//     TableColumn<Pustakawan, String> namaPustakawan;
//     TableColumn<Pustakawan, String> alamatPustakawan;
//     TextField tfIdPustakawan;
//     TextField tfNamaPustakawan;
//     TextField tfAlamatPustakawan;
//     Button bUpdatePustakawan;
//     Button bCancelPustakawan;
//     Button bAddPustakawan;
//     Button bEditPustakawan;
//     Button bDelPustakawan;

//     boolean flagEditAnggota;
//     TableView<Anggota> tableViewAnggota;
//     TableColumn<Anggota, Integer> idAnggota;
//     TableColumn<Anggota, String> namaAnggota;
//     TableColumn<Anggota, String> alamatAnggota;
//     TextField tfIdAnggota;
//     TextField tfNamaAnggota;
//     TextField tfAlamatAnggota;
//     Button bUpdateAnggota;
//     Button bCancelAnggota;
//     Button bAddAnggota;
//     Button bEditAnggota;
//     Button bDelAnggota;

//     @Override
//     public void start(Stage stage) throws Exception {
//         border = new BorderPane();
//         HBox hbox = addHBox();
//         border.setTop(hbox);
//         border.setLeft(addVBox());
//         border.setCenter(addVBoxHome());
//         stage.setTitle("Sistem Perpustakaan");
//         Scene scene = new Scene(border);
//         stage.setScene(scene);
//         stage.setTitle("Layout Sample");
//         stage.show();
//     }

//     private HBox addHBox() {
//         HBox hbox = new HBox();
//         hbox.setPadding(new Insets(15, 12, 15, 12));
//         hbox.setSpacing(10); // Gap between nodes
//         hbox.setStyle("-fx-background-color: #336699;");
//         Text tjudul = new Text("Sistem Perpustakaan");
//         // tjudul.setStyle("-fx-font-size:18pt;");
//         tjudul.setFont(Font.font("Verdana", 20));
//         tjudul.setFill(Color.WHITESMOKE);
//         hbox.setAlignment(Pos.CENTER);
//         hbox.getChildren().add(tjudul);
//         return hbox;
//     }

//     private VBox addVBox() {
//         VBox vbox = new VBox();
//         vbox.setPadding(new Insets(10)); // Set all sides to 10
//         vbox.setSpacing(8); // Gap between nodes
//         Text title = new Text("Perpustakaan");
//         title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//         vbox.getChildren().add(title);
//         Hyperlink options[] = new Hyperlink[] {
//                 new Hyperlink("Home"),
//                 new Hyperlink("Buku"),
//                 new Hyperlink("Pustakawan"),
//                 new Hyperlink("Anggota"),
//                 new Hyperlink("Peminjaman"),
//                 new Hyperlink("Koleksi Buku"),
//                 new Hyperlink("Selesai") };
//         for (int i = 0; i < 7; i++) {
//             // Add offset to left side to indent from title
//             VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
//             vbox.getChildren().add(options[i]);
//         }
//         options[0].setOnAction(e -> {
//             border.setCenter(addVBoxHome());
//         });
//         options[1].setOnAction(e -> {
//             border.setCenter(addVBoxBuku());
//         });
//         options[2].setOnAction(e -> {
//             border.setCenter(addVBoxPustakawan());
//         });
//         options[3].setOnAction(e -> {
//             border.setCenter(addVBoxAnggota());
//         });
//         options[6].setOnAction(e -> {
//             System.exit(0);
//         });
//         return vbox;
//     }

//     private FlowPane AddFlowPaneHome() {
//         FlowPane fp = new FlowPane();
//         Text tjudul = new Text("Home");
//         tjudul.setFont(Font.font("Arial", 18));
//         fp.setAlignment(Pos.CENTER);
//         FileInputStream input = null;
//         try {
//             input = new FileInputStream("D:/Lukman/Learning/Java/perpustakaan/src/img/bg.png");
//         } catch (FileNotFoundException e) {
//             throw new RuntimeException(e);
//         }
//         Image image = new Image(input);
//         ImageView imageview = new ImageView(image);
//         fp.getChildren().add(tjudul);
//         fp.getChildren().add(imageview);
//         return fp;
//     }

//     private VBox addVBoxHome() {
//         VBox vb = new VBox();
//         vb.setFillWidth(true);
//         Text tjudul = new Text("Home");
//         tjudul.setFont(Font.font("Arial", 18));
//         vb.setAlignment(Pos.CENTER);
//         FileInputStream input = null;
//         try {
//             input = new FileInputStream("D:/Lukman/Learning/Java/perpustakaan/src/img/bg.png");
//         } catch (FileNotFoundException e) {
//             throw new RuntimeException(e);
//         }
//         Image image = new Image(input);
//         ImageView imageview = new ImageView(image);
//         imageview.setFitHeight(500);
//         imageview.setPreserveRatio(true);
//         vb.getChildren().add(tjudul);
//         vb.getChildren().add(imageview);
//         return vb;
//     }

//     StackPane spTableBuku() {
//         StackPane sp = new StackPane();
//         tableView = new TableView<Buku>();
//         idBuku = new TableColumn<>("idBuku");
//         judul = new TableColumn<>("judul");
//         penerbit = new TableColumn<>("penerbit");
//         penulis = new TableColumn<>("penulis");
//         tahun_terbit = new TableColumn<Buku, Integer>("tahun_terbit");
//         tableView.getColumns().addAll(idBuku, judul, penerbit, penulis, tahun_terbit);
//         idBuku.setCellValueFactory(new PropertyValueFactory<Buku, Integer>("idBuku"));
//         judul.setCellValueFactory(new PropertyValueFactory<Buku, String>("judul"));
//         penerbit.setCellValueFactory(new PropertyValueFactory<Buku, String>("penerbit"));
//         penulis.setCellValueFactory(new PropertyValueFactory<Buku, String>("penulis"));
//         tahun_terbit.setCellValueFactory(new PropertyValueFactory<Buku, Integer>("tahun_terbit"));
//         // tableView.setItems(listBuku);
//         tableView.getItems().add(new Buku(1, "Buku A", "Erlangga", "Joko", 2024));
//         tableView.getItems().add(new Buku(2, "Buku B", "Andik Offside", "Dino", 2024));
//         tableView.getItems().add(new Buku(3, "Buku C", "ElekMedia", "Jojon", 2024));
//         sp.getChildren().add(tableView);
//         return sp;
//     }

//     GridPane gpFormBuku() {
//         flagEdit = false;
//         GridPane gp = new GridPane();
//         gp.setPrefHeight(500);
//         gp.setAlignment(Pos.CENTER);
//         gp.setVgap(10);
//         gp.setHgap(10);
//         gp.setPadding(new Insets(10, 10, 10, 10));
//         Label lbJudulForm = new Label("Form Data Buku");
//         Label lbIdBuku = new Label("Id Buku");
//         Label lbJudul = new Label("Judul");
//         Label lbPenerbit = new Label("Penerbit");
//         Label lbPenulis = new Label("Penulis");
//         Label lbTahunTerbit = new Label("Tahun Terbit");
//         tfIdBuku = new TextField();
//         tfJudul = new TextField();
//         tfPenerbit = new TextField();
//         tfPenulis = new TextField();
//         tfTahunTerbit = new TextField();
//         bUpdate = new Button("Update");
//         bCancel = new Button("Cancel");
//         bAdd = new Button("Add");
//         bEdit = new Button("Edit");
//         bDel = new Button("Del");
//         bAdd.setPrefWidth(100);
//         bEdit.setPrefWidth(100);
//         bDel.setPrefWidth(100);
//         bUpdate.setPrefWidth(100);
//         bCancel.setPrefWidth(100);
//         bUpdate.setOnAction(e -> {
//             int idBuku, tahunTerbit;
//             String judul, penulis, penerbit;
//             idBuku = Integer.parseInt(tfIdBuku.getText());
//             judul = tfJudul.getText();
//             penulis = tfPenulis.getText();
//             penerbit = tfPenerbit.getText();
//             tahunTerbit = Integer.parseInt(tfTahunTerbit.getText());
//             Buku b = new Buku(idBuku, judul, penerbit, penulis, tahunTerbit);
//             if (flagEdit == false) {
//                 tableView.getItems().add(b);
//             } else {
//                 int idx = tableView.getSelectionModel().getSelectedIndex();
//                 tableView.getItems().set(idx, b);
//             }
//             teksAktif(false);
//             buttonAktif(false);
//             clearTeks();
//             flagEdit = true;
//         });
//         bEdit.setOnAction(e -> {
//             buttonAktif(true);
//             teksAktif(true);
//             flagEdit = true;
//             int idx = tableView.getSelectionModel().getSelectedIndex();
//             tfIdBuku.setText(String.valueOf(tableView.getItems().get(idx).getIdBuku()));
//             tfJudul.setText(tableView.getItems().get(idx).getJudul());
//             tfPenerbit.setText(tableView.getItems().get(idx).getPenulis());
//             tfPenulis.setText(tableView.getItems().get(idx).getPenulis());
//             tfTahunTerbit.setText(String.valueOf(tableView.getItems().get(idx).getTahun_terbit()));
//         });
//         bAdd.setOnAction(e -> {
//             flagEdit = false;
//             clearTeks();
//             teksAktif(true);
//             buttonAktif(true);
//         });
//         bCancel.setOnAction(e -> {
//             teksAktif(false);
//             buttonAktif(false);
//         });
//         bDel.setOnAction(e -> {
//             int idx = tableView.getSelectionModel().getSelectedIndex();
//             tableView.getItems().remove(idx);
//             clearTeks();
//         });
//         TilePane tp1 = new TilePane();
//         // TilePane tp2 = new TilePane();
//         tp1.getChildren().addAll(bAdd, bEdit, bDel, bUpdate, bCancel);
//         // tp2.getChildren().addAll(bUpdate,bCancel);
//         gp.addRow(0, new Label(""), lbJudulForm);
//         gp.addRow(1, lbIdBuku, tfIdBuku);
//         gp.addRow(2, lbJudul, tfJudul);
//         gp.addRow(3, lbPenerbit, tfPenerbit);
//         gp.addRow(4, lbPenulis, tfPenulis);
//         gp.addRow(5, lbTahunTerbit, tfTahunTerbit);
//         gp.addRow(6, new Label(""), tp1);
//         teksAktif(false);
//         buttonAktif(false);
//         return gp;
//     }

//     private VBox addVBoxBuku() {
//         VBox vb = new VBox();
//         Text tjudul = new Text("Form Data Buku");
//         tjudul.setFont(Font.font("Arial", 18));
//         vb.getChildren().add(tjudul);
//         vb.setAlignment(Pos.CENTER);
//         vb.getChildren().add(spTableBuku());
//         vb.getChildren().add(gpFormBuku());
//         return vb;
//     }

//     StackPane spTablePustakawan() {
//         StackPane sp = new StackPane();
//         tableViewPustakawan = new TableView<Pustakawan>();
//         idPustakawan = new TableColumn<>("ID Pustakawan");
//         namaPustakawan = new TableColumn<>("Nama Pustakawan");
//         alamatPustakawan = new TableColumn<>("Alamat Pustakawan");
//         tableViewPustakawan.getColumns().addAll(idPustakawan, namaPustakawan, alamatPustakawan);
//         idPustakawan.setCellValueFactory(new PropertyValueFactory<Pustakawan, Integer>("idPustakawan"));
//         namaPustakawan.setCellValueFactory(new PropertyValueFactory<Pustakawan, String>("namaPustakawan"));
//         alamatPustakawan.setCellValueFactory(new PropertyValueFactory<Pustakawan, String>("alamatPustakawan"));
//         tableViewPustakawan.getItems().add(new Pustakawan(0, "Lukman", "Jl. Raya"));
//         sp.getChildren().add(tableViewPustakawan);
//         return sp;
//     }

//     GridPane gpFormPustakawan() {
//         flagEditPustakawan = false;
//         GridPane gp = new GridPane();
//         gp.setPrefHeight(500);
//         gp.setAlignment(Pos.CENTER);
//         gp.setVgap(10);
//         gp.setHgap(10);
//         gp.setPadding(new Insets(10, 10, 10, 10));
//         Label lbJudulForm = new Label("Form Data Pustakawan");
//         Label lbIdPustakawan = new Label("Id Pustakawan");
//         Label lbNamaPustakawan = new Label("Nama Pustakawan");
//         Label lbAlamatPustakawan = new Label("Alamat Pustakawan");
//         tfIdPustakawan = new TextField();
//         tfNamaPustakawan = new TextField();
//         tfAlamatPustakawan = new TextField();
//         bUpdatePustakawan = new Button("Update");
//         bCancelPustakawan = new Button("Cancel");
//         bAddPustakawan = new Button("Add");
//         bEditPustakawan = new Button("Edit");
//         bDelPustakawan = new Button("Del");
//         bAddPustakawan.setPrefWidth(100);
//         bEditPustakawan.setPrefWidth(100);
//         bDelPustakawan.setPrefWidth(100);
//         bUpdatePustakawan.setPrefWidth(100);
//         bCancelPustakawan.setPrefWidth(100);

//         bUpdatePustakawan.setOnAction(e -> {
//             int idPustakawan;
//             String namaPustakawan, alamatPustakawan;
//             idPustakawan = Integer.parseInt(tfIdPustakawan.getText());
//             namaPustakawan = tfNamaPustakawan.getText();
//             alamatPustakawan = tfAlamatPustakawan.getText();
//             Pustakawan p = new Pustakawan(idPustakawan, namaPustakawan, alamatPustakawan);
//             if (flagEditPustakawan == false) {
//                 tableViewPustakawan.getItems().add(p);
//             } else {
//                 int idx = tableViewPustakawan.getSelectionModel().getSelectedIndex();
//                 tableViewPustakawan.getItems().set(idx, p);
//             }
//             teksAktifPustakawan(false);
//             buttonAktifPustakawan(false);
//             clearTeksPustakawan();
//             flagEditPustakawan = true;
//         });

//         bEditPustakawan.setOnAction(e -> {
//             buttonAktifPustakawan(true);
//             teksAktifPustakawan(true);
//             flagEditPustakawan = true;
//             int idx = tableViewPustakawan.getSelectionModel().getSelectedIndex();
//             tfIdPustakawan.setText(String.valueOf(tableViewPustakawan.getItems().get(idx).getIdPustakawan()));
//             tfNamaPustakawan.setText(tableViewPustakawan.getItems().get(idx).getNamaPustakawan());
//             tfAlamatPustakawan.setText(tableViewPustakawan.getItems().get(idx).getAlamatPustakawan());
//         });

//         bAddPustakawan.setOnAction(e -> {
//             flagEditPustakawan = false;
//             clearTeksPustakawan();
//             teksAktifPustakawan(true);
//             buttonAktifPustakawan(true);
//         });

//         bCancelPustakawan.setOnAction(e -> {
//             teksAktifPustakawan(false);
//             buttonAktifPustakawan(false);
//         });

//         bDelPustakawan.setOnAction(e -> {
//             int idx = tableViewPustakawan.getSelectionModel().getSelectedIndex();
//             tableViewPustakawan.getItems().remove(idx);
//             clearTeksPustakawan();
//         });

//         TilePane tp1 = new TilePane();
//         tp1.getChildren().addAll(bAddPustakawan, bEditPustakawan, bDelPustakawan, bUpdatePustakawan, bCancelPustakawan);
//         gp.addRow(0, new Label(""), lbJudulForm);
//         gp.addRow(1, lbIdPustakawan, tfIdPustakawan);
//         gp.addRow(2, lbNamaPustakawan, tfNamaPustakawan);
//         gp.addRow(3, lbAlamatPustakawan, tfAlamatPustakawan);
//         gp.addRow(4, new Label(""), tp1);
//         teksAktifPustakawan(false);
//         buttonAktifPustakawan(false);
//         return gp;
//     }

//     private VBox addVBoxPustakawan() {
//         VBox vb = new VBox();
//         Text tjudul = new Text("Data Pustakawan");
//         tjudul.setFont(Font.font("Arial", 18));
//         vb.getChildren().add(tjudul);
//         vb.setAlignment(Pos.CENTER);
//         vb.getChildren().add(spTablePustakawan());
//         vb.getChildren().add(gpFormPustakawan());
//         return vb;
//     }

//     StackPane spTableAnggota() {
//         StackPane sp = new StackPane();
//         tableViewAnggota = new TableView<Anggota>();
//         idAnggota = new TableColumn<>("ID Anggota");
//         namaAnggota = new TableColumn<>("Nama Anggota");
//         alamatAnggota = new TableColumn<>("Alamat Anggota");
//         tableViewAnggota.getColumns().addAll(idAnggota, namaAnggota, alamatAnggota);
//         idAnggota.setCellValueFactory(new PropertyValueFactory<Anggota, Integer>("idAnggota"));
//         namaAnggota.setCellValueFactory(new PropertyValueFactory<Anggota, String>("namaAnggota"));
//         alamatAnggota.setCellValueFactory(new PropertyValueFactory<Anggota, String>("alamatAnggota"));
//         tableViewAnggota.getItems().add(new Anggota(0, "Joko", "Jl. Anggrek"));
//         sp.getChildren().add(tableViewAnggota);
//         return sp;
//     }

//     GridPane gpFormAnggota() {
//         flagEditAnggota = false;
//         GridPane gp = new GridPane();
//         gp.setPrefHeight(500);
//         gp.setAlignment(Pos.CENTER);
//         gp.setVgap(10);
//         gp.setHgap(10);
//         gp.setPadding(new Insets(10, 10, 10, 10));
//         Label lbJudulForm = new Label("Form Data Anggota");
//         Label lbIdAnggota = new Label("Id Anggota");
//         Label lbNamaAnggota = new Label("Nama Anggota");
//         Label lbAlamatAnggota = new Label("Alamat Anggota");
//         tfIdAnggota = new TextField();
//         tfNamaAnggota = new TextField();
//         tfAlamatAnggota = new TextField();
//         bUpdateAnggota = new Button("Update");
//         bCancelAnggota = new Button("Cancel");
//         bAddAnggota = new Button("Add");
//         bEditAnggota = new Button("Edit");
//         bDelAnggota = new Button("Del");
//         bAddAnggota.setPrefWidth(100);
//         bEditAnggota.setPrefWidth(100);
//         bDelAnggota.setPrefWidth(100);
//         bUpdateAnggota.setPrefWidth(100);
//         bCancelAnggota.setPrefWidth(100);
//         bUpdateAnggota.setOnAction(e -> {
//             int idAnggota;
//             String namaAnggota, alamatAnggota;
//             idAnggota = Integer.parseInt(tfIdAnggota.getText());
//             namaAnggota = tfNamaAnggota.getText();
//             alamatAnggota = tfAlamatAnggota.getText();
//             Anggota a = new Anggota(idAnggota, namaAnggota, alamatAnggota);
//             if (flagEditAnggota == false) {
//                 tableViewAnggota.getItems().add(a);
//             } else {
//                 int idx = tableViewAnggota.getSelectionModel().getSelectedIndex();
//                 tableViewAnggota.getItems().set(idx, a);
//             }

//             teksAktifAnggota(false);
//             buttonAktifAnggota(false);
//             clearTeksAnggota();
//             flagEditAnggota = true;
//         });

//         bEditAnggota.setOnAction(e -> {
//             teksAktifAnggota(true);
//             buttonAktifAnggota(true);
//             flagEditAnggota = true;
//             int idx = tableViewAnggota.getSelectionModel().getSelectedIndex();
//             tfIdAnggota.setText(String.valueOf(tableViewAnggota.getItems().get(idx).getIdAnggota()));
//             tfNamaAnggota.setText(tableViewAnggota.getItems().get(idx).getNamaAnggota());
//             tfAlamatAnggota.setText(tableViewAnggota.getItems().get(idx).getAlamatAnggota());
//         });

//         bAddAnggota.setOnAction(e -> {
//             flagEditAnggota = false;
//             clearTeksAnggota();
//             teksAktifAnggota(true);
//             buttonAktifAnggota(true);
//         });

//         bCancelAnggota.setOnAction(e -> {
//             teksAktifAnggota(false);
//             buttonAktifAnggota(false);
//         });

//         bDelAnggota.setOnAction(e -> {
//             int idx = tableViewAnggota.getSelectionModel().getSelectedIndex();
//             tableViewAnggota.getItems().remove(idx);
//             clearTeksAnggota();
//         });

//        TilePane tp1 = new TilePane();
//        tp1.getChildren().addAll(bAddAnggota, bEditAnggota, bDelAnggota, bUpdateAnggota, bCancelAnggota);
//        gp.addRow(0, new Label(""), lbJudulForm);
//        gp.addRow(1, lbIdAnggota, tfIdAnggota);
//        gp.addRow(2, lbNamaAnggota, tfNamaAnggota);
//        gp.addRow(3, lbAlamatAnggota, tfAlamatAnggota);
//        gp.addRow(4, new Label(""), tp1);
//        teksAktifAnggota(false);
//        buttonAktifAnggota(false);
//         return gp;
//     }

//     private VBox addVBoxAnggota() {
//         VBox vbx = new VBox();
//         Text tjudul = new Text("Anggota");
//         tjudul.setFont(Font.font("Arial", 18));
//         vbx.getChildren().add(tjudul);
//         vbx.setAlignment(Pos.CENTER);
//         vbx.getChildren().add(spTableAnggota());
//         vbx.getChildren().add(gpFormAnggota());
//         return vbx;
//     }

//     public void teksAktifAnggota(boolean aktif) {
//         tfIdAnggota.setEditable(aktif);
//         tfNamaAnggota.setEditable(aktif);
//         tfAlamatAnggota.setEditable(aktif);
//     }

//     public void buttonAktifAnggota(boolean nonAktif) {
//         bAddAnggota.setDisable(nonAktif);
//         bEditAnggota.setDisable(nonAktif);
//         bDelAnggota.setDisable(nonAktif);
//         bUpdateAnggota.setDisable(!nonAktif);
//         bCancelAnggota.setDisable(!nonAktif);
//     }

//     public void clearTeksAnggota() {
//         tfIdAnggota.setText("");
//         tfNamaAnggota.setText("");
//         tfAlamatAnggota.setText("");
//     }

//     public void teksAktifPustakawan(boolean aktif) {
//         tfIdPustakawan.setEditable(aktif);
//         tfNamaPustakawan.setEditable(aktif);
//         tfAlamatPustakawan.setEditable(aktif);
//     }

//     public void buttonAktifPustakawan(boolean nonAktif) {
//         bAddPustakawan.setDisable(nonAktif);
//         bEditPustakawan.setDisable(nonAktif);
//         bDelPustakawan.setDisable(nonAktif);
//         bUpdatePustakawan.setDisable(!nonAktif);
//         bCancelPustakawan.setDisable(!nonAktif);
//     }

//     public void clearTeksPustakawan() {
//         tfIdPustakawan.setText("");
//         tfNamaPustakawan.setText("");
//         tfAlamatPustakawan.setText("");
//     }

//     public void buttonAktif(boolean nonAktif) {
//         bAdd.setDisable(nonAktif);
//         bEdit.setDisable(nonAktif);
//         bDel.setDisable(nonAktif);
//         bUpdate.setDisable(!nonAktif);
//         bCancel.setDisable(!nonAktif);
//     }

//     public void teksAktif(boolean aktif) {
//         tfIdBuku.setEditable(aktif);
//         tfJudul.setEditable(aktif);
//         tfPenerbit.setEditable(aktif);
//         tfPenulis.setEditable(aktif);
//         tfTahunTerbit.setEditable(aktif);
//     }

//     public void clearTeks() {
//         tfIdBuku.setText("");
//         tfJudul.setText("");
//         tfPenerbit.setText("");
//         tfPenulis.setText("");
//         tfTahunTerbit.setText("");
//     }
// }