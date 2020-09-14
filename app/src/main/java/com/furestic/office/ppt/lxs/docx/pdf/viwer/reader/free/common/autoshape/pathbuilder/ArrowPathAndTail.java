package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.common.autoshape.pathbuilder;

import android.graphics.Path;
import android.graphics.PointF;

public class ArrowPathAndTail 
{
	public void reset()
	{
		path = null;
		tail = null;
	}
	
	public Path getArrowPath()
	{
		return path;
	}
	public void setArrowPath(Path path)
	{
		this.path = path;
	}
	public PointF getArrowTailCenter()
	{
		return tail;
	}
	public void setArrowTailCenter(float x, float y)
	{
		this.tail = new PointF(x, y);
	}
	
	//arrow path
	private Path path;
	//arrow tail center position
	private PointF tail;
}
