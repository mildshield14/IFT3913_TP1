/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2022, by David Gilbert and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ------------------
 * LineChartTest.java
 * ------------------
 * (C) Copyright 2005-2022, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

import ValueAxis;
import ChartChangeEvent;
import ChartChangeListener;
import CategoryToolTipGenerator;
import StandardCategoryToolTipGenerator;
import CategoryPlot;
import CategoryItemRenderer;
import CategoryURLGenerator;
import StandardCategoryURLGenerator;
import Range;
import CategoryDataset;
import DatasetUtils;
import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;

/**
 * Some tests for a line chart.
 */
public class LineChartTest  {

    /** A chart. */
    private JFreeChart chart;

    /**
     * Common test setup.
     */
    @BeforeEach
    public void setUp() {
        this.chart = createLineChart();
    }

    /**
     * Draws the chart with a null info object to make sure that no exceptions
     * are thrown (a problem that was occurring at one point).
     */
    @Test
    public void testDrawWithNullInfo() {
        BufferedImage image = new BufferedImage(200 , 100,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        this.chart.draw(g2, new Rectangle2D.Double(0, 0, 200, 100), null,
                 null);
        g2.dispose();
    }

    /**
     * Replaces the chart's dataset and then checks that the new dataset is OK.
     */
    @Test
    public void testReplaceDataset() {

        // create a dataset...
        Number[][] data = new Integer[][] {{-30, -20}, {-10, 10}, {20, 30}};

        CategoryDataset<String, String> newData 
                = DatasetUtils.createCategoryDataset("S", "C", data);

        LocalListener l = new LocalListener();
        this.chart.addChangeListener(l);
        
        @SuppressWarnings("unchecked")
        CategoryPlot<String, String> plot = (CategoryPlot) this.chart.getPlot();
        plot.setDataset(newData);
        assertTrue(l.flag);
        ValueAxis axis = plot.getRangeAxis();
        Range range = axis.getRange();
        assertTrue(range.getLowerBound() <= -30, 
                "Expecting the lower bound of the range to be around -30: " + range.getLowerBound());
        assertTrue(range.getUpperBound() >= 30, 
                "Expecting the upper bound of the range to be around 30: " + range.getUpperBound());
    }

    /**
     * Check that setting a tool tip generator for a series does override the
     * default generator.
     */
    @Test
    public void testSetSeriesToolTipGenerator() {
        CategoryPlot<?, ?> plot = (CategoryPlot) this.chart.getPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        StandardCategoryToolTipGenerator tt
                = new StandardCategoryToolTipGenerator();
        renderer.setSeriesToolTipGenerator(0, tt);
        CategoryToolTipGenerator tt2 = renderer.getToolTipGenerator(0, 0);
        assertSame(tt2, tt);
    }

    /**
     * Check that setting a URL generator for a series does override the
     * default generator.
     */
    @Test
    public void testSetSeriesURLGenerator() {
        CategoryPlot<?, ?> plot = (CategoryPlot) this.chart.getPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        StandardCategoryURLGenerator url1
                = new StandardCategoryURLGenerator();
        renderer.setSeriesItemURLGenerator(0, url1);
        CategoryURLGenerator url2 = renderer.getItemURLGenerator(0, 0);
        assertSame(url2, url1);
    }

    /**
     * Create a line chart with sample data in the range -3 to +3.
     *
     * @return The chart.
     */
    private static JFreeChart createLineChart() {
        Number[][] data = new Integer[][] {{-3, -2}, {-1, 1}, {2, 3}};
        CategoryDataset<String,String> dataset 
                = DatasetUtils.createCategoryDataset("S", "C", data);
        return ChartFactory.createLineChart("Line Chart", "Domain", "Range",
                dataset);
    }

    /**
     * A chart change listener.
     *
     */
    static class LocalListener implements ChartChangeListener {

        /** A flag. */
        private boolean flag;

        /**
         * Event handler.
         *
         * @param event  the event.
         */
        @Override
        public void chartChanged(ChartChangeEvent event) {
            this.flag = true;
        }

    }

}