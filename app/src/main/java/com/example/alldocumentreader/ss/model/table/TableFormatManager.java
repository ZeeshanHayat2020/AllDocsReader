package com.example.alldocumentreader.ss.model.table;

import com.example.alldocumentreader.ss.model.style.CellStyle;

import java.util.HashMap;
import java.util.Map;

public class TableFormatManager 
{
	public TableFormatManager(int count)
	{
		formats = new HashMap<Integer, CellStyle>(count);
	}
	
	public int addFormat(CellStyle format)
	{
		int size = formats.size();
		formats.put(size, format);
		return size;
	}
	
	public CellStyle getFormat(int index)
	{
		if(index >= 0 && index < formats.size())
		{
			return formats.get(index);
		}
		
		return null;
	}
	
	public void dispose()
	{
		formats.clear();
		formats = null;
	}
	
	private Map<Integer, CellStyle> formats;
}
