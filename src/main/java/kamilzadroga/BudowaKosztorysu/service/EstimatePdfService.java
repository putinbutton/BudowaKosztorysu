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
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
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
                "metr", "m²", "m³", "worek/wiadro","godzin", "Firma", "Adres", "Telefon"
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
            table.setWidthPercentage(100);
            table.setSpacingBefore(15f);

            table.addCell(t.get("Nazwa"));
            table.addCell(t.get("Typ"));
            table.addCell(t.get("Jednostka"));
            table.addCell(t.get("Ilość"));
            table.addCell(t.get("Cena/jedn."));
            table.addCell(t.get("Wartość"));

            for(EstimateItemResponse item : estimate.items()) {
                table.addCell(t.getOrDefault(item.name(),item.name()));
                table.addCell(translateItemType(item.itemType(), t));
                table.addCell(translateUnit(item.unit(), t));
                table.addCell(item.quantity().toString());
                table.addCell(item.pricePerUnit().toString());
                table.addCell(item.lineTotal().setScale(2, RoundingMode.HALF_UP).toString());
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
            case "METER" -> "metr";
            case "SQUARE_METER" -> "m²";
            case "CUBIC_METER" -> "m³";
            case "SACK" -> "worek/wiadro";
            case "HOUR" -> "godzin";
            default -> unit;
        };
        return t.getOrDefault(polish, polish);
    }
}
