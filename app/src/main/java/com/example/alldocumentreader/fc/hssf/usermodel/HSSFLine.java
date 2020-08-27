/*
 * 文件名称:           HSSFLine.java
 *  
 * 编译器:             android2.2
 * 时间:               下午2:52:33
 */
package com.example.alldocumentreader.fc.hssf.usermodel;

import com.example.alldocumentreader.fc.ShapeKit;
import com.example.alldocumentreader.fc.ddf.EscherContainerRecord;
import com.example.alldocumentreader.ss.model.XLSModel.AWorkbook;

/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2013-4-1
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class HSSFLine extends HSSFSimpleShape
{

    public HSSFLine(AWorkbook workbook, EscherContainerRecord escherContainer, 
           HSSFShape parent, HSSFAnchor anchor, int shapeType)
    {
        super(escherContainer, parent, anchor);
        setShapeType(shapeType);
        processLineWidth();
        processLine(escherContainer, workbook);
        processArrow(escherContainer);
        setAdjustmentValue(escherContainer);
        processRotationAndFlip(escherContainer);
    }
    
    public Float[] getAdjustmentValue()
    {
        return adjusts;
    }
    
    public void setAdjustmentValue(EscherContainerRecord escherContainer)
    {
        adjusts = ShapeKit.getAdjustmentValue(escherContainer);
    }
    
    private Float adjusts[];
}
