import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout.Alignment;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDownload {
	
	// title = 엑셀 헤더
	// data = 엑셀 데이터
	public void excelDownload(List<String> title, List<String> column, List<Map<String, Object>> data, String filePreName) {
		
		String fileName = filePreName + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String filepath = "";
		
		//파일 생성
		File dir = new File(filepath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File outputFile = new File(filepath + fileName + ".xlsx");
		try (FileOutputStream fos = new FileOutputStream(outputFile);) {
			Workbook workbook = new XSSFWorkbook();
			
			Sheet sheet = workbook.createSheet(fileName);
			
			// 제목 폰트, 스타일
			XSSFCellStyle styleHeader = createHeaderCellStyle((XSSFWorkbook) workbook);
			
			// 결과 입력 부분 스타일, 폰트
			XSSFCellStyle styleBody = createBodyCellStyle((XSSFWorkbook) workbook);
			
			resultHeaders(sheet, styleHeader, title);
			resultBody(sheet, styleBody, column, data);
			
			workbook.write(fos);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 헤더부분 스타일 생성
	 * @param workBook
	 * @return
	 */
	public XSSFCellStyle createHeaderCellStyle(XSSFWorkbook workBook) {
		Font fontHeader = workBook.createFont();
		fontHeader.setFontHeightInPoints((short) 12);
		fontHeader.setBold(true);
		
		CellStyle styleHeader = workBook.createCellStyle();
		styleHeader.setFont(fontHeader);
		styleHeader.setBorderTop(BorderStyle.THIN);
		styleHeader.setTopBorderColor((short) 8);
		styleHeader.setBorderRight(BorderStyle.THIN);
		styleHeader.setRightBorderColor((short) 8);
		styleHeader.setBorderBottom(BorderStyle.THIN);
		styleHeader.setBottomBorderColor((short) 8);
		styleHeader.setBorderLeft(BorderStyle.THIN);
		styleHeader.setLeftBorderColor((short) 8);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
		styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		return (XSSFCellStyle) styleHeader;
	}
	
	/**
	 * 바디부분 스타일 생성
	 * @param workBook
	 * @return
	 */
	public XSSFCellStyle createBodyCellStyle(XSSFWorkbook workBook) {
		Font fontBody = workBook.createFont();
		fontBody.setFontHeightInPoints((short) 9);
		
		CellStyle styleBody = workBook.createCellStyle();
		styleBody.setFont(fontBody);
		styleBody.setBorderTop(BorderStyle.THIN);
		styleBody.setTopBorderColor((short) 8);
		styleBody.setBorderRight(BorderStyle.THIN);
		styleBody.setRightBorderColor((short) 8);
		styleBody.setBorderBottom(BorderStyle.THIN);
		styleBody.setBottomBorderColor((short) 8);
		styleBody.setBorderLeft(BorderStyle.THIN);
		styleBody.setLeftBorderColor((short) 8);
		styleBody.setAlignment(HorizontalAlignment.CENTER);
		styleBody.setVerticalAlignment(VerticalAlignment.CENTER);
		
		return (XSSFCellStyle) styleBody;
	}
	
	/**
	 * 헤더 내용
	 * @param resultSheet
	 * @param styleHeader
	 */
	private void resultHeaders(Sheet resultSheet, XSSFCellStyle styleHeader, List<String> title) {
		Row row = resultSheet. createRow(0);
		
		// 헤더 이름 채우기
		int cnt = title.size();
		Cell cell = null;
		
		for(int i=0; i<cnt; i++) {
			cell = row.createCell(i);
			cell.setCellValue (title.get(i).toString());
			cell.setCellStyle(styleHeader);
		}
	}
	
	/**
	 * 바디 내용
	 * @param resultSheet
	 * @param styleBody
	 * @param resultList
	 */
	private void resultBody(Sheet resultSheet, XSSFCellStyle styleBody, List<String> column, List<Map<String, Object>> resultList) {
		int cellCnt = 0;
		
		for (int i = 0; i < resultList.size(); ++i) {
			Map<String, Object> data = resultList.get(i);
			Row row = resultSheet.createRow(1 + i);
			
			cellCnt = 0;
			Cell cell = row.createCell(cellCnt);
			cell.setCellValue(Integer.toString(i + 1)); // No
			cell.setCellStyle(styleBody);
			
			for(int j = 0; j < column.size(); j++) {
				cell = row.createCell(++cellCnt);
				cell.setCellValue(data.get(column.get(j)).toString());
				cell.setCellStyle(styleBody);
			}
		}
		
		for(int i = 0; i <= cellCnt; i++){ 
			resultSheet.autoSizeColumn((short)i);
			resultSheet.setColumnWidth(i, (resultSheet.getColumnWidth(i)) + 1000 );
		}
	}
}
