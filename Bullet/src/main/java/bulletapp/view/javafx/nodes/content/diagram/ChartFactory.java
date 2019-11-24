package bulletapp.view.javafx.nodes.content.diagram;

import javafx.scene.chart.*;

public abstract class ChartFactory {


    //number eller string
    static<T,A> LineChart createLineChart(Axis<T> x,Axis<A> y){
      return new LineChart<>(x,y);
    }

    static LineChart<Number,Number> createLineChart(){
        return new LineChart<>(new NumberAxis(),numberAxis());
    }
    static AreaChart<Number, Number> createAreaChart(){
        return new AreaChart<>(new NumberAxis(),new NumberAxis());
    }

    static BubbleChart<Number, Number> createBubbleChart(){
        return new BubbleChart<>(new NumberAxis(),new NumberAxis());
    }
    static ScatterChart<Number, Number> createScatterChart(){
        return new ScatterChart<>(new NumberAxis(),new NumberAxis());
    }
    static StackedAreaChart<Number, Number> createStackedAreaChart(){
        return new StackedAreaChart<>(new NumberAxis(),new NumberAxis());
    }

    static StackedBarChart<String, Number> createStackedBarChart(){
        return new StackedBarChart<>(new CategoryAxis(),new NumberAxis());
    }
    static BarChart<String, Number> createBarChart(){
        return new BarChart<>(new CategoryAxis(),new NumberAxis());
    }

    public static Axis<Number> numberAxis(){
        return new NumberAxis();
    }
    public static Axis<String> categoryAxis(){
        return new CategoryAxis();
    }
}
