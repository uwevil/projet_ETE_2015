package calculRepartitionBit;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Generation_Graph {

	@SuppressWarnings("deprecation")
	public Generation_Graph(String title, String in1, String in2, String out) {
		try(BufferedReader reader = new BufferedReader(new FileReader(in1));
				BufferedReader reader2 = new BufferedReader(new FileReader(in2)))
		{	
			XYSeries data = new XYSeries("Sang");
			
			while (true)
			{
				String s = new String();
				s = reader.readLine();
				if (s == null)
					break;
				
				String[] tmp = s.split(" ");
				data.add(Integer.parseInt(tmp[0]), Float.parseFloat(tmp[1]));	
			}
			reader.close();
			
			XYSeries data2 = new XYSeries("Bassirou");
			
			while (true)
			{
				String s = new String();
				s = reader2.readLine();
				if (s == null)
					break;
				
				String[] tmp = s.split(" ");
				data2.add(Integer.parseInt(tmp[0]), Float.parseFloat(tmp[1]));	
			}
			reader2.close();
			
			XYSeriesCollection dataset = new XYSeriesCollection( );
			dataset.addSeries(data);
			XYSeriesCollection dataset2 = new XYSeriesCollection( );
			dataset.addSeries(data2);
			
			JFreeChart jf = ChartFactory.createXYLineChart(title, "i", "%", null);
	        jf.setBackgroundPaint(Color.white);
	        
			XYPlot xyplot = jf.getXYPlot();
			xyplot.setDataset(0, dataset);
			xyplot.setDataset(1, dataset2);

			XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
			xyplot.setRenderer(0, render);
			render.setShapesVisible(false);
			xyplot.getRendererForDataset(xyplot.getDataset(0)).setSeriesPaint(0, Color.blue);
			
			xyplot.setRenderer(1, render);
			xyplot.getRendererForDataset(xyplot.getDataset(1)).setSeriesPaint(1, Color.red);
			
			xyplot.setBackgroundPaint(Color.white);
			
			
			ValueAxis yaxis = (ValueAxis) xyplot.getRangeAxis();
			NumberAxis numaxis = (NumberAxis) xyplot.getRangeAxis();
			yaxis.setLowerBound(0.09);
			yaxis.setUpperBound(0.30);
			numaxis.setTickUnit(new NumberTickUnit(0.01));
			
			ValueAxis xaxis = (ValueAxis) xyplot.getDomainAxis();
			xaxis.setLowerBound(-15);

			
			int width = 1920; /* Width of the image */
		    int height = 1080; /* Height of the image */ 
		    File XYChart = new File(out); 
		    ChartUtilities.saveChartAsJPEG( XYChart, jf, width, height);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Generation_Graph g = new Generation_Graph("Qualité de filtre de Bloom",
				"/Users/dcs/vrac/test/30-07-2015/sang.txt",
				"/Users/dcs/vrac/test/30-07-2015/bassirou.txt",
				"/Users/dcs/vrac/test/30-07-2015/out.jpeg"
				);
	}

}
