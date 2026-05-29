package beauty_salon_explorer.salonExplorer.salon.importer;

import beauty_salon_explorer.salonExplorer.salon.Salon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public List<Salon> importWarsawBeautySalons() {
        List<Salon> salons = Arrays.stream(WarsawDistrict.values())
                .flatMap(district -> importDistrictSalons(district).stream())
                .sorted(Comparator.comparing(Salon::getName))
                .toList();

        return salonDeduplicationService.deduplicate(salons);
    }

    private List<Salon> importDistrictSalons(WarsawDistrict district) {
        OsmResponse response = osmOverpassClient.fetchBeautySalonsByDistrict(district);

        if (response == null || response.elements() == null) {
            return List.of();
        }

        return response.elements()
                .stream()
                .map(element -> osmSalonMapper.mapToSalon(element, district.displayName()))
                .flatMap(Optional::stream)
                .filter(Objects::nonNull)
                .toList();
    }
}