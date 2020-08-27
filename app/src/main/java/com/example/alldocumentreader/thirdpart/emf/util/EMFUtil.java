package com.example.alldocumentreader.thirdpart.emf.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.alldocumentreader.thirdpart.emf.EMFHeader;
import com.example.alldocumentreader.thirdpart.emf.EMFInputStream;
import com.example.alldocumentreader.thirdpart.emf.EMFRenderer;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class EMFUtil 
{
	/**
	 * convert EMF picture ot PNG picture
	 * @param strSrc
	 * @param strDst
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public static Bitmap convert(String strSrc, String strDst, int width,
                                 int height) throws Exception
	{
		FileInputStream is = new FileInputStream(strSrc);
		EMFInputStream inputStream = new EMFInputStream(is,
				EMFInputStream.DEFAULT_VERSION);
		EMFHeader header = inputStream.readHeader();
		int frameW = (int) header.getFrame().getWidth();
		int frameH = (int) header.getFrame().getHeight();

		int deviceW = header.getDevice().width;
		int deviceH = header.getDevice().height;

		int millimetersW = (int) header.getMillimeters().getWidth();
		int millimetersH = (int) header.getMillimeters().getHeight();

		int fileWidth = frameW * deviceW / millimetersW / 100 + 1;
		int fileHeight = frameH * deviceH / millimetersH / 100 + 1;
		
		int frameX = (int) header.getFrame().x;
		int frameY = (int) header.getFrame().y;

		int x = frameX * deviceW / millimetersW / 100;
		int y = frameY * deviceH / millimetersH / 100;

		EMFRenderer emfRenderer = new EMFRenderer(inputStream);
		Bitmap bitmap = null;
		Canvas canvas = null;
		if(width * height < fileWidth * fileHeight)
		{
			bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);

			canvas = new Canvas(bitmap);
			float sx = (float) width / fileWidth;
			float sy = (float) height / fileHeight;
			canvas.scale(sx, sy);
		}
		else
		{
			bitmap = Bitmap.createBitmap(fileWidth, fileHeight,
					Bitmap.Config.ARGB_8888);
			
			canvas = new Canvas(bitmap);
		}

		canvas.translate(-x, -y);
		emfRenderer.paint(canvas);
		
		FileOutputStream out = new FileOutputStream(strDst);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		out.close();
		return bitmap;
	}
}
