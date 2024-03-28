package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.Impound.ImpoundLogEntry;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.logs.Incident.IncidentLogEntry;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import com.drozal.dataterminal.logs.Patrol.PatrolLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import com.drozal.dataterminal.logs.Search.SearchLogEntry;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopLogEntry;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportLogs;
import com.drozal.dataterminal.util.ResizeHelper;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.util.controllerUtils.*;
import static com.drozal.dataterminal.util.treeViewUtils.expandTreeItem;
import static com.drozal.dataterminal.util.treeViewUtils.parseTreeXML;
import static com.drozal.dataterminal.util.windowUtils.toggleWindowedFullscreen;

public class actionController {

    //<editor-fold desc="FXML Elements">
    @javafx.fxml.FXML
    public Button notesButton;
    @javafx.fxml.FXML
    public Button shiftInfoBtn;
    @javafx.fxml.FXML
    public AnchorPane shiftInformationPane;
    @javafx.fxml.FXML
    public TextField OfficerInfoName;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoDivision;
    @javafx.fxml.FXML
    public TextArea ShiftInfoNotesTextArea;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoAgency;
    @javafx.fxml.FXML
    public TextField OfficerInfoCallsign;
    @javafx.fxml.FXML
    public TextField OfficerInfoNumber;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoRank;
    @javafx.fxml.FXML
    public Label generatedDateTag;
    @javafx.fxml.FXML
    public Label generatedByTag;
    @javafx.fxml.FXML
    public AnchorPane infoPane;
    @javafx.fxml.FXML
    public Label updatedNotification;
    @javafx.fxml.FXML
    public AnchorPane vbox;
    @javafx.fxml.FXML
    public AnchorPane UISettingsPane;
    @javafx.fxml.FXML
    public BarChart reportChart;
    @javafx.fxml.FXML
    public ColorPicker colorSelectMain;
    @javafx.fxml.FXML
    public AnchorPane topPane;
    @javafx.fxml.FXML
    public AnchorPane sidepane;
    @javafx.fxml.FXML
    public ColorPicker colorSelectSecondary;
    @javafx.fxml.FXML
    public Label mainColor8;
    @javafx.fxml.FXML
    public Label mainColor9Bkg;
    @javafx.fxml.FXML
    public Button updateInfoBtn;
    @javafx.fxml.FXML
    public MenuButton settingsDropdown;
    @javafx.fxml.FXML
    public Button resetDefaultsBtn;
    @javafx.fxml.FXML
    public Label primaryColor;
    @javafx.fxml.FXML
    public Label secondaryColor;
    @javafx.fxml.FXML
    public AnchorPane calloutReportPane;
    private double xOffset = 0;
    private double yOffset = 0;
    private MedicalInformation medicalInformationController;
    @javafx.fxml.FXML
    private Label secondaryColor2;
    @javafx.fxml.FXML
    private Label secondaryColor5;
    @javafx.fxml.FXML
    private Label secondaryColor3;
    @javafx.fxml.FXML
    private Label secondaryColor4;
    @javafx.fxml.FXML
    private Label secondaryColor3Bkg;
    @javafx.fxml.FXML
    private Label mainColor6;
    @javafx.fxml.FXML
    private Label mainColor7Bkg;
    @javafx.fxml.FXML
    private Label secondaryColor4Bkg;
    @javafx.fxml.FXML
    private Label secondaryColor5Bkg;
    @javafx.fxml.FXML
    private Button logsButton;
    @javafx.fxml.FXML
    private Button mapButton;
    @javafx.fxml.FXML
    private MenuButton createReportBtn;
    @javafx.fxml.FXML
    private TextField calloutReportResponseCounty;
    @javafx.fxml.FXML
    private MenuItem parkingCitationReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportName;
    @javafx.fxml.FXML
    private Label calloutincompleteLabel;
    @javafx.fxml.FXML
    private MenuItem searchReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportAgency;
    @javafx.fxml.FXML
    private MenuItem trafficReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportResponseAddress;
    @javafx.fxml.FXML
    private MenuItem impoundReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportResponseGrade;
    @javafx.fxml.FXML
    private TextArea calloutReportNotesTextArea;
    @javafx.fxml.FXML
    private Button calloutReportSubmitBtn;
    @javafx.fxml.FXML
    private MenuItem incidentReportButton;
    @javafx.fxml.FXML
    private MenuItem patrolReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportTime;
    @javafx.fxml.FXML
    private MenuItem calloutReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportDivision;
    @javafx.fxml.FXML
    private Spinner calloutReportSpinner;
    @javafx.fxml.FXML
    private TextField calloutReportResponeType;
    @javafx.fxml.FXML
    private TextField calloutReportResponseArea;
    @javafx.fxml.FXML
    private MenuItem arrestReportButton;
    @javafx.fxml.FXML
    private MenuItem trafficCitationReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportNumber;
    @javafx.fxml.FXML
    private TextField calloutReportDate;
    @javafx.fxml.FXML
    private TextField calloutReportRank;
    @javafx.fxml.FXML
    private TextField patrolDate;
    @javafx.fxml.FXML
    private TextField patrolLength;
    @javafx.fxml.FXML
    private TextField patrolofficerNumber;
    @javafx.fxml.FXML
    private TextField patrolStopTime;
    @javafx.fxml.FXML
    private AnchorPane patrolReportPane;
    @javafx.fxml.FXML
    private TextField patrolofficerDivision;
    @javafx.fxml.FXML
    private Label patrolincompleteLabel;
    @javafx.fxml.FXML
    private TextField patrolofficerVehicle;
    @javafx.fxml.FXML
    private TextField patrolStartTime;
    @javafx.fxml.FXML
    private TextArea patrolComments;
    @javafx.fxml.FXML
    private Spinner patrolSpinnerNumber;
    @javafx.fxml.FXML
    private TextField patrolofficerRank;
    @javafx.fxml.FXML
    private TextField patrolofficerAgency;
    @javafx.fxml.FXML
    private TextField patrolofficerName;
    @javafx.fxml.FXML
    private Button incidentReportSubmitBtn;
    @javafx.fxml.FXML
    private TextArea incidentComments;
    @javafx.fxml.FXML
    private TextField incidentofficerName;
    @javafx.fxml.FXML
    private TextField incidentofficerAgency;
    @javafx.fxml.FXML
    private AnchorPane incidentReportPane;
    @javafx.fxml.FXML
    private TextField incidentofficerDivision;
    @javafx.fxml.FXML
    private TextField incidentVictims;
    @javafx.fxml.FXML
    private TextField incidentofficerRank;
    @javafx.fxml.FXML
    private TextField incidentCounty;
    @javafx.fxml.FXML
    private TextField incidentWitnesses;
    @javafx.fxml.FXML
    private TextArea incidentStatement;
    @javafx.fxml.FXML
    private TextField incidentofficerNumber;
    @javafx.fxml.FXML
    private TextField incidentStreet;
    @javafx.fxml.FXML
    private TextArea incidentActionsTaken;
    @javafx.fxml.FXML
    private Label incidentincompleteLabel;
    @javafx.fxml.FXML
    private TextField incidentArea;
    @javafx.fxml.FXML
    private TextField incidentReportTime;
    @javafx.fxml.FXML
    private Spinner incidentNumber;
    @javafx.fxml.FXML
    private TextField incidentReportdate;
    @javafx.fxml.FXML
    private Button searchReportSubmitBtn;
    @javafx.fxml.FXML
    private TextArea searchComments;
    @javafx.fxml.FXML
    private TextField searchTime;
    @javafx.fxml.FXML
    private TextField searchofficerName;
    @javafx.fxml.FXML
    private Spinner SearchNumber;
    @javafx.fxml.FXML
    private TextField searchofficerDivision;
    @javafx.fxml.FXML
    private TextField searchWitnesses;
    @javafx.fxml.FXML
    private Button searchpopOverBtn;
    @javafx.fxml.FXML
    private AnchorPane searchReportPane;
    @javafx.fxml.FXML
    private TextField searchCounty;
    @javafx.fxml.FXML
    private TextField searchedPersons;
    @javafx.fxml.FXML
    private ComboBox searchType;
    @javafx.fxml.FXML
    private TextField searchofficerRank;
    @javafx.fxml.FXML
    private TextField searchofficerNumber;
    @javafx.fxml.FXML
    private ComboBox searchMethod;
    @javafx.fxml.FXML
    private Label searchincompleteLabel;
    @javafx.fxml.FXML
    private TextField searchArea;
    @javafx.fxml.FXML
    private TextField searchStreet;
    @javafx.fxml.FXML
    private TextField searchDate;
    @javafx.fxml.FXML
    private TextField searchGrounds;
    @javafx.fxml.FXML
    private TextArea searchSeizedItems;
    @javafx.fxml.FXML
    private TextField searchofficerAgency;
    private DUIInformationController duiInformationController;
    @javafx.fxml.FXML
    private ComboBox impoundColor;
    @javafx.fxml.FXML
    private TextField impoundofficerDivision;
    @javafx.fxml.FXML
    private TextField impoundownerGender;
    @javafx.fxml.FXML
    private TextField impoundofficerNumber;
    @javafx.fxml.FXML
    private TextArea impoundComments;
    @javafx.fxml.FXML
    private ComboBox impoundType;
    @javafx.fxml.FXML
    private TextField impoundDate;
    @javafx.fxml.FXML
    private TextField impoundownerAge;
    @javafx.fxml.FXML
    private TextField impoundofficerAgency;
    @javafx.fxml.FXML
    private TextField impoundofficerRank;
    @javafx.fxml.FXML
    private Spinner impoundNumber;
    @javafx.fxml.FXML
    private AnchorPane impoundReportPane;
    @javafx.fxml.FXML
    private TextField impoundownerName;
    @javafx.fxml.FXML
    private Label impoundincompleteLabel;
    @javafx.fxml.FXML
    private TextField impoundModel;
    @javafx.fxml.FXML
    private TextField impoundPlateNumber;
    @javafx.fxml.FXML
    private TextField impoundownerAddress;
    @javafx.fxml.FXML
    private TextField impoundTime;
    @javafx.fxml.FXML
    private TextField impoundofficerName;
    @javafx.fxml.FXML
    private Label trafficStopIncompleteLabel;
    @javafx.fxml.FXML
    private AnchorPane trafficStopReportPane;
    @javafx.fxml.FXML
    private TextField trafficStopCounty;
    @javafx.fxml.FXML
    private TextField trafficStopofficerNumber;
    @javafx.fxml.FXML
    private ComboBox trafficStopColor;
    @javafx.fxml.FXML
    private TextField trafficStopownerGender;
    @javafx.fxml.FXML
    private Spinner trafficStopNumber;
    @javafx.fxml.FXML
    private TextArea trafficStopComments;
    @javafx.fxml.FXML
    private ComboBox trafficStopType;
    @javafx.fxml.FXML
    private TextField trafficStopownerAge;
    @javafx.fxml.FXML
    private TextField trafficStopOtherInfo;
    @javafx.fxml.FXML
    private TextField trafficStopownerName;
    @javafx.fxml.FXML
    private TextField trafficStopofficerDivision;
    @javafx.fxml.FXML
    private TextField trafficStopModel;
    @javafx.fxml.FXML
    private TextField trafficStopofficerAgency;
    @javafx.fxml.FXML
    private TextField trafficStopTime;
    @javafx.fxml.FXML
    private TextField trafficStopownerDescription;
    @javafx.fxml.FXML
    private TextField trafficStopArea;
    @javafx.fxml.FXML
    private TextField trafficStopofficerRank;
    @javafx.fxml.FXML
    private TextField trafficStopPlateNumber;
    @javafx.fxml.FXML
    private TextField trafficStopDate;
    @javafx.fxml.FXML
    private TextField trafficStopofficerName;
    @javafx.fxml.FXML
    private TextField trafficStopStreet;
    @javafx.fxml.FXML
    private TextField trafficStopownerAddress;
    @javafx.fxml.FXML
    private TextField arrestProbationChance;
    @javafx.fxml.FXML
    private TextField arrestMinMonths;
    @javafx.fxml.FXML
    private TableView arrestChargeTable;
    @javafx.fxml.FXML
    private TextField arrestChargeField;
    @javafx.fxml.FXML
    private TextArea arrestComments;
    @javafx.fxml.FXML
    private TextField arrestOwnerAge;
    @javafx.fxml.FXML
    private AnchorPane arrestYearsPane;
    @javafx.fxml.FXML
    private TableColumn arrestChargeColumn;
    @javafx.fxml.FXML
    private TextField arrestMaxMonths;
    @javafx.fxml.FXML
    private TextField arrestMaxSuspension;
    @javafx.fxml.FXML
    private TextField arrestOfficerRank;
    @javafx.fxml.FXML
    private TextField arrestMinSuspension;
    @javafx.fxml.FXML
    private TextField arrestOwnerGender;
    @javafx.fxml.FXML
    private AnchorPane arrestMonthsPane;
    @javafx.fxml.FXML
    private TextField arrestOwnerAddress;
    @javafx.fxml.FXML
    private AnchorPane arrestReportPane;
    @javafx.fxml.FXML
    private TextField arrestDate;
    @javafx.fxml.FXML
    private TextField arrestOwnerName;
    @javafx.fxml.FXML
    private TextField arrestOfficerName;
    @javafx.fxml.FXML
    private TreeView arrestTreeView;
    @javafx.fxml.FXML
    private Spinner arrestNumber;
    @javafx.fxml.FXML
    private TextField arrestOfficerNumber;
    @javafx.fxml.FXML
    private Label arrestIncompleteLabel;
    @javafx.fxml.FXML
    private TextField arrestStreet;
    @javafx.fxml.FXML
    private TextField arrestOfficerAgency;
    @javafx.fxml.FXML
    private TextField arrestArea;
    @javafx.fxml.FXML
    private TextField arrestOwnerDescription;
    @javafx.fxml.FXML
    private TextField arrestTime;
    @javafx.fxml.FXML
    private TextField arrestOfficerDivision;
    @javafx.fxml.FXML
    private TextField arrestCounty;
    @javafx.fxml.FXML
    private TextField arrestMinYears;
    @javafx.fxml.FXML
    private ScrollPane arrestChargeScrollPane;
    @javafx.fxml.FXML
    private TextField arrestMaxYears;
    @javafx.fxml.FXML
    private Button arrestMedInfoBtn;
    @javafx.fxml.FXML
    private AnchorPane arrestTrafficChargeInfoPane;
    @javafx.fxml.FXML
    private TitledPane arrestAccordionInformation;
    //</editor-fold>

