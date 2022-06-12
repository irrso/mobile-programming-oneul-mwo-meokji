package com.example.mobiletest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.InputStream;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Vector;

public class CreateBasicInfo {
    Vector<Vector<BasicInfo>> basicInfo = new Vector<Vector<BasicInfo>>(37);
    HashMap<String, Integer> nameToIdx = new HashMap<String, Integer>();


    public CreateBasicInfo() {
        for(int i=0; i<37; i++) basicInfo.add(new Vector<BasicInfo>());

        String[] foods = ((MainActivity)MainActivity.context).idxToName;

        // 엑셀에서 데이터 받아오기
        try {
            InputStream is = ((MainActivity)MainActivity.context).get_path();
            POIFSFileSystem fs = new POIFSFileSystem(is);
            HSSFWorkbook wb = new HSSFWorkbook(fs);

            for (int foodIdx=0; foodIdx<foods.length; foodIdx++) {
                // name -> idx 매핑
                nameToIdx.put(foods[foodIdx], foodIdx);

//                Log.d("crawling", "food idx is : " + foodIdx + " " + foods[foodIdx]); //
                Log.d("crawling", "food idx is : " + nameToIdx.get(foods[foodIdx]) + " " + foods[foodIdx]); //
                Sheet sheet = wb.getSheet(foods[foodIdx]);

                for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                    if (i == 0) continue;
                    Row row = sheet.getRow(i);
                    String[] s = new String[9];

                    if (row != null) {
                        for (int j = 0; j <= row.getPhysicalNumberOfCells(); j++) { // 블록 단위 처리
                            Cell cell = row.getCell(j);

                            if (cell == null) continue;
                            else {
                                switch(cell.getCellType()){
                                    case FORMULA:
                                        s[j] = cell.getCellFormula();
                                        break;
                                    case NUMERIC:
                                        s[j] = cell.getNumericCellValue()+"";
                                        break;
                                    case STRING:
                                        s[j] = cell.getStringCellValue()+"";
                                        break;
                                    case BLANK:
                                        s[j] = cell.getBooleanCellValue()+"";
                                        break;
                                    case ERROR:
                                        s[j] = cell.getErrorCellValue()+"";
                                        break;
                                }
                            }
                        }
                    }

                    BasicInfo temp = new BasicInfo(s);
                    basicInfo.get(foodIdx).add(temp);
                }
                Log.d("crawling", "v.get(foodIdx).size() is : " + basicInfo.get(nameToIdx.get(foods[foodIdx])).size()); //
                Log.d("crawling", "됨");

            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("crawling", "안됨");
        }
//        Log.d("crawling", v.get(2).get(1).phoneNum);
    }


}