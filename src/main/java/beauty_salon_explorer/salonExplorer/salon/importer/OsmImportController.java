package beauty_salon_explorer.salonExplorer.salon.importer;

import beauty_salon_explorer.salonExplorer.salon.Salon;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/osm")
@RequiredArgsConstructor
public class OsmImportController {

    private final OsmOverpassClient osmOverpassClient;
    private final OsmSalonImportService osmSalonImportService;

    @GetMapping("/raw-salons")
    public OsmResponse fetchRawSalonsFromOsm() {
        return osmOverpassClient.fetchWarsawBeautySalons();
    }

    @GetMapping("/mapped-salons")
    public List<Salon> fetchMappedSalonsFromOsm() {
        return osmSalonImportService.importWarsawBeautySalons();
    }
}