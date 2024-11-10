package com.worktree.hrms.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.ConfigurationDao;
import com.worktree.hrms.entity.ServerConfigEntity;
import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.utils.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ConfigurationDaoImpl implements ConfigurationDao {

    @Autowired
    private HibernateUtils hibernateUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> saveEmailConfiguration(Map<String, Object> payload) {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    "serverConfigName", CommonConstants.ServerConfig.EMAIL_CONFIG);
            if (serverConfig == null) {
                serverConfig = new ServerConfigEntity();
            }
            serverConfig.setServerConfigName(CommonConstants.ServerConfig.EMAIL_CONFIG);
            serverConfig.setServerConfiguration(objectMapper.writeValueAsString(payload));
            hibernateUtils.saveOrUpdateEntity(serverConfig);
        } catch (Exception e) {
            throw new BadRequestException("Something went wrong please try after sometime");
        }
        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public Map<String, Object> getEmailConfiguration() {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    "serverConfigName", CommonConstants.ServerConfig.EMAIL_CONFIG);
            if (serverConfig == null) {
                return getEmptyEmailResponse();
            } else {
                return objectMapper.readValue(serverConfig.getServerConfiguration(),
                        new TypeReference<>() {
                        });
            }
        } catch (Exception e) {
            return getEmptyEmailResponse();
        }
    }


    @Override
    public Map<String, Object> saveAIConfiguration(Map<String, Object> payload) {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    "serverConfigName", CommonConstants.ServerConfig.AI_CONFIG);
            if (serverConfig == null) {
                serverConfig = new ServerConfigEntity();
            }
            serverConfig.setServerConfigName(CommonConstants.ServerConfig.AI_CONFIG);
            serverConfig.setServerConfiguration(objectMapper.writeValueAsString(payload));
            hibernateUtils.saveOrUpdateEntity(serverConfig);
        } catch (Exception e) {
            throw new BadRequestException("Something went wrong please try after sometime");
        }
        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public Map<String, Object> getAIConfiguration() {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    "serverConfigName", CommonConstants.ServerConfig.AI_CONFIG);
            if (serverConfig == null) {
                return getEmptyAIResponse();
            } else {
                return objectMapper.readValue(serverConfig.getServerConfiguration(),
                        new TypeReference<>() {
                        });
            }
        } catch (Exception e) {
            return getEmptyAIResponse();
        }
    }

    private Map<String, Object> getEmptyAIResponse() {
        Map<String, Object> aiPayload = new HashMap<>();
        aiPayload.put("provider", "openai");
        aiPayload.put("key", "");
        aiPayload.put("model", "");
        aiPayload.put("version", "");
        return aiPayload;
    }


    private Map<String, Object> getEmptyEmailResponse() {
        Map<String, Object> emailConfigResponse = new HashMap<>();
        emailConfigResponse.put("emailHost", "");
        emailConfigResponse.put("emailPort", "");
        emailConfigResponse.put("username", "");
        emailConfigResponse.put("password", "");
        emailConfigResponse.put("emailFrom", "");
        emailConfigResponse.put("additionalProperties", Arrays.asList());
        return emailConfigResponse;
    }
}
