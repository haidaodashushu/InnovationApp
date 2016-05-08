package com.innovation.app.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author WangZhengkui on 2016-05-08 13:27
 */
public class ChartLineView extends View {
    Context mContext;
    float[] xPoints;
    float[] yPoints;
    float[] line1Points;
    float[] line2Points;

    Paint paint,tempPaint,humPaint;
    private String[] x;
    private String[] y;
    private String[] temp;
    private String[] humidity;

    int left, bottom;
    int right, top;
    int xOffset = 50,yOffset=150;

    public ChartLineView(Context context) {
        this(context, null);
    }

    public ChartLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        tempPaint = new Paint();
        tempPaint.setAntiAlias(true);
        tempPaint.setColor(0xff7CB5EC);
        tempPaint.setTextSize(18);

        humPaint = new Paint();
        humPaint.setAntiAlias(true);
        humPaint.setColor(0xff434348);
        humPaint.setTextSize(18);

    }

    public void setData(final String[] x,String[] y,String[] temp,String[] humidity) {
        Log.i("ChartLineView","x = "+ Arrays.toString(x)+",y = "+Arrays.toString(temp));
        this.x = x;
        this.y = y;
        this.temp = temp;
        this.humidity = humidity;
        invalidate();
    }


    RectF rectF = new RectF();
    int count = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        if (x == null || y == null || x.length==0||y.length==0) {
            return;
        }
        Log.i("ChartLineView","onDraw");
        left = getPaddingLeft();
        right = getWidth()-10-getPaddingRight();
        bottom = getHeight()-yOffset;
        top = getPaddingTop();
        Log.i("ChartLineView","right = "+right+",bottom = "+bottom);

        //画Y轴
        float yLength = bottom / y.length;
        for (int i = 0; i < y.length; i++) {
            float startX = left ;
            float startY = bottom - i * yLength;
            float textX = startX;
            float textY = startY + paint.getFontMetrics().descent;
            paint.setColor(0xff606060);
            canvas.drawText(y[i], textX+10, textY, paint);
            if (i != 0) {
                paint.setColor(0xffd8d8d8);
                canvas.drawLine(startX + xOffset, startY, right, startY, paint);
            }
        }
        //画X轴
        float xLength = (right-left - xOffset) / x.length;
        for (int i = 0; i < x.length; i++) {
            float startX = left + xOffset + i * xLength;
            float startY = bottom;
            paint.setColor(0xffc0d0e0);
            //分段画出X轴
            canvas.drawLine(startX, startY, startX + xLength, startY, paint);
            //画x轴上的每个短线
            canvas.drawLine(startX, startY, startX, startY + 15, paint);
            if (i == x.length - 1) {
                canvas.drawLine(startX + xLength, startY, startX + xLength, startY + 15, paint);
            }

            //画文字
            paint.setColor(0xff606060);
            canvas.save();
            float textX = startX + (xLength - getTextWidth(paint, x[i])) / 2;

            float textY = startY + 15 + getTextHeight();
            rectF.left = textX;
            rectF.top = textY - 30;
            rectF.right = textX + getTextWidth(paint, x[i]);
            rectF.bottom = textY + getTextHeight();
            canvas.clipRect(rectF);
            //倾斜45度
            canvas.rotate(-45, rectF.centerX(), rectF.centerY());
            canvas.drawText(x[i], textX, textY, paint);
            canvas.restore();
        }

        //画文字说明
        String text = "温度（˚C）";
        float length = 20;
        float startX = getWidth() / 2 - 100 - 2 * length - getTextWidth(tempPaint, text) - 3 * 2 - 6;
        float startY = bottom + 100;

        canvas.drawLine(startX, startY, startX + length, startY, tempPaint);
        drawCircleDot(canvas, startX + length + 6, startY, tempPaint);
        canvas.drawLine(startX + length + 12, startY, startX + 2 * length + 12, startY, tempPaint);
        canvas.drawText(text, startX + 2 * length + 12, startY - tempPaint.getFontMetrics().descent + getTextHeight() / 2, tempPaint);

        text = "湿度";
        startX = getWidth() / 2 + 100 + 2 * length + getTextWidth(tempPaint, text) + 3 * 2 + 6;
        startY = bottom + 100;

        canvas.drawLine(startX, startY, startX + length, startY, humPaint);
        canvas.drawCircle(startX + length + 6, startY, 3, humPaint);
        drawDiamondDot(canvas, startX + length + 6, startY, humPaint);
        canvas.drawLine(startX + length + 12, startY, startX + 2 * length + 12, startY, humPaint);
        canvas.drawText(text, startX + 2 * length + 12, startY - humPaint.getFontMetrics().descent + getTextHeight() / 2, humPaint);


        float yNumLength = (Float.parseFloat(y[y.length-1])-Float.parseFloat(y[0]))/y.length;
        if (count < temp.length) {
            drawLines(temp, count++, 0, xLength, yLength, yNumLength,tempPaint, canvas);
            drawLines(humidity, count++, 0, xLength, yLength,yNumLength, humPaint, canvas);
            postInvalidateDelayed(60);
        } else {
            drawLines(temp, temp.length, 0, xLength, yLength, yNumLength,tempPaint, canvas);
            drawLines(humidity, humidity.length, 0, xLength, yLength,yNumLength, humPaint, canvas);
        }
    }

    public void drawLines(String[] line2,int count,int dot, float xLength,float yLength,float yNumLength,Paint paint,Canvas canvas) {
        //湿度线
        for (int i = 0; i < count; i++) {
            float startX = left +xOffset +xLength/2+i*xLength;
            float startY = bottom-(Float.parseFloat(line2[i])-Float.parseFloat(y[0]))/yNumLength*yLength;
            float nextX = 0;
            float nextY = 0;
            if (i==line2.length-1){
                nextX = startX;
                nextY = startY;
            }else{
                nextX = startX+xLength;
                nextY = bottom-(Float.parseFloat(line2[i+1])-Float.parseFloat(y[0]))/yNumLength*yLength;;
            }
            canvas.drawLine(startX, startY, nextX, nextY, paint);

            if (dot == 0) {
                drawCircleDot(canvas, startX, startY,paint);
            } else {
                drawDiamondDot(canvas,startX,startY,paint);
            }
            //数值
            String num = formatFloat(line2[i]);
            startX -=getTextWidth(paint,num)/2;
            canvas.drawText(num,startX,startY-8,paint);
        }

    }


    public float getTextWidth(Paint paint, String text) {
        int iRet = 0;
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        for (int j = 0; j < text.length(); j++) {
            iRet += Math.ceil(widths[j]);
        }
        return iRet;
    }

    private float getTextHeight() {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.descent-fontMetrics.ascent;
    }


    private void drawDiamondDot(Canvas canvas,float startX,float startY,Paint paint) {
        RectF rectF = new RectF(startX-6,startY-6,startX+6,startY+6);
        canvas.save();
        canvas.clipRect(rectF);
        canvas.rotate(-45, rectF.centerX(), rectF.centerY());
        rectF.inset(3, 3);
        canvas.drawRect(rectF, paint);
        canvas.restore();
    }
    private void drawCircleDot(Canvas canvas,float startX,float startY,Paint paint) {
        canvas.drawCircle(startX, startY, 3, paint);
    }
    public String formatFloat(String sFloat) {
        float value = Float.parseFloat(sFloat);
        DecimalFormat format = new DecimalFormat("##");
        return format.format(value);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        Log.i("ChartLineView", "onConfigureationChanged = "+newConfig.orientation);
        postInvalidateDelayed(60);
        super.onConfigurationChanged(newConfig);
    }
}
