package beauty_salon_explorer.salonExplorer.salon;

import beauty_salon_explorer.salonExplorer.salon.dto.PageResponse;
import beauty_salon_explorer.salonExplorer.salon.dto.SalonDetailsResponse;
import beauty_salon_explorer.salonExplorer.salon.dto.SalonListItemResponse;
import beauty_salon_explorer.salonExplorer.salon.dto.UpdateSalonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PageResponse<SalonListItemResponse> getSalons(
            String district,
            String service,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = buildSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Salon> salonsPage = salonRepository.findAll(
                SalonSpecification.hasDistrict(district)
                        .and(SalonSpecification.hasService(service)),
                pageable
        );

        List<SalonListItemResponse> content = salonsPage.getContent()
                .stream()
                .map(SalonMapper::toListItemResponse)
                .toList();

        return new PageResponse<>(
                content,
                salonsPage.getNumber(),
                salonsPage.getSize(),
                salonsPage.getTotalElements(),
                salonsPage.getTotalPages()
        );
    }

    private Sort buildSort(String sortBy, String direction) {
        String safeSortBy = switch (sortBy == null ? "" : sortBy) {
            case "district" -> "district";
            case "rating" -> "rating";
            case "priceRange" -> "priceRange";
            default -> "name";
        };

        Sort.Direction safeDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(safeDirection, safeSortBy);
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