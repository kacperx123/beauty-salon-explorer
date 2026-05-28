package beauty_salon_explorer.salonExplorer.salon;

import beauty_salon_explorer.salonExplorer.salon.dto.SalonDetailsResponse;
import beauty_salon_explorer.salonExplorer.salon.dto.SalonListItemResponse;
import beauty_salon_explorer.salonExplorer.salon.dto.UpdateSalonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SalonService {

    private final SalonRepository salonRepository;

    @Transactional(readOnly = true)
    public List<SalonListItemResponse> getSalons(String district, String service) {
        return salonRepository.findAll()
                .stream()
                .filter(salon -> district == null || district.isBlank()
                        || salon.getDistrict().equalsIgnoreCase(district))
                .filter(salon -> service == null || service.isBlank()
                        || salon.getServices().stream().anyMatch(item -> item.equalsIgnoreCase(service)))
                .sorted(Comparator.comparing(Salon::getName))
                .map(SalonMapper::toListItemResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SalonDetailsResponse getSalon(Long id) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new SalonNotFoundException(id));

        return SalonMapper.toDetailsResponse(salon);
    }

    @Transactional
    public SalonDetailsResponse updateSalon(Long id, UpdateSalonRequest request) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new SalonNotFoundException(id));

        if (request.name() != null) {
            salon.setName(request.name());
        }

        if (request.address() != null) {
            salon.setAddress(request.address());
        }

        if (request.district() != null) {
            salon.setDistrict(request.district());
        }

        if (request.phoneNumber() != null) {
            salon.setPhoneNumber(request.phoneNumber());
        }

        if (request.websiteUrl() != null) {
            salon.setWebsiteUrl(request.websiteUrl());
        }

        if (request.services() != null) {
            salon.setServices(request.services());
        }

        if (request.priceRange() != null) {
            salon.setPriceRange(request.priceRange());
        }

        if (request.rating() != null) {
            salon.setRating(request.rating());
        }

        if (request.reviewCount() != null) {
            salon.setReviewCount(request.reviewCount());
        }

        return SalonMapper.toDetailsResponse(salon);
    }

    @Transactional(readOnly = true)
    public List<String> getDistricts() {
        return salonRepository.findAll()
                .stream()
                .map(Salon::getDistrict)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getServices() {
        return salonRepository.findAll()
                .stream()
                .flatMap(salon -> salon.getServices().stream())
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();
    }
}