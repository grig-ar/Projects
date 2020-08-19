package ru.nsu.fit.g16203.grigorovich.controller.FilterFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FilterMaker {
    private Map<String, String> classes = new HashMap<>();

    public void init() {
        String buf = "";
        try (InputStream ist = FilterMaker.class.getResourceAsStream("filters.txt");
             ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ist.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            buf = result.toString("UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        buf = buf.replaceAll("\r\n", " ");
        buf = buf.replaceAll("\n", " ");
        String[] pd = buf.split(" ");
        for (int i = 0; i < pd.length - 1; i += 2) {
            classes.put(pd[i], pd[i + 1]);
        }
    }

    public Filter createFilter(String filterName) {
        for (Map.Entry entry : classes.entrySet()) {
            if (entry.getKey().equals(filterName)) {
                try {
                    Class<?> c = Class.forName(entry.getValue().toString());
                    Object obj = c.newInstance();
                    return (Filter) obj;
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
