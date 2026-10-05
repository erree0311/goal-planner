package com.goalplanner;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.ContentBlock;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.OutputConfig;
import com.anthropic.models.messages.ThinkingConfigAdaptive;
import com.anthropic.models.messages.WebSearchTool20260209;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaudeService {

    private static final String SYSTEM_PROMPT = """
        Eres un asistente que ayuda a planificar cualquier meta personal o de negocio
        (mudarse a otro pais, comprar un vehiculo, abrir un negocio, tramites legales,
        viajes, etc.).

        Cuando el usuario te de una meta:
        1. Busca en internet los requisitos y tramites actuales y especificos relacionados con esa meta.
        2. Organiza la informacion en pasos ordenados y accionables.
        3. Para cada paso incluye, cuando aplique: documentos necesarios, costos aproximados,
           tiempo estimado, y donde o como hacerlo.
        4. Al final, evalua que tan viable ves la meta y que podria faltar o ser un obstaculo.

        FORMATO DE RESPUESTA (obligatorio, sigue esto exactamente):

        Linea 1: KEYWORDS: seguido de 2 a 3 palabras clave EN INGLES separadas por espacios,
        simples y visuales, que describan la meta para buscar fotos de referencia.
        Ejemplos: "KEYWORDS: egypt pyramids", "KEYWORDS: tesla car", "KEYWORDS: miami food truck".

        Linea 2: vacia.

        Desde la linea 3 en adelante: el plan completo en HTML puro (nunca markdown).
        Usa solamente estas etiquetas: <h2>, <h3>, <p>, <strong>, <ul>, <ol>, <li>, <table>,
        <thead>, <tbody>, <tr>, <th>, <td>. No incluyas <html>, <head>, <body>, ni bloques
        de codigo. No uses **, #, ni ningun simbolo de markdown.

        Responde siempre en espanol (excepto las KEYWORDS, que van en ingles).
        """;

    private final AnthropicClient client = AnthropicOkHttpClient.fromEnv();

    public GoalPlan planGoal(String goal) {
        MessageCreateParams params = MessageCreateParams.builder()
            .model("claude-opus-5-5")
            .maxTokens(8000L)
            .system(SYSTEM_PROMPT)
            .thinking(ThinkingConfigAdaptive.builder().build())
            .outputConfig(OutputConfig.builder().effort(OutputConfig.Effort.HIGH).build())
            .addTool(WebSearchTool20260209.builder().build())
            .addUserMessage(goal)
            .build();

        Message response = client.messages().create(params);

        StringBuilder rawText = new StringBuilder();
        for (ContentBlock block : response.content()) {
            block.text().ifPresent(t -> rawText.append(t.text()));
        }

        return parse(rawText.toString());
    }

    private GoalPlan parse(String raw) {
        String trimmed = raw.trim();
        String keywords = "";
        String html = trimmed;

        if (trimmed.startsWith("KEYWORDS:")) {
            int newlineIdx = trimmed.indexOf('\n');
            if (newlineIdx > 0) {
                keywords = trimmed.substring("KEYWORDS:".length(), newlineIdx).trim();
                html = trimmed.substring(newlineIdx).trim();
            }
        }

        return new GoalPlan(html, buildImageUrls(keywords));
    }

    private List<String> buildImageUrls(String keywords) {
        String source = keywords.isBlank() ? "adventure goals" : keywords;
        String tags = Arrays.stream(source.replace(",", " ").trim().split("\\s+"))
            .limit(3)
            .map(w -> URLEncoder.encode(w, StandardCharsets.UTF_8))
            .collect(Collectors.joining(","));

        return List.of(
            "https://loremflickr.com/900/600/" + tags + "?lock=1",
            "https://loremflickr.com/900/600/" + tags + "?lock=2",
            "https://loremflickr.com/900/600/" + tags + "?lock=3"
        );
    }
}
