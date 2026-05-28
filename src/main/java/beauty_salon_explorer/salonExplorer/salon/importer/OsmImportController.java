package beauty_salon_explorer.salonExplorer.salon.importer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/osm")
@RequiredArgsConstructor
public class OsmImportController {

    private final OsmOverpassClient osmOverpassClient;

    @GetMapping("/salons")
    public OsmResponse fetchSalonsFromOsm() {
        return osmOverpassClient.fetchWarsawBeautySalons();
    }
}