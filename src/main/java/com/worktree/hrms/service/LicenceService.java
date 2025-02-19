package com.worktree.hrms.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface LicenceService {

    Map<String, Object> uploadLicence(MultipartFile file);

    Map<String, Object> getLicence();
}
