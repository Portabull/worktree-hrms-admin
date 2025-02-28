package com.worktree.hrms.service;

import java.io.IOException;
import java.util.Map;

public interface LogsService {

    byte[] downloadLogs(Map<String, Object> payload) throws IOException;

}
