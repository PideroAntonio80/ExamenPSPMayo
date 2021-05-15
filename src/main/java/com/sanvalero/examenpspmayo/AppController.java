package com.sanvalero.examenpspmayo;

import com.sanvalero.examenpspmayo.domain.Job;
import com.sanvalero.examenpspmayo.service.JobService;
import com.sanvalero.examenpspmayo.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Creado por @ author: Pedro Orós
 * el 14/05/2021
 */
public class AppController implements Initializable {

    public Label lTitle, lLocation, lType;
    public TextField tfLocLeng;
    public TextArea taDescription;
    public CheckBox chbFull;
    public TableView<Job> tvData;
    public ComboBox<String> cbChoose;
    public WebView wvImage;
    public WebEngine engine;

    private ObservableList<Job> jobList;
    private List<Job> list;

    private JobService jobService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        putTableColumnsSw();

        String[] options = new String[]{"<<All>>", "Localidad",  "Lenguaje programacion"};
        cbChoose.setValue("<<All>>");
        cbChoose.setItems(FXCollections.observableArrayList(options));

        engine = wvImage.getEngine();

        jobService = new JobService();

        jobList = FXCollections.observableArrayList();
        list = new ArrayList<>();

        tvData.setItems(jobList);

        loadingAllData();
    }

    public void putTableColumnsSw() {
        Field[] fields = Job.class.getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equals("type") || field.getName().equals("location") || field.getName().equals("description") || field.getName().equals("company_logo")) continue;

            TableColumn<Job, String> column = new TableColumn<>(field.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            tvData.getColumns().add(column);
        }
        tvData.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void show() {

        if(chbFull.isSelected()) {
            String type = "Full Time";
            jobsByType(type);

        } else {
            String option = cbChoose.getValue();

            switch (option) {
                case "<<All>>":
                    loadingAllData();
                    break;

                case "Localidad":
                    String loc = tfLocLeng.getText();
                    jobsByLocation(loc);
                    break;

                case "Lenguaje programacion":
                    String language = tfLocLeng.getText();
                    jobsByLanguage(language);
                    break;
            }
        }
    }

    @FXML
    public void showData() {
        Job job = tvData.getSelectionModel().getSelectedItem();

        String image = job.getCompany_logo();

        engine.load(image);

        lTitle.setText(job.getTitle());
        lLocation.setText(job.getLocation());
        lType.setText(job.getType());
        taDescription.setWrapText(true);
        taDescription.setMaxSize(305,115);
        taDescription.setText(job.getDescription());
    }

    @FXML
    public void csv() {
        export();
        AlertUtils.mostrarInformacion("Datos transferidos a fichero");
    }

    public void loadingAllData() {
        jobList.clear();
        list.clear();
        //tvData.setItems(jobList);

        jobService.getAllJobs()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de trabajos descargado"))
                .doOnError(throwable -> System.out.println(throwable.getMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(job -> {
                    jobList.add(job);
                    list.add(job);
                });
    }

    public void jobsByLocation(String location) {
        jobList.clear();
        list.clear();

        jobService.getJobsByLocation(location)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de trabajos descargado"))
                .doOnError(throwable -> System.out.println(throwable.getMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(job -> {
                    jobList.add(job);
                    list.add(job);
                });

    }

    public void jobsByLanguage(String language) {
        jobList.clear();
        list.clear();

        jobService.getJobsByDescription(language)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de trabajos descargado"))
                .doOnError(throwable -> System.out.println(throwable.getMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(job -> {
                    jobList.add(job);
                    list.add(job);
                });
    }

    public void jobsByType(String type) {
        jobList.clear();
        list.clear();

        jobService.getJobsByType(type)
                .flatMap(Observable::from)
                .filter(job -> job.getType().equals(type))
                .doOnCompleted(() -> System.out.println("Listado de trabajos descargado"))
                .doOnError(throwable -> System.out.println(throwable.getMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(job -> {
                    jobList.add(job);
                    list.add(job);
                });
    }

    public void export() {
        File file = null;
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showSaveDialog(null);

        try {
            FileWriter fileWriter = new FileWriter(file + ".csv");
            CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.TDF.withHeader("TITULO;"));
            for (Job job : list) {
                printer.printRecord(job.getTitle());
            }
            printer.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            AlertUtils.mostrarError("Error al exportar los datos");
        }
    }
}
