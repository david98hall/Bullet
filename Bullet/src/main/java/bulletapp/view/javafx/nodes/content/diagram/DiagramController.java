package bulletapp.view.javafx.nodes.content.diagram;


import bulletapp.content.ContentType;
import bulletapp.util.removable.IRemoveRequestListener;
import bulletapp.view.javafx.nodes.content.diagram.tab.interfaces.ITabListener;
import bulletapp.view.javafx.nodes.content.diagram.tab.TabNode;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

class DiagramController implements IRemoveRequestListener<TabNode>, ITabListener {

    private final DiagramNode view;


    private final Map<TabNode, SimpleEntry<XYChart.Series, SimpleEntry<List<List<String>>, List<List<Number>>>>> tabMap;


    private XYChart chart;

    private Charts chartType;

    private int numOfStringColumns;
    private int numOfNumberColumns;

    DiagramController(DiagramNode diagramNode) {
        tabMap = new HashMap<>();
        view = diagramNode;

    }


    private void setChart(XYChart chart, int string, int number) {
        this.chart = chart;

        numOfStringColumns = string;
        numOfNumberColumns = number;


        tabMap.values().forEach(simpleEntry -> simpleEntry.setValue(updateDataLists(string, number, simpleEntry.getValue())));


        tabMap.keySet().forEach(key -> key.setData(tabMap.get(key).getValue()));


        tabMap.keySet().forEach(this::createNewSeries);

        updateAllSeries();

        view.setChartNode(chart);
    }

    private void createNewSeries(TabNode tab) {
        XYChart.Series series = new XYChart.Series();
        series.setName(tab.getName());

        tabMap.replace(tab, new SimpleEntry<>(series, tabMap.get(tab).getValue()));
    }

    private void updateAllSeries() {
        chart.getData().clear();
        tabMap.values().forEach(value -> chart.getData().add(value.getKey()));
    }

    private SimpleEntry<List<List<String>>, List<List<Number>>> updateDataLists(int newString, int newNumber, SimpleEntry<List<List<String>>, List<List<Number>>> tuple) {
        //todo fix method
        List<List<Number>> numberData = tuple.getValue();
        List<List<String>> stringData = tuple.getKey();

        List<List<String>> newStringData = new ArrayList<>();
        List<List<Number>> newNumberData = new ArrayList<>();

        int oldString = stringData.size();
        int oldNumber = numberData.size();

        //remove excess Data
        int excess = (oldString + newString) - (oldNumber + newNumber);
        if (excess > 0) {
            for (int i = 0; i < numberData.size() && excess > 0; i++, excess--) {
                numberData.remove(numberData.size());
            }
            for (int i = 0; i < stringData.size() && excess > 0; i++, excess--) {
                stringData.remove(stringData.size());
            }
        }

        //add
        int minS = Math.min(newString, oldString);
        int minN = Math.min(newNumber, oldNumber);
        addDataPoints(minS, newStringData, stringData);
        addDataPoints(minN, newNumberData, numberData);

        int deltaS = newString - oldString;
        int deltaN = newNumber - oldNumber;

        //shuffle Data
        if (deltaS > 0) {
            for (int i = 0; i < deltaS && newStringData.size() < newString && i < oldNumber; i++) {
                newStringData.add(numberDataToStringData(numberData.get(i)));
            }
        }
        if (deltaN > 0) {
            for (int i = 0; i < deltaN && newNumberData.size() < newNumber && i < oldString; i++) {
                newNumberData.add(stringDataToNumberData(stringData.get(i)));
            }
        }

        //add missing data
        addMissingDataPoints(newString, newStringData);
        addMissingDataPoints(newNumber, newNumberData);

        return new SimpleEntry<List<List<String>>, List<List<Number>>>(newStringData, newNumberData);
    }


    private <B> void addDataPoints(int min, List<B> newData, List<B> oldData) {
        while (min-- > 0) {
            newData.add(oldData.get(0));
            oldData.remove(0);
        }
    }

    private void addMissingDataPoints(int size, List list) {
        while (size > list.size()) {
            list.add(new ArrayList<>());
        }
    }

    private List<String> numberDataToStringData(List<Number> list) {
        List<String> string = new ArrayList<>(list.size());
        list.forEach(num -> string.add(numberToString(num)));
        return string;
    }

    private List<Number> stringDataToNumberData(List<String> list) {
        List<Number> num = new ArrayList<>(list.size());
        list.forEach(string -> num.add(stringToNumber(string)));
        return num;
    }

