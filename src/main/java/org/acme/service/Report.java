package org.acme.service;

import net.sf.jasperreports.engine.JasperPrint;

public interface Report {
    byte[] createExcel(JasperPrint fill);
    byte[] createPdf(JasperPrint fill);
    byte[] createOdt(JasperPrint fill);
}
