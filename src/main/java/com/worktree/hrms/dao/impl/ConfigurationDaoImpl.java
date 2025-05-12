package com.worktree.hrms.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.CommonDao;
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
    private CommonDao commonDao;

    @Autowired
    private HibernateUtils hibernateUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SERVER_CONFIG_NAME = "serverConfigName";

    private static final String PROVIDER = "provider";
    private static final String ADDITIONAL_PROPS = "additionalProperties";

    @Override
    public Map<String, Object> saveEmailConfiguration(Map<String, Object> payload) {
        try {

            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class, SERVER_CONFIG_NAME
                    , CommonConstants.ServerConfig.EMAIL_CONFIG);
            if (serverConfig == null) {
                serverConfig = new ServerConfigEntity();
            }
            serverConfig.setServerConfigName(CommonConstants.ServerConfig.EMAIL_CONFIG);
            serverConfig.setServerConfiguration(objectMapper.writeValueAsString(payload));
            hibernateUtils.saveOrUpdateEntity(serverConfig);
        } catch (Exception e) {
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        }
        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public Map<String, Object> getEmailConfiguration() {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    SERVER_CONFIG_NAME, CommonConstants.ServerConfig.EMAIL_CONFIG);
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
                    SERVER_CONFIG_NAME, CommonConstants.ServerConfig.AI_CONFIG);
            if (serverConfig == null) {
                serverConfig = new ServerConfigEntity();
            }
            serverConfig.setServerConfigName(CommonConstants.ServerConfig.AI_CONFIG);
            serverConfig.setServerConfiguration(objectMapper.writeValueAsString(payload));
            hibernateUtils.saveOrUpdateEntity(serverConfig);
        } catch (Exception e) {
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        }
        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public Map<String, Object> getAIConfiguration() {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    SERVER_CONFIG_NAME, CommonConstants.ServerConfig.AI_CONFIG);
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

    @Override
    public Map<String, Object> saveMobileConfiguration(Map<String, Object> payload) {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    SERVER_CONFIG_NAME, CommonConstants.ServerConfig.MOBILE_CONFIG);
            if (serverConfig == null) {
                serverConfig = new ServerConfigEntity();
            }
            serverConfig.setServerConfigName(CommonConstants.ServerConfig.MOBILE_CONFIG);
            serverConfig.setServerConfiguration(objectMapper.writeValueAsString(payload));
            hibernateUtils.saveOrUpdateEntity(serverConfig);
        } catch (Exception e) {
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        }
        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public Map<String, Object> getMobileConfiguration() {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    SERVER_CONFIG_NAME, CommonConstants.ServerConfig.MOBILE_CONFIG);
            if (serverConfig == null) {
                return getEmptyMobileResponse();
            } else {
                return objectMapper.readValue(serverConfig.getServerConfiguration(),
                        new TypeReference<>() {
                        });
            }
        } catch (Exception e) {
            return getEmptyMobileResponse();
        }
    }

    @Override
    public Map<String, Object> saveStorageConfiguration(Map<String, Object> payload) {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    SERVER_CONFIG_NAME, CommonConstants.ServerConfig.STORAGE_CONFIG);
            if (serverConfig == null) {
                serverConfig = new ServerConfigEntity();
            }
            serverConfig.setServerConfigName(CommonConstants.ServerConfig.STORAGE_CONFIG);
            serverConfig.setServerConfiguration(objectMapper.writeValueAsString(payload));
            hibernateUtils.saveOrUpdateEntity(serverConfig);
        } catch (Exception e) {
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        }
        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public Map<String, Object> getStorageConfiguration() {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    SERVER_CONFIG_NAME, CommonConstants.ServerConfig.STORAGE_CONFIG);
            if (serverConfig == null) {
                return getEmptyStorageResponse();
            } else {
                return objectMapper.readValue(serverConfig.getServerConfiguration(),
                        new TypeReference<>() {
                        });
            }
        } catch (Exception e) {
            return getEmptyStorageResponse();
        }
    }

    @Override
    public Map<String, Object> saveProxyConfiguration(Map<String, Object> payload) {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    SERVER_CONFIG_NAME, CommonConstants.ServerConfig.PROXY_CONFIG);
            if (serverConfig == null) {
                serverConfig = new ServerConfigEntity();
            }
            serverConfig.setServerConfigName(CommonConstants.ServerConfig.PROXY_CONFIG);
            serverConfig.setServerConfiguration(objectMapper.writeValueAsString(payload));
            hibernateUtils.saveOrUpdateEntity(serverConfig);
        } catch (Exception e) {
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        }
        return CommonConstants.SUCCESS_RESPONSE;
    }

    @Override
    public Map<String, Object> getProxyConfiguration() {
        try {
            ServerConfigEntity serverConfig = hibernateUtils.findEntityByCriteria(ServerConfigEntity.class,
                    SERVER_CONFIG_NAME, CommonConstants.ServerConfig.PROXY_CONFIG);
            if (serverConfig == null) {
                return getEmptyProxyResponse();
            } else {
                return objectMapper.readValue(serverConfig.getServerConfiguration(),
                        new TypeReference<>() {
                        });
            }
        } catch (Exception e) {
            return getEmptyProxyResponse();
        }
    }

    private Map<String, Object> getEmptyProxyResponse() {
        Map<String, Object> emptyProxyResponse = new HashMap<>();
        emptyProxyResponse.put("proxyHttpsHost", "");
        emptyProxyResponse.put("proxyHttpsPort", "");
        emptyProxyResponse.put("proxyHttpsUserName", "");
        emptyProxyResponse.put("proxyHttpsPassword", "");
        emptyProxyResponse.put("proxyHttpHost", "");
        emptyProxyResponse.put("proxyHttpPort", "");
        emptyProxyResponse.put("proxyHttpUserName", "");
        emptyProxyResponse.put("proxyHttpPassword", "");
        emptyProxyResponse.put("proxyHttpEnabled", false);
        return emptyProxyResponse;
    }

    private Map<String, Object> getEmptyStorageResponse() {
        Map<String, Object> emptyMobileResponse = new HashMap<>();
        emptyMobileResponse.put(PROVIDER, "aws3");
        emptyMobileResponse.put("bucketName", "");
        emptyMobileResponse.put("secretKey", "");
        emptyMobileResponse.put("accessKey", "");
        emptyMobileResponse.put("localFileLocation", "");
        emptyMobileResponse.put(ADDITIONAL_PROPS, Arrays.asList());
        emptyMobileResponse.put("awsRegion", "");
        return emptyMobileResponse;
    }

    private Map<String, Object> getEmptyMobileResponse() {
        Map<String, Object> emptyMobileResponse = new HashMap<>();
        emptyMobileResponse.put("sid", "");
        emptyMobileResponse.put(PROVIDER, "twilio");
        emptyMobileResponse.put("token", "");
        emptyMobileResponse.put("mobile", "");
        emptyMobileResponse.put("whatsapp", "");
        emptyMobileResponse.put(ADDITIONAL_PROPS, Arrays.asList());
        return emptyMobileResponse;
    }

    private Map<String, Object> getEmptyAIResponse() {
        Map<String, Object> emptyAIResponse = new HashMap<>();
        emptyAIResponse.put(PROVIDER, "openai");
        emptyAIResponse.put("key", "");
        emptyAIResponse.put("model", "");
        emptyAIResponse.put("version", "");
        return emptyAIResponse;
    }


    private Map<String, Object> getEmptyEmailResponse() {
        Map<String, Object> emptyEmailResponse = new HashMap<>();
        emptyEmailResponse.put("emailHost", "");
        emptyEmailResponse.put("emailPort", "");
        emptyEmailResponse.put("username", "");
        emptyEmailResponse.put("password", "");
        emptyEmailResponse.put("emailFrom", "");
        emptyEmailResponse.put(ADDITIONAL_PROPS, Arrays.asList());
        return emptyEmailResponse;
    }
}
