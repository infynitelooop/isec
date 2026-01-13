package com.infyniteloop.runningroom.crew.service.impl;

import com.infyniteloop.runningroom.crew.dto.CrewRequest;
import com.infyniteloop.runningroom.crew.dto.CrewResponse;
import com.infyniteloop.runningroom.crew.entity.Crew;
import com.infyniteloop.runningroom.crew.mapper.CrewMapper;
import com.infyniteloop.runningroom.crew.repository.CrewRepository;
import com.infyniteloop.runningroom.crew.service.CrewService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrewServiceImpl implements CrewService {

    private final CrewRepository crewRepository;

    private final CrewMapper crewMapper;


    public CrewServiceImpl(CrewRepository crewRepository, CrewMapper crewMapper) {
        this.crewRepository = crewRepository;
        this.crewMapper = crewMapper;
    }

    @Override
    public CrewResponse createCrew(CrewRequest dto) {
        Crew crew = crewMapper.toEntity(dto);
        crew = crewRepository.save(crew);
        return crewMapper.toDto(crew);
    }

    @Override
    public CrewResponse getCrew(String crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew not found"));
        return crewMapper.toDto(crew);
    }

    @Override
    public List<CrewResponse> getAllCrews() {
        return crewRepository.findAll().stream()
                .map(crewMapper::toDto)
                .toList();
    }

    @Override
    public CrewResponse updateCrew(String crewId, CrewRequest dto) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew not found"));
        crewMapper.updateEntityFromDto(dto, crew);
        crew = crewRepository.save(crew);
        return crewMapper.toDto(crew);
    }

    @Override
    public void deleteCrew(String crewId) {
        crewRepository.deleteById(crewId);
    }


    @Transactional
    @Override
    public void importFromCsv(MultipartFile file) {
        List<Crew> crews = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord record : csvParser) {
                Crew crew = new Crew();

                // Map CSV → Entity
                crew.setCrewId(record.get("CREW_ID_V"));
                crew.setName(record.get("NAME_V"));
                crew.setFathersName(record.get("FATHER_NAME_V"));
                crew.setGender(record.get("GENDER_V"));

                // Date format in CSV is assumed to be YYYY-MM-DD (ISO_LOCAL_DATE), Truncate time if present
                String birthDate = record.get("BIRTH_DATE_D");
                if (birthDate != null && birthDate.length() >= 10) {
                    birthDate = birthDate.substring(0, 10);
                }

                // Convert birthDate to LocalDate
                LocalDate localBirthDate = LocalDate.parse(record.get("BIRTH_DATE_D"));

                crew.setDateOfBirth(localBirthDate);
                crew.setMobileNumber(record.get("MOBILE_NO_N"));
                crew.setAddress(record.get("CALLSERV_ADD_V"));
                crew.setPermanentAddress(record.get("PERMANENT_ADD_V"));
                crew.setMaritalStatus(record.get("MARITAL_STTS_V"));
                crew.setBloodGroup(record.get("BLOOD_GROUP_C"));
                crew.setEmergencyContactNumber(record.get("PERSONAL_MOBL_N"));
                crew.setDesignation(record.get("CREW_DESIG_V"));
                crew.setCrewType(record.get("CREW_TYPE_C"));
                crew.setOrgType(record.get("ORG_TYPE_C"));
                crew.setHqCode(record.get("HQ_CODE_C"));
                crew.setCadre(record.get("CREW_CADRE_V"));

                // Skip empty crewIds (bad data)
                if (crew.getCrewId() != null && !crew.getCrewId().isBlank()) {
                    crews.add(crew);
                }

                crews.add(crew);
            }

            crewRepository.saveAll(crews);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage(), e);
        }
    }
}
