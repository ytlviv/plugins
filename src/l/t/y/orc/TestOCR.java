package l.t.y.orc;

import java.io.File;
import java.io.IOException;

public class TestOCR {
	/** 
     * @param args 
     */  
    public static void main(String[] args) {  
        String path = "d://222.jpg";  
        try {  
            String valCode = new OCR().recognizeText(new File(path), "jpg");  
            System.out.println(valCode);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}
