package com.ligootech.odt.web.util;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//import sandbox.WrapToTest;

/**
 * Created by wly on 2016/2/18 11:38.
 */
//@WrapToTest
public class UnequalPages {

    public static final String DEST = "D:/unequal_pages.pdf";
    public static final String DEST_TEBLE = "D:/table_pages.pdf";
    public static final String DEST_TEST = "D:/test_pages.pdf";

    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        //new UnequalPages().createPdf(DEST);
        //new UnequalPages().createPdfTab(DEST_TEBLE);
        try {
            new UnequalPages().gettoPDFTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        Rectangle one = new Rectangle(70,140);
        Rectangle two = new Rectangle(700,400);
        document.setPageSize(one);
        document.setMargins(2, 2, 2, 2);
        document.open();
        Paragraph p = new Paragraph("Hi");
        document.add(p);

        document.setPageSize(two);
        document.setMargins(20, 20, 20, 20);
        document.newPage();
        document.add(p);
        document.close();
    }

    public void createPdfTab(String dest) throws IOException, DocumentException {
        BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
        BaseFont baseFpnt = BaseFont.createFont("STSongStd-Light" ,"UniGB-UCS2-H", false);

        Font font = new Font(baseFont);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new int[]{ 1, 2, 2, 2, 1});
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("S/N"));
        cell.setRowspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("中文名称"));
        //cell.setAccessibleAttribute();
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Age"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("SURNAME");
        table.addCell("FIRST NAME");
        table.addCell("MIDDLE NAME");
        table.addCell("1");
        table.addCell("James");
        table.addCell("Fish");
        table.addCell("Stone");
        table.addCell("17");
        document.add(table);
        document.close();


    }

    public void gettoPDFAction() throws Exception
    {

                //中文问题 , 12f,Font.NORMAL,Color.black
                BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",false );
                Font fontHead =  new Font(bfChinese, 12, Font.NORMAL );
                Font font =  new Font(bfChinese, 8, Font.NORMAL );
                // 步骤 1: 创建一个document对象
                Document document = new Document();
                // 步骤 2:
                // 我们为document创建一个监听，并把PDF流写到文件中
                PdfWriter.getInstance(document, new FileOutputStream("D:\\MyFirstTable.pdf"));
                // 步骤 3:打开文档
                document.open();
                //创建一个有4列的表格
                PdfPTable table = new PdfPTable(4);
                float[] cellWidth = new float[4];
                cellWidth[0] = 40f;
                cellWidth[1] = 80f;
                cellWidth[2] = 80f;
                cellWidth[3] = 60f;
                table.setTotalWidth(cellWidth);//设置表格的各列宽度

                table.setLockedWidth(true);
                //定义一个表格单元
                PdfPCell cell = new PdfPCell(new Paragraph("项目立项详情",fontHead)); // 标题换行 \n
                //定义一个表格单元的跨度
                cell.setColspan(4);

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//字体居中 1 居左 0  居右 2
                //把单元加到表格中
                table.addCell(cell);
                //把下面这4顺次的加入到表格中，当一行充满时候自动折行到下一行
                cell = new PdfPCell(new Paragraph("建设单位:",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("安徽建设集团第一分公司",font));
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("联系人:",font));
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("张新",font));
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("联系方式:",font));
                cell.setHorizontalAlignment(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("0551-65840453",font));
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("联系地址:",font));
        cell.setHorizontalAlignment(2);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("黄山路800号",font));
                table.addCell(cell);
                //重新定义单元格
                cell = new PdfPCell(new Paragraph("备注:",font));
                table.addCell(cell);
                //重新定义单元格
                cell = new PdfPCell(new Paragraph("第二次合作项目",font));
                //定义单元格的跨度
                cell.setColspan(3);
                //增加到表格上
                table.addCell(cell);
                //增加到文档中
                document.add(table);
                System.out.println("中文输出结束");
                // 步骤 5:关闭文档
                document.close();

    }

    /**
     * 设置最小单元格的宽度，以后自行合并
     * @throws Exception
     */
    public void gettoPDFTest() throws Exception
    {

        //中文问题 , 12f,Font.NORMAL,Color.black
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",false );
        Font fontTitle =  new Font(bfChinese, 8, Font.NORMAL );
        //Font fontHead =  new Font(bfChinese, 5, Font.BOLD ); //单元格最佳比例 5号字体 每个汉字宽度设定为 6f
        /*Font fontHead =  new Font(bfChinese, 5, Font.NORMAL ); //单元格最佳比例 5号字体 每个汉字宽度设定为 6f
        Font font =  new Font(bfChinese, 5, Font.NORMAL ); //单元格最佳比例 5号字体 每个汉字宽度设定为 6f*/

        Font fontHead =  new Font(bfChinese, 6, Font.NORMAL );
        Font font =  new Font(bfChinese, 6, Font.NORMAL );

        // 步骤 1: 创建一个document对象
        //Document document = new Document(PageSize.A4, 10, 10, 10, 10);
        Document document = new Document();
        /**
         * public Document(Rectangle pageSize,
         int marginLeft,
         int marginRight,
         int marginTop,
         int marginBottom);
         */
        // 步骤 2:
        // 我们为document创建一个监听，并把PDF流写到文件中
        PdfWriter.getInstance(document, new FileOutputStream("D:/testpdf/swFirstTable.pdf"));
        // 步骤 3:打开文档
        document.open();
        //创建一个有4列的表格  这个页面宽度不能超过 60*10 = 600
        int cellNum = 59;//TODO 最小为59  表的单元格个数由从机串数加上固有的数量  串数不足时需补充完整
        PdfPTable table = new PdfPTable(cellNum);

        //设置单元格宽度
        float[] cellWidth = new float[cellNum];
        for (int i = 0; i <cellNum; i++) {
            //cellWidth[i] = 6f;
            cellWidth[i] = 7f;
        }
        table.setTotalWidth(cellWidth);//设置表格的各列宽度
        table.setLockedWidth(true);
        /**************************************
         * 头文件部分
         *************************************/
        PdfPCell cell = new PdfPCell(new Paragraph("软件生产任务单", fontTitle));
        //定义一个表格单元的跨度
        cell.setColspan(cellNum);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//字体居中 1 居左 0  居右 2
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        //把单元加到表格中
        table.addCell(cell);
        /**************************************
         * 第1行：基本信息
         *************************************/
        cell = new PdfPCell(new Paragraph("编制日期",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(10f);// 设置高度
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2016年02月17日",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("订单编号",fontHead));
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("LG201602170301",font));
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("软件交付日期",fontHead));
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2016年02月17日",font));
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("生产交付日期",fontHead));
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2016年02月17日",font));
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("合同编号",fontHead));
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("PU201602039",font));
        cell.setColspan(cellNum - 52);
        table.addCell(cell);
        /**************************************
         * 第2行：审核、会签人员签名信息
         *************************************/
        String spaceStr = "                                        ";
        cell = new PdfPCell(new Paragraph("编制：武佳" + spaceStr + "会签：" + spaceStr + "审核：" + spaceStr + "批准：",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //水平居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setFixedHeight(12f);// 设置高度
        cell.setColspan(cellNum);
        table.addCell(cell);

        /**************************************
         * 第3行：客户信息 1
         *************************************/
        cell = new PdfPCell(new Paragraph("客户名称",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(10f);// 设置高度
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("中航锂电（洛阳）有限公司",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(16);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("联系电话",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("37960697538",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("地址",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("河南省洛阳高新技术开发区滨河北路66号",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(cellNum - 41);
        table.addCell(cell);

        /**************************************
         * 第5行：客户信息 2
         *************************************/
        cell = new PdfPCell(new Paragraph("项目描述",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(10f);// 设置高度
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("D135M",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(16);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("销售代表",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("陶洪涛",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("数量",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("1套",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(cellNum - 41);
        table.addCell(cell);

        /**************************************
         * 第6行：电池信息 1
         *************************************/
        cell = new PdfPCell(new Paragraph("基本信息",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        //cell.setFixedHeight(10f);// 设置高度
        cell.setRowspan(3);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("车辆类型",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(10f);// 设置高度
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("纯电动",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("车辆型号",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("中巴车",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("电池厂商",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("中航锂电（洛阳）有限公司",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(cellNum - 41);
        table.addCell(cell);

        /**************************************
         * 第7行：电池信息 2
         *************************************/
        cell = new PdfPCell(new Paragraph("电芯类型",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(10f);// 设置高度
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("磷酸铁锂",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("电池组容量",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("288AH",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("供电电压",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("24V",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(cellNum - 41);
        table.addCell(cell);

        /**************************************
         * 第8行：电池信息 3
         *************************************/
        cell = new PdfPCell(new Paragraph("电池组总串数",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(10f);// 设置高度
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("96串",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("箱体数目",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2箱",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("产品标识",fontHead));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("标准标识",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(cellNum - 41);
        table.addCell(cell);

        /**************************************
         * 第9行：模块信息 1
         *************************************/
        cell = new PdfPCell(new Paragraph("模\n块\n信\n息",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setRowspan(12);
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("1", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setRowspan(3);
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("主控模块", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setRowspan(3);
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("硬件版本号", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("BC52A", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("充电控制方式", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("继电器     CAN总线", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("放电控制方式", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("CAN总线", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 44);
        table.addCell(cell);

        /**************************************
         * 第10行：模块信息 2
         *************************************/
        cell = new PdfPCell(new Paragraph("软件版本号", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("BC52A", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("充电CAN协议", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("GB 27930/2011", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("匹配电阻", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("1", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("放电CAN协议", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(7);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("客户协议", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("匹配电阻", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("1", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        cell.setColspan(cellNum - 58);
        table.addCell(cell);

        /**************************************
         * 第11行：模块信息 3
         *************************************/
        cell = new PdfPCell(new Paragraph("备注", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("集成数据存储功能，带安装边。", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 11);
        table.addCell(cell);

        /**************************************
         * 第12行：模块信息 4 从机显示 1
         *************************************/
        cell = new PdfPCell(new Paragraph("2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setRowspan(5);
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("从控模块", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setRowspan(5);
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("从控版本号", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setRowspan(2);
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("BM5124A", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setRowspan(2);
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("每个模块\n串联电池数", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setRowspan(2);
        cell.setColspan(5);
        table.addCell(cell);

        //TODO 从机电池串数动态显示
        cell = new PdfPCell(new Paragraph("M1", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(2);//电池组数量 为占用单元格数
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("M2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(2);//电池组数量 为占用单元格数
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("M3", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(2);//电池组数量 为占用单元格数
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("M4", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(2);//电池组数量 为占用单元格数
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 29);
        table.addCell(cell);

        /**************************************
         * 第13行：模块信息 5 从机显示 2
         *************************************/

        cell = new PdfPCell(new Paragraph("24", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(2);//电池组数量 为占用单元格数
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("24", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(2);//电池组数量 为占用单元格数
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("24", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(2);//电池组数量 为占用单元格数
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("24", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(2);//电池组数量 为占用单元格数
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 29);
        table.addCell(cell);

        /**************************************
         * 第14行：模块信息 6 从机显示 3
         *************************************/
        cell = new PdfPCell(new Paragraph("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setRowspan(2);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("采集单元\n采集单体数", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        //TODO 对应上面的电池组 确保一个单元格使用 左右都加上间距设定
        cell = new PdfPCell(new Paragraph("28", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("12", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("12", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("12", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("12", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("12", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("12", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("12", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 29);
        table.addCell(cell);

        /**************************************
         * 第15行：模块信息 7 从机显示 4
         *************************************/
        cell = new PdfPCell(new Paragraph("采集单元\n温感数", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        //TODO 对应上面的电池组
        cell = new PdfPCell(new Paragraph("2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("2", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 29);
        table.addCell(cell);

        /**************************************
         * 第16行：模块信息 8 从机显示 5
         *************************************/
        cell = new PdfPCell(new Paragraph("外壳尺寸",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("备注:带安装边\n" +
                "注意！从机需要单独标签：M1标“2#-上”，M2标“2#-下”，M3标“1#-上”，M4标“1#-下”，此标示打印在从控“M5124A　 D135F88MA”的下一行。",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 11);
        table.addCell(cell);

        /**************************************
         * 第17行：模块信息 9 显示屏
         *************************************/
        cell = new PdfPCell(new Paragraph("3",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        cell.setPaddingLeft(0.1f);
        cell.setPaddingRight(0.1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("显示屏",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("型号",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("BS03524",font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("供电电压",font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("24V",font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("显示语言",font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("中文",font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(3);
        cell.setPaddingLeft(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("开机画面", font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("力高画面", font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(4);
        cell.setPaddingLeft(1f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("数量", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("1", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("主界面", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(3);
        cell.setPaddingLeft(0.5f);
        cell.setPaddingRight(0.5f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("数字式", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(3);
        cell.setPaddingLeft(0.5f);
        cell.setPaddingRight(0.5f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("屏显示",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(3);
        cell.setPaddingLeft(0.5f);
        cell.setPaddingRight(0.5f);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("力高界面",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 53);
        table.addCell(cell);

        /**************************************
         * 第18行：模块信息 10 其他相关信息 1
         *************************************/
        cell = new PdfPCell(new Paragraph("",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("名称",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("型号",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("数量",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("备注",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 29);
        table.addCell(cell);
        /************************************************************
         * 固定显示信息结束
         ************************************************************/
        //TODO 列表显示其他模块信息
        /**************************************
         * 第19行：模块信息 11 其他相关信息 2
         *************************************/
        cell = new PdfPCell(new Paragraph("4",font)); //编号加上前面的 3 所以从4开始
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("电流传感器",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("FS500E2T",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("1个",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("单量程",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 29);
        table.addCell(cell);

        /**************************************
         * 第20行：模块信息 12 其他相关信息 3
         *************************************/
        cell = new PdfPCell(new Paragraph("5",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(1);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("绝缘模块",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("BL51A",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("1个",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(9);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("低压版",font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(cellNum - 29);
        table.addCell(cell);

        /**************************************
         * 第21行：  控制策略清单信息
         *************************************/











        //增加到文档中
        document.add(table);
        System.out.println("任务单生成好了!!");
        // 步骤 5:关闭文档
        document.close();

    }


}