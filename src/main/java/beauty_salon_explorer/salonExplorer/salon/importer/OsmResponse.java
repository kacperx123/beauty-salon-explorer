package beauty_salon_explorer.salonExplorer.salon.importer;

import java.util.List;

public record OsmResponse(
        List<OsmElement> elements
) {
}