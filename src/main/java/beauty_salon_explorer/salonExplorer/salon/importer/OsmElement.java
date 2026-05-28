package beauty_salon_explorer.salonExplorer.salon.importer;

import java.util.Map;

public record OsmElement(
        String type,
        Long id,
        Double lat,
        Double lon,
        OsmCenter center,
        Map<String, String> tags
) {
}