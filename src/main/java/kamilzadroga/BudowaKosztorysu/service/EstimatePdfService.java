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

@Service
@RequiredArgsConstructor
public class EstimatePdfService {

    private final EstimateService estimateService;
    private final ProjectRepository projectRepository;

    public byte[] generatePdf(Long estimateId) {
        EstimateResponse estimate = estimateService.getById(estimateId);
        Project project = projectRepository.findById(estimate.projectId())
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(estimate.projectId()));
        Client client = project.getClient();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Kosztorys - " + estimate.projectName()));
            document.add(new Paragraph("Klient: " + client.getName()));
            document.add(new Paragraph("Data: " + estimate.creationDate()));
            // miejsce na dane użytkownika
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(15f);

            table.addCell("Nazwa");
            table.addCell("Typ");
            table.addCell("Jednostka");
            table.addCell("Ilość");
            table.addCell("Cena/jedn.");
            table.addCell("Wartość");

            for(EstimateItemResponse item : estimate.items()) {
                table.addCell(item.name());
                table.addCell(translateItemType(item.itemType()));
                table.addCell(translateUnit(item.unit()));
                table.addCell(item.quantity().toString());
                table.addCell(item.pricePerUnit().toString());
                table.addCell(item.lineTotal().setScale(2, RoundingMode.HALF_UP).toString());
            }
            //Istotna rzecz - waluta jest Stringiem, jeżeli chcemy w PLN trzeba to zmienić ręcznie
            document.add(table);
            document.add(new Paragraph("Suma: " + estimate.totalAmount().setScale(2, RoundingMode.HALF_UP) + " EUR"));

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Błąd generowania PDF", e);
        }
        return outputStream.toByteArray();
    }

    private String translateItemType(String itemType) {
        return switch (itemType) {
            case "MATERIAL" -> "Materiał";
            case "LABOR" -> "Robocizna";
            default -> itemType;
        };
    }

    private String translateUnit (String unit) {
        return switch (unit) {
            case "METER" -> "metr";
            case "SQUARE_METER" -> "m²";
            case "CUBIC_METER" -> "m³";
            case "SACK" -> "worek/wiadro";
            default -> unit;
        };
    }
}
