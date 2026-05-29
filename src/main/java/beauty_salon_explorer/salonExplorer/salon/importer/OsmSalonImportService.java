package beauty_salon_explorer.salonExplorer.salon.importer;

import beauty_salon_explorer.salonExplorer.salon.Salon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}