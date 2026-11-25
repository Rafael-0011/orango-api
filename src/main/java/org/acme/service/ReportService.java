package org.acme.service;

import io.quarkiverse.jasperreports.repository.ReadOnlyStreamingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@ApplicationScoped
public class ReportService implements Report {


    @Inject
    ReadOnlyStreamingService jasperService;

    public JasperPrint fill(
            Map<String, Object> params,
            List<Map<String, Object>> dataSource,
            String reportName
    ) throws JRException {
        try (var inputStream = getClass().getResourceAsStream("/" + reportName)){
            return JasperFillManager.fillReport(inputStream, params, new JRBeanCollectionDataSource(dataSource, false));

        }catch (Exception e) {
            if (e instanceof JRException) {
                throw (JRException) e;
            }
            throw new JRException("Error loading report template: " + reportName, e);
        }
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public byte[] createOdt(JasperPrint fill) {
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JROdtExporter exporter = new JROdtExporter();
            exporter.setExporterInput(new SimpleExporterInput(fill));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório ODT", e);
        }
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public byte[] createPdf(JasperPrint fill) {
        try {
            return JasperExportManager.exportReportToPdf(fill);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório PDF", e);
        }
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public byte[] createExcel(JasperPrint fill) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(fill));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configExcel(configuration, exporter);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de desempenho  descritor.", e);
        }
    }

    private void configExcel(SimpleXlsxReportConfiguration configuration, JRXlsxExporter exporter) throws JRException {
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        configuration.setOnePagePerSheet(false);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        configuration.setWhitePageBackground(false);
        configuration.setIgnorePageMargins(true);

        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }

    private String formatarPeriodo(LocalDate dataSaida, LocalDate dataChegada) {
        DateTimeFormatter diaFormatter = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter diaMesAnoFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return String.format("%s à %s",
                dataSaida.format(diaFormatter),
                dataChegada.format(diaMesAnoFormatter));
    }
}
