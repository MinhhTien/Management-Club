package fcode.backend.management.controller;

import fcode.backend.management.model.dto.PlusPointDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.PlusPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/pluspoint")
public class PlusPointController {
    @Autowired
    PlusPointService plusPointService;

    @GetMapping(value = "/all")
    public Response<List<PlusPointDTO>> getAllPlusPoint() {
        return plusPointService.getAllPlusPoints();
    }

    @GetMapping(value = "/id/{id}")
    public Response<PlusPointDTO> getPlusPointById(@PathVariable Integer id) {
        return plusPointService.getPlusPointById(id);
    }

    @GetMapping(value = "/memberId/{memberId}")
    public Response<List<PlusPointDTO>> getByMemberId(@PathVariable Integer memberId) {
        return plusPointService.getByMemberId(memberId);
    }

    @GetMapping(value = "/period")
    public Response<List<PlusPointDTO>> getByPeriodTime(@RequestBody Integer id, Date date1, Date date2) {
        return plusPointService.getByMemberIdBetweenTime(id, date1, date2);
    }

    @PostMapping(value = "/new")
    public Response<Void> createPlusPoint(@RequestBody PlusPointDTO plusPointDTO) {
        return plusPointService.createPlusPoint(plusPointDTO);
    }

    @PutMapping
    public Response<Void> updatePlusPoint(@RequestBody PlusPointDTO plusPointDTO) {
        return plusPointService.updatePlusPoint(plusPointDTO);
    }

    @DeleteMapping(value = "/{id}")
    public Response<Void> deletePlusPoint(@PathVariable Integer id) {
        return plusPointService.deletePlusPoint(id);
    }
}
