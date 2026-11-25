package org.acme.dto.req;

import java.util.List;
import java.util.Map;

public class ReportioReqDTO {
    public Map<String, Object> params;
    public List<Map<String, Object>> dataSource;
    public String reportName;
}
