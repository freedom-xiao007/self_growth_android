package com.example.selfgrowth.ui.custum;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.selfgrowth.R;

import java.util.ArrayList;
import java.util.List;

public class TableView extends View {

    /**
     * 单元格基准宽度，设权重的情况下，为最小单元格宽度
     */
    private float unitColumnWidth;
    private float rowHeight;
    private float dividerWidth;
    private int dividerColor;
    private float textSize;
    private int textColor;
    private int headerColor;
    private float headerTextSize;
    private int headerTextColor;

    private int rowCount;
    private int columnCount;

    private Paint paint;

    private float[] columnLefts;
    private float[] columnWidths;

    private int[] columnWeights;
    private List<String[]> tableContents;

    public TableView(Context context) {
        super(context);
        init(null);
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        tableContents = new ArrayList<>();
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TableView);
            unitColumnWidth = typedArray.getDimensionPixelSize(R.styleable.TableView_unitColumnWidth, 0);
            rowHeight = typedArray.getDimensionPixelSize(R.styleable.TableView_rowHeight, dip2px(getContext(), 36));
            dividerWidth = typedArray.getDimensionPixelSize(R.styleable.TableView_dividerWidth, 1);
            dividerColor = typedArray.getColor(R.styleable.TableView_dividerColor, Color.parseColor("#E1E1E1"));
            textSize = typedArray.getDimensionPixelSize(R.styleable.TableView_textSize, dip2px(getContext(), 10));
            textColor = typedArray.getColor(R.styleable.TableView_textColor, Color.parseColor("#999999"));
            headerColor = typedArray.getColor(R.styleable.TableView_headerColor, Color.parseColor("#00ffffff"));
            headerTextSize = typedArray.getDimensionPixelSize(R.styleable.TableView_headerTextSize, dip2px(getContext(), 10));
            headerTextColor = typedArray.getColor(R.styleable.TableView_headerTextColor, Color.parseColor("#999999"));
            typedArray.recycle();
        } else {
            unitColumnWidth = 0;
            rowHeight = dip2px(getContext(), 36);
            dividerWidth = 1;
            dividerColor = Color.parseColor("#E1E1E1");
            textSize = dip2px(getContext(), 10);
            textColor = Color.parseColor("#999999");
            headerColor = Color.parseColor("#00ffffff");
            headerTextSize = dip2px(getContext(), 10);
            headerTextColor = Color.parseColor("#111111");
        }
        setHeader("Header1", "Header2").addContent("Column1", "Column2");
        initTableSize();
    }

    private int dip2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //通过权重计算最小单元格宽度
        int weightSum = 0;
        if (columnWeights != null) {
            for (int i = 0; i < columnCount; i++) {
                if (columnWeights.length > i) {
                    weightSum += columnWeights[i];
                } else {
                    weightSum += 1;
                }
            }
        } else {
            //默认等分，每列权重为1
            weightSum = columnCount;
        }

        //计算宽度及列宽
        float width;
        if (unitColumnWidth == 0) {
            //未设置宽度，根据控件宽度来确定最小单元格宽度
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            width = getMeasuredWidth();
            unitColumnWidth = (width - (columnCount + 1) * dividerWidth) / weightSum;
        } else {
            //设置了最小单元格宽度
            width = dividerWidth * (columnCount + 1) + unitColumnWidth * weightSum;
        }
        //计算高度
        float height = (dividerWidth + rowHeight) * rowCount + dividerWidth;

        setMeasuredDimension((int) width, (int) height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateColumns();
        drawHeader(canvas);
        drawFramework(canvas);
        drawContent(canvas);
    }

    /**
     * 画表头
     *
     * @param canvas
     */
    private void drawHeader(Canvas canvas) {
        paint.setColor(headerColor);
        canvas.drawRect(dividerWidth, dividerWidth, getWidth() - dividerWidth, rowHeight + dividerWidth, paint);
    }

    /**
     * 画整体表格框架
     */
    private void drawFramework(Canvas canvas) {
        paint.setColor(dividerColor);
        for (int i = 0; i < columnCount + 1; i++) {
            if (i == 0) {
                //最左侧分割线
                canvas.drawRect(0, 0, dividerWidth, getHeight(), paint);
                continue;
            }
            if (i == columnCount) {
                //最右侧分割线
                canvas.drawRect(getWidth() - dividerWidth, 0, getWidth(), getHeight(), paint);
                continue;
            }
            canvas.drawRect(columnLefts[i], 0, columnLefts[i] + dividerWidth, getHeight(), paint);
        }
        for (int i = 0; i < rowCount + 1; i++) {
            canvas.drawRect(0, i * (rowHeight + dividerWidth), getWidth(), i * (rowHeight + dividerWidth) + dividerWidth, paint);
        }
    }

    /**
     * 画内容
     */
    private void drawContent(Canvas canvas) {
        for (int i = 0; i < rowCount; i++) {
            final String[] rowContent = tableContents.size() > i ? tableContents.get(i) : new String[0];
            if (i == 0) {
                //设置表头文字画笔样式
                paint.setColor(headerTextColor);
                paint.setTextSize(headerTextSize);
            }
            for (int j = 0; j < columnCount; j++) {
                if (rowContent.length > j) {
                    canvas.drawText(rowContent[j],
                            columnLefts[j] + columnWidths[j] / 2,
                            getTextBaseLine(i * (rowHeight + dividerWidth), paint),
                            paint);
                }
            }
            if (i == 0) {
                //恢复表格文字画笔样式
                paint.setColor(textColor);
                paint.setTextSize(textSize);
            }
        }
    }

    /**
     * 计算每列左端坐标及列宽
     */
    private void calculateColumns() {
        columnLefts = new float[columnCount];
        columnWidths = new float[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnLefts[i] = getColumnLeft(i);
            columnWidths[i] = getColumnWidth(i);
        }
    }

    private float getColumnLeft(int columnIndex) {
        if (columnWeights == null) {
            return columnIndex * (unitColumnWidth + dividerWidth);
        }
        //计算左边的权重和
        int weightSum = 0;
        for (int i = 0; i < columnIndex; i++) {
            if (columnWeights.length > i) {
                weightSum += columnWeights[i];
            } else {
                weightSum += 1;
            }
        }
        return columnIndex * dividerWidth + weightSum * unitColumnWidth;
    }

    private float getColumnWidth(int columnIndex) {
        if (columnWeights == null) {
            return unitColumnWidth;
        }
        int weight = columnWeights.length > columnIndex ? columnWeights[columnIndex] : 1;
        return weight * unitColumnWidth;
    }

    private float getTextBaseLine(float rowStart, Paint paint) {
        final Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (rowStart + (rowStart + rowHeight) - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    /**
     * 设置表格内容
     */
    public TableView clearTableContents() {
        columnWeights = null;
        tableContents.clear();
        return this;
    }

    /**
     * 设置每列的权重
     *
     * @param columnWeights
     * @return
     */
    public TableView setColumnWeights(int... columnWeights) {
        this.columnWeights = columnWeights;
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers
     */
    public TableView setHeader(String... headers) {
        tableContents.add(0, headers);
        return this;
    }

    /**
     * 设置表格内容
     */
    public TableView addContent(String... contents) {
        tableContents.add(contents);
        return this;
    }

    /**
     * 设置表格内容
     */
    public TableView addContents(List<String[]> contents) {
        tableContents.addAll(contents);
        return this;
    }

    /**
     * 初始化行列数
     */
    private void initTableSize() {
        rowCount = tableContents.size();
        if (rowCount > 0) {
            //如果设置了表头，根据表头数量确定列数
            columnCount = tableContents.get(0).length;
        }
    }

    /**
     * 设置数据后刷新表格
     */
    public void refreshTable() {
        initTableSize();
        requestLayout();
//         invalidate();
    }
}