    private Number stringToNumber(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            return null;
        }
    }

    private String numberToString(Number number) {
        if (number == null) {
            return "";
        }
        return number + "";
    }

    private String getTabName() {
        int num = view.getTabPaneSize();
        if (num == 0) {
            num = 1;
        }
        return "series" + num;
    }

    @Override
    public void onRemovalRequest(TabNode tab) {
        removeSeries(getSeries(tab));
        view.removeTab(tab.getTab());
        tabMap.remove(tab);

        view.removeNavigationListener(tab);

    }

    private XYChart.Series getSeries(TabNode tab) {

        return tabMap.get(tab).getKey();
    }

    private void addDataTab(Tab tab) {
        addDataTab(tab, view.getTabPaneSize());
    }

    private void addDataTab(Tab tab, int index) {
        //todo fix method
        XYChart.Series series = new XYChart.Series();
        series.setName(getTabName());

        addSeries(series);

        List<List<String>> stringData = createStringData();
        List<List<Number>> numberData = createNumberData();


        TabNode tabNode = new TabNode(series.getName(), tab, stringData, numberData);
        tabNode.addRemoveListener(this);
        tabNode.addITabListener(this);
        view.addDiagramListener(tabNode);

        tabMap.put(tabNode, new SimpleEntry<>(series, new SimpleEntry<>(stringData, numberData)));

        tab.setContent(tabNode.getNode());
        view.addTab(index, tab);
    }

    private List<List<String>> createStringData() {
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < numOfStringColumns; i++) {
            lists.add(new ArrayList<>());
        }
        return lists;
    }

    private List<List<Number>> createNumberData() {
        List<List<Number>> lists = new ArrayList<>();
        for (int i = 0; i < numOfNumberColumns; i++) {
            lists.add(new ArrayList<>());
        }
        return lists;
    }

    void addDataTab() {
        addDataTab(new Tab());
    }


    void addAddTab() {
        Tab tab = new Tab();

        tab.setText(" + ");

        tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                Tab newTab = new Tab();
                addDataTab(newTab, view.getTabPaneSize() - 1);
                view.setSelected(newTab);
            }
        });
        view.addTab(tab);
    }


    public ContentType getContentType() {
        return ContentType.DIAGRAM;
    }


    void addSeries(XYChart.Series series) {
        chart.getData().add(series);
    }

    void removeSeries(XYChart.Series series) {
        chart.getData().remove(series);
    }

    boolean isDataEmpty() {
        //todo
        return true;
    }

    @Override
    public void onChange(List<List<String>> table, TabNode tab) {
        try {
            tabMap.get(tab).setValue(matrixToData(table));
            updateSeries(tab);
        } catch (Exception e) {
            System.out.println("Diagram Error");
        }

    }

    private void updateSeries(TabNode tab) {
        //todo fix
        XYChart.Series series = tabMap.get(tab).getKey();

        SimpleEntry<List<List<String>>, List<List<Number>>> simpleEntry = tabMap.get(tab).getValue();

        XYChart.Series newSeries = new XYChart.Series();
        for (int i = 0; i < numberOfDataPoints(simpleEntry); i++) {

            XYChart.Data data = dataToXYChartData(i, simpleEntry);
            if (containsValidData(data)) {
                newSeries.getData().add(data);
            }

        }
        if (newSeries.getData().size() > 0) {

            series.setData(newSeries.getData());
            series.setName(tab.getName());

        }
    }


    private <A, T> int numberOfDataPoints(SimpleEntry<List<List<A>>, List<List<T>>> simpleEntry) {
        List<List<A>> l1;
        List<List<T>> l2;
        try {
            l1 = simpleEntry.getKey();
        } catch (Exception e) {
            l1 = new ArrayList<>();
        }
        try {
            l2 = simpleEntry.getValue();
        } catch (Exception e) {
            l2 = new ArrayList<>();
        }
        int datapoints = 0;
        if (l1.size() > 0) {
            datapoints = l1.get(0).size();
        }
        if (l2.size() > 0 && (l2.get(0).size() > datapoints)) {
            datapoints = l2.get(0).size();
        }
        return datapoints;
    }

    private XYChart.Data dataToXYChartData(int index, SimpleEntry<List<List<String>>, List<List<Number>>> simpleEntry) {
        //todo fix
        if (numOfStringColumns == 1) {
            return new XYChart.Data(simpleEntry.getKey().get(0).get(index), simpleEntry.getValue().get(0).get(index));
        } else if (numOfNumberColumns == 2) {
            return new XYChart.Data(simpleEntry.getValue().get(0).get(index), simpleEntry.getValue().get(1).get(index));
        }
        return new XYChart.Data(simpleEntry.getValue().get(0).get(index), simpleEntry.getValue().get(1).get(index), simpleEntry.getValue().get(2).get(index));

    }

    private boolean containsValidData(XYChart.Data data) {
        if (data.getXValue() == null || data.getYValue() == null) {
            return false;
        }
        return true;
    }

    private SimpleEntry<List<List<String>>, List<List<Number>>> matrixToData(List<List<String>> table) {
        return new SimpleEntry<>(matrixToString(table), matrixToNumber(table));
    }

    private List<List<String>> matrixToString(List<List<String>> table) {
        List<List<String>> str = new ArrayList<>();
        for (int i = 0; i < numOfStringColumns; i++) {
            str.add(table.get(i));
        }
        return str;
    }

    private List<List<Number>> matrixToNumber(List<List<String>> table) {
        List<List<Number>> num = new ArrayList<>();
        for (int i = numOfStringColumns; i < numOfStringColumns + numOfNumberColumns; i++) {
            num.add(stringDataToNumberData(table.get(i)));
        }
        return num;
    }


    private void setChartToLineChart() {
        setChart(ChartFactory.createLineChart(), 0, 2);
    }

    private void setChartToBubbleChart() {
        setChart(ChartFactory.createBubbleChart(), 0, 3);
    }

    private void setChartToScatterChart() {
        setChart(ChartFactory.createScatterChart(), 0, 2);

    }

    private void setChartToAreaChart() {
        setChart(ChartFactory.createAreaChart(), 0, 2);
    }

    private void setChartToStackedBarChart() {
        setChart(ChartFactory.createStackedBarChart(), 1, 1);

    }

    private void setChartToBarChart() {
        setChart(ChartFactory.createBarChart(), 1, 1);

    }

    private void setChartToStackedAreaChart() {
        setChart(ChartFactory.createStackedAreaChart(), 0, 2);
    }

    String getData() {
        StringBuilder builder = new StringBuilder();
        builder.append(chartTypeToString(chartType));
        tabMap.keySet().forEach(tab -> {
            builder.append("{" + tab.getName() + "}");
            String s = tab.getData().toString();
            builder.append(s.substring(1, s.length() - 1));
        });

        return builder.toString();
    }

    void loadSavedData(String data) {
        if (data.contains("[") && data.contains("]") && data.contains("{") && data.contains("}")) {
            String type = data.substring(0, data.indexOf("{"));
            setChart(Charts.valueOf(type));
            String temp = data.substring(data.indexOf("{"));
            List<SimpleEntry<List<List<String>>, List<List<Number>>>> simpleEntries = new ArrayList<>();
            List<List<String>> sList = new ArrayList<>();
            List<List<Number>> nList = new ArrayList<>();
            List<String> names = new ArrayList<>();

            for (int n = 0; temp.length() > 1; n++) {
                sList = new ArrayList<>();
                nList = new ArrayList<>();

                names.add(temp.substring(temp.indexOf("{") + 1, temp.indexOf("}")));
                temp = temp.substring(temp.indexOf("}") + 1);

                for (int i = 0; i < numOfStringColumns; i++) {
                    sList.add(stringToStringList(temp.substring(temp.indexOf("[") + 1, temp.indexOf("]")) + ","));
                    temp = temp.substring(temp.indexOf("]") + 1).trim();
                }

                for (int i = 0; i < numOfNumberColumns; i++) {
                    nList.add(stringToNumberList(temp.substring(temp.indexOf("[") + 1, temp.indexOf("]")) + ","));
                    temp = temp.substring(temp.indexOf("]") + 1).trim();
                }
                Tab tab = new Tab();
                addDataTab(tab);
                simpleEntries.add(new SimpleEntry<>(sList, nList));
            }

            tabMap.keySet().forEach(tab -> {
                tabMap.get(tab).setValue(simpleEntries.get(0));
                tab.setName(names.get(0));
                simpleEntries.remove(0);
                names.remove(0);
            });

            setChart(Charts.valueOf(type));

        } else {
            setChart(Charts.LINE_CHART);
        }
    }

    private List<String> stringToStringList(String sub) {
        List<String> list = new ArrayList<>();
        while (sub.length() > 1) {
            String element = sub.substring(0, sub.indexOf(","));
            if (element.contains("null")) {
                element = "";
            }
            list.add(element);

            sub = sub.substring(sub.indexOf(",") + 1);
            sub = sub.trim();
        }
        return list;
    }

    private List<Number> stringToNumberList(String sub) {
        List<Number> list = new ArrayList<>();
        while (sub.length() > 1) {
            String element = sub.substring(0, sub.indexOf(","));
            if (element.contains("null")) {
                element = "";
            }
            list.add(stringToNumber(element));

            sub = sub.substring(sub.indexOf(",") + 1);
            sub = sub.trim();
        }
        return list;
    }

    private String chartTypeToString(Charts chartType) {
        switch (chartType) {
            case AREA_CHART:
                return Charts.AREA_CHART.name();
            case STACKED_AREA_CHART:
                return Charts.STACKED_AREA_CHART.name();
            case BAR_CHART:
                return Charts.BAR_CHART.name();
            case STACKED_BAR_CHART:
                return Charts.STACKED_BAR_CHART.name();
            case BUBBLE_CHART:
                return Charts.BUBBLE_CHART.name();
            case LINE_CHART:
                return Charts.LINE_CHART.name();
            case SCATTER_CHART:
                return Charts.SCATTER_CHART.name();
        }
        return "";
    }

    private Charts stringToChartsType(String name) {
        return Charts.valueOf(name);
    }

    void setChart(Charts chartType) {
        this.chartType = chartType;
        switch (chartType) {
            case AREA_CHART:
                setChartToAreaChart();
                break;
            case STACKED_AREA_CHART:
                setChartToStackedAreaChart();
                break;
            case BAR_CHART:
                setChartToBarChart();
                break;
            case STACKED_BAR_CHART:
                setChartToStackedBarChart();
                break;
            case BUBBLE_CHART:
                setChartToBubbleChart();
                break;
            case LINE_CHART:
                setChartToLineChart();
                break;
            case SCATTER_CHART:
                setChartToScatterChart();
                break;
        }

    }
}
