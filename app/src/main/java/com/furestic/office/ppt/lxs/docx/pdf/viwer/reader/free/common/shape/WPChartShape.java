package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.common.shape;

import com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.thirdpart.achartengine.chart.AbstractChart;

public class WPChartShape extends WPAutoShape
{

	public AbstractChart getAChart() 
	{
		return chart;
	}

	public void setAChart(AbstractChart chart) 
	{
		this.chart = chart;
	}
	
	private AbstractChart chart;
}
