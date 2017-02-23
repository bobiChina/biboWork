package com.ligootech.webdtu.util.excel;

import com.ligootech.webdtu.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wly on 2015/12/19 16:13.
 */
public class RowReaderImpl implements IRowReader {
    private int count = 0;
    private List<List<String>> rowList = new ArrayList<List<String>>();

    public int getCount() {
        return count;
    }

    public List<List<String>> getRowList() {
        return rowList;
    }

    @Override
    public void getRows(int sheetIndex, int curRow, List<String> rowlist) throws Exception {
        //未做分sheet处理,目前全部在一起
        count++;
        if (null != rowlist && rowlist.size() > 0) {
            if (!"".equals(StringUtil.null2String(rowlist.get(0)).trim())) {
                ArrayList<String> slist = new ArrayList<String>();
                for(int i=0;i<rowlist.size();i++){
                    slist.add(StringUtil.null2String(rowlist.get(i)));
                }
                rowList.add(slist);
            }
        }
    }
}
