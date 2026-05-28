package beauty_salon_explorer.salonExplorer.salon;

import beauty_salon_explorer.salonExplorer.salon.dto.SalonDetailsResponse;
import beauty_salon_explorer.salonExplorer.salon.dto.SalonListItemResponse;
import beauty_salon_explorer.salonExplorer.salon.dto.UpdateSalonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    @GetMapping
    public List<SalonListItemResponse> getSalons(
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String service
    ) {
        return salonService.getSalons(district, service);
    }

    @GetMapping("/{id}")
    public SalonDetailsResponse getSalon(@PathVariable Long id) {
        return salonService.getSalon(id);
    }

    @PatchMapping("/{id}")
    public SalonDetailsResponse updateSalon(
            @PathVariable Long id,
            @RequestBody UpdateSalonRequest request
    ) {
        return salonService.updateSalon(id, request);
    }

    @GetMapping("/districts")
    public List<String> getDistricts() {
        return salonService.getDistricts();
    }

    @GetMapping("/services")
    public List<String> getServices() {
        return salonService.getServices();
    }
}