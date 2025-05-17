package com.worktree.hrms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.dao.LicenceDao;
import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.service.LicenceService;
import com.worktree.hrms.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LicenceServiceImpl implements LicenceService {

    private final LicenceDao licenceDao;

    @Override
    public Map<String, Object> uploadLicence(MultipartFile file) {

        try {
            StringBuilder content = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                throw new BadRequestException("Invalid Licence");
            }

            Map<String, Object> response = new ObjectMapper().readValue(content.toString(), Map.class);

            if (DateUtils.isDateExceeded(response.get("licenseValidTill").toString(), DateUtils.LICENSE_DATE_FORMAT)) {
                throw new BadRequestException("You uploaded Expired Licence file");
            }

            return licenceDao.uploadLicence(content.toString());

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Invalid Licence");
        }
    }

    @Override
    public Map<String, Object> getLicence() {
        return licenceDao.getLicence();
    }


}
