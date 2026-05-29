package beauty_salon_explorer.salonExplorer.salon.importer;

import beauty_salon_explorer.salonExplorer.salon.Salon;
import beauty_salon_explorer.salonExplorer.salon.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OsmSalonImportService {

    private final OsmOverpassClient osmOverpassClient;
    private final OsmSalonMapper osmSalonMapper;
    private final SalonDeduplicationService salonDeduplicationService;
    private final SalonRepository salonRepository;

    private List<Salon> cachedImportedSalons;

    public List<Salon> importWarsawBeautySalons() {
        if (cachedImportedSalons != null && !cachedImportedSalons.isEmpty()) {
            return cachedImportedSalons;
        }

        OsmResponse response = osmOverpassClient.fetchWarsawBeautySalons();

        if (response == null || response.elements() == null) {
            return List.of();
        }

        List<Salon> salons = response.elements()
                .stream()
                .map(element -> osmSalonMapper.mapToSalon(element, "Warsaw"))
                .flatMap(Optional::stream)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Salon::getName))
                .toList();

        cachedImportedSalons = salonDeduplicationService.deduplicate(salons);

        return cachedImportedSalons;
    }

    @Transactional
    public OsmImportResult importSalonsToDatabase() {
        List<Salon> importedSalons = importWarsawBeautySalons();

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

        return new OsmImportResult(created, updated);
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
    }
}