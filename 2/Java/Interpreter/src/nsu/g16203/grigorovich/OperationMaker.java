package nsu.g16203.grigorovich;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

class OperationMaker {
    private Map<String, String> classes = new HashMap<>();
    public void init () {
        InputStream ist = OperationMaker.class.getResourceAsStream("operations.txt");
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        String buf = "";
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ist.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            buf = result.toString("UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            ist.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buf = buf.replaceAll("\r\n", " ");
        String[] pd = buf.split(" ");
        for(int i = 0; i < pd.length-1; i+=2) {
            classes.put(pd[i], pd[i+1]);
        }
    }
    public Operation createOperation(String op) {
        for (Map.Entry entry : classes.entrySet() ) {
            if (entry.getKey().equals(op)) {
                try {
                    Class<?> c = Class.forName(entry.getValue().toString());
                    Object obj = c.newInstance();
                    return (Operation) obj;
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
