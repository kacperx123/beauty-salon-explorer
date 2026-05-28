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

    public List<Salon> importWarsawBeautySalons() {
        OsmResponse response = osmOverpassClient.fetchWarsawBeautySalons();

        if (response == null || response.elements() == null) {
            return List.of();
        }

        return response.elements()
                .stream()
                .map(osmSalonMapper::mapToSalon)
                .flatMap(Optional::stream)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Salon::getName))
                .toList();
    }
}