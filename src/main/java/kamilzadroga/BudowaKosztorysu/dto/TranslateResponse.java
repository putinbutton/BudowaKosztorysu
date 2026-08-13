package kamilzadroga.BudowaKosztorysu.dto;

import java.util.List;

public record TranslateResponse(TranslateData data) {
    public record TranslateData(List<Translation> translations) {}
    public record Translation(String translatedText) {}
}
