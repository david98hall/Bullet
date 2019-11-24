package bulletapp.view.javafx.nodes.content.diagram;

import bulletapp.content.ContentType;
import bulletapp.view.javafx.nodes.content.ContentNode;
import bulletapp.view.javafx.nodes.content.diagram.interfaces.IDiagramNavigationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;


public class DiagramNode extends ContentNode {

    private final DiagramController controller;
    private final List<IDiagramNavigationListener> navigationListeners;

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane diagramChoicePane;

    @FXML
    private AnchorPane editPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private AnchorPane diagramPane;

    private String data;


    public DiagramNode() {
        this("");
    }

    public DiagramNode(String data) {
        super("/view/javafx/content/diagram/diagram.fxml", new AnchorPane());
        controller = new DiagramController(this);
        navigationListeners = new ArrayList<>();

        controller.loadSavedData(data);
        setContentData(data);

        init();
    }


    private void init() {

        if (getTabPaneSize() == 0) {
            controller.addDataTab();
            navigateTo(DiagramViews.CHART_CHOOSER);
        } else {
            navigateTo(DiagramViews.DIAGRAM_VIEW);
        }

        controller.addAddTab();


    }

    void setChartNode(Chart chart) {
        this.diagramPane.getChildren().clear();
        this.diagramPane.getChildren().add(chart);
        chart.prefWidthProperty().bind(diagramPane.widthProperty());
        chart.prefHeightProperty().bind(diagramPane.heightProperty());
    }


    void removeTab(Tab tab) {
        tabPane.getTabs().remove(tab);
    }


    private void navigateTo(DiagramViews view) {
        switch (view) {
            case DATA_EDIT:
                navigateTo(editPane);
                notifyListeners(DiagramViews.DATA_EDIT);
                break;
            case CHART_CHOOSER:
                navigateTo(diagramChoicePane);
                notifyListeners(DiagramViews.CHART_CHOOSER);
                break;
            case DIAGRAM_VIEW:
                navigateTo(diagramPane);
                notifyListeners(DiagramViews.DIAGRAM_VIEW);
                save();
                break;
        }
    }

    private void save() {
        setContentData(controller.getData());
    }

    private void navigateTo(Pane pane) {
        pane.toFront();
        hideAllStackPaneChildren();
        pane.setDisable(false);
        pane.setVisible(true);
    }


    void addTab(Tab tab) {
        tabPane.getTabs().add(tab);
    }

    void addTab(int index, Tab tab) {
        tabPane.getTabs().add(index, tab);
    }

    void setSelected(Tab tab) {
        tabPane.getSelectionModel().select(tab);
    }

    int getTabPaneSize() {
        return tabPane.getTabs().size();
    }

    private void hideAllStackPaneChildren() {
        stackPane.getChildren().forEach(child -> {
            child.setDisable(true);
            child.setVisible(false);
        });
    }

    @FXML
    void EditDataMenueItemOnAction(ActionEvent event) {
        navigateTo(DiagramViews.DATA_EDIT);
    }

    @FXML
    void EditDiagramTypeMenueItemOnAction(ActionEvent event) {
        navigateTo(DiagramViews.CHART_CHOOSER);
    }

    @FXML
    void viewDiagramMenueItemOnAction(ActionEvent event) {
        navigateTo(DiagramViews.DIAGRAM_VIEW);
    }


    @FXML
    void areaChartButtonOnAction(ActionEvent event) {
        controller.setChart(Charts.AREA_CHART);
        afterDiagramChoice();
    }

    @FXML
    void barChartButtonOnAction(ActionEvent event) {
        controller.setChart(Charts.BAR_CHART);
        afterDiagramChoice();
    }

    @FXML
    void bubbleChartButtonOnAction(ActionEvent event) {
        controller.setChart(Charts.BUBBLE_CHART);
        afterDiagramChoice();
    }

    @FXML
    void lineChartButtonOnAction(ActionEvent event) {
        controller.setChart(Charts.LINE_CHART);
        afterDiagramChoice();
    }

    @FXML
    void scatterChartButtonOnAction(ActionEvent event) {
        controller.setChart(Charts.SCATTER_CHART);
        afterDiagramChoice();
    }

    @FXML
    void stackedAreaChartButtonOnAction(ActionEvent event) {
        controller.setChart(Charts.STACKED_AREA_CHART);
        afterDiagramChoice();
    }

    @FXML
    void stackedBarChartButtonOnAction(ActionEvent event) {
        controller.setChart(Charts.STACKED_BAR_CHART);
        afterDiagramChoice();
    }

    private void afterDiagramChoice() {

        if (controller.isDataEmpty()) {
            navigateTo(DiagramViews.DATA_EDIT);
        } else {
            navigateTo(DiagramViews.DIAGRAM_VIEW);
        }

    }

    void addDiagramListener(IDiagramNavigationListener listener) {
        navigationListeners.add(listener);
    }

    private void notifyListeners(DiagramViews view) {
        navigationListeners.forEach(listener -> listener.navigateTo(view));
    }

    void removeNavigationListener(IDiagramNavigationListener listener) {
        navigationListeners.remove(listener);
    }

    @Override
    public String getData() {
        return data;
    }

    public void setContentData(String data) {
        super.setContentData(data);
        this.data = data;
    }


    @Override
    public ContentType getContentType() {
        return ContentType.DIAGRAM;
    }

}
