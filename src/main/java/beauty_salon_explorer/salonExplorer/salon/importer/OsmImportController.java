package beauty_salon_explorer.salonExplorer.salon.importer;

import beauty_salon_explorer.salonExplorer.salon.Salon;
import beauty_salon_explorer.salonExplorer.salon.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/osm")
@RequiredArgsConstructor
public class OsmImportController {

    private final SalonRepository salonRepository;
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

    private void updateExistingSalon(Salon existingSalon, Salon importedSalon) {
        existingSalon.setName(importedSalon.getName());
        existingSalon.setAddress(importedSalon.getAddress());
        existingSalon.setDistrict(importedSalon.getDistrict());
        existingSalon.setPhoneNumber(importedSalon.getPhoneNumber());
        existingSalon.setWebsiteUrl(importedSalon.getWebsiteUrl());
        existingSalon.setServices(importedSalon.getServices());
        existingSalon.setPriceRange(importedSalon.getPriceRange());
        existingSalon.setRating(importedSalon.getRating());
        existingSalon.setReviewCount(importedSalon.getReviewCount());
        existingSalon.setSource(importedSalon.getSource());
        existingSalon.setLatitude(importedSalon.getLatitude());
        existingSalon.setLongitude(importedSalon.getLongitude());

        salonRepository.save(existingSalon);
    }

    @PostMapping("/import")
    public String importSalonsToDatabase() {
        List<Salon> importedSalons = osmSalonImportService.importWarsawBeautySalons();

        int created = 0;
        int updated = 0;

        for (Salon importedSalon : importedSalons) {
            Optional<Salon> existingSalon = salonRepository.findByExternalId(importedSalon.getExternalId());

            if (existingSalon.isPresent()) {
                updateExistingSalon(existingSalon.get(), importedSalon);
                updated++;
                continue;
            }

            salonRepository.save(importedSalon);
            created++;
        }

        return "Created salons: " + created + ", updated salons: " + updated;
    }
}