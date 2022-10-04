package fcode.backend.management.controller;

import fcode.backend.management.model.dto.PositionDTO;
import fcode.backend.management.model.response.Response;
import fcode.backend.management.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/position")
public class PositionController {
    @Autowired
    PositionService positionService;

    @GetMapping(value = "/all")
    public Response<List<PositionDTO>> getAllPosition() {
        return positionService.getAllPositions();
    }

    @GetMapping(value = "/id/{id}")
    public Response<PositionDTO> getPositionById(@PathVariable Integer id) {
        return positionService.getPositionById(id);
    }

    @PostMapping(value = "/new")
    public Response<Void> createPosition(@RequestBody PositionDTO positionDTO) {
        return positionService.createPosition(positionDTO);
    }

    @PutMapping
    public Response<Void> updatePosition(@RequestBody PositionDTO positionDTO) {
        return positionService.updatePosition(positionDTO);
    }

    @PutMapping(value = "/enroll")
    public Response<Void> enrollMembertoPosition(@RequestParam Integer memberId, @RequestParam Integer positionId) {
        return positionService.assignMemberToPosition(memberId, positionId);
    }

    @DeleteMapping(value = "/id/{id}")
    public Response<Void> deletePosition(@PathVariable Integer id) {
        return positionService.deletePosition(id);
    }
}
