package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.EstimateItemResponse;
import kamilzadroga.BudowaKosztorysu.dto.EstimateResponse;
import kamilzadroga.BudowaKosztorysu.exception.BudowaKosztorysuNotFoundException;
import kamilzadroga.BudowaKosztorysu.model.Client;
import kamilzadroga.BudowaKosztorysu.model.Project;
import kamilzadroga.BudowaKosztorysu.model.User;
import kamilzadroga.BudowaKosztorysu.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EstimatePdfService {

    private final EstimateService estimateService;
    private final ProjectRepository projectRepository;
    private final TranslationService translationService;
    private final CurrentUserService currentUserService;

    public byte[] generatePdf(Long estimateId, String lang) {
        User currentUser = currentUserService.getCurrentUser();
        EstimateResponse estimate = estimateService.getById(estimateId);
        Project project = projectRepository.findById(estimate.projectId())
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(estimate.projectId()));
        Client client = project.getClient();

        List<String> labels = new ArrayList<>(List.of(
                "Kosztorys", "Klient", "Data", "Nazwa", "Typ", "Jednostka",
                "Ilość", "Cena/jedn.", "Wartość", "Suma", "Materiał", "Robocizna",
                "m", "m²", "m³", "worek/wiadro","godz.", "Firma", "Adres", "Telefon"
        ));

        List<String> itemNames = estimate.items().stream()
                .map(EstimateItemResponse::name)
                .toList();
        labels.addAll(itemNames);

        List<String> translated = lang.equals("de") ? translationService.translate(labels, "de") : labels;

        Map<String, String> t = new HashMap<>();
        for (int i = 0; i < labels.size(); i++) {
            t.put(labels.get(i), translated.get(i));

        }



        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);

            PdfPCell titleCell = new PdfPCell(new Paragraph(t.get("Kosztorys") + " - " + estimate.projectName()));
            titleCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(titleCell);

            PdfPCell dateCell = new PdfPCell(new Paragraph(LocalDate.now().toString()));
            dateCell.setBorder(Rectangle.NO_BORDER);
            dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(dateCell);

            PdfPCell clientCell = new PdfPCell();
            clientCell.setBorder(Rectangle.NO_BORDER);
            clientCell.addElement(new Paragraph(" "));
            clientCell.addElement(new Paragraph(t.get("Klient") + ": " + client.getName()));
            headerTable.addCell(clientCell);

            PdfPCell companyCell = new PdfPCell();
            companyCell.setBorder(Rectangle.NO_BORDER);
            companyCell.addElement(new Paragraph(t.get("Firma") + ": " + currentUser.getCompanyName()));
            if(currentUser.getAddress() != null && !currentUser.getAddress().isBlank()) {
                companyCell.addElement(new Paragraph(t.get("Adres") + ": " + currentUser.getAddress()));
            }
            if(currentUser.getPhone() != null && !currentUser.getPhone().isBlank()) {
                companyCell.addElement(new Paragraph(t.get("Telefon") + ": " + currentUser.getPhone()));
            }
            if(currentUser.getNip() != null && !currentUser.getNip().isBlank()) {
                companyCell.addElement(new Paragraph("NIP: " + currentUser.getNip()));
            }
            headerTable.addCell(companyCell);

            document.add(headerTable);
            document.add(new Paragraph(" "));



            PdfPTable table = new PdfPTable(6);
            table.setWidths(new float[]{3f, 2f, 1.5f, 2f, 1.5f, 2f});
            table.setWidthPercentage(100);
            table.setSpacingBefore(15f);

            Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
            Color headerBackground = new Color(60, 60, 60);

            for (String header : List.of(t.get("Nazwa"), t.get("Typ"), t.get("Ilość"), t.get("Jednostka"), t.get("Cena/jedn."), t.get("Wartość"))) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header, headerFont));
                headerCell.setBackgroundColor(headerBackground);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(6f);
                table.addCell(headerCell);
            }

            for(EstimateItemResponse item : estimate.items()) {
                table.addCell(createCell(t.getOrDefault(item.name(),item.name()), Element.ALIGN_LEFT));
                table.addCell(createCell(translateItemType(item.itemType(), t), Element.ALIGN_LEFT));
                table.addCell(createCell(item.quantity().toString(), Element.ALIGN_RIGHT));
                table.addCell(createCell(translateUnit(item.unit(), t), Element.ALIGN_LEFT));
                table.addCell(createCell(item.pricePerUnit().toString(), Element.ALIGN_RIGHT));
                table.addCell(createCell(item.lineTotal().setScale(2, RoundingMode.HALF_UP).toString(), Element.ALIGN_RIGHT));
            }

            document.add(table);
            document.add(new Paragraph(t.get("Suma") + " : " + estimate.totalAmount().setScale(2, RoundingMode.HALF_UP) + " EUR"));
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Błąd generowania PDF", e);
        }
        return outputStream.toByteArray();
    }

    private String translateItemType(String itemType, Map<String,String> t) {
        String polish = switch (itemType) {
            case "MATERIAL" -> "Materiał";
            case "LABOR" -> "Robocizna";
            default -> itemType;
        };
        return t.getOrDefault(polish, polish);
    }

    private final String translateUnit (String unit, Map<String,String> t) {
        String polish = switch (unit) {
            case "METER" -> "m";
            case "SQUARE_METER" -> "m²";
            case "CUBIC_METER" -> "m³";
            case "SACK" -> "worek/wiadro";
            case "HOUR" -> "godz.";
            default -> unit;
        };
        return t.getOrDefault(polish, polish);
    }
    private PdfPCell createCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(6f);
        return cell;
    }
}