    //Initialization
    public void initialize() throws IOException {
        setDisable(infoPane, UISettingsPane, patrolReportPane, calloutReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(shiftInformationPane);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/DUIInformation.fxml"));
        loader.load();
        duiInformationController = loader.getController();

        refreshChart();
        updateChartIfMismatch(reportChart);
        loadTheme();

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        getOfficerInfoRank().getItems().addAll(dropdownInfo.ranks);
        getOfficerInfoDivision().getItems().addAll(dropdownInfo.divisions);
        getOfficerInfoDivision().setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                            setAlignment(javafx.geometry.Pos.CENTER);
                        }
                    }
                };
            }
        });
        getOfficerInfoAgency().getItems().addAll(dropdownInfo.agencies);

        OfficerInfoName.setText(name);
        OfficerInfoDivision.setValue(division);
        OfficerInfoRank.setValue(rank);
        OfficerInfoAgency.setValue(agency);
        OfficerInfoNumber.setText(number);
        generatedByTag.setText("Generated By:" + " " + name);
        String time = DataTerminalHomeApplication.getTime();
        generatedDateTag.setText("Generated at: " + time);
    }

    //<editor-fold desc="Utils">
    private void showSettingsWindow() {
        // Create a new stage for the settings window
        Stage settingsStage = new Stage();
        settingsStage.setTitle("UI Settings");

        // Create color pickers for selecting colors
        Label primColor = primaryColor;
        Label secColor = secondaryColor;

        ColorPicker colorPicker1 = colorSelectMain;
        ColorPicker colorPicker2 = colorSelectSecondary;

        Button saveButton = resetDefaultsBtn;

        // Create a GridPane layout for the settings window
        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(15);
        root.setVgap(15);
        root.addRow(0, primColor);
        root.addRow(1, colorPicker1);
        root.addRow(2, secColor);
        root.addRow(3, colorPicker2);
        root.add(saveButton, 0, 4);

        // Create the scene for the settings window
        Scene scene = new Scene(root);

        // Set the scene on the settings stage
        settingsStage.setScene(scene);

        // Show the settings window
        settingsStage.initStyle(StageStyle.DECORATED);
        settingsStage.setResizable(false);
        settingsStage.show();
    }

    private void loadTheme() throws IOException {
        colorSelectMain.setValue(Color.valueOf(ConfigReader.configRead("mainColor")));
        colorSelectSecondary.setValue(Color.valueOf(ConfigReader.configRead("secondaryColor")));
        changeBarColors(getReportChart(), ConfigReader.configRead("mainColor"));
        //Main
        String mainclr = ConfigReader.configRead("mainColor");
        topPane.setStyle("-fx-background-color: " + mainclr + ";");
        mainColor6.setStyle("-fx-text-fill: " + mainclr + ";");
        mainColor8.setStyle("-fx-text-fill: " + mainclr + ";");
        mainColor7Bkg.setStyle("-fx-background-color: " + mainclr + ";");
        mainColor9Bkg.setStyle("-fx-background-color: " + mainclr + ";");
        primaryColor.setStyle("-fx-text-fill: " + mainclr + ";");
        //Secondary
        String secclr = ConfigReader.configRead("secondaryColor");
        secondaryColor.setStyle("-fx-text-fill: " + secclr + ";");
        sidepane.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor2.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor3.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor4.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor5.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor3Bkg.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor4Bkg.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor5Bkg.setStyle("-fx-background-color: " + secclr + ";");
        //Buttons
        String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
        String initialStyle = "-fx-background-color: transparent;";
        String nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";";
        updateInfoBtn.setStyle("-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";");
        resetDefaultsBtn.setStyle("-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";");

        // Add hover event handling
        shiftInfoBtn.setOnMouseEntered(e -> shiftInfoBtn.setStyle(hoverStyle));
        shiftInfoBtn.setOnMouseExited(e -> shiftInfoBtn.setStyle(initialStyle));
        settingsDropdown.setOnMouseEntered(e -> settingsDropdown.setStyle(hoverStyle));
        settingsDropdown.setOnMouseExited(e -> settingsDropdown.setStyle(initialStyle));
        notesButton.setOnMouseEntered(e -> notesButton.setStyle(hoverStyle));
        notesButton.setOnMouseExited(e -> notesButton.setStyle(initialStyle));
        createReportBtn.setOnMouseEntered(e -> createReportBtn.setStyle(hoverStyle));
        createReportBtn.setOnMouseExited(e -> createReportBtn.setStyle(initialStyle));
        logsButton.setOnMouseEntered(e -> logsButton.setStyle(hoverStyle));
        logsButton.setOnMouseExited(e -> logsButton.setStyle(initialStyle));
        mapButton.setOnMouseEntered(e -> mapButton.setStyle(hoverStyle));
        mapButton.setOnMouseExited(e -> mapButton.setStyle(initialStyle));
        updateInfoBtn.setOnMouseEntered(e -> updateInfoBtn.setStyle(hoverStyle));
        updateInfoBtn.setOnMouseExited(e -> {
            updateInfoBtn.setStyle(nonTransparentBtn);
        });
        resetDefaultsBtn.setOnMouseEntered(e -> resetDefaultsBtn.setStyle(hoverStyle));
        resetDefaultsBtn.setOnMouseExited(e -> {
            resetDefaultsBtn.setStyle(nonTransparentBtn);
        });
    }

    public void refreshChart() throws IOException {
        // Clear existing data from the chart
        reportChart.getData().clear();
        String[] categories = {"Callout", "Arrests", "Traffic Stops", "Patrols", "Searches", "Incidents", "Impounds", "Parking Cit.", "Traffic Cit."};
        CategoryAxis xAxis = (CategoryAxis) getReportChart().getXAxis();
        NumberAxis yAxis = (NumberAxis) getReportChart().getYAxis();

        // Setting font for X and Y axis labels
        Font axisLabelFont = Font.font("Segoe UI Bold", 11.5); // Change the font family and size as needed
        xAxis.setTickLabelFont(axisLabelFont);
        yAxis.setTickLabelFont(axisLabelFont);

        xAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(categories)));
        yAxis.setAutoRanging(true);
        yAxis.setMinorTickVisible(false);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Series 1");

        String color = ConfigReader.configRead("mainColor");
        for (String category : categories) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(category, 1); // Value doesn't matter, just need to add a data point
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " + color + ";");
                }
            });
            series1.getData().add(data);
        }

        getReportChart().getData().add(series1);
        getReportChart().setLegendVisible(false);
        getReportChart().getXAxis().setTickLabelGap(20);
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public BarChart getReportChart() {
        return reportChart;
    }

    public ComboBox getOfficerInfoAgency() {
        return OfficerInfoAgency;
    }

    public ComboBox getOfficerInfoDivision() {
        return OfficerInfoDivision;
    }

    public TextField getOfficerInfoName() {
        return OfficerInfoName;
    }

    public TextField getOfficerInfoNumber() {
        return OfficerInfoNumber;
    }

    public ComboBox getOfficerInfoRank() {
        return OfficerInfoRank;
    }

    public AnchorPane getShiftInformationPane() {
        return shiftInformationPane;
    }

    public AnchorPane getInfoPane() {
        return infoPane;
    }


    //</editor-fold>


    //<editor-fold desc="WindowUtils">
    @javafx.fxml.FXML
    public void onTopCLick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    @javafx.fxml.FXML
    public void onTopDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @javafx.fxml.FXML
    public void onFullscreenBtnClick(Event event) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        if (stage != null) {
            toggleWindowedFullscreen(stage, 1150, 800);
        }
    }

    @javafx.fxml.FXML
    public void onExitButtonClick(MouseEvent actionEvent) {
        Platform.exit();
    }

    //</editor-fold>


    //<editor-fold desc="Side Button Events">
    @javafx.fxml.FXML
    public void onMapButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("map-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Los Santos Map");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
        setDisable(infoPane, UISettingsPane, patrolReportPane, calloutReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(shiftInformationPane);
    }

    @javafx.fxml.FXML
    public void onLogsButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logBrowser.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Log Browser");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setMinHeight(stage.getHeight() - 250);
        stage.setMinWidth(stage.getWidth() - 250);
        ResizeHelper.addResizeListener(stage);
    }

    @javafx.fxml.FXML
    public void onNotesButtonClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("notes-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Notes");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.show();
        stage.setAlwaysOnTop(true);
        stage.setAlwaysOnTop(false);
        stage.centerOnScreen();
        stage.setMinHeight(stage.getHeight() - 150);
        stage.setMinWidth(stage.getWidth() - 150);
        ResizeHelper.addResizeListener(stage);
        stage.getScene().getStylesheets().add(getClass().getResource("css/notification-styles.css").toExternalForm());
    }
    //</editor-fold>


    //<editor-fold desc="Settings Button Events">
    @javafx.fxml.FXML
    public void testBtnPress(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("testWindow-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Test Window");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void aboutBtnClick(ActionEvent actionEvent) {
        setDisable(shiftInformationPane, UISettingsPane, calloutReportPane, patrolReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(infoPane);
    }

    @javafx.fxml.FXML
    public void clearLogsBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        confirmLogClearDialog(stage, reportChart);
    }

    @javafx.fxml.FXML
    public void clearAllSaveDataBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        confirmSaveDataClearDialog(stage);
    }

    // UI Settings Events
    @javafx.fxml.FXML
    public void UISettingsBtnClick(ActionEvent actionEvent) {
        showSettingsWindow();
    }

    @javafx.fxml.FXML
    public void resetDefaultsBtnPress(ActionEvent actionEvent) throws IOException {
        updateMain(Color.valueOf("#524992"));
        updateSecondary(Color.valueOf("#665cb6"));
        loadTheme();
    }

    @javafx.fxml.FXML
    public void onColorSelectMainPress(ActionEvent actionEvent) throws IOException {
        Color selectedColor = colorSelectMain.getValue();
        updateMain(selectedColor);
        loadTheme();
    }

    @javafx.fxml.FXML
    public void onColorSelectSecondaryPress(ActionEvent actionEvent) throws IOException {
        Color selectedColor = colorSelectSecondary.getValue();
        updateSecondary(selectedColor);
        loadTheme();
    }

    //</editor-fold>


    //<editor-fold desc="Open Report Button Events">

    @javafx.fxml.FXML
    public void onCalloutReportButtonClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, patrolReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(calloutReportPane);

        createSpinner(calloutReportSpinner, 0, 9999, 0);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        calloutReportName.setText(name);
        calloutReportDivision.setText(division);
        calloutReportRank.setText(rank);
        calloutReportAgency.setText(agency);
        calloutReportNumber.setText(number);

        calloutReportDate.setText(DataTerminalHomeApplication.getDate());
        calloutReportTime.setText(DataTerminalHomeApplication.getTime());
    }

    @javafx.fxml.FXML
    public void trafficStopReportButtonClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, patrolReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, calloutReportPane, arrestReportPane);
        setActive(trafficStopReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        trafficStopType.getItems().addAll(dropdownInfo.vehicleTypes);
        trafficStopColor.getItems().addAll(dropdownInfo.carColors);


        trafficStopofficerName.setText(name);
        trafficStopofficerDivision.setText(division);
        trafficStopofficerRank.setText(rank);
        trafficStopofficerAgency.setText(agency);
        trafficStopofficerNumber.setText(number);

        createSpinner(trafficStopNumber, 0, 9999, 0);

        trafficStopTime.setText(getTime());
        trafficStopDate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onIncidentReportBtnClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, patrolReportPane, calloutReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(incidentReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        incidentofficerName.setText(name);
        incidentofficerDivision.setText(division);
        incidentofficerRank.setText(rank);
        incidentofficerAgency.setText(agency);
        incidentofficerNumber.setText(number);

        createSpinner(incidentNumber, 0, 9999, 0);
        incidentReportTime.setText(getTime());
        incidentReportdate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onSearchReportBtnClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, patrolReportPane, calloutReportPane, incidentReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(searchReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        searchType.getItems().addAll(dropdownInfo.searchTypes);
        searchMethod.getItems().addAll(dropdownInfo.searchMethods);

        searchofficerName.setText(name);
        searchofficerDivision.setText(division);
        searchofficerRank.setText(rank);
        searchofficerAgency.setText(agency);
        searchofficerNumber.setText(number);

        createSpinner(SearchNumber, 0, 9999, 0);
        searchTime.setText(getTime());
        searchDate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onArrestReportBtnClick(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, patrolReportPane, calloutReportPane, incidentReportPane, impoundReportPane, trafficStopReportPane, searchReportPane);
        setActive(arrestReportPane);
        arrestAccordionInformation.setExpanded(true);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/medicalInformation.fxml"));
        loader.load();
        medicalInformationController = loader.getController();

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        arrestOfficerName.setText(name);
        arrestOfficerDivision.setText(division);
        arrestOfficerRank.setText(rank);
        arrestOfficerAgency.setText(agency);
        arrestOfficerNumber.setText(number);
        createSpinner(arrestNumber, 0, 9999, 0);
        arrestTime.setText(getTime());
        arrestDate.setText(getDate());

        //Tree View
        File file = new File("data/testXML.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().parse(file);

        Element root = document.getDocumentElement();

        TreeItem<String> rootItem = new TreeItem<>(root.getNodeName());

        parseTreeXML(root, rootItem);
        arrestTreeView.setRoot(rootItem);
        expandTreeItem(rootItem, "Root");
        arrestMonthsPane.setVisible(true);
        arrestYearsPane.setVisible(false);
    }

    @javafx.fxml.FXML
    public void onCitationReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("trafficCitationReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Citation Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onPatrolButtonClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, calloutReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(patrolReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        patrolofficerName.setText(name);
        patrolofficerDivision.setText(division);
        patrolofficerRank.setText(rank);
        patrolofficerAgency.setText(agency);
        patrolofficerNumber.setText(number);
        createSpinner(patrolSpinnerNumber, 0, 9999, 0);
        patrolStopTime.setText(getTime());
        patrolDate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onImpoundReportBtnClick(ActionEvent actionEvent) throws IOException {
        setDisable(shiftInformationPane, infoPane, UISettingsPane, calloutReportPane, incidentReportPane, searchReportPane, patrolReportPane, trafficStopReportPane, arrestReportPane);
        setActive(impoundReportPane);
        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        impoundType.getItems().addAll(dropdownInfo.vehicleTypes);
        impoundColor.getItems().addAll(dropdownInfo.carColors);
        impoundofficerName.setText(name);
        impoundofficerDivision.setText(division);
        impoundofficerRank.setText(rank);
        impoundofficerAgency.setText(agency);
        impoundofficerNumber.setText(number);
        impoundTime.setText(getTime());
        impoundDate.setText(getDate());
        createSpinner(impoundNumber, 0, 9999, 0);
    }

    @javafx.fxml.FXML
    public void onParkingCitationReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingCitationReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Parking Citation Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    //</editor-fold>


    //<editor-fold desc="Submit Report Button Events">

    @javafx.fxml.FXML
    public void onCalloutReportSubmitBtnClick(ActionEvent actionEvent) {
        if (calloutReportSpinner.getValue() == null) {
            calloutincompleteLabel.setText("Fill Out Form.");
            calloutincompleteLabel.setStyle("-fx-text-fill: red;");
            calloutincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                calloutincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {// Load existing logs from XML
            List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new CalloutLogEntry(
                    calloutReportDate.getText(),
                    calloutReportTime.getText(),
                    calloutReportName.getText(),
                    calloutReportRank.getText(),
                    calloutReportNumber.getText(),
                    calloutReportDivision.getText(),
                    calloutReportAgency.getText(),
                    calloutReportResponeType.getText(),
                    calloutReportResponseGrade.getText(),
                    calloutReportSpinner.getValue().toString(),
                    calloutReportNotesTextArea.getText(),
                    calloutReportResponseAddress.getText(),
                    calloutReportResponseCounty.getText(),
                    calloutReportResponseArea.getText()

            ));
            // Save logs to XML
            CalloutReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(calloutReportPane);
            updateChartIfMismatch(reportChart);
            calloutReportDate.setText("");
            calloutReportTime.setText("");
            calloutReportName.setText("");
            calloutReportRank.setText("");
            calloutReportNumber.setText("");
            calloutReportDivision.setText("");
            calloutReportAgency.setText("");
            calloutReportResponeType.setText("");
            calloutReportResponseGrade.setText("");
            calloutReportSpinner.getEditor().setText("0");
            calloutReportNotesTextArea.setText("");
            calloutReportResponseAddress.setText("");
            calloutReportResponseCounty.setText("");
            calloutReportResponseArea.setText("");
            showNotification("Reports", "A new Callout Report has been submitted.", vbox);
        }
    }

    @javafx.fxml.FXML
    public void onPatrolReportSubmitBtnClick(ActionEvent actionEvent) {
        if (patrolSpinnerNumber.getValue() == null) {
            patrolincompleteLabel.setText("Fill Out Form.");
            patrolincompleteLabel.setStyle("-fx-text-fill: red;");
            patrolincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                patrolincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new PatrolLogEntry(
                    patrolSpinnerNumber.getValue().toString(),
                    patrolDate.getText(),
                    patrolLength.getText(),
                    patrolStartTime.getText(),
                    patrolStopTime.getText(),
                    patrolofficerRank.getText(),
                    patrolofficerName.getText(),
                    patrolofficerNumber.getText(),
                    patrolofficerDivision.getText(),
                    patrolofficerAgency.getText(),
                    patrolofficerVehicle.getText(),
                    patrolComments.getText()
            ));
            // Save logs to XML
            PatrolReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(patrolReportPane);
            updateChartIfMismatch(reportChart);
            showNotification("Reports", "A new Patrol Report has been submitted.", vbox);
            patrolSpinnerNumber.getEditor().setText("0");
            patrolDate.setText("");
            patrolLength.setText("");
            patrolStartTime.setText("");
            patrolStopTime.setText("");
            patrolofficerRank.setText("");
            patrolofficerName.setText("");
            patrolofficerNumber.setText("");
            patrolofficerDivision.setText("");
            patrolofficerAgency.setText("");
            patrolofficerVehicle.setText("");
            patrolComments.setText("");
        }
    }

    @javafx.fxml.FXML
    public void onIncidentReportSubmitBtnClick(ActionEvent actionEvent) {
        if (incidentNumber.getValue() == null) {
            incidentincompleteLabel.setText("Fill Out Form.");
            incidentincompleteLabel.setStyle("-fx-text-fill: red;");
            incidentincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incidentincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new IncidentLogEntry(
                    incidentNumber.getValue().toString(),
                    incidentReportdate.getText(),
                    incidentReportTime.getText(),
                    incidentStatement.getText(),
                    incidentWitnesses.getText(),
                    incidentVictims.getText(),
                    incidentofficerName.getText(),
                    incidentofficerRank.getText(),
                    incidentofficerNumber.getText(),
                    incidentofficerAgency.getText(),
                    incidentofficerDivision.getText(),
                    incidentStreet.getText(),
                    incidentArea.getText(),
                    incidentCounty.getText(),
                    incidentActionsTaken.getText(),
                    incidentComments.getText()
            ));

            // Save logs to XML
            IncidentReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(incidentReportPane);
            updateChartIfMismatch(reportChart);
            showNotification("Reports", "A new Incident Report has been submitted.", vbox);
            incidentNumber.getEditor().setText("0");
            incidentReportdate.setText("");
            incidentReportTime.setText("");
            incidentStatement.setText("");
            incidentWitnesses.setText("");
            incidentVictims.setText("");
            incidentofficerName.setText("");
            incidentofficerRank.setText("");
            incidentofficerNumber.setText("");
            incidentofficerAgency.setText("");
            incidentofficerDivision.setText("");
            incidentStreet.setText("");
            incidentArea.setText("");
            incidentCounty.setText("");
            incidentActionsTaken.setText("");
            incidentComments.setText("");
        }
    }

    @javafx.fxml.FXML
    public void onSearchReportSubmitBtnClick(ActionEvent actionEvent) {
        if (SearchNumber.getValue() == null
                || searchType.getSelectionModel().isEmpty() || searchType.getValue() == null
                || searchMethod.getSelectionModel().isEmpty() || searchMethod.getValue() == null) {
            searchincompleteLabel.setText("Fill Out Form.");
            searchincompleteLabel.setStyle("-fx-text-fill: red;");
            searchincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                searchincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();
            String BreathalyzerUsed;
            String ResultPassFail;
            String BACMeasurement = duiInformationController.getBacMeasure().getText();

            if (duiInformationController.getBreathalyzerUsedNo().isSelected()) {
                BreathalyzerUsed = "no";
                ResultPassFail = "n/a";
                BACMeasurement = "n/a";
            } else {
                BreathalyzerUsed = "yes";
                if (duiInformationController.getBreathalyzerResultFail().isSelected()) {
                    ResultPassFail = "Failed";
                } else {
                    ResultPassFail = "Passed";
                }
            }

            // Add new entry
            logs.add(new SearchLogEntry(
                    SearchNumber.getValue().toString(),
                    searchedPersons.getText(),
                    searchDate.getText(),
                    searchTime.getText(),
                    searchSeizedItems.getText(),
                    searchGrounds.getText(),
                    searchType.getValue().toString(),
                    searchMethod.getValue().toString(),
                    searchWitnesses.getText(),
                    searchofficerRank.getText(),
                    searchofficerName.getText(),
                    searchofficerNumber.getText(),
                    searchofficerAgency.getText(),
                    searchofficerDivision.getText(),
                    searchStreet.getText(),
                    searchArea.getText(),
                    searchCounty.getText(),
                    searchComments.getText(),
                    BreathalyzerUsed,
                    ResultPassFail,
                    BACMeasurement
            ));

            // Save logs to XML
            SearchReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(searchReportPane);
            updateChartIfMismatch(reportChart);
            SearchNumber.getEditor().setText("0");
            searchedPersons.setText("");
            searchDate.setText("");
            searchTime.setText("");
            searchSeizedItems.setText("");
            searchGrounds.setText("");
            searchWitnesses.setText("");
            searchofficerRank.setText("");
            searchofficerName.setText("");

            searchType.getSelectionModel().selectFirst();
            searchMethod.getSelectionModel().selectFirst();

            searchofficerNumber.setText("");
            searchofficerAgency.setText("");
            searchofficerDivision.setText("");
            searchStreet.setText("");
            searchArea.setText("");
            searchCounty.setText("");
            searchComments.setText("");
            showNotification("Reports", "A new Search Report has been submitted.", vbox);
        }
    }

    @javafx.fxml.FXML
    public void onImpoundReportSubmitBtnClick(ActionEvent actionEvent) {
        if (impoundNumber.getValue() == null ||
                impoundType.getValue() == null ||
                impoundColor.getValue() == null) {
            impoundincompleteLabel.setText("Fill Out Form.");
            impoundincompleteLabel.setStyle("-fx-text-fill: red;");
            impoundincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                impoundincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new ImpoundLogEntry(
                    impoundNumber.getValue().toString(),
                    impoundDate.getText(),
                    impoundTime.getText(),
                    impoundownerName.getText(),
                    impoundownerAge.getText(),
                    impoundownerGender.getText(),
                    impoundownerAddress.getText(),
                    impoundPlateNumber.getText(),
                    impoundModel.getText(),
                    impoundType.getValue().toString(),
                    impoundColor.getValue().toString(),
                    impoundComments.getText(),
                    impoundofficerRank.getText(),
                    impoundofficerName.getText(),
                    impoundofficerNumber.getText(),
                    impoundofficerDivision.getText(),
                    impoundofficerAgency.getText()
            ));
            // Save logs to XML
            ImpoundReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(impoundReportPane);
            updateChartIfMismatch(reportChart);
            impoundNumber.getEditor().setText("0");
            impoundDate.setText("");
            impoundTime.setText("");
            impoundownerName.setText("");
            impoundownerAge.setText("");
            impoundownerGender.setText("");
            impoundownerAddress.setText("");
            impoundPlateNumber.setText("");
            impoundModel.setText("");
            impoundComments.setText("");

            impoundType.getSelectionModel().selectFirst();
            impoundColor.getSelectionModel().selectFirst();

            impoundofficerRank.setText("");
            impoundofficerName.setText("");
            impoundofficerNumber.setText("");
            impoundofficerDivision.setText("");
            impoundofficerAgency.setText("");
            showNotification("Reports", "A new Impound Report has been submitted.", vbox);
        }
    }

    @javafx.fxml.FXML
    public void onTrafficStopReportSubmitBtnClick(ActionEvent actionEvent) {
        if (trafficStopColor.getSelectionModel().isEmpty() || trafficStopColor.getValue() == null
                || trafficStopType.getSelectionModel().isEmpty() || trafficStopType.getValue() == null
                || trafficStopNumber.getValue() == null) {
            trafficStopIncompleteLabel.setText("Fill Out Form.");
            trafficStopIncompleteLabel.setStyle("-fx-text-fill: red;");
            trafficStopIncompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                trafficStopIncompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<TrafficStopLogEntry> logs = TrafficStopReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new TrafficStopLogEntry(
                    trafficStopDate.getText(),
                    trafficStopTime.getText(),
                    trafficStopModel.getText(),
                    trafficStopOtherInfo.getText(),
                    trafficStopownerName.getText(),
                    trafficStopownerAddress.getText(),
                    trafficStopownerDescription.getText(),
                    trafficStopownerGender.getText(),
                    trafficStopofficerName.getText(),
                    trafficStopofficerRank.getText(),
                    trafficStopofficerNumber.getText(),
                    trafficStopofficerDivision.getText(),
                    trafficStopofficerAgency.getText(),
                    trafficStopNumber.getValue().toString(),
                    trafficStopComments.getText(),
                    trafficStopStreet.getText(),
                    trafficStopCounty.getText(),
                    trafficStopArea.getText(),
                    trafficStopPlateNumber.getText(),
                    trafficStopColor.getValue().toString(),
                    trafficStopType.getValue().toString()
            ));
            // Save logs to XML
            TrafficStopReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(trafficStopReportPane);
            updateChartIfMismatch(reportChart);
            trafficStopDate.setText("");
            trafficStopTime.setText("");
            trafficStopModel.setText("");
            trafficStopOtherInfo.setText("");
            trafficStopownerName.setText("");
            trafficStopownerAddress.setText("");
            trafficStopownerDescription.setText("");
            trafficStopownerGender.setText("");
            trafficStopofficerName.setText("");
            trafficStopofficerRank.setText("");
            trafficStopofficerNumber.setText("");
            trafficStopofficerDivision.setText("");
            trafficStopofficerAgency.setText("");
            trafficStopNumber.getEditor().setText("");
            trafficStopComments.setText("");
            trafficStopStreet.setText("");
            trafficStopCounty.setText("");
            trafficStopArea.setText("");
            trafficStopPlateNumber.setText("");
            trafficStopColor.getSelectionModel().selectFirst();
            trafficStopType.getSelectionModel().selectFirst();
            showNotification("Reports", "A new Traffic Stop Report has been submitted.", vbox);
        }
    }

    @javafx.fxml.FXML
    public void onArrestReportSubmitBtnClick(ActionEvent actionEvent) {
    }

    //</editor-fold>


    //<editor-fold desc="Misc.">

    @javafx.fxml.FXML
    public void onSearchPopOverBtnPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/DUIInformation.fxml"));
        Parent root = loader.load();
        duiInformationController = loader.getController();

        // Create a PopOver and set the content node
        PopOver popOver = new PopOver();
        popOver.setContentNode(root);

        // Optionally configure other properties of the PopOver
        popOver.setDetachable(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
        popOver.cornerRadiusProperty().setValue(23);
        popOver.setFadeInDuration(Duration.seconds(0.5));
        popOver.setFadeOutDuration(Duration.seconds(0.4));
        popOver.setTitle("Breathalyzer Information");
        popOver.setHeaderAlwaysVisible(true);
        popOver.show(searchpopOverBtn);
    }

    @javafx.fxml.FXML
    public void updateInfoButtonClick(ActionEvent actionEvent) {
        if (getOfficerInfoAgency().getValue() == null || getOfficerInfoDivision().getValue() == null ||
                getOfficerInfoRank().getValue() == null || getOfficerInfoName().getText().isEmpty() ||
                getOfficerInfoNumber().getText().isEmpty()) {
            updatedNotification.setText("Fill Out Form.");
            updatedNotification.setStyle("-fx-text-fill: red;");
            updatedNotification.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline1.play();
        } else {
            ConfigWriter.configwrite("Agency", getOfficerInfoAgency().getValue().toString());
            ConfigWriter.configwrite("Division", getOfficerInfoDivision().getValue().toString());
            ConfigWriter.configwrite("Name", getOfficerInfoName().getText());
            ConfigWriter.configwrite("Rank", getOfficerInfoRank().getValue().toString());
            ConfigWriter.configwrite("Number", getOfficerInfoNumber().getText());
            generatedByTag.setText("Generated By:" + " " + getOfficerInfoName().getText());
            updatedNotification.setText("updated.");
            updatedNotification.setStyle("-fx-text-fill: green;");
            updatedNotification.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline.play();
        }
    }

    @javafx.fxml.FXML
    public void TrafficStopToTrafficCitationBtnClick(ActionEvent actionEvent) throws IOException {
        setActive(calloutReportPane);

        createSpinner(calloutReportSpinner, 0, 9999, 0);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        calloutReportName.setText(name);
        calloutReportDivision.setText(division);
        calloutReportRank.setText(rank);
        calloutReportAgency.setText(agency);
        calloutReportNumber.setText(number);

        // Add transferring traffic stop information to Traffic Citation Pane

        calloutReportDate.setText(DataTerminalHomeApplication.getDate());
        calloutReportTime.setText(DataTerminalHomeApplication.getTime());
    }

    @javafx.fxml.FXML
    public void ArrestToImpoundBtnClick(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void ArrestToIncidentBtnClick(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void ArrestToSearchBtnClick(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void onTreeViewMouseClick(Event event) {
    }

    @javafx.fxml.FXML
    public void addChargeBtnPress(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void removeChargeBtnPress(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void onArrestMedInfoBtnPress(ActionEvent actionEvent) {
    }

    //</editor-fold>


}