package kamilzadroga.BudowaKosztorysu.dto;

import java.util.List;

public record TranslateRequest(
        List<String> q,
        String source,
        String target,
        String format
) {
}
