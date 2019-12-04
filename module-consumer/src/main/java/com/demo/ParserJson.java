package com.demo;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.*;

public class ParserJson {

    private static class ZipkinData implements Comparable<ZipkinData> {

        @Override
        public int compareTo(@NotNull ZipkinData o) {
            return (int) (this.timestamp - o.timestamp);
        }

        public static class Tags {
            @SerializedName("http.method")
            private String httpMethod;
            @SerializedName("http.path")
            private String httpPath;
            @SerializedName("mvc.controller.class")
            private String mvcControllerClass;
            @SerializedName("mvc.controller.method")
            private String mvcControllerMethod;

            public String getHttpMethod() {
                return httpMethod;
            }

            public void setHttpMethod(String httpMethod) {
                this.httpMethod = httpMethod;
            }

            public String getHttpPath() {
                return httpPath;
            }

            public void setHttpPath(String httpPath) {
                this.httpPath = httpPath;
            }

            public String getMvcControllerClass() {
                return mvcControllerClass;
            }

            public void setMvcControllerClass(String mvcControllerClass) {
                this.mvcControllerClass = mvcControllerClass;
            }

            public String getMvcControllerMethod() {
                return mvcControllerMethod;
            }

            public void setMvcControllerMethod(String mvcControllerMethod) {
                this.mvcControllerMethod = mvcControllerMethod;
            }
        }

        public static class LocalEndpoint {
            private String serviceName;

            public String getServiceName() {
                return serviceName;
            }

            public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
            }
        }

        private String traceId;
        private String parentId;
        private String id;
        private String kind;
        private String name;
        private Long timestamp;
        private Long duration;
        private LocalEndpoint localEndpoint;
        private Tags tags;
        private boolean shared;

        public Boolean getShared() {
            return shared;
        }

        public void setShared(Boolean shared) {
            this.shared = shared;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Long getDuration() {
            return duration;
        }

        public void setDuration(Long duration) {
            this.duration = duration;
        }

        public LocalEndpoint getLocalEndpoint() {
            return localEndpoint;
        }

        public void setLocalEndpoint(LocalEndpoint localEndpoint) {
            this.localEndpoint = localEndpoint;
        }

        public Tags getTags() {
            return tags;
        }

        public void setTags(Tags tags) {
            this.tags = tags;
        }
    }
    
    private static final String JSON_FILE_PATH = "module-consumer/log.json";
    private static final String OUTPUT_DIR_PATH = "";

    public static void main(String[] args) {
        try {
            File file = new File(JSON_FILE_PATH);
            TypeToken type = new TypeToken<List<List<ZipkinData>>>() {};

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();


            List<List<ZipkinData>> json = new Gson().fromJson(new FileReader(file), type.getType());
            Set<String> headers = new LinkedHashSet<>();
            int rowCount = 1;
            for (List<ZipkinData> row : json) {
                Row excelRow = sheet.createRow(rowCount++);
                Collections.sort(row);
                int cellCount = 0;
                Cell excelCell;
                for (ZipkinData cell : row) {

                    if (cellCount == 0) {
                        headers.add("Trace Id");
                        excelCell = excelRow.createCell(cellCount++);
                        excelCell.setCellValue(cell.getTraceId());
                    }

                    if (!("CLIENT".equalsIgnoreCase(cell.getKind())
                            || ("SERVER".equalsIgnoreCase(cell.getKind()) && !cell.getShared()))) {
                        continue;
                    }

                    if (cell.getTags() != null && !"/".equalsIgnoreCase(cell.getTags().getHttpPath())) {
                        headers.add(cell.getTags().getHttpPath().replace("/", "") + "(ms)");
                    } else {
                        headers.add(cell.getLocalEndpoint().getServiceName() + "(ms)");
                    }

                    excelCell = excelRow.createCell(cellCount++);
                    excelCell.setCellValue(cell.getDuration() / 1000f);
                }
            }

            Row headerRow = sheet.createRow(0);
            int cellCount = 0;
            for (String header: headers) {
                Cell cell = headerRow.createCell(cellCount);
                cell.setCellValue(header);
                sheet.autoSizeColumn(cellCount++);
            }

            File output = new File(OUTPUT_DIR_PATH + "report.csv");
            if (output.exists()) {
                output.delete();
            }

            FileOutputStream fos = new FileOutputStream(output);
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.exit(0);
    }


}
