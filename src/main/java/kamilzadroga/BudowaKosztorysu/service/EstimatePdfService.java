package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.EstimateItemResponse;
import kamilzadroga.BudowaKosztorysu.dto.EstimateResponse;
import kamilzadroga.BudowaKosztorysu.exception.BudowaKosztorysuNotFoundException;
import kamilzadroga.BudowaKosztorysu.model.Client;
import kamilzadroga.BudowaKosztorysu.model.Project;
import kamilzadroga.BudowaKosztorysu.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.openpdf.text.DocumentException;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import org.openpdf.text.Document;
import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;
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

    public byte[] generatePdf(Long estimateId, String lang) {
        EstimateResponse estimate = estimateService.getById(estimateId);
        Project project = projectRepository.findById(estimate.projectId())
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(estimate.projectId()));
        Client client = project.getClient();

        List<String> labels = new ArrayList<>(List.of(
                "Kosztorys", "Klient", "Data", "Nazwa", "Typ", "Jednostka",
                "Ilość", "Cena/jedn.", "Wartość", "Suma", "Materiał", "Robocizna",
                "metr", "m²", "m³", "worek/wiadro"
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

            document.add(new Paragraph(t.get("Kosztorys") + " - "  + estimate.projectName()));
            document.add(new Paragraph(t.get("Klient") + " : " + client.getName()));
            document.add(new Paragraph(t.get("Data") + " : " + estimate.creationDate()));
            // miejsce na dane użytkownika
            document.add(new Paragraph(" "));
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

    private final String translateItemType(String itemType, Map<String,String> t) {
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
            default -> unit;
        };
        return t.getOrDefault(polish, polish);
    }
}
